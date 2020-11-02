package com.google.texxxi;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class menu2Activity extends Activity implements TimePicker.OnTimeChangedListener {

    private TextView ttv;  //타임 텍스트뷰
    private TimePicker tp; //타임 피커
    Calendar c; //캘린더

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu2);

        c = Calendar.getInstance(); // 캘린더로 현재 시간, 분 가져오기위해 설정
        ttv = (TextView) findViewById(R.id.timetextview);
        tp = (TimePicker) findViewById(R.id.timepicker);

        int hourofday = c.get(c.HOUR_OF_DAY);
        int minute = c.get(c.MINUTE);

        ttv.setText("현재 시간\n" + hourofday + ":" + minute);

        tp.setOnTimeChangedListener(this); // 시간, 분 선택

    }

    public void onTimeChanged(TimePicker view, int hourofday, int minute) {
        ttv.setText("설정된 시간\n" + hourofday + ":" + minute);  // 선택한 시간, 분  설정
    }
}