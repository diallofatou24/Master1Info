package fr.liafa.zielonka.adresse;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

public class AfficherAddresse extends ListActivity {
    final MySQLiteHelper helper = new MySQLiteHelper(this);
    private Cursor cursor;

    private void afficher() {

//android.R.layout.
        cursor = helper.getAdresses();
        ListAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.list_item_layout,
                cursor,
                new String[]{MySQLiteHelper.NUMERO, MySQLiteHelper.RUE,
                        MySQLiteHelper.ZIP, MySQLiteHelper.VILLE},
                new int[]{R.id.numero, R.id.rue, R.id.zip, R.id.ville}, 0);
        setListAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_afficher_addresse);
        final MySQLiteHelper helper = new MySQLiteHelper(this);
        afficher();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        cursor.moveToPosition(position);
        int rowid = cursor.getInt(cursor.getColumnIndex("_id"));
        if (helper.delete(rowid) > 0) {
            cursor.close();
            afficher();
        }
    }
}
