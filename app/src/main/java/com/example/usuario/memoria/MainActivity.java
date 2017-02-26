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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private Integer inicial = -1;
    private Integer actual;
    private Integer contador = 0;
    private String dificultad;
    private Resources res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        res = getApplicationContext().getResources();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        String[] titulos_imagenes = getResources().getStringArray(R.array.titulos_imagenes);
        Set<String> images=sharedPref.getStringSet("imagenes",new HashSet<String>(Arrays.asList(titulos_imagenes)));
        final List<String> imagenesGuardadas=new ArrayList<String>(images);

        SharedPreferences.Editor editor = sharedPref.edit();
        if(sharedPref.contains("actual") && sharedPref.contains("inicial")){
            actual = sharedPref.getInt("actual", 0) + 1;

            if(actual == imagenesGuardadas.size()){
                actual = 0;
            }
            inicial = sharedPref.getInt("inicial", 0);
            if(actual == inicial){
                this.contador = 0;
                this.actual = null;
                this.inicial = -1;
                editor = sharedPref.edit();
                editor.remove("inicial");
                editor.remove("actual");
                editor.remove("contador");
                Integer dific = Integer.parseInt(this.dificultad);
                if(dific + 1 != 4) {
                    dific++;
                    editor.putString("dificultad",dific.toString() );
                }
                editor.commit();
                //gana el juego
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setCancelable(false).setPositiveButton("Jugar de nuevo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                        //startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        recreate();
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setMessage("¡Ganaste este nivel!");
                alertDialog.show();

            }
            contador = sharedPref.getInt("contador", 0) + 1;
        }

        if(inicial == -1){
            Random random = new Random();
            inicial = random.nextInt(imagenesGuardadas.size()); //asignar random
            actual = inicial;
            contador++;
        }







        final String voz = sharedPref.getString(getString(R.string.titulo_voz),getString(R.string.default_voz));




        Integer[] ids = {};

        this.dificultad = sharedPref.getString(getString(R.string.titulo_dificultad), "0");
        int limite = 1;
        switch (dificultad){
            case "0":
                ids = new Integer[] {R.id.imageView3};
                limite = 1;
                break;
            case "1":
                ids = new Integer[] {R.id.imageView3, R.id.imageView4};
                limite = 2;
                break;
            case "2":
                ids = new Integer[] {R.id.imageView3, R.id.imageView4, R.id.imageView5};
                limite = 3;
                break;
            case "3":
                ids = new Integer[] {R.id.imageView3, R.id.imageView4, R.id.imageView5, R.id.imageView6};
                limite = 4;
                break;
        }




        Random random=new Random();
        Integer pos;
        final List<Integer> posiciones=new ArrayList<Integer>();
        posiciones.add(actual);
        for(int i=0;i < limite-1;i++){
            pos=random.nextInt(imagenesGuardadas.size());
            while(posiciones.contains(pos) || pos == actual){
                pos=random.nextInt(imagenesGuardadas.size());
            }
            posiciones.add(pos);
        }


        final int indiceCorrecto= actual;
        TextView label= (TextView) this.findViewById(R.id.label);
        label.setText(imagenesGuardadas.get(actual).replaceAll("_"," "));
        ImageView parlante=(ImageView)this.findViewById(R.id.parlante);

                parlante.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view){
                        String audio=voz+"_"+imagenesGuardadas.get(actual);
                        MediaPlayer mp=MediaPlayer.create(MainActivity.this.getApplicationContext(),res.getIdentifier(audio,"raw", getApplicationContext().getPackageName()));
                        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                mp.reset();
                                mp.release();
                                mp = null;
                            }
                        });
                        mp.start();
                    }
                });




        TextView contador = (TextView) this.findViewById(R.id.contador);
        contador.setText("Imagen "+this.contador+" de "+imagenesGuardadas.size());

        // PARA VERSION FINAL, RANDOMIZAR EL PRIMERO Y QUEDARSE CON ESE INDICE PARA REFERENCIAR EL ELEMENTO CORRECTO EN LOS ARREGLOS


        int total = Integer.parseInt(dificultad) + 1;

        for(int i=0; i<total;i++){
            ImageView imageView = (ImageView) findViewById(ids[i]);
            imageView.setImageResource(res.getIdentifier(imagenesGuardadas.get(posiciones.get(i)), "drawable", getApplicationContext().getPackageName()));
            imageView.setTag(res.getIdentifier(imagenesGuardadas.get(posiciones.get(i)), "drawable", getApplicationContext().getPackageName()));
            imageView.setPadding(10,10,10,10);
            final Integer contadorTemp = this.contador;
            imageView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(final View view) {
                    view.setBackgroundColor(Color.RED);
                    MediaPlayer mp = null;
                    boolean gano;
                    String mensaje;
                    if((int) view.getTag()==res.getIdentifier(imagenesGuardadas.get(actual), "drawable", getApplicationContext().getPackageName())){
                        mp = MediaPlayer.create(getApplicationContext(), R.raw.relincho);
                        mensaje = "¡MUY BIEN!";
                        gano=true;
                    }
                    else{
                        mp = MediaPlayer.create(getApplicationContext(), R.raw.resoplido);
                        mensaje = "Lo siento, volve a intentarlo";
                        gano=false;
                    }
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.reset();
                            mp.release();
                            mp = null;
                        }
                    });
                    mp.start();

                    Handler h=new Handler();
                    h.postAtTime(new Runnable() {
                        @Override
                        public void run() {

                            view.setBackgroundColor(Color.TRANSPARENT);
                            
                        }
                    },2000);

                    if(gano){
                        recreate();
                        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putInt("actual", actual);
                        editor.putInt("inicial", inicial);
                        editor.putInt("contador", contadorTemp);
                        editor.commit();
                    }


                   // AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                   // if(gano){
                    //    alertDialogBuilder.setCancelable(false).setPositiveButton("Siguiente nivel", new DialogInterface.OnClickListener() {
                     //       @Override
                     //       public void onClick(DialogInterface dialog, int which) {
                      //          finish();
                        //        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                       //     }
                      //  });
                       // AlertDialog alertDialog = alertDialogBuilder.create();
                       // alertDialog.show();
                   // }
                }
            });
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

        this.dificultad = sharedPref.getString(getString(R.string.titulo_dificultad), "0");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.descripcion);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String dificultad = sharedPref.getString(getString(R.string.titulo_dificultad),getString(R.string.default_dificultad));
        String[] dificultades = getResources().getStringArray(R.array.titulos_dificultades);
        String nivel=dificultades[Integer.parseInt(dificultad)];
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
