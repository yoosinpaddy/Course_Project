package com.mohamed_mosabeh.course_project;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static com.mohamed_mosabeh.course_project.MenuActivity.PRIMARY_ID;
import static com.mohamed_mosabeh.course_project.MenuActivity.TICKET_PERSON_NAME;
import static com.mohamed_mosabeh.course_project.MenuActivity.USERNAMEX;

public class AccountActivity extends AppCompatActivity {
    
    private TextView IDTVdetails, fnameTV, unameTV;
    private EditText etchangeFullname;
    private boolean hiddenET = true;
    private boolean hiddenET2 = true;
    private TextView swatcher, swatcher2, passLocker;
    
    private EditText etchangePass1, etchangePass2, etchangePass3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        
        initiateViews();
        addListeners();
    }
    
    private void addListeners() {
        etchangeFullname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        
            }
    
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
        
            }
    
            @Override
            public void afterTextChanged(Editable s) {
        
            }
        });
    }
    
    private void initiateViews() {
        IDTVdetails = findViewById(R.id.idAccountTV);
        IDTVdetails.setText("ID: #" + PRIMARY_ID);
        
        swatcher = findViewById(R.id.btnFNDET);
        fnameTV = findViewById(R.id.fullnameDetails);
        passLocker = findViewById(R.id.passDetailsToYou);
        unameTV = findViewById(R.id.usernameDetails);
        fnameTV.setText("Full Name: " + TICKET_PERSON_NAME);
        unameTV.setText("Username: " + USERNAMEX);
        etchangeFullname = findViewById(R.id.etchangeFullname);
        etchangeFullname.setVisibility(View.GONE);
    
        swatcher2 = findViewById(R.id.btnFNDET2);
        
        etchangePass1 = findViewById(R.id.etchangePass);
        etchangePass1.setVisibility(View.GONE);
        
        etchangePass2 = findViewById(R.id.etchangePass2);
        etchangePass2.setVisibility(View.GONE);
    
        etchangePass3 = findViewById(R.id.etchangePass3);
        etchangePass3.setVisibility(View.GONE);
    }
    
    public void mootET() {
        etchangePass1.setText("");
        etchangePass2.setText("");
        etchangePass3.setText("");
    }
    public void changePASSWORD(View view) {
        if (hiddenET2) {
            etchangePass1.setVisibility(View.VISIBLE);
            etchangePass2.setVisibility(View.VISIBLE);
            etchangePass3.setVisibility(View.VISIBLE);
            passLocker.setVisibility(View.GONE);
            swatcher2.setText("Save");
            mootET();
            hiddenET2 = !hiddenET2;
        } else {
            String a = etchangePass1.getText().toString();
            String b = etchangePass2.getText().toString();
            String c = etchangePass3.getText().toString();
            if (!a.equals("") && !b.equals("") && !c.equals("")) {
                try {
                    if (!b.equals(c)) {
                        Toast.makeText(this, "Password mismatch", Toast.LENGTH_SHORT).show();
                        mootET();
                    } else {
                        if (checkUser(USERNAMEX, a)) {
                            updateDB2(b);
                            etchangePass1.setVisibility(View.GONE);
                            etchangePass2.setVisibility(View.GONE);
                            etchangePass3.setVisibility(View.GONE);
                            hiddenET2 = !hiddenET2;
                            passLocker.setVisibility(View.VISIBLE);
                            passLocker.setText("Password:");
                            swatcher2.setText("Change");
                            Toast.makeText(this, "Saved changes.", Toast.LENGTH_SHORT).show();
                            mootET();
                        } else {
                            mootET();
                            Toast.makeText(this, "Incorrect Old Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Error Occurred.", Toast.LENGTH_SHORT).show();
                }
            } else {
                mootET();
                etchangePass1.setVisibility(View.GONE);
                etchangePass2.setVisibility(View.GONE);
                hiddenET2 = !hiddenET2;
                etchangePass3.setVisibility(View.GONE);
                passLocker.setVisibility(View.VISIBLE);
                passLocker.setText("Password:");
                swatcher2.setText("Change");
            }
        }
    }
    
    public boolean checkUser (String username,String password)
    {
        SQLiteDatabase db = openOrCreateDatabase("dbMain", MODE_PRIVATE, null);
        String selection = "username = ?" + " and " +"password = ?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query("users",new String[]{"ID"},selection,selectionArgs,null,null,null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        if(count>0)
            return  true;
        else
            return  false;
    }
    
    public void changeFULLNAME(View view) {
        if (hiddenET) {
            etchangeFullname.setVisibility(View.VISIBLE);
            fnameTV.setVisibility(View.GONE);
            swatcher.setText("Save");
            etchangeFullname.setText(TICKET_PERSON_NAME);
            hiddenET = !hiddenET;
        } else {
            if (!etchangeFullname.getText().toString().trim().equals("")) {
                try {
                    String name = etchangeFullname.getText().toString().trim();
                    etchangeFullname.setVisibility(View.GONE);
                    fnameTV.setVisibility(View.VISIBLE);
                    swatcher.setText("Change");
                    updateDB(name);
                    hiddenET = !hiddenET;
                    TICKET_PERSON_NAME = name;
                    fnameTV.setText("Full Name: " + TICKET_PERSON_NAME);
                    Toast.makeText(this, "Saved changes.", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(this, "Error Occurred.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Cannot leave this empty", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    private void updateDB(String name) {
        SQLiteDatabase db = openOrCreateDatabase("dbMain", MODE_PRIVATE, null);
        db.execSQL("UPDATE users SET fullname = '"+name+"' WHERE ID = '"+ PRIMARY_ID +"'");
    }
    
    private void updateDB2(String pass) {
        SQLiteDatabase db = openOrCreateDatabase("dbMain", MODE_PRIVATE, null);
        db.execSQL("UPDATE users SET password = '"+pass+"' WHERE ID = '"+ PRIMARY_ID +"'");
    }
}