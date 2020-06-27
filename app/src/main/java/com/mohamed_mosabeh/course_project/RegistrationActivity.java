package com.mohamed_mosabeh.course_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mohamed_mosabeh.course_project.dao.DatabaseHelper;

import static com.mohamed_mosabeh.course_project.dao.DatabaseHelper.PRIM_ID;

public class RegistrationActivity extends AppCompatActivity {
    
    DatabaseHelper db;
    EditText userName, userPassword, userPasswrd2, etfullname1;
    
    Button regButton, userLogin;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        
        //FOR THA DATA
        db = new DatabaseHelper(this);
        
        userName = findViewById(R.id.etUserName);
        userPassword = findViewById(R.id.etUserpassword1);
        userPasswrd2 = findViewById(R.id.etUserPassword2);
        regButton = findViewById(R.id.btnRigester);
        userLogin = findViewById(R.id.tvUserLogin);
        etfullname1 = findViewById(R.id.etfullname);
        
        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                finish();
            }
        });
        //for the buttom cheik
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
    
                boolean[] errors = {validateET(userName), validateET(userPassword), validateET(userPasswrd2), validateET(etfullname1)};
                
                if (errors[0] && errors[1] && errors[2] && errors[3]) {
                    String user = userName.getText().toString().trim();
                    String pwd = userPassword.getText().toString().trim();
                    String pwd2 = userPasswrd2.getText().toString().trim();
                    String fullN = etfullname1.getText().toString().trim();
                    if (pwd.equals(pwd2)) {
                        long val = db.addUser(user, pwd, fullN);
        
                        if (val > 0) {
                            Toast.makeText(RegistrationActivity.this, "Account Registered Successfully", Toast.LENGTH_SHORT).show();
                            Intent moveToLogin = new Intent(RegistrationActivity.this, MenuActivity.class);
                            moveToLogin.putExtra("fullname", fullN);
                            moveToLogin.putExtra("username", user);
                            moveToLogin.putExtra("primid", PRIM_ID);
                            startActivity(moveToLogin);
                            finish();
                        } else {
                            //for cheak the user if ti alerady reguster
            
                            Toast.makeText(RegistrationActivity.this, "User already exist in System", Toast.LENGTH_SHORT).show();
            
                        }
                    } else {
                        //for the password if not sam
                        Toast.makeText(RegistrationActivity.this, "Password doesn't Match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    
    private boolean validateET(EditText editText) {
        if (editText.getText().toString().trim().equals("")) {
            editText.setError("Required Field");
            return false;
        } else {
            return true;
        }
    }
}
