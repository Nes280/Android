package com.example.niels.android;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.niels.Code.getExemple;
import com.example.niels.bdd.BddUser;
import com.example.niels.bdd.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.ExecutionException;

public class ListeActivitePrivee extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView mListView;
    private ArrayAdapter<String> adapter;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    ArrayList<String> paramEve = new ArrayList<String>();
    String valeur;
    String rep = null;
    Intent intent = null;
    String idA ="";
    String nomA ="";
    String description = "";
    String date = "";
    String type = "";
    String idU = "";
    Hashtable<Integer, ArrayList<String>> contenueEvenement = new Hashtable<Integer, ArrayList<String>>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_activite_privee);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //récuperation du pseudo
        BddUser db = new BddUser(ListeActivitePrivee.this);
        db.open();
        User u = db.getUserByIsConnected();

        //Mettre le pseudo dans le menu
        View v =navigationView.getHeaderView(0);
        TextView pseudo = (TextView) v.findViewById(R.id.pseudoTet);
        pseudo.setText(u.get_pseudo());
        pseudo.setTextSize(20);
        pseudo.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        db.close();

        //Demande de permission
        int permissionCheck = ContextCompat.checkSelfPermission(ListeActivitePrivee.this,
                Manifest.permission.INTERNET);

        int permissionCheck2 = ContextCompat.checkSelfPermission(ListeActivitePrivee.this,
                Manifest.permission.ACCESS_NETWORK_STATE);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED && permissionCheck2 == PackageManager.PERMISSION_GRANTED) {
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {


                mListView = (ListView) findViewById(R.id.listeActivitePrivee);
                ScrollView liste = (ScrollView) findViewById(R.id.scrollView);

                ArrayList<String> evenements = new ArrayList<String>();

                //on recupère les evenements en BD
                String url = "/Android/recupActivitePrive.php?utilisateur=" + u.get_pseudo();
                AccesBD recActivite = new AccesBD();
                recActivite.execute(url);
                try {
                    recActivite.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                String response = rep;
                valeur = null;


                try {
                    JSONObject jsonEvenements = new JSONObject(rep);
                    valeur = jsonEvenements.getString("state");
                    Log.e("state", valeur);
                    if(valeur.equals("1")) {
                        JSONArray jsonEveInfo = jsonEvenements.getJSONArray("activite");

                        for (int i = 0; i < jsonEveInfo.length(); i++) {
                            //paramEve.clear();
                            JSONObject objNomActivite = jsonEveInfo.getJSONObject(i);
                            idA = objNomActivite.getString("id activite");
                            nomA = objNomActivite.getString("nom activite");
                            description = objNomActivite.getString("description");
                            date = objNomActivite.getString("date");
                            type = objNomActivite.getString("type");
                            idU = objNomActivite.getString("id utilisateur");

                            evenements.add("\n" + nomA.toUpperCase() + "\n\n" +
                                            getString(R.string.description) + " : " + description + "\n" +
                                            getString(R.string.date) + " : " + date + "\n" +
                                            getString(R.string.photo) + "\n"
                            );

                            paramEve.add(idA);
                            paramEve.add(nomA);
                            paramEve.add(description);
                            paramEve.add(date);
                            paramEve.add(type);
                            paramEve.add(idU);

                            contenueEvenement.put(i,paramEve);
                        }
                    }
                    else
                    {
                        evenements.add(getString(R.string.pasActivitePrivee));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter = new ArrayAdapter<String>(this, R.layout.simple_row, evenements);

                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        if(valeur.equals("1")) {
                            Log.e("LOG---->", contenueEvenement.get(position).toString());
                            ArrayList<String> liste = contenueEvenement.get(position);
                            Intent intent = new Intent();
                            //Log.e("LOG####",liste.get((position ) * 8) + 4+" "+liste.get((position ) * 8) + 2+)
                            //8 parcequ'il y a 8 paramètres par evenement donc position*8 permet de se placer au debut de la liste de l'evenement position
                            intent.setClass(ListeActivitePrivee.this, AjoutActivitePrive.class);
                            intent.putExtra("id", idA);

                            startActivity(intent);
                        }
                    }
                });

                mListView.setAdapter(adapter);
            } else {
                Toast.makeText(ListeActivitePrivee.this, R.string.demandeDeConnexion, Toast.LENGTH_LONG).show();
            }
        } else
        {
            ActivityCompat.requestPermissions(ListeActivitePrivee.this,
                    new String[]{Manifest.permission.INTERNET},
                    REQUEST_CODE_ASK_PERMISSIONS);

            ActivityCompat.requestPermissions(ListeActivitePrivee.this,
                    new String[]{Manifest.permission.ACCESS_NETWORK_STATE},
                    REQUEST_CODE_ASK_PERMISSIONS);

            Log.e("erreur", "permission denied " );

        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.liste_activite_privee, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.accueil){
            Intent intent = new Intent(ListeActivitePrivee.this, Accueil_Utilisateur.class);
            startActivity(intent);
        }
        else if (id == R.id.autresActivite) {
            Intent intent = new Intent(ListeActivitePrivee.this, AutresActivites.class);
            startActivity(intent);
        } else if (id == R.id.creerActivite) {
            Intent intent = new Intent(ListeActivitePrivee.this, AjoutActivite.class);
            startActivity(intent);

        }
        else if(id == R.id.gererActivitePrivee) {
            Intent intent = new Intent(ListeActivitePrivee.this, ListeActivitePrivee.class);
            startActivity(intent);

        }else if (id == R.id.modifProfil) {
            Intent intent = new Intent(ListeActivitePrivee.this, Modification_Profil.class);
            startActivity(intent);
        }
        else if(id == R.id.changePassword){
            Intent intent = new Intent(ListeActivitePrivee.this, ChangementPassword.class);
            startActivity(intent);
        }
        else if (id == R.id.deconnexion) {
            BddUser db = new BddUser(ListeActivitePrivee.this);
            db.open();

            User u = db.getUserByIsConnected();

            db.setIsConnected(u.get_pseudo(), 0);

            Intent intent = new Intent(ListeActivitePrivee.this, MainActivity.class);
            startActivity(intent);

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class AccesBD extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                getExemple e = new getExemple();
                String host = "http://folionielsbenichou.franceserv.com";
                //String rep = null;

                //rep = e.run(host+"/Android/nouvelUtilisateur.php?nom=" +n+"&prenom="+p+"&pseudo="+ps+"&motDePasse="+mdpHash+"&date="+date);
                rep = e.run(host + params[0]);
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
