package com.example.usuario.memoria;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;





/**
 * Created by Usuario on 2/13/2017.
 */

public class GridViewActivity extends AppCompatActivity {
    private Set<String> imagenes = new HashSet<String>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setContentView(R.layout.gridlayout);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String[] titulos_imagenes = getResources().getStringArray(R.array.titulos_imagenes);
        this.imagenes.addAll(sharedPref.getStringSet("imagenes",new HashSet<String>(Arrays.asList(titulos_imagenes))));

        final GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this, imagenes));
        gridview.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);



        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                ImageView imageView = (ImageView) v;
                String titulo_imagen = (String) imageView.getTag();
                if (imagenes.contains(titulo_imagen))
                    imagenes.remove(titulo_imagen);
                else
                    imagenes.add(titulo_imagen);
                gridview.getAdapter().getView(position,v,parent);

            }
        });

    }
    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //if(item.getItemId() == android.R.id.home )
        //{
          if(this.imagenes.size() > 0) {
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
        //}
        return super.onOptionsItemSelected(item);
    }
    */
    @Override
    public void onPause(){
        super.onPause();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putStringSet("imagenes", this.imagenes);
        editor.commit();

    }




}
