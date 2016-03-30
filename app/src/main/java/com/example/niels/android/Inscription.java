package com.example.niels.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Inscription extends AppCompatActivity {

    Intent intent = null;

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
                    Toast.makeText(Inscription.this, "Veuillez verifier les champs", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Les mdp ne sont pas pareils
                if(!mdp.equals(mdp_c))
                {
                    Toast.makeText(Inscription.this, "Veuillez verifier les champs passwords", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Verification pseudo

                //Ajout dans la base de données
                Toast.makeText(Inscription.this, "Insertion", Toast.LENGTH_LONG).show();

                DatabaseHandler db = new DatabaseHandler(Inscription.this);

                String format = "dd/MM/yy H:mm:ss";
                java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat(format);
                java.util.Date date = new java.util.Date();
                System.out.println( formater.format( date ) );
                Log.e("date ", formater.format( date ) + "" );
                db.addUser(new User(n, p, ps, mdp, date));

                //on va à l'activité main
                intent = new Intent(Inscription.this, MainActivity.class);
                startActivity(intent);

            }
        });


    }

}
