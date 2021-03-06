package com.example.endivinanumero;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Date;


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
    private Intent intent;
    private Button btnCamera;
    private ImageView iv;
    private String currentPhotoPath;
    private File image = null;
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

                        intent = new Intent (v.getContext(), RankingActivity.class);
                        createDialog(intent);

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
    private void createDialog(Intent intent){
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

        alert.setTitle("Has Guanyat!");
        alert.setMessage("-Numero secret: "+numero+
                "\n-Numero de intents: "+String.valueOf(intentos+1)+
                "\n-Temps: "+min+":"+seg+":"+mili+
                "\nIntrodueix el teu nom per afegirlo al ranking:");

        // Set an EditText view to get user input
        final EditText input = new EditText(MainActivity.this);
        alert.setView(input);
        alert.setCancelable(false);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                nameUser = input.getText();
                if(!nameUser.toString().isEmpty()){
                    //System.out.println(nameUser.toString());

                    intentos ++;
                    try {
                        abrirCamara();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"No pots posar el nom en blanc",Toast.LENGTH_SHORT).show();
                    createDialog(intent);
                }

            }
        });

        alert.show();
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        }
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private void abrirCamara() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent

        // Create the File where the photo should go

        File photoFile = createImageFile();
        // Continue only if the File was successfully created

        Uri photoURI = FileProvider.getUriForFile(this,
                "com.victormerch.android.fileprovider",
                photoFile);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        startActivityForResult(takePictureIntent, 1);
        System.out.println("Si entra en la camara");


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri fileUri = null;
        System.out.println("---Image:"+image.toString());
//        fileUri = Uri.fromFile(image);

        //Intent intent = new Intent (v.getContext(), RankingActivity.class);
        intentos ++;
        String message = String.valueOf(nameUser.toString()+" "+intentos+" "+min+" "+seg+" "+mili+" "+image.toString());
        intent.putExtra(EXTRA_MESSAGE, message);

        startActivity(intent);
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            imageView.setImageBitmap(imageBitmap);
//        }
    }



}



