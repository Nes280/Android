package com.example.niels.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.niels.bdd.Activite;
import com.example.niels.bdd.BddActivite;
import com.example.niels.bdd.BddMembreActivite;
import com.example.niels.bdd.BddUser;
import com.example.niels.bdd.MembreActivite;
import com.example.niels.bdd.User;

public class AjoutActivite extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String nom, description;
    int publication;
    Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_activite);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        BddUser db = new BddUser(AjoutActivite.this);
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

        //Recuperer les informations
        final EditText tb_nom = (EditText) findViewById(R.id.editText);
        final EditText tb_description = (EditText) findViewById(R.id.editText2);
        final RadioButton br_publication = (RadioButton) findViewById(R.id.radioPublique);

        //bouton d'envoi
        ((Button) findViewById(R.id.btn_activity)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //récuperation du pseudo
                BddUser dbd = new BddUser(AjoutActivite.this);
                dbd.open();
                User u = dbd.getUserByIsConnected();
                int idUser = u.get_id();

                //creation de la date
                String format = "dd/MM/yy H:mm:ss";
                java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat(format);
                java.util.Date dateJava = new java.util.Date();

                //recuperation des valeurs
                nom = tb_nom.getText().toString();
                description = tb_description.getText().toString();
                if (br_publication.isChecked())
                    publication = 0;
                else publication = 1;

                //test si tout va bien.
                if (nom.isEmpty() || description.isEmpty()) {
                    Toast.makeText(AjoutActivite.this, R.string.empty_field_activity, Toast.LENGTH_LONG).show();
                    return;
                } else // on envoi la nouvelle activite en BD
                {
                    BddActivite bd = new BddActivite(AjoutActivite.this);
                    bd.open();

                    //on test si le nom n'existe pas déja
                    if (bd.isExistActivity(nom))
                    {
                        //existe deja
                        Toast.makeText(AjoutActivite.this,R.string.name_already_exist_activity, Toast.LENGTH_LONG).show();
                        return;

                    }
                    //si non le nom n'existe pas déjà donc on continue
                    Activite a = new Activite(nom, description, idUser, formater.format(dateJava), publication);

                    bd.addActivite(a);

                    int[] i = bd.getIdByNames(nom);
                    int idU = i[0];
                    int idA = i[1];
                    //on dit aussi que le proprietaire est membre de son activité
                    BddMembreActivite bdMa = new BddMembreActivite(AjoutActivite.this);
                    bdMa.open();
                    bdMa.addMembreActivite(new MembreActivite(idA, idU, formater.format(dateJava)));
                    //Toast.makeText(AjoutActivite.this, bd."", Toast.LENGTH_LONG).show();

                    //on va à l'activité main
                    intent = new Intent(AjoutActivite.this, Accueil_Utilisateur.class);
                    startActivity(intent);

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
        getMenuInflater().inflate(R.menu.ajout_activite, menu);
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
            // Handle the camera action
        }
        else if (id == R.id.accueil) {
            Intent intent = new Intent(AjoutActivite.this, Accueil_Utilisateur.class);
            startActivity(intent);
        }
        else if (id == R.id.modifProfil) {

        }
        else if (id == R.id.deconnexion) {
            BddUser db = new BddUser(AjoutActivite.this);
            db.open();

            User u = db.getUserByIsConnected();

            db.setIsConnected(u.get_pseudo(), 0);
            db.close();
            Intent intent = new Intent(AjoutActivite.this, MainActivity.class);
            startActivity(intent);
        }
        /*else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
