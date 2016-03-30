package com.example.niels.android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Date;

/**
 * Created by azaz1 on 30/03/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    //Base de données
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "userBD.db";

    //Table
    private static final String TABLE_USER = "users";

    //Colonnes
    private static final String COLONNE_ID = "id";
    private static final String COLONNE_NOM = "nom";
    private static final String COLONNE_PRENOM = "prenom";
    private static final String COLONNE_PSEUDO = "pseudo";
    private static final String COLONNE_PASSWORD = "password";
    private static final String COLONNE_DATE = "date";

    //Requêtes de création
    private static final String REQUETE_CREATION_BASE = "create table " + TABLE_USER +
            "(" + COLONNE_ID + " integer primary key autoincrement, " +
            COLONNE_NOM + " text not null, " +
            COLONNE_PRENOM + " text not null, " +
            COLONNE_PSEUDO + " text not null, " +
            COLONNE_PASSWORD + " text not null, " +
            COLONNE_DATE + " date not null);";

    //Instance de la base qui sera manipulé au travers de cette classe
    private SQLiteDatabase maBaseDeDonnees;

    public DatabaseHandler(Context contexte){
        super(contexte,DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(REQUETE_CREATION_BASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_USER + ";");
        onCreate(db);
    }

    public void addUser(User user){
        maBaseDeDonnees = this.getWritableDatabase();
        Log.e("n", maBaseDeDonnees.getPath());
        ContentValues values = new ContentValues();
        values.put(COLONNE_NOM, user.get_nom());
        values.put(COLONNE_PRENOM, user.get_prenom());
        values.put(COLONNE_PSEUDO, user.get_pseudo());
        values.put(COLONNE_PASSWORD, user.get_password());
        values.put(COLONNE_DATE, user.get_date().toString());

        maBaseDeDonnees.insert(TABLE_USER, null, values);
    }


    public User getContactByPseudo(String pseudo){
        maBaseDeDonnees = this.getReadableDatabase();

        String whereClause = COLONNE_PSEUDO + " = ? ";
        String[] whereArgs = new String[] {
                pseudo
        };
        Cursor cursor = maBaseDeDonnees.query(TABLE_USER,
                new String[]{COLONNE_ID, COLONNE_NOM, COLONNE_PRENOM, COLONNE_PSEUDO, COLONNE_PASSWORD, COLONNE_DATE},
                whereClause,
                whereArgs,
                null,
                null,
                null,
                null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        if(cursor.getCount() <= 0)
            return null;

        User user = new User(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                Date.valueOf(cursor.getString(5)));

        return user;
    }


}
