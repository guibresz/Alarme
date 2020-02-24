package com.example.premiereinterface;

import android.telephony.SmsManager;

public class CSMS {

    public String Message = null;
    public String Numero = null;


    CSMS(){

    }


    public void EnvoieSMS(String Numero, String Message){

        SmsManager smsmanager = SmsManager.getDefault();
        smsmanager.sendTextMessage(Numero,null, Message, null, null);

    }



}
