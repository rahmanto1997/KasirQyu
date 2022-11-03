package com.otnam.kasirqiu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    TextView log;
    EditText fullname, email, password;
    Button button_register;
    boolean passwordVisible;
    FirebaseAuth auth;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        auth=FirebaseAuth.getInstance();
        loadingBar= new ProgressDialog(this);

        fullname=findViewById(R.id.field_fullname);
        email=findViewById(R.id.field_email);
        password=findViewById(R.id.field_password);
        log = (TextView) findViewById(R.id.existing_login);
        button_register=findViewById(R.id.btn_register);

        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sFullname=fullname.getText().toString();
                String sEmail=email.getText().toString();
                String sPassword=password.getText().toString();

                registerMethod(sEmail,sPassword);
            }

            private void registerMethod(String sEmail, String sPassword) {
                if (TextUtils.isEmpty(sEmail)){
                    email.setError("Email is required");
                }
                if (TextUtils.isEmpty(sPassword)){
                     password.setError("Password  is required");
                }
                else {
                     loadingBar.setTitle("Register is in Progress");
                     loadingBar.setMessage("Please Wait While the Process is Continue");
                     loadingBar.show();

                     auth.createUserWithEmailAndPassword(sEmail,sPassword)
                             .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                 @Override
                                 public void onComplete(@NonNull Task<AuthResult> task) {
                                     if (task.isSuccessful()){
                                         Toast.makeText(getApplicationContext(), "Register Successfull", Toast.LENGTH_SHORT).show();
                                         loadingBar.dismiss();
                                     }
                                     else {
                                         Toast.makeText(getApplicationContext(), "Register Failed", Toast.LENGTH_SHORT).show();
                                         loadingBar.dismiss();
                                     }
                                 }

                             });
                }
            }
        });

        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int Right=2;
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(event.getRawX()>=password.getRight()-password.getCompoundDrawables()[Right].getBounds().width()){
                        int selection=password.getSelectionEnd();
                        if(passwordVisible){
                            //set drawable image here
                            password.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_baseline_visibility_off_24, 0);
                            //for hide password
                            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordVisible=false;
                        }else{
                            //set drawable image here
                            password.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_baseline_visibility_24, 0);
                            //for show  password
                            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordVisible=true;
                        }
                        password.setSelection(selection);
                    }
                }
                return false;
            }
        });

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        });
    }
}