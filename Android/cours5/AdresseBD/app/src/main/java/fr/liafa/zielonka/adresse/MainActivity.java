package fr.liafa.zielonka.adresse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

//import android.support.v7.app.AppCompatActivity;

public class MainActivity extends Activity {

    EditText numero, rue, ville, code;
    static final String tag = "MainActivity";
    ArrayList<String> liste = new ArrayList<String>();


    public void afficher(View view){
        Intent intent = new Intent(this, AfficherAddresse.class);
        startActivity(intent);

    }
    public void ajouter(View view){
        Intent intent = new Intent(this, AjouterAdresse.class);
        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


}
