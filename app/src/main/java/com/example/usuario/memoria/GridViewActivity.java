package com.example.usuario.memoria;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
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
        setContentView(R.layout.gridlayout);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String[] titulos_imagenes = getResources().getStringArray(R.array.titulos_imagenes);
        imagenes =sharedPref.getStringSet("imagenes",new HashSet<String>(Arrays.asList(titulos_imagenes)));

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

    public void onDestroy() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putStringSet("imagenes", imagenes);
        editor.commit();
        super.onDestroy();
    }




}
