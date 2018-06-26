package com.acadview.www.aq;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.RecoverySystem;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.TreeMap;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfile extends AppCompatActivity implements View.OnClickListener{

    private ImageView backimage;
    private CircleImageView profileimage;
    TextView totalscore,correctattempts,totalattempts,user_name,java_score,python_score,php_score,c_score;

    private Uri filepath;
    private final int PICK_IMAGE_REQUEST = 71;
    private int id;

    StorageReference storageReference;
    DatabaseReference users,defaultimages,scoretbl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        storageReference = FirebaseStorage.getInstance().getReference();
        users = FirebaseDatabase.getInstance().getReference("Users");
        defaultimages = FirebaseDatabase.getInstance().getReference("DefaultImages");
        scoretbl = FirebaseDatabase.getInstance().getReference("Scores/"+Common.currentuser.getUsername());

        java_score=(TextView)findViewById(R.id.javascore);
        python_score=(TextView)findViewById(R.id.pythonscore);
        php_score=(TextView)findViewById(R.id.phpscore);
        c_score =(TextView)findViewById(R.id.cscore);

        backimage = (ImageView)findViewById(R.id.header_cover_image);
        profileimage=(CircleImageView) findViewById(R.id.user_profile_photo);
        totalattempts=(TextView)findViewById(R.id.questionsattempted);
        correctattempts=(TextView)findViewById(R.id.correctattempts);
        totalscore=(TextView)findViewById(R.id.totalscore);
        user_name =(TextView)findViewById(R.id.user_profile_name);

        backimage.setOnClickListener(this);
        profileimage.setOnClickListener(this);

        user_name.setText(Common.currentuser.getUsername());

        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String score=dataSnapshot.child(Common.currentuser.getUsername()).child("totalScore").getValue().toString();
                totalscore.setText(score);
                String tattempts=dataSnapshot.child(Common.currentuser.getUsername()).child("questionsAttempted").getValue().toString();
                totalattempts.setText(tattempts);
                String cattempts=dataSnapshot.child(Common.currentuser.getUsername()).child("correctAttempts").getValue().toString();
                correctattempts.setText(cattempts);

                Picasso.with(getBaseContext()).load(dataSnapshot.child(Common.currentuser.getUsername()).child("pathtobackimage").getValue().toString())
                        .into(backimage);

                Picasso.with(getBaseContext()).load(dataSnapshot.child(Common.currentuser.getUsername()).child("pathtoprofileimage").getValue().toString())
                        .into(profileimage);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        scoretbl.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Java").exists())
                java_score.setText(dataSnapshot.child("Java").child("score").getValue().toString());
                if(dataSnapshot.child("Python").exists())
                python_score.setText(dataSnapshot.child("Python").child("score").getValue().toString());
                if(dataSnapshot.child("PHP").exists())
                php_score.setText(dataSnapshot.child("PHP").child("score").getValue().toString());
                if(dataSnapshot.child("C").exists())
                c_score.setText(dataSnapshot.child("C").child("score").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST&&resultCode ==RESULT_OK
                && data !=null && data.getData()!= null){
            filepath = data.getData();
            if(id==R.id.header_cover_image)
                Picasso.with(this).load(filepath).into(backimage);
            else
                Picasso.with(this).load(filepath).into(profileimage);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.header_cover_image:{
                id = R.id.header_cover_image;
                chooseImage();
                uploadImageback();

                break;
            }
            case R.id.user_profile_photo:{
                id = R.id.user_profile_photo;
                chooseImage();
                uploadImageprofile();

                break;
            }
        }
    }

    private void uploadImageback() {

        final StorageReference backref = storageReference.child("images/").
                child(Common.currentuser.getUsername()+"/"+ Common.currentuser.getUsername()+"back");

        if(filepath!=null){

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading..");
            progressDialog.show();

            backref.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    backref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            users.child(Common.currentuser.getUsername()).child("pathtobackimage").setValue(uri.toString());
                            Toast.makeText(MyProfile.this,"Uploaded",Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MyProfile.this,"Not Uploaded",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(MyProfile.this,"Failure",Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded "+(int)progress+"%");
                }
            });
        }

    }

    private void uploadImageprofile() {

        if(filepath!=null){

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading.. ");
            progressDialog.show();

            final StorageReference profileref = storageReference.child("images/").
                    child(Common.currentuser.getUsername()+"/"+ Common.currentuser.getUsername()+"profile");
            profileref.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    profileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            users.child(Common.currentuser.getUsername()).child("pathtoprofileimage").setValue(uri.toString());
                            Toast.makeText(MyProfile.this,"Profile Uploaded",Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MyProfile.this,"Profile not Uploaded",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(MyProfile.this,"Failure",Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded "+(int)progress+" %");
                }
            });
        }

    }

}
