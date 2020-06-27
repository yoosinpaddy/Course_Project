package com.mohamed_mosabeh.course_project.dao;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.mohamed_mosabeh.course_project.MenuActivity;

public class DatabaseHelper extends SQLiteOpenHelper {
    
    public static final String DATABASE_NAME="dbMain";
    public static final String TABLE_NAME="users";
    public static final String COL_1="ID";
    public static final String COL_2= "username";
    public static final String COL_3= "password";
    public static final String COL_4= "fullname";
    public static String FULLNAMEDB = "";
    public static int PRIM_ID;
    
    
    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME,null, 1);
    }
    
    @Override
    public void onCreate(SQLiteDatabase SQLiteDatabase) {
        SQLiteDatabase.execSQL("CREATE TABLE users (ID INTEGER PRIMARY KEY AUTOINCREMENT,username TEXT UNIQUE,password TEXT,fullname TEXT)");
        
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase SQLiteDatabase, int i, int i1) {
        
        SQLiteDatabase.execSQL(" DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(SQLiteDatabase);
    }
    
    //add new password
    
    public long addUser(String user , String password,String fullname) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username",user);
        contentValues.put("password",password);
        contentValues.put("fullname",fullname);
        
        Long res= db.insert(TABLE_NAME,null,contentValues);
        
        boolean test = checkUser(user, password);
        db.close();
        return res;
        
    }
    
    //chek the user if it register
    
    public boolean checkUser (String username,String password)
    {
        String[] columns = { COL_1 };
        SQLiteDatabase db = getReadableDatabase();
        String selection = COL_2 + "=?" + " and " + COL_3 + "=?";
        String[] selectionArgs = { username, password};
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE " + COL_2 + " = '"+username + "' AND " + COL_3 + " = '" +password+ "'", null);
        while (cursor.moveToNext()) {
            FULLNAMEDB = cursor.getString(3);
            PRIM_ID = cursor.getInt(0);
        }
        cursor.close();
        cursor = db.query(TABLE_NAME,columns,selection,selectionArgs,null,null,null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        if(count>0)
            return  true;
        else
            return  false;
    }
}
