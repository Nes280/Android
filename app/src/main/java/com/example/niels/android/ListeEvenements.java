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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

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

public class ListeEvenements extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView mListView;
    private ArrayAdapter<String> adapter;
    ArrayList<String> paramEve = new ArrayList<String>();
    String rep = null;
    Intent intent = null;
    String lat = "";
    String lon = "";
    String idEve = "";
    String nom = "";
    String desc = "";
    String photo = "";
    String date = "";
    String idUser = "";
    Hashtable<Integer, ArrayList<String>> contenueEvenement = new Hashtable<Integer, ArrayList<String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_evenements);
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

        //récuperation du pseudo
        BddUser db = new BddUser(ListeEvenements.this);
        db.open();
        User u = db.getUserByIsConnected();

        //Mettre le pseudo dans le menu
        View v =navigationView.getHeaderView(0);
        TextView pseudo = (TextView) v.findViewById(R.id.pseudoTet);
        pseudo.setText(u.get_pseudo());
        pseudo.setTextSize(20);
        pseudo.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        db.close();


        mListView = (ListView) findViewById(R.id.liste_evenements);
        ScrollView liste = (ScrollView) findViewById(R.id.scrollView);

        Bundle b = getIntent().getExtras();
        String id = (String) b.get("id");
        ArrayList<String> evenements = new ArrayList<String>();

        //on recupère les evenements en BD
        String url = "/Android/recupEvenement.php?activite=" + id;
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
        String valeur = null;


        try {
            JSONObject jsonEvenements = new JSONObject(rep);
            valeur = jsonEvenements.getString("state");
            if (valeur.equals("1")) {
                JSONArray jsonEveInfo = jsonEvenements.getJSONArray("evenement");

                for (int i = 0; i < jsonEveInfo.length(); i++) {
                    //paramEve.clear();
                    JSONObject objNomActivite = jsonEveInfo.getJSONObject(i);
                    idEve = objNomActivite.getString("id evenement");
                    idUser = objNomActivite.getString("idUtilisateur");
                    lat = objNomActivite.getString("latitude GPS");
                    lon = objNomActivite.getString("longitude GPS");
                    nom = objNomActivite.getString("nom evenement");
                    desc = objNomActivite.getString("description");
                    photo = objNomActivite.getString("photo");
                    date = objNomActivite.getString("date");

                    evenements.add("\n" + nom.toUpperCase() + "\n\n" +
                                    getString(R.string.description) + " : " + desc + "\n" +
                                    getString(R.string.date) + " : " + date + "\n" +
                                    getString(R.string.photo) + "\n"
                    );

                    paramEve.add(idEve);
                    paramEve.add(idUser);
                    paramEve.add(lat);
                    paramEve.add(lon);
                    paramEve.add(nom);
                    paramEve.add(desc);
                    paramEve.add(photo);
                    paramEve.add(date);

                    contenueEvenement.put(i,paramEve);
                }
            } else evenements.add(getString(R.string.no_event));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter = new ArrayAdapter<String>(this, R.layout.simple_row, evenements);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Log.e("LOG---->", contenueEvenement.get(position).toString());
                ArrayList<String>liste = contenueEvenement.get(position);
                Intent intent = new Intent();
                //Log.e("LOG####",liste.get((position ) * 8) + 4+" "+liste.get((position ) * 8) + 2+)
                intent.setClass(ListeEvenements.this, MapEvenement.class);
                intent.putExtra("nom", liste.get((((position) * 8) + 4)));
                intent.putExtra("desc", liste.get((((position ) * 8) + 5)));
                intent.putExtra("date", liste.get((((position ) * 8) + 7)));
                intent.putExtra("lat", liste.get((((position ) * 8) + 2)));
                intent.putExtra("lon", liste.get((((position ) * 8) + 3)));
                startActivity(intent);
            }
        });

        mListView.setAdapter(adapter);


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
        getMenuInflater().inflate(R.menu.liste_evenements, menu);
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

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
