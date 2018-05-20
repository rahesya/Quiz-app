package com.acadview.www.aq;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Login extends AppCompatActivity {

    private static final String TAG = "PhoneAuthActivity";
    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private static final String Uname = "Username";
    private static final String Pword = "Password";
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private FirebaseAuth mAuth;

    SharedPreferences userdetails;

    boolean verificationdone = false;

    Button Bsign_in, Bresend, Bok;
    TextView Tsign_up, forgot;
    EditText Euname, Epaswd, Ephone, EOtp;
    ImageView Iunme, Ipswd, Iphn;

    FirebaseDatabase database;
    DatabaseReference users;

    public void onsignupTap(View v) {
        if (Ephone.getText().toString().length() != 10) {
            Toast.makeText(Login.this, "Phone", Toast.LENGTH_LONG).show();
        } else {
            findViewById(R.id.pbar).setVisibility(View.VISIBLE);
            final User user = new User(Epaswd.getText().toString(), Euname.getText().toString(), Ephone.getText().toString());
            users.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.child(user.getUsername()).exists()) {
                            findViewById(R.id.pbar).setVisibility(View.INVISIBLE);
                            Toast.makeText(Login.this, "username taken", Toast.LENGTH_LONG).show();
                        } else {
                            startPhoneNumberVerification(Ephone.getText().toString());
                            findViewById(R.id.pbar).setVisibility(View.INVISIBLE);
                            showPopup();
                        }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public void onforgotTap(View v) {


    }

    private void startPhoneNumberVerification(String phoneNumber) {
        setUpVerificationCallbacks();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

        mVerificationInProgress = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_login);

        View progress_loop = (ProgressBar) findViewById(R.id.pbar);
        progress_loop.bringToFront();

        findViewById(R.id.pbar).setVisibility(View.INVISIBLE);
        mAuth = FirebaseAuth.getInstance();

        Bsign_in = (Button) findViewById(R.id.SignInbt);


        Tsign_up = (TextView) findViewById(R.id.signuptv);
        forgot = (TextView) findViewById(R.id.forgottv);


        Euname = (EditText) findViewById(R.id.usernameet);
        Epaswd = (EditText) findViewById(R.id.passwordet);
        Ephone = (EditText) findViewById(R.id.phoneet);


        Ipswd = (ImageView) findViewById(R.id.passwordic);
        Iunme = (ImageView) findViewById(R.id.useric);
        Iphn = (ImageView) findViewById(R.id.phoneic);

        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");

        userdetails = getSharedPreferences("Users_info", Context.MODE_PRIVATE);

        final SharedPreferences.Editor edit = userdetails.edit();

        if (userdetails.contains(Uname) && userdetails.contains(Pword)) {

            Euname.setText(userdetails.getString(Uname, null));
            Epaswd.setText(userdetails.getString(Pword, null));
        }

        Bsign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.pbar).setVisibility(View.VISIBLE);
                signIn(Epaswd.getText().toString(), Euname.getText().toString());
            }

            private void signIn(final String password, final String username) {
                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (username.isEmpty() != true) {
                            if (dataSnapshot.child(username).exists()) {
                                User login = dataSnapshot.child(username).getValue(User.class);
                                if (login.getPassword().equals(password)) {

                                    edit.putString(Uname, Euname.getText().toString());
                                    edit.putString(Pword, Epaswd.getText().toString());

                                    edit.apply();

                                    Intent intent = new Intent(Login.this, Catalog.class);
                                    startActivity(intent);
                                } else {
                                    findViewById(R.id.pbar).setVisibility(View.INVISIBLE);
                                    Toast.makeText(Login.this, "either username or password is wrong", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                findViewById(R.id.pbar).setVisibility(View.INVISIBLE);
                                Toast.makeText(Login.this, "Have'nt you Signed up yet!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            findViewById(R.id.pbar).setVisibility(View.INVISIBLE);
                            Toast.makeText(Login.this, "Please enter your username", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

    }

    private void setUpVerificationCallbacks() {

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

                Log.d(TAG, "onVerificationCompleted:" + credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {

                } else if (e instanceof FirebaseTooManyRequestsException) {

                }

            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {

                Log.d(TAG, "onCodeSent:" + verificationId);

                mVerificationId = verificationId;
                mResendToken = token;

                // ...
            }
        };
    }

    public void verifyCode() {
        String code = EOtp.getText().toString();
        final User user = new User(Epaswd.getText().toString(), Euname.getText().toString(), Ephone.getText().toString());
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        verificationdone =true;
        users.child(user.getUsername()).setValue(user);
        Toast.makeText(this, " OTP Verification done", Toast.LENGTH_LONG).show();
        Toast.makeText(Login.this, "User Registration success!", Toast.LENGTH_LONG).show();

    }

    public void resendCode() {

        String PhoneNumber = Ephone.getText().toString();

        setUpVerificationCallbacks();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                PhoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,
                mResendToken);

    }

    private boolean showPopup() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        builder.setTitle("Enter OTP");
        builder.setMessage("An OTP has been sent to Phone number");

        View view =getLayoutInflater().inflate(R.layout.otp,null);

        builder.setView(view);
        final AlertDialog otpalert_dialog=builder.create();

        EOtp = (EditText)view.findViewById(R.id.otpet);
        Bresend = (Button) view.findViewById(R.id.resend);
        Bok = (Button) view.findViewById(R.id.ok);

        Bresend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendCode();
            }
        });
        Bok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!verificationdone){
                    verifyCode();
                    otpalert_dialog.dismiss();
                }
                else{
                    Toast.makeText(Login.this,"OTP Incorrect",Toast.LENGTH_LONG).show();
                }
            }
        });

        otpalert_dialog.show();
        return verificationdone;
    }
}