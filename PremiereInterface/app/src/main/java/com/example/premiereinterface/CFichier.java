package com.example.premiereinterface;

import android.content.Context;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class CFichier {


    private Context context = null;
    private String fichier = null;

    public CFichier(Context pcontext,String pfichier){

        context = pcontext;
        fichier = pfichier;

    }



    public void sauvegarder(String data) throws IOException {

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fichier, Context.MODE_APPEND));
        outputStreamWriter.write("\n");
        outputStreamWriter.write(data);
        outputStreamWriter.close();



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


}
