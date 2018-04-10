package application.adhiraj.testscroll;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

import static application.adhiraj.testscroll.R.id.servicesChosen;

public class AppointmentBookActivity extends AppCompatActivity{

    TextView mDate, mTime;
    private int year, month, day;
    private TimePicker timePicker;
    private int hour, min;
    boolean hour24 = true;
    Calendar calendar;
    public static String name, time, date, servicesAdd;
    public static StringBuilder priceBuilder;
    private int LAUNCH_CODE = 1;
    TextView servicesChosenTextView;
    private int counter = 0;
    private boolean done = false;
    private EditText mNameEditText;
    public static int SUMMARY_CODE = 2;
    public static int hourSet, yearSet, daySet, monthSet, timeSet;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_book);

        mDate = (TextView) findViewById(R.id.selectDateAppointment);
        mTime = (TextView) findViewById(R.id.selectTimeAppointment);

        //Set the date automatically
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
//        showDate(year, month+1, day);

        firebaseAuth = FirebaseAuth.getInstance();
        mNameEditText = (EditText) findViewById(R.id.BookNameEditText);
        mNameEditText.setText(firebaseAuth.getCurrentUser().getDisplayName());

        servicesChosenTextView = (TextView) findViewById(servicesChosen);

        if (getIntent().getFlags() == SUMMARY_CODE) {
            addServices();
            mNameEditText.setText(name);
            mDate.setText(date);
            mTime.setText(time);
        }

        BroadcastReceiver broadcast_reciever = new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent intent) {
                String action = intent.getAction();
                if (action.equals("finish_activity")) {
                    finish();
                    // DO WHATEVER YOU WANT.
                }
            }
        };
        registerReceiver(broadcast_reciever, new IntentFilter("finish_activity"));
    }

    public void selectServices (View view){
        startActivityForResult(new Intent(this, SelectServiceCatigory.class), LAUNCH_CODE);
    }

    @SuppressWarnings("deprecation")
    public void showCallender (View view){
        showDialog(999);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }else if (id == 888){
            //Return timepicker diaouge
            return new TimePickerDialog(this, myTimeListner, hour, min, hour24);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int year, int month, int day) {
                    yearSet = year;
                    monthSet = month+1;
                    daySet = day;
                    showDate(year, month+1, day);
                }
            };

    private void showDate(int year, int month, int day) {
        date = new StringBuilder().append(day).append("/").append(month).append("/").append(year).toString();
        mDate.setText(date);
    }
    private void showTime (String hour, String min){
        time = new StringBuilder().append(hour).append(":").append(min).toString();
        mTime.setText(time);
    }

    private TimePickerDialog.OnTimeSetListener myTimeListner = new
            TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String hour;
                    String min;
                    hourSet = hourOfDay;
                    timeSet = minute;

                    if (hourOfDay < 10){
                        hour = new StringBuilder().append("0").append(hourOfDay).toString();
                    }else{
                        hour = new StringBuilder().append(hourOfDay).toString();
                    }

                    if (minute < 10){
                        min = new StringBuilder().append("0").append(minute).toString();
                    }else{
                        min = new StringBuilder().append(minute).toString();
                    }
                    showTime(hour, min);
                }
            };

    public void showTime (View view){
        showDialog(888);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_CODE){
            addServices();
        }else if(requestCode == SUMMARY_CODE){
            addServices();
        }
    }

    private void addServices() {
        if (ServicesActivity.servicesAdded.size() != 0){
            TextView textView = (TextView) findViewById(R.id.BookServiceEditText);
            textView.setText("Services Added:");

            if (!done){
                Service service = ServicesActivity.servicesAdded.get(0);
                servicesChosenTextView.append(service.getName());;
                done = true;
            }

            if (counter == 0) {
                for (int i = 1; i < ServicesActivity.servicesAdded.size(); i++) {
                    Service service = ServicesActivity.servicesAdded.get(i);
                    servicesChosenTextView.append("\n"+service.getName());
                    counter++;
                }counter++;
            }else {
                for (int i = counter; i < ServicesActivity.servicesAdded.size(); i++) {
                    Service service = ServicesActivity.servicesAdded.get(i);
                    servicesChosenTextView.append("\n"+service.getName());
                    counter++;
                }
            }
        }else{
            TextView textView = (TextView) findViewById(R.id.BookServiceEditText);
            textView.setText("Tap to add a service");

            servicesChosenTextView.setText("");
        }
    }

    public void LaunchSummary (View view){
        mNameEditText = (EditText) findViewById(R.id.BookNameEditText);
        name = mNameEditText.getText().toString();
        servicesAdd = servicesChosenTextView.getText().toString();

        if (name.isEmpty()){
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (time == null){
            Toast.makeText(this, "Choose a time", Toast.LENGTH_SHORT).show();
            return;
        }

        if (servicesAdd.isEmpty()){
            Toast.makeText(this, "Please choose at least one service", Toast.LENGTH_SHORT).show();
            return;
        }

        startActivity(new Intent(this, CheckoutSummary.class));
    }

    public void delete (View view){
        mNameEditText = (EditText) findViewById(R.id.BookNameEditText);
        name = mNameEditText.getText().toString();
        servicesAdd = servicesChosenTextView.getText().toString();

        startActivity(new Intent(this, EditServicesActivity.class));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
