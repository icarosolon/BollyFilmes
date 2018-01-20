package br.com.icaro.projetos.bollyfilmes.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by whoami on 29/12/2017.
 */
//classe para trabalhar com os dados
public class FilmesProvider extends ContentProvider {
    private FilmesDBHelper dbHelper;
    private static final UriMatcher URI_MATCHER = buildUriMatcher();

    private static final int FILME = 100;
    private static final int FILME_ID = 101;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(FilmesContract.CONTENT_AUTHORITY, FilmesContract.PATH_FILES, FILME);
        uriMatcher.addURI(FilmesContract.CONTENT_AUTHORITY, FilmesContract.PATH_FILES + "/#", FILME_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {

        dbHelper = new FilmesDBHelper(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase readableDatabase = dbHelper.getReadableDatabase();

        Cursor cursor;

        switch (URI_MATCHER.match(uri)) {
            case FILME:
                cursor = readableDatabase.query(FilmesContract.FilmesEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case FILME_ID:
                selection = FilmesContract.FilmesEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(FilmesContract.FilmesEntry.getIdFromUri(uri))};
                cursor = readableDatabase.query(FilmesContract.FilmesEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("URI não identificada" + uri);

        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();

        long id;
        switch (URI_MATCHER.match(uri)) {
            case FILME:
                id = writableDatabase.insert(FilmesContract.FilmesEntry.TABLE_NAME, null, values);
                if (id == -1) {
                    return null;
                }
                break;
            default:
                throw new IllegalArgumentException("URI não identificada" + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return FilmesContract.FilmesEntry.buildUriForFilmes(id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();

        int delete = 0;

        switch (URI_MATCHER.match(uri)) {
            case FILME:
                delete = writableDatabase.delete(FilmesContract.FilmesEntry.TABLE_NAME, selection, selectionArgs);
            case FILME_ID:
                selection = FilmesContract.FilmesEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(FilmesContract.FilmesEntry.getIdFromUri(uri))};

                delete = writableDatabase.delete(FilmesContract.FilmesEntry.TABLE_NAME, selection, selectionArgs);
        }

        if (delete != 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return delete;

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();

        int update = 0;

        switch (URI_MATCHER.match(uri)) {
            case FILME:
                update = writableDatabase.update(FilmesContract.FilmesEntry.TABLE_NAME, values, selection, selectionArgs);
            case FILME_ID:
                selection = FilmesContract.FilmesEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(FilmesContract.FilmesEntry.getIdFromUri(uri))};

                update = writableDatabase.update(FilmesContract.FilmesEntry.TABLE_NAME, values, selection, selectionArgs);
        }

        if (update != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return update;
    }


}
