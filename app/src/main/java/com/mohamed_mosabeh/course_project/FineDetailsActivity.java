package com.mohamed_mosabeh.course_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.mohamed_mosabeh.course_project.objects.CarFine;

import org.w3c.dom.Text;

import static com.mohamed_mosabeh.course_project.MenuActivity.TICKET_PERSON_NAME;
import static com.mohamed_mosabeh.course_project.MenuActivity.USERNAMEX;
import static com.mohamed_mosabeh.course_project.dao.DatabaseHelper.FULLNAMEDB;

public class FineDetailsActivity extends AppCompatActivity {
    
    private int CAR_FINE_CODE;
    private CarFine CAR_FINE_OBJECT = new CarFine();
    private String CURRENT_USER = "Mohamed";
    
    private TextView uppermostTextView, ticketCodeTextView, dateIssuedTextView, violatorTextView;
    private TextView carTypeTextView, violationTextView, fineAmountTextView, descriptionTextView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fine_details);
        
        Intent intent = getIntent();
        CAR_FINE_CODE = intent.getIntExtra("fineCode", 1);
        
        initializeViews();
        getFine();
        bindFineToTextViews();
    }
    
    private void initializeViews() {
        uppermostTextView = findViewById(R.id.uppermostTextView);
        ticketCodeTextView = findViewById(R.id.ticketCodeTextView);
        dateIssuedTextView = findViewById(R.id.dateIssuedTextView);
        violatorTextView = findViewById(R.id.violatorTextView);
        carTypeTextView = findViewById(R.id.carTypeTextView);
        violationTextView = findViewById(R.id.violationTextView);
        fineAmountTextView = findViewById(R.id.fineAmountTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
    }
    
    private void getFine() {
        SQLiteDatabase db = openOrCreateDatabase("dbMain", MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT * FROM CarFines WHERE id = " + CAR_FINE_CODE, null, null);
    
        while (cursor.moveToNext()) {
            CAR_FINE_OBJECT.setFineCode(cursor.getInt(0));
            CAR_FINE_OBJECT.setIssueDate(cursor.getString(1));
            CAR_FINE_OBJECT.setFineAmount(cursor.getFloat(2));
            CAR_FINE_OBJECT.setOwnerId(cursor.getInt(3));
            CAR_FINE_OBJECT.setOwnerName(cursor.getString(4));
            CAR_FINE_OBJECT.setCarType(cursor.getString(5));
            CAR_FINE_OBJECT.setViolation(cursor.getString(6));
            CAR_FINE_OBJECT.setDescription(cursor.getString(7));
            CAR_FINE_OBJECT.setLatLng(new Double[]{cursor.getDouble(8), cursor.getDouble(9)});
        }
        cursor.close();
    }
    
    private void bindFineToTextViews() {
        ticketCodeTextView.setText("Ticket Code: #" + CAR_FINE_OBJECT.getFineCode());
        String[] parts = CAR_FINE_OBJECT.getIssueDate().split(" ");
        dateIssuedTextView.setText("Date Issued: " + parts[0]);
        violatorTextView.setText("Individual Responsible: " + TICKET_PERSON_NAME);
        carTypeTextView.setText("Car Specifications: " + CAR_FINE_OBJECT.getCarType());
        violationTextView.setText("Violation: " + CAR_FINE_OBJECT.getViolation());
        fineAmountTextView.setText("Fine Amount: $" + CAR_FINE_OBJECT.getFineAmount());
        descriptionTextView.setText(CAR_FINE_OBJECT.getDescription());
        uppermostTextView.setText("Logged in as " + USERNAMEX + ":");
    }
}
