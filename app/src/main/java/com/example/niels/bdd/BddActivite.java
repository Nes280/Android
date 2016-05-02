package com.example.niels.bdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Elsa on 07/04/2016.
 */
public class BddActivite {

    private static final String TABLE_ACTIVITE = "activite";
    private static final String COLONNE_ID_ACTIVITE = "idActivite";
    private static final String COLONNE_NOM_ACTIVITE = "nomActivite";
    private static final String COLONNE_DESC_ACTIVITE = "descriptionActivite";
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


    public void addActivite(Activite activite)
    {
        ContentValues values = new ContentValues();
        values.put(COLONNE_NOM_ACTIVITE, activite.get_nomActivite());
        values.put(COLONNE_DESC_ACTIVITE, activite.get_descActivite());
        values.put(COLONNE_TYPE, activite.get_type());
        values.put(COLONNE_ID_USER_ACTIVITE, activite.get_idUtilisateur());
        values.put(COLONNE_DATE_CREATION_ACTIVITE, activite.get_dateCreation());

        bdd.insert(TABLE_ACTIVITE, null, values);
    }

    public Activite getActivitebyOwner(int idUser){
        bdd = bddProjet.getReadableDatabase();

        String whereClause = COLONNE_ID_USER_ACTIVITE + " = ? ";
        String[] whereArgs = new String[] {
                idUser+""
        };
        Cursor cursor = bdd.query(TABLE_ACTIVITE,
                new String[]{COLONNE_ID_ACTIVITE, COLONNE_NOM_ACTIVITE, COLONNE_DESC_ACTIVITE,
                        COLONNE_ID_USER_ACTIVITE, COLONNE_DATE_CREATION_ACTIVITE, COLONNE_TYPE},
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

        Activite activite = new Activite();
        activite.set_idActivite(Integer.parseInt(cursor.getString(0)));
        activite.set_nomActivite(cursor.getString(1));
        activite.set_description(cursor.getString(2));
        activite.set_idUtilisateur(Integer.parseInt(cursor.getString(3)));
        activite.set_dateCreation(cursor.getString(4));
        activite.set_type(Integer.parseInt(cursor.getString(5)));
        return activite;
    }

    public Activite getActivitebyIdActivite(int idActivite){
        bdd = bddProjet.getReadableDatabase();

        String whereClause = COLONNE_ID_ACTIVITE + " = ? ";
        String[] whereArgs = new String[] {
                idActivite+""
        };
        Cursor cursor = bdd.query(TABLE_ACTIVITE,
                new String[]{COLONNE_ID_ACTIVITE, COLONNE_NOM_ACTIVITE, COLONNE_DESC_ACTIVITE,
                        COLONNE_ID_USER_ACTIVITE, COLONNE_DATE_CREATION_ACTIVITE, COLONNE_TYPE},
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

        Activite activite = new Activite();
        activite.set_idActivite(Integer.parseInt(cursor.getString(0)));
        activite.set_nomActivite(cursor.getString(1));
        activite.set_description(cursor.getString(2));
        activite.set_idUtilisateur(Integer.parseInt(cursor.getString(3)));
        activite.set_dateCreation(cursor.getString(4));
        activite.set_type(Integer.parseInt(cursor.getString(5)));
        return activite;
    }




}
