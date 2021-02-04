package fr.uge.confroid.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
<<<<<<< HEAD
import android.util.Log;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
=======

import java.sql.Blob;
import java.sql.Connection;
>>>>>>> refs/remotes/origin/database

import fr.uge.confroid.configuration.Configuration;
import fr.uge.confroid.configuration.Value;


public class Database extends SQLiteOpenHelper {

    private  static final String DATABASE_NAME = "database_confroid";
    private  static final String TABLE_NAME = "configuration";
    private  static final String  COLUMN_PACKAGE_NAME = "packageName";
    private  static final String  COLUMN_VERSION = "version";
    private  static final String  COLUMN_TAG = "tag";
    private  static final String  COLUMN_DATA = "data";
    private  static final String  COLUMN_DATE = "date";

    private  static final int  DATABASE_VERSION = 1 ;
    private String tag = "tag";



    public Database(Context context, String name, CursorFactory cursorFactory, int version){
        super(context, name, cursorFactory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String request = "create table " +  TABLE_NAME + " ("
                + COLUMN_PACKAGE_NAME + " text  not null ,"
                + COLUMN_VERSION + " text not null,"
                + COLUMN_TAG + " text not null,"
                + COLUMN_DATA + " blob not null,"
                + COLUMN_DATE + " text not null,"
                + "PRIMARY KEY (" + COLUMN_PACKAGE_NAME + "," +  COLUMN_VERSION + ")"
                + ")";
        db.execSQL(request);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // update of the database structure
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
<<<<<<< HEAD
=======
    }

    public byte[] getData(){
        byte[] blob = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(" SELECT " + COLUMN_DATA + " FROM " + TABLE_NAME ,null );
        while(cursor.moveToNext())
            blob = cursor.getBlob(0);
        cursor.close();
        return blob;
>>>>>>> refs/remotes/origin/database
    }


    /**
     *Sauvegarde la configuration,name,la version,tag dans la bd
     * @param packag permet de stocker toutes les information d'une configuration
     * @return true si la sauvegarde est validée, false sinon
     */
    public boolean saveConfig(ConfroidPackage packag) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + '*' + " FROM "+TABLE_NAME+" WHERE "+ COLUMN_PACKAGE_NAME + " = " + "'"+ packag.getName() + "'" +   " and " + COLUMN_VERSION + " = " + "'" +  packag.getVersion() +"'" , null);
        if (cursor.moveToFirst() == true){
            return false;
        }
        values.put(COLUMN_PACKAGE_NAME, packag.getName());
        values.put(COLUMN_VERSION, packag.getVersion());
        db.execSQL("UPDATE " +  TABLE_NAME + " SET " + COLUMN_TAG + " = " + "''" + " WHERE " +  COLUMN_TAG + " = " +  "'" +packag.getTag() + "'");
        values.put(COLUMN_TAG, packag.getTag());
        //TODO voir comment on mieux stocker la config
        values.put(COLUMN_DATA, packag.getConfig().toJson());
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        values.put(COLUMN_DATE, date);
        this.getWritableDatabase().insert(TABLE_NAME, null, values);
        return true;
    }


    /** Fonction annexe qui permet de crée un ConfroidPackage à partir d'un cursor
     * @param cursor contient le résultat d'une requete SQL
     * @return un ConfroidPackage (configuration,name,la version,tag)
     */
    private  ConfroidPackage createConfig(Cursor cursor){
        String configeName  = cursor.getString(0);
        String configVersion  = cursor.getString(1);
        String configTag  = cursor.getString(2);
        java.lang.String  jsonCongig =  cursor.getString(3);
       // Log.i("ollll",jsonCongig);
        Value val = Configuration.fromJson(jsonCongig).getContent();
        Configuration configContent = new  Configuration(val);
        return new ConfroidPackage(configeName , configVersion, configContent , configTag);
    }


    /**
     * Fonction qui permet de renvoyer ConfroidPackage en focntion des paramètres donnés
     * @param name répresente le nom de la Configuration
     * @param version répresente la version  de la Configuration
     * @return un Optionnal<ConfroidPackage>  car la Config n'est peut etre pas stockée en bd
     */
    public Optional<ConfroidPackage> getConfig(String name, String version){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + '*' + " FROM "+TABLE_NAME+" WHERE "+ COLUMN_PACKAGE_NAME + " = " + "'" + name + "'" +  " and " + COLUMN_VERSION + " = " + "'" + version +"'" , null);
        if (cursor != null){
            cursor.moveToFirst();
            ConfroidPackage confroidPackage = createConfig(cursor);
           return Optional.of(confroidPackage);
        }
        return Optional.empty();
    }


    //TODO
    /**
     *
     * @param name
     * @param version
     * @param callback
     */
    public void getConfigAsync(String name, String version, Consumer<ConfroidPackage> callback){

    }

    /*

    /**
     * Renvoie toutes les configs correspondant au nom donné
     * @param name correpond au nom de la config
     * @return une liste de toutes les configs.
     */
    public List<ConfroidPackage> getAllConfigs(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + '*' + " FROM "+TABLE_NAME+" WHERE "+ COLUMN_PACKAGE_NAME + " = " + "'"+name +"'", null);
        List<ConfroidPackage> listConfroidPackage = new ArrayList<>();
        if (cursor.moveToFirst() ) {
            do {
                ConfroidPackage confroidPackage = createConfig(cursor);
                listConfroidPackage.add(confroidPackage);
            } while (cursor.moveToNext());
        }
        return listConfroidPackage;
    }


    //TODO
    /**
     *
     * @param name
     * @param callback
     */
    public void getAllConfigsAsync(String name, Consumer<List<ConfroidPackage>> callback){

    }




    /**
     * Récupère la liste de tous les noms de config enregistrés dans la bdd
     * @return une liste des nom de Configs
     */
    public List<String> getConfigsName(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_PACKAGE_NAME + " FROM "+TABLE_NAME, null);
       //og.i("oooo","SELECT " + COLUMN_PACKAGE_NAME + " FROM "+TABLE_NAME);
        List<String> listPackageName = new ArrayList<>();
        if (cursor.moveToFirst() ) {
            do {

                String name = cursor.getString(0);
                if (!listPackageName.contains(name))
                listPackageName.add(name);
            } while (cursor.moveToNext());
        }
        return listPackageName;
    }

    /*
    Remplace le contenu de la config (config.content) pour une config donnée déjà existante.
    « Version » correspond soit à un tag soit au numero d’une version comme la methode getConfig(), on regarde d’abord si c’est un tag. */

    /**
     *permet de mettre à jour une configuration.
     * @param name  de la config
     * @param version  de la congig
     * @param config nouvelle  config à stocker
     */
    public void updateConfig(String name, String version, Configuration config){
        SQLiteDatabase db = this.getReadableDatabase();
        Log.i("oooooo","UPDATE " +  TABLE_NAME + " SET " + COLUMN_DATA + " = " + "'" + config.toJson() + "'" +" WHERE "+ COLUMN_PACKAGE_NAME + " = " + "'" + name + "'" +  " and " + COLUMN_VERSION + " = " + "'"+ version +  "'");
        //db.execSQL(  "UPDATE " +  TABLE_NAME + " SET " + COLUMN_DATA + " = " + "'" + config.toJson() + "'" +" WHERE "+ COLUMN_PACKAGE_NAME + " = " + "'" + name + "'" +  " and " + COLUMN_VERSION + " = " + "'"+ version +  "'");
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATA, config.toJson());
        db.update(TABLE_NAME,values,COLUMN_PACKAGE_NAME + " = ? and " + COLUMN_VERSION + " = ?",   new String[] {name,version} );
    }

}


