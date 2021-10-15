package com.example.endivinanumero;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
//==Tareas pendientes==
//-Segundo activity
public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "con.example.endivinanumero";
    private int intentos;
    private int numero;
    private boolean isOn = false;
    private int mili =0,seg = 0, min =0;
    private String m ="",s = "",mi="";
    private Handler h = new Handler();
    private Editable nameUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        numero = (int)(Math.random()*100+1);
        intentos = 0;
        mili =0;
        seg = 0;
        min =0;
        TextView cronom = (TextView) findViewById(R.id.crono2);
        //Algoritmo del cronometro
        Thread cronos = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    if (isOn) {

                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                           System.out.println("A");
                        }
                        mili ++;
                        if(mili == 999){
                            seg ++;
                            mili = 0;
                        }
                        if(seg == 99){
                            min ++;
                            seg = 0;
                        }
                        h.post(new Runnable() {
                            @Override
                            public void run() {
                                String m ="",s = "",mi="";
                                if (mili<10) {
                                    m = "00" + mili;
                                }else if(mili<100){
                                    m = "0" + mili;
                                }else{
                                    m = "" + mili;
                                }
                                if(seg<10){
                                    s = "0" + seg;
                                }else {
                                    s = "" +seg;
                                }
                                if(min<10){
                                    mi = "0" +min;
                                }else{
                                    mi = "" +min;
                                }
                                cronom.setText(mi+":"+s+":"+m);
                            }

                        });

                    }
                }
            }
        }
        );
        cronos.start();
        final EditText numeroEditText = (EditText) findViewById(R.id.editTextNumber);

        final Button button = findViewById(R.id.button);
        final TextView aviso = (TextView) findViewById(R.id.aviso);
        final TextView textIntent = (TextView) findViewById(R.id.textIntent);
        final Switch s = (Switch) findViewById(R.id.switch1);
        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (s.isChecked()){
                    cronom.setVisibility(View.INVISIBLE);
                }else{
                    cronom.setVisibility(View.VISIBLE);
                }
            }
        });

        textIntent.setText(String.valueOf(intentos));
        button.setOnClickListener(new View.OnClickListener() {
            //Si clica y no ha rellenado nada hay que hacer una condicion para que no pete
            public void onClick(View v) {

                try{

                    //Cogemos el numero de el EditTextNumber
                    int numero2 = Integer.parseInt(numeroEditText.getText().toString());
                    numeroEditText.getText().clear();

                    if(intentos == 0){
                        isOn = true;
                    }
                    System.out.println("-Numero random: "+numero+"\n-Numero ecogido: "+numero2);
                    //Comprobamos si coincide con el numero random de entre el 1 al 100
                    if (numero==numero2){

                        isOn = false;
                        aviso.setTextColor(Color.GREEN);
                        aviso.setText("Numero Correcto!");


                        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

                        alert.setTitle("Has Guanyat!");
                        alert.setMessage("-Numero secret: "+numero+
                                "\n-Numero de intents: "+String.valueOf(intentos+1)+
                                "\n-Temps: "+min+":"+seg+":"+mili+
                                "\nIntrodueix el teu nom per afegirlo al ranking:");

                        // Set an EditText view to get user input
                        final EditText input = new EditText(MainActivity.this);
                        alert.setView(input);

                        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                nameUser = input.getText();
                                System.out.println(nameUser.toString());
                                Intent intent = new Intent (v.getContext(), RankingActivity.class);
                                intentos ++;
                                String message = String.valueOf(nameUser.toString()+" "+intentos+" "+min+" "+seg+" "+mili);
                                intent.putExtra(EXTRA_MESSAGE, message);
                                startActivityForResult(intent, 0);

                            }
                        });

                        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // Canceled.
                            }
                        });

                        alert.show();

                        //Reseteo de variables
                        numero = (int)(Math.random()*100+1);
                        aviso.setText("");
                        textIntent.setText("0");





                    }else{
                        intentos++;
                        textIntent.setText(String.valueOf(intentos));
                        aviso.setTextColor(Color.RED);
                        if (numero2<numero){
                            aviso.setText("El numero secret es mes gran que "+ numero2);
                        }else{
                            aviso.setText("El numero secret es mes petit que "+ numero2);
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

