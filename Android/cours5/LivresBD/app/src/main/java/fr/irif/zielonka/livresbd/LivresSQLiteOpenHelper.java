package fr.irif.zielonka.livresbd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zielonka on 10/10/2016.
 */


public class LivresSQLiteOpenHelper extends SQLiteOpenHelper {
    private static int VERSION = 3;
    private static LivresSQLiteOpenHelper instance;
    private static String BOOKSBD = "BooksBD";
    private String author_table = "create table author_table( " +
            " nom varchar(30) not null," +
            " _id integer primary key "       +
    ");";
    private String book_table = "create table book_table (" +
            " title varchar(50) not null,"                   +
            " author_id int references author_table ,"          +
            " _id integer primary key "        +
    ");";


    public static LivresSQLiteOpenHelper getInstance(Context context){
        if( instance == null ){
            instance = new LivresSQLiteOpenHelper(context);
        }
        return instance;
    }

    private LivresSQLiteOpenHelper(Context context) {
        super(context, BOOKSBD, null, VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(author_table);
        db.execSQL(book_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("drop table if exists book_table ;");
            db.execSQL("drop table if exists author_table ;");
            onCreate(db);
        }
    }
}
