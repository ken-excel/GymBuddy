package com.example.gymbuddy;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AddExerciseActivity extends AppCompatActivity {
    DatabaseHelper db;
    Button regBtn, doneBtn;
    EditText editText;
    String nameReg, muscleReg, typeReg;
    Spinner muscleSpinner, typeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_exercise);

        //Database
        db = new DatabaseHelper(this);

        //Muscle Group Spinner
        muscleSpinner = findViewById(R.id.muscle_spinner);
        ArrayAdapter<CharSequence> muscleAdapter = ArrayAdapter.createFromResource(this,
                R.array.muscle_group, R.layout.spinner_item);
        muscleAdapter.setDropDownViewResource(R.layout.spinner_item);
        muscleSpinner.setAdapter(muscleAdapter);

        //Set Type Spinner
        typeSpinner = findViewById(R.id.type_spinner);
        ;
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,
                R.array.set_type, R.layout.spinner_item);
        typeAdapter.setDropDownViewResource(R.layout.spinner_item);
        typeSpinner.setAdapter(typeAdapter);

        //Get Edit Text
        regBtn = findViewById(R.id.button_reg);
        editText = findViewById(R.id.editText);
        doneBtn = findViewById(R.id.button_done);
    }

    public void doneButton(View view){
        Intent intentRoutine = new Intent(AddExerciseActivity.this, ExerciseListActivity.class);
        startActivity(intentRoutine);
    }

    public void addExercise(View view){
        nameReg = editText.getText().toString();
        muscleReg = muscleSpinner.getSelectedItem().toString();
        typeReg = typeSpinner.getSelectedItem().toString();
        if(nameReg.isEmpty() || muscleReg.isEmpty() || typeReg.isEmpty())
        {
            Toast.makeText(AddExerciseActivity.this, "Please enter all information", Toast.LENGTH_SHORT).show();
        }
        else{
            switch (typeReg) {
                case "Reps-Weight":
                    typeReg = "W";
                    break;
                case "Reps":
                    typeReg = "R";
                    break;
                case "Time":
                    typeReg = "T";
                    break;
            }
            boolean inserted = db.addExercise(nameReg,typeReg,muscleReg);
            if(inserted){
                Toast.makeText(AddExerciseActivity.this, "Exercise added", Toast.LENGTH_SHORT).show();
                editText.setText("");
            }
            else{
                Toast.makeText(AddExerciseActivity.this, "Exercise exists", Toast.LENGTH_SHORT).show();
                editText.setText("");
            }
        }
    }

    public void clearExercise(View view){
        boolean clear = db.clearExercise();
        if(clear){
            Toast.makeText(AddExerciseActivity.this, "Exercise list cleared", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(AddExerciseActivity.this, "Exercise list not cleared", Toast.LENGTH_SHORT).show();
        }
    }

}
