package com.example.niels.android;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.sax.TextElementListener;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.niels.Code.getExemple;
import com.example.niels.android.R;
import com.example.niels.bdd.BddUser;
import com.example.niels.bdd.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class AjoutEvenement extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationListener  {

    Intent intent =  null;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    String rep = null;
    Location location;
    LocationManager lm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_evenement);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //récuperation du pseudo
        BddUser db = new BddUser(AjoutEvenement.this);
        db.open();
        User u = db.getUserByIsConnected();

        //Mettre le pseudo dans le menu
        View v =navigationView.getHeaderView(0);
        TextView pseudo = (TextView) v.findViewById(R.id.pseudoTet);
        pseudo.setText(u.get_pseudo());
        pseudo.setTextSize(20);
        pseudo.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        db.close();


        //Recuperation des informations de l'évenement
        final TextView nomEvenement = (TextView) findViewById(R.id.nom);
        final TextView descriptionEvenement = (TextView) findViewById(R.id.description);

        //Clique pour créer evenement
        ((Button) findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Recuperation des textes
                String nomE = nomEvenement.getText().toString();
                String descriptionE = descriptionEvenement.getText().toString();

                //Verifier que l'utilisateur a rempli tous les champs
                if(nomE.isEmpty() || descriptionE.isEmpty()){
                    Toast.makeText(AjoutEvenement.this, R.string.verifChamps, Toast.LENGTH_LONG).show();
                    return;
                }


                //Recuperer la date du jour
                String format = "dd/MM/yy H:mm:ss";
                java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat(format);
                java.util.Date dateJava = new java.util.Date();
                System.out.println(formater.format(dateJava));
                String date = formater.format(dateJava);

                //récuperation du pseudo
                BddUser db = new BddUser(AjoutEvenement.this);
                db.open();
                User u = db.getUserByIsConnected();
                String pseudo = u.get_pseudo();
                db.close();

                //Demande de permission
                int permissionCheck = ContextCompat.checkSelfPermission(AjoutEvenement.this,
                        Manifest.permission.INTERNET);

                int permissionCheck2 = ContextCompat.checkSelfPermission(AjoutEvenement.this,
                        Manifest.permission.ACCESS_NETWORK_STATE);

                int permissionCheck3 = ContextCompat.checkSelfPermission(AjoutEvenement.this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

                //Vérification permission
                if (permissionCheck == PackageManager.PERMISSION_GRANTED && permissionCheck2 == PackageManager.PERMISSION_GRANTED
                        && permissionCheck3 == PackageManager.PERMISSION_GRANTED) {
                    ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected()) {

                        //Recuperer l'id de l'utilisateur
                        AccesBD a = new AccesBD();
                        String urlUtilisateur = "/Android/recupUtilisateur.php?pseudo="+pseudo;
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
                        String identifiantUtilisateur = null;

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            valeur = jsonObject.getString("state");
                            Log.e("resultat json " , valeur);
                            if(valeur.equals("0")){
                                Toast.makeText(AjoutEvenement.this, R.string.verifExistancePseudo, Toast.LENGTH_LONG).show();
                                return;
                            }
                            JSONObject utilisateur = jsonObject.getJSONObject("utilisateur");
                            identifiantUtilisateur = utilisateur.getString("id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //Recuperer l'id de l'activité
                        //A faire
                        String idActivite = "4";

                        //Verification que l'evenement est unique par rapport à l'activité
                        //id activité en dur
                        AccesBD verifNom = new AccesBD();
                        String urlVerifNom = "/Android/isExisteNomEve.php?activite="+idActivite+"&nom="+nomE;
                        verifNom.execute(urlVerifNom);
                        try {
                            verifNom.get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }

                        String responseNom = rep;
                        Log.e("script nom eve", responseNom);
                        String valeurNom = null;

                        try {
                            JSONObject jsonObject = new JSONObject(responseNom);
                            valeurNom = jsonObject.getString("state");
                            Log.e("resultat valeur eve" , valeurNom);
                            if(valeurNom.equals("0")){
                                Toast.makeText(AjoutEvenement.this, R.string.verifNomEvenement, Toast.LENGTH_LONG).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        //Recuperer la latitude et la longitude
                        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

                        boolean gps_enable = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                        boolean network_enable = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                        Log.e("gps ", gps_enable + "");
                        Log.e("network", network_enable + "");

                        location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        double longitude;
                        double latitude;

                        if(location != null){
                            longitude = location.getLongitude();
                            latitude = location.getLatitude();

                            Log.e("latitude", latitude + "");
                            Log.e("longitude", longitude + "");
                        }
                        else
                        {
                            Log.e("location ", "null");
                            Toast.makeText(AjoutEvenement.this, R.string.verifActivationLocalisation, Toast.LENGTH_LONG).show();
                            return;
                        }

                        String latitudeS = String.valueOf(latitude);
                        String longitudeS = String.valueOf(longitude);

                        //Insertion dans la bd Externe
                        AccesBD addEvenement = new AccesBD();
                        String urlAddEvenement = "/Android/nouvelEvenement.php?latitude="+latitudeS+"&longitude="+longitudeS+
                                "&nom="+nomE+"&description="+descriptionE+"&photo=null&date=" + date +
                                "&utilisateur="+identifiantUtilisateur + "&activite=" + idActivite;
                        addEvenement.execute(urlAddEvenement);
                        try {
                            addEvenement.get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }

                        intent = new Intent(AjoutEvenement.this, Accueil_Utilisateur.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(AjoutEvenement.this, R.string.demandeDeConnexion, Toast.LENGTH_LONG).show();
                    }
                } else
                {
                    ActivityCompat.requestPermissions(AjoutEvenement.this,
                            new String[]{Manifest.permission.INTERNET},
                            REQUEST_CODE_ASK_PERMISSIONS);

                    ActivityCompat.requestPermissions(AjoutEvenement.this,
                            new String[]{Manifest.permission.ACCESS_NETWORK_STATE},
                            REQUEST_CODE_ASK_PERMISSIONS);

                    ActivityCompat.requestPermissions(
                            AjoutEvenement.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            1);

                    boolean i = checkLocationPermission();

                    Log.e("erreur", "permission denied " + i);

                }

            }
        });
    }

    public boolean checkLocationPermission()
    {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
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
        getMenuInflater().inflate(R.menu.ajout_evenement, menu);
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
            Intent intent = new Intent(AjoutEvenement.this, Accueil_Utilisateur.class);
            startActivity(intent);
        }
        else if (id == R.id.autresActivite) {
            // Handle the camera action
        } else if (id == R.id.creerActivite) {
            Intent intent = new Intent(AjoutEvenement.this, AjoutActivite.class);
            startActivity(intent);

        } else if (id == R.id.modifProfil) {
            /*Intent intent = new Intent(AjoutEvenement.this, AjoutEvenement.class);
            startActivity(intent);*/
        }
        else if (id == R.id.deconnexion) {
            BddUser db = new BddUser(AjoutEvenement.this);
            db.open();

            User u = db.getUserByIsConnected();

            db.setIsConnected(u.get_pseudo(), 0);

            Intent intent = new Intent(AjoutEvenement.this, MainActivity.class);
            startActivity(intent);


        }/* else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

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
