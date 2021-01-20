package fr.uge.confroid.storage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Blob;
import java.sql.Connection;

import fr.uge.confroid.configuration.Configuration;

public class Database extends SQLiteOpenHelper {

    private  static final String DATABASE_NAME = "database_confroid";
    private  static final String TABLE_NAME = "configuration";
    private  static final String  COLUMN_PACKAGE_NAME = "packageName";
    private  static final String  COLUMN_VERSION = "version";
    private  static final String  COLUMN_TAG = "tag";
    private  static final String  COLUMN_DATA = "data";
    private  static final int  DATABASE_VERSION = 1 ;
    public Database(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // we create the database$

        String request = "create table " +  TABLE_NAME + "("
                + COLUMN_PACKAGE_NAME + "text  not null ,"
                + COLUMN_VERSION + "text not null,"
                + COLUMN_TAG + "text not null,"
                + COLUMN_DATA + "blob not null,"
                + "PRIMARY KEY (packageName, version)"
                + ")";
        db.execSQL(request);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // update of the database structure
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public byte[] getData(){
        byte[] blob = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(" SELECT " + COLUMN_DATA + " FROM " + TABLE_NAME ,null );
        while(cursor.moveToNext())
            blob = cursor.getBlob(0);
        cursor.close();
        return blob;
    }

    public void save(String packageName, int version,String tag, Configuration config){
        String request = "insert into configuration (" + COLUMN_PACKAGE_NAME+ "," + COLUMN_VERSION + "," + COLUMN_TAG + "," + COLUMN_DATA + ") values ('"
                + packageName + "', " + version + ", " + tag + ", "+  config+ ")";
        this.getWritableDatabase().execSQL(request);
    }

    public int delete(String packageName, int version) {
        SQLiteDatabase db =  this.getWritableDatabase();
        String where = COLUMN_PACKAGE_NAME + "=? and" + COLUMN_VERSION + "=?" ;
        String[] whereArgs = {packageName + "," + version};
        return db.delete(TABLE_NAME,where, whereArgs);
    }

    public byte[] get(String packageName, int version){
        SQLiteDatabase db =  this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT" + COLUMN_DATA + " FROM "+TABLE_NAME+" WHERE "+ COLUMN_PACKAGE_NAME + "=" + packageName +  "and" + COLUMN_VERSION + "=" + version , null);
        if (cursor != null){
            cursor.moveToFirst();
            byte[] config = cursor.getBlob(0);
            cursor.close();
            return config;
        }
        return null;
    }

}


