package RoutineMobile;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import java.lang.*;
import java.util.ArrayList;
import java.util.Calendar;

import RoutineMobile.Utility.CalendarManager;

public class MainActivity extends ActionBarActivity
{
    private GridView CalendarTable;

    private void InitViews()
    {
        CalendarTable = (GridView)findViewById(R.id.CalendarTable);

        /*NewButton = (Button)findViewById(R.id.button);
        NewButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            { NewButton_OnClick(view); }
        });*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitViews();

        ShowCalendar();
    }

    private void ShowCalendar()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        CalendarManager cm = new CalendarManager();
        cm.SetDate(year, month);
        final int length = CalendarManager.HEIGHT * CalendarManager.WIDTH;
        String[] data = new String[length];
        for(int i = 0; i < length; i++)
        {
            int tmp = cm.At(i / CalendarManager.WIDTH, i % CalendarManager.WIDTH);
            if(tmp == 0)
                data[i] = "";
            else
                data[i] = String.valueOf(tmp);
        }
        CalendarTable.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings ||
               super.onOptionsItemSelected(item);
    }
}
