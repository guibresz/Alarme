package com.example.premiereinterface;
import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.telephony.SmsManager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button bouton_intrusion =  null;
    Button bouton_incendie =  null;
    Button bouton_confinement =  null;
    Button bouton_option =  null;

    TabHost th = null;

    String fichier = "liste_a_contacter.txt";

    View view = null;
    View bouton_appuye = null;

    CSMS envoieDeSms;

    private static final int PERMISSION_SEND_SMS = 123;


    private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, intent.getExtras().getString("message"), Toast.LENGTH_SHORT).show();
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //forcer l'écran sur le côté
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        ///DEMANDE LES AUTORISATION//
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, PERMISSION_SEND_SMS); //autorisation d'envoie de sms
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, 1); //autorisation reception de sms
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, 1); //autorisation reception de sms


        //RECUPERE LES BOUTTONS//
         bouton_intrusion =  findViewById(R.id.button_intrusion);
         bouton_incendie = findViewById(R.id.button_incendie);
         bouton_confinement = findViewById(R.id.button_confinement);
         bouton_option = findViewById(R.id.button_configuration);

        envoieDeSms = new CSMS();


        th = findViewById(R.id.TabHost);


        //ajoute la tab//
        th.setup();

        TabHost.TabSpec specs = th.newTabSpec("Tag1");
        specs.setContent(R.id.declenchement);
        specs.setIndicator("Declenchement");
        th.addTab(specs);

        specs = th.newTabSpec("Tag2");
        specs.setContent(R.id.Test);
        specs.setIndicator("Test");
        th.addTab(specs);

        specs = th.newTabSpec("Tag3");
        specs.setContent(R.id.Archive);
        specs.setIndicator("Archive");
        th.addTab(specs);



        //AJOUTE DES EVENEMENT SUR LES BOUTONS//
        bouton_intrusion.setOnClickListener(this);
        bouton_incendie.setOnClickListener(this);
        bouton_confinement.setOnClickListener(this);


        bouton_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Option_activity.class);
                startActivity(intent);
            }
        });



        view = this.getWindow().getDecorView(); //pour changer le background



    }


    @Override
    public void onClick(View v) {

        bouton_appuye = v;

        AlertDialog.Builder message_confirmation = new AlertDialog.Builder(this);
        message_confirmation.setMessage("Etes-vous sûr de déclencher l'alarme ?");
        message_confirmation.setCancelable(false);
        message_confirmation.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(bouton_appuye.getId())
                {
                    case R.id.button_intrusion:
                      //  view.setBackgroundColor(Color.RED);
                        LireNumeroAContacter("Alerte intrusion");

                        break;

                    case R.id.button_incendie:
                       // view.setBackgroundColor(Color.BLUE);
                        LireNumeroAContacter("Alerte incendie");

                        break;

                    case R.id.button_confinement:
                        //view.setBackgroundColor(Color.GREEN);
                        LireNumeroAContacter("Alerte confinement");

                        break;



                }

                Toast.makeText(getApplicationContext(), "Tous les batiments ont été informés!", Toast.LENGTH_SHORT).show();
                

            }
        });

        message_confirmation.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        message_confirmation.create().show();



    }



    public void LireNumeroAContacter(String Message){
        try {

            String[] data_formatee = new String[2];

            // Open stream to read file.
            FileInputStream in = this.openFileInput(fichier);

            BufferedReader br= new BufferedReader(new InputStreamReader(in));

            StringBuilder sb= new StringBuilder();
            String s= null;
            while((s= br.readLine())!= null)  {
                sb.append(s).append("\n");

                data_formatee = s.split(" "); // sépare le batiment du numero

                envoieDeSms.EnvoieSMS( data_formatee[1] ,Message);


            }


            in.close();

        } catch (Exception e) {
            Toast.makeText(this,"Pas de numéro enregistré !",Toast.LENGTH_SHORT).show();
        }


    }




    public void lecture_archive(View v) throws IOException {

        CFichier classFichier = new CFichier(getApplicationContext(),"Archive.txt");

        TextView text_archive =  findViewById(R.id.textView_Archive);

        text_archive.setText(classFichier.LireFichier());


    }

}
