package fr.irif.zielonka.livresbd;

import android.app.LoaderManager;

import android.content.ContentUris;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

/* application avec une bases de données, un ContentProvider et CursorLoader
 * pour faire les requêtes SELECT (méthode query() de ContentProvider.
  *
  * TODO :
  * Dans l'activité AfficherLivres les titres disparaissent
  * quand on change l'orientation de l'appareil.
  * Faire en sorte que les données persistent
  * avec un Fragment sans interface graphique.
 */


public class AfficherLivres extends AppCompatActivity {

    private String authority;

    private Spinner spinner;
    private SimpleCursorAdapter spinnerAdapter;
    private SimpleCursorAdapter listAdapter;
    private ListView titre;
    private long spinnerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afficher_livres);
        authority = getResources().getString(R.string.authority);
        spinner = (Spinner) findViewById(R.id.spinner);


        spinnerAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_spinner_item, null,
                new String[]{"nom"},
                new int[]{android.R.id.text1}, 0);
        spinner.setAdapter(spinnerAdapter);

        titre = (ListView) findViewById(R.id.list);
        listAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1, null,
                new String[]{"title"},
                new int[]{android.R.id.text1}, 0);
        titre.setAdapter(listAdapter);



        getLoaderManager().initLoader(0, null,
                new LoaderManager.LoaderCallbacks<Cursor>() {
                    @Override
                    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                        Uri uri;
                        Uri.Builder builder = new Uri.Builder();
                        uri = builder.scheme("content")
                                .authority(authority)
                                .appendPath("author_table")
                                .build();
                        return new CursorLoader(getApplicationContext(), uri, new String[]{"_id", "nom"},
                                null, null, null);
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                        spinnerAdapter.swapCursor(data);
                        spinnerAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onLoaderReset(Loader<Cursor> loader) {
                        spinnerAdapter.swapCursor(null);
                        spinnerAdapter.notifyDataSetChanged();
                    }
                });


    }

    public void afficher(View view) {

        long id = spinner.getSelectedItemId();
        Toast.makeText(getApplicationContext(), "id=" + id, Toast.LENGTH_SHORT).show();
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("content")
                .authority(authority)
                .appendPath("book_table")
                .appendPath("author");
        builder = ContentUris.appendId(builder, id);
        final Uri uri = builder.build();
        Log.d("uri=", uri.toString());

        // Pour alimenter la liste de titres il faut utiliser
        // restartLoader() et non pas initLoader().
        // Quand on change l'auteur alors initLoader ne  reinitialise pas
        // le chargement de donnees.
        getLoaderManager().restartLoader(1, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(getApplicationContext(),
                        uri, null, null, null, null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                listAdapter.swapCursor(data);
                listAdapter.notifyDataSetChanged();
                Log.d("Afficher Livres","onLoadFinished");
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                listAdapter.swapCursor(null);
                listAdapter.notifyDataSetChanged();
                Log.d("Afficher Livres","onLoaderReset");
            }
        });
    }

}





