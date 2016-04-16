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
            COLONNE_DATE_USER + " text not null);";


    //Table Activite
    private static final String TABLE_ACTIVITE = "activite";
    private static final String COLONNE_ID_ACTIVITE = "idActivite";
    private static final String COLONNE_NOM_ACTIVITE = "nomActivite";
    private static final String COLONNE_DESCRIPTION_ACTIVITE = "descriptionActivite";
    private static final String COLONNE_ID_USER_ACTIVITE = "idUser";
    private static final String COLONNE_DATE_CREATION_ACTIVITE = "dateCreation";
    private static final String COLONNE_TYPE = "type";

    //Requêtes de création table Activite
    private static final String REQUETE_CREATION_BASE_ACTIVITE = "create table " + TABLE_ACTIVITE +
            "(" + COLONNE_ID_ACTIVITE + " integer primary key autoincrement, " +
            COLONNE_NOM_ACTIVITE + " text not null, " +
            COLONNE_DESCRIPTION_ACTIVITE + " text not null, " +
            COLONNE_ID_USER_ACTIVITE + " integer not null, " +
            COLONNE_DATE_CREATION_ACTIVITE + " date not null, " +
            COLONNE_TYPE + " integer not null);"; //0 publique 1 privee


    //Table MembreActivite
    private static final String TABLE_ACTIVITE_MEMBRE = "membreActivite";
    private static final String COLONNE_ID_USER_MEMBRE = "idUser";
    private static final String COLONNE_ID_ACTIVITE_MEMBRE = "idActivite";
    private static final String COLONNE_DATE_INSCRIPTION_MEMBRE = "dateInscriptionMembre";


    //Requêtes de création table MembreActivite
    private static final String REQUETE_CREATION_BASE_ACTIVITE_MEMBRE = "create table " + TABLE_ACTIVITE_MEMBRE +
            "(" + COLONNE_ID_USER_MEMBRE + " integer, " +
            COLONNE_ID_ACTIVITE_MEMBRE + " integer, " +
            COLONNE_DATE_INSCRIPTION_MEMBRE + " date not null, " +
            "primary key(" + COLONNE_ID_USER_MEMBRE +"," + COLONNE_ID_ACTIVITE_MEMBRE+"));";


    //Table Forum
    private static final String TABLE_FORUM = "forum";
    private static final String COLONNE_ID_USER_FORUM = "idUser";
    private static final String COLONNE_ID_ACTIVITE_FORUM = "idActivite";
    private static final String COLONNE_COMMENTAIRE_FORUM = "commentaire";
    private static final String COLONNE_DATE_FORUM = "dateCommentaire";


    //Requêtes de création table Activite
    private static final String REQUETE_CREATION_BASE_FORUM = "create table " + TABLE_FORUM +
            "(" + COLONNE_ID_USER_FORUM + " integer, " +
            COLONNE_ID_ACTIVITE_FORUM + " integer, " +
            COLONNE_COMMENTAIRE_FORUM + "text not null, "+
            COLONNE_DATE_FORUM + " date not null, "+
            "primary key(" + COLONNE_ID_USER_FORUM +"," + COLONNE_ID_ACTIVITE_FORUM+"));";


    //Table Evenement
    private static final String TABLE_EVENEMENT = "evenement";
    private static final String COLONNE_ID_EVENEMENT = "idEvenement";
    private static final String COLONNE_ID_USER_EVENEMENT = "idUser";
    private static final String COLONNE_ID_ACTIVITE_EVENEMENT = "idActivite";
    private static final String COLONNE_LATITUDE_EVENEMENT = "latitude";
    private static final String COLONNE_LONGITUDE_EVENEMENT = "longitude";
    private static final String COLONNE_NOM_EVENEMENT = "nomEvenement";
    private static final String COLONNE_DESCRIPTION_EVENEMENT = "descriptionEvenement";
    private static final String COLONNE_PHOTO_EVENEMENT = "photoEvenement";
    private static final String COLONNE_DATE_EVENEMENT = "dateCommentaire";


    //Requêtes de création table Activite
    private static final String REQUETE_CREATION_BASE_EVENEMENT = "create table " + TABLE_EVENEMENT +
            "(" + COLONNE_ID_EVENEMENT + " integer primary key autoincrement, " +
            COLONNE_ID_USER_EVENEMENT + " integer not null, " +
            COLONNE_ID_ACTIVITE_EVENEMENT + " integer not null, " +
            COLONNE_LATITUDE_EVENEMENT + " double, " +
            COLONNE_LONGITUDE_EVENEMENT + " double, " +
            COLONNE_NOM_EVENEMENT + "text not null, "+
            COLONNE_DESCRIPTION_EVENEMENT + "text not null, "+
            COLONNE_PHOTO_EVENEMENT + "text, "+
            COLONNE_DATE_EVENEMENT + " date not null "+
            ");";

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
        db.execSQL(REQUETE_CREATION_BASE_EVENEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*db.execSQL("drop table if exists " + TABLE_USER + ";");
        onCreate(db);*/
    }


}
