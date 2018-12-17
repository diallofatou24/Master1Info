package fr.irif.zielonka.livresbd;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

/*************************************************
 * ajoute un livre pour un auteur a selectionner
 * dans spinner
 */
public class AjouterTitre extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>{
    private String authority ;
    private Spinner spinner;
    private SimpleCursorAdapter adapter;
    private EditText titre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_titre);
        authority = getResources().getString(R.string.authority);
        spinner = (Spinner) findViewById(R.id.auteur);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_spinner_item, null,
                new String[]{"nom"},
                new int[]{android.R.id.text1}, 0);
        spinner.setAdapter(adapter);

        LoaderManager manager = getLoaderManager();
        manager.initLoader(0, null, this);

        titre = (EditText)findViewById(R.id.titre);


    }

    public void ajouter(View view){
        Editable editable = titre.getText();
        String title = editable.toString();
        if(title.contentEquals("")){
            Toast.makeText(this,"titre vide",Toast.LENGTH_SHORT).show();
            return;
        }

        long id = spinner.getSelectedItemId();

        ContentResolver resolver = getContentResolver();

        ContentValues values = new ContentValues();
        values.put("author_id",id);
        values.put("title",title);

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("content").authority(authority)
                .appendPath("book_table");
        Uri uri = builder.build();
        uri = resolver.insert(uri,values);
        editable.clear();

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
        Button ajouter = (Button) findViewById(R.id.ajouter);
        ajouter.setActivated(true);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
