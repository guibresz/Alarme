package com.example.premiereinterface;

import android.content.Context;
import android.media.audiofx.EnvironmentalReverb;
import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class CFichier {


    public CFichier(){

    }



    public void sauvegarder(Context context,String fichier,String data) throws IOException {

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fichier, Context.MODE_APPEND));
        outputStreamWriter.write("\n");
        outputStreamWriter.write(data);
        outputStreamWriter.close();



    }

    public String LireFichier(Context context,String fichier) throws IOException {

            FileInputStream in = null;

            in = context.openFileInput(fichier);

            BufferedReader br= new BufferedReader(new InputStreamReader(in));

            StringBuilder sb= new StringBuilder();
            String s= null;
            while((s= br.readLine())!= null)
                sb.append(s).append("\n");


            return sb.toString();

    }

    public void EffacerFichier(Context context,String fichier) throws IOException {


        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fichier, Context.MODE_PRIVATE));
        outputStreamWriter.write("");
        outputStreamWriter.close();

    }


}
