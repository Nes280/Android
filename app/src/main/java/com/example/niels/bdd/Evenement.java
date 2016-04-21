package com.example.niels.bdd;

import java.util.Date;

/**
 * Created by Elsa on 10/04/2016.
 */
public class Evenement {
    private int _idEvenement;
    private int _idActivite;
    private int _idUtilisateur;
    private double _latitude;
    private double _longitude;
    private String _nomEvenements;
    private String _descriptionEvenement;
    private String _photo;
    private String _dateEvenement;

    public int get_idEvenement() {
        return _idEvenement;
    }

    public void set_idEvenement(int _idEvenement) {
        this._idEvenement = _idEvenement;
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

    public double get_latitude() {
        return _latitude;
    }

    public void set_latitude(double _latitude) {
        this._latitude = _latitude;
    }

    public double get_longitude() {
        return _longitude;
    }

    public void set_longitude(double _longitude) {
        this._longitude = _longitude;
    }

    public String get_nomEvenements() {
        return _nomEvenements;
    }

    public void set_nomEvenements(String _nomEvenements) {
        this._nomEvenements = _nomEvenements;
    }

    public String get_descriptionEvenement() {
        return _descriptionEvenement;
    }

    public void set_descriptionEvenement(String _descriptionEvenement) {
        this._descriptionEvenement = _descriptionEvenement;
    }

    public String get_photo() {
        return _photo;
    }

    public void set_photo(String _photo) {
        this._photo = _photo;
    }

    public String get_dateEvenement() {
        return _dateEvenement;
    }

    public void set_dateEvenement(String _dateEvenement) {
        this._dateEvenement = _dateEvenement;
    }
}
