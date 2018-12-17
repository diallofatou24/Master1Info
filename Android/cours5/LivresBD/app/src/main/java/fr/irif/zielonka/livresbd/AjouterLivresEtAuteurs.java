package fr.irif.zielonka.livresbd;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AjouterLivresEtAuteurs extends AppCompatActivity {
    private String authority ;
    private Button button;
    private EditText nom;
    private EditText titre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_livres);
authority = getResources().getString(R.string.authority);
        button = (Button) findViewById(R.id.button);
        nom = (EditText) findViewById(R.id.nom);
        titre = (EditText) findViewById(R.id.titre);
    }
    public void ajouter(View view){
        String n = nom.getText().toString();
        String t = titre.getText().toString();
        ContentValues values = new ContentValues();
        values.put("nom",n);


        ContentResolver resolver = getContentResolver();
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("content").authority(authority).appendPath("author_table");
        Uri uri = builder.build();
        uri = resolver.insert(uri,values);

        long id = ContentUris.parseId(uri);
        values = new ContentValues();
        values.put("title",t);
        values.put("author_id",id);

        //builder = builder.clearQuery();
        builder = new Uri.Builder();
        builder.scheme("content").authority(authority).appendPath("book_table");
        uri = builder.build();
        uri = resolver.insert(uri,values);

        nom.getText().clear();
        titre.getText().clear();
    }
}
