package com.insectiousapp.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;


public class GenericReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("rec", "onRecieve");
        Toast.makeText(context, "Outgoing Call", Toast.LENGTH_SHORT).show();

    }
}
