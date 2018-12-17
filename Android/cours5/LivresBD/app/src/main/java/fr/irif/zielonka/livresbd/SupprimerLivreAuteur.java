package fr.irif.zielonka.livresbd;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;

public class SupprimerLivreAuteur extends AppCompatActivity {
    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback;
    private MySimpleCursorAdapter mAdapter;
    private static final String KEY = "arrayLong";
    private static final String LOG = "SupprimerLIvreAuteur";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supprimer_livre_auteur);

        mAdapter = new MySimpleCursorAdapter(this, R.layout.list_item,
                null, //pas de cursor pour l'instant
                new String[]{"nom", "title"},
                new int[]{R.id.auteur, R.id.titre}, 0);
        if (savedInstanceState != null) {
            long[] array = savedInstanceState.getLongArray(KEY);
            if (array == null) {
                Log.d(LOG, "onCreate array is null");
            } else {
                Log.d(LOG, "onCreate array size = " + array.length);
            }
            if (array != null) {
                mAdapter.setLongArray(array);
            }
        }


        ListView list = (ListView) findViewById(R.id.list);

        list.setAdapter(mAdapter);


        mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                Uri uri = (new Uri.Builder()).scheme("content")
                        .authority(SupprimerLivreAuteur.this.getResources().getString(R.string.authority))
                        .appendPath("author")
                        .appendPath("title")
                        .build();
                return new CursorLoader(SupprimerLivreAuteur.this, uri,
                        null, null, null, null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                Log.d(LOG, "cursor size onLoadFinished=" + data.getCount());
                Toast.makeText(SupprimerLivreAuteur.this, "onLoadFinished", Toast.LENGTH_SHORT).show();
                mAdapter.swapCursor(data);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                mAdapter.swapCursor(null);
            }
        };

        getLoaderManager().initLoader(0, null, mLoaderCallback);

    }

    /**************
     * end onCreate method
     **********************************/

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        long[] array = mAdapter.toLongArray();
        if (array == null) {
            Log.d(LOG, "saveInstanceState empty array");
        }
        if (array != null) {
            outState.putLongArray(KEY, array);
            Log.d(LOG, "putLongArray size=" + array.length);
        }
        super.onSaveInstanceState(outState);
    }

    public void supprimerLivres(View view) {
        long[] idArray = mAdapter.toLongArray();
        if (idArray == null) {
            Log.d(LOG, "idArray is null in supprimerLivres");
            return;
        }
        ContentResolver contentResolver = getContentResolver();

        for (long l : idArray) {
            Uri.Builder builder = new Uri.Builder();
            builder = builder.scheme("content")
                    .authority(getResources().getString(R.string.authority))
                    .path("book_table");
            Uri uri = ContentUris.appendId(builder, l).build();
            contentResolver.delete(uri, null, null);
        }
        mAdapter.clear();
        getLoaderManager().restartLoader(0, null, mLoaderCallback);
    }
    /*********************************************************************
     * definition of MySimpleCursorAdapter
     * simplifier par rapport a la version
     * montree en cours
     *********************************************************************/
    private static class MySimpleCursorAdapter extends SimpleCursorAdapter {

        //private LayoutInflater mInflater;

        /** un HashSet pour stocker les id des items "checkes" par l'utilisateur*/
        private HashSet<Long> mSetId = new HashSet<>();

        MySimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
            super(context, layout, c, from, to, flags);
            //mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        private static class Holder {
            long id;
            CheckBox checkBox;
        }

        /*transformer HashSet en long[] */
        long[] toLongArray() {
            if (mSetId.isEmpty())
                return null;
            long[] tab = new long[mSetId.size()];
            int i = 0;
            for (Long l : mSetId) {
                tab[i++] = l;
            }
            return tab;
        }

        void clear() {
            mSetId.clear();
        }

        /* transformer long[] en HashSet */
        void setLongArray(long[] array) {
            if (array == null || array.length == 0) {
                //Log.d(LOG, "void array setLongArray");
                return;
            }
            mSetId.clear();

            for (long l : array)
                mSetId.add(l);
            //Log.d(LOG, "setLongArray in adapter size=" + mSetId.size());
        }


/* creation de view laisse a la classe mere,
ici on cree un holder et on l'attache au view
 */
        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            /* laisser a la classe mere la creation du vue */
            View view = super.newView(context, cursor, parent);
            Log.d(LOG,"newView");
            //View view = mInflater.inflate(R.layout.list_item, parent, false);
            /* mettre en place le Holder */
            final Holder holder = new Holder();
            holder.checkBox = (CheckBox) view.findViewById(R.id.check);
            /* attacher le holder au vue comme tag */
            view.setTag(holder);

            /* attacher un listener pour suivre et memoriser
             * les operations sur CheckBox
             * Le listener suit les clicks de l'utlisateur,
             * si CheckBox est "checked" alors on ajoute id
             * dans mSetId, si CheckBox deselectionne alors on supprime id
              * de mSetId*/
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(LOG, "onClick");
                    CheckBox check = (CheckBox) v;
                    if(check.isChecked())
                        mSetId.add(holder.id);
                    else
                        mSetId.remove(holder.id);
                }
            });
            return view;
        }
/*ListView appelle bindView en demandant de remplir view avec les donees */
        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            Log.d(LOG,"bindView");

            /* laisser la classe mere a mettre les donnees texte dans la vue.*/
            super.bindView(view, context, cursor);
            /* Mettre a jour id et l'etat de CheckBox. */
            Holder holder = (Holder) view.getTag();
            holder.id = cursor.getLong(cursor.getColumnIndex("_id"));
            holder.checkBox.setChecked(mSetId.contains(holder.id));
        }
        /* getView - le paramètre position donne la position de view sur la liste.
           On peut utiliser, comme ici, pour changer la couleur background
           de chaque deuxième élément de la liste.
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            if( (position & 0x1) == 0  ){
                view.setBackgroundColor(Color.CYAN);
            }else{
                view.setBackgroundColor(Color.LTGRAY);
            }
            return view;
        }
    }/* end MySimpleCursorAdapter definition */

}
