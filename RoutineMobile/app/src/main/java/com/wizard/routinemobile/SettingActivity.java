package com.wizard.routinemobile;

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
import android.widget.TimePicker;
import android.widget.Toast;

import com.wizard.routinemobile.utility.NoteRow;
import com.wizard.routinemobile.utility.RoutineManager;

public class SettingActivity extends ActionBarActivity {

    private EditText titleText;
    private EditText contentText;
    private Button startTimeButton;
    private Button stopTimeButton;
    private Button alertTypeButton;
    private Button alertTimeButton;
    private Button okButton;
    private Button cancelButton;
    private AlertDialog startTimeDialog;
    private TimePicker startTimePicker;
    private AlertDialog stopTimeDialog;
    private TimePicker stopTimePicker;
    private AlertDialog alertTypeDialog;
    private AlertDialog alertTimeDialog;
    private TimePicker alertTimePicker;

    private RoutineManager manager;
    private NoteRow row;
    private int index;
    private boolean isAdd;

    private int alertType;
    private int startTimeHr;
    private int startTimeMin;
    private int stopTimeHr;
    private int stopTimeMin;
    private int alertTimeHr;
    private int alertTimeMin;

    private void initViews()
    {
        titleText = (EditText)findViewById(R.id.titleText);

        contentText = (EditText)findViewById(R.id.contentText);

        okButton = (Button)findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                okButton_Click(view);
            }
        });

        cancelButton = (Button)findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelButton_Click(view);
            }
        });

        startTimeButton = (Button)findViewById(R.id.startTimeButton);
        startTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimeButton_Click(view);
            }
        });

        stopTimeButton = (Button)findViewById(R.id.stopTimeButton);
        stopTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopTimeButton_Click(view);
            }
        });

        alertTypeButton = (Button)findViewById(R.id.alertTypeButton);
        alertTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertTypeButton_Click(view);
            }
        });

        alertTimeButton = (Button)findViewById(R.id.alertTimeButton);
        alertTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertTimeButton_Click(view);
            }
        });

        startTimePicker = new TimePicker(this);
        startTimePicker.setIs24HourView(true);
        startTimePicker.setCurrentHour(0);
        startTimePicker.setCurrentMinute(0);

        startTimeDialog = new AlertDialog.Builder(this).setTitle("请输入开始时间")
                .setView(startTimePicker)
                .setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    { startTimeDialogOkButton_Click(); }
                })
                .setNegativeButton("取消", null)
                .create();

        stopTimePicker = new TimePicker(this);
        stopTimePicker.setIs24HourView(true);
        stopTimePicker.setCurrentHour(0);
        stopTimePicker.setCurrentMinute(0);

        stopTimeDialog = new AlertDialog.Builder(this).setTitle("请输入结束时间")
                .setView(stopTimePicker)
                .setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    { stopTimeDialogOkButton_Click(); }
                })
                .setNegativeButton("取消", null)
                .create();

        alertTimePicker = new TimePicker(this);
        alertTimePicker.setIs24HourView(true);
        alertTimePicker.setCurrentHour(0);
        alertTimePicker.setCurrentMinute(0);

        alertTimeDialog = new AlertDialog.Builder(this).setTitle("请输入提醒时间")
                .setView(alertTimePicker)
                .setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    { alertTimeDialogOkButton_Click(); }
                })
                .setNegativeButton("取消", null)
                .create();

        alertTypeDialog = new AlertDialog.Builder(this).setTitle("请输入提醒方式")
                .setSingleChoiceItems(new String[] {"无", "提前一天", "当天"}, alertType,
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    { alertTypeDialogOkButton_Click(dialog, which); }
                })
                .setNegativeButton("取消", null)
                .create();
    }

    private void okButton_Click(View view)
    {
        try
        {
            String title = titleText.getText().toString();
            if (title.equals("")) {
                Toast.makeText(this, "事件名称为空！", Toast.LENGTH_SHORT).show();
                return;
            }
            if (title.length() > 32) {
                Toast.makeText(this, "事件名称不得大于32个字符！", Toast.LENGTH_SHORT).show();
                return;
            }
            row.setTitle(title);

            String content = contentText.getText().toString();
            if (content.length() > 256) {
                Toast.makeText(this, "备注不得大于256个字符！", Toast.LENGTH_SHORT).show();
                return;
            }
            row.setComment(content);

            row.setAlertType(alertType);

            int starttime = startTimeHr * 100 + startTimeMin;
            int stoptime = stopTimeHr * 100 + stopTimeMin;
            if (starttime > stoptime) {
                Toast.makeText(this, "结束时间不得小于起始时间！", Toast.LENGTH_SHORT).show();
                return;
            }
            row.setStartTime(starttime);
            row.setEndTime(stoptime);

            int alerttime = alertTimeHr * 100 + alertTimeMin;
            if (alertType == 2 && starttime < alerttime) {
                Toast.makeText(this, "结束时间不得小于起始时间！", Toast.LENGTH_SHORT).show();
                return;
            }
            row.setAlertTime(alerttime);

            if (isAdd)
                manager.add(this, row);
            else
                manager.updateAt(this, index, row);

            this.finish();
        }
        catch(Exception ex)
        {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void cancelButton_Click(View view)
    {
        this.finish();
    }

    private void startTimeButton_Click(View view)
    {
        startTimePicker.setCurrentHour(startTimeHr);
        startTimePicker.setCurrentMinute(startTimeMin);
        startTimeDialog.show();
    }

    private void stopTimeButton_Click(View view)
    {
        stopTimePicker.setCurrentHour(stopTimeHr);
        stopTimePicker.setCurrentMinute(stopTimeMin);
        stopTimeDialog.show();
    }

    private void alertTypeButton_Click(View view)
    {
        alertTypeDialog.show();
    }

    private void alertTimeButton_Click(View view)
    {
        alertTimePicker.setCurrentHour(alertTimeHr);
        alertTimePicker.setCurrentMinute(alertTimeMin);
        alertTimeDialog.show();
    }

    private void startTimeDialogOkButton_Click()
    {
        startTimeHr = startTimePicker.getCurrentHour();
        startTimeMin = startTimePicker.getCurrentMinute();
        startTimeButton.setText(String.format("%02d:%02d", startTimeHr, startTimeMin));
    }

    private void stopTimeDialogOkButton_Click()
    {
        stopTimeHr = stopTimePicker.getCurrentHour();
        stopTimeMin = stopTimePicker.getCurrentMinute();
        stopTimeButton.setText(String.format("%02d:%02d", stopTimeHr, stopTimeMin));
    }

    private void alertTimeDialogOkButton_Click()
    {
        alertTimeHr = alertTimePicker.getCurrentHour();
        alertTimeMin = alertTimePicker.getCurrentMinute();
        alertTimeButton.setText(String.format("%02d:%02d", alertTimeHr, alertTimeMin));
    }

    private void alertTypeDialogOkButton_Click(DialogInterface dialog, int which)
    {
        alertType = which;
        alertTypeButton.setText(new String[]{"无", "提前一天", "当天"}[which]);
        dialog.dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initViews();

        Intent intent = this.getIntent();
        manager = (RoutineManager) intent.getSerializableExtra("manager");
        row = (NoteRow) intent.getSerializableExtra("row");
        index = intent.getIntExtra("index", 0);
        isAdd = intent.getBooleanExtra("isadd", false);

        titleText.setText(row.getTitle());
        contentText.setText(row.getComment());
        int starttime = row.getStartTime();
        startTimeMin = starttime % 100;
        startTimeHr = starttime / 100;
        startTimeButton.setText(String.format("%02d:%02d", startTimeHr, startTimeMin));
        int stoptime = row.getEndTime();
        stopTimeMin = stoptime % 100;
        stopTimeHr = stoptime / 100;
        stopTimeButton.setText(String.format("%02d:%02d", stopTimeHr, stopTimeMin));
        int alerttime = row.getAlertTime();
        alertTimeMin = alerttime % 100;
        alertTimeHr = alerttime / 100;
        alertTimeButton.setText(String.format("%02d:%02d", alertTimeHr, alertTimeMin));
        alertType = row.getAlertType();
        alertTypeButton.setText(new String[]{"无", "提前一天", "当天"}[alertType]);
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
        // as you specify at parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
