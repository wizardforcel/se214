package com.example.wizard.myapplication;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.wizard.myapplication.utility.NoteRow;
import com.example.wizard.myapplication.utility.RoutineManager;

public class SettingActivity extends ActionBarActivity {

    private EditText TitleText;
    private EditText ContentText;
    private RadioButton AlertRadio0;
    private RadioButton AlertRadio1;
    private RadioButton AlertRadio2;
    private TimePicker StartTime;
    private TimePicker StopTime;
    private TimePicker AlertTime;
    private Button OKButton;
    private Button CancelButton;

    private RoutineManager Manager;
    private NoteRow Row;
    private int Index;
    private boolean IsAdd;


    private void InitViews()
    {
        TitleText = (EditText)findViewById(R.id.TitleText);
        ContentText = (EditText)findViewById(R.id.ContentText);
        AlertRadio0 = (RadioButton)findViewById(R.id.AlertRadio0);
        AlertRadio0.setChecked(true);
        AlertRadio1 = (RadioButton)findViewById(R.id.AlertRadio1);
        AlertRadio2 = (RadioButton)findViewById(R.id.AlertRadio2);
        StartTime = (TimePicker)findViewById(R.id.StartTimePicker);
        StartTime.setIs24HourView(true);
        StopTime = (TimePicker)findViewById(R.id.StopTimePicker);
        StopTime.setIs24HourView(true);
        AlertTime = (TimePicker)findViewById(R.id.AlertTimePicker);
        AlertTime.setIs24HourView(true);
        OKButton = (Button)findViewById(R.id.OKButton);
        OKButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            { OKButton_OnClick(view); }
        });
        CancelButton = (Button)findViewById(R.id.CancelButton);
        CancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            { CancelButton_OnClick(view); }
        });
    }

    private void OKButton_OnClick(View view)
    {
        try
        {
            String title = TitleText.getText().toString();
            if (title.equals("")) {
                Toast.makeText(this, "事件名称为空！", Toast.LENGTH_SHORT).show();
                return;
            }
            if (title.length() > 32) {
                Toast.makeText(this, "事件名称不得大于32个字符！", Toast.LENGTH_SHORT).show();
                return;
            }
            Row.SetTitle(title);

            String content = ContentText.getText().toString();
            if (content.length() > 256) {
                Toast.makeText(this, "备注不得大于256个字符！", Toast.LENGTH_SHORT).show();
                return;
            }
            Row.SetComment(content);

            int alerttype;
            if (AlertRadio0.isChecked())
                alerttype = 0;
            else if (AlertRadio1.isChecked())
                alerttype = 1;
            else
                alerttype = 2;
            Row.SetAlertType(alerttype);

            int starttime = StartTime.getCurrentHour() * 100 +
                    StartTime.getCurrentMinute();
            int stoptime = StopTime.getCurrentHour() * 100 +
                    StopTime.getCurrentMinute();
            if (starttime > stoptime) {
                Toast.makeText(this, "结束时间不得小于起始时间！", Toast.LENGTH_SHORT).show();
                return;
            }
            Row.SetStartTime(starttime);
            Row.SetEndTime(stoptime);

            int alerttime = AlertTime.getCurrentHour() * 100 +
                    AlertTime.getCurrentMinute();
            if (alerttype == 2 && starttime < alerttime) {
                Toast.makeText(this, "结束时间不得小于起始时间！", Toast.LENGTH_SHORT).show();
                return;
            }
            Row.SetAlertTime(alerttime);

            if (IsAdd)
                Manager.Add(Row);
            else
                Manager.UpdateAt(Index, Row);

            this.finish();
        }
        catch(Exception ex)
        {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void CancelButton_OnClick(View view)
    {
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        InitViews();

        Intent intent = this.getIntent();
        Manager = (RoutineManager)intent.getSerializableExtra("manager");
        Row = (NoteRow)intent.getSerializableExtra("row");
        Index = intent.getIntExtra("index", 0);
        IsAdd = intent.getBooleanExtra("isadd", false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.setting, menu);
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
