package com.example.niels.android;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.niels.Code.getExemple;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class ListeEvenement extends AppCompatActivity {
    private ListView mListView;
    private ArrayAdapter<String> adapter;
    String rep = null;
    Intent intent = null;
    double lat = 0.0;
    double lon = 0.0;

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

        Bundle b = getIntent().getExtras();
        String id = (String)b.get("id");
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
        int idEve = 0;

        String nom = "";
        String desc = "";
        String photo = "";
        String date = "";
        int idUser = 0;
//TODO tester si il y a des evenements
        try {
            JSONObject jsonEvenements = new JSONObject(rep);
            valeur = jsonEvenements.getString("state");
            if (valeur.equals("1"))
            {
                JSONArray jsonEveInfo = jsonEvenements.getJSONArray("evenement");

                for (int i = 0; i<jsonEveInfo.length();i++)
                {
                    JSONObject objNomActivite = jsonEveInfo.getJSONObject(i);
                    idEve = Integer.parseInt(objNomActivite.getString("id evenement") + "");
                    idUser = Integer.parseInt(objNomActivite.getString("idUtilisateur") + "");
                    lat = Double.parseDouble(objNomActivite.getString("latitude GPS") + "");
                    lon = Double.parseDouble(objNomActivite.getString("longitude GPS") + "");
                    nom = objNomActivite.getString("nom evenement");
                    desc = objNomActivite.getString("description");
                    photo = objNomActivite.getString("photo");
                    date = objNomActivite.getString("date");

                    evenements.add(nom);
                    evenements.add(getString(R.string.description)+" : "+desc);
                    evenements.add(getString(R.string.date)+" : "+date);
                    evenements.add(getString(R.string.photo));
                }
            }
            else  evenements.add(getString(R.string.no_event));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter = new ArrayAdapter<String>(this, R.layout.simple_row, evenements);
        mListView.setAdapter(adapter);
        ((Button) findViewById(R.id.button2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(ListeEvenement.this, DetailsEvent.class);
                intent.putExtra("lat", lat);
                intent.putExtra("lon", lon);
                startActivity(intent);
            }
        });

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
