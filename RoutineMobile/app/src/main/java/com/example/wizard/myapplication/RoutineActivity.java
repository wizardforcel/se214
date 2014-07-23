package com.example.wizard.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import com.example.wizard.myapplication.utility.NoteRow;
import com.example.wizard.myapplication.utility.RoutineManager;

import java.util.ArrayList;
import java.util.Calendar;

public class RoutineActivity extends ActionBarActivity {

    private int Year;
    private int Month;
    private int Day;
    private RoutineManager Manager
      = new RoutineManager();
    private int RemoveIndex;

    private ListView RoutineTable;
    private Button AddButton;

    private void InitViews()
    {
        RoutineTable = (ListView)findViewById(R.id.RoutineTable);
        RoutineTable.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            { RoutineTable_OnItemClick(adapterView, view, i, l); }
        });
        RoutineTable.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                RoutineTable_OnItemLongClick(adapterView, view, i, l);
                return true;
            }
        });

        AddButton = (Button)findViewById(R.id.AddButton);
        AddButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            { AddButton_OnClick(view); }
        });
    }

    private void ShowRoutine()
    {
        Manager.Load(this, Year, Month, Day);
        ArrayList<String> titles
          = new ArrayList<String>();
        for(int i = 0; i < Manager.Size(); i++)
        {
            NoteRow note = Manager.Get(i);
            titles.add(note.GetTitle());
        }
        RoutineTable.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, titles));

    }

    private void AddButton_OnClick(View view)
    {
        Intent intent = new Intent();
        intent.setClass(this, SettingActivity.class);
        intent.putExtra("isadd", true);
        NoteRow note = new NoteRow();
        note.SetDate(Year * 10000 + Month * 100 + Day);
        intent.putExtra("row", note);
        intent.putExtra("manager", Manager);
        this.startActivity(intent);
    }

    //短按修改
    private void RoutineTable_OnItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        Intent intent = new Intent();
        intent.setClass(this, SettingActivity.class);
        intent.putExtra("isadd", false);
        intent.putExtra("index", i);
        NoteRow note = Manager.Get(i);
        intent.putExtra("row", note);
        intent.putExtra("manager", Manager);
        this.startActivity(intent);
    }

    //长按删除
    private void RoutineTable_OnItemLongClick(AdapterView<?> adapterView, View view, int i, long l)
    {
            RemoveIndex = i;
            new AlertDialog.Builder(this)
                    .setTitle("确实要删除吗？")
                    .setPositiveButton("是", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        { DeleteDialogOKButton_OnClick(dialogInterface, i); }
                    })
                    .setNegativeButton("否", null)
                    .show();
    }

    private void DeleteDialogOKButton_OnClick(DialogInterface dialogInterface, int i)
    {
        try
        {
            Manager.RemoveAt(this, RemoveIndex);
            ShowRoutine();
        }
        catch(Exception ex)
        {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

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

        InitViews();

        ShowRoutine();
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        Manager.Clear();
        Manager.Load(this, Year, Month, Day);
        ShowRoutine();
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
