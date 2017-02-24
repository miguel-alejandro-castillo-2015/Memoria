package com.example.usuario.memoria;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Arrays;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Created by Usuario on 2/13/2017.
 */

public class GridViewActivity  extends AppCompatActivity{
    private Set<Integer> im;
    private List<String> imagenes;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gridlayout);

        Gson gson = new Gson();
        String json;
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        if(!sharedPref.contains("imagenes")){
            SharedPreferences.Editor editor = sharedPref.edit();
            Set<String> textos = new HashSet<String>(Arrays.asList("aros","arriador","bajo_montura",
                    "bozal", "cabezada","casco","cascos","cepillo","cinchon_de_volteo",
                    "cola","crines","cuerda","escarba_vasos","fusta",
                    "matra","montura","monturin","ojos","orejas",
                    "palos","pasto","pelota","rasqueta","riendas","zanahoria"));
            json = gson.toJson(textos);
            editor.putString("imagenes", json);
            editor.commit();
        }
        //json=sharedPref.getString("imagenes",gson.toJson(MainActivity.imagenes));

        json=sharedPref.getString("imagenes",gson.toJson(MainActivity.imagenes));



        imagenes=gson.fromJson(json, new TypeToken<ArrayList<String>>() {}.getType());

        if(imagenes == null){
            imagenes=new ArrayList<String>();
        }
        System.out.println("size imagenes"+imagenes.size());
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this,imagenes));
        gridview.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);


       /*for(int position=0;position <  imagenes.size();position++) {
           ImageView image= (ImageView) findViewById((int)gridview.getAdapter().getItem(position));
           image.setSelected(true);
           image.setBackgroundColor(Color.BLUE);
           image.invalidate();
        }*/




        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                ImageView imageView=(ImageView) v;
                if(imageView.isSelected()){
                    System.out.println("entre por aca");
                    imageView.setBackgroundColor(Color.TRANSPARENT);
                    imageView.setSelected(false);
                    imagenes.remove(position);
                }else{
                    imageView.setSelected(true);
                    imageView.setBackgroundColor(Color.BLUE);
                    //if(!imagenes.contains(id))
                      //  imagenes.add(v.getId());
                }

            }
        });
    }

    public void onDestroy(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        Set<String> set=new HashSet<String>();
        set.addAll(imagenes);
        String json = gson.toJson(set);
        editor.putString("imagenes",json);
        editor.commit();
        super.onDestroy();
    }
}
