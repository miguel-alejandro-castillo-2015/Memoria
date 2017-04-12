package com.example.usuario.memoria;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.Chronometer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by BGH on 22/02/2017.
 */

public class Preferences extends AppCompatPreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home )
        {

            Set<String> imagenes = new HashSet<String>();
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String[] titulos_imagenes = getResources().getStringArray(R.array.titulos_imagenes);
            imagenes.addAll(sharedPref.getStringSet("imagenes",new HashSet<String>(Arrays.asList(titulos_imagenes))));
            if(imagenes.size() > 0) {
                finish();
                return true;
            }
            else{
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setCancelable(false).setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setMessage("Seleccione al menos una imagen para poder jugar");
                alertDialog.show();
            }

        }
        return super.onOptionsItemSelected(item);
    }


}
