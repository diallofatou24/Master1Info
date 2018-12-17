package fr.irif.zielonka.livresbd;


import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

public class AfficherAuteurs extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {
    private String authority;
    private Spinner spinner;
    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afficher_auteurs);
        authority = getResources().getString(R.string.authority);

        spinner = (Spinner) findViewById(R.id.spinner);
        adapter = new SimpleCursorAdapter(AfficherAuteurs.this,
                android.R.layout.simple_spinner_item, null,
                new String[]{"nom"},
                new int[]{android.R.id.text1}, 0);
        spinner.setAdapter(adapter);

        LoaderManager manager = getLoaderManager();
        manager.initLoader(0, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri;
        Uri.Builder builder = new Uri.Builder();
        uri = builder.scheme("content")
                .authority(authority)
                .appendPath("author_table")
                .build();
        return new CursorLoader(this, uri, new String[]{"_id", "nom"},
                null, null, null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
