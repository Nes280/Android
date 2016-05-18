package com.example.niels.android;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Elsa on 18/05/2016.
 */
public class DetailsAutresActivites extends Fragment {

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
        type.setPadding(20,0,0,20);

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
        ajout.setText(getString(R.string.add_event));
        //voir.setText(getString(R.string.show_events));

        /*voir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ListeEvenements.class);
                intent.putExtra("id", list[0]);
                startActivity(intent);
                //Toast.makeText(getActivity(),"Pas disponible pour le moment", Toast.LENGTH_LONG).show();

            }
        });*/
        ajout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), AjoutEvenement.class);
                intent.putExtra("id", list[0]);
                startActivity(intent);
            }
        });

        return layout;

    }

}
