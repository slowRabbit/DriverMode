package com.insectiousapp.broadcastreceiver;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnDMOn, btnDMOff;
    IncomingCallReceiver incomingCallReceiver;
    CheckBox cbSend;
    String MyPREFERENCES="spmain";
    String CHOICE="choice";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        incomingCallReceiver=new IncomingCallReceiver();
        btnDMOn=(Button)findViewById(R.id.btRegister);
        btnDMOff=(Button)findViewById(R.id.btUnregister);
        cbSend=(CheckBox) findViewById(R.id.cbSendMessage);

        btnDMOn.setOnClickListener(this);
        btnDMOff.setOnClickListener(this);

        cbSend.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                SharedPreferences.Editor editor = sharedpreferences.edit();

                if ( isChecked )
                {
                    // perform logic
                    editor.putBoolean(CHOICE, true);
                    Toast.makeText(getApplicationContext(), "Respond Mode Enabled", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    editor.putBoolean(CHOICE, false);
                    Toast.makeText(getApplicationContext(), "Respond Mode Disabled !", Toast.LENGTH_SHORT).show();
                }


                editor.commit();

            }
        });



    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btRegister:


                registerReceiver(incomingCallReceiver, new IntentFilter("android.intent.action.PHONE_STATE"));
                Toast.makeText(this, "Driver mode On !", Toast.LENGTH_SHORT).show();

//                TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//                tm.listen(callStateListener, PhoneStateListener.LISTEN_CALL_STATE);

                break;

            case R.id.btUnregister:

                unregisterReceiver(incomingCallReceiver);
                Toast.makeText(this, "Driver mode Off !!", Toast.LENGTH_SHORT).show();

//                String strPhone = "9574110030";
//                String strMessage = "Assignment done baby !";
//
//                SmsManager sms = SmsManager.getDefault();
//
//                sms.sendTextMessage(strPhone, null, strMessage, null, null);
//
//                Toast.makeText(this, "Sent Message to : "+strPhone, Toast.LENGTH_SHORT).show();

                break;
        }
    }
}
