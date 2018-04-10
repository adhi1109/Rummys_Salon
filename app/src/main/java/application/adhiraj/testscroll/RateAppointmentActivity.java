package application.adhiraj.testscroll;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.Map;

public class RateAppointmentActivity extends AppCompatActivity {

    private int id;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    private String comments, stars;
    EditText mCommentsEditText;
    RatingBar mRatingBar;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_appointment);

        setTitle("Rate Appointment");

        id = getIntent().getIntExtra("id", 1);
        Log.e("ID is: ", ""+ id);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading. Please wait");
        progressDialog.show();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("appointments");
        Query query = databaseReference.orderByChild("id").equalTo(id);

        mCommentsEditText = (EditText) findViewById(R.id.comments_appointment);
        mRatingBar = (RatingBar) findViewById(R.id.ratings_overaall);

        query(query);

    }

    private void query(Query query){
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                reference = dataSnapshot.getRef();
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
        });
    }

    public void submitFeedback (View view){
        submit(reference);
        startActivity(new Intent(RateAppointmentActivity.this, AboutUsActivity.class));
    }

    private void submit (DatabaseReference reference){
        comments = mCommentsEditText.getText().toString();
        stars = Float.toString(mRatingBar.getRating());

        Map<String, Object> updates = new HashMap<String, Object>();
        updates.put("feedback", comments);
        updates.put("stars", stars);

        reference.updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RateAppointmentActivity.this, "Thank you for the feedback", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(RateAppointmentActivity.this, "Failed to submit feedback. Try again later", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void addAppointment (View view){
        submit(reference);
        startActivity(new Intent(this, AppointmentBookActivity.class));
    }
}
