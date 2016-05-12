package com.example.niels.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.TextView;

import com.example.niels.bdd.BddUser;
import com.example.niels.bdd.User;

public class ChangementPassword extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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
        User u = db.getUserByIsConnected();

        //Mettre le pseudo dans le menu
        View v =navigationView.getHeaderView(0);
        TextView pseudo = (TextView) v.findViewById(R.id.pseudoTet);
        pseudo.setText(u.get_pseudo());
        pseudo.setTextSize(20);
        pseudo.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        db.close();
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
            // Handle the camera action
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
}
