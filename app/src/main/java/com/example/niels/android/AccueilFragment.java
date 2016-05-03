package com.example.niels.android;

import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.niels.bdd.Activite;
import com.example.niels.bdd.BddActivite;
import com.example.niels.bdd.BddMembreActivite;
import com.example.niels.bdd.BddUser;
import com.example.niels.bdd.MembreActivite;
import com.example.niels.bdd.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Lecture de la base de données pour avoir les activités de l'utilisateur
        String[] contact = new String[] {};
        /*
                "Venus a 555", "Earth zds 444", "Mars elsa",
                "Jupiter", "Saturn", "Uranus", "Neptune"};*/
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

        List<Activite> ac = dbActivite.getAllActivite();
        Log.e("taille liste activite", ac.size() + "");
        for (int i = 0; i < ac.size(); i++) {
            Log.e("id activite",ac.get(i).get_idActivite()+"");
            //Date d = u.get(i).get_date();
            Log.e("nom activite", ac.get(i).get_nomActivite());
            Log.e("id utilisateur ", ac.get(i).get_idUtilisateur()+"");
            //Log.e("date ", u.get(i).get_date() + " ");
        }

        List<MembreActivite> listMa = dbMembre.getMembreByIdUser(u.get_id());
        if(listMa == null){
            String c = getString(R.string.pasActivite);
            listAdapter.add(c);
        }
        else {
            //Log.e("nombre actvité de l'utilisateur", listMa.size()+"");
            for (int i = 0; i < listMa.size(); i++) {
                MembreActivite ma = listMa.get(i);
                Log.e("id membre", ma.get_idActivite()+"");
                Activite a = dbActivite.getActivitebyIdActivite(ma.get_idActivite());
                /*if(a == null){
                    Log.e("activite null", "activite null");
                }
                else {
                    Log.e("activite non null", "activite non null");
                }*/
                listAdapter.add(a.get_nomActivite());
            }
        }
        // Populate list with our static array of titles.
        //listAdapter.add("Test");
        //setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1, contactList));
        setListAdapter(listAdapter);

        // Check to see if we have a frame in which to embed the details
        // fragment directly in the containing UI.
        /*View detailsFrame = getActivity().findViewById(R.id.details);
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

        if (savedInstanceState != null) {
            // Restore last state for checked position.
            mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
        }

        if (mDualPane) {
            // In dual-pane mode, the list view highlights the selected item.
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            // Make sure our UI is in the correct state.
            showDetails(mCurCheckPosition);
        }*/
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

        if (mDualPane) {
            // We can display everything in-place with fragments, so update
            // the list to highlight the selected item and show the data.
            getListView().setItemChecked(index, true);

            // Check what fragment is currently shown, replace if needed.
            DetailsFragment details = (DetailsFragment)
                    getFragmentManager().findFragmentById(R.id.detail);
            if (details == null || details.getShownIndex() != index) {
                // Make new fragment to show this selection.
                details = DetailsFragment.newInstance(index);

                // Execute a transaction, replacing any existing fragment
                // with this one inside the frame.
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                if (index == 0) {
                    ft.replace(R.id.detail, details);
                } else {
                    //ft.replace(R.id.a_item, details);
                }
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }

        } else {
            // Otherwise we need to launch a new activity to display
            // the dialog fragment with selected text.
            Intent intent = new Intent();
            intent.setClass(getActivity(), DetailsActivity.class);
            intent.putExtra("index", index);
            startActivity(intent);
        }
    }

}
