package com.example.usuario.memoria;

import android.content.Intent;
import android.os.Bundle;
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
    }
}
