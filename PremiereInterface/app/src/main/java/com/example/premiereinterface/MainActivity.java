package com.example.premiereinterface;
import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.usage.UsageEvents;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaCas;
import android.os.Build;
import android.telephony.SmsManager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.EventLog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Calendar;

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

    MySmsReceiver receptionSMS = null;



    private static final int PERMISSION_SEND_SMS = 123;
    private static final String TAG = MySmsReceiver.class.getSimpleName();
    public static final String pdu_type = "pdus";

    private BroadcastReceiver listener = null;





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


        envoieDeSms = new CSMS();
        receptionSMS = new MySmsReceiver();


        view = this.getWindow().getDecorView(); //pour changer le background


        try {


            CreationBouttonEtEvennement();
            creationTab();



        } catch (IOException e) {
            e.printStackTrace();
        }


        listener = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                SmsMessage[] msgs = null;
                String str = "";
                if (bundle != null) {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        str += msgs[i].getMessageBody();

                    }
                    Toast.makeText(context, str, Toast.LENGTH_LONG).show();

                    CFichier fichier = new CFichier(context, "Archive.txt");
                    try {
                        fichier.sauvegarder("message recu");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };





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
                        receptionSMS.onReceive(getApplicationContext(), getIntent());

                        break;

                    case R.id.button_incendie:
                        // view.setBackgroundColor(Color.BLUE);
                        LireNumeroAContacter("Alerte incendie");
                        receptionSMS.onReceive(getApplicationContext(), getIntent());

                        break;

                    case R.id.button_confinement:
                        //view.setBackgroundColor(Color.GREEN);
                        LireNumeroAContacter("Alerte confinement");
                        receptionSMS.onReceive(getApplicationContext(), getIntent());

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

    public void CreationBouttonEtEvennement(){

        //RECUPERE LES BOUTTONS//
        bouton_intrusion =  findViewById(R.id.button_intrusion);
        bouton_incendie = findViewById(R.id.button_incendie);
        bouton_confinement = findViewById(R.id.button_confinement);
        bouton_option = findViewById(R.id.button_configuration);



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

    }


    public void LireNumeroAContacter(String Message){
        try {

            CFichier file = new CFichier(getApplicationContext(), "Archive.txt");

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

                file.sauvegarderDeclenchement(Message, data_formatee[0], data_formatee[1]);

            }


            in.close();

        } catch (Exception e) {
            Toast.makeText(this,"Pas de numéro enregistré !",Toast.LENGTH_SHORT).show();
        }


    }


    public void lecture_archive(View v)  {

        CFichier classFichier = new CFichier(getApplicationContext(),"Archive.txt");

        TextView text_archive =  findViewById(R.id.textView_Archive);

        try {
            text_archive.setText(classFichier.LireFichier());
        } catch (IOException e) {
            Toast.makeText(this,"Archive vide !",Toast.LENGTH_SHORT).show();
        }


    }


    public void choix_heure_et_date(View v){


        if(v.getId() == R.id.button_choix_date){

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {



                            Button choixDate = findViewById(R.id.button_choix_date);
                            choixDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            Button choixHeure = findViewById(R.id.button_choix_heure);
                            choixHeure.setEnabled(true);



                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();

        }

        if(v.getId() == R.id.button_choix_heure){

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            int mHour = c.get(Calendar.HOUR_OF_DAY);
            int mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            Button choixHeure = findViewById(R.id.button_choix_heure);
                            choixHeure.setText(hourOfDay + ":" + minute);


                            Button valider = findViewById(R.id.button_valider_test);
                            valider.setEnabled(true);

                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }


    }


    public void test_valider(View v){

        Button choixHeure = findViewById(R.id.button_choix_heure);
        Button choixDate = findViewById(R.id.button_choix_date);


        CFichier fichier = new CFichier(getApplicationContext(), "Archive.txt");



        fichier.EnregistrerTest(choixDate.getText().toString(), choixHeure.getText().toString());


        Toast.makeText(getApplicationContext(), "Test sauvegardé !", Toast.LENGTH_LONG).show();


        choixHeure.setEnabled(false);


        choixDate.setText("Choisir Date");
        choixHeure.setText("Choisir Heure");



        Button valider = findViewById(R.id.button_valider_test);
        valider.setEnabled(false);





    }


    public void creationTab() throws IOException {
        th = findViewById(R.id.TabHost);


        //AJOUTE LA TAB//
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


        specs = th.newTabSpec("Tag4");
        specs.setContent(R.id.Etat);
        specs.setIndicator("Etat");
        th.addTab(specs);

        //FIN DE LA TAB//


        ////cree une ligne dans la Tab Etat pour chaque batiment///

        CFichier classfichier = new CFichier(getApplicationContext(),"liste_a_contacter.txt");

        String data = classfichier.LireFichier(); //recupere toutes les lignes des batiment plus leur numero
        String[] ListBatimentavecNumero = data.split("\n"); //sépare chaque ligne
        String[][]ListBatimentNumerosplit = new String[ListBatimentavecNumero.length][2]; //List des numeros et des batiments pour chaque ligne séparés
        String[] BatimentEtNumero = new String[2]; //String

        for(int i = 0; i < ListBatimentavecNumero.length; i++){

            BatimentEtNumero = ListBatimentavecNumero[i].split(" ");

            ListBatimentNumerosplit[i][0] = BatimentEtNumero[0];
            ListBatimentNumerosplit[i][1] = BatimentEtNumero[1];

        }


        for(int i = 0; i < ListBatimentavecNumero.length; i++ ){

            AjoutBatimentDansEtat(ListBatimentNumerosplit[i][0],  ListBatimentNumerosplit[i][1]); //cree une ligne dans la Tab Etat pour chaque batiment

        }



    }

    public void AjoutBatimentDansEtat(String batiment, final String numero){


        //MODIFIE la tab Etat//
        TextView TextBatiment = new TextView(this);
        TextView TextNumero = new TextView(this);
        TextView TextEtat = new TextView(this);
        Button boutonPing = new Button(this);
        ImageView logoEcole = new ImageView(this);
        LinearLayout lineaLayoutHorizontal = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100, 100, 1);

        LinearLayout layout = (LinearLayout)findViewById(R.id.Etat);


        lineaLayoutHorizontal.setOrientation(LinearLayout.HORIZONTAL);


        if(batiment.equals("Ecole")){
            logoEcole.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.ecole));
        }

        if(batiment.equals("Raspberry")){
            logoEcole.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.raspberry));
        }

        if(batiment.equals("Mairie")){
            logoEcole.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.mairie));
        }

        Toast.makeText(this, "batiment:" + batiment +":", Toast.LENGTH_SHORT).show();


        logoEcole.setLayoutParams(params);
        lineaLayoutHorizontal.addView(logoEcole); //ajoute un bouton dans le linear layout


        TextBatiment.setText("Batiment : " + batiment ); //ajoute du text dans le linear layout
        TextBatiment.setTextSize(20);
        TextBatiment.setLayoutParams(params);
        lineaLayoutHorizontal.addView(TextBatiment);


        TextNumero.setText("Numero : " + numero ); //ajoute du text dans le linear layout
        TextNumero.setTextSize(20);
        TextNumero.setLayoutParams(params);
        lineaLayoutHorizontal.addView(TextNumero);


        TextEtat.setText("Etat : Ok");
        TextEtat.setTextSize(20);
        TextEtat.setLayoutParams(params);
        lineaLayoutHorizontal.addView(TextEtat);




        boutonPing.setText("Vérifier état");
        boutonPing.setLayoutParams(params);
        boutonPing.setTag(numero); //pour savoir à quel numéro tester l'état
        boutonPing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String numero = v.getTag().toString();

                Toast.makeText(MainActivity.this, "Ping au numero : " + numero, Toast.LENGTH_SHORT).show();

                envoieDeSms.EnvoieSMS(numero, "Ping");

                CFichier file = new CFichier(getApplicationContext(), "Archive.txt");
                try {

                    file.sauvegarderPing(numero);

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });

        lineaLayoutHorizontal.addView(boutonPing); //ajoute un bouton dans le linear layout


        layout.addView(lineaLayoutHorizontal); // ajoute un linear layout dans la tab



    }



}


