package com.example.usuario.memoria;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Usuario on 2/13/2017.
 */

public class ImageAdapter extends BaseAdapter {
    private Context c;
    private List<String> imagenes;
    private List<Integer> estados;

    public ImageAdapter(Context c,List<String> imagenes) {
        this.c= c;
        this.imagenes=imagenes;
    }

    public int getCount() {
        return this.imagenes.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position){
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(c);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
            //imageView.invalidate();
            if(imageView.isSelected()){
                imageView.setBackgroundColor(Color.TRANSPARENT);
                imageView.setSelected(false);
            }else{
                imageView.setBackgroundColor(Color.BLUE);
                imageView.setSelected(true);
            }
        }

        imageView.setImageResource(c.getResources().getIdentifier(this.imagenes.get(position),"drawable",c.getPackageName()));
        imageView.setId(c.getResources().getIdentifier(this.imagenes.get(position),"drawable",c.getPackageName()));

        return imageView;
    }




}
