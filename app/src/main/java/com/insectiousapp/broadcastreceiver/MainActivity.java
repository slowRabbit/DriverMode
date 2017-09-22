package com.insectiousapp.broadcastreceiver;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    IncomingCallReceiver incomingCallReceiver;
    CheckBox cbSend;
    String MyPREFERENCES="spmain";
    String CHOICE="choice";
    SharedPreferences sharedpreferences;
    ToggleButton btnDriverMode;
    boolean permissionGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissionGranted=isPermissionGranted();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        incomingCallReceiver=new IncomingCallReceiver();
        btnDriverMode=(ToggleButton)findViewById(R.id.btnDriverMode);
        cbSend=(CheckBox) findViewById(R.id.cbSendMessage);
        btnDriverMode.setOnCheckedChangeListener(this);

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
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        if(isPermissionGranted()) {
            //check if all permissions are granted
            //if all permissions are granted then only allow user to proceed
            if (compoundButton.getId() == R.id.btnDriverMode)

            {
                if (b == true) {
                    //driver mode on
                    registerReceiver(incomingCallReceiver, new IntentFilter("android.intent.action.PHONE_STATE"));
                    btnDriverMode.setTextOn("Driver Mode On");
                    Toast.makeText(this, "Driver mode On !", Toast.LENGTH_SHORT).show();

                } else {
                    //driver mode off

                    unregisterReceiver(incomingCallReceiver);
                    btnDriverMode.setTextOff("Driver Mode Off !");
                    Toast.makeText(this, "Driver mode Off !!", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getTitle().equals("item1"))
        {
            Toast.makeText(this, "Item 1", Toast.LENGTH_SHORT).show();
        }
        else if(item.getTitle().equals("item2"))
        {
            Toast.makeText(this, "Item 2", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }


    public  boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {

            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                    == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_GRANTED&& checkSelfPermission(Manifest.permission.MODIFY_AUDIO_SETTINGS)
                    == PackageManager.PERMISSION_GRANTED&& checkSelfPermission(Manifest.permission.VIBRATE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted");
                return true;
            } else {

                Log.v("TAG","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.SEND_SMS,
                        Manifest.permission.MODIFY_AUDIO_SETTINGS, Manifest.permission.VIBRATE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1: {

                if (grantResults.length > 3
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED&& grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED&& grantResults[3] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                 //   call_action();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


}
