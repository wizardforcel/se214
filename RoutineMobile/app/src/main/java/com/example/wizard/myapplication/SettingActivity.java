package com.example.wizard.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
    private Button StartTimeButton;
    private Button StopTimeButton;
    private Button AlertTypeButton;
    private Button AlertTimeButton;
    private Button OKButton;
    private Button CancelButton;
    private AlertDialog StartTimeDialog;
    private TimePicker StartTimePicker;
    private AlertDialog StopTimeDialog;
    private TimePicker StopTimePicker;
    private AlertDialog AlertTypeDialog;
    private AlertDialog AlertTimeDialog;
    private TimePicker AlertTimePicker;

    private RoutineManager Manager;
    private NoteRow Row;
    private int Index;
    private boolean IsAdd;

    private int AlertType;
    private int StartTimeHr;
    private int StartTimeMin;
    private int StopTimeHr;
    private int StopTimeMin;
    private int AlertTimeHr;
    private int AlertTimeMin;

    private void InitViews()
    {
        TitleText = (EditText)findViewById(R.id.TitleText);

        ContentText = (EditText)findViewById(R.id.ContentText);

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

        StartTimeButton = (Button)findViewById(R.id.StartTimeButton);
        StartTimeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            { StartTimeButton_OnClick(view); }
        });

        StopTimeButton = (Button)findViewById(R.id.StopTimeButton);
        StopTimeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            { StopTimeButton_OnClick(view); }
        });

        AlertTypeButton = (Button)findViewById(R.id.AlertTypeButton);
        AlertTypeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            { AlertTypeButton_OnClick(view); }
        });

        AlertTimeButton = (Button)findViewById(R.id.AlertTimeButton);
        AlertTimeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            { AlertTimeButton_OnClick(view); }
        });

        StartTimePicker = new TimePicker(this);
        StartTimePicker.setIs24HourView(true);
        StartTimePicker.setCurrentHour(0);
        StartTimePicker.setCurrentMinute(0);

        StartTimeDialog = new AlertDialog.Builder(this).setTitle("请输入开始时间")
                .setView(StartTimePicker)
                .setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    { StartTimeDialogOKButton_OnClick(); }
                })
                .setNegativeButton("取消", null)
                .create();

        StopTimePicker = new TimePicker(this);
        StopTimePicker.setIs24HourView(true);
        StopTimePicker.setCurrentHour(0);
        StopTimePicker.setCurrentMinute(0);

        StopTimeDialog = new AlertDialog.Builder(this).setTitle("请输入结束时间")
                .setView(StopTimePicker)
                .setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    { StopTimeDialogOKButton_OnClick(); }
                })
                .setNegativeButton("取消", null)
                .create();

        AlertTimePicker = new TimePicker(this);
        AlertTimePicker.setIs24HourView(true);
        AlertTimePicker.setCurrentHour(0);
        AlertTimePicker.setCurrentMinute(0);

        AlertTimeDialog = new AlertDialog.Builder(this).setTitle("请输入提醒时间")
                .setView(AlertTimePicker)
                .setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    { AlertTimeDialogOKButton_OnClick(); }
                })
                .setNegativeButton("取消", null)
                .create();

        AlertTypeDialog = new AlertDialog.Builder(this).setTitle("请输入提醒方式")
                .setSingleChoiceItems(new String[] {"无", "提前一天", "当天"}, AlertType,
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    { AlertTypeDialogOKButton_OnClick(dialog, which); }
                })
                .setNegativeButton("取消", null)
                .create();
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

            Row.SetAlertType(AlertType);

            int starttime = StartTimeHr * 100 + StartTimeMin;
            int stoptime = StopTimeHr * 100 + StopTimeMin;
            if (starttime > stoptime) {
                Toast.makeText(this, "结束时间不得小于起始时间！", Toast.LENGTH_SHORT).show();
                return;
            }
            Row.SetStartTime(starttime);
            Row.SetEndTime(stoptime);

            int alerttime = AlertTimeHr * 100 + AlertTimeMin;
            if (AlertType == 2 && starttime < alerttime) {
                Toast.makeText(this, "结束时间不得小于起始时间！", Toast.LENGTH_SHORT).show();
                return;
            }
            Row.SetAlertTime(alerttime);

            if (IsAdd)
                Manager.Add(this, Row);
            else
                Manager.UpdateAt(this, Index, Row);

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

    private void StartTimeButton_OnClick(View view)
    {
        StartTimePicker.setCurrentHour(StartTimeHr);
        StartTimePicker.setCurrentMinute(StartTimeMin);
        StartTimeDialog.show();
    }

    private void StopTimeButton_OnClick(View view)
    {
        StopTimePicker.setCurrentHour(StopTimeHr);
        StopTimePicker.setCurrentMinute(StopTimeMin);
        StopTimeDialog.show();
    }

    private void AlertTypeButton_OnClick(View view)
    {
        AlertTypeDialog.show();
    }

    private void AlertTimeButton_OnClick(View view)
    {
        AlertTimePicker.setCurrentHour(AlertTimeHr);
        AlertTimePicker.setCurrentMinute(AlertTimeMin);
        AlertTimeDialog.show();
    }

    private void StartTimeDialogOKButton_OnClick()
    {
        StartTimeHr = StartTimePicker.getCurrentHour();
        StartTimeMin = StartTimePicker.getCurrentMinute();
        StartTimeButton.setText(String.format("%02d:%02d", StartTimeHr, StartTimeMin));
    }

    private void StopTimeDialogOKButton_OnClick()
    {
        StopTimeHr = StopTimePicker.getCurrentHour();
        StopTimeMin = StopTimePicker.getCurrentMinute();
        StopTimeButton.setText(String.format("%02d:%02d", StopTimeHr, StopTimeMin));
    }

    private void AlertTimeDialogOKButton_OnClick()
    {
        AlertTimeHr = AlertTimePicker.getCurrentHour();
        AlertTimeMin = AlertTimePicker.getCurrentMinute();
        AlertTimeButton.setText(String.format("%02d:%02d", AlertTimeHr, AlertTimeMin));
    }

    private void AlertTypeDialogOKButton_OnClick(DialogInterface dialog, int which)
    {
        AlertType = which;
        AlertTypeButton.setText(new String[] {"无", "提前一天", "当天"}[which]);
        dialog.dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        InitViews();

        Intent intent = this.getIntent();
        Manager = (RoutineManager) intent.getSerializableExtra("manager");
        Row = (NoteRow) intent.getSerializableExtra("row");
        Index = intent.getIntExtra("index", 0);
        IsAdd = intent.getBooleanExtra("isadd", false);

        TitleText.setText(Row.GetTitle());
        ContentText.setText(Row.GetComment());
        int starttime = Row.GetStartTime();
        StartTimeMin = starttime % 100;
        StartTimeHr = starttime / 100;
        StartTimeButton.setText(String.format("%02d:%02d", StartTimeHr, StartTimeMin));
        int stoptime = Row.GetEndTime();
        StopTimeMin = stoptime % 100;
        StopTimeHr = stoptime / 100;
        StopTimeButton.setText(String.format("%02d:%02d", StopTimeHr, StopTimeMin));
        int alerttime = Row.GetAlertTime();
        AlertTimeMin = alerttime % 100;
        AlertTimeHr = alerttime / 100;
        AlertTimeButton.setText(String.format("%02d:%02d", AlertTimeHr, AlertTimeMin));
        AlertType = Row.GetAlertType();
        AlertTypeButton.setText(new String[] {"无", "提前一天", "当天"}[AlertType]);
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
