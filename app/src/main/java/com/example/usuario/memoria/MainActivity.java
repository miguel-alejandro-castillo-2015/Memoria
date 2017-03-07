package com.example.usuario.memoria;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
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
    private boolean finalizoTiempo;
    private int tiempo;
    private Set<String> imagenes_seleccionadas_act=new HashSet<String>();
    private Set<String> imagenes_seleccionadas_aux=new HashSet<String>();
    private TextView texto_reloj;
    private ProgressBar progressBar;
    private MenuItem item_ajustes;
    private int id=0;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Resources res=getResources();
        finalizoTiempo=false;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        this.progressBar=(ProgressBar)findViewById(R.id.progress_bar);
        final ImageView parlante=(ImageView)this.findViewById(R.id.parlante);
        label= (TextView) this.findViewById(R.id.label);


        Integer []image_views= new Integer[] {R.id.imageView3, R.id.imageView4, R.id.imageView5, R.id.imageView6};
        this.voz = sharedPref.getString(getString(R.string.titulo_voz),getString(R.string.default_voz));
        this.nivel = Integer.parseInt(sharedPref.getString(getString(R.string.titulo_dificultad),getString(R.string.default_dificultad)));

        String []titulos_imagenes=res.getStringArray(R.array.titulos_imagenes);
        final List<String> imagenes=new ArrayList<String>(Arrays.asList(titulos_imagenes));
        this.imagenes_seleccionadas_act.addAll(sharedPref.getStringSet("imagenes",new HashSet<String>(imagenes)));
        this.imagenes_seleccionadas_aux.addAll(this.imagenes_seleccionadas_act);
        if(this.imagenes_seleccionadas_aux.size() >=  nivel) {
            imageViews=new ImageView[nivel];
            for(int i=0; i<nivel;i++)
                imageViews[i]=(ImageView)findViewById(image_views[i]);
            //progressBar.setProgress(0);
            progressBar.setMax(this.imagenes_seleccionadas_act.size());
            progressBar.getIndeterminateDrawable().setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_IN);
            imagen_ganadora = MyRandomizer.random(imagenes_seleccionadas_aux, 1).get(0);
            imagenes.remove(imagen_ganadora);
            lista_views = MyRandomizer.random(imagenes, nivel-1);
            imagenes.add(imagen_ganadora);
            lista_views.add(imagen_ganadora);
            Collections.shuffle(lista_views);


             texto_reloj = (TextView) this.findViewById(R.id.reloj);
            this.tiempo= Integer.parseInt(sharedPref.getString(getString(R.string.titulo_tiempo),getString(R.string.default_tiempo)));

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
                        private  CountDownTimer reloj=null;
                        private long tiempoRestante;
                        private boolean pausado=false;

                        private void init(int segundos){
                            if(segundos > 0) {
                                tiempoRestante = ++segundos * 1000;
                                reloj = new CountDownTimer(tiempoRestante, 1000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        tiempoRestante = millisUntilFinished;
                                        MainActivity.this.texto_reloj.setText(millisUntilFinished / 1000 + "");
                                    }

                                    @Override
                                    public void onFinish() {
                                        texto_reloj.setText("0");
                                        reloj = null;
                                       /* synchronized (MainActivity.this) {
                                            reloj = null;
                                            MainActivity.this.finalizoTiempo = true;
                                        }*/
                                        MainActivity.this.finalizoTiempo(true);
                                        imagen_ganadora = MyRandomizer.random(imagenes_seleccionadas_aux, 1).get(0);
                                        imagenes.remove(imagen_ganadora);
                                        lista_views = MyRandomizer.random(imagenes, nivel - 1);
                                        imagenes.add(imagen_ganadora);
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

                        }

                        @Override
                        public void start()
                        {
                            if(!isActive()){
                                init(tiempo);
                                if(isActive())
                                    reloj.start();
                            }
                        }

                        @Override
                        public void pause(){
                            if(isActive() &&!pausado) {
                                pausado=true;
                                reloj.cancel();
                            }
                        }

                        @Override
                        public void resume() {
                            if(isActive() && pausado) {
                                this.init((int) (tiempoRestante/1000));
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

                    timer.start();
                    if(timer.isActive())
                        MainActivity.this.finalizoTiempo(false);

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
                        int myID=MainActivity.this.getID();
                        if(!MainActivity.this.finalizoTiempo() && myID==1){
                                parlante.setClickable(false);
                                item_ajustes.setEnabled(false);
                                for (ImageView imageView : imageViews)
                                    imageView.setClickable(false);
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
                                        mp.reset();mp.release();mp = null;
                                        v.setBackgroundColor(Color.TRANSPARENT);
                                        if (gane) {
                                            progressBar.incrementProgressBy(1);
                                            imagenes_seleccionadas_aux.remove(imagen_ganadora);
                                        }
                                        if (imagenes_seleccionadas_aux.isEmpty()) {
                                            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                                            alertDialogBuilder.setCancelable(true).setPositiveButton("Avanzar de nivel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    SharedPreferences.Editor editor = sharedPref.edit();
                                                    if(nivel == 4){
                                                        editor.putString(getString(R.string.titulo_dificultad),getString(R.string.default_dificultad));
                                                    }else{
                                                        editor.putString(getString(R.string.titulo_dificultad), String.valueOf(++nivel));
                                                    }
                                                    editor.commit();
                                                    dialog.cancel();
                                                    MainActivity.super.recreate();

                                                }
                                            });
                                            alertDialogBuilder.setCancelable(true).setNegativeButton("Repetir nivel", new DialogInterface.OnClickListener(){
                                                @Override
                                                public void onClick(DialogInterface dialog, int which){
                                                    dialog.cancel();
                                                    MainActivity.super.recreate();
                                                }
                                            });
                                            AlertDialog alertDialog = alertDialogBuilder.create();
                                            alertDialog.setMessage("¡Ganaste este nivel!");
                                            alertDialog.show();
                                            progressBar.setProgress(0);
                                        } else {
                                            imagen_ganadora = MyRandomizer.random(imagenes_seleccionadas_aux, 1).get(0);
                                            imagenes.remove(imagen_ganadora);
                                            lista_views = MyRandomizer.random(imagenes, nivel - 1);
                                            imagenes.add(imagen_ganadora);
                                            lista_views.add(imagen_ganadora);
                                            Collections.shuffle(lista_views);
                                            for (int i = 0; i < nivel; i++) {
                                                imageViews[i].setImageResource(res.getIdentifier(lista_views.get(i), "drawable", getApplicationContext().getPackageName()));
                                                imageViews[i].setTag(lista_views.get(i));
                                            }
                                            label.setText(imagen_ganadora.replaceAll("_", " "));
                                        }
                                        parlante.setClickable(true);
                                        item_ajustes.setEnabled(true);
                                        for (ImageView imageView : imageViews)
                                            imageView.setClickable(true);
                                        MainActivity.this.resetID();
                                    }
                                });
                                mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                    @Override
                                    public void onPrepared(MediaPlayer mp) {
                                        mp.start();
                                    }
                                });

                            }//fin de if(!MainActivity.this.finalizoTiempo() && myID==1)

                        }

                });
                //----------- Fin del Listener de una Image View---------------------------------------------
            }
            label.setText(imagen_ganadora.replaceAll("_"," "));
        }
        else{
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
            alertDialogBuilder.setCancelable(true).setPositiveButton("Ir a pantalla de ajustes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    startActivity(new Intent(getApplicationContext(), Preferences.class));
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.setMessage("No se seleccionaron las imagenes suficientes para este nivel");
            alertDialog.show();
        }


    }
    private synchronized  int getID(){
        return ++this.id;
    }
    private synchronized void resetID(){
        this.id=0;
    }
    private synchronized boolean finalizoTiempo(){
        return this.finalizoTiempo;
    }
    private synchronized void  finalizoTiempo(boolean value){
        this.finalizoTiempo=value;
    }
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int nivel_conf = Integer.parseInt(sharedPref.getString(getString(R.string.titulo_dificultad),getString(R.string.default_dificultad)));
        Set<String> imagenes_seleccionadas_conf=sharedPref.getStringSet("imagenes",new HashSet<String>(Arrays.asList(getResources().getStringArray(R.array.titulos_imagenes))));
        int tiempo_conf= Integer.parseInt(sharedPref.getString(getString(R.string.titulo_tiempo),getString(R.string.default_tiempo)));
        if((tiempo_conf != this.tiempo)||(nivel_conf != this.nivel)||(!imagenes_seleccionadas_conf.equals( this.imagenes_seleccionadas_act))) {
            recreate();
            progressBar.setProgress(0);
        }
        else {
            String voz_conf = sharedPref.getString(getString(R.string.titulo_voz),getString(R.string.default_voz));
            if (!voz_conf.equals(this.voz))
                this.voz = voz_conf;
            timer.resume();
        }

    }

    @Override
    public void recreate(){
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.super.recreate();
            }
        },1);
    }
    @Override
    protected void onPause() {
        timer.pause();
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        timer.stop();
        timer=null;
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.descripcion);
         item_ajustes=menu.findItem(R.id.titulo_ajustes);
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
        if(id == R.id.titulo_salir){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
