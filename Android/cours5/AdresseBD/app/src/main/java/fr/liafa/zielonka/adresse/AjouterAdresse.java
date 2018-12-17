package fr.liafa.zielonka.adresse;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class AjouterAdresse extends AppCompatActivity {


    EditText numero, rue, ville, code;
    static final String tag = "MainActivity";
    ArrayList<Adresse> liste = new ArrayList<Adresse>();
    private SQLiteDatabase bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_adresse);

        rue = (EditText) findViewById(R.id.rue);
        numero = (EditText) findViewById(R.id.numero);
        ville = (EditText) findViewById(R.id.ville);
        code = (EditText) findViewById(R.id.code_postale);
        final MySQLiteHelper helper = new MySQLiteHelper(this);

        code.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                           @Override
                                           public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                               /* first condition for soft enter
                                                  second condition for hard enter
                                                */
                                               if ((actionId == EditorInfo.IME_ACTION_SEND)
                                                       || (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN)) {
                                                   /*
                                                   Log.d(tag,"dans if");
                                                   Toast.makeText(MainActivity.this, "send", Toast.LENGTH_LONG);
                                                   */
                                                   TextView text = new TextView(AjouterAdresse.this);
                                                   Adresse adresse = new Adresse(numero.getText().toString(),
                                                           rue.getText().toString(), ville.getText().toString(),
                                                           code.getText().toString());


                                                   text.setText(adresse.toString());
                                                   liste.add(adresse);
                                                   helper.putAdresse(adresse);
                                                   /* add text to Layout (at the end)*/
                                                   LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
                                                   layout.addView(text);


                                                   numero.getText().clear();
                                                   rue.getText().clear();
                                                   code.getText().clear();
                                                   ville.getText().clear();
                                                   rue.requestFocus();


                                                   return true;
                                               }
                                               return false;
                                           }
                                       }
        );

    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putParcelableArrayList(MySQLiteHelper.ADRESY, liste);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        liste = savedInstanceState.getParcelableArrayList(MySQLiteHelper.ADRESY);
        LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
        for (Adresse s : liste) {
            TextView text = new TextView(AjouterAdresse.this);
            text.setText(s.toString());
            layout.addView(text);
        }

    }
}
