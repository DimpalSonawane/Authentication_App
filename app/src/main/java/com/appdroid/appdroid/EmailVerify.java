package com.appdroid.appdroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class EmailVerify extends AppCompatActivity {

    TextView textView;

    EditText txtemail;

    Button button;
    
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    
    String verificationId, docID;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verify);

        textView = findViewById(R.id.textView);
        txtemail = findViewById(R.id.txtemail);
        button = findViewById(R.id.btn_check);
        verificationId = getIntent().getStringExtra("OTPID");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String OTPValue = txtemail.getText().toString();
               Log.w("ssssssssss","Edit Text Value:"+OTPValue);
               verifyOTP(OTPValue);
            }
        });
    }

    private void verifyOTP(String otpValue) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otpValue);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                //Log.d("ssss", "signInWithCredential:success");

                                //FirebaseUser user = task.getResult().getUser();
                                // Update UI
                                Toast.makeText(EmailVerify.this, "OTP Verified...", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(EmailVerify.this, ResetPassword.class);
                                i.putExtra("docID",docID);
                                startActivity(i);
                                finish();
                            } else {
                                // Sign in failed, display a message and update the UI
                                Log.w("ssss", "signInWithCredential:failure", task.getException());
                                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                    // The verification code entered was invalid
                                }
                            }
                        }
                    });
        }

}