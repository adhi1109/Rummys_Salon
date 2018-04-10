package application.adhiraj.testscroll;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUpActivity extends AppCompatActivity {

    private AutoCompleteTextView mEmailEditText;
    private EditText mPasswordEditText, mPasswordRepeatEditText, mNameEditText, mPhoneNumberEditText;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Set up the login form.
        mEmailEditText = (AutoCompleteTextView) findViewById(R.id.emailSignUp);
        mPasswordEditText = (EditText) findViewById(R.id.passwordSignUp);
        mPasswordRepeatEditText = (EditText) findViewById(R.id.passwordRepeat);
        mNameEditText = (EditText) findViewById(R.id.fullName);
        mPhoneNumberEditText = (EditText) findViewById(R.id.PhoneNumber);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button_Sign_up);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing in...");

        if (firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
        }

    }

    public void signUp (View view){
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void signUpUser() {
            progressDialog.show();

            String email = mEmailEditText.getText().toString().trim();
            String password = mPasswordEditText.getText().toString();

            if (TextUtils.isEmpty(email)){
                Toast.makeText(SignUpActivity.this, "Please enter an email ID", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)){
                Toast.makeText(SignUpActivity.this, "Please enter an password", Toast.LENGTH_SHORT).show();
                return;
            }

            progressDialog.setMessage("Registering User. Please wait");
            progressDialog.show();

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()){
                                Toast.makeText(SignUpActivity.this, "Sign in Successful", Toast.LENGTH_SHORT).show();
                                FirebaseUser user = firebaseAuth.getCurrentUser();

                                UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(mNameEditText.getText().toString().trim()).build();

                                user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("", "User profile updated.");
                                        }
                                    }
                                });
                                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                            }else {
                                Toast.makeText(SignUpActivity.this, "Failed to Sign in", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

    }

    public void signUpUserCheck (View view){
        if (mPasswordEditText.getText().toString().equals(mPasswordRepeatEditText.getText().toString())){
            if (mPhoneNumberEditText.getText().toString().isEmpty()){
                Toast.makeText(this, "Please Enter a Phone Number", Toast.LENGTH_SHORT).show();
            }else {
                if(isValidMobile(mPhoneNumberEditText.getText().toString().trim())) {
                    if (isValidMail(mEmailEditText.getText().toString().trim())){
                        if (mNameEditText.getText().toString().isEmpty()){
                            Toast.makeText(this, "Please enter your full name", Toast.LENGTH_SHORT).show();
                        }else {
                            signUpUser();
                        }
                    }else {
                        Toast.makeText(this, "Invalid E-mail ID", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                }
            }
        }else{
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    private boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}