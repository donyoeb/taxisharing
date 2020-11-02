package com.google.texxxi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
                intent1.putExtra("방버튼", roomflag);

                startActivity(intent1); // 메뉴화면 시작

            }
        });

        msearchbt.setOnClickListener(new View.OnClickListener() {   //방 검색 버튼
            @Override
            public void onClick(View view) {
                roomflag = 1; // 방 검색은 플레그 1

                Intent intent1 = new Intent(MenuActivity.this, menu2Activity.class); // 메뉴 화면 -> 메뉴설정화면
                intent1.putExtra("닉네임", name);
                intent1.putExtra("방버튼", roomflag);

                startActivity(intent1); // 메뉴화면 시작
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


