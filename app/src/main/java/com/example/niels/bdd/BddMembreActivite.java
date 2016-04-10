package com.example.niels.bdd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Elsa on 10/04/2016.
 */
public class BddMembreActivite {

    private static final String TABLE_ACTIVITE_MEMBRE = "membreActivite";
    private static final String COLONNE_ID_USER_MEMBRE = "idUser";
    private static final String COLONNE_ID_ACTIVITE_MEMBRE = "idActivite";

    private SQLiteDatabase bdd;

    private BddProjet bddProjet;

    public BddMembreActivite(Context context){
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
