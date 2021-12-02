package com.example.endivinanumero;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class RankingActivity extends AppCompatActivity {
    //Hacer la tabla con un TableLayout(dos tablas intento y tiempo), con un scrolldown
    static private ArrayList<String> users = new ArrayList<String>();
    static private ArrayList<Integer> intents = new ArrayList<Integer>();
    static private ArrayList<Integer> min = new ArrayList<Integer>();
    static private ArrayList<Integer> seg = new ArrayList<Integer>();
    static private ArrayList<Integer> mili = new ArrayList<Integer>();
    //static private ArrayList<Integer> mili = new ArrayList<Integer>();
    private ImageView iv;
    private File image = null;
    class Record {
        public int intents;
        public String nom;

        public Record(int _intents, String _nom ) {
            intents = _intents;
            nom = _nom;
        }
    }
    // Model = Taula de records: utilitzem ArrayList
    ArrayList<Record> records;

    // ArrayAdapter serà l'intermediari amb la ListView
    ArrayAdapter<Record> adapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        final TextView sms = (TextView) findViewById(R.id.message);
        String[] newUser = new String[5];
        newUser = getIntent().getStringExtra(MainActivity.EXTRA_MESSAGE).split( " ");
        users.add(newUser[0]);
        intents.add(Integer.parseInt(newUser[1]));
        min.add(Integer.parseInt(newUser[2]));
        seg.add(Integer.parseInt(newUser[3]));
        mili.add(Integer.parseInt(newUser[4]));

        //Ordenacion por intentos
        int i, j, aux;
        for (i = 0; i < intents.size() - 1; i++) {
            for (j = 0; j < intents.size() - i - 1; j++) {

                if (intents.get(j + 1) < intents.get(j)) {

                    aux = intents.get(j + 1);
                    intents.set(j + 1,intents.get(j));
                    intents.set(j,intents.get(aux));

                    String aux2 = users.get(j + 1);
                    users.set(j + 1,users.get(j));
                    users.set(j,users.get(aux));

                    aux = min.get(j + 1);
                    min.set(j + 1,min.get(j));
                    min.set(j,min.get(aux));

                    aux = seg.get(j + 1);
                    seg.set(j + 1,seg.get(j));
                    seg.set(j,seg.get(aux));

                    aux = mili.get(j + 1);
                    mili.set(j + 1,mili.get(j));
                    mili.set(j,mili.get(aux));

                }
            }
        }
        // Inicialitzem model
        records = new ArrayList<Record>();
        // Afegim alguns exemples
        for(int x = 0; x< users.size();x++) {
            records.add(new Record(intents.get(x), users.get(x)));

        }

        // Inicialitzem l'ArrayAdapter amb el layout pertinent
        adapter = new ArrayAdapter<Record>( this, R.layout.list_item, records )
        {
            @Override
            public View getView(int pos, View convertView, ViewGroup container)
            {
                // getView ens construeix el layout i hi "pinta" els valors de l'element en la posició pos
                if( convertView==null ) {
                    // inicialitzem l'element la View amb el seu layout
                    convertView = getLayoutInflater().inflate(R.layout.list_item, container, false);
                }
                // "Pintem" valors (també quan es refresca)
                ((TextView) convertView.findViewById(R.id.nom)).setText(getItem(pos).nom);
                ((TextView) convertView.findViewById(R.id.intents)).setText(Integer.toString(getItem(pos).intents));
                return convertView;
            }

        };

        // busquem la ListView i li endollem el ArrayAdapter
        ListView lv = (ListView) findViewById(R.id.recordsView);
        lv.setAdapter(adapter);

    }
    protected File getFile(){
        File path = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File photo = new File(path,"imatge.jpg");
        return photo;
    }
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {

            // Get the dimensions of the View
            int targetW = iv.getWidth();
            int targetH = iv.getHeight();

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            File path = this.getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

            if (image == null){
                image = getFile();
            }
            BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);


            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.max(1, Math.min(photoW/targetW, photoH/targetH));

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            Bitmap bitmap = null;


            bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);


            iv.setImageBitmap(bitmap);

        }

    }
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Uri fileUri = null;
//
//        fileUri = Uri.fromFile(image);
//
//        iv.setImageURI(fileUri);
//
//
//    }

}