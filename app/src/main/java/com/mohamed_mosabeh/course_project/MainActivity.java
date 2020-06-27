package com.mohamed_mosabeh.course_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mohamed_mosabeh.course_project.dao.DatabaseHelper;

import static com.mohamed_mosabeh.course_project.dao.DatabaseHelper.FULLNAMEDB;
import static com.mohamed_mosabeh.course_project.dao.DatabaseHelper.PRIM_ID;

public class MainActivity extends AppCompatActivity {
    
    private EditText name, password;
    private TextView info;
    private Button Login, Rigisterd;
    ;
    private DatabaseHelper db;
    private int counter = 0;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        db = new DatabaseHelper(this);
        name = findViewById(R.id.etname);
        password = findViewById(R.id.etpsword);
        info = findViewById(R.id.tvifo);
        Login = findViewById(R.id.btnLogin);
        
        Rigisterd = findViewById(R.id.btRegister);
        
        info.setText("Number of Login Incorrect Attempts: " + counter);
        
        Rigisterd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(intent);
                finish();
                
            }
        });
        
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                boolean[] errors = {validateET(name), validateET(password)};
                
                if (errors[0] && errors[1]) {
                    String user = name.getText().toString().trim();
                    String pwd = password.getText().toString().trim();
                    boolean res = db.checkUser(user, pwd);
                    if (res == true) {
                        Intent HomePage = new Intent(MainActivity.this, MenuActivity.class);
                        HomePage.putExtra("fullname", FULLNAMEDB);
                        HomePage.putExtra("username", name.getText().toString().trim());
                        HomePage.putExtra("primid", PRIM_ID);
                        startActivity(HomePage);
                        FULLNAMEDB = "";
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "Incorrect Credentials", Toast.LENGTH_SHORT).show();
                        counter++;
                        info.setText("Number of Login Incorrect Attempts: " + counter);
                        if (counter > 5) {
                            info.setText("Exceeded Allowed Login Attempts: " + counter);
                            info.setTextColor(Color.RED);
                            Toast.makeText(getApplicationContext(), "Exceeded Allowed Login Attempts", Toast.LENGTH_LONG).show();
                            finish();
                        }
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