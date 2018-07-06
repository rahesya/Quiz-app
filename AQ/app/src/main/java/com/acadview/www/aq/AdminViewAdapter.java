package com.acadview.www.aq;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AdminViewAdapter extends RecyclerView.Adapter<AdminViewAdapter.AdminViewHolder>{

    List<User> StudentList;

    AdminViewAdapter(List <User> List){
        this.StudentList = List;
    }

    @NonNull
    @Override
    public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adminviewlayout, null);
        AdminViewHolder vh = new AdminViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull AdminViewHolder holder, int position) {
        holder.studentname.setText(StudentList.get(position).getUsername());
        holder.studentscore.setText(StudentList.get(position).getTotalScore());
    }

    @Override
    public int getItemCount() {
        return StudentList.size();
    }


    public class AdminViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        FirebaseDatabase database;
        DatabaseReference users;
        public TextView studentname,studentscore;

        public AdminViewHolder(View itemView) {
            super(itemView);
            studentname = (TextView)itemView.findViewById(R.id.student_name);
            studentscore = (TextView)itemView.findViewById(R.id.student_Score);
            studentname.setOnClickListener(this);
            studentscore.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId()==studentname.getId()){
                database = FirebaseDatabase.getInstance();
                users = database.getReference("Users");
                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(studentname.getText().toString()).exists()){
                            User student = dataSnapshot.child(studentname.getText().toString()).getValue(User.class);
                            Common.currentuser = student;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Intent intent = new Intent(v.getContext(),MyProfile.class);
                v.getContext().startActivity(intent);
            }
        }
    }

}
