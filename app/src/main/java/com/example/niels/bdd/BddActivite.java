package com.example.niels.bdd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Elsa on 07/04/2016.
 */
public class BddActivite {

    private static final String TABLE_ACTIVITE = "activite";
    private static final String COLONNE_ID_ACTIVITE = "idActivite";
    private static final String COLONNE_NOM_ACTIVITE = "nomActivite";
    private static final String COLONNE_ID_USER_ACTIVITE = "idUser";
    private static final String COLONNE_DATE_CREATION_ACTIVITE = "dateCreation";
    private static final String COLONNE_TYPE = "type";

    private SQLiteDatabase bdd;

    private BddProjet bddProjet;

    public BddActivite(Context context){
        bddProjet = new BddProjet(context);
    }

    public void open(){
        bdd = bddProjet.getWritableDatabase();
    }

    public void close(){
        bdd.close();
    }

    public SQLiteDatabase getBdd(){
        return bdd;
    }

}
