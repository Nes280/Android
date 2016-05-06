package com.example.niels.android;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.example.niels.Code.Md5;
import com.example.niels.Code.getExemple;
import com.example.niels.bdd.BddUser;
import com.example.niels.bdd.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {

    Intent intent =  null;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    String rep = null;


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
                if (p.isEmpty() || mdp.isEmpty()) {
                    Toast.makeText(MainActivity.this, R.string.verifChamps, Toast.LENGTH_LONG).show();
                    return;
                }

                //Demande de permission
                int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.INTERNET);

                int permissionCheck2 = ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_NETWORK_STATE);

                if (permissionCheck == PackageManager.PERMISSION_GRANTED && permissionCheck2 == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(Inscription.this, "Permission", Toast.LENGTH_LONG).show();
                    ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected()) {

                        //Verification du mot de passe
                        AccesBD a = new AccesBD();
                        String urlUtilisateur = "/Android/recupUtilisateur.php?pseudo="+p;
                        a.execute(urlUtilisateur);
                        try {
                            a.get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }

                        String response = rep;
                        String valeur = null;

                        String nom = null;
                        String prenom = null;
                        String pseudo = null;
                        String date = null;
                        String mdpBD = null;

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            //JSONObject newJson = jsonObject.getJSONObject("state");
                            valeur = jsonObject.getString("state");
                            Log.e("resultat json " , valeur);
                            if(valeur.equals("0")){
                                Toast.makeText(MainActivity.this, R.string.verifExistancePseudo, Toast.LENGTH_LONG).show();
                                return;
                            }
                            JSONObject utilisateur = jsonObject.getJSONObject("utilisateur");
                            nom = utilisateur.getString("nom");
                            prenom = utilisateur.getString("prenom");
                            pseudo = utilisateur.getString("pseudo");
                            mdpBD = utilisateur.getString("motDePasse");
                            date = utilisateur.getString("date");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        //verification du pseudo et mot de passe
                        BddUser db = new BddUser(MainActivity.this);
                        db.open();

                        db.updateAll();

                        User u = db.getUserByPseudo(p);
                        //Le pseudo n'existe pas il faut inserer dans BD Sqlite
                        if (u == null) {
                            Log.e("nom ", nom);
                            Log.e("prenom", prenom);
                            Log.e("pseudo", pseudo);
                            Log.e("mdp", mdpBD);
                            Log.e("date", date);
                            User utilisateur = new User(nom, prenom, pseudo,mdpBD, date, 0);
                            db.addUser(utilisateur);
                        }
                        //String mdpUser = u.get_password();

                        //Verification du mot de passe
                        /*Hashage h = new Hashage();
                        String mdpHash = h.computeMD5Hash(mdp);*/
                        Md5 m = new Md5(mdp);
                        String mdpHash = m.getCode();

                        Log.e("mdpUser ", mdpBD + " ");
                        Log.e("mdpHash ", mdpHash + " ");

                        if (!mdpBD.equals(mdpHash)) {
                            Toast.makeText(MainActivity.this, R.string.verifConnexionPassword, Toast.LENGTH_LONG).show();
                            return;
                        }

                        /*List<User> usc = db.getAllUsers();
                        Log.e("taille liste", usc.size() + "");
                        for (int i = 0; i < usc.size(); i++) {
                            //Date d = u.get(i).get_date();
                            Log.e("nom ", usc.get(i).get_nom());
                            Log.e("date ", usc.get(i).get_date() + " ");
                            Log.e("isConneced ", usc.get(i).get_isConnect() + " ");
                        }*/

                        db.setIsConnected(p, 1);

                        /*List<User> us = db.getAllUsers();
                        Log.e("taille liste", us.size() + "");
                        for (int i = 0; i < us.size(); i++) {
                            //Date d = u.get(i).get_date();
                            Log.e("nom ", us.get(i).get_nom());
                            Log.e("date ", us.get(i).get_date() + " ");
                            Log.e("isConneced ", us.get(i).get_isConnect() + " ");
                        }*/

                        db.close();

                        intent = new Intent(MainActivity.this, Accueil_Utilisateur.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, R.string.demandeDeConnexion, Toast.LENGTH_LONG).show();
                    }
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.INTERNET},
                            REQUEST_CODE_ASK_PERMISSIONS);
                    Log.e("erreur", "permission denied ");
                }
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

    private class AccesBD extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                getExemple e = new getExemple();
                String host = "http://folionielsbenichou.franceserv.com";
                //String rep = null;

                //rep = e.run(host+"/Android/nouvelUtilisateur.php?nom=" +n+"&prenom="+p+"&pseudo="+ps+"&motDePasse="+mdpHash+"&date="+date);
                rep = e.run(host+params[0]);
                Log.e("REPOSE", rep);
                //return downloadUrl(params[0]);
                return rep;
            } catch (IOException e) {
                return "Unable to retrieve web page. URL maybe invalide ";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            rep = result;
            //Log.e("rep" , " res " + result);
            //Toast.makeText(Inscription.this, "Response " + result, Toast.LENGTH_LONG).show();
        }
    }
}
