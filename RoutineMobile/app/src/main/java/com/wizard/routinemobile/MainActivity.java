package com.wizard.routinemobile;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.app.*;
import android.widget.*;
import java.lang.*;
import java.util.Calendar;
import com.wizard.routinemobile.baiduapi.BaiduAPI;
import com.wizard.routinemobile.baiduapi.LocResult;
import com.wizard.routinemobile.baiduapi.WeatherResult;
import com.wizard.routinemobile.utility.CalendarManager;
import com.wizard.routinemobile.utility.DatabaseHelper;
import com.wizard.routinemobile.utility.WizardHTTP;

public class MainActivity extends ActionBarActivity
{
    private GridView calendarTable;
    private Button settingButton;
    AlertDialog dateDialog;
    private DatePicker datePicker;
    private TextView weatherLabel;

    private int year;
    private int month;
    private String weather = "";

    private void initViews()
    {
        calendarTable = (GridView)findViewById(R.id.calendarTable);
        calendarTable.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                routineTable_ItemClick(adapterView, view, i, l);
            }
        });

        settingButton = (Button)findViewById(R.id.settingButton);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingButton_Click(view);
            }
        });

        datePicker = new DatePicker(this);
        dateDialog = new AlertDialog.Builder(this).setTitle("请输入日期")
                .setView(datePicker)
                .setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    { dateDialogOkButton_Click(dialogInterface, i); }
                })
                .setNegativeButton("取消", null).create();

        weatherLabel = (TextView)findViewById(R.id.weatherText);
    }

    private void routineTable_ItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        String daystr = (String) calendarTable.getAdapter().getItem(i);
        if(daystr.equals("")) return;
        int day = Integer.parseInt(daystr);

        Intent intent = new Intent();
        intent.setClass(this, RoutineActivity.class);
        intent.putExtra("year", year);
        intent.putExtra("month", month);
        intent.putExtra("day", day);
        this.startActivity(intent);
    }

    private void dateDialogOkButton_Click(DialogInterface dialogInterface, int i)
    {
        year = datePicker.getYear();
        month = datePicker.getMonth() + 1;
        showCalendar();
    }


    private void settingButton_Click(View view)
    {
        datePicker.init(year, month - 1, 1, null);
        dateDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("日程管理");

        initViews();

        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;
        showCalendar();

        /*new Thread(new Runnable()
        {
            @Override
            public void run() { doNotice(); }
        }).start();*/

        new Thread(new Runnable()
        {
            @Override
            public void run() { getWeather(); }
        }).start();
    }

    private void showCalendar()
    {
        CalendarManager cm = new CalendarManager();
        cm.setDate(year, month);
        final int length = CalendarManager.HEIGHT * CalendarManager.WIDTH;
        String[] data = new String[length];
        for(int i = 0; i < length; i++)
        {
            int tmp = cm.at(i / CalendarManager.WIDTH, i % CalendarManager.WIDTH);
            if(tmp == 0)
                data[i] = "";
            else
                data[i] = String.valueOf(tmp);
        }
        calendarTable.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data));
        settingButton.setText(String.format("%d年%d月", year, month));
    }

    private void doNotice()
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
                    showNotify("日程管理", title + " 即将开始");
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

    private void showNotify(String title, String content)
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

    public void showWeather()
    {
        weatherLabel.setText(weather);
        //System.out.println("blablabla");
        //this.settingButton.setText(weather);
    }

    private void getWeather()
    {
        try
        {
            Looper.prepare();
            WizardHTTP wc = new WizardHTTP();
            wc.setDefHeader(false);
            LocResult lr = BaiduAPI.getLoc(wc, "");
            if(lr.errno != 0)
                throw new Exception(lr.errmsg);
            String city = lr.city;
            WeatherResult wr = BaiduAPI.getWeater(wc, city);
            if(wr.errno != 0)
                throw new Exception(wr.errmsg);
            String weather = wr.weather;
            String wind = wr.wind;
            String temperature = wr.temprature;
            this.weather = city + " " + temperature + " " +
                         weather + " " + wind;
            //System.out.println(this.weather);
            Handler hdl = new Handler(){
                @Override
                public void handleMessage(Message msg)
                {
                    showWeather();
                    super.handleMessage(msg);
                }
            };
            hdl.sendMessage(new Message());
        }
        catch(Exception ex)
        {
            this.weather = "天气获取失败！" + ex.getMessage();
            Handler hdl = new Handler(){
                @Override
                public void handleMessage(Message msg)
                {
                    showWeather();
                    super.handleMessage(msg);
                }
            };
            hdl.sendMessage(new Message());
        }
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
        // as you specify at parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings ||
               super.onOptionsItemSelected(item);
    }
}
