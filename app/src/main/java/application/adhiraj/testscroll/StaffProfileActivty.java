package application.adhiraj.testscroll;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class StaffProfileActivty extends AppCompatActivity {

    String phone;
    private static final int PHONE_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_profile_activty);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call();
            }
        });

        TextView details = (TextView) findViewById(R.id.StaffDescriptionTxtView);
        ImageView image = (ImageView) findViewById(R.id.StaffDescriptionImage);
        RatingBar rating = (RatingBar) findViewById(R.id.StaffDescriptionRating);

        String string = getResources().getString(R.string.small_text);

        StaffProfile rummy = new StaffProfile("Rummy Chaddha", "This is rummy\n\n" + string, "9810497613", 4.5f, R.drawable.rummy);
        StaffProfile mathew = new StaffProfile("Mathew Beck", "This is mathew\n\n" + string, "9876543219", 4f, R.drawable.mathew);

        Intent intent = getIntent();
        int flag = intent.getFlags();

        //Toast.makeText(this, "The flag is: "+flag,Toast.LENGTH_SHORT).show();

        switch (flag) {
            case StaffPageLauncher.rummyLaunch:
                setTitle(rummy.getName());
                details.setText(rummy.getDescription());
                image.setImageResource(rummy.getPhoto());
                rating.setRating(rummy.getRating());
                phone = rummy.getNumber();
                return;

            case StaffPageLauncher.mathewLaunch:
                setTitle(mathew.getName());
                details.setText(mathew.getDescription());
                image.setImageResource(mathew.getPhoto());
                rating.setRating(mathew.getRating());
                phone = mathew.getNumber();
                return;
        }
    }

    private void call() {
        checkPhonePermission();

        int permissionCheck = ContextCompat.checkSelfPermission(StaffProfileActivty.this,
                Manifest.permission.CALL_PHONE);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
            startActivity(intent);
        }else{
            Toast.makeText(this, "Can't make call - Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkPhonePermission (){
        if (ContextCompat.checkSelfPermission(StaffProfileActivty.this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
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
                    call();
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
