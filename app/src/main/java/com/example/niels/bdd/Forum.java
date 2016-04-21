package com.example.niels.bdd;

import java.util.Date;

/**
 * Created by Elsa on 10/04/2016.
 */
public class Forum {

    private int _idActivite;
    private int _idUtilisateur;
    private String _commentaire;
    private String _dateEdit;

    public Forum(){}

    public Forum(int idA, int idU, String com, String d){
        this.set_idActivite(idA);
        this.set_idUtilisateur(idU);
        this.set_commentaire(com);
        this.set_dateEdit(d);
    }

    public int get_idUtilisateur() {
        return _idUtilisateur;
    }

    public void set_idUtilisateur(int _idUtilisateur) {
        this._idUtilisateur = _idUtilisateur;
    }

    public int get_idActivite() {
        return _idActivite;
    }

    public void set_idActivite(int _idActivite) {
        this._idActivite = _idActivite;
    }

    public String get_commentaire() {
        return _commentaire;
    }

    public void set_commentaire(String _commentaire) {
        this._commentaire = _commentaire;
    }

    public String get_dateEdit() {
        return _dateEdit;
    }

    public void set_dateEdit(String _dateEdit) {
        this._dateEdit = _dateEdit;
    }
}
