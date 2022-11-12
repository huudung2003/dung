package com.example.lab3;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    TextView txtcontenName, txtcontenNumber;
    Button getContent, SMS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtcontenName = findViewById(R.id.txtcontenName);
        txtcontenNumber = findViewById(R.id.txtcontenNumber);
        getContent = findViewById(R.id.getContent);
        SMS = findViewById(R.id.SMS);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getCallLogClick(View view){
        Boolean allowed = ActivityCompat
                .checkSelfPermission(MainActivity.this, Manifest.permission.READ_CALL_LOG)
                == PackageManager.PERMISSION_GRANTED;
        if (allowed) {
            StringBuffer sb = new StringBuffer();
            Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null,
                    null, null, null);
            int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
            int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
            int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
            int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
            sb.append("Call Details :");
            while (managedCursor.moveToNext()) {
                String phNumber = managedCursor.getString(number);
                String callType = managedCursor.getString(type);
                String callDate = managedCursor.getString(date);
                Date callDayTime = new Date(Long.valueOf(callDate));
                String callDuration = managedCursor.getString(duration);
                String dir = null;
                int dircode = Integer.parseInt(callType);
                switch (dircode) {
                    case CallLog.Calls.OUTGOING_TYPE:
                        dir = "OUTGOING";
                        break;

                    case CallLog.Calls.INCOMING_TYPE:
                        dir = "INCOMING";
                        break;

                    case CallLog.Calls.MISSED_TYPE:
                        dir = "MISSED";
                        break;
                }
                sb.append("\nPhone Number:--- " + phNumber + " \nCall Type:--- "
                        + dir + " \nCall Date:--- " + callDayTime
                        + " \nCall duration in sec :--- " + callDuration);
                sb.append("\n----------------------------------");
            }
            managedCursor.close();
            Log.d(">>>>>>>>>>>TAGCALL",""+sb);
        }
        else {
            requestPermissions(new String[]{Manifest.permission.READ_CALL_LOG},100);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getSMSClick(View view){
        Boolean allowed = ActivityCompat
                .checkSelfPermission(MainActivity.this, Manifest.permission.READ_SMS)
                == PackageManager.PERMISSION_GRANTED;
        if (allowed) {
            Uri allMessages = Uri.parse("content://sms/");
            //Cursor cursor = managedQuery(allMessages, null, null, null, null); Both are same
            Cursor cursor = this.getContentResolver().query(allMessages, null,
                    null, null, null);

            while (cursor.moveToNext()) {
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    Log.d(cursor.getColumnName(i) + "", cursor.getString(i) + "");
                }
                Log.d("One row finished",
                        "**************************************************");
            }
        }
        else {
            requestPermissions(new String[]{Manifest.permission.READ_SMS},100);
        }
    }


                        @Override
                        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                            switch (requestCode){
                                case 100: {
                                    if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                                        Uri allMessages = Uri.parse("content://sms/");
                                        //Cursor cursor = managedQuery(allMessages, null, null, null, null); Both are same
                                        Cursor cursor = this.getContentResolver().query(allMessages, null,
                                                null, null, null);

                                        while (cursor.moveToNext()) {
                                            Log.d(">>>>>>>>>>>>>>TAG" + "", cursor.getString(11) + "");
                                        }
                                    }
                                    else {
                                        Toast.makeText(MainActivity.this,"ko cho",Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                                }
                                default: break;
                            }
                        }

                    }

//


