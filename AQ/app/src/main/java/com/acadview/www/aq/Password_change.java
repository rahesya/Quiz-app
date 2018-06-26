package com.acadview.www.aq;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class Password_change extends AppCompatActivity {

    EditText newpassword, reenternewpassword, username, EOtp;
    Button changepassword, Bresend, Bok;

    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String TAG = "PhoneAuthentic";
    private String mVerificationId;

    final String[] PhoneNumber = new String[1];
    private FirebaseAuth mAuth;
    Boolean verificationdone;

    FirebaseDatabase database;
    DatabaseReference user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = database.getReference("Users");

        newpassword = (EditText) findViewById(R.id.newpassword);
        username = (EditText) findViewById(R.id.username);
        reenternewpassword = (EditText) findViewById(R.id.reenternewpassword);
        changepassword = (Button) findViewById(R.id.changepassword);

        changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (newpassword.getText().toString().equals(reenternewpassword.getText().toString()) && username.getText().toString() != null) {

                    user.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(username.getText().toString()).exists()) {
                                User user = dataSnapshot.child(username.getText().toString()).getValue(User.class);
                                PhoneNumber[0] = "+91" + user.getPhone();
                                startPhoneNumberVerification(PhoneNumber[0]);
                                showPopup();
                            } else {
                                Snackbar snackbar = Snackbar.make(v, "Username does not exist", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else {
                    Toast.makeText(Password_change.this, "both password not same", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void startPhoneNumberVerification(String phoneNumber) {
        setUpVerificationCallbacks();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

    }

    private void showPopup() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Password_change.this);
        builder.setTitle("Enter Verification code");
        builder.setMessage("A verification code has been sent to registered phone number");

        View view = getLayoutInflater().inflate(R.layout.otp, null);

        builder.setView(view);
        final AlertDialog otpalert_dialog = builder.create();

        EOtp = (EditText) view.findViewById(R.id.otpet);
        Bresend = (Button) view.findViewById(R.id.resend);
        Bok = (Button) view.findViewById(R.id.ok);

        Bresend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Password_change.this, "verification code has been resend", Toast.LENGTH_LONG).show();
                startPhoneNumberVerification(PhoneNumber[0]);
            }
        });
        Bok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!verificationdone) {
                    verifyCode();
                    otpalert_dialog.dismiss();
                } else {
                    Toast.makeText(Password_change.this, "OTP Incorrect", Toast.LENGTH_LONG).show();
                }
            }
        });

        otpalert_dialog.show();

    }

    public void verifyCode() {
        String code = EOtp.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        verificationdone = true;
        signInWithPhoneAuthCredential(credential);

    }

    private void setUpVerificationCallbacks() {

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);

                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                // ...
            }
        };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user.child(username.getText().toString()).child("password").setValue(newpassword.getText().toString());
                            Toast.makeText(Password_change.this, "Password changed", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(Password_change.this, Login.class));
                            finish();

                        } else {

                            Toast.makeText(Password_change.this, "OTP incorrect", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}

