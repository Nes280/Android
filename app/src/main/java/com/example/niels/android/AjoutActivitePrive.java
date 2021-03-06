package com.example.niels.android;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import com.example.niels.Code.getExemple;
import com.example.niels.bdd.BddUser;
import com.example.niels.bdd.User;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class AjoutActivitePrive extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String idActivite;
    String rep = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_activite_prive);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        //récuperation du pseudo
        BddUser db = new BddUser(AjoutActivitePrive.this);
        db.open();
        User u = db.getUserByIsConnected();

        //Mettre le pseudo dans le menu
        View v =navigationView.getHeaderView(0);
        TextView pseudo = (TextView) v.findViewById(R.id.pseudoTet);
        pseudo.setText(u.get_pseudo());
        pseudo.setTextSize(20);
        pseudo.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        db.close();

        final TextView lePseudo = (TextView)findViewById(R.id.pseudo);
        Bundle b = getIntent().getExtras();
        idActivite = (String) b.get("id");

        navigationView.setNavigationItemSelectedListener(this);
        ((Button) findViewById(R.id.button3)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pseudo = (String)lePseudo.getText().toString();
                //Log.e("PSeudo---->",pseudo);
                int activite = Integer.parseInt(idActivite);
                //Log.e("idActivite---->",activite+"");
                String format = "dd/MM/yy H:mm:ss";
                java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat(format);
                java.util.Date dateJava = new java.util.Date();

                partage(pseudo, activite, formater.format(dateJava));

                AjoutActivitePrive.this.finish();
            }
        });
    }

    private void partage(String pseudo, int activite, String date)
    {
        String urlActivite = "/Android/ajoutMembrePrive.php?utilisateur="+pseudo+"&activite="+activite+"&date="+date;
        Log.e("URL : ", urlActivite);
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
        getMenuInflater().inflate(R.menu.ajout_activite_prive, menu);
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
            Intent intent = new Intent(AjoutActivitePrive.this, Accueil_Utilisateur.class);
            startActivity(intent);
        }
        else if (id == R.id.autresActivite) {
            Intent intent = new Intent(AjoutActivitePrive.this, AutresActivites.class);
            startActivity(intent);
        } else if (id == R.id.creerActivite) {
            Intent intent = new Intent(AjoutActivitePrive.this, AjoutActivite.class);
            startActivity(intent);

        }
        else if(id == R.id.gererActivitePrivee) {
            Intent intent = new Intent(AjoutActivitePrive.this, ListeActivitePrivee.class);
            startActivity(intent);

        }else if (id == R.id.modifProfil) {
            Intent intent = new Intent(AjoutActivitePrive.this, Modification_Profil.class);
            startActivity(intent);
        }
        else if(id == R.id.changePassword){
            Intent intent = new Intent(AjoutActivitePrive.this, ChangementPassword.class);
            startActivity(intent);
        }
        else if (id == R.id.deconnexion) {
            BddUser db = new BddUser(AjoutActivitePrive.this);
            db.open();

            User u = db.getUserByIsConnected();

            db.setIsConnected(u.get_pseudo(), 0);

            Intent intent = new Intent(AjoutActivitePrive.this, MainActivity.class);
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
