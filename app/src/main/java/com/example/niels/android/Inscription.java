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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.niels.bdd.*;
import com.example.niels.Code.*;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


public class Inscription extends AppCompatActivity {

    Intent intent = null;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    String n, p, ps, mdpHash, date;


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
                n = nom.getText().toString();
                p = prenom.getText().toString();
                ps = pseudo.getText().toString();
                String mdp = password.getText().toString();
                String mdp_c = password_conf.getText().toString();

                //Un champ n'est pas rempli
                if (n.isEmpty() || p.isEmpty() || ps.isEmpty() || mdp.isEmpty() || mdp_c.isEmpty()) {
                    Toast.makeText(Inscription.this, R.string.verifChamps, Toast.LENGTH_LONG).show();
                    return;
                }

                //Les mdp ne sont pas pareils
                if (!mdp.equals(mdp_c)) {
                    Toast.makeText(Inscription.this, R.string.verifPassword, Toast.LENGTH_LONG).show();
                    return;
                } else if (mdp.length() < 5) {
                    Toast.makeText(Inscription.this, R.string.verifTaillePassword, Toast.LENGTH_LONG).show();
                    return;
                }

                //Base de données
                BddUser db = new BddUser(Inscription.this);
                db.open();

                //Verification pseudo
                User user = db.getUserByPseudo(ps);
                if (user != null) {
                    //A changer
                    Toast.makeText(Inscription.this, R.string.verifPseudo, Toast.LENGTH_LONG).show();
                    return;
                }

                //Ajout dans la base de données
                //Toast.makeText(Inscription.this, "Insertion", Toast.LENGTH_LONG).show();

                //Demande de permission
                int permissionCheck = ContextCompat.checkSelfPermission(Inscription.this,
                        Manifest.permission.INTERNET);

                int permissionCheck2 = ContextCompat.checkSelfPermission(Inscription.this,
                        Manifest.permission.ACCESS_NETWORK_STATE);

                if (permissionCheck == PackageManager.PERMISSION_GRANTED && permissionCheck2 == PackageManager.PERMISSION_GRANTED) {
                    //startActivity(callintent);
                    //Toast.makeText(Inscription.this, "Permission", Toast.LENGTH_LONG).show();
                    ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected()) {
                        //Toast.makeText(Inscription.this, "connecté", Toast.LENGTH_LONG).show();
                        //Date du jours
                        String format = "dd/MM/yy H:mm:ss";
                        java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat(format);
                        java.util.Date dateJava = new java.util.Date();
                        System.out.println(formater.format(dateJava));
                        Log.e("date Java ", formater.format(dateJava) + "");

                        /*Date dateSql = new Date(dateJava.getTime());
                        Log.e("Date Sql" , formater.format(dateSql) + "" );*/

                        //Crypter le mot de passe
                        Hashage h = new Hashage();
                        mdpHash = h.computeSHAHash(mdp);
                        Log.e("mdp crypte", mdpHash + " ");

                        date = formater.format(dateJava);
                        String s = "test";

                        new AccesBD().execute(s);

                        //Ajout dans la bd
                        db.addUser(new User(n, p, ps, mdpHash, formater.format(dateJava)));

                        List<User> u = db.getAllUsers();
                        Log.e("taille liste", u.size() + "");
                        for (int i = 0; i < u.size(); i++) {
                            //Date d = u.get(i).get_date();
                            Log.e("nom ", u.get(i).get_nom());
                            Log.e("date ", u.get(i).get_date() + " ");
                        }

                        //on va à l'activité main
                        intent = new Intent(Inscription.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(Inscription.this, R.string.demandeDeConnexion, Toast.LENGTH_LONG).show();
                    }
                } else {
                    ActivityCompat.requestPermissions(Inscription.this,
                            new String[]{Manifest.permission.INTERNET},
                            REQUEST_CODE_ASK_PERMISSIONS);
                    Log.e("erreur", "permission denied ");
                }


            }
        });
    }


    private class AccesBD extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                return downloadUrl(params[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL maybe invalide ";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("rep" , " res " + result);
            Toast.makeText(Inscription.this, "Response " + result, Toast.LENGTH_LONG).show();
        }
    }

    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        int len = 500;
        //Afficher l'url
        String u = "http://folionielsbenichou.franceserv.com/Android/" +
                    "nouvelUtilisateur.php?nom=" + n + "&prenom=" + p + "&pseudo=" +
                    ps + "&motDePasse=" + mdpHash + "&date=" + date;
            Log.e("url" ,  u);

        URL url = new URL("http://folionielsbenichou.franceserv.com/Android/" +
                    "nouvelUtilisateur.php?nom=" + n + "&prenom=" + p + "&pseudo=" +
                    ps + "&motDePasse=" + mdpHash + "&date=" + date);
        //URL url = new URL("http://www.google.com/");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try{
            urlConnection.setReadTimeout(10000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.connect();

            /*URL url = new URL("http://www.android.com/");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                readStream(in);
            }
            finally{
                urlConnection.disconnect();
            }*/

            int response = urlConnection.getResponseCode();
            //Toast.makeText(Inscription.this, "Response " + response, Toast.LENGTH_LONG).show();
            Log.e("resultat", response + " ");
            is = urlConnection.getInputStream();

            String contentAsString = readIt(is, len);
            //InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            //readStream(in);
            //String [] tab = new String[0];
            //tab[0] = "Ok";
            return contentAsString;
        }
        catch(MalformedURLException e) {
            e.printStackTrace();
            //Toast.makeText(Inscription.this, "Malformé", Toast.LENGTH_LONG).show();
        }
        catch(IOException e) {
            e.printStackTrace();
            //Toast.makeText(Inscription.this, "Autre", Toast.LENGTH_LONG).show();
        }
        finally{
            urlConnection.disconnect();
        }

        return myurl;
    }


    private static String readStream(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            Log.e("e", "IOException", e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                Log.e("e", "IOException", e);
            }
        }
        return sb.toString();
    }

    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }
}



