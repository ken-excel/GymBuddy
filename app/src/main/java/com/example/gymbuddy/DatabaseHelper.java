package com.example.gymbuddy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Exercise.db";

    //Exercise List
    public static final String TABLE_Ex_list = "exercise_list";
    public static final String Ex_col1 = "name";
    public static final String Ex_col2 = "type";
    public static final String Ex_col3 = "muscle";

    //Session (Log - bond to exercise list by exercise name)
    public static final String TABLE_Se = "session";
    public static final String Se_col1 = "name";
    public static final String Se_col2 = "date"; //in History page of each exercise, show by name, sort by date //In Main page show all exercises with specific date
    public static final String Se_col3 = "data1"; //rep (rep only) or rep(weight/rep) or time (time only)
    public static final String Se_col4 = "data2"; //NULL (rep only) weight(weight/rep) or NULL (time only)

    //Routine w/ Exercise List
    public static final String TABLE_Rou_ex_list = "routine_ex_list";
    public static final String Rou_ex_col1 = "name"; //routine_name
    public static final String Rou_ex_col2 = "exercise";

    //Routine List
    public static final String TABLE_Rou_list = "routine_list";
    public static final String Rou_col1 = "name"; //routine_name

    //Exercise list populated on each day
    public static final String TABLE_Day_list = "day_list";
    public static final String Day_col1 = "date";
    public static final String Day_col2 = "exercise";
    public static final String Day_col3 = "type";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_ex_list = "CREATE TABLE " + TABLE_Ex_list + "("
                + Ex_col1 + " TEXT PRIMARY KEY, "
                + Ex_col2 + " TEXT, "
                + Ex_col3 + " TEXT"
                + ")";
        String create_se = "CREATE TABLE " + TABLE_Se + "("
                + Se_col1 + " TEXT, "
                + Se_col2 + " TEXT, "
                + Se_col3 + " INTEGER, "
                + Se_col4 + " REAL"
                + ")";
        String create_rou_ex_list = "CREATE TABLE " + TABLE_Rou_ex_list + "("
                + Rou_ex_col1 + " TEXT, "
                + Rou_ex_col2 + " TEXT, "
                + "PRIMARY KEY(" + Rou_ex_col1 +"," + Rou_ex_col2 +")"
                + ")";
        String create_rou_list = "CREATE TABLE " + TABLE_Rou_list + "("
                + Rou_col1 + " TEXT PRIMARY KEY"
                + ")";
        String create_day_list = "CREATE TABLE " + TABLE_Day_list + "("
                + Day_col1 + " TEXT, "
                + Day_col2 + " TEXT, "
                + Day_col3 + " INTEGER, "
                + "PRIMARY KEY(" + Day_col1 +"," + Day_col2 +")"
                + ")";
        try {
            db.execSQL(create_ex_list);
        } catch (Exception e) {
            Log.d("ex_list_create",e.toString());
        }
        try {
            db.execSQL(create_se);
        } catch (Exception e) {
            Log.d("se_create",e.toString());
        }
        try {
            db.execSQL(create_rou_ex_list);
        } catch (Exception e) {
            Log.d("rou_ex_list_create",e.toString());
        }
        try {
            db.execSQL(create_rou_list);
        } catch (Exception e) {
            Log.d("rou_list_create",e.toString());
        }
        try {
            db.execSQL(create_day_list);
        } catch (Exception e) {
            Log.d("day_list_create",e.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String drop_ex_list = "DROP TABLE IF EXISTS "+TABLE_Ex_list;
        String drop_se = "DROP TABLE IF EXISTS "+TABLE_Se;
        String drop_rou_list = "DROP TABLE IF EXISTS "+TABLE_Rou_list;
        String drop_day_list = "DROP TABLE IF EXISTS "+TABLE_Day_list;
        try {
            Log.d("ex_list_up","OnUpgrade");
            db.execSQL(drop_ex_list);
            onCreate(db);
        }catch (Exception e) {
            Log.d("ex_list_up",e.toString());
        }
        try {
            Log.d("se_up","OnUpgrade");
            db.execSQL(drop_se);
            onCreate(db);
        }catch (Exception e) {
            Log.d("se_up",e.toString());
        }
        try {
            Log.d("rou_list_up","OnUpgrade");
            db.execSQL(drop_rou_list);
            onCreate(db);
        }catch (Exception e) {
            Log.d("rou_list_up",e.toString());
        }
        try {
            Log.d("day_list_up","OnUpgrade");
            db.execSQL(drop_day_list);
            onCreate(db);
        }catch (Exception e) {
            Log.d("day_list_up",e.toString());
        }
        onCreate(db);
    }

    //Exercise list / Add Exercise to library
    public boolean addExercise(String name, String type, String muscle){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Ex_col1,name);
        contentValues.put(Ex_col2,type);
        contentValues.put(Ex_col3,muscle);
        long result = db.insert(TABLE_Ex_list,null,contentValues);
        Log.d("addExercise",Long.toString(result));
        if(result <= 0)
            return false;
        else
            return true;
    }

    public ArrayList<String> getExerciseNames()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {Ex_col1,Ex_col2,Ex_col3};
        String orderBy = Ex_col3+", "+Ex_col2;
        Cursor cursor =db.query(TABLE_Ex_list,columns,null,null,null,null,orderBy);
        ArrayList<String> aList= new ArrayList<String>();
        while (cursor.moveToNext())
        {
            String name =cursor.getString(cursor.getColumnIndex(Ex_col1));
            Log.d("getExercise",name);
            aList.add(name);
        }
        return aList;
    }

    public HashMap<String,String> getExerciseType()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {Ex_col1,Ex_col2,Ex_col3};
        String orderBy = Ex_col3+", "+Ex_col2;
        Cursor cursor =db.query(TABLE_Ex_list,columns,null,null,null,null,orderBy);
        HashMap<String,String> aList= new HashMap<String,String>();
        while (cursor.moveToNext())
        {
            String name =cursor.getString(cursor.getColumnIndex(Ex_col1));
            String type =cursor.getString(cursor.getColumnIndex(Ex_col2));
            aList.put(name,type);
        }
        return aList;
    }

    public boolean clearExercise(){
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_Ex_list, null, null);
        Log.d("clearExercise",Long.toString(result));
        if(result <= 0)
            return false;
        else
            return true;
    }

    public void deleteExercise(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = Ex_col1+" =?";
        String[] whereArgs = new String[] { name };
        db.delete(TABLE_Ex_list, whereClause, whereArgs);
    }

    //Add Session
    public boolean addSession(String name, String date, Integer data1, Double data2){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Se_col1,name); //exercise name
        contentValues.put(Se_col2,date); //session date
        contentValues.put(Se_col3,data1);
        contentValues.put(Se_col4,data2);
        long result = db.insert(TABLE_Se,null,contentValues);
        Log.d("addSession_to_a_day",Long.toString(result));
        if(result <= 0)
            return false;
        else
            return true;
    }

    //Add exercise to daily page
    public boolean addExercise_Day(String date, String name, String type){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Day_col1,date);
        contentValues.put(Day_col2,name);
        contentValues.put(Day_col3,type);
        long result = db.insert(TABLE_Day_list,null,contentValues);
        Log.d("addExercise_to_a_day",Long.toString(result));
        if(result <= 0)
            return false;
        else
            return true;
    }

    //Delete exercise in daily page
    public void deleteExercise_Day(String date, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = Day_col1+" =?"+ " AND " + Day_col2 + "=?";
        String[] whereArgs = new String[] {date, name};
        db.delete(TABLE_Day_list, whereClause, whereArgs);
    }

    public List<String> getExercise_Day(String date)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {Day_col1,Day_col2,Day_col3};
        String selection = Day_col1+" =?";
        String[] selectionArgs = {date};
        Cursor cursor =db.query(TABLE_Day_list,columns,selection,selectionArgs,null,null,null);
        ArrayList<String> aList= new ArrayList<String>();
        while (cursor.moveToNext())
        {
            String name =cursor.getString(cursor.getColumnIndex(Day_col2));
            Log.d("getExercise",name);
            aList.add(name);
        }
        return aList;
    }


    public HashMap<String, List<Session>> getSession_Day(String date)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {Day_col1,Day_col2,Day_col3};
        String selection = Day_col1+" =?";
        String[] selectionArgs = {date};
        Cursor cursor =db.query(TABLE_Day_list,columns,selection,selectionArgs,null,null,null);
        HashMap<String, List<Session>> dList= new HashMap<String, List<Session>>();
        while (cursor.moveToNext())
        {
            String temp_exercise =cursor.getString(cursor.getColumnIndex(Day_col2)); //get each exercise
            //String temp_type = cursor.getString(cursor.getColumnIndex(Day_col3)); //get each exercise type
            List<Session> temp_session = new ArrayList<Session>();
            String[] col = {Se_col1,Se_col2,Se_col3,Se_col4};
            String selection_Se = Se_col1+" =?"+ " AND " + Se_col2 + "=?";
            String[] selectionArgs_Se = {temp_exercise, date};
            Cursor cur =db.query(TABLE_Se,col,selection_Se,selectionArgs_Se,null,null,null);
            while(cur.moveToNext()){
                Session Se = new Session();
                Se.data1 = cur.getInt(cur.getColumnIndex(Se_col3));
                Se.data2 = cur.getDouble(cur.getColumnIndex(Se_col4));
                temp_session.add(Se);
            }
            Session Se = new Session();
            temp_session.add(Se); // add empty session to the end
            dList.put(temp_exercise,temp_session);
        }
        return dList;
    }

    //Routine List
    //Get Routine&Exercise
    public HashMap<String, List<String>> getRoutine()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {Rou_col1};
        Cursor cursor =db.query(TABLE_Rou_list,columns,null,null,null,null,null);
        HashMap<String, List<String>> rList= new HashMap<String, List<String>>();
        while (cursor.moveToNext())
        {
            String temp_name = cursor.getString(cursor.getColumnIndex(Rou_col1));
            List<String> temp_list = new ArrayList<String>();
            String[] col = {Rou_ex_col1, Rou_ex_col2};
            String selection = Rou_ex_col1+" =?";
            String[] selectionArgs = {temp_name};
            Cursor cur =db.query(TABLE_Rou_ex_list,col,selection,selectionArgs,null,null,null);
            while(cur.moveToNext()){
                String rou_exercise = cur.getString(cur.getColumnIndex(Rou_ex_col2));
                temp_list.add(rou_exercise);
            }
            rList.put(temp_name,temp_list);
        }
        return rList;
    }

    public void deleteRoutine(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = Rou_col1+" =?";
        String[] whereArgs = new String[] { name };
        db.delete(TABLE_Rou_list, whereClause, whereArgs);
    }

    //Add exercise to routine
    public boolean addExercise_Rou(String routine, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Rou_col1,routine);
        long result = db.insert(TABLE_Rou_list,null,contentValues);
        Log.d("addRoutine",Long.toString(result));

        contentValues = new ContentValues();
        contentValues.put(Rou_ex_col1, routine);
        contentValues.put(Rou_ex_col2, name);
        result = db.insert(TABLE_Rou_ex_list, null, contentValues);
        Log.d("addExercise_to_a_routine", Long.toString(result));

        if(result <= 0)
            return false;
        else
            return true;
    }



    //Add routine to daily page
        //look up routine in database => get exercises into list
        //add exercises in the list to the daily page
}
