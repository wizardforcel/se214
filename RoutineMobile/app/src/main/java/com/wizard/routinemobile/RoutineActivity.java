package com.wizard.routinemobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import com.wizard.routinemobile.utility.NoteRow;
import com.wizard.routinemobile.utility.RoutineManager;

import java.util.ArrayList;
import java.util.Calendar;

public class RoutineActivity extends ActionBarActivity {

    private int year;
    private int month;
    private int day;
    private RoutineManager manager
      = new RoutineManager();
    private int removeIndex;

    private ListView routineTable;
    private Button addButton;

    private void initViews()
    {
        routineTable = (ListView)findViewById(R.id.routineTable);
        routineTable.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                routineTable_ItemClick(adapterView, view, i, l);
            }
        });
        routineTable.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                routineTable_ItemLongClick(adapterView, view, i, l);
                return true;
            }
        });

        addButton = (Button)findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addButton_Click(view);
            }
        });
    }

    private void showRoutine()
    {
        manager.load(this, year, month, day);
        ArrayList<String> titles
          = new ArrayList<String>();
        for(int i = 0; i < manager.size(); i++)
        {
            NoteRow note = manager.get(i);
            titles.add(note.getTitle());
        }
        routineTable.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, titles));

    }

    private void addButton_Click(View view)
    {
        Intent intent = new Intent();
        intent.setClass(this, SettingActivity.class);
        intent.putExtra("isadd", true);
        NoteRow note = new NoteRow();
        note.setDate(year * 10000 + month * 100 + day);
        intent.putExtra("row", note);
        intent.putExtra("manager", manager);
        this.startActivity(intent);
    }

    //短按修改
    private void routineTable_ItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        Intent intent = new Intent();
        intent.setClass(this, SettingActivity.class);
        intent.putExtra("isadd", false);
        intent.putExtra("index", i);
        NoteRow note = manager.get(i);
        intent.putExtra("row", note);
        intent.putExtra("manager", manager);
        this.startActivity(intent);
    }

    //长按删除
    private void routineTable_ItemLongClick(AdapterView<?> adapterView, View view, int i, long l)
    {
            removeIndex = i;
            new AlertDialog.Builder(this)
                    .setTitle("确实要删除吗？")
                    .setPositiveButton("是", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        { deleteDialogOkButton_Click(dialogInterface, i); }
                    })
                    .setNegativeButton("否", null)
                    .show();
    }

    private void deleteDialogOkButton_Click(DialogInterface dialogInterface, int i)
    {
        try
        {
            manager.removeAt(this, removeIndex);
            showRoutine();
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
        year = intent.getIntExtra("year", cal.get(Calendar.YEAR));
        month = intent.getIntExtra("month", cal.get(Calendar.MONTH) + 1);
        day = intent.getIntExtra("day", cal.get(Calendar.DATE));
        this.setTitle(String.format("%d年%d月%d日", year, month, day));

        initViews();

        showRoutine();
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        manager.clear();
        manager.load(this, year, month, day);
        showRoutine();
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
        // as you specify at parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
