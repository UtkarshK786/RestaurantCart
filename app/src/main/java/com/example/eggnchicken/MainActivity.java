package com.example.eggnchicken;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    EditText email,password;
    TextView register;
    Button login;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email=findViewById(R.id.editText3);
        password=findViewById(R.id.editText2);
        login=findViewById(R.id.login);
        register=findViewById(R.id.textView);
        firebaseAuth=FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progressBar2);
    }
    public void reg(View view){
        Intent i = new Intent(getApplicationContext(), register.class);
        startActivity(i);
    }
//    @Override
//    protected void onStart() {
//        super.onStart();
//        if(firebaseAuth.getCurrentUser()!=null){
//            startActivity(new Intent(this,Home.class));
//            finish();
//        }
//    }
    public void log(View view){
        String usrnm=email.getText().toString().trim();
        String pswrd=password.getText().toString().trim();

        if(usrnm.isEmpty()){
            email.setError("Email required");
            email.requestFocus();
            return;
        }
        if(pswrd.isEmpty()){
            password.setError("Password required");
            password.requestFocus();
            return;
        }
        login.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(usrnm,pswrd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Sign in successful", Toast.LENGTH_SHORT).show();

                    Intent intent=new Intent(getApplicationContext(),Home.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //clears all the activities on top of it on the stock i.e. registeration and/or login
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                   login.setEnabled(true);
                }
            }
        });
    }
}
