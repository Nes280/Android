package com.example.niels.android;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Elsa on 20/05/2016.
 */
public class DetailsAutresA extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            // If the screen is now in landscape mode, we can show the
            // dialog in-line with the list so we don't need this activity.
            finish();
            return;
        }

        if (savedInstanceState == null) {
            // During initial setup, plug in the details fragment.
            DetailsAutresActivites details = new DetailsAutresActivites();
            details.setArguments(getIntent().getExtras());

            getFragmentManager().beginTransaction().add(android.R.id.content, details).commit();

        }
    }
}
