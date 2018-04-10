package application.adhiraj.testscroll;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import static application.adhiraj.testscroll.AppointmentBookActivity.date;
import static application.adhiraj.testscroll.AppointmentBookActivity.hourSet;
import static application.adhiraj.testscroll.AppointmentBookActivity.name;
import static application.adhiraj.testscroll.AppointmentBookActivity.timeSet;
import static application.adhiraj.testscroll.ServicesActivity.servicesAdded;

public class CheckoutSummary extends AppCompatActivity {

    private static final int PHONE_CALL = 2;
    TextView mName, mDate, mTime, mSummary, mPrice;
    int counter = 0;
    boolean done = false;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseRefrence, mDatabaseUsersRef;
    private AlarmManager alarmMgr;
    private Appointment appointment;
    private PendingIntent alarmIntent;
    private FirebaseAuth firebaseAuth;
    boolean doneCal = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_summary);

        setTitle("Summary");

        if (doneCal){
            startActivity(new Intent(this, MainActivity.class));
        }

        mName = (TextView) findViewById(R.id.showName);
        mDate = (TextView) findViewById(R.id.showDate);
        mTime = (TextView) findViewById(R.id.showTime);
        mSummary = (TextView) findViewById(R.id.showServices);
        mPrice = (TextView) findViewById(R.id.servicesPrice);

        mName.setText(name);
        mDate.setText(date);
        mTime.setText(AppointmentBookActivity.time);
        mSummary.setText(AppointmentBookActivity.servicesAdd);

        firebaseAuth = FirebaseAuth.getInstance();

        String userName = firebaseAuth.getCurrentUser().getDisplayName();
        String modUserName = userName.replace(' ','-');

        String userEmail = firebaseAuth.getCurrentUser().getEmail();
        String modUserEmail1 = userEmail.replace('.','-');
        String modUserEmail2 = modUserEmail1.replace('#','-');
        String modUserEmail3 = modUserEmail2.replace('_','-');
        String modUserEmail4 = modUserEmail3.replace('@','-');

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseRefrence = mFirebaseDatabase.getReference().child("appointments");
        mDatabaseUsersRef = mFirebaseDatabase.getReference().child("users").child(modUserEmail4);


        if (servicesAdded.size() != 0){
            if (!done){
                Service service = servicesAdded.get(0);
                mPrice.append(service.getPrice());;
                done = true;
            }

            if (counter == 0) {
                for (int i = 1; i < servicesAdded.size(); i++) {
                    Service service = servicesAdded.get(i);
                    mPrice.append("\n"+service.getPrice());
                    counter++;
                }counter++;
            }else {
                for (int i = counter; i < servicesAdded.size(); i++) {
                    Service service = servicesAdded.get(i);
                    mPrice.append("\n"+service.getPrice());
                    counter++;
                }
            }
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, AppointmentBookActivity.class);
        intent.addFlags(AppointmentBookActivity.SUMMARY_CODE);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent = new Intent(this, AppointmentBookActivity.class);
                intent.addFlags(AppointmentBookActivity.SUMMARY_CODE);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void bookAppointment(View view){
        Intent intent = new Intent("finish_activity");
        sendBroadcast(intent);

        int id = genertaeID();
        appointment = new Appointment(AppointmentBookActivity.name,
                date,
                AppointmentBookActivity.time,
                AppointmentBookActivity.servicesAdd, id, null, null);

        mDatabaseRefrence.push().setValue(appointment);
        mDatabaseUsersRef.push().setValue(appointment);

        Toast.makeText(this, "Appointment Booked Successfully", Toast.LENGTH_LONG).show();

        sameDayNotify(this);

        longTermNotify(this);

        feedbackNotify(this);

        //startActivity(new Intent(CheckoutSummary.this, AboutUsActivity.class));

        notifyUser();

        showDialogBox();
    }

    private void notifyUser (){

        String date = appointment.getDate();
        String time = appointment.getTime();
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.final_icon)
                        .setContentTitle("We are waiting for you!")
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.final_icon))
                        .setColor(getResources().getColor(R.color.colorPrimary))
                        .setContentText(date+" at "+time);
        Intent resultIntent = new Intent(this, AboutUsActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(AboutUsActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

//        NotificationCompat.InboxStyle inboxStyle =
//                new NotificationCompat.InboxStyle();
//        String[] events = new String[6];
//        // Sets a title for the Inbox in expanded layout
//        inboxStyle.setBigContentTitle("Appointment Details");
//
//        // Moves events into the expanded layout
//        for (int i=0; i < events.length; i++) {
//            inboxStyle.addLine(events[i]);
//        }
//
//        mBuilder.setStyle(inboxStyle);

        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(appointment.getID(), mBuilder.build());
    }

    private int genertaeID (){
        int min = 0;
        int max = 100000;

        Random r = new Random();
        return r.nextInt(max - min + 1) + min;
    }

    public void longTermNotify (Context context){
        Calendar cal=Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.YEAR, AppointmentBookActivity.yearSet);
        cal.set(Calendar.MONTH, AppointmentBookActivity.monthSet-1);
        if (AppointmentBookActivity.daySet == 1){
            cal.set(Calendar.DAY_OF_MONTH, 30);
        }else{
            cal.set(Calendar.DAY_OF_MONTH, AppointmentBookActivity.daySet-1);
        }
        cal.set(Calendar.HOUR_OF_DAY,AppointmentBookActivity.hourSet);
        cal.set(Calendar.MINUTE, timeSet);
        Log.e("Hour set is ", ""+hourSet);
        Log.e("UNIX Time Stamp is ", ""+cal.getTimeInMillis());

        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmRecieverDayBef.class);

        ArrayList<String> data = new ArrayList<String>();
        data.add(appointment.getDate());
        data.add(appointment.getTime());
        data.add(Integer.toString(appointment.getID()));
        intent.putExtra("details", data);

        alarmIntent = PendingIntent.getBroadcast(context, appointment.getID()+1, intent, 0);

        alarmMgr.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), alarmIntent);

//        Log.e("UNIX Time stamp is: ", ""+cal.getTimeInMillis());
//        Log.e("UNIX Year is: ", ""+yearSet);
//        Log.e("UNIX Month is: ", ""+monthSet);
//        Log.e("UNIX Day is: ", ""+daySet);
    }

    public void sameDayNotify (Context context){
        Calendar cal=Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.YEAR, AppointmentBookActivity.yearSet);
        cal.set(Calendar.MONTH, AppointmentBookActivity.monthSet-1);
        cal.set(Calendar.DAY_OF_MONTH, AppointmentBookActivity.daySet);
        cal.set(Calendar.HOUR_OF_DAY,AppointmentBookActivity.hourSet-5);
        cal.set(Calendar.MINUTE, timeSet);

        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmRecieverDayBef.class);

        ArrayList<String> data = new ArrayList<String>();
        data.add(appointment.getDate());
        data.add(appointment.getTime());
        data.add(Integer.toString(appointment.getID()));

        intent.addFlags(1);
        intent.putExtra("details", data);

        alarmIntent = PendingIntent.getBroadcast(context, appointment.getID(), intent, 0);

//        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
//                1000 * 60 * 60 * 4, alarmIntent);
        alarmMgr.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), alarmIntent);
        Log.e("Time is MS is ", ""+cal.getTimeInMillis());

    }

    public void feedbackNotify (Context context){
        Calendar cal=Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis()+1000*3);
        cal.set(Calendar.YEAR, AppointmentBookActivity.yearSet);
        cal.set(Calendar.MONTH, AppointmentBookActivity.monthSet-1);
        cal.set(Calendar.DAY_OF_MONTH, AppointmentBookActivity.daySet);
        cal.set(Calendar.HOUR_OF_DAY,AppointmentBookActivity.hourSet-5);
        cal.set(Calendar.MINUTE, timeSet);

        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, FeedbackNotifyReciever.class);

        intent.putExtra("id", appointment.getID());
        Log.e("ID Given is: ", ""+appointment.getID());

        alarmIntent = PendingIntent.getBroadcast(context, appointment.getID(), intent, 0);

//        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
//                1000 * 60 * 60 * 4, alarmIntent);
        alarmMgr.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), alarmIntent);
        Log.e("Feedback Time is MS is ", ""+cal.getTimeInMillis());

    }

    private void showDialogBox (){
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Save Event to Calender?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int permissionCheck = ContextCompat.checkSelfPermission(CheckoutSummary.this, Manifest.permission.WRITE_CALENDAR);
                if (permissionCheck == PackageManager.PERMISSION_GRANTED){
                    addToCalender();
                    doneCal = true;
                    finish();
                }else{
                    checkCallenderPermission();
                    doneCal = true;
                    finish();
                }
                //addToCalender();
                //startActivity(new Intent(CheckoutSummary.this, MainActivity.class));
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null){
                    dialog.dismiss();
                    startActivity(new Intent(CheckoutSummary.this, MainActivity.class));
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void addToCalender() {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType("vnd.android.cursor.item/event");

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.YEAR, AppointmentBookActivity.yearSet);
        cal.set(Calendar.MONTH, AppointmentBookActivity.monthSet-1);
        cal.set(Calendar.DAY_OF_MONTH, AppointmentBookActivity.daySet);
        cal.set(Calendar.HOUR_OF_DAY,AppointmentBookActivity.hourSet);
        cal.set(Calendar.MINUTE, timeSet);

        Calendar calE = Calendar.getInstance();
        calE.setTimeInMillis(System.currentTimeMillis());
        calE.set(Calendar.YEAR, AppointmentBookActivity.yearSet);
        calE.set(Calendar.MONTH, AppointmentBookActivity.monthSet-1);
        calE.set(Calendar.DAY_OF_MONTH, AppointmentBookActivity.daySet);
        calE.set(Calendar.HOUR_OF_DAY,AppointmentBookActivity.hourSet+1);
        calE.set(Calendar.MINUTE, timeSet);

        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, cal.getTimeInMillis());
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,calE.getTimeInMillis());
        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false);
        intent.putExtra(CalendarContract.Events.TITLE, "Salon Appointment");
        intent.putExtra(CalendarContract.Events.DESCRIPTION, "This is a sample description");
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, "Rummy's Salon");

        checkCallenderPermission();

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED){
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);;
            }else{
                Toast.makeText(CheckoutSummary.this, "Failed to add to calender.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void checkCallenderPermission (){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_CALENDAR},
                    PHONE_CALL);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PHONE_CALL: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    addToCalender();
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}

