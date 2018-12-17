package fr.irif.zielonka.livresbd;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActivityChooserView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SupprimerLivres extends AppCompatActivity {
    private String authority ;
    private ListView list;
    private Spinner spinner;
    private SimpleCursorAdapter adapterSpinner, adapterList;
    private LoaderManager.LoaderCallbacks<Cursor> callbackTitres;
    private long id_author;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        long[] ids = list.getCheckedItemIds();
        if (ids.length == 0)
            return;
        ArrayList<Integer> idesArray = new ArrayList<>();
        for (long id : ids) {
            idesArray.add(new Integer((int) id));

        }
        outState.putIntegerArrayList("ids", idesArray);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ArrayList<Integer> idesArray = savedInstanceState.getIntegerArrayList("ids");
        if(idesArray == null)
            return;
        /*
        adapterList.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {


                return false;
            }
        });
        */

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supprimer_livres);
        authority = getResources().getString(R.string.authority);
        list = (ListView) findViewById(R.id.list);
        spinner = (Spinner) findViewById(R.id.spinner);
        adapterSpinner = new SimpleCursorAdapter(this,
                android.R.layout.simple_spinner_item,
                null, /*Cursor - null initialement */
                new String[]{"nom"},
                new int[]{android.R.id.text1}, 0);
        spinner.setAdapter(adapterSpinner);

        adapterList = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_checked,
                null,/* Cursor null initalement */
                new String[]{"title"},
                new int[]{android.R.id.text1}, 0);
        list.setAdapter(adapterList);

        /* definir Callback pour les titres */
        callbackTitres = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id_loader, Bundle args) {
                Uri.Builder builder = (new Uri.Builder()).scheme("content")
                        .authority(authority)
                        .appendPath("book_table")
                        .appendPath("author");
                ContentUris.appendId(builder,id_author);
                return new CursorLoader(SupprimerLivres.this, builder.build(),
                        new String[]{"_id", "title"},
                        "author_id=" + id_author, null, null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                adapterList.swapCursor(data);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                Log.d("loader reset", "ok");
                adapterList.swapCursor(null);
            }
        };/* fin Callback pour les  titres */


        /* instaler Listener sur Spinner */
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                /* Installer Loader pour les titres du livre           *
                 * restartLoader est necessaire                        *
                 * pour recharger la liste de titre si l'auteur        *
                 * selectionne dans spinner change.                    *
                 * Variable globale id_author utlisee par callbackTitres
                 * pour determiner l'auteur de titres a afficher       *
                 */
                id_author = id;
                getLoaderManager().restartLoader(1, null, callbackTitres);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        /************  end of listener for Spinner ****************************/


        /*****     loader pour les auteurs            */
        getLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                Uri uri = (new Uri.Builder()).scheme("content")
                        .authority(authority)
                        .appendPath("author_table")
                        .build();
                return new CursorLoader(SupprimerLivres.this, uri,
                        new String[]{"_id", "nom"},
                        null, null, null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                adapterSpinner.swapCursor(data);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                adapterSpinner.swapCursor(null);
            }
        });
        /*******  end of Loadrr on Spinner *******/
    }/* fin onCreate */


    /* onClick active par le bouton supprimer livres */
    public void supprimerLivres(View view) {
        /* recuperer les ids des livres selectionnes dans
         * la liste de livres
         */
        long[] ids = list.getCheckedItemIds();
        if (ids.length == 0)
            return;

        for (long id : ids) {
            Log.d("supprimer id =", id + "");
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("content")
                    .authority(authority)
                    .appendPath("book_table");
            /* id du livre a supprimer a la fin de uri */
            ContentUris.appendId(builder, id);
            Uri uri = builder.build();

            int res = getContentResolver().delete(uri, null, null);
            Log.d("result of delete=", res + "");
        }
        /* apres la suppression de titres mettre a jour la liste
        de titres, id_author ne change pas */
        getLoaderManager().restartLoader(1, null, callbackTitres);
    }
}
