package com.google.texxxi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class menu2Activity extends Activity implements TimePicker.OnTimeChangedListener {

    private TextView ttv;  //타임 텍스트뷰
    private TimePicker tp; //타임 피커
    Calendar c; //캘린더

    private Spinner stspot; //스피너 변수
    private Spinner arspot;
    private String startspot; // 스피너 값 저장 변수
    private String arrivespot;

    private String name;
    private int mflag;
    private int hour;
    private int min;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu2);

        /////////////////////////////////////////////////////////////////////////////  시간 관련
        Intent intent = getIntent();
        name =intent.getExtras().getString("닉네임");
        mflag =intent.getExtras().getInt("방깃발"); // 방 생성(=0), 방검색(=1)

        c = Calendar.getInstance(); // 캘린더로 현재 시간, 분 가져오기위해 설정
        ttv = (TextView) findViewById(R.id.timetextview);    // 메세지정보
        tp = (TimePicker) findViewById(R.id.timepicker); //시간정보

        int nowhour = c.get(c.HOUR_OF_DAY);
        int nowmin = c.get(c.MINUTE);

        hour = nowhour; // 리턴할 시
        min = nowmin; // 리턴할 분

        ttv.setText("현재 시간\n  " + nowhour + ":" + nowmin); // 현재 시간 출력


        /////////////////////////////////////////////////////////////////////  출발,도착지점 관련

        stspot = (Spinner)findViewById(R.id.startspot);  //출발지점
        arspot = (Spinner)findViewById(R.id.arrivespot); // 도착지점

        stspot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                switch(i){   //출발지점 스피너 선택 값 설정
                    case 0:
                        startspot = "두정역";
                        break;
                    case 1:
                        startspot = "천안역";
                        break;
                    case 2:
                        startspot = "야우리";
                        break;
                    case 3:
                        startspot = "상명대";
                        break;
                    case 4:
                        startspot = "안서동보";
                        break;

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        arspot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                switch(i){   //도착지점 스피너 선택 값 설정
                    case 0:
                        arrivespot = "두정역";
                        break;
                    case 1:
                        arrivespot = "천안역";
                        break;
                    case 2:
                        arrivespot = "야우리";
                        break;
                    case 3:
                        arrivespot = "상명대";
                        break;
                    case 4:
                        arrivespot = "안서동보";
                        break;

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


    ////////////////////////////////////////////////////////////////////////

        tp.setOnTimeChangedListener(this); // 시간, 분 선택 함수로 가기

    }

    public void onTimeChanged(TimePicker view, int hourofday, int minute) {
        ttv.setText("설정된 출발 시간\n         " + hourofday + ":" + minute);  // 선택한 시간, 분  설정
        hour = hourofday; // 리턴할 시
        min = minute; // 리턴할 분

    }

    public void mOnClose1(View v){ //확인버튼 클릭 이벤트

        if(startspot == arrivespot){

            Toast.makeText(menu2Activity.this,"출발, 도착지점을 확인해주세요.",Toast.LENGTH_SHORT).show();
            return;
        }
        else{

            if (mflag==0){ // 방 생성  flag= 0

                String h = String.valueOf(hour);
                String m = String.valueOf(min);

                Intent intent1 = new Intent(menu2Activity.this, MainActivity.class);  //팝업메뉴 -> 메인(지도)
                intent1.putExtra("닉네임",name);
                intent1.putExtra("출발시",h);
                intent1.putExtra("출발분",m);
                intent1.putExtra("출발지",startspot);
                intent1.putExtra("도착지",arrivespot);

                startActivity(intent1);
                finish();


            }
            else { //방 검색 flag = 1

                String h = String.valueOf(hour);
                String m = String.valueOf(min);

                //데이터 다시 전 단계로 전달하기
                Intent intent2 = new Intent();
                intent2.putExtra("리턴시",h);
                intent2.putExtra("리턴분",m);
                intent2.putExtra("출발지리턴",startspot);
                intent2.putExtra("도착지리턴",arrivespot);

                setResult(RESULT_OK, intent2);
                finish();

            }


        }

    }

    public void mOnClose2(View v) { //취소버튼 클릭이벤트
        finish();
    } // 취소버튼 클릭시 그냥 닫기


}