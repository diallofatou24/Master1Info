package fr.irif.zielonka.livresbd;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Livres extends AppCompatActivity {
    Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livres);
        Button titre = (Button) findViewById(R.id.titre);

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        titre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        AjouterTitre.class);
                startActivity(intent);
            }
        });
    }
    public void afficherLivres(View view){
        Intent intent = new Intent(this, AfficherLivres.class);
        startActivity(intent);
    }
    public void ajouter(View view){
        Intent intent = new Intent(this,AjouterLivresEtAuteurs.class);
        startActivity(intent);
    }
    public void getAuthors(View view){
        Intent intent = new Intent(this,AfficherAuteurs.class);
        startActivity(intent);
    }
    public void supprimerLivres(View view){
        Intent intent = new Intent(this,SupprimerLivres.class);
        startActivity(intent);
    }
    public void supprimerLivresAuteur(View view){
        Intent intent = new Intent(this,SupprimerLivreAuteur.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.livres, menu);
        MenuItem searchItem = menu.findItem(R.id.search_item);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if ( searchItem != null ){
            Log.d("Livres","searchItem");
            searchView = (SearchView) searchItem.getActionView();
        }
        if ( searchView != null ){
            Log.d("livres","searchView");

            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(this.getComponentName()));

            /*
            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(new ComponentName("fr.irif.zielonka.livresbd","Searchable")));
                    */
        }
        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch( id ) {
            case R.id.livreauteur:
                intent = new Intent(this, AjouterLivresEtAuteurs.class);
                startActivity(intent);
                return true;
            case R.id.livre:
                intent = new Intent(this,
                        AjouterTitre.class);
                startActivity(intent);
                return true;
            case R.id.suplivreauteur:
                intent = new Intent(this, SupprimerLivreAuteur.class);
                startActivity(intent);
                return true;
            case R.id.suplivre:
                intent = new Intent(this, SupprimerLivres.class);
                startActivity(intent);
                return true;
            case R.id.quitter:
                mToolbar.collapseActionView();
                mToolbar.dismissPopupMenus();
                Livres.this.finish();
                //return super.onOptionsItemSelected(item);
                return true;
            /*
            case R.id.search_item:
                return onSearchRequested();
                */
            default:
                //return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        //super.onDestroy();
        if( mToolbar != null )
        mToolbar.dismissPopupMenus();
        super.onDestroy();
    }
}
