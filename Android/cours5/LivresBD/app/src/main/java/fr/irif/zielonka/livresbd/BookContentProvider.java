package fr.irif.zielonka.livresbd;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class BookContentProvider extends ContentProvider {
    private static final String LOG = "BookContentProvider";
    private String authority = "fr.irif.zielonka.bookcontentprovider";
    private LivresSQLiteOpenHelper helper;
    private static final int AUTHOR = 1;
    private static final int BOOK = 2;
    private static final int AUTHOR_TITLE = 3;
    private static final int ONE_BOOK = 4;
    private static final int BOOKS_OF_ONE_AUTHOR = 5;
    private static final int ONE_AUTHOR = 6;
    private static final int LIKE = 7;
    //private static final String[] path = new String[]{"author_table", "book_table"};

    private final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

    {
        matcher.addURI(authority, "author_table", AUTHOR);
        matcher.addURI(authority, "book_table", BOOK);
        matcher.addURI(authority, "author/title", AUTHOR_TITLE);
        matcher.addURI(authority, "book_table/#", ONE_BOOK);
        matcher.addURI(authority, "author_table/#", ONE_AUTHOR);
        matcher.addURI(authority, "book_table/author/#", BOOKS_OF_ONE_AUTHOR);
        matcher.addURI(authority, "book_table/like",LIKE);

    }

    public BookContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int code = matcher.match(uri);
        int i;
        long id;
        Log.d(LOG, "delete uri=" + uri.toString());
        switch (code) {
            case ONE_BOOK:
                id = ContentUris.parseId(uri);
                i = db.delete("book_table", "_id=" + id, null);
                break;
            case ONE_AUTHOR:
                id = ContentUris.parseId(uri);
                i = db.delete("book_table", "author_id=" + id, null);
                i = db.delete("author_table", "_id =" + id, null);
                break;
            default:
                throw new UnsupportedOperationException("This delete not yet implemented");
        }
        return i;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int code = matcher.match(uri);
        Log.d(LOG, "Uri=" + uri.toString());
        long id = 0;
        String path;
        switch (code) {
            case AUTHOR:
                id = db.insert("author_table", null, values);
                path = "author_table";
                break;
            case BOOK:
                id = db.insert("book_table", null, values);
                path = "book_table";
                break;
            default:
                throw new UnsupportedOperationException("this insert not yet implemented");
        }
        Uri.Builder builder = (new Uri.Builder())
                .authority(authority)
                .appendPath(path);

        return ContentUris.appendId(builder, id).build();

    }

    @Override
    public boolean onCreate() {
        helper = LivresSQLiteOpenHelper.getInstance(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.d("provider","start query");
        SQLiteDatabase db = helper.getReadableDatabase();
        int code = matcher.match(uri);
        Cursor cursor;
        switch (code) {
            case AUTHOR:

                cursor = db.query("author_table", projection, selection,
                        selectionArgs, null, null, sortOrder);

                break;
            case BOOK:
                cursor = db.query("book_table", projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case AUTHOR_TITLE:
                cursor = db.rawQuery("SELECT book_table._id as _id, nom, title " +
                                " FROM author_table, book_table " +
                                " where author_table._id = book_table.author_id"
                        , selectionArgs);
                break;
            case BOOKS_OF_ONE_AUTHOR:
                long id = ContentUris.parseId(uri);
                cursor = db.query("book_table", new String[]{"_id", "title"},
                        "author_id = " + id, null, null, null, null);
                break;
            case LIKE :
                Log.d("Provider",selection);
                cursor = db.query("book_table", new String[]{"_id", "title"},
                        selection, null, null, null, null);
                break;
            default:
                Log.d("Uri provider def=", uri.toString());
                throw new UnsupportedOperationException("this query is not yet implemented  " +
                        uri.toString());
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
