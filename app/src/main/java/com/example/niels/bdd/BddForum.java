package com.example.niels.bdd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Elsa on 10/04/2016.
 */
public class BddForum {

    private static final String TABLE_FORUM = "forum";
    private static final String COLONNE_ID_USER_FORUM = "idUser";
    private static final String COLONNE_ID_ACTIVITE_FORUM = "idActivite";
    private static final String COLONNE_COMMENTAIRE_FORUM = "commentaire";
    private static final String COLONNE_DATE_FORUM = "date";

    private SQLiteDatabase bdd;

    private BddProjet bddProjet;

    public BddForum(Context context){
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
