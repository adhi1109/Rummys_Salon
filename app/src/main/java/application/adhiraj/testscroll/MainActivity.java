package application.adhiraj.testscroll;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

import static application.adhiraj.testscroll.AboutUsActivity.newFacebookIntent;
import static application.adhiraj.testscroll.AboutUsActivity.newInstagramProfileIntent;
import static application.adhiraj.testscroll.R.id.pager;

public class MainActivity extends AppCompatActivity {

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static final Integer[] XMEN= {R.drawable.one,R.drawable.two,R.drawable.three,R.drawable.four,R.drawable.five,R.drawable.six};
    private ArrayList<Integer> XMENArray = new ArrayList<Integer>();
    private FirebaseAuth firebaseAuth;
    private int RESULT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null){
            startActivity(new Intent(this, SignUpActivity.class));
        }

        setTitle("Rummy's Salon");

        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if (id == R.id.log_out){
            firebaseAuth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
        }else if (id == R.id.facebook_button){
            Intent fbIntent = newFacebookIntent(this.getPackageManager(), "https://www.facebook.com/Rummys.salon/");
            startActivity(fbIntent);
        }else if(id == R.id.instagram_button){
            Intent instaIntent = newInstagramProfileIntent(this.getPackageManager(), "https://www.instagram.com/rummys.salon/");
            startActivity(instaIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        for(int i=0;i<XMEN.length;i++)
            XMENArray.add(XMEN[i]);

        mPager = (ViewPager) findViewById(pager);
        mPager.setAdapter(new MyAdapter(MainActivity.this,XMENArray));
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == XMEN.length) {
                    //init();
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 0, 6000);
    }

    public void google (View view){
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.google.co.in/maps/place/About+You/@28.4720368,77.1018309,19.75z/data=!4m13!1m7!3m6!1s0x390d18d4d5624019:0x4b146296e4bd77cb!2sQutab+Plaza,+Ashoka+Cresent+Marg,+Block+A,+Sector+26A,+Sikanderpur+Ghosi,+Haryana+122022!3b1!8m2!3d28.4717348!4d77.1019058!3m4!1s0x0:0xa5342a3f5d1c546d!8m2!3d28.4725192!4d77.101678"));
        startActivity(intent);
    }

    public void hairClick (View view){
        Intent intent = new Intent(this, ServicesActivity.class);
        intent.putExtra("name", 2);
        intent.addFlags(StaffPageLauncher.hairLaunch);
        startActivityForResult(intent, RESULT);
    }

    public void makeUpClick (View view){
        Intent intent = new Intent(this, ServicesActivity.class);
        intent.putExtra("name", 2);
        intent.addFlags(StaffPageLauncher.makeUpLaunch);
        startActivityForResult(intent, RESULT);
    }

    public void BeautyClick (View view){
        Intent intent = new Intent(this, ServicesActivity.class);
        intent.putExtra("name", 2);
        intent.addFlags(StaffPageLauncher.beautyLaunch);
        startActivityForResult(intent, RESULT);
    }

    public void NailsClick (View view){
        Intent intent = new Intent(this, ServicesActivity.class);
        intent.putExtra("name", 2);
        intent.addFlags(StaffPageLauncher.nailsLaunch);
        startActivityForResult(intent, RESULT);
    }

    public void appointment (View view){
        startActivity(new Intent(this, AppointmentBookActivity.class));
    }

    public void about_us (View view){
        startActivity(new Intent(this, AboutUsActivity.class));
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        i.setAction(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        MainActivity.this.startActivity(i);
        finish();
    }
}
