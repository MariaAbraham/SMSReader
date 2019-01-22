package com.manam.andorid.mobeeverification.smsreader;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final int permsRequestCode = 201;

    private List<Object> smsList;
    private final double hourInMilliSec = 3600000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (hasSMSPermission()) {
            fetchAndPopulateSMS();
            return;
        }
        requestSMSPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case permsRequestCode:
                boolean readSMSPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                boolean receiveSMSPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                if (readSMSPermission && receiveSMSPermission) {
                    fetchAndPopulateSMS();
                } else {
                    requestSMSPermission();
                }

        }

    }

    private void fetchAndPopulateSMS() {
        getAllSmsFromProvider();
        ListView listView = findViewById(R.id.list_sms);

        groupListBasedOnHours();
        smsList.removeAll(Collections.singleton(""));
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.zoom_in);

        SMSAdapter smsmAdapter = new SMSAdapter(this, smsList, getIntent().getStringExtra("NEW_SMS") == null ? null : animation);
        listView.setAdapter(smsmAdapter);
    }

    private void groupListBasedOnHours() {
        double currentTime = Calendar.getInstance().getTimeInMillis();

        for (int index = 0; index < smsList.size(); index++) {
            if (!(smsList.get(index) instanceof SMSModel)) {
                continue;
            }
            if (currentTime - (((SMSModel) smsList.get(index)).getDate()) > hourInMilliSec * 48) {
                smsList.set(index, "");
                continue;
            }
            String title = "0 hour ago";
            double itemTime = (((SMSModel) smsList.get(index)).getDate());
            if ((currentTime - itemTime) < hourInMilliSec * 1) {
                updateHeader(currentTime, index, 1, title);
                continue;
            }
            title = "1 hour ago";
            if ((currentTime - itemTime) < hourInMilliSec * 2) {
                updateHeader(currentTime, index, 2, title);
                continue;
            }
            title = "2 hours ago";
            if ((currentTime - itemTime) < hourInMilliSec * 3) {
                updateHeader(currentTime, index, 3, title);
                continue;
            }
            title = "3 hours ago";
            if ((currentTime - itemTime) < hourInMilliSec * 6) {
                updateHeader(currentTime, index, 6, title);
                continue;
            }
            title = "6 hours ago";
            if ((currentTime - itemTime) < hourInMilliSec * 12) {
                updateHeader(currentTime, index, 12, title);
                continue;
            }
            title = "12 hours ago";
            if ((currentTime - itemTime) < hourInMilliSec * 24) {
                updateHeader(currentTime, index, 24, title);
                continue;
            }
            title = "1 day ago";
            if ((currentTime - itemTime) < hourInMilliSec * 48) {
                updateHeader(currentTime, index, 48, title);
                continue;
            }
        }
    }

    private void updateHeader(double currentTime, int index, int hours, String title) {
        if (!(smsList.contains(title))) {
            smsList.add(smsList.indexOf(smsList.get(index)), title);
        }
    }

    public void getAllSmsFromProvider() {
        smsList = new ArrayList<>();
        ContentResolver cr = getContentResolver();

        Cursor c = cr.query(Telephony.Sms.Inbox.CONTENT_URI, // Official CONTENT_URI from docs
                new String[]{Telephony.Sms.Inbox.BODY, Telephony.Sms.Inbox.DATE}, // Select body text
                null,
                null,
                Telephony.Sms.Inbox.DEFAULT_SORT_ORDER); // Default sort order

        int totalSMS = c.getCount();

        if (c.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {
                smsList.add(new SMSModel(c.getString(0), c.getDouble(1)));
                c.moveToNext();
            }
        } else {
            throw new RuntimeException("No SMS in Inbox");
        }
        c.close();
    }

    private boolean hasSMSPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestSMSPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.READ_SMS,
                Manifest.permission.RECEIVE_SMS}, permsRequestCode);
    }
}
