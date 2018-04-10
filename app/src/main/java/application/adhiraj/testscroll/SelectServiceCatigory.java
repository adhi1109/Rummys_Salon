package application.adhiraj.testscroll;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class SelectServiceCatigory extends AppCompatActivity {

    private int RESULT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_service_catigory);
        setTitle("Select Service");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.services_menue, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.SaveReminder) {
            //Toast.makeText(this, "You Pressed Save", Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void hairClick (View view){
        Intent intent = new Intent(this, ServicesActivity.class);
        intent.putExtra("name", 1);
        intent.addFlags(StaffPageLauncher.hairLaunch);
        startActivityForResult(intent, RESULT);
    }

    public void makeUpClick (View view){
        Intent intent = new Intent(this, ServicesActivity.class);
        intent.putExtra("name", 1);
        intent.addFlags(StaffPageLauncher.makeUpLaunch);
        startActivityForResult(intent, RESULT);
    }

    public void BeautyClick (View view){
        Intent intent = new Intent(this, ServicesActivity.class);
        intent.putExtra("name", 1);
        intent.addFlags(StaffPageLauncher.beautyLaunch);
        startActivityForResult(intent, RESULT);
    }

    public void NailsClick (View view){
        Intent intent = new Intent(this, ServicesActivity.class);
        intent.putExtra("name", 1);
        intent.addFlags(StaffPageLauncher.nailsLaunch);
        startActivityForResult(intent, RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT){
            //Toast.makeText(this, "reached From Lauch code", Toast.LENGTH_SHORT).show();
            //super.onBackPressed();
        }
    }
}
