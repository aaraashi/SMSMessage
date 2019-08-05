package com.example.hyongdonjeong_juheekim_comp304lab6_ex1;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MessageActivity extends Activity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int REQUEST_READ_PHONE_STATE = 2;

    private EditText eText;
    private TextView SMSes;
    private TextView textMessage;
    //
    String SENT = "SMS_SENT";
    String DELIVERED = "SMS_DELIVERED";
    //
    PendingIntent sentPI, deliveredPI;
    BroadcastReceiver smsSentReceiver, smsDeliveredReceiver;
    IntentFilter intentFilter;
    //
    // receive intents sent by sendBroadcast()
    private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            //display the SMS received in the TextView
            textMessage = (TextView) findViewById(R.id.textMessage);


            //display the content of the received message in text view
            //SMSes.setText(intent.getExtras().getString("sms"));
            textMessage.setText(textMessage.getText()+"\n"+ intent.getExtras().getString("sms"));
        }
    };

    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Bundle extras = getIntent().getExtras(); String contactName="";
        if(extras != null)
            contactName = extras.getString("contactName");

        textMessage = (TextView) findViewById(R.id.textMessage);
        textMessage.setMovementMethod(ScrollingMovementMethod.getInstance());
        TextView tView = (TextView) findViewById(R.id.textView);
        tView.setText(contactName);
        //this.getSupportActionBar().setTitle(contactName);
        ImageView imgView = (ImageView) findViewById(R.id.imageView);
        imgView.setImageResource(R.drawable.contacts);
//
        eText = (EditText) findViewById(R.id.editText);
        eText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // clear text in edit text when user clicks on it
                eText.setText("");

            }
        });
//
        //an action to take in the future with same permission
        //as your application
        sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);

        deliveredPI = PendingIntent.getBroadcast(this, 0, new
                Intent(DELIVERED), 0);

        //intent to filter the action for SMS messages received
        intentFilter = new IntentFilter();
        intentFilter.addAction("SMS_RECEIVED_ACTION");

        //---register the receiver---
        registerReceiver(intentReceiver, intentFilter);
        //

    }



    //
    @Override
    public void onResume() {
        super.onResume();


        //---create the BroadcastReceiver when the SMS is sent---
        smsSentReceiver = new BroadcastReceiver(){

            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) //Retrieve the current result code,                      //as set by the previous receiver
                {
                    case Activity.RESULT_OK: Toast.makeText(getBaseContext(), "SMS sent",
                            Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE: Toast.makeText(getBaseContext(), "Generic failure",
                            Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE: Toast.makeText(getBaseContext(), "No service",
                            Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU: Toast.makeText(getBaseContext(), "Null PDU",
                            Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF: Toast.makeText(getBaseContext(), "Radio off",
                            Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        //---create the BroadcastReceiver when the SMS is delivered---
        smsDeliveredReceiver = new BroadcastReceiver(){

            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK: Toast.makeText(getBaseContext(), "SMS delivered",
                            Toast.LENGTH_LONG).show();
                        break;
                    case Activity.RESULT_CANCELED: Toast.makeText(getBaseContext(), "SMS not delivered",
                            Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };

        //---register the two BroadcastReceivers---
        registerReceiver(smsDeliveredReceiver, new IntentFilter(DELIVERED));
        registerReceiver(smsSentReceiver, new IntentFilter(SENT));
    }

    @Override

    public void onPause() {
        super.onPause();

        //---unregister the two BroadcastReceivers---
        unregisterReceiver(smsSentReceiver);
        unregisterReceiver(smsDeliveredReceiver);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        //---unregister the receiver---
        unregisterReceiver(intentReceiver);
    }


    //
    public void sendMessage(View v)
    {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_DENIED) {

                Log.d("permission", "permission denied to SEND_SMS - requesting it");
                String[] permissions = {Manifest.permission.SEND_SMS};

                requestPermissions(permissions, PERMISSION_REQUEST_CODE);

            }
        }

        eText = findViewById(R.id.editText);

        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        //telephonyManager.getDeviceId();
//        if (Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID).contains("5556")) {
//            sendSMS("5554", eText.getText().toString());
//        }
        //sendSMS("5556", eText.getText().toString());
        sendSMS("5554", eText.getText().toString());

        textMessage.setText(textMessage.getText()+"\n"+ eText.getText());

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //TODO
                }
                break;

            default:
                break;
        }
    }

    //sends an SMS message to another device
    private void sendSMS(String phoneNumber, String message)
    {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_DENIED) {

                Log.d("permission", "permission denied to SEND_SMS - requesting it");
                String[] permissions = {Manifest.permission.SEND_SMS};

                requestPermissions(permissions, PERMISSION_REQUEST_CODE);

            }
        }

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }

    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_message, menu);
        return true;
    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
// Handle action bar item clicks here. The action bar will
// automatically handle clicks on the Home/Up button, so long
// as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//noinspection SimplifiableIfStatement
//if (id == R.id.action_settings) {
//    return true;
//}

        return super.onOptionsItemSelected(item);
    }


}
