package com.example.premiereinterface;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class CFichier {


    private Context context = null;
    private String fichier = null;

    public CFichier(Context pcontext,String pfichier){

        context = pcontext;
        fichier = pfichier;

    }



    public void sauvegarder(String data) throws IOException {

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fichier, Context.MODE_APPEND));
        outputStreamWriter.write(data);
        outputStreamWriter.write("\n");
        outputStreamWriter.close();


    }

    public void sauvegarderDeclenchement(String Message, String NomDest, String NumeroDest) throws IOException {

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);

        Calendar calendar = Calendar.getInstance();
        int hour24hrs = calendar.get(Calendar.HOUR_OF_DAY);
        int hour12hrs = calendar.get(Calendar.HOUR);
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);

        String Clock = hour24hrs + ":" +  minutes + ":" + seconds;

        String data = Message + " envoyé à " + NomDest + " au numéro " + NumeroDest + " le " + formattedDate + " à " + Clock;

        sauvegarder(data);


    }


    public void sauvegarderPing(String numero) throws IOException {

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);

        Calendar calendar = Calendar.getInstance();
        int hour24hrs = calendar.get(Calendar.HOUR_OF_DAY);
        int hour12hrs = calendar.get(Calendar.HOUR);
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);

        String Clock = hour24hrs + ":" +  minutes + ":" + seconds;

        String data = "Ping envoyé à " + numero + " le " + formattedDate + " à " + Clock;

        sauvegarder(data);



    }

    public void sauvegarderRecepetionSMS(String numero, String message){


        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);

        Calendar calendar = Calendar.getInstance();
        int hour24hrs = calendar.get(Calendar.HOUR_OF_DAY);
        int hour12hrs = calendar.get(Calendar.HOUR);
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);

        String Clock = hour24hrs + ":" +  minutes + ":" + seconds;

        String data = " Message : " + message + " recu au numéro " + numero + " le " + formattedDate + " à " + Clock;

        try {
            sauvegarder(data);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public String LireFichier() throws IOException {

            FileInputStream in = null;

            in = context.openFileInput(fichier);

            BufferedReader br= new BufferedReader(new InputStreamReader(in));

            StringBuilder sb= new StringBuilder();
            String s= null;
            while((s= br.readLine())!= null)
                sb.append(s).append("\n");


            return sb.toString();

    }

    public void EffacerFichier() throws IOException {


        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fichier, Context.MODE_PRIVATE));
        outputStreamWriter.write("");
        outputStreamWriter.close();

    }

    public void EnregistrerTest(String date, String heure){

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);

        Calendar calendar = Calendar.getInstance();
        int hour24hrs = calendar.get(Calendar.HOUR_OF_DAY);
        int hour12hrs = calendar.get(Calendar.HOUR);
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);

        String Clock = hour24hrs + ":" +  minutes + ":" + seconds;




        String data = "Nouveau test sauvegardé le " + formattedDate.toString() + " à " + Clock.toString() + " pour le " + date + " à " + heure;

        try {
            sauvegarder(data);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public void deleteNumero(String numero){

        String data = ""; //anciens numeros
        String newData = ""; //même numeros mais sans le numéro à supprimer

        try {

            data = LireFichier();
            EffacerFichier();

        } catch (IOException e) {
            e.printStackTrace();
        }



        String[] ListBatimentavecNumero = data.split("\n"); //sépare chaque ligne
        String[] BatimentEtNumeroetNom = new String[3]; //String


        //recupere les données de chaque ligne//
        for(int i = 0; i < ListBatimentavecNumero.length; i++){

            BatimentEtNumeroetNom = ListBatimentavecNumero[i].split("-");

            if(BatimentEtNumeroetNom[1].equals(numero)){ //si c'est le numero qu'on veut supprimer

                //on ne le recopie pas
            }

            else //sinon
            {
                newData = BatimentEtNumeroetNom[0] + "-" + BatimentEtNumeroetNom[1] + "-" + BatimentEtNumeroetNom[2]; //on le recopie
                try {

                    sauvegarder(newData);


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }



        }



    }


}
