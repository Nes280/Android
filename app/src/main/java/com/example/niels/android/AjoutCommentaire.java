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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.niels.Code.getExemple;
import com.example.niels.bdd.BddUser;
import com.example.niels.bdd.User;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class AjoutCommentaire extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String idActivite;
    String idUser;
    Intent intent;
    String rep = null;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_commentaire);
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
        BddUser db = new BddUser(AjoutCommentaire.this);
        db.open();
        User u = db.getUserByIsConnected();

        //Mettre le pseudo dans le menu
        View v =navigationView.getHeaderView(0);
        TextView pseudo = (TextView) v.findViewById(R.id.pseudoTet);
        //tx.setText("test");
        //Log.e("pseudoUser", u.get_pseudo() + "");
        pseudo.setText(u.get_pseudo());
        pseudo.setTextSize(20);
        pseudo.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        db.close();

        //Demande de permission
        int permissionCheck = ContextCompat.checkSelfPermission(AjoutCommentaire.this,
                Manifest.permission.INTERNET);

        int permissionCheck2 = ContextCompat.checkSelfPermission(AjoutCommentaire.this,
                Manifest.permission.ACCESS_NETWORK_STATE);

        int permissionCheck3 = ContextCompat.checkSelfPermission(AjoutCommentaire.this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        //Vérification permission
        if (permissionCheck == PackageManager.PERMISSION_GRANTED && permissionCheck2 == PackageManager.PERMISSION_GRANTED
                && permissionCheck3 == PackageManager.PERMISSION_GRANTED) {
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {

                Bundle b = getIntent().getExtras();
                idActivite = (String) b.get("idActivite");
                idUser = (String) b.get("idUser");
                final TextView leCommentaire = (TextView)findViewById(R.id.editText3);


                        ((Button) findViewById(R.id.button2)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String contenue = (String)leCommentaire.getText().toString();

                        int activite = Integer.parseInt(idActivite);

                        String format = "dd/MM/yy H:mm:ss";
                        java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat(format);
                        java.util.Date dateJava = new java.util.Date();

                        addCommentaite(contenue,idUser,activite, formater.format(dateJava));
                        /*
                        intent = new Intent(AjoutCommentaire.this, AjoutCommentaire.class);
                        intent.putExtra("idUser", idUser);
                        intent.putExtra("idActivite", idActivite);

                        startActivity(intent);
                        */
                        AjoutCommentaire.this.finish();
                    }
                });
            } else {
                Toast.makeText(AjoutCommentaire.this, R.string.demandeDeConnexion, Toast.LENGTH_LONG).show();
            }
        } else
        {
            ActivityCompat.requestPermissions(AjoutCommentaire.this,
                    new String[]{Manifest.permission.INTERNET},
                    REQUEST_CODE_ASK_PERMISSIONS);

            ActivityCompat.requestPermissions(AjoutCommentaire.this,
                    new String[]{Manifest.permission.ACCESS_NETWORK_STATE},
                    REQUEST_CODE_ASK_PERMISSIONS);

            ActivityCompat.requestPermissions(
                    AjoutCommentaire.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);

            boolean i = checkLocationPermission();

            //Log.e("erreur", "permission denied " + i);

        }
    }

    public boolean checkLocationPermission()
    {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    private void addCommentaite(String commentaire, String user, int activite, String date)
    {
        String urlActivite = "/Android/ajoutCommentaire.php?utilisateur="+user+"&activite="+activite+"&commentaire="+commentaire+"&date="+date;
        AccesBD activiteAcces = new AccesBD();
        activiteAcces.execute(urlActivite);
        try {
            activiteAcces.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
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
        getMenuInflater().inflate(R.menu.ajout_commentaire, menu);
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

        if (id == R.id.autresActivite) {
            Intent intent = new Intent(AjoutCommentaire.this, AutresActivites.class);
            startActivity(intent);        }
        else if (id == R.id.accueil) {
            Intent intent = new Intent(AjoutCommentaire.this, Accueil_Utilisateur.class);
            startActivity(intent);
        }
        else if(id == R.id.gererActivitePrivee) {
            Intent intent = new Intent(AjoutCommentaire.this, ListeActivitePrivee.class);
            startActivity(intent);

        }
        else if (id == R.id.modifProfil) {
            Intent intent = new Intent(AjoutCommentaire.this, Modification_Profil.class);
            startActivity(intent);
        }
        else if(id == R.id.changePassword){
            Intent intent = new Intent(AjoutCommentaire.this, ChangementPassword.class);
            startActivity(intent);
        }
        else if (id == R.id.deconnexion) {
            BddUser db = new BddUser(AjoutCommentaire.this);
            db.open();

            User u = db.getUserByIsConnected();

            db.setIsConnected(u.get_pseudo(), 0);
            db.close();
            Intent intent = new Intent(AjoutCommentaire.this, MainActivity.class);
            startActivity(intent);
        }
        /*else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

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
                rep = e.run(host+params[0]);
                //Log.e("REPONSE FRAGMENT", rep);
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
