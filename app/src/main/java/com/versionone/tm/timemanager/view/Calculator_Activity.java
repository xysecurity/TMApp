package com.versionone.tm.timemanager.view;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

import com.versionone.tm.timemanager.R;
import com.versionone.tm.timemanager.tools.TimeIntervalUtils;

public class Calculator_Activity extends AppCompatActivity {
    private EditText SelectDate;
    private TextView ResultText;
    private Button CalculatorBtn;
    int selectDate;

//    private static final String FROM_RESULT_STR_PATTERN = "From %s : %s years %s months %s days has passed.";
//    private static final String UNTIL_RESULT_STR_PATTERN = "Until %s : %s years %s months %s days are left.";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        SelectDate = (EditText) findViewById(R.id.SelectDate);
        ResultText = (TextView) findViewById(R.id.ResultText);
        CalculatorBtn = (Button) findViewById(R.id.CalculatorBtn);
        SelectDate.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickDlg();
                    return true;
                }
                return false;
            }
        });

        SelectDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickDlg();
                }
            }
        });
        CalculatorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResultText.setText(TimeIntervalUtils.getTimeInterval(selectDate));
            }
        });

    }




    protected void showDatePickDlg(){

        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(Calculator_Activity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear++;
                Calculator_Activity.this.SelectDate.setText(monthOfYear+"/"+dayOfMonth + "/" +year);
                selectDate=year*10000+monthOfYear*100+dayOfMonth;
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }

}
