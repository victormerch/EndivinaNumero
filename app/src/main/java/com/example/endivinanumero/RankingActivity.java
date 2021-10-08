package com.example.endivinanumero;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class RankingActivity extends AppCompatActivity {
    //Hacer la tabla con un TableLayout(dos tablas intento y tiempo), con un scrolldown
    static private ArrayList<String> users = new ArrayList<String>();
    static private ArrayList<Integer> intents = new ArrayList<Integer>();
    static private ArrayList<Integer> min = new ArrayList<Integer>();
    static private ArrayList<Integer> seg = new ArrayList<Integer>();
    static private ArrayList<Integer> mili = new ArrayList<Integer>();
    @Override
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
        String output = "";
        for (int k = 0;k<intents.size();k++){
            output +=users.get(k)+" "+intents.get(k)+" "+min.get(k)+" "+seg.get(k)+" "+mili.get(k)+"\n";
        }
        sms.setText(output);
    }

}