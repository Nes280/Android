package com.example.niels.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.niels.bdd.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.util.List;

import static android.util.Base64.encodeToString;



public class Inscription extends AppCompatActivity {

    Intent intent = null;
    private String SHAHash;
    public static int NO_OPTIONS=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Recuperer les informations
        final EditText prenom = (EditText) findViewById(R.id.prenom);
        final EditText nom = (EditText) findViewById(R.id.nom);
        final EditText pseudo = (EditText) findViewById(R.id.pseudo);
        final EditText password = (EditText) findViewById(R.id.mdp);
        final EditText password_conf = (EditText) findViewById(R.id.mdpConf);

        //Appuie sur bouton Inscription
        ((Button) findViewById(R.id.registration)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Recupération des info saisies
                String n = nom.getText().toString();
                String p = prenom.getText().toString();
                String ps = pseudo.getText().toString();
                String mdp = password.getText().toString();
                String mdp_c = password_conf.getText().toString();

                //Un champ n'est pas rempli
                if(n.isEmpty() || p.isEmpty() || ps.isEmpty() || mdp.isEmpty() || mdp_c.isEmpty())
                {
                    //A changer
                    Toast.makeText(Inscription.this, "Veuillez verifier les champs", Toast.LENGTH_LONG).show();
                    return;
                }

                //Les mdp ne sont pas pareils
                if(!mdp.equals(mdp_c))
                {
                    //A changer
                    Toast.makeText(Inscription.this, "Veuillez verifier les champs passwords", Toast.LENGTH_LONG).show();
                    return;
                }
                else if(mdp.length() < 5)
                {
                    //A changer
                    Toast.makeText(Inscription.this, "Le mot de passe doit comporter 5 caractères", Toast.LENGTH_LONG).show();
                    return;
                }

                //Base de données
                BddUser db = new BddUser(Inscription.this);
                db.open();

                //Verification pseudo
                User user = db.getUserByPseudo(ps);
                if(user != null )
                {
                    //A changer
                    Toast.makeText(Inscription.this, "Pseudo déjà existant", Toast.LENGTH_LONG).show();
                    return;
                }

                //Ajout dans la base de données
                Toast.makeText(Inscription.this, "Insertion", Toast.LENGTH_LONG).show();

                //Date du jours
                String format = "dd/MM/yy H:mm:ss";
                java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat(format);
                java.util.Date dateJava = new java.util.Date();
                System.out.println(formater.format(dateJava));
                Log.e("date Java ", formater.format(dateJava) + "");

                /*Date dateSql = new Date(dateJava.getTime());
                Log.e("Date Sql" , formater.format(dateSql) + "" );*/

                //Crypter le mot de passe
                computeSHAHash(mdp);
                //Log.e("mdp crypte", SHAHash + " ");

                //Ajout dans la bd
                db.addUser(new User(n, p, ps, mdp, formater.format(dateJava)));

                List<User> u = db.getAllUsers();
                Log.e("taille liste", u.size() + "");
                for(int i = 0; i < u.size(); i++)
                {
                    //Date d = u.get(i).get_date();
                    Log.e("nom " , u.get(i).get_nom());
                    Log.e("date ", u.get(i).get_date() + " ");
                }

                //on va à l'activité main
                intent = new Intent(Inscription.this, MainActivity.class);
                startActivity(intent);

            }
        });
    }

    //Convertisseur
    private static String convertToHex(byte[] data) throws java.io.IOException
    {

        StringBuffer sb = new StringBuffer();
        String hex=null;

        hex= encodeToString(data, 0, data.length, NO_OPTIONS);

        sb.append(hex);

        return sb.toString();
    }


    //Cryptage en SHA 1
    public void computeSHAHash(String password) {
        MessageDigest mdSha1 = null;
        try {
            mdSha1 = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e1) {
            Log.e("myapp", "Error initializing SHA1 message digest");
        }
        try {
            mdSha1.update(password.getBytes("ASCII"));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        byte[] data = mdSha1.digest();
        try {
            SHAHash = convertToHex(data);
        } catch (IOException ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
    }

}
