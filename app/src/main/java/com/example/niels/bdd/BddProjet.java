package com.example.niels.bdd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Elsa on 07/04/2016.
 */
public class BddProjet extends SQLiteOpenHelper {

    //Base de données
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "AndroidBD.db";

    //Table User
    private static final String TABLE_USER = "users";
    private static final String COLONNE_ID_USER = "id";
    private static final String COLONNE_NOM_USER = "nom";
    private static final String COLONNE_PRENOM_USER = "prenom";
    private static final String COLONNE_PSEUDO_USER = "pseudo";
    private static final String COLONNE_PASSWORD_USER = "password";
    private static final String COLONNE_DATE_USER = "date";

    //Requêtes de création table User
    private static final String REQUETE_CREATION_BASE_USER = "create table " + TABLE_USER +
            "(" + COLONNE_ID_USER + " integer primary key autoincrement, " +
            COLONNE_NOM_USER + " text not null, " +
            COLONNE_PRENOM_USER + " text not null, " +
            COLONNE_PSEUDO_USER + " text not null, " +
            COLONNE_PASSWORD_USER + " text not null, " +
            COLONNE_DATE_USER + " date not null);";


    //Table Activite
    private static final String TABLE_ACTIVITE = "activite";
    private static final String COLONNE_ID_ACTIVITE = "idActivite";
    private static final String COLONNE_NOM_ACTIVITE = "nomActivite";
    private static final String COLONNE_ID_USER_ACTIVITE = "idUser";
    private static final String COLONNE_DATE_CREATION_ACTIVITE = "dateCreation";
    private static final String COLONNE_TYPE = "type";

    //Requêtes de création table Activite
    private static final String REQUETE_CREATION_BASE_ACTIVITE = "create table " + TABLE_ACTIVITE +
            "(" + COLONNE_ID_ACTIVITE + " integer primary key autoincrement, " +
            COLONNE_NOM_ACTIVITE + " text not null, " +
            COLONNE_ID_USER_ACTIVITE + " integer not null, " +
            COLONNE_DATE_CREATION_ACTIVITE + " date not null, " +
            COLONNE_TYPE + " text not null);";


    //Table MembreActivite
    private static final String TABLE_ACTIVITE_MEMBRE = "membreActivite";
    private static final String COLONNE_ID_USER_MEMBRE = "idUser";
    private static final String COLONNE_ID_ACTIVITE_MEMBRE = "idActivite";

    //Requêtes de création table MembreActivite
    private static final String REQUETE_CREATION_BASE_ACTIVITE_MEMBRE = "create table " + TABLE_ACTIVITE_MEMBRE +
            "(" + COLONNE_ID_USER_MEMBRE + " integer, " +
            COLONNE_ID_ACTIVITE_MEMBRE + " integer, " +
             "primary key(" + COLONNE_ID_USER_MEMBRE +"," + COLONNE_ID_ACTIVITE_MEMBRE+"));";


    //Table Forum
    private static final String TABLE_FORUM = "forum";
    private static final String COLONNE_ID_USER_FORUM = "idUser";
    private static final String COLONNE_ID_ACTIVITE_FORUM = "idActivite";
    private static final String COLONNE_COMMENTAIRE_FORUM = "commentaire";
    private static final String COLONNE_DATE_FORUM = "date";


    //Requêtes de création table Activite
    private static final String REQUETE_CREATION_BASE_FORUM = "create table " + TABLE_FORUM +
            "(" + COLONNE_ID_USER_FORUM + " integer, " +
            COLONNE_ID_ACTIVITE_FORUM + " integer, " +
            COLONNE_COMMENTAIRE_FORUM + "text not null, "+
            COLONNE_DATE_FORUM + " date not null, "+
            "primary key(" + COLONNE_ID_USER_FORUM +"," + COLONNE_ID_ACTIVITE_FORUM+"));";


    //Manque table evenement


    //Instance de la base qui sera manipulé au travers de cette classe
    //private SQLiteDatabase maBaseDeDonnees;

    public BddProjet(Context contexte){
        super(contexte, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(REQUETE_CREATION_BASE_USER);
        db.execSQL(REQUETE_CREATION_BASE_ACTIVITE);
        db.execSQL(REQUETE_CREATION_BASE_ACTIVITE_MEMBRE);
        db.execSQL(REQUETE_CREATION_BASE_FORUM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*db.execSQL("drop table if exists " + TABLE_USER + ";");
        onCreate(db);*/
    }


}
