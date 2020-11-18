package com.google.texxxi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

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


    private room_list_adapter adapter = new room_list_adapter();

    ArrayList<String> usern = new ArrayList<String>(); //유저 네임 리스트
    ArrayList<String> starts = new ArrayList<String>();  // 출발지역 리스트
    ArrayList<String> arraives = new ArrayList<String>(); // 도착지역 리스트
    ArrayList<String> starth = new ArrayList<String>();  //출발 시 리스트
    ArrayList<String> startm = new ArrayList<String>();  // 출발 분 리스트

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


        mReference = mDatabase.getReference().child("방");
        mReference.addValueEventListener(new ValueEventListener() { //처음 들어오면 데이터베이스에 저장되어있는 유저 리스트 가져와서 나중에 비교할때 사용하게 하기
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                usern.clear(); //올때마다 클리어 ( 나중에 버튼하나 누르면 클리어하고 다시 받아오는 기능 추가)
                starts.clear();
                arraives.clear();
                starth.clear();
                startm.clear();

                for (DataSnapshot i : dataSnapshot.getChildren()) {

                    String u2 = i.getKey(); //유저정보 가져오기
                    String ss2 = i.child("1출발지").getValue().toString();
                    String as2 = i.child("2도착지").getValue().toString();
                    String sh2 = i.child("3출발시").getValue().toString();
                    String sm2 = i.child("4출발분").getValue().toString();

                    usern.add(u2);   //하나하나 다 리스트에 저장시켜둠 나중에 쓰기위해
                    starts.add(ss2);
                    arraives.add(as2);
                    starth.add(sh2);
                    startm.add(sm2);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });





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

                adapter = new room_list_adapter();
                listv.setAdapter(adapter); //리스트뷰에 넣을 어댑터 설정

                returnstart = data.getStringExtra("출발지리턴");
                returnarrive = data.getStringExtra("도착지리턴");
                returnhour = data.getStringExtra("리턴시");
                returnmin = data.getStringExtra("리턴분");

                int rh = Integer.parseInt(returnhour); //조건 적용을 위해 숫자로 변환
                int rm = Integer.parseInt(returnmin);

                // 데이터베이스에서 위에 가져온 정보를 토대로 검색한 결과를 리스트에 담아서 리스트뷰에 보여주기 ㄱㄱㄱㄱㄱㄱㄱ
                menutv.setText("방 찾기 기능 설정값  ( 출발지 / 도착지 / 출발시 : 출발분) \n  ->   " + returnstart + " / " + returnarrive + " / " + returnhour + " : " + returnmin);


                int l_size = usern.size();

                for (int i = 0; i < l_size; i++) {

                    int rh_2 = Integer.parseInt(starth.get(i)); // 데이터베이스에 저장된 i번째 시간
                    int rm_2 = Integer.parseInt(startm.get(i));  // i번째 분

                    if (returnstart.equals(starts.get(i)) && returnarrive.equals(arraives.get(i))) {

                        if(rh_2 == rh) {   //시간이 같을때만 분까지 비교
                            if (rm_2 >= rm){
                                adapter.addItem(starts.get(i), arraives.get(i), starth.get(i), startm.get(i)); //adapter에 저장
                            }
                        }
                        else if(rh_2 > rh){ // 시간이 더 크면 그냥 바로 확인
                            adapter.addItem(starts.get(i), arraives.get(i), starth.get(i), startm.get(i)); //adapter에 저장

                        }
                        else{

                        }
                    }

                }
                adapter.notifyDataSetChanged(); // 어댑터리스트 갱신
                listv.setSelection(adapter.getCount() - 1);


            } else if (resultCode == RESULT_CANCELED) {
                // 취소 버튼 클릭
            }
        }

//////////////리스트뷰 클릭이벤트부터 시작하면 됨
        listv.setOnItemClickListener(new AdapterView.OnItemClickListener() {    //리스트뷰 클릭이벤트

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long idinlist) {

                Toast.makeText(MenuActivity.this,"닉네임 : "+usern.get(position),Toast.LENGTH_SHORT).show();

            }
        });



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


