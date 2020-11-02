package com.google.texxxi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class menu2Activity extends Activity implements TimePicker.OnTimeChangedListener {

    private TextView ttv;  //타임 텍스트뷰
    private TimePicker tp; //타임 피커
    Calendar c; //캘린더
    private String name;
    private int mflag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu2);

        c = Calendar.getInstance(); // 캘린더로 현재 시간, 분 가져오기위해 설정
        ttv = (TextView) findViewById(R.id.timetextview);
        tp = (TimePicker) findViewById(R.id.timepicker);

        int hourofday = c.get(c.HOUR_OF_DAY);
        int minute = c.get(c.MINUTE);

        ttv.setText("현재 시간\n  " + hourofday + ":" + minute);

        tp.setOnTimeChangedListener(this); // 시간, 분 선택

        Intent intent = getIntent();
        name =intent.getExtras().getString("닉네임");
        mflag =intent.getExtras().getInt("방버튼");


    }

    public void onTimeChanged(TimePicker view, int hourofday, int minute) {
        ttv.setText("설정된 출발 시간\n         " + hourofday + ":" + minute);  // 선택한 시간, 분  설정
    }

    public void mOnClose(View v){ //확인버튼 클릭 이벤트

        if (mflag==0){ // 방 생성= 0

        }
        else if(mflag==1){ //방 검색 =1

        }
        else{

        }

        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        setResult(RESULT_OK, intent);

        //액티비티(팝업) 닫기
        finish();
    }


}