package routinemobile;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;
import java.lang.*;
import java.util.Calendar;
import routinemobile.utility.CalendarManager;

public class MainActivity extends ActionBarActivity
{
    private GridView CalendarTable;
    private TextView DateText;

    private void InitViews()
    {
        CalendarTable = (GridView)findViewById(R.id.CalendarTable);
        DateText = (TextView)findViewById(R.id.DateText);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitViews();

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        ShowCalendar(year, month);
    }

    private void ShowCalendar(int year, int month)
    {
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
        DateText.setText(String.format("%d年%d月", year, month));
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
