package com.acadview.www.aq;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class Login extends Activity {

    private static final String TAG = "PhoneAuthActivity";
    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private static final String Uname = "Username";
    private static final String Pword = "Password";
    private String backImage,profile_image;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private FirebaseAuth mAuth;

    int logout=0;

    MediaPlayer submitmedia;

    boolean doubleBackToExitPressedOnce = false;

    static SharedPreferences userdetails,admindetails;

    boolean verificationdone = false,alarmraised=false;

    Button Bsign_in,Badminsign_in, Bresend, Bok;
    TextView Tsign_up, forgot;
    EditText Euname, Epaswd, Ephone, EOtp;

    FirebaseDatabase database;
    DatabaseReference users,defaultimages,admin;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_login);

        if (!alarmraised)
        registerAlarm();

        submitmedia = MediaPlayer.create(this,R.raw.fordback);

        View progress_loop = (ProgressBar) findViewById(R.id.pbar);
        progress_loop.bringToFront();

        findViewById(R.id.pbar).setVisibility(View.INVISIBLE);
        mAuth = FirebaseAuth.getInstance();

        Bsign_in = (Button) findViewById(R.id.SignInbt);
        Badminsign_in = (Button) findViewById(R.id.AdminSignInbt);

        Tsign_up = (TextView) findViewById(R.id.signuptv);
        forgot = (TextView) findViewById(R.id.forgottv);

        Euname = (EditText) findViewById(R.id.usernameet);
        Epaswd = (EditText) findViewById(R.id.passwordet);
        Ephone = (EditText) findViewById(R.id.phoneet);

        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");
        admin = database.getReference("Admins");
        defaultimages = database.getReference("DefaultImages");

        userdetails = getSharedPreferences("Users_info", Context.MODE_PRIVATE);
        admindetails = getSharedPreferences("Admins_info",Context.MODE_PRIVATE);

        Bundle extra=getIntent().getExtras();
        if(extra != null){
            logout = extra.getInt("Logout");
           if (logout==1) {
               setsharedpreference(Uname,"",userdetails);
               setsharedpreference(Pword,"",userdetails);
               Euname.setText(getsharedpreference(Uname,userdetails));
               Epaswd.setText(getsharedpreference(Pword,userdetails));
           }
           if (logout==2){
               setsharedpreference(Uname,"",admindetails);
               setsharedpreference(Pword,"",admindetails);
               Euname.setText(getsharedpreference(Uname,admindetails));
               Epaswd.setText(getsharedpreference(Pword,admindetails));
           }
        }
        else if(getsharedpreference(Uname,userdetails)!=null && getsharedpreference(Pword,userdetails)!=null){

            Euname.setText(getsharedpreference(Uname,userdetails));
            Epaswd.setText(getsharedpreference(Pword,userdetails));
            signIn(getsharedpreference(Pword,userdetails),getsharedpreference(Uname,userdetails));
        }
        else if(getsharedpreference(Uname,admindetails)!=null && getsharedpreference(Pword,admindetails)!=null){

            Euname.setText(getsharedpreference(Uname,admindetails));
            Epaswd.setText(getsharedpreference(Pword,admindetails));

            adminsignin(getsharedpreference(Uname,admindetails),getsharedpreference(Pword,admindetails));
        }

        Bsign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Login.getsharedpreference(Euname.getText().toString(),Login.userdetails)=="true"){
                    submitmedia.start();
                }
                if (!Euname.getText().toString().isEmpty()||!Epaswd.getText().toString().isEmpty()) {
                    setsharedpreference(Uname, Euname.getText().toString(), userdetails);
                    setsharedpreference(Pword, Epaswd.getText().toString(), userdetails);
                }

                findViewById(R.id.pbar).setVisibility(View.VISIBLE);
                signIn(Epaswd.getText().toString(), Euname.getText().toString());
            }
        });

        Badminsign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Login.getsharedpreference(Euname.getText().toString(),admindetails)=="true")
                    submitmedia.start();
                if (!Euname.getText().toString().isEmpty()||!Epaswd.getText().toString().isEmpty()) {
                    setsharedpreference(Uname, Euname.getText().toString(), admindetails);
                    setsharedpreference(Pword, Epaswd.getText().toString(), admindetails);
                }

                findViewById(R.id.pbar).setVisibility(View.VISIBLE);
                adminsignin(Euname.getText().toString(),Epaswd.getText().toString());
            }
        });

    }

    public void adminsignin(final String username, final String password){
        admin.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!username.isEmpty()){
                    if (dataSnapshot.child(username).exists()){
                        final Admin newadmin = dataSnapshot.child(username).getValue(Admin.class);
                        if (newadmin.getPassword().equals(password)) {
                            Intent intent = new Intent(Login.this, AdminView.class);
                            Common.currentadmin = newadmin;
                            startActivity(intent);
                        }
                        else {
                            findViewById(R.id.pbar).setVisibility(View.INVISIBLE);
                            Toast.makeText(Login.this, "Username or password is wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Euname.setText("");
                        Epaswd.setText("");
                        findViewById(R.id.pbar).setVisibility(View.INVISIBLE);
                        Toast.makeText(Login.this, "You are not an admin", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    findViewById(R.id.pbar).setVisibility(View.INVISIBLE);
                    Toast.makeText(Login.this, "Please enter your username", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void onsignupTap(View v) {
        if (Ephone.getText().toString().length() != 10) {
            Toast.makeText(Login.this, "Phone", Toast.LENGTH_LONG).show();
        }
        if (Euname.getText().toString().contains(".")||Euname.getText().toString().contains("#")||Euname.getText().toString().contains("$")||Euname.getText().toString().contains("[")||Euname.getText().toString().contains("]")){
            Toast.makeText(Login.this,"username must not contain '.', '#', '$', '[', or ']'",Toast.LENGTH_LONG).show();
        }
        else if(Euname.getText().toString().length()>=1&&Epaswd.getText().toString().length()>=6){
            findViewById(R.id.pbar).setVisibility(View.VISIBLE);
            final User user = new User(Epaswd.getText().toString(), Euname.getText().toString(),"0","0","0","","", Ephone.getText().toString());
            users.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.child(user.getUsername()).exists()) {
                        findViewById(R.id.pbar).setVisibility(View.INVISIBLE);
                        Toast.makeText(Login.this, "username taken", Toast.LENGTH_LONG).show();
                    } else {
                        startPhoneNumberVerification("+91"+Ephone.getText().toString());
                        findViewById(R.id.pbar).setVisibility(View.INVISIBLE);
                        showPopup();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else {
            Toast.makeText(Login.this,"password should be at least 6 characters long",Toast.LENGTH_LONG).show();
        }
    }

    public void onforgotTap(View v) {

        Intent intent = new Intent(Login.this,Password_change.class);
        startActivity(intent);

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

    private void signIn(final String password, final String username) {
        logout=0;
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!username.isEmpty()) {
                    if (dataSnapshot.child(username).exists()) {
                        User login = dataSnapshot.child(username).getValue(User.class);
                        if (login.getPassword().equals(password)) {
                            Intent intent = new Intent(Login.this, Catalog.class);
                            Common.currentuser =login;
                            startActivity(intent);
                        } else {
                            findViewById(R.id.pbar).setVisibility(View.INVISIBLE);
                            Toast.makeText(Login.this, "Username or password is wrong", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        findViewById(R.id.pbar).setVisibility(View.INVISIBLE);
                        Toast.makeText(Login.this, "Have'nt you Signed up yet!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    findViewById(R.id.pbar).setVisibility(View.INVISIBLE);
                    Toast.makeText(Login.this, "Please enter your username", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

    public void verifyCode() {
        String code = EOtp.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        verificationdone =true;
        signInWithPhoneAuthCredential(credential);

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

    private void registerAlarm(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,8);
        calendar.set(Calendar.MINUTE,0 );
        calendar.set(Calendar.SECOND,0);

        Intent intent = new Intent(Login.this,AlarmReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(Login.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager)this.getSystemService(this.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
        alarmraised=true;
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
                Toast.makeText(Login.this,"OTP has been resend",Toast.LENGTH_LONG).show();
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

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");

                            defaultimages.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    backImage = dataSnapshot.child("back_image").getValue().toString();
                                    profile_image = dataSnapshot.child("profile_image").getValue().toString();
                                    final User user = new User(Epaswd.getText().toString(), Euname.getText().toString(),"0","0","0",backImage , profile_image, Ephone.getText().toString());
                                    users.child(user.getUsername()).setValue(user);
                                    Toast.makeText(Login.this, " OTP Verification done", Toast.LENGTH_LONG).show();
                                    Common.currentuser =user;
                                    startActivity(new Intent(Login.this, Catalog.class));
                                    finish();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        } else {

                            Toast.makeText(Login.this, "OTP incorrect", Toast.LENGTH_LONG).show();
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    public static String getsharedpreference(String key,SharedPreferences sharedPreferences){

        return sharedPreferences.getString(key,null);
    }

    public static void setsharedpreference(String key, String value,SharedPreferences sharedPreferences){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

}