package com.example.niels.android;

import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.niels.Code.getExemple;
import com.example.niels.bdd.Activite;
import com.example.niels.bdd.BddActivite;
import com.example.niels.bdd.BddMembreActivite;
import com.example.niels.bdd.BddUser;
import com.example.niels.bdd.MembreActivite;
import com.example.niels.bdd.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A placeholder fragment containing a simple view.
 */
public class AccueilFragment extends ListFragment {

   /* public AccueilFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_accueil, container, false);
    }*/

    boolean mDualPane;
    int mCurCheckPosition = 0;
    private ArrayAdapter<String> listAdapter ;
    private ArrayList<String[]> listActivite;
    String rep = null;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listActivite = new ArrayList<String[]>();
        //Lecture de la base de données pour avoir les activités de l'utilisateur
        String[] contact = new String[] {};
        ArrayList<String> contactList = new ArrayList<String>();
        contactList.addAll(Arrays.asList(contact));
        listAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1, contactList);

        //Connexion à la base de données
        BddMembreActivite dbMembre = new BddMembreActivite(getActivity());
        BddUser dbUser = new BddUser(getActivity());
        BddActivite dbActivite = new BddActivite(getActivity());

        dbUser.open();
        dbMembre.open();
        dbActivite.open();

        User u = dbUser.getUserByIsConnected();
        String pseudo = u.get_pseudo();

        String urlUtilisateur = "/Android/recupUtilisateur.php?pseudo="+pseudo;
        AccesBD acUtilisateur = new AccesBD();
        acUtilisateur.execute(urlUtilisateur);
        try {
            acUtilisateur.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        String response = rep;
        String valeur = null;

        String identifiant = null;

        try {
            JSONObject jsonObject = new JSONObject(response);
            //JSONObject newJson = jsonObject.getJSONObject("state");
            valeur = jsonObject.getString("state");
            Log.e("resultat json " , valeur);

            JSONObject utilisateur = jsonObject.getJSONObject("utilisateur");
            identifiant = utilisateur.getString("id");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Recuperation des activités dont l'utilisateur est membre
        String url = "/Android/recupMembre.php?utilisateur="+identifiant;
        AccesBD acbd = new AccesBD();
        acbd.execute(url);
        try {
            acbd.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Log.e("reponse recupMembre", rep);

        try {
            JSONObject jsonObject = new JSONObject(rep);
            valeur = jsonObject.getString("state");
            //Log.e("resultat json membre" , valeur);
            if(valeur.equals("0")){
                Toast.makeText(getActivity(), R.string.pasActivite, Toast.LENGTH_LONG).show();
                String chaine = getString(R.string.pasActivite);
                listAdapter.add(chaine);
            }
            else{
                JSONArray jsonActivites = jsonObject.getJSONArray("Activites");
                int tailleListe = jsonActivites.length();
                //Log.e("nb activite" , tailleListe + "");

                //String chaine = "Vous avez " + tailleListe + " activités";

                for(int i = 0; i < tailleListe; i++){
                    JSONObject c = jsonActivites.getJSONObject(i);
                    String idActivite = c.getString("Activite");
                    //Log.e("object",idActivite + "");

                    //Recuperation de l'activité
                    String urlActivite = "/Android/recupActivite.php?activite="+idActivite;
                    AccesBD activiteAcces = new AccesBD();
                    activiteAcces.execute(urlActivite);
                    try {
                        activiteAcces.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    //Log.e("reponse recupActivite", rep);
                    JSONObject jsonActivite = new JSONObject(rep);
                    valeur = jsonActivite.getString("state");
                    JSONArray jsonActiviteInfo = jsonActivite.getJSONArray("activite");
                    //Log.e("obNomActivite", jsonActiviteInfo + "");

                    JSONObject objMonActivite = jsonActiviteInfo.getJSONObject(0);
                    //Log.e("obNomActivite", objNomActivite + "");
                    String nomActivite = objMonActivite.getString("nom activite");
                    String id = objMonActivite.getString("id activite");
                    String description = objMonActivite.getString("description");
                    String date = objMonActivite.getString("date");
                    String type = objMonActivite.getString("type");
                    String  prop = objMonActivite.getString("id utilisateur");

                    listAdapter.add(nomActivite);
                    String liste[] = {id, nomActivite, description, date, type, prop};
                    listActivite.add(liste); ;

                }
                //listAdapter.add(chaine);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }



        setListAdapter(listAdapter);

        // Check to see if we have a frame in which to embed the details
        // fragment directly in the containing UI.
       View detailsFrame = getActivity().findViewById(R.id.fragment);
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;
        /*
        boolean a = detailsFrame == null;
        //boolean b = detailsFrame.getVisibility() == View.VISIBLE;
        Log.e("TEST1:","##############################detailsFrame == null-->"+a);
       // Log.e("TEST2:","##############################-->detailsFrame.getVisibility() == View.VISIBLE"+b);
*/
        if (savedInstanceState != null) {
            // Restore last state for checked position.
            mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
        }

        if (mDualPane) {
            // In dual-pane mode, the list view highlights the selected item.
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            // Make sure our UI is in the correct state.
            showDetails(mCurCheckPosition);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mCurCheckPosition);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        showDetails(position);
    }

    /**
     * Helper function to show the details of a selected item, either by
     * displaying a fragment in-place in the current UI, or starting a
     * whole new activity in which it is displayed.
     */
    void showDetails(int index) {
        mCurCheckPosition = index;
        //Log.e("FRAGMENT:","###############################################VALEUR mDualPane->"+mDualPane);
        if (mDualPane) {
           // Log.e("FRAGMENT:","###########################################VALEUR mDualPane->"+mDualPane);
            // We can display everything in-place with fragments, so update
            // the list to highlight the selected item and show the data.
            getListView().setItemChecked(index, true);

            // Check what fragment is currently shown, replace if needed.
            DetailsFragment details = (DetailsFragment)
                    getFragmentManager().findFragmentById(R.id.detail);
            if (details == null || details.getShownId() != index) {

                // Make new fragment to show this selection
                String[] list = listActivite.get(index);
                details = DetailsFragment.newInstance(list, index);

                // Execute a transaction, replacing any existing fragment
                // with this one inside the frame.
                FragmentTransaction ft = getFragmentManager().beginTransaction();

                ft.replace(R.id.details, details);

                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }

        } else {
            // Otherwise we need to launch a new activity to display
            // the dialog fragment with selected text.
            Intent intent = new Intent();
            intent.setClass(getActivity(), DetailsActivity.class);
            String[] list = listActivite.get(index);
            intent.putExtra("list", list);
            intent.putExtra("index", index);
            startActivity(intent);
        }
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
                Log.e("REPONSE FRAGMENT", rep);
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
