package com.example.niels.bdd;

/**
 * Created by Elsa on 07/04/2016.
 */
public class User {

    private int _id;
    private String _nom;
    private String _prenom;
    private String _pseudo;
    private String _password;
    private String _date;
    private int _isConnect; //0 non connecté 1 connecté

    public User(){}

    public User(String nom, String prenom, String pseudo, String mdp, String d, int isConnect)
    {
        this._nom = nom;
        this._prenom = prenom;
        this._pseudo = pseudo;
        this._password = mdp;
        this._date = d;
        this.set_isConnect(isConnect);
    }

    public User(int i, String nom, String prenom, String pseudo, String mdp, String d, int isConnect)
    {
        this._id = i;
        this._nom = nom;
        this._prenom = prenom;
        this._pseudo = pseudo;
        this._password = mdp;
        this._date = d;
        this.set_isConnect(isConnect);
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int id) {
        this._id = id;
    }

    public String get_nom() {
        return _nom;
    }

    public void set_nom(String nom) {
        this._nom = nom;
    }

    public String get_prenom() {
        return _prenom;
    }

    public void set_prenom(String prenom) {
        this._prenom = prenom;
    }

    public String get_pseudo() {
        return _pseudo;
    }

    public void set_pseudo(String pseudo) {
        this._pseudo = pseudo;
    }

    public String get_password() {
        return _password;
    }

    public void set_password(String password) {
        this._password = password;
    }

    public String get_date(){
        return this._date;
    }

    public void set_date(String d){
        this._date = d;
    }

    public int get_isConnect() {
        return _isConnect;
    }

    public void set_isConnect(int _isConnect) {
        this._isConnect = _isConnect;
    }
}
