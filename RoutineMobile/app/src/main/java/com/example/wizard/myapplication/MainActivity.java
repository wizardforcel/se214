package com.example.wizard.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.app.*;
import android.widget.*;
import java.lang.*;
import java.util.Calendar;
import com.example.wizard.myapplication.utility.CalendarManager;
import com.example.wizard.myapplication.utility.DatabaseHelper;

public class MainActivity extends ActionBarActivity
{
    private GridView CalendarTable;
    private Button SettingButton;
    AlertDialog DateDialog;
    private DatePicker DatePicker;

    private int Year;
    private int Month;

    private void InitViews()
    {
        CalendarTable = (GridView)findViewById(R.id.CalendarTable);
        CalendarTable.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            { RoutineTable_OnItemClick(adapterView, view, i, l); }
        });

        SettingButton = (Button)findViewById(R.id.SettingButton);
        SettingButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            { SettingButton_OnClick(view); }
        });

        DatePicker = new DatePicker(this);
        DateDialog = new AlertDialog.Builder(this).setTitle("请输入日期")
                .setView(DatePicker)
                .setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    { DateDialogOKButton_OnClick(dialogInterface, i); }
                })
                .setNegativeButton("取消", null).create();
    }

    private void RoutineTable_OnItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        String daystr = (String)CalendarTable.getAdapter().getItem(i);
        if(daystr.equals("")) return;
        int day = Integer.parseInt(daystr);

        Intent intent = new Intent();
        intent.setClass(this, RoutineActivity.class);
        intent.putExtra("year", Year);
        intent.putExtra("month", Month);
        intent.putExtra("day", day);
        this.startActivity(intent);
    }

    private void DateDialogOKButton_OnClick(DialogInterface dialogInterface, int i)
    {
        Year = DatePicker.getYear();
        Month = DatePicker.getMonth() + 1;
        ShowCalendar();
    }


    private void SettingButton_OnClick(View view)
    {
        DatePicker.init(Year, Month - 1, 1, null);
        DateDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("日程管理");

        InitViews();

        Calendar cal = Calendar.getInstance();
        Year = cal.get(Calendar.YEAR);
        Month = cal.get(Calendar.MONTH) + 1;
        ShowCalendar();

        new Thread(new Runnable()
        {
            @Override
            public void run() { DoNotice(); }
        }).start();
    }

    private void ShowCalendar()
    {
        CalendarManager cm = new CalendarManager();
        cm.SetDate(Year, Month);
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
        SettingButton.setText(String.format("%d年%d月", Year, Month));
    }

    private void DoNotice()
    {
        int lastmin = -1;
        SQLiteDatabase db;
        try
        {
            db = new DatabaseHelper(this).getReadableDatabase();
        }
        catch(Exception ex)
        {
            //Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }
        String sql = "SELECT title FROM note " +
                     "WHERE ((alerttype=2 AND date=?) OR " +
                     "(alerttype=1 AND date=?)) AND alerttime=?";

        while(true)
        {
            try
            {
                System.out.println("Thread is running...");
                Calendar cal =  Calendar.getInstance();
                int min = cal.get(Calendar.MINUTE);
                if(min == lastmin)
                {
                    Thread.sleep(10 * 1000);
                    continue;
                }
                lastmin = min;
                int date = cal.get(Calendar.YEAR) * 10000 +
                        (cal.get(Calendar.MONTH) + 1) * 100 +
                        cal.get(Calendar.DATE);
                int time = cal.get(Calendar.HOUR_OF_DAY) * 100 + min;
                Cursor cur = db.rawQuery(sql, new String[]
                {
                    String.valueOf(date), String.valueOf(date + 1), String.valueOf(time)
                });
                while(cur.moveToNext())
                {
                    String title = cur.getString(cur.getColumnIndex("title"));
                    ShowNotify("日程管理", title + " 即将开始");
                }
                System.out.println(String.format("%d %d %d", date, time, cur.getCount()));
                cur.close();
                Thread.sleep(10 * 1000);
            }
            catch(Exception ex)
            {
                //Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void ShowNotify(String title, String content)
    {
        NotificationManager nm
                = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Notification noti
                = new Notification(R.drawable.ic_launcher, title, System.currentTimeMillis());
        noti.defaults |= Notification.DEFAULT_SOUND;

        Context context = getApplicationContext();
        Intent notiintent = new Intent(); //点击该通知后要跳转的Activity
        notiintent.setClass(this, MainActivity.class);
        PendingIntent cointent = PendingIntent.getActivity(this, 0 , notiintent, 0);
        noti.setLatestEventInfo(context, title, content, cointent);

        nm.notify(0, noti);
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
