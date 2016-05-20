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

import com.example.niels.Code.Md5;
import com.example.niels.Code.getExemple;
import com.example.niels.bdd.BddUser;
import com.example.niels.bdd.User;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class ChangementPassword extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Intent intent = null;
    String rep = null;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changement_password);
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
        BddUser db = new BddUser(ChangementPassword.this);
        db.open();
        final User u = db.getUserByIsConnected();

        //Mettre le pseudo dans le menu
        View v =navigationView.getHeaderView(0);
        TextView pseudo = (TextView) v.findViewById(R.id.pseudoTet);
        pseudo.setText(u.get_pseudo());
        pseudo.setTextSize(20);
        pseudo.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        db.close();

        final TextView oldPassword = (TextView) findViewById(R.id.oldPassword);
        final TextView newPassword = (TextView) findViewById(R.id.newPassword);
        final TextView newPassword2 = (TextView) findViewById(R.id.newPassword2);


        ((Button) findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(ChangementPassword.this, "A faire", Toast.LENGTH_LONG).show();

                //Recuperation des info
                String oldP = oldPassword.getText().toString();
                String newP = newPassword.getText().toString();
                String newP2 = newPassword2.getText().toString();

                if(oldP.isEmpty() || newP.isEmpty() || newP2.isEmpty()){
                    Toast.makeText(ChangementPassword.this, R.string.verifChamps, Toast.LENGTH_LONG).show();
                    return;
                }

                //Cryptage de l'ancien mot de passe
                Md5 m = new Md5(oldP);
                String oldPHash = m.getCode();

                //verifier que l'ancien password est le même que celui en BD
                Log.e("mdp bd", u.get_password() + "");
                Log.e("mdp hash ", oldPHash);
                if(!oldPHash.equals(u.get_password())){
                    Toast.makeText(ChangementPassword.this, R.string.verifAncienPassword, Toast.LENGTH_LONG).show();
                    return;
                }

                //Verifier que ca soit les mêmes
                //Verfier le nombre de caractère des 2 nouveaux mots de passe
                if (!newP.equals(newP2)) {
                    Toast.makeText(ChangementPassword.this, R.string.verifPassword, Toast.LENGTH_LONG).show();
                    return;
                } else if (newP.length() < 5) {
                    Toast.makeText(ChangementPassword.this, R.string.verifTaillePassword, Toast.LENGTH_LONG).show();
                    return;
                }

                Md5 m2 = new Md5(newP);
                String newPHash = m2.getCode();

                //Demande de permission
                int permissionCheck = ContextCompat.checkSelfPermission(ChangementPassword.this,
                        Manifest.permission.INTERNET);

                int permissionCheck2 = ContextCompat.checkSelfPermission(ChangementPassword.this,
                        Manifest.permission.ACCESS_NETWORK_STATE);

                if (permissionCheck == PackageManager.PERMISSION_GRANTED && permissionCheck2 == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(Inscription.this, "Permission", Toast.LENGTH_LONG).show();
                    ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected()) {

                        AccesBD a = new AccesBD();
                        String urlUtilisateur = "/Android/modifUtilisateur.php?pseudo="+u.get_pseudo()+"&nom="+u.get_nom()+"&prenom="
                                +u.get_prenom()+"&pass="+newPHash;
                        a.execute(urlUtilisateur);
                        try {
                            a.get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }

                        BddUser db = new BddUser(ChangementPassword.this);
                        db.open();

                        db.setPassword(u.get_pseudo(), newPHash);
                        db.close();

                        //on va à l'activité main
                        intent = new Intent(ChangementPassword.this, Accueil_Utilisateur.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(ChangementPassword.this, R.string.demandeDeConnexion, Toast.LENGTH_LONG).show();
                    }
                } else {
                    ActivityCompat.requestPermissions(ChangementPassword.this,
                            new String[]{Manifest.permission.INTERNET},
                            REQUEST_CODE_ASK_PERMISSIONS);
                    Log.e("erreur", "permission denied ");
                }

            }
        });
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
        getMenuInflater().inflate(R.menu.changement_password, menu);
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
            Intent intent = new Intent(ChangementPassword.this, Accueil_Utilisateur.class);
            startActivity(intent);
        }
        else if (id == R.id.autresActivite) {
            Intent intent = new Intent(ChangementPassword.this, AutresActivites.class);
            startActivity(intent);
        } else if (id == R.id.creerActivite) {
            Intent intent = new Intent(ChangementPassword.this, AjoutActivite.class);
            startActivity(intent);

        } else if (id == R.id.modifProfil) {
            Intent intent = new Intent(ChangementPassword.this, Modification_Profil.class);
            startActivity(intent);
        }
        else if(id == R.id.changePassword){
            Intent intent = new Intent(ChangementPassword.this, ChangementPassword.class);
            startActivity(intent);
        }
        else if (id == R.id.deconnexion) {
            BddUser db = new BddUser(ChangementPassword.this);
            db.open();

            User u = db.getUserByIsConnected();

            db.setIsConnected(u.get_pseudo(), 0);

            Intent intent = new Intent(ChangementPassword.this, MainActivity.class);
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
