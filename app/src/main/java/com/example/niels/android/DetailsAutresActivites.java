package com.example.niels.android;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.niels.Code.getExemple;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by Elsa on 18/05/2016.
 */
public class DetailsAutresActivites extends Fragment {

    String rep = null;

    public static DetailsAutresActivites newInstance( String[] list, int index) {
        DetailsAutresActivites f = new DetailsAutresActivites();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putStringArray("list", list );
        args.putInt("index", index);
        f.setArguments(args);

        return f;
    }

    public String[] getShownList() {
        return getArguments().getStringArray("list");
    }

    public int getShownId() {
        return getArguments().getInt("index");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //TODO recuperer l'acivité en fonction de son id
        if (container == null) {

            return null;
        }
        final String[] list = getShownList();
        //ScrollView scroller = new ScrollView(getActivity());
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        TextView nomActivite = new TextView(getActivity());
        TextView description = new TextView(getActivity());
        TextView date = new TextView(getActivity());
        TextView type = new TextView(getActivity());
        Button ajout = new Button(getActivity());
        //Button voir = new Button(getActivity());
        int taille = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                4, getActivity().getResources().getDisplayMetrics());
/*
        nomActivite.setPadding(padding, padding, padding, padding);
        description.setPadding(padding, padding, padding, padding);
        date.setPadding(padding+20,padding+20,padding+20,padding+20);
        type.setPadding(padding+30,padding+30,padding+30,padding+30);*/

        nomActivite.setPadding(20,5,0,5);
        description.setPadding(20,0,0,0);
        date.setPadding(20, 0, 0, 0);
        type.setPadding(20, 0, 0, 20);

        nomActivite.setTextSize(20);
        nomActivite.setTextColor(Color.parseColor("#0099CC"));

        layout.addView(nomActivite);
        layout.addView(description);
        layout.addView(date);
        layout.addView(type);
        layout.addView(ajout);
        //layout.addView(voir);

        nomActivite.setText(list[1]);
        description.setText(getString(R.string.description)+": "+list[2]);
        date.setText(getString(R.string.date)+": "+list[3]);
        String typePublication;
        if (list[4].equals("1")) typePublication = getString(R.string.radio_private)+"";
        else typePublication = getString(R.string.radio_public)+"";
        type.setText(getString(R.string.type)+": "+typePublication);
        //A changer
        //ajout.setText(getString(R.string.add_event));
        ajout.setText("Rejoindre");

        ajout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                //Date du jours
                String format = "dd/MM/yy H:mm:ss";
                java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat(format);
                java.util.Date dateJava = new java.util.Date();
                String date = formater.format(dateJava);

                String url3 = "/Android/ajoutMembre.php?utilisateur=" + list[6] + "&activite=" + list[0] +
                        "&date=" + date;
                AccesBD acMembre = new AccesBD();
                acMembre.execute(url3);
                try {
                    acMembre.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                intent.setClass(getActivity(), Accueil_Utilisateur.class);
                //intent.putExtra("id", list[0]);
                startActivity(intent);
            }
        });

        return layout;

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
