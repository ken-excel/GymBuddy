package com.example.gymbuddy;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper db;
    private TextView date1,date2,date3;
    String date, dayofweek;
    ImageButton addExercise, addRoutine, editButton;
    ExpandableListView expandableListView;
    SessionListAdapter expandableListAdapter;
    List<String> exerciseList;
    boolean delBtn_visible = false;
    HashMap<String, String> typeList;
    HashMap<String, List<Session>> sessionList;

    //Adapter Class starts here
    public class SessionListAdapter extends BaseExpandableListAdapter {

        private Context context;
        private List<String> exerciseList;
        private HashMap<String, String> typeList;
        private HashMap<String, List<Session>> sessionList;

        public SessionListAdapter(Context context, List<String> Title, HashMap<String,String> Type,
                                  HashMap<String, List<Session>> Detail) {
            this.context = context;
            this.exerciseList = Title;
            this.typeList = Type;
            this.sessionList = Detail;
        }

        @Override
        public Object getChild(int listPosition, int expandedListPosition) {
            return this.sessionList.get(this.exerciseList.get(listPosition))
                    .get(expandedListPosition);
        }

        @Override
        public long getChildId(int listPosition, int expandedListPosition) {
            return expandedListPosition;
        }

        public String getType(int listPosition) {
            return this.typeList.get(this.exerciseList.get(listPosition));
        }

        @Override
        public View getChildView(int listPosition, final int expandedListPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            final String listTitle = (String) getGroup(listPosition);
            final String typeSession = (String) getType(listPosition);
            final long childId = (long) getChildId(listPosition, expandedListPosition);
            final Session expandedSession = (Session) getChild(listPosition, expandedListPosition);
            if(isLastChild){
                LayoutInflater layoutInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.list_item_addsession, null);
            }
            else{
                LayoutInflater layoutInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.list_item_session, null);
            }
            if(!isLastChild){
                TextView setTextView = (TextView) convertView
                        .findViewById(R.id.set);
                setTextView.setText("Set "+childId);
                TextView firstTextView = (TextView) convertView
                        .findViewById(R.id.first_slot);
                TextView secondTextView = (TextView) convertView
                        .findViewById(R.id.second_slot);
                switch (typeSession) {
                    case "W":
                        firstTextView.setText(expandedSession.data1+" Reps");
                        secondTextView.setText(expandedSession.data2+" kg");
                        break;
                    case "R":
                        firstTextView.setText(expandedSession.data1+" Reps");
                        secondTextView.setText("");
                        break;
                    case "T":
                        firstTextView.setText(expandedSession.data1+" Seconds");
                        secondTextView.setText("");
                        break;
                }
            }
            else{
                TextView setTextView = (TextView) convertView
                        .findViewById(R.id.set);
                setTextView.setText("Set "+childId);
                final EditText firstEditText = (EditText) convertView.findViewById(R.id.first_edit);
                final EditText secondEditText = (EditText) convertView.findViewById(R.id.second_edit);
                TextView firstTextView = (TextView) convertView
                        .findViewById(R.id.first_slot);
                TextView secondTextView = (TextView) convertView
                        .findViewById(R.id.second_slot);
                switch (typeSession) {
                    case "W":
                        firstTextView.setText("Reps");
                        secondTextView.setText("kg");
                        break;
                    case "R":
                        firstTextView.setText("Reps");
                        secondTextView.setText("");
                        break;
                    case "T":
                        firstTextView.setText("Seconds");
                        secondTextView.setText("");
                        break;
                }

                Button addBtn = convertView.findViewById(R.id.add_btn);
                addBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        String text1 = firstEditText.getText().toString();
                        String text2 = secondEditText.getText().toString();
                        if(text1.isEmpty() || text2.isEmpty())
                        {
                            Toast.makeText(MainActivity.this, "Please enter all information", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Integer data1 = Integer.parseInt(text1);
                            Double data2 = Double.parseDouble(text2);
                            boolean inserted = db.addSession(listTitle, date, data1, data2);
                            if (inserted) {
                                Toast.makeText(MainActivity.this, "Session added", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                            List<Session> sList = sessionList.get(listTitle);
                            Session Se = new Session();
                            Se.data1 = data1;
                            Se.data2 = data2;
                            sList.set(expandedListPosition,Se);
                            Se = new Session();
                            sList.add(Se);
                            notifyDataSetChanged();
                        }
                    }
                });

            }
            return convertView;
        }

        @Override
        public int getChildrenCount(int listPosition) {
            return this.sessionList.get(this.exerciseList.get(listPosition))
                    .size();
        }

        @Override
        public Object getGroup(int listPosition) {
            return this.exerciseList.get(listPosition);
        }

        @Override
        public int getGroupCount() {
            return this.exerciseList.size();
        }

        @Override
        public long getGroupId(int listPosition) {
            return listPosition;
        }

        @Override
        public View getGroupView(int listPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            final String listTitle = (String) getGroup(listPosition);
            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) this.context.
                        getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.list_group_removebtn, null);
            }
            TextView listTitleTextView = (TextView) convertView
                    .findViewById(R.id.list_item_string);
            listTitleTextView.setTypeface(null, Typeface.BOLD);
            listTitleTextView.setText(listTitle);

            Button sessionBtn = convertView.findViewById(R.id.del_btn);
            if(!delBtn_visible){
                sessionBtn.setVisibility(View.GONE);
            }
            else{
                sessionBtn.setVisibility(View.VISIBLE);
            }
            sessionBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    db.deleteExercise_Day(date, listTitle);
                    exerciseList.remove(new String(listTitle));
                    typeList.remove(new String(listTitle));
                    sessionList.remove(new String(listTitle));
                    notifyDataSetChanged();
                }
            });

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int listPosition, int expandedListPosition) {
            return true;
        }
    }
    //Adapter class ends here

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_routine:
                    Intent intentRoutine = new Intent(MainActivity.this, RoutineListActivity.class);
                    startActivity(intentRoutine);
                    overridePendingTransition(R.anim.right_in,R.anim.left_out);
                    return true;
                case R.id.navigation_exercise:
                    Intent intentExercise = new Intent(MainActivity.this, ExerciseListActivity.class);
                    startActivity(intentExercise);
                    overridePendingTransition(R.anim.right_in,R.anim.left_out);
                    return true;
                case R.id.navigation_calendar:
                    Intent intentCalendar = new Intent(MainActivity.this, CalendarActivity.class);
                    startActivity(intentCalendar);
                    overridePendingTransition(R.anim.right_in,R.anim.left_out);
                    return true;
                case R.id.navigation_statistic:
                    Intent intentStatistic = new Intent(MainActivity.this, StatisticActivity.class);
                    startActivity(intentStatistic);
                    overridePendingTransition(R.anim.right_in,R.anim.left_out);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(this);

        //Nav View
        BottomNavigationView navView = findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.navigation_home);
        menuItem.setChecked(true);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Date Panel -- now just work on today
        date1 = findViewById(R.id.date1);
        date = new SimpleDateFormat("MMMM dd").format(Calendar.getInstance().getTime()); //currently is today but we will change here
        dayofweek = new SimpleDateFormat("EEE").format(Calendar.getInstance().getTime());
        date1.setText("Today");
        date2 = findViewById(R.id.date2);
        date2.setText(date);
        date3 = findViewById(R.id.date3);
        date3.setText(dayofweek);

        addExercise = findViewById(R.id.AddExercise);
        addRoutine = findViewById(R.id.AddRoutine);
        editButton = findViewById(R.id.EditButton);

        addExercise.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(MainActivity.this, "Add Exercise", Toast.LENGTH_SHORT).show();
                Intent intentAddRoutine = new Intent(MainActivity.this, AddExerciseToDayActivity.class);
                intentAddRoutine.putExtra("date",date);
                startActivity(intentAddRoutine);
            }
        });
        addRoutine.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(MainActivity.this, "Add Routine", Toast.LENGTH_SHORT).show();
                Intent intentAddRoutine = new Intent(MainActivity.this, AddRoutineToDayActivity.class);
                intentAddRoutine.putExtra("date",date);
                startActivity(intentAddRoutine);
            }
        });
        editButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(MainActivity.this, "Edit Exercise", Toast.LENGTH_SHORT).show();
                if(!delBtn_visible)delBtn_visible = true;
                else delBtn_visible = false;
                expandableListAdapter.notifyDataSetChanged();
            }
        });

        //Get exercise data for this day
        //get data from database
        expandableListView = (ExpandableListView) findViewById(R.id.sessionListView);
        sessionList = db.getSession_Day(date);
        typeList = db.getExerciseType();
        exerciseList = db.getExercise_Day(date);
        expandableListAdapter = new SessionListAdapter(this, exerciseList, typeList, sessionList);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        expandableListTitle.get(groupPosition) + " List Expanded.",
//                        Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        expandableListTitle.get(groupPosition) + " List Collapsed.",
//                        Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
//                Toast.makeText(
//                        getApplicationContext(),
//                        expandableListTitle.get(groupPosition)
//                                + " -> "
//                                + expandableListDetail.get(
//                                expandableListTitle.get(groupPosition)).get(
//                                childPosition), Toast.LENGTH_SHORT
//                ).show();
                return false;
            }
        });

    }

}
