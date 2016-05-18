package com.example.niels.Code;

/**
 * Created by Niels on 18/05/2016.
 */
public class Commentaires {
    //private int color;
    private String auteur;
    private String commentaire;

    public Commentaires( String auteur, String commentaire) {
        //this.color = color;
        this.auteur = auteur;
        this.commentaire = commentaire;
    }

    public String getAuteur() {
        return auteur;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }
}
