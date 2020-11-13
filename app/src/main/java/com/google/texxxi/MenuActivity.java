package com.google.texxxi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends Activity {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChild;

    private String name;
    private String pass;


    private String returnhour;
    private String returnmin;
    private String returnstart;
    private String returnarrive;


    private Button mnewbt;
    private Button msearchbt;
    private int roomflag = 0;

    private TextView menutv;

    private ListView listv;
    private ArrayAdapter<String> adapter;
    List<Object> Array = new ArrayList<Object>();

    ArrayList<String> usern = new ArrayList<String>(); //유저 네임 리스트
    ArrayList<String> starts = new ArrayList<String>();  // 출발지역 리스트
    ArrayList<String> arraives = new ArrayList<String>(); // 도착지역 리스트
    ArrayList<String> starth = new ArrayList<String>();  //출발 시 리스트
    ArrayList<String> startm = new ArrayList<String>();  // 출발 분 리스트

    private String result;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent intent = getIntent();
        name =intent.getExtras().getString("닉네임");
        pass =intent.getExtras().getString("비밀번호");

        initDatabase();
        databaseReference.child("유저").child(name).child("비밀번호").setValue(pass); //로그인 정보 데이터베이스에 저장

        mnewbt = (Button)findViewById(R.id.newbt);  // 방생성 버튼
        msearchbt = (Button) findViewById(R.id.searchbt); //방 검색 버튼

        menutv = (TextView)findViewById(R.id.menutv); //메뉴 텍스트뷰

        listv =(ListView)findViewById(R.id.roomlist); //방 리스트뷰
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
        listv.setAdapter(adapter); //리스트뷰에 넣을 어댑터 설정

        mnewbt.setOnClickListener(new View.OnClickListener() {   //방 생성 버튼
            @Override
            public void onClick(View view) {
                roomflag = 0; // 방생성은 플레그 0

                Intent intent1 = new Intent(MenuActivity.this, menu2Activity.class); // 메뉴 화면 -> 메뉴설정화면
                intent1.putExtra("닉네임", name);
                intent1.putExtra("방깃발", roomflag);

                startActivity(intent1); // 메뉴화면 시작
            }
        });

        msearchbt.setOnClickListener(new View.OnClickListener() {   //방 검색 버튼
            @Override
            public void onClick(View view) {
                roomflag = 1; // 방 검색은 플레그 1

                Intent intent2 = new Intent(MenuActivity.this, menu2Activity.class); // 메뉴 화면 -> 메뉴설정화면
                intent2.putExtra("닉네임", name);
                intent2.putExtra("방깃발", roomflag);

               // startActivity(intent1); // 메뉴화면 시작
                startActivityForResult(intent2, 1);
            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { // 인텐트로 방생성, 검색 버튼 눌렀다가 돌아왔을때 처리하는곳
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                // 방 검색버튼 누를시에만 여기로 돌아옴

                returnhour = data.getStringExtra("리턴시");
                returnmin = data.getStringExtra("리턴분");
                returnstart = data.getStringExtra("출발지리턴");
                returnarrive = data.getStringExtra("도착지리턴");

                // 데이터베이스에서 위에 가져온 정보를 토대로 검색한 결과를 리스트에 담아서 리스트뷰에 보여주기 ㄱㄱㄱㄱㄱㄱㄱ
                menutv.setText("방 찾기 기능 설정값  ( 출발지 / 도착지 / 출발시 : 출발분) \n  ->   "+returnstart+" / "+returnarrive+" / "+returnhour+" : "+returnmin);

                mReference = mDatabase.getReference("방").child("공통방"); // 변경값을 확인할 child 이름
                mReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        adapter.clear();

                        for (DataSnapshot i : dataSnapshot.getChildren()) {

                            String username = i.getKey(); //방-공통방의 키값 (유저 닉넴)
                         /*   String startspot = i.child(username).child("1출발지").getValue().toString();
                            String arrivespot = i.child(username).child("2도착지").getValue().toString();
                            String starthour = i.child(username).child("3출발시").getValue().toString();
                            String startmin = i.child(username).child("4출발분").getValue().toString();
/* 여기가 오류뜸 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                            result = "출발지: "+startspot+" / 도착지: "+arrivespot+" / 출발 시:분 "+starthour+":"+startmin;
                            usern.add(username);
                            starts.add(startspot);
                            arraives.add(arrivespot);
                            starth.add(starthour);
                            startm.add(startmin);*/
                            result = username + startspot;
                            Array.add(result); // array에 저장
                            adapter.add(result); //adapter에 저장


                        }

                        int l_size = usern.size();

                        for(int i=0; i<l_size; i++){

                            if(returnstart == starts.get(i)){
                                if(returnarrive == arraives.get(i)){

                                }
                            }



                        }





                        adapter.notifyDataSetChanged(); // 어댑터리스트 갱신
                        listv.setSelection(adapter.getCount() - 1);


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            } else if (resultCode == RESULT_CANCELED) {
                // 취소 버튼 클릭
            }
        }


    }
    private void initDatabase() {

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
        mChild = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mReference.addChildEventListener(mChild);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mReference.removeEventListener(mChild);
    }

}


