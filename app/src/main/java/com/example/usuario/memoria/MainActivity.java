package com.example.usuario.memoria;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private String voz;
    private int nivel;
    private String imagen_ganadora;
    private List<String> lista_views;
    private TextView label;
    private boolean gane;
    private MyPlayer Player=new MyPlayer(this);
    private  ICountDownTimer timer=null;
    private ImageView []imageViews;
    private Boolean finalizoTiempo=false;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Resources res=getResources();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        final ProgressBar progressBar=(ProgressBar)findViewById(R.id.progress_bar);
        ImageView parlante=(ImageView)this.findViewById(R.id.parlante);
        label= (TextView) this.findViewById(R.id.label);



        Integer []image_views= new Integer[] {R.id.imageView3, R.id.imageView4, R.id.imageView5, R.id.imageView6};
        this.voz = sharedPref.getString(getString(R.string.titulo_voz),getString(R.string.default_voz));
        this.nivel = Integer.parseInt(sharedPref.getString(getString(R.string.titulo_dificultad),getString(R.string.default_dificultad)));

        String []titulos_imagenes=res.getStringArray(R.array.titulos_imagenes);
        final List<String> imagenes=new ArrayList<String>(Arrays.asList(titulos_imagenes));
        final Set<String> imagenes_seleccionadas=sharedPref.getStringSet("imagenes",new HashSet<String>(imagenes));


        if(imagenes_seleccionadas.size() >=  nivel) {
            imageViews=new ImageView[nivel];
            for(int i=0; i<nivel;i++)
                imageViews[i]=(ImageView)findViewById(image_views[i]);

            progressBar.setMax(imagenes_seleccionadas.size());
            imagen_ganadora = MyRandomizer.random(imagenes_seleccionadas, 1).get(0);
            imagenes.remove(imagen_ganadora);
            lista_views = MyRandomizer.random(imagenes, nivel-1);
            lista_views.add(imagen_ganadora);
            Collections.shuffle(lista_views);


            final TextView texto_reloj = (TextView) this.findViewById(R.id.reloj);
            final int tiempo= Integer.parseInt(sharedPref.getString(getString(R.string.titulo_tiempo),getString(R.string.default_tiempo)));
            //------------Listener del Text View ---------------------------------------------
            label.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}
                @Override
                public void afterTextChanged(Editable s) {
                    Player.play(voz+"_"+s.toString().replaceAll(" ","_"));
                    timer=new ICountDownTimer() {
                        private  CountDownTimer reloj;
                        private long tiempoRestante;
                        private boolean pausado=false;
                        private void init(long tiempoTotal, long intervalo){
                            reloj = new CountDownTimer(tiempoTotal, intervalo) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    tiempoRestante=millisUntilFinished;
                                    texto_reloj.setText(millisUntilFinished / 1000 + "");
                                }
                                @Override
                                public void onFinish() {
                                    reloj=null;
                                    synchronized (MainActivity.this) {
                                        finalizoTiempo = true;
                                    }
                                    imagen_ganadora = MyRandomizer.random(imagenes_seleccionadas, 1).get(0);
                                    imagenes.remove(imagen_ganadora);
                                    lista_views = MyRandomizer.random(imagenes, nivel - 1);
                                    lista_views.add(imagen_ganadora);
                                    Collections.shuffle(lista_views);
                                    for (int i = 0; i < nivel; i++) {
                                        imageViews[i].setImageResource(res.getIdentifier(lista_views.get(i), "drawable", getApplicationContext().getPackageName()));
                                        imageViews[i].setTag(lista_views.get(i));
                                    }
                                    label.setText(imagen_ganadora.replaceAll("_", " "));
                                }
                            };
                        }
                        @Override
                        public void start()
                        {
                            if(!isActive()){
                                this.init(tiempo * 1000, 1000);
                                reloj.start();
                            }
                        }

                        @Override
                        public void pause(){
                            if(isActive()) {
                                pausado=true;
                                reloj.cancel();
                            }
                        }

                        @Override
                        public void resume() {
                            if(isActive() && pausado) {
                                this.init(tiempoRestante, 1000);
                                reloj.start();
                                pausado=false;
                            }
                        }

                        @Override
                        public void stop() {
                            if(isActive()){
                               reloj.cancel();
                                reloj=null;
                            }
                        }

                        @Override
                        public boolean isActive() {
                            return reloj != null;
                        }
                    };

                    if(tiempo > 0) {
                        timer.start();
                        synchronized (MainActivity.this) {
                            finalizoTiempo = false;
                        }
                    }
                }
            });
            //------------Fin del Listener del Text View ---------------------------------------------

            //------------Listener del parlante---------------------------------------------
            parlante.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                   Player.play(voz+"_"+imagen_ganadora);
                }
            });
            //------------Fin del Listener del parlante---------------------------------------------

            for(int i=0; i<nivel;i++){
                imageViews[i].setImageResource(res.getIdentifier(lista_views.get(i), "drawable", getApplicationContext().getPackageName()));
                imageViews[i].setTag(lista_views.get(i));
                imageViews[i].setPadding(10,10,10,10);
                //----------- Inicio del Listener de una Image View---------------------------------------------

                imageViews[i].setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(final View v) {

                        for (ImageView imageView : imageViews)
                            imageView.setClickable(false);
                        synchronized (MainActivity.this) {
                            if(!finalizoTiempo){
                                if(timer != null)
                                   timer.stop();
                                gane = false;
                                int color;
                                String audio;
                                if (v.getTag().equals(imagen_ganadora)) {
                                    audio = "relincho";
                                    gane = true;
                                    color = Color.GREEN;
                                } else {
                                    audio = "resoplido";
                                    color = Color.RED;
                                }
                                v.setBackgroundColor(color);
                                MediaPlayer mp = Player.create(audio);
                                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mp) {
                                        mp.reset();
                                        mp.release();
                                        mp = null;
                                        v.setBackgroundColor(Color.TRANSPARENT);
                                        if (gane) {
                                            progressBar.setProgress(progressBar.getProgress() + 1);
                                            imagenes_seleccionadas.remove(imagen_ganadora);
                                        } else {
                                            //pìntar rojo la imagen ganadora
                                        }
                                        imagenes.add(imagen_ganadora);
                                        if (imagenes_seleccionadas.isEmpty()) {
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
                                        } else {
                                            imagen_ganadora = MyRandomizer.random(imagenes_seleccionadas, 1).get(0);
                                            imagenes.remove(imagen_ganadora);
                                            lista_views = MyRandomizer.random(imagenes, nivel - 1);
                                            lista_views.add(imagen_ganadora);
                                            Collections.shuffle(lista_views);

                                            for (int i = 0; i < nivel; i++) {
                                                imageViews[i].setImageResource(res.getIdentifier(lista_views.get(i), "drawable", getApplicationContext().getPackageName()));
                                                imageViews[i].setTag(lista_views.get(i));
                                            }
                                            label.setText(imagen_ganadora.replaceAll("_", " "));
                                        }
                                        for (ImageView imageView : imageViews)
                                            imageView.setClickable(true);

                                    }
                                });
                                mp.start();
                            }
                            }
                        }

                });
                //----------- Fin del Listener de una Image View---------------------------------------------
            }
            label.setText(imagen_ganadora.replaceAll("_"," "));
        }
        else{
            //informar que no se seleccionaron la cantidad suficiente de  imagenes para el nivel
        }


    }
    @Override
    protected void onResume() {
        super.onResume();
        if(timer != null && timer.isActive()){
            System.out.println("el reloj volvio del pause");
            timer.resume();
        }

    }
    @Override
    protected void onPause() {
        super.onPause();
        if(timer != null && timer.isActive()){
            System.out.println("el reloj se pauso");
            timer.pause();
        }

    }
    @Override
    protected void onPostResume(){
        super.onPostResume();
        //SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //String voz = sharedPref.getString(getString(R.string.titulo_voz),getString(R.string.default_voz));
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
