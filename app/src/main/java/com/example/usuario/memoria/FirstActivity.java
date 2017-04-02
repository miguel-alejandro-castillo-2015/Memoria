package com.example.usuario.memoria;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        Button boton1 = (Button) findViewById(R.id.button);

        boton1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        Button boton2 = (Button) findViewById(R.id.button2);

        boton2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(getApplicationContext(), Preferences.class));
            }
        });

        Button boton3 = (Button) findViewById(R.id.button3);

        boton3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                 finish();
            }
        });

        Button boton4 = (Button) findViewById(R.id.button4);

        boton4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FirstActivity.this);
                alertDialogBuilder.setCancelable(true).setPositiveButton("Cerrar", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setMessage("Esta aplicacion fue desarrollada por los alumnos Alvarado Cristian y Castillo Miguel para el CEDICA, en el marco de la materia Laboratorio de Software");
                alertDialog.show();
            }
        });
    }
}
