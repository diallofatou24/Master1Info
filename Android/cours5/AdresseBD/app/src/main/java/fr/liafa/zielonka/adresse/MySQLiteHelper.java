package fr.liafa.zielonka.adresse;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zielonka on 12/10/15.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {
    /* nom de BD */
    private static final String DB_NAME = "adresy.db";
    /*nom de la table*/
    public static final String ADRESY = "adresy";

    /* les attributs */
    public static final String RUE = "rue";
    public static final String ZIP = "zip";
    public static final String VILLE = "ville";
    public static final String NUMERO = "numero";

    private static int VERSION = 3;


    private static final String CREATE_ADRESSE = "create table " + ADRESY + "( " +
            "_id integer primary key autoincrement," +
            RUE + " text, " +
            ZIP + " text not null," +
            VILLE + " text not null," +
            NUMERO + " text);";

    public MySQLiteHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ADRESSE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("drop table if exists " + ADRESY);
            onCreate(db);
        }
    }

    public void putAdresse(Adresse ad) {
        SQLiteDatabase bd = getWritableDatabase();
        ContentValues row = new ContentValues();
        row.put(MySQLiteHelper.RUE, ad.getRue());
        row.put(MySQLiteHelper.ZIP, ad.getZip());
        row.put(MySQLiteHelper.VILLE, ad.getVille());
        row.put(MySQLiteHelper.NUMERO, ad.getNumero());
        bd.insert(MySQLiteHelper.ADRESY, null, row);
    }


    public int delete(int _id){
        SQLiteDatabase bd = getWritableDatabase();
        return bd.delete(ADRESY, "_id = " +  _id, null);    //new String[]{Integer.toString(rowid)});

    }

    public Cursor getAdresses() {
        SQLiteDatabase bd = getReadableDatabase();
        String[] columns = new String[]{"_id", VILLE, ZIP, RUE, NUMERO};
        return bd.query(ADRESY, columns, null, null, null, null, null);
    }

}


