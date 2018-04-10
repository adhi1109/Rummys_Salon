package application.adhiraj.testscroll;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Adhiraj on 09/06/17.
 */

public class EditServicesActivity extends AppCompatActivity {
    private ServiceListAdaptr mAdapter;

    private ArrayList<Service> servicesAdded = new ArrayList<Service>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        setTitle("Edit Services");

        servicesAdded = ServicesActivity.servicesAdded;

        Toast.makeText(this, "Tap to remove a service", Toast.LENGTH_LONG).show();

        mAdapter = new ServiceListAdaptr(this, ServicesActivity.servicesAdded);

        ListView listView = (ListView) findViewById(R.id.listview);

        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Service service = mAdapter.getItem(position);
                ServicesActivity.servicesAdded.remove(service);
                mAdapter.remove(service);
                Toast.makeText(EditServicesActivity.this, "Removed", Toast.LENGTH_SHORT).show();
            }
        });
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
            Intent intent = new Intent(this, AppointmentBookActivity.class);
            intent.addFlags(AppointmentBookActivity.SUMMARY_CODE);
            startActivity(intent);

        } else if(id == android.R.id.home){
            DialogInterface.OnClickListener discardBurttonClickListner =
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // if the user clciekd disacrd naviagate up to the parent activity
                            ServicesActivity.servicesAdded = servicesAdded;
                            Intent intent = new Intent(EditServicesActivity.this, AppointmentBookActivity.class);
                            intent.addFlags(AppointmentBookActivity.SUMMARY_CODE);
                            startActivity(intent);
                        }
                    };
            showDialogBox(discardBurttonClickListner);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialogBox (DialogInterface.OnClickListener discardButtonClickListener){
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Discard your changes and quit editing?");
        builder.setPositiveButton("Discard", discardButtonClickListener);
        builder.setNegativeButton("Keep Editing", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null){
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
