package com.mohamed_mosabeh.course_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mohamed_mosabeh.course_project.objects.CarFine;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mohamed_mosabeh.course_project.MenuActivity.USERNAMEX;

public class FinesListActivity extends AppCompatActivity {
    
    private String CURRENT_USER = "Mohamed";
    
    /** TextView */
    private TextView headerUserTextView;
    
    /** EditText */
    private EditText searchFinesEditText;
    private String searchEditTextValue;
    
    /** Dropdown box variables */
    private Spinner searchCategorySpinner;
    
    /** List and Adapters */
    private ListView finesList;
    private List<Map<String, String>> fineData = new ArrayList<Map<String, String>>();
    private Map<String, String> fineDataInner;
    private SimpleAdapter adapter;
    
    /** Fines Array */
    private ArrayList<CarFine> carFineArrayList = new ArrayList<CarFine>();
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fines_list);
        
        Intent intent = getIntent();
        CURRENT_USER = USERNAMEX;
        /** Assigns XML Views to Java Variables */
        initializeViews();
        /** Assigns Listeners to Views */
        initializeListeners();
        
    }
    
    private void initializeViews() {
        headerUserTextView = findViewById(R.id.upmostTextView);
        headerUserTextView.setText("Logged in as " + USERNAMEX);
    
        searchFinesEditText = findViewById(R.id.searchFinesEditText);
        
        searchCategorySpinner = findViewById(R.id.searchCategory);
        finesList = findViewById(R.id.finesList);
        fineData.clear();
        carFineArrayList.clear();
        
        adapter = new SimpleAdapter(this, fineData, android.R.layout.simple_list_item_2, new String[]{"upperText","bottomText"}, new int[]{android.R.id.text1, android.R.id.text2});
        finesList.setAdapter(adapter);
    }
    
    private void initializeListeners() {
        searchCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    switch (searchCategorySpinner.getSelectedItem().toString()) {
                        case "Recent":
                            searchFinesEditText.setHint("Most Recent Fines: ");
                            searchFinesEditText.getBackground().setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN);
                            searchFinesEditText.setText("");
                            searchFinesEditText.setEnabled(false);
    
    
                            fineData.clear();
                            carFineArrayList.clear();
    
                            getAllCarFines(searchCategorySpinner.getSelectedItem().toString().toLowerCase());
                            /** Reads & Displays fines from the Database */
                            readCarFinesData();
                            adapter.notifyDataSetChanged();
                            
                            break;
                        case "Amount":
                            
                            
                            searchFinesEditText.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
                            searchFinesEditText.setVisibility(View.VISIBLE);
                            searchFinesEditText.setEnabled(true);
                            searchFinesEditText.setHint("Search Fines Lower Than..");
                            searchFinesEditText.setText("");
                            
                            fineData.clear();
                            carFineArrayList.clear();
    
                            getAllCarFines(searchCategorySpinner.getSelectedItem().toString().toLowerCase());
                            /** Reads & Displays fines from the Database */
                            /* TODO: readCarFinesData(); */
                            adapter.notifyDataSetChanged();
    
    
                            break;
                    }
                } catch (Exception e) { }
            }
        
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            
            }
        });
        
        searchFinesEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
    
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
    
                searchEditTextValue = s.toString();
                try {
                    if (!searchEditTextValue.trim().equals("")) {
                        switch (searchCategorySpinner.getSelectedItem().toString()) {
                            case "Recent":
                                break;
                            case "Amount":
                                searchFinesEditText.setHint("Search Fines Lower Than..");
    
                                fineData.clear();
                                carFineArrayList.clear();
    
                                getAllCarFines(searchCategorySpinner.getSelectedItem().toString().toLowerCase(), Integer.valueOf(searchEditTextValue));
                                /** Reads & Displays fines from the Database */
                                readCarFinesData();
                                adapter.notifyDataSetChanged();
                                break;
                        }
                    } else {
                    
                    }
                } catch (Exception e) {
                
                }
            }
    
            @Override
            public void afterTextChanged(Editable s) {
        
            }
        });
        
        finesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), FineDetailsActivity.class);
                intent.putExtra("fineCode", carFineArrayList.get(position).getFineCode());
                startActivity(intent);
            }
        });
    }
    
    private void getAllCarFines(String Sorting) {
        SQLiteDatabase db = openOrCreateDatabase("dbMain", MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT * FROM CarFines WHERE ownerName = '" + CURRENT_USER + "' ORDER BY date DESC", null, null);
        
        while (cursor.moveToNext()) {
            CarFine fine = new CarFine();
            fine.setFineCode(cursor.getInt(0));
            fine.setIssueDate(cursor.getString(1));
            fine.setFineAmount(cursor.getFloat(2));
            fine.setOwnerId(cursor.getInt(3));
            fine.setOwnerName(cursor.getString(4));
            fine.setCarType(cursor.getString(5));
            fine.setViolation(cursor.getString(6));
            fine.setDescription(cursor.getString(7));
            carFineArrayList.add(fine);
        }
        cursor.close();
    }
    
    private void getAllCarFines(String Sorting, int amount) throws ParseException {
        SQLiteDatabase db = openOrCreateDatabase("dbMain", MODE_PRIVATE, null);
        Cursor cursor = null;
        
        if (Sorting.equals("amount")) {
            cursor = db.rawQuery("SELECT * FROM CarFines WHERE ownerName = '" + CURRENT_USER + "' AND amount < " + amount + " ORDER BY " + Sorting + " DESC", null, null);
        } else {
            cursor = db.rawQuery("SELECT * FROM CarFines WHERE ownerName = '" + CURRENT_USER + "' ORDER BY date DESC", null, null);
        }
        
        while (cursor.moveToNext()) {
            CarFine fine = new CarFine();
            fine.setFineCode(cursor.getInt(0));
            fine.setIssueDate(cursor.getString(1));
            fine.setFineAmount(cursor.getFloat(2));
            fine.setOwnerId(cursor.getInt(3));
            fine.setOwnerName(cursor.getString(4));
            fine.setCarType(cursor.getString(5));
            fine.setViolation(cursor.getString(6));
            fine.setDescription(cursor.getString(7));
            carFineArrayList.add(fine);
        }
        cursor.close();
    }
    
    private void readCarFinesData() {
        for (CarFine fine : carFineArrayList) {
            fineDataInner = new HashMap<String, String>();
            fineDataInner.put("upperText", "Date: " + fine.getIssueDate());
            fineDataInner.put("bottomText", fine.getViolation() + " $" + fine.getFineAmount());
            fineData.add(fineDataInner);
        }
    }
}
