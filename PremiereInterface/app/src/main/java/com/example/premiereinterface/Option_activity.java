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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

public class Option_activity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {

    TableLayout tableau = null;
    TableRow ligne = null;

    Button bouton_valider = null;
    Button bouton_effacer = null;

    String fichier = "liste_a_contacter.txt";

    Spinner batiment = null;

    String batimentText = null;



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

        batiment = findViewById(R.id.Spinner_batiment);
        ArrayAdapter<CharSequence> adapter  = ArrayAdapter.createFromResource(this, R.array.materiel, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        batiment.setAdapter(adapter);
        batiment.setOnItemSelectedListener(this);


        bouton_valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                EditText numero = findViewById(R.id.editText_numero);
                EditText text_nom_batiment = findViewById(R.id.editText_Nom_Batiment);

                if(text_nom_batiment.isEnabled()){

                    if( (batimentText.length() != 0) && (numero.getText().toString().length() == 10) && (text_nom_batiment.getText().toString().length() != 0)  ){

                        NouvelleLigne();

                    }

                }

                if( (text_nom_batiment.isEnabled() == false)  && (batimentText.length() != 0) && (numero.getText().toString().length() == 10) ){
                    NouvelleLigne();
                }

                else{
                    Toast toast = Toast.makeText(getApplicationContext(), "Formulaire Incomplet!", Toast.LENGTH_SHORT);
                    toast.show();
                }


            }
        });

        bouton_effacer = findViewById(R.id.button_affacer);
        bouton_effacer.setText("effacer les numéros");



    }

    public void onClickSupprimer(View v){

        switch (bouton_effacer.getText().toString()){

            case "effacer les numéros":
                bouton_effacer.setText("êtes vous sûr ?");
                break;

            case "êtes vous sûr ?":
                bouton_effacer.setText("vraiment ?");
                break;

            case "vraiment ?":
                bouton_effacer.setText("encore une fois");
                break;

            case "encore une fois":
                bouton_effacer.setText("effacer les numéros");
                EffacerFichier();
                Toast.makeText(getApplicationContext(),"tous les numéros ont été supprimés",Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());
                break;

        }

    }

    public void sauvegarder(String batiment_string, String numero_string, String nom_batiment) throws IOException {


        EditText text_nom_batiment = findViewById(R.id.editText_Nom_Batiment);
        String data;


        if(text_nom_batiment.isEnabled()){
            data = new String(batiment_string + "-" + numero_string + "-" + nom_batiment + "\n") ;
        }
        else
        {
            data = new String(batiment_string + "-" + numero_string + "-Aucun"  + "\n") ;
        }





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

        //EditText batiment = findViewById(R.id.editText_batiment);
        EditText numero = findViewById(R.id.editText_numero);
        EditText nomBatiment = findViewById(R.id.editText_Nom_Batiment);

        tableau = findViewById(R.id.Tableau);

        ligne = new TableRow(getApplicationContext());
        ligne.setLayoutParams(findViewById(R.id.ligne).getLayoutParams());


        TextView texte1 = new TextView(getApplicationContext());
        texte1.setText(batimentText);
        texte1.setGravity(Gravity.CENTER);
        texte1.setLayoutParams(findViewById(R.id.texte_batiment).getLayoutParams());


        TextView texte2 = new TextView(getApplicationContext());
        texte2.setText(numero.getText());
        texte2.setGravity(Gravity.CENTER);
        texte2.setLayoutParams(findViewById(R.id.texte_batiment).getLayoutParams());



        TextView texte3 = new TextView(getApplicationContext());
        texte3.setText(nomBatiment.getText());
        texte3.setGravity(Gravity.CENTER);
        texte3.setLayoutParams(findViewById(R.id.texte_batiment).getLayoutParams());


        Button supprimer_num = new Button(getApplicationContext());
        supprimer_num.setText("X");
        supprimer_num.setGravity(Gravity.CENTER);
        supprimer_num.setTag(numero.getText());
        supprimer_num.setLayoutParams(findViewById(R.id.texte_batiment).getLayoutParams());
        supprimer_num.setOnClickListener(new View.OnClickListener() { //evennement pour le clic
            @Override
            public void onClick(View v) {

                CFichier file = new CFichier(getApplicationContext(),fichier);

                file.deleteNumero(v.getTag().toString());


                finish(); //recharge la page
                startActivity(getIntent());

            }
        });





        ligne.addView(texte1);
        ligne.addView(texte3);
        ligne.addView(texte2);
        ligne.addView(supprimer_num);

        tableau.addView(ligne);


        try {
            sauvegarder(batimentText, numero.getText().toString(), nomBatiment.getText().toString());
            numero.setText("");
            nomBatiment.setText("");
            Toast.makeText(getApplicationContext(), "Ajouté!", Toast.LENGTH_SHORT).show();


        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Erreur enregistrement!", Toast.LENGTH_SHORT).show();

        }



    }

    public void NouvelleLigneApresLecture(String data){

        String data_formatee[] = data.split("-");

        //EditText batiment = findViewById(R.id.editText_batiment);
        EditText numero = findViewById(R.id.editText_numero);
        EditText nom = findViewById(R.id.editText_Nom_Batiment);

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


        TextView texte3 = new TextView(getApplicationContext());
        texte3.setText( data_formatee[2]  );
        texte3.setGravity(Gravity.CENTER);
        texte3.setLayoutParams(findViewById(R.id.texte_batiment).getLayoutParams());


        Button supprimer_num = new Button(getApplicationContext());
        supprimer_num.setText("X");
        supprimer_num.setGravity(Gravity.CENTER);
        supprimer_num.setTag(data_formatee[1]);
        supprimer_num.setLayoutParams(findViewById(R.id.texte_batiment).getLayoutParams());
        supprimer_num.setOnClickListener(new View.OnClickListener() { //evennement pour le clic
            @Override
            public void onClick(View v) {

                CFichier file = new CFichier(getApplicationContext(),fichier);
                file.deleteNumero(v.getTag().toString());


                finish(); //recharge la page
                startActivity(getIntent());

            }
        });



        ligne.addView(texte1);
        ligne.addView(texte3);
        ligne.addView(texte2);
        ligne.addView(supprimer_num);
        tableau.addView(ligne);




    }

    public void EffacerFichier(){

        deleteFile(fichier);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        batimentText = parent.getItemAtPosition(position).toString();
        EditText text_nom_batiment = findViewById(R.id.editText_Nom_Batiment);

        if(batimentText.equals("Ecole")){

            text_nom_batiment.setEnabled(true);
        }

        else{
            text_nom_batiment.setEnabled(false);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        batimentText = "";
    }
}
