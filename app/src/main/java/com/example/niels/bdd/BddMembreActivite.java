package com.example.niels.bdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elsa on 10/04/2016.
 */
public class BddMembreActivite {

    private static final String TABLE_ACTIVITE_MEMBRE = "membreActivite";
    private static final String COLONNE_ID_USER_MEMBRE = "idUser";
    private static final String COLONNE_ID_ACTIVITE_MEMBRE = "idActivite";
    private static final String COLONNE_DATE_INSCRIPTION_MEMBRE = "dateInscriptionMembre";


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

    public void addMembreActivite(MembreActivite m){
        ContentValues values = new ContentValues();
        values.put(COLONNE_ID_ACTIVITE_MEMBRE, m.get_idActivite());
        values.put(COLONNE_ID_USER_MEMBRE, m.get_idUtilisateur());
        values.put(COLONNE_DATE_INSCRIPTION_MEMBRE, m.get_dateInscription());
        bdd.insert(TABLE_ACTIVITE_MEMBRE, null, values);
    }

    public List<MembreActivite> getMembreByIdUser(int idUser){
        List<MembreActivite> membreList = new ArrayList<MembreActivite>();
        bdd = bddProjet.getReadableDatabase();

        String whereClause = COLONNE_ID_USER_MEMBRE + " = ? ";
        String[] whereArgs = new String[] {
                idUser + ""
        };
        Cursor cursor = bdd.query(TABLE_ACTIVITE_MEMBRE,
                new String[]{COLONNE_ID_USER_MEMBRE, COLONNE_ID_ACTIVITE_MEMBRE, COLONNE_DATE_INSCRIPTION_MEMBRE},
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

        if (cursor.moveToFirst()){
            do{
                MembreActivite ma = new MembreActivite();
                ma.set_idUtilisateur(Integer.parseInt(cursor.getString(0)));
                ma.set_idActivite(Integer.parseInt(cursor.getString(1)));
                ma.set_dateInscription(cursor.getString(2));
                membreList.add(ma);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return membreList;
    }

    public List<MembreActivite> getAllMembreActivite(){
        List<MembreActivite> membreList = new ArrayList<MembreActivite>();
        String query = "select * from " + TABLE_ACTIVITE_MEMBRE;
        Cursor cursor = bdd.rawQuery(query, null);
        if (cursor.moveToFirst()){
            do{
                MembreActivite ma = new MembreActivite();
                ma.set_idUtilisateur(Integer.parseInt(cursor.getString(0)));
                ma.set_idActivite(Integer.parseInt(cursor.getString(1)));
                ma.set_dateInscription(cursor.getString(2));
                membreList.add(ma);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return membreList;
    }

}
