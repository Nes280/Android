package com.example.niels.bdd;

import java.util.Date;

/**
 * Created by Elsa on 10/04/2016.
 */
public class Activite {

    private int _idActivite;
    private String _nomActivite;
    private int _idUtilisateur;
    private String _dateCreation;
    private int _type;

    public Activite(){}

    public Activite(String nom, int idUtilisateur, String date, int type)
    {
        this.set_nomActivite(nom);
        this.set_idUtilisateur(idUtilisateur);
        this.set_dateCreation(date);
        this.set_type(type);
    }

    public Activite(int i, String nom, int idUtilisateur, String date, int type)
    {
        this.set_idActivite(i);
        this.set_nomActivite(nom);
        this.set_idUtilisateur(idUtilisateur);
        this.set_dateCreation(date);
        this.set_type(type);
    }


    public int get_idActivite() {
        return _idActivite;
    }

    public void set_idActivite(int _idActivite) {
        this._idActivite = _idActivite;
    }

    public String get_nomActivite() {
        return _nomActivite;
    }

    public void set_nomActivite(String _nomActivite) {
        this._nomActivite = _nomActivite;
    }

    public int get_idUtilisateur() {
        return _idUtilisateur;
    }

    public void set_idUtilisateur(int _idUtilisateur) {
        this._idUtilisateur = _idUtilisateur;
    }

    public String get_dateCreation() {
        return _dateCreation;
    }

    public void set_dateCreation(String _dateCreation) {
        this._dateCreation = _dateCreation;
    }

    public int get_type() {
        return _type;
    }

    public void set_type(int _type) {
        this._type = _type;
    }
}
