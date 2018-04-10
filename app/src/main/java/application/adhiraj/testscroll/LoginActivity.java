package application.adhiraj.testscroll;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {


    // UI references.
    private AutoCompleteTextView mEmailEditText;
    private EditText mPasswordEditText;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmailEditText = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordEditText = (EditText) findViewById(R.id.password);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing in...");

        if (firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(LoginActivity.this, AboutUsActivity.class));
        }

    }

    public void signInUser (View view) {
        progressDialog.show();

        String email = mEmailEditText.getText().toString().trim();
        String password = mPasswordEditText.getText().toString();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(LoginActivity.this, "Please enter an email ID", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)){
            Toast.makeText(LoginActivity.this, "Please enter an password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Signing in. Please wait");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Sign in Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        }else {
                            Toast.makeText(LoginActivity.this, "Failed to Sign in", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void signUp (View view){
        startActivity(new Intent(this, SignUpActivity.class));
    }
}

