package com.example.niels.bdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Date.*;

/**
 * Created by Elsa on 07/04/2016.
 */
public class BddUser {

    private static final String TABLE_USER = "users";

    private static final String COLONNE_ID_USER = "id";
    private static final String COLONNE_NOM_USER = "nom";
    private static final String COLONNE_PRENOM_USER = "prenom";
    private static final String COLONNE_PSEUDO_USER = "pseudo";
    private static final String COLONNE_PASSWORD_USER = "password";
    private static final String COLONNE_DATE_USER = "date";

    private SQLiteDatabase bdd;

    private BddProjet bddProjet;

    public BddUser(Context context){
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

    public void addUser(User user){
        //bdd = this.getWritableDatabase();
        //Log.e("n", bdd.getPath());
        ContentValues values = new ContentValues();
        values.put(COLONNE_NOM_USER, user.get_nom());
        values.put(COLONNE_PRENOM_USER, user.get_prenom());
        values.put(COLONNE_PSEUDO_USER, user.get_pseudo());
        values.put(COLONNE_PASSWORD_USER, user.get_password());
        values.put(COLONNE_DATE_USER, user.get_date().toString());

        bdd.insert(TABLE_USER, null, values);
    }


    public User getContactByPseudo(String pseudo){
        bdd = bddProjet.getReadableDatabase();

        String whereClause = COLONNE_PSEUDO_USER + " = ? ";
        String[] whereArgs = new String[] {
                pseudo
        };
        Cursor cursor = bdd.query(TABLE_USER,
                new String[]{COLONNE_ID_USER, COLONNE_NOM_USER, COLONNE_PRENOM_USER,
                            COLONNE_PSEUDO_USER, COLONNE_PASSWORD_USER, COLONNE_DATE_USER},
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
                valueOf(cursor.getString(5)));

        return user;
    }

    public List<User> getAllContact(){
        List<User> userList = new ArrayList<User>();
        String query = "select * from " + TABLE_USER;
        Cursor cursor = bdd.rawQuery(query, null);
        if (cursor.moveToFirst()){
            do{
                User user = new User();
                user.set_id(Integer.parseInt(cursor.getString(0)));
                user.set_nom(cursor.getString(1));
                user.set_prenom(cursor.getString(2));
                user.set_pseudo(cursor.getString(3));
                user.set_password(cursor.getString(4));
                //user.set_date(valueOf(cursor.));
                userList.add(user);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return userList;
    }

}
