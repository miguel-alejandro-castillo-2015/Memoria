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
import java.util.List;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MainActivity extends AppCompatActivity {


    private int idSonido;


    private String []textos={"aros","arriador","bajo montura",
            "bozal", "cabezada","casco","cascos","cepillo","cinchon de volteo",
            "cola","crines","cuerda","escarba vasos","fusta",
            "matra","montura","monturin","ojos","orejas",
            "palos","pasto","pelota","rasqueta","riendas","zanahoria"};

    private List<String> listaTextos = new ArrayList<String>(Arrays.asList("aros","arreador","bajo montura",
            "bozal", "cabezada","casco","cascos","cepillo","cinchón de volteo",
            "cola","crines","cuerda","escarba vasos","fusta",
            "matra","montura","monturín","ojos","orejas",
            "palos","pasto","pelota","rasqueta","riendas","zanahoria"));

    private Integer [] sonidos;

    private List<Integer> listaSonidos = new ArrayList<Integer>();

    private Integer []sonidosF={R.raw.aros_fem,R.raw.arriador_fem,R.raw.bajo_montura_fem,
            R.raw.bozal_fem, R.raw.cabezada_fem,R.raw.casco_fem,R.raw.cascos_fem,R.raw.cepillo_fem,R.raw.cinchon_de_volteo_fem,
            R.raw.cola_fem,R.raw.crines_fem,R.raw.cuerda_fem,R.raw.escarba_vasos_fem,R.raw.fusta_fem,
            R.raw.matra_fem,R.raw.montura_fem,R.raw.monturin_fem,R.raw.ojo_fem,R.raw.orejas_fem,
            R.raw.palos_fem,R.raw.pasto_fem, R.raw.pelota_fem,R.raw.rasqueta_fem,R.raw.riendas_fem,R.raw.zanahoria_fem};

    private List<Integer> listaSonidosF = new ArrayList<Integer>(Arrays.asList(R.raw.aros_fem,R.raw.arriador_fem,R.raw.bajo_montura_fem,
            R.raw.bozal_fem, R.raw.cabezada_fem,R.raw.casco_fem,R.raw.cascos_fem,R.raw.cepillo_fem,R.raw.cinchon_de_volteo_fem,
            R.raw.cola_fem,R.raw.crines_fem,R.raw.cuerda_fem,R.raw.escarba_vasos_fem,R.raw.fusta_fem,
            R.raw.matra_fem,R.raw.montura_fem,R.raw.monturin_fem,R.raw.ojo_fem,R.raw.orejas_fem,
            R.raw.palos_fem,R.raw.pasto_fem, R.raw.pelota_fem,R.raw.rasqueta_fem,R.raw.riendas_fem,R.raw.zanahoria_fem));

    private Integer []sonidosM={R.raw.aros_masc, R.raw.arriador_masc, R.raw.bajo_montura_masc,
            R.raw.bozal_masc, R.raw.cabezada_masc, R.raw.casco_masc, R.raw.cascos_masc, R.raw.cepillo_masc, R.raw.cinchon_de_volteo_masc,
            R.raw.cola_masc,R.raw.crines_masc,R.raw.cuerda_masc,R.raw.escarba_vasos_masc,R.raw.fusta_masc,
            R.raw.matra_masc,R.raw.montura_masc,R.raw.monturin_masc,R.raw.ojo_masc,R.raw.orejas_masc,
            R.raw.palos_masc,R.raw.pasto_masc, R.raw.pelota_fem,R.raw.rasqueta_masc,R.raw.riendas_masc,R.raw.zanahoria_masc};

    private List<Integer> listaSonidosM = new ArrayList<Integer>(Arrays.asList(R.raw.aros_masc, R.raw.arriador_masc, R.raw.bajo_montura_masc,
            R.raw.bozal_masc, R.raw.cabezada_masc, R.raw.casco_masc, R.raw.cascos_masc, R.raw.cepillo_masc, R.raw.cinchon_de_volteo_masc,
            R.raw.cola_masc,R.raw.crines_masc,R.raw.cuerda_masc,R.raw.escarba_vasos_masc,R.raw.fusta_masc,
            R.raw.matra_masc,R.raw.montura_masc,R.raw.monturin_masc,R.raw.ojo_masc,R.raw.orejas_masc,
            R.raw.palos_masc,R.raw.pasto_masc, R.raw.pelota_fem,R.raw.rasqueta_masc,R.raw.riendas_masc,R.raw.zanahoria_masc));

    public static Integer []imagenes= {R.drawable.aros,R.drawable.arriador,R.drawable.bajo_montura,
            R.drawable.bozal,R.drawable.cabezada,R.drawable.casco, R.drawable.cascos,R.drawable.cepillo,R.drawable.cinchon_de_volteo,
            R.drawable.cola,R.drawable.crines,R.drawable.cuerda,R.drawable.escarba_vasos, R.drawable.fusta,
            R.drawable.matra,R.drawable.montura,R.drawable.monturin, R.drawable.ojo, R.drawable.orejas,
            R.drawable.palos, R.drawable.pasto, R.drawable.pelota, R.drawable.rasqueta, R.drawable.riendas, R.drawable.zanahoria};

    private List<Integer> listaImagenes = new ArrayList<Integer>(Arrays.asList(R.drawable.aros,R.drawable.arriador,R.drawable.bajo_montura,
            R.drawable.bozal,R.drawable.cabezada,R.drawable.casco, R.drawable.cascos,R.drawable.cepillo,R.drawable.cinchon_de_volteo,
            R.drawable.cola,R.drawable.crines,R.drawable.cuerda,R.drawable.escarba_vasos, R.drawable.fusta,
            R.drawable.matra,R.drawable.montura,R.drawable.monturin, R.drawable.ojo, R.drawable.orejas,
            R.drawable.palos, R.drawable.pasto, R.drawable.pelota, R.drawable.rasqueta, R.drawable.riendas, R.drawable.zanahoria));

    private Integer inicial = -1;
    private Integer actual;
    private Integer contador = 0;
    private String dificultad;
    private Integer[] ids;

    private Resources res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        res = getApplicationContext().getResources();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Gson gson=new Gson();
        if(sharedPref.contains("imagenes")){
            String json=sharedPref.getString("imagenes",gson.toJson(imagenes));

            final List<String> imagenesGuardadas =gson.fromJson(json,new TypeToken<ArrayList<String>>() {}.getType());
        }

        final List<String> imagenesGuardadas = new ArrayList<String>(Arrays.asList("aros","arriador","bajo_montura",
                "bozal", "cabezada","casco","cascos","cepillo","cinchon_de_volteo",
                "cola","crines","cuerda","escarba_vasos","fusta",
                "matra","montura","monturin","ojo","orejas",
                "palos","pasto","pelota","rasqueta","riendas","zanahoria"));

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("imagenes", gson.toJson(imagenesGuardadas));
        editor.commit();

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







        String voz = sharedPref.getString(getString(R.string.titulo_voz),getString(R.string.default_voz));

        if(voz.equals("Femenino")){
            sonidos = sonidosF;
        }else if(voz.equals("Masculino")){
            sonidos = sonidosM;
        }
        //------------------


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
        label.setText(imagenesGuardadas.get(actual));
        ImageView parlante=(ImageView)this.findViewById(R.id.parlante);
        if(voz.equals("Masculino")){
            parlante.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    MediaPlayer mp=MediaPlayer.create(MainActivity.this.getApplicationContext(),res.getIdentifier(imagenesGuardadas.get(actual)+"_masc","raw", getApplicationContext().getPackageName()));
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
        }else{
            if(voz.equals("Femenino")){
                parlante.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view){
                        MediaPlayer mp=MediaPlayer.create(MainActivity.this.getApplicationContext(),res.getIdentifier(imagenesGuardadas.get(actual)+"_fem","raw", getApplicationContext().getPackageName()));
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
            }
        }


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

        if(voz.equals("Femenino")){
            sonidos = sonidosF;
        }else if(voz.equals("Masculino")){
            sonidos = sonidosM;
        }

        this.dificultad = sharedPref.getString(getString(R.string.titulo_dificultad), "0");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.descripcion);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String dificultad = sharedPref.getString(getString(R.string.titulo_dificultad),getString(R.string.default_dificultad));
        switch (dificultad){
            case "0":
                item.setTitle("Memoria( nivel:inicial )");
                break;
            case "1":
                item.setTitle("Memoria( nivel:intermedio )");
                break;
            case "2":
                item.setTitle("Memoria( nivel:avanzado )");
                break;
            case "3":
                item.setTitle("Memoria( nivel:experto )");
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.action_settings){
            startActivity(new Intent(getApplicationContext(), Preferences.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
