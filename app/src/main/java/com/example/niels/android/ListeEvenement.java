package com.example.niels.android;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.niels.Code.getExemple;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.ExecutionException;


public class ListeEvenement extends AppCompatActivity {
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
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_evenement);
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


        mListView = (ListView) findViewById(R.id.listView);
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
                intent.setClass(ListeEvenement.this, MapEvenement.class);
                intent.putExtra("nom", liste.get((((position) * 8) + 4)));
                intent.putExtra("desc", liste.get((((position ) * 8) + 5)));
                intent.putExtra("date", liste.get((((position ) * 8) + 7)));
                intent.putExtra("lat", liste.get((((position ) * 8) + 2)));
                intent.putExtra("lon", liste.get((((position ) * 8) + 3)));
                startActivity(intent);
            }
        });

        mListView.setAdapter(adapter);


        /*((Button) findViewById(R.id.button2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(ListeEvenement.this, MapEvenement.class);
                intent.putExtra("lat", lat);
                intent.putExtra("lon", lon);
                startActivity(intent);
            }
        });*/

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ListeEvenement Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.niels.android/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ListeEvenement Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.niels.android/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
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
