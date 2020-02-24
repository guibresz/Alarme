package com.example.premiereinterface;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Parameter;

public class Option_activity extends AppCompatActivity {

    TableLayout tableau = null;
    TableRow ligne = null;

    Button bouton_valider = null;
    Button bouton_effacer = null;

    String fichier = "liste_a_contacter.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //forcer l'écran sur le côté
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_option_activity);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);


        LireFichier();

        bouton_valider = findViewById(R.id.button_ajout);
        bouton_valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText batiment = findViewById(R.id.editText_batiment);
                EditText numero = findViewById(R.id.editText_numero);


                if( (batiment.getText().length() != 0) && (numero.getText().length() != 0) ){
                    NouvelleLigne();
                }

                else{
                    Toast toast = Toast.makeText(getApplicationContext(), "Formulaire Incomplet!", Toast.LENGTH_SHORT);
                    toast.show();
                }


            }
        });

        bouton_effacer = findViewById(R.id.button_affacer);
        bouton_effacer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EffacerFichier();
            }
        });


    }

    public void sauvegarder(String batiment_string, String numero_string) throws IOException {

        String data = new String(batiment_string + " " + numero_string + "\n") ;


        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getApplicationContext().openFileOutput(fichier, Context.MODE_APPEND));
        outputStreamWriter.write(data);
        outputStreamWriter.close();

    }

    public void LireFichier(){
        try {

            // Open stream to read file.
            FileInputStream in = this.openFileInput(fichier);

            BufferedReader br= new BufferedReader(new InputStreamReader(in));

            StringBuilder sb= new StringBuilder();
            String s= null;
            while((s= br.readLine())!= null)  {
                sb.append(s).append("\n");
                NouvelleLigneApresLecture(s.toString());

            }


            in.close();

        } catch (Exception e) {
            Toast.makeText(this,"Pas de numéro enregistré !",Toast.LENGTH_SHORT).show();
        }


    }



    public void NouvelleLigne(){
        Toast toast;

        EditText batiment = findViewById(R.id.editText_batiment);
        EditText numero = findViewById(R.id.editText_numero);

        tableau = findViewById(R.id.Tableau);

        ligne = new TableRow(getApplicationContext());
        ligne.setLayoutParams(findViewById(R.id.ligne).getLayoutParams());


        TextView texte1 = new TextView(getApplicationContext());
        texte1.setText(batiment.getText());
        texte1.setGravity(Gravity.CENTER);
        texte1.setLayoutParams(findViewById(R.id.texte_batiment).getLayoutParams());


        TextView texte2 = new TextView(getApplicationContext());
        texte2.setText(numero.getText());
        texte2.setGravity(Gravity.CENTER);
        texte2.setLayoutParams(findViewById(R.id.texte_batiment).getLayoutParams());


        ligne.addView(texte1);
        ligne.addView(texte2);
        tableau.addView(ligne);


        try {
            sauvegarder(batiment.getText().toString(), numero.getText().toString());
            numero.setText("");
            batiment.setText("");
            Toast.makeText(getApplicationContext(), "Ajouté!", Toast.LENGTH_SHORT).show();


        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Erreur enregistrement!", Toast.LENGTH_SHORT).show();

        }



    }

    public void NouvelleLigneApresLecture(String data){

        String data_formatee[] = data.split(" ");

        EditText batiment = findViewById(R.id.editText_batiment);
        EditText numero = findViewById(R.id.editText_numero);

        tableau = findViewById(R.id.Tableau);

        ligne = new TableRow(getApplicationContext());
        ligne.setLayoutParams(findViewById(R.id.ligne).getLayoutParams());


        TextView texte1 = new TextView(getApplicationContext());
        texte1.setText( data_formatee[0] );
        texte1.setGravity(Gravity.CENTER);
        texte1.setLayoutParams(findViewById(R.id.texte_batiment).getLayoutParams());


        TextView texte2 = new TextView(getApplicationContext());
        texte2.setText( data_formatee[1]  );
        texte2.setGravity(Gravity.CENTER);
        texte2.setLayoutParams(findViewById(R.id.texte_batiment).getLayoutParams());


        ligne.addView(texte1);
        ligne.addView(texte2);
        tableau.addView(ligne);




    }

    public void EffacerFichier(){

        deleteFile(fichier);
    }


}
