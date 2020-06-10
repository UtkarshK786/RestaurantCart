package com.example.eggnchicken;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class register extends AppCompatActivity {
    EditText email,password,phone;
    TextView login;
    Button register;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    users Users;
    FirebaseDatabase database;
    DatabaseReference myRef;
    long maxID=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        phone=findViewById(R.id.phone);
        login=findViewById(R.id.log);
        register=findViewById(R.id.Register);
        firebaseAuth=FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progressBar);
        database=FirebaseDatabase.getInstance();
        Users=new users();
        myRef = database.getReference("users");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                maxID=  dataSnapshot.getChildrenCount();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void log(View view){
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }
    public void Register(View view){
        final String eml=email.getText().toString().trim();
        final String pswrd=password.getText().toString().trim();
        final String phn=phone.getText().toString().trim();
        if(eml.isEmpty()){
            email.setError("Email required");
            email.requestFocus();
            return;
        }
        if(phn.isEmpty()){
            phone.setError("Phone required");
            phone.requestFocus();
            return;
        }
        else if(phn.length()>10|| Pattern.matches("[a-zA-Z]+", phn) == true ){
            phone.setError("Enter a valid number");
            phone.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(eml).matches()){
            email.setError("Enter a valid email");
            email.requestFocus();
            return;
        }
        else if(pswrd.isEmpty()){
            password.setError("Password required");
            password.requestFocus();
            return;
        }
        else if(pswrd.length()<6){
            password.setError("minimum length of password should be 6");
            password.requestFocus();
            return;
        }
        register.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(eml,pswrd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    Toast.makeText(register.this, "registration successful", Toast.LENGTH_SHORT).show();
                     Users.setPassword(pswrd);
                     Users.setEmail(eml);
                     Users.setItems(0);
                    Users.setItem1(0);
                    Users.setItem2(0);
                    Users.setItem3(0);
//                    Toast.makeText(Register.this, "String value of maxid+1 is"+maxID+1, Toast.LENGTH_SHORT).show();
                    myRef.child(String.valueOf(maxID+1)).setValue(Users);
                     Toast.makeText(register.this, "Please login", Toast.LENGTH_SHORT).show();
                     Intent intent=new Intent(register.this,MainActivity.class);
                    startActivity(intent);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //clears all the activities on top of it on the stock i.e. registeration and/or login
                    finish();
                }
                else{
                    register.setEnabled(true);
                    Toast.makeText(register.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
