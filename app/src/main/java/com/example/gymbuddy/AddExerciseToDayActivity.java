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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddExerciseToDayActivity extends AppCompatActivity {
    DatabaseHelper db;
    Button doneBtn;
    String date;
    ListView listView;
    HashMap<String,String> aList;
    ArrayList<String> nameList;

    //Adapter Class Starts
    class AddExerciseToDayAdapter extends BaseAdapter implements ListAdapter {
        private ArrayList<String> list = new ArrayList<String>();
        private Context context;
        public String selectedItem;

        public AddExerciseToDayAdapter(ArrayList<String> list, Context context) {
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
                    String name = (String) list.get(position);
                    String type = (String) aList.get(name);
                    boolean inserted = db.addExercise_Day(date, name, type);
                    if(inserted){
                        Toast.makeText(AddExerciseToDayActivity.this, "Exercise added to Day", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(AddExerciseToDayActivity.this, "Exercise already existed", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            addBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    String name = (String) list.get(position);
                    String type = (String) aList.get(name);
                    boolean inserted = db.addExercise_Day(date, name, type);
                    if(inserted){
                        Toast.makeText(AddExerciseToDayActivity.this, "Exercise added to Day", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(AddExerciseToDayActivity.this, "Exercise already existed", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_new_exercise_day);

        Intent i = getIntent();
        Bundle extras = i.getExtras();
        if(extras.containsKey("date")) {
            date = i.getStringExtra("date");
        }
        //Database
        db = new DatabaseHelper(this);

        //Exercise List
        listView = findViewById(R.id.exercise_list);
        aList = db.getExerciseType();
        nameList = new ArrayList<String>(aList.keySet());
        AddExerciseToDayActivity.AddExerciseToDayAdapter adapter = new AddExerciseToDayActivity.AddExerciseToDayAdapter(nameList,this);
        listView.setAdapter(adapter);

        //Get Edit Text
        doneBtn = findViewById(R.id.button_done);
    }

    public void doneButton(View view){
        Intent intentRoutine = new Intent(AddExerciseToDayActivity.this, MainActivity.class);
        startActivity(intentRoutine);
    }
}
