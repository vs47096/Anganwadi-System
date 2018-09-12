package com.example.vinamra.anganwadi_helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Vinamra on 1/4/2018.
 */

public class OtpSmsBroacastReceiver extends BroadcastReceiver {
    private String TAG="Broadcast Receiver";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        // Get Bundle object contained in the SMS intent passed in
        Bundle bundle = intent.getExtras();
        SmsMessage[] smsm = null;
        String sms_str ="";

        if (bundle != null)
        {
            // Get the SMS message
            Object[] pdus = (Object[]) bundle.get("pdus");
            smsm = new SmsMessage[pdus.length];
            for (int i=0; i<smsm.length; i++){
                smsm[i] = SmsMessage.createFromPdu((byte[])pdus[i]);

                sms_str += smsm[i].getMessageBody().toString();
                sms_str=sms_str.substring(0,6);
                String Sender = smsm[i].getOriginatingAddress();
                //Check here sender is yours
                if(Sender.endsWith("TFCTOR"))
                {
                    Intent smsIntent = new Intent("otp");
                    smsIntent.putExtra("message",sms_str);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(smsIntent);
                }

            }
        }
    }

}
