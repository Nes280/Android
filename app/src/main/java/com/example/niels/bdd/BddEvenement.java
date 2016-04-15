package com.example.niels.bdd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by azaz1 on 10/04/2016.
 */
public class BddEvenement {

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

    private SQLiteDatabase bdd;

    private BddProjet bddProjet;

    public BddEvenement(Context context){
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
