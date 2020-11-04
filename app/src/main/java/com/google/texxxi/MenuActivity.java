package com.google.texxxi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MenuActivity extends Activity {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChild;

    private String name;
    private String pass;

    private Button mnewbt;
    private Button msearchbt;
    private int roomflag = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent intent = getIntent();
        name =intent.getExtras().getString("닉네임");
        pass =intent.getExtras().getString("비밀번호");

        initDatabase();
        databaseReference.child("유저").child(name).child("비밀번호").setValue(pass); //로그인 정보 데이터베이스에 저장

        mnewbt = (Button)findViewById(R.id.newbt);
        msearchbt = (Button) findViewById(R.id.searchbt);

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

                String returnhour = data.getStringExtra("리턴시");
                String returnmin = data.getStringExtra("리턴분");
                String returnstart = data.getStringExtra("출발지리턴");
                String returnarrive = data.getStringExtra("도착지리턴");

                // 데이터베이스에서 위에 가져온 정보를 토대로 검색한 결과를 리스트에 담아서 리스트뷰에 보여주기 ㄱㄱㄱㄱㄱㄱㄱ

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


