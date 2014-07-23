package com.example.wizard.myapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
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

public class MainActivity extends ActionBarActivity
{
    private GridView CalendarTable;
    private Button SettingButton;
    private int Year;
    private int Month;

    AlertDialog DateDialog;
    private EditText YearText;
    private EditText MonthText;

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

        View datedialogview = LayoutInflater.from(this).inflate(R.layout.date_dialog, null);
        DateDialog = new AlertDialog.Builder(this).setTitle("请输入日期")
                .setView(datedialogview)
                .setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    { DateDialogOKButton_OnClick(dialogInterface, i); }
                })
                .setNegativeButton("取消", null).create();
        YearText = (EditText)datedialogview.findViewById(R.id.YearText);
        MonthText = (EditText)datedialogview.findViewById((R.id.MonthText));
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
        String yearstr = YearText.getText().toString();
        int year = yearstr.equals("")? 0: Integer.parseInt(yearstr);
        if(year < 1900 || year > 2999)
        {
            Toast.makeText(this, "请输入正确的年份！", Toast.LENGTH_SHORT).show();
            return;
        }

        String monthstr = MonthText.getText().toString();
        int month = monthstr.equals("")? 0: Integer.parseInt(monthstr);
        if(month < 1 || month > 12)
        {
            Toast.makeText(this, "请输入正确的月份！", Toast.LENGTH_SHORT).show();
            return;
        }

        Year = year;
        Month = month;
        ShowCalendar();
    }


    private void SettingButton_OnClick(View view)
    {
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
        YearText.setText(String.valueOf(Year));
        MonthText.setText(String.valueOf(Month));
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
