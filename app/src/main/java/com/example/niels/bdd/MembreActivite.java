package com.example.niels.bdd;

import java.util.Date;

/**
 * Created by Elsa on 10/04/2016.
 */
public class MembreActivite {

    private int _idActivite;
    private int _idUtilisateur;
    private String _dateInscription;

    public MembreActivite(){}

    public MembreActivite(int idA, int idU, String d){
        this.set_idActivite(idA);
        this.set_idUtilisateur(idU);
        this.set_dateInscription(d);
    }

    public int get_idActivite() {
        return _idActivite;
    }

    public void set_idActivite(int _idActivite) {
        this._idActivite = _idActivite;
    }

    public int get_idUtilisateur() {
        return _idUtilisateur;
    }

    public void set_idUtilisateur(int _idUtilisateur) {
        this._idUtilisateur = _idUtilisateur;
    }

    public String get_dateInscription() {
        return _dateInscription;
    }

    public void set_dateInscription(String _dateInscription) {
        this._dateInscription = _dateInscription;
    }
}
