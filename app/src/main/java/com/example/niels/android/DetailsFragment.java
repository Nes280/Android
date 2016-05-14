package com.example.niels.android;

import android.app.Fragment;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.niels.bdd.Activite;

import java.util.ArrayList;

/**
 * Created by Elsa on 04/04/2016.
 */
public class DetailsFragment extends Fragment{

    /**
     * Create a new instance of DetailsFragment, initialized to
     * show the text at 'index'.
     */
    public static DetailsFragment newInstance( String[] list, int index) {
        DetailsFragment f = new DetailsFragment();

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
        String[] list = getShownList();
        View detailsFrame = getActivity().findViewById(R.id.fragment);
        ScrollView scroller = new ScrollView(getActivity());
        TextView nomActivite = new TextView(getActivity());
        int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                4, getActivity().getResources().getDisplayMetrics());
        nomActivite.setPadding(padding, padding, padding, padding);
        scroller.addView(nomActivite);
        nomActivite.setText(list[1] + " "+ list[2]);
        return scroller;



    }
}
