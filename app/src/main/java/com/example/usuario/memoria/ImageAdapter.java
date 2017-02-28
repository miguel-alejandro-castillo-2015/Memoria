package com.example.usuario.memoria;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by Usuario on 2/13/2017.
 */

public class ImageAdapter extends BaseAdapter {
    private Context c;
    private Set<String> imagenes_seleccionadas;
    private final List<String> imagenes;


    public ImageAdapter(Context c,Set<String> imagenes_seleccionadas) {
        this.c= c;
        this.imagenes_seleccionadas=imagenes_seleccionadas;
        this.imagenes=Arrays.asList(c.getResources().getStringArray(R.array.titulos_imagenes));
    }

    public int getCount() {
        return this.imagenes.size();
    }

    public Object getItem(int position) { return null;
    }

    public long getItemId(int position){return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(c);
            imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView)convertView;
        }

        if(this.imagenes_seleccionadas.contains((String)this.imagenes.get(position)) )
            imageView.setBackgroundColor(Color.BLUE);
        else
           imageView.setBackgroundColor(Color.TRANSPARENT);

        imageView.setImageResource(c.getResources().getIdentifier(this.imagenes.get(position),"drawable",c.getPackageName()));
        //imageView.setId(c.getResources().getIdentifier(this.imagenes.get(position),"drawable",c.getPackageName()));
        imageView.setTag(this.imagenes.get(position));
        return imageView;
    }




}
