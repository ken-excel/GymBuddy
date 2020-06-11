package com.example.gymbuddy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AddRoutineActivity extends AppCompatActivity {
    DatabaseHelper db;
    Button doneBtn;
    EditText editText;
    String nameReg;
    private ListView listView;
    private ArrayList<String> aList;;

//Adapter Class Starts

    class AddRoutineAdapter extends BaseAdapter implements ListAdapter {
        private ArrayList<String> list = new ArrayList<String>();
        private Context context;
        public String selectedItem;

        public AddRoutineAdapter(ArrayList<String> list, Context context) {
            this.list = list;
            this.context = context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int pos) {
            return list.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_group_addbtn, null);
            }

            //Handle TextView and display string from your list
            TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
            listItemText.setText(list.get(position));

            Button addBtn = (Button)view.findViewById(R.id.add_btn);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    selectedItem = (String) list.get(position);
                    nameReg = editText.getText().toString();
                    boolean inserted = db.addExercise_Rou(nameReg,selectedItem);
                    if(inserted){
                        Toast.makeText(AddRoutineActivity.this, "Exercise added to Routine", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(AddRoutineActivity.this, "Exercise already existed", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            addBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    selectedItem = (String) list.get(position);
                    nameReg = editText.getText().toString();
                    boolean inserted = db.addExercise_Rou(nameReg,selectedItem);
                    if(inserted){
                        Toast.makeText(AddRoutineActivity.this, "Exercise added to Routine", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(AddRoutineActivity.this, "Exercise already existed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            return view;
        }
    }
//Adapter Class Ends


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_routine);

        //Database
        db = new DatabaseHelper(this);

        //Exercise List
        listView = findViewById(R.id.exercise_list);
        aList = db.getExerciseNames();
        AddRoutineAdapter adapter = new AddRoutineAdapter(aList,this);
        listView.setAdapter(adapter);

        //Get Edit Text
        doneBtn = findViewById(R.id.button_done);
        editText = findViewById(R.id.editText);
    }

    public void doneButton(View view){
        Intent intentRoutine = new Intent(AddRoutineActivity.this, RoutineListActivity.class);
        startActivity(intentRoutine);
    }
}
