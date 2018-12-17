package fr.irif.zielonka.livresbd;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SimpleCursorAdapter;

public class Searchable extends ListActivity {
    String authority;
    private SimpleCursorAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_searchable);
        //setContentView(android.R.layout.simple_list_item_1);
        authority = getResources().getString(R.string.authority);
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doSearch(query);
        }

        listAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1, null,
                new String[]{"title"},
                new int[]{android.R.id.text1}, 0);
        setListAdapter(listAdapter);

    }

    void doSearch(String query) {
        Log.d("searchable", "doSearch");
        Log.d("Searchable", query);
        Uri.Builder builder = new Uri.Builder();

        builder.scheme("content")
                .authority(authority)
                .appendPath("book_table")
                .appendPath("like");
        //builder = ContentUris.appendId(builder, id);
        final Uri uri = builder.build();
        Log.d("uri=", uri.toString());
        Bundle bundle = new Bundle();
        bundle.putString("query", query);
        // Pour alimenter la liste de titres il faut utiliser
        // restartLoader() et non pas initLoader().
        // Quand on change l'auteur alors initLoader ne  reinitialise pas
        // le chargement de donnees.
        getLoaderManager().restartLoader(3, bundle,
                new LoaderManager.LoaderCallbacks<Cursor>() {
                    @Override
                    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                        String query = args.getString("query");
                        Log.d("OnCreateLoader", query);
                        String q = "title like '%" + query + "%'";
                        Log.d("LoaderQQ", q);
                        return new CursorLoader(getApplicationContext(),
                                uri, null, q, null, null);
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                        listAdapter.swapCursor(data);
                        listAdapter.notifyDataSetChanged();
                        Log.d("Searchable", "onLoadFinished");
                        if (data == null)
                            Log.d("Searchable", "data null");
                        else
                            Log.d("SearchableCount", "" + data.getCount());
                    }

                    @Override
                    public void onLoaderReset(Loader<Cursor> loader) {
                        listAdapter.swapCursor(null);
                        listAdapter.notifyDataSetChanged();
                        Log.d("Searchable", "onLoaderReset");
                    }
                });
    }
}
