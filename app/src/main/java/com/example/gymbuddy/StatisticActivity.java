package com.example.gymbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StatisticActivity extends AppCompatActivity {
    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intentHome = new Intent(StatisticActivity.this, MainActivity.class);
                    startActivity(intentHome);
                    overridePendingTransition(R.anim.left_in,R.anim.right_out);
                    return true;
                case R.id.navigation_routine:
                    Intent intentRoutine = new Intent(StatisticActivity.this, RoutineListActivity.class);
                    startActivity(intentRoutine);
                    overridePendingTransition(R.anim.left_in,R.anim.right_out);
                    return true;
                case R.id.navigation_exercise:
                    Intent intentExercise = new Intent(StatisticActivity.this, ExerciseListActivity.class);
                    startActivity(intentExercise);
                    overridePendingTransition(R.anim.left_in,R.anim.right_out);
                    return true;
                case R.id.navigation_calendar:
                    Intent intentCalendar = new Intent(StatisticActivity.this, CalendarActivity.class);
                    startActivity(intentCalendar);
                    overridePendingTransition(R.anim.left_in,R.anim.right_out);
                    return true;
                case R.id.navigation_statistic:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        mTextMessage.setText(R.string.title_statistic);
        Menu menu = navView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.navigation_statistic);
        menuItem.setChecked(true);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
}
