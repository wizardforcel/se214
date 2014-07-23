package com.example.wizard.myapplication;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Calendar;

public class RoutineActivity extends ActionBarActivity {

    private int Year;
    private int Month;
    private int Day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine);
        Calendar cal = Calendar.getInstance();
        Intent intent = this.getIntent();
        Year = intent.getIntExtra("year", cal.get(Calendar.YEAR));
        Month = intent.getIntExtra("month", cal.get(Calendar.MONTH) + 1);
        Day = intent.getIntExtra("day", cal.get(Calendar.DATE));
        this.setTitle(String.format("%d年%d月%d日", Year, Month, Day));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.routine, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
