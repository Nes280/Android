package com.example.niels.android;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.niels.Code.Commentaire;
import com.example.niels.Code.CommentaireAdapter;
import com.example.niels.Code.getExemple;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.CookieHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MapEvenement extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    double lat = 0.0;
    double lon = 0.0;
    private ArrayAdapter<String> adapter;
    private ListView mListView;
    private CommentaireAdapter adapter2;
    private ListView mListView2;
    ArrayList<String> paramEve = new ArrayList<String>();
    String rep = null;
    String idActivite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_evenement);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle b = getIntent().getExtras();
       // paramEve = (ArrayList<String>)b.get("array");

        TextView nom = (TextView) findViewById(R.id.textView);/*
        String desc = (TextView) findViewById(R.id.textView2);
        String date = (TextView) findViewById(R.id.textView3);*/

        ArrayList<String> contenue = new ArrayList<String>();
       // ArrayList<String> commentaire = new ArrayList<String>();

        mListView = (ListView) findViewById(R.id.listView2);
        mListView2 = (ListView) findViewById(R.id.listCommentaires);

        nom.setText((String) b.get("nom"));
        String de = (String) b.get("desc");
        String da = (String) b.get("date");
        idActivite = (String) b.get("idActivite");
        String desc = (getString(R.string.description) + " : "+de);
        String date = (getString(R.string.date) + " : "+da);

        contenue.add(desc);
        contenue.add(date);

        adapter = new ArrayAdapter<String>(MapEvenement.this, R.layout.simple_row, contenue);
        mListView.setAdapter(adapter);


        //commentaire.add("test\n blabla blabla");

        List<Commentaire> lesCommentaires = recupCommentaires();
        //Log.e("COMM : ",lesCommentaires.toString());
        adapter2 = new CommentaireAdapter(this, lesCommentaires);
        mListView2.setAdapter(adapter2);

    }

    private List<Commentaire> recupCommentaires()
    {
        String valeur = null;
        List<Commentaire> lesCommentaires = new ArrayList<Commentaire>();
        //Recuperation de l'activité
        String urlActivite = "/Android/recupCommentaires.php?activite="+idActivite;
        AccesBD activiteAcces = new AccesBD();
        activiteAcces.execute(urlActivite);
        try {
            activiteAcces.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        try {
            //Log.e("reponse recupActivite", rep);
            JSONObject jsonCommetaires = new JSONObject(rep);
            valeur = jsonCommetaires.getString("state");
            if (valeur.equals("1"))
            {
                JSONArray jsonCommentairesInfo = jsonCommetaires.getJSONArray("commentaires");
                //Log.e("obNomActivite", jsonActiviteInfo + "");

                for (int i=0;i<jsonCommentairesInfo.length();i++)
                {
                    JSONObject objMonCommentaire = jsonCommentairesInfo.getJSONObject(i);
                    //Log.e("obNomActivite", objNomActivite + "");
                    String unCommentaire = objMonCommentaire.getString("commentaire");
                    String auteur = objMonCommentaire.getString("utilisateur");
                    lesCommentaires.add(new Commentaire(auteur,unCommentaire));

                }

            }
            else lesCommentaires.add(new Commentaire(" ", getString(R.string.no_comment)));

        }catch (JSONException e) {
            e.printStackTrace();
        }
        return lesCommentaires;
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
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Bundle b = getIntent().getExtras();
        paramEve = (ArrayList<String>)b.get("array");
        lat = Double.parseDouble((String)b.get("lat"));
        lon = Double.parseDouble((String)b.get("lon"));
       // Log.e("LAT LON : ",lat+" "+lon);

        // Add a marker in Sydney and move the camera
        LatLng ici = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(ici).title(getString(R.string.location_event)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ici));
    }
}
