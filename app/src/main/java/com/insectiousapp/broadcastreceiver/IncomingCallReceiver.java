package com.insectiousapp.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by cyris on 21/9/17.
 */

public class IncomingCallReceiver extends BroadcastReceiver {


    String TAG="incomingreceiver";
    String MyPREFERENCES="spmain";
    String CHOICE="choice";
    SharedPreferences sharedpreferences;
    Boolean sendMessageBool;
    static int lastRingerMode;


    @Override
    public void onReceive(Context context, Intent intent) {

        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sendMessageBool=sharedpreferences.getBoolean(CHOICE, false);
        AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);

        String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
        String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
        int state = 0;

        if(stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)){

            //store last state
            lastRingerMode=audioManager.getRingerMode();
            //set phone to silent state upon ringing
            audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);

            Log.i(TAG, "phone is ringing from : "+number+" Bool :"+sendMessageBool);
            Toast.makeText(context, "Phone is ringing from :"+number, Toast.LENGTH_SHORT).show();



            if(sendMessageBool) {
                String strPhone = number;
                String strMessage = "Will Call you later, driving right now !";
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(strPhone, null, strMessage, null, null);
                Toast.makeText(context, "Sent Message to : " + strPhone, Toast.LENGTH_SHORT).show();
            }

        }
        else if(stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)){
            //set phone to call state upon getting back idle again
            audioManager.setRingerMode(lastRingerMode);
            Log.i(TAG, "Phone is in idle state, ringer mode was :"+lastRingerMode);
        }

    }

}
