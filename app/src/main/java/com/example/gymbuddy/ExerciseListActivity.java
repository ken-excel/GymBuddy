package com.example.gymbuddy;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class ExerciseListActivity extends AppCompatActivity {

    DatabaseHelper db;
    private ListView listView;
    private ArrayList<String> aList;
    boolean delBtn_visible = false;
    Button doneBtn;
    ExerciseListAdapter adapter;

    //Navigation View
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intentHome = new Intent(ExerciseListActivity.this, MainActivity.class);
                    startActivity(intentHome);
                    overridePendingTransition(R.anim.left_in,R.anim.right_out);
                    return true;
                case R.id.navigation_routine:
                    Intent intentRoutine = new Intent(ExerciseListActivity.this, RoutineListActivity.class);
                    startActivity(intentRoutine);
                    overridePendingTransition(R.anim.left_in,R.anim.right_out);
                    return true;
                case R.id.navigation_exercise:
                    return true;
                case R.id.navigation_calendar:
                    Intent intentCalendar = new Intent(ExerciseListActivity.this, CalendarActivity.class);
                    startActivity(intentCalendar);
                    overridePendingTransition(R.anim.right_in,R.anim.left_out);
                    return true;
                case R.id.navigation_statistic:
                    Intent intentStatistic = new Intent(ExerciseListActivity.this, StatisticActivity.class);
                    startActivity(intentStatistic);
                    overridePendingTransition(R.anim.right_in,R.anim.left_out);
                    return true;
            }
            return false;
        }
    };

    //Adapter Class Starts

    class ExerciseListAdapter extends BaseAdapter implements ListAdapter {
        private ArrayList<String> list = new ArrayList<String>();
        private Context context;
        public String selectedItem;

        public ExerciseListAdapter(ArrayList<String> list, Context context) {
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
                view = inflater.inflate(R.layout.list_group_removebtn, null);
            }

            //Handle TextView and display string from your list
            TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
            listItemText.setText(list.get(position));

            Button delBtn = (Button)view.findViewById(R.id.del_btn);
            if(!delBtn_visible){
                delBtn.setVisibility(View.GONE);
            }
            else{
                delBtn.setVisibility(View.VISIBLE);
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    selectedItem = (String) list.get(position);
                    //goto Statistic activity of selected exercise
                }
            });

            delBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    selectedItem = (String) list.get(position);
                    db.deleteExercise(selectedItem);
                    aList.remove(new String(selectedItem));
                    notifyDataSetChanged();
                }
            });
            return view;
        }
    }
//Adapter Class Ends

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);

        //Database
        db = new DatabaseHelper(this);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.exercise_toolbar);
        toolbar.setTitle("Exercise List");
        setSupportActionBar(toolbar);

        //Navigation Bar
        BottomNavigationView navView = findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.navigation_exercise);
        menuItem.setChecked(true);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //List View
        listView = findViewById(R.id.exercise_list);
        aList = db.getExerciseNames();
        adapter = new ExerciseListAdapter(aList,this);
        listView.setAdapter(adapter);

        //Done editing Button
        doneBtn = (Button)findViewById(R.id.button_done);
    }

    //Toolbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflater
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.exercise_toolbar_menu, menu);
        //Search Bar
//        MenuItem searchViewItem = menu.findItem(R.id.exerciseSearch);
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        final SearchView searchView = (SearchView) searchViewItem.getActionView();
//        searchView.setQueryHint("Search exercises");
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setIconifiedByDefault(false);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                if(aList.contains(query)) arrayAdapter.getFilter().filter(query);
//                //else Toast.makeText(ExerciseListActivity.this,"Not found", Toast.LENGTH_SHORT).show();
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                arrayAdapter.getFilter().filter(newText);
//                return false;
//            }
//        });
        return true;
    }

    public void doneButton(View view){
        delBtn_visible = false;
        doneBtn.setVisibility(View.GONE);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.menuAddExercise:
                Toast.makeText(this, "Add Exercise", Toast.LENGTH_SHORT).show();
                Intent intentAddExercise = new Intent(ExerciseListActivity.this, AddExerciseActivity.class);
                startActivity(intentAddExercise);
                break;

            case R.id.menuEditExercise:
                Toast.makeText(this, "Edit Exercise", Toast.LENGTH_SHORT).show();
                delBtn_visible = true;
                doneBtn.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
                break;
        }
        return true;
    }
}
