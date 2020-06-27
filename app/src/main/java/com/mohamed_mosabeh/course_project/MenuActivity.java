package com.mohamed_mosabeh.course_project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.app.LauncherActivity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import static com.mohamed_mosabeh.course_project.dao.DatabaseHelper.PRIM_ID;

public class MenuActivity extends AppCompatActivity {
    
    private TextView tv;
    private Button logout;
    public static String USERNAMEX;
    public static String TICKET_PERSON_NAME;
    public static int PRIMARY_ID;
    Toast toast;
    String vio ="placeholder";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        tv = findViewById(R.id.highUpTextView);
        logout = findViewById(R.id.logout);
        Intent intent = getIntent();
        // user name
        USERNAMEX = intent.getStringExtra("username");
        // full name
        TICKET_PERSON_NAME = intent.getStringExtra("fullname");
        PRIMARY_ID = PRIM_ID;
        tv.setText("Logged in as " + USERNAMEX + ":");
    }
    
    public void logout(View view) {
        new AlertDialog.Builder(this).setTitle("Confirm")
                .setMessage("Do you really want to log out?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }
    public void changeActivity(View view) {
        Intent intent = new Intent(this, FinesListActivity.class);
        intent.putExtra("name", "Mohamed");
        startActivity(intent);
    }
    
    public void generateRandomFine(View view) {
        
        SQLiteDatabase db = openOrCreateDatabase("dbMain", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS CarFines (id INTEGER PRIMARY KEY AUTOINCREMENT, date VARCHAR NOT NULL, amount REAL NOT NULL, ownerId INTEGER NOT NULL, ownerName VARCHAR NOT NULL, type VARCHAR NOT NULL, violation VARCHAR NOT NULL, description VARCHAR, Lat REAL NOT NULL, Lng REAL NOT NULL)");
        
        Random random = new Random();
        int ranNumber = random.nextInt(7);
        String[] cars = {"Black Volkswagen Beetle", "Jeep Grand Cherokee", "Toyota Highlander 2019", "Lexus RX 350", "Hyundai Kona", "Mercedes-Benz GLE-Class", "Chevrolet Silverado 1500"};
        String[] violations = {"Jaywalking", "Bypassing Red light", "Illegal Parking", "Hit and Run Incident", "Exceeding Speed Limit", "Driving in car pool lane", "Drunk Driving"};
        String[] descs = {"Pedestrians must wait the light to be green to pass the street", "Violator caught recklessly driving and not stopping for the red traffic light", "Offending party has incorrectly parked his car and hasn't paid the meter", "Offending party has hit another car in the parking and escaped from the scene", "offender caught over speeding and exceeded the speed limit by 40mph", "Violator drove in the car pool lane and interrupted the traffic, incident caught on camera", "Offending party caught drunk driving and entirely intoxicated, measured blood alcohol level 9.2mgh"};
        ContentValues values = new ContentValues();
        values.put("date", getDateNow());
        values.put("amount", (random.nextInt(20) + 1) * 100);
        values.put("ownerId", PRIMARY_ID);
        values.put("ownerName", USERNAMEX);
        values.put("type", random.nextInt(7));
        try {
    
            toast.cancel();
        } catch (Exception e) {
        
        }
        vio = violations[ranNumber];
        values.put("violation", violations[ranNumber]);
        values.put("description", descs[ranNumber]);
        toast = Toast.makeText(this,"Fine Added to Account:\n" + vio, Toast.LENGTH_SHORT);
        toast.show();

        //calling create notification
        createNortification("Fine Added to Account:\n" + vio,MenuActivity.this);
        /* TODO: Make me dynamic */
        values.put("Lat", 24.465135);
        values.put("Lng", 54.347118);
    
        //Try Catch Data insertion
        try {
            db.insertOrThrow("CarFines", null, values);
        } catch (Exception e) {
            Toast.makeText(this, "Entry Not Added", Toast.LENGTH_SHORT).show();
        }
    }
    public  void createNortification(String n, Context context){
        //Activity to open when notification is clicked
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("CHANNEL_1",
                    "FINES",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("FOR FINES");
            mNotificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "CHANNEL_1")
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle("Fine") // title for notification
                .setContentText(n)// message for notification
                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(0, mBuilder.build());
        /*Intent mainIntent = new Intent(context, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mainIntent, 0);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel mChannel = null;
        String idChannel="channel_1";
        // The id of the channel.

        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, null);
        builder.setContentTitle(context.getString(R.string.app_name))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .setContentText("Fine:"+n );

        //Creating a channell for android 0 and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(idChannel, context.getString(R.string.app_name), importance);
            // Configure the notification channel.
            mChannel.setDescription("Fines");
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mNotificationManager.createNotificationChannel(mChannel);
        } else {
            builder.setContentTitle(context.getString(R.string.app_name))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setColor(ContextCompat.getColor(context, R.color.transparent))
                    .setLights(Color.YELLOW, 500, 5000)
                    .setAutoCancel(true);
        }
        //display notification
        mNotificationManager.notify(1, builder.build());*/
    }
    
    private String getDateNow() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return formatter.format(date);
    }
    
    public void moveAccount(View view) {
        
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
    }
}
