package com.example.hyongdonjeong_juheekim_comp304lab6_ex1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class SMSReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            //---get the SMS message passed in---
            Bundle bundle = intent.getExtras(); SmsMessage[] msgs = null;
            String str = "SMS from "; if (bundle != null)
        {
            //---retrieve the SMS message received---
            Object[] pdus = (Object[]) bundle.get("pdus"); msgs = new SmsMessage[pdus.length];
            for (int i=0; i<msgs.length; i++){
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                if (i==0) {
                    //---get the sender address/phone number---
                    str += msgs[i].getOriginatingAddress(); str += ": ";
                }

                //---get the message body---
                str += msgs[i].getMessageBody().toString();
            }

            //---display the new SMS message---
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();

            Log.d("SMSReceiver", str);

            //---stop the SMS message from being broadcasted---
            this.abortBroadcast();

        }

            //---send a broadcast intent to update the SMS received in the activity
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("SMS_RECEIVED_ACTION");
            broadcastIntent.putExtra("sms", str);
            context.sendBroadcast(broadcastIntent);
        }



    }


