package com.example.niels.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.niels.Code.Hashage;
import com.example.niels.bdd.BddUser;
import com.example.niels.bdd.User;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    Intent intent =  null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Recuperer les informations
        final EditText pseudo = (EditText) findViewById(R.id.pseudo);
        final EditText password = (EditText) findViewById(R.id.password);


        //Appuie sur bouton Connexion
        ((Button) findViewById(R.id.connection)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Recupération des informations du formulaire
                String p = pseudo.getText().toString();
                String mdp = password.getText().toString();

                //Verification que l'utilisateur a rempli tous les champs
                if(p.isEmpty() || mdp.isEmpty())
                {
                    Toast.makeText(MainActivity.this, R.string.verifChamps, Toast.LENGTH_LONG).show();
                    return;
                }

                //verification du pseudo et mot de passe
                BddUser db = new BddUser(MainActivity.this);
                db.open();

                db.updateAll();

                User u = db.getUserByPseudo(p);

                //Le pseudo existe pas
                if(u == null){
                    Toast.makeText(MainActivity.this, R.string.verifExistancePseudo, Toast.LENGTH_LONG).show();
                    return;
                }

                String mdpUser = u.get_password();

                //Verification du mot de passe
                Hashage h = new Hashage();
                String mdpHash = h.computeSHAHash(mdp);

                /*Log.e("mdpUser ", mdpUser + " ");
                Log.e("mdpHash ", mdpHash + " ");*/

                if(!mdpUser.equals(mdpHash)){
                    Toast.makeText(MainActivity.this, R.string.verifConnexionPassword, Toast.LENGTH_LONG).show();
                    return;
                }

                List<User> usc = db.getAllUsers();
                Log.e("taille liste", usc.size() + "");
                for (int i = 0; i < usc.size(); i++) {
                    //Date d = u.get(i).get_date();
                    Log.e("nom ", usc.get(i).get_nom());
                    Log.e("date ", usc.get(i).get_date() + " ");
                    Log.e("isConneced ", usc.get(i).get_isConnect() + " ");
                }

                db.setIsConnected(p, 1);

                List<User> us = db.getAllUsers();
                Log.e("taille liste", us.size() + "");
                for (int i = 0; i < us.size(); i++) {
                    //Date d = u.get(i).get_date();
                    Log.e("nom ", us.get(i).get_nom());
                    Log.e("date ", us.get(i).get_date() + " ");
                    Log.e("isConneced ", us.get(i).get_isConnect() + " ");
                }

                intent = new Intent(MainActivity.this, Accueil_Utilisateur.class);
                startActivity(intent);
            }
        });


        //Appuie sur bouton Inscription
        ((Button) findViewById(R.id.registration)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //on va à l'activité inscription
                intent =  new Intent(MainActivity.this, Inscription.class);
                startActivity(intent);

            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
