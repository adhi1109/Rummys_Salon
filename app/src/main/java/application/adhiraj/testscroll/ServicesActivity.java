package application.adhiraj.testscroll;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ServicesActivity extends AppCompatActivity {

    private ServiceListAdaptr mAdapter;
    private int[] checked = new int[100];
    private DatabaseReference mDatabase;
    private ChildEventListener mChildEventListener;
    private ProgressDialog progressDialog;
    private int counter =0;

    public static ArrayList<Service> servicesAdded = new ArrayList<Service>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        for (int i=0; i<100;i++){
            checked[i] =0;
        }

        int from = getIntent().getIntExtra("name", 0);

        if (from == 2){
            setTitle("Services Offered");
        }else {
            setTitle("Select Services");
        }


        final ArrayList<Service> services = new ArrayList<Service>();

        mAdapter = new ServiceListAdaptr(this, services);

        ListView listView = (ListView) findViewById(R.id.listview);

        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Service service = mAdapter.getItem(position);
                //makeToast();
                if (checked[position] == 0){
                    // Selected
                    view.findViewById(R.id.chckboxServiceChosen).setVisibility(View.VISIBLE);
                    checked[position] = 1;
                    servicesAdded.add(service);
                }else if(checked[position] == 1){
                    // Unselected
                    view.findViewById(R.id.chckboxServiceChosen).setVisibility(View.INVISIBLE);
                    checked[position] = 0;
                    servicesAdded.remove(service);
                }
            }
        });

        int catigory = getIntent().getFlags();
        switch (catigory) {
            case (StaffPageLauncher.hairLaunch):
                attachDatabase("hair");
                return;
            case (StaffPageLauncher.makeUpLaunch):
                attachDatabase("make-up");
                return;
            case (StaffPageLauncher.nailsLaunch):
                attachDatabase("nails");
                return;
            case (StaffPageLauncher.beautyLaunch):
                attachDatabase("beauty");
                return;
        }

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

    private void attachDatabase (String catigory){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("services").child(catigory);

        mChildEventListener = new ChildEventListener() {
           @Override
           public void onChildAdded(DataSnapshot dataSnapshot, String s) {
               Service service = dataSnapshot.getValue(Service.class);
               mAdapter.add(service);
               counter++;
               Log.e("Reached Here", ""+counter+" Times");
               progressDialog.dismiss();
           }
           @Override
           public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
           @Override
           public void onChildRemoved(DataSnapshot dataSnapshot) {}
           @Override
           public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
           @Override
           public void onCancelled(DatabaseError databaseError) {}
        };
        mDatabase.addChildEventListener(mChildEventListener);
    }

}
