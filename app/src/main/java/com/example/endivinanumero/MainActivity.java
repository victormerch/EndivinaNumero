package com.example.endivinanumero;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private int intentos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        int numero = (int)(Math.random()*100+1);
        intentos = 0;


        final EditText numeroEditText = (EditText) findViewById(R.id.editTextNumber);

        final Button button = findViewById(R.id.button);
        final TextView aviso = (TextView) findViewById(R.id.aviso);
        final TextView textIntent = (TextView) findViewById(R.id.textIntent);

        textIntent.setText(String.valueOf(intentos));
        button.setOnClickListener(new View.OnClickListener() {
            //Si clica y no ha rellenado nada hay que hacer una condicion para que no pete
            public void onClick(View v) {

                try{

                    //Cogemos el numero de el EditTextNumber
                    int numero2 = Integer.parseInt(numeroEditText.getText().toString());
                    numeroEditText.getText().clear();

                    System.out.println("-Numero random: "+numero+"\n-Numero ecogido: "+numero2);
                    //Comprobamos si coincide con el numero random de entre el 1 al 100
                    if (numero==numero2){

                        aviso.setTextColor(Color.GREEN);
                        aviso.setText("Numero Correcto!");
                    }else{
                        intentos++;
                        textIntent.setText(String.valueOf(intentos));
                        aviso.setTextColor(Color.RED);
                        if (numero2>numero){
                            aviso.setText(numero2+" es mas grande que el numero escogido");
                        }else{
                            aviso.setText(numero2+" es mas pequeño que el numero escogido");
                        }

                    }
                }catch(Exception E){

                    Context context = getApplicationContext();

                    Toast.makeText(context,"Tienes que introducir un numero",Toast.LENGTH_SHORT).show();
                }

            }

        });


    }




}