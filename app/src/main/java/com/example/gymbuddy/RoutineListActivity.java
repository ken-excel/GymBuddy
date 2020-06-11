package com.example.gymbuddy;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RoutineListActivity extends AppCompatActivity {
    DatabaseHelper db;
    ExpandableListView expandableListView;
    RoutineListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;
    boolean delBtn_visible = false;
    Button doneBtn;

    //Adapter Class starts here
    public class RoutineListAdapter extends BaseExpandableListAdapter {

        private Context context;
        private List<String> expandableListTitle;
        private HashMap<String, List<String>> expandableListDetail;

        public RoutineListAdapter(Context context, List<String> expandableListTitle,
                                  HashMap<String, List<String>> expandableListDetail) {
            this.context = context;
            this.expandableListTitle = expandableListTitle;
            this.expandableListDetail = expandableListDetail;
        }

        @Override
        public Object getChild(int listPosition, int expandedListPosition) {
            return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                    .get(expandedListPosition);
        }

        @Override
        public long getChildId(int listPosition, int expandedListPosition) {
            return expandedListPosition;
        }

        @Override
        public View getChildView(int listPosition, final int expandedListPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            final String expandedListText = (String) getChild(listPosition, expandedListPosition);
            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.list_item, null);
            }
            TextView expandedListTextView = (TextView) convertView
                    .findViewById(R.id.expandedListItem);
            expandedListTextView.setText(expandedListText);
            return convertView;
        }

        @Override
        public int getChildrenCount(int listPosition) {
            return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                    .size();
        }

        @Override
        public Object getGroup(int listPosition) {
            return this.expandableListTitle.get(listPosition);
        }

        @Override
        public int getGroupCount() {
            return this.expandableListTitle.size();
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

            Button delBtn = convertView.findViewById(R.id.del_btn);
            if(!delBtn_visible){
                delBtn.setVisibility(View.GONE);
            }
            else{
                delBtn.setVisibility(View.VISIBLE);
            }
            delBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    db.deleteRoutine(listTitle);
                    expandableListTitle.remove(new String(listTitle));
                    expandableListDetail.remove(new String(listTitle));
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
    //Adapter Class ends here

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intentHome = new Intent(RoutineListActivity.this, MainActivity.class);
                    startActivity(intentHome);
                    overridePendingTransition(R.anim.left_in,R.anim.right_out);
                    return true;
                case R.id.navigation_routine:
                    return true;
                case R.id.navigation_exercise:
                    Intent intentExercise = new Intent(RoutineListActivity.this, ExerciseListActivity.class);
                    startActivity(intentExercise);
                    overridePendingTransition(R.anim.right_in,R.anim.left_out);
                    return true;
                case R.id.navigation_calendar:
                    Intent intentCalendar = new Intent(RoutineListActivity.this, CalendarActivity.class);
                    startActivity(intentCalendar);
                    overridePendingTransition(R.anim.right_in,R.anim.left_out);
                    return true;
                case R.id.navigation_statistic:
                    Intent intentStatistic = new Intent(RoutineListActivity.this, StatisticActivity.class);
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
        setContentView(R.layout.activity_routine_list);

        //database
        db = new DatabaseHelper(this);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.routine_toolbar);
        toolbar.setTitle("Routine List");
        setSupportActionBar(toolbar);

        //navigation Bar
        BottomNavigationView navView = findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.navigation_routine);
        menuItem.setChecked(true);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //get data from database
        expandableListView = (ExpandableListView) findViewById(R.id.routineListView);
        expandableListDetail = db.getRoutine();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new RoutineListAdapter(this, expandableListTitle, expandableListDetail);
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
                Toast.makeText(
                        getApplicationContext(),
                        expandableListTitle.get(groupPosition)
                                + " -> "
                                + expandableListDetail.get(
                                expandableListTitle.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT
                ).show();
                return false;
            }
        });

        //Done editing Button
        doneBtn = (Button)findViewById(R.id.button_done);
    }
    //Toolbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflater
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.routine_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.menuAddRoutine:
                Toast.makeText(this, "Add Routine", Toast.LENGTH_SHORT).show();
                Intent intentAddRoutine = new Intent(RoutineListActivity.this, AddRoutineActivity.class);
                startActivity(intentAddRoutine);
                break;

            case R.id.menuEditRoutine:
                Toast.makeText(this, "Edit Routine", Toast.LENGTH_SHORT).show();
                delBtn_visible = true;
                doneBtn.setVisibility(View.VISIBLE);
                expandableListAdapter.notifyDataSetChanged();
                break;
        }
        return true;
    }

    public void doneButton(View view){
        delBtn_visible = false;
        doneBtn.setVisibility(View.GONE);
        expandableListAdapter.notifyDataSetChanged();
    }
}
