package com.example.usuario.memoria;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private String voz;
    private int nivel;
    private String imagen_ganadora;
    private List<String> lista_views;
    private TextView label;
    private MediaPlayer mp3;
    private boolean gane;
    private MyPlayer Player=new MyPlayer(this);
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        final Resources res=getResources();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        final ProgressBar progressBar=(ProgressBar)findViewById(R.id.progress_bar);
        ImageView parlante=(ImageView)this.findViewById(R.id.parlante);
        label= (TextView) this.findViewById(R.id.label);

        label.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {            }
            @Override
            public void afterTextChanged(Editable s) {
                Player.play(voz+"_"+label.getText().toString().replaceAll(" ","_"));
            }
        });

        final Integer []image_views= new Integer[] {R.id.imageView3, R.id.imageView4, R.id.imageView5, R.id.imageView6};
        this.voz = sharedPref.getString(getString(R.string.titulo_voz),getString(R.string.default_voz));

        this.nivel = Integer.parseInt(sharedPref.getString(getString(R.string.titulo_dificultad),getString(R.string.default_dificultad)));
        String[] titulos_imagenes = res.getStringArray(R.array.titulos_imagenes);
        Set<String> imagenes=sharedPref.getStringSet("imagenes",new HashSet<String>(Arrays.asList(titulos_imagenes)));
        final List<String> imagenes_seleccionadas=new ArrayList<String>(imagenes);
        final List<String> aux_lista_imagenes_seleccionadas=new ArrayList<String>(imagenes_seleccionadas);

        if(imagenes_seleccionadas.size() >=  nivel) {
            progressBar.setMax(imagenes_seleccionadas.size());
            lista_views = MyRandomizer.random(imagenes_seleccionadas, nivel);
            imagen_ganadora = MyRandomizer.random(lista_views, 1).get(0);
            label.setText(imagen_ganadora.replaceAll("_"," "));

            //------------Listener del parlante---------------------------------------------
            parlante.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                   Player.play(voz+"_"+imagen_ganadora);
                }
            });
            //------------Fin del Listener del parlante---------------------------------------------

            for(int i=0; i<nivel;i++){
                ImageView imageView = (ImageView) findViewById(image_views[i]);
                imageView.setImageResource(res.getIdentifier(lista_views.get(i), "drawable", getApplicationContext().getPackageName()));
                imageView.setTag(lista_views.get(i));
                imageView.setPadding(10,10,10,10);
                //----------- Inicio del Listener de una Image View---------------------------------------------
                imageView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(final View v) {
                        gane=false;
                         int color;
                        String audio;
                        if(v.getTag().equals(imagen_ganadora)){
                            audio="relincho";
                            gane=true;
                            color=Color.GREEN;
                        }
                        else{
                            audio="resoplido";
                            color=Color.RED;
                        }
                        v.setBackgroundColor(color);
                        Player.play(audio);

                        Handler handler=new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                v.setBackgroundColor(Color.TRANSPARENT);
                                if(gane){
                                    aux_lista_imagenes_seleccionadas.remove(imagen_ganadora);
                                    progressBar.setProgress(progressBar.getProgress()+1);
                                    //pintar verde la imagen ganadora
                                }
                                else{
                                    //pìntar rojo la imagen ganadora
                                }
                                if(aux_lista_imagenes_seleccionadas.isEmpty()){
                                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                                    alertDialogBuilder.setCancelable(false).setPositiveButton("Jugar de nuevo", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //finish();
                                            //startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                            progressBar.setProgress(0);
                                            recreate();
                                            dialog.cancel();
                                        }
                                    });
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.setMessage("¡Ganaste este nivel!");
                                    alertDialog.show();
                                }else {
                                    imagen_ganadora = MyRandomizer.random(aux_lista_imagenes_seleccionadas, 1).get(0);
                                    imagenes_seleccionadas.remove(imagen_ganadora);
                                    lista_views = MyRandomizer.random(imagenes_seleccionadas, nivel - 1);
                                    lista_views.add(imagen_ganadora);
                                    lista_views = MyRandomizer.random(lista_views, nivel);
                                    imagenes_seleccionadas.add(imagen_ganadora);
                                    label.setText(imagen_ganadora.replaceAll("_"," "));
                                    for (int i = 0; i < nivel; i++) {
                                        ImageView imageView = (ImageView) findViewById(image_views[i]);
                                        imageView.setImageResource(res.getIdentifier(lista_views.get(i), "drawable", getApplicationContext().getPackageName()));
                                        imageView.setTag(lista_views.get(i));
                                    }
                                }
                            }
                        },Player.getDuration(audio));



                    }
                });
                //----------- Fin del Listener de una Image View---------------------------------------------
            }

        }
        else{
            //informar que no se seleccionaron la cantidad suficiente de  imagenes para el nivel
        }
    }

    @Override
    protected void onPostResume(){
        super.onPostResume();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String voz = sharedPref.getString(getString(R.string.titulo_voz),getString(R.string.default_voz));
        /*
        if(voz.equals("Femenino")){
            sonidos = sonidosF;
        }else if(voz.equals("Masculino")){
            sonidos = sonidosM;
        }
        */

        //this.dificultad = sharedPref.getString(getString(R.string.titulo_dificultad), "0");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.descripcion);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String dificultad = sharedPref.getString(getString(R.string.titulo_dificultad),getString(R.string.default_dificultad));
        String[] dificultades = getResources().getStringArray(R.array.titulos_dificultades);
        String nivel=dificultades[Integer.parseInt(dificultad)-1];
        item.setTitle("Memoria( nivel:"+nivel+" )");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.titulo_ajustes){
            startActivity(new Intent(getApplicationContext(), Preferences.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
