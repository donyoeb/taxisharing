package com.google.texxxi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;


public class loginActivity extends Activity {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChild;

    private Button newjoinbt;  //신규가입 버튼
    private Button joinbt; //로그인 버튼
    private EditText name; //닉네임
    private EditText pass; // 패스워드
    private String stname; // 닉네임 문자열
    private String stpass; // 패스워드 문자열
    private String mypass; // 데이터베이스에 저장된 내 비밀번호

    private ArrayList<String> Users_nick = new ArrayList<>();   //유저 저장을 위한 리스트
    private ArrayList<String> Users_pass = new ArrayList<>();   //유저 저장을 위한 리스트


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        name = (EditText) findViewById(R.id.name);
        pass = (EditText) findViewById(R.id.pass);

        Handler h = new Handler();

        initDatabase();

        mReference = mDatabase.getReference().child("유저");
        mReference.addValueEventListener(new ValueEventListener() { //처음 접속시 데이터베이스에 저장되어있는 유저 리스트 가져오기
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Users_nick.clear(); // 초기화
                Users_pass.clear();

                for (DataSnapshot i : dataSnapshot.getChildren()) {

                    String nick = i.getKey(); //닉네임 가져오기
                    String pass = i.child("비밀번호").getValue().toString(); // 비밀번호 문자열식으로 가져오기

                    Users_nick.add(nick); //리스트에 저장
                    Users_pass.add(pass);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        final Toast toast = Toast.makeText(loginActivity.this, "데이터베이스 정보 가져오는 중...", Toast.LENGTH_SHORT);
        new CountDownTimer(3000, 1000) {        //데이터베이스에서 정보 가져오는 시간동안 toast 보여주는 딜레이 걸어주기 / 초당 1000
            public void onTick(long millisUntilFinished) {
                toast.show();
            }

            public void onFinish() {
                toast.show();
            }
        }.start();


        h.postDelayed(new Runnable() { // 실행하는것도 딜레이처리 해주기

            public void run() { //3초 후 코드 실행됨

                newjoinbt = (Button) findViewById(R.id.btnlogin1); // 신규가입 버튼 클릭리스너
                newjoinbt.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        stname = name.getText().toString(); // 문자열화
                        stpass = pass.getText().toString();

                        if (stname.length() <= 0 || stpass.length() <= 0) {  //빈값이 넘어올때의 처리
                            Toast.makeText(loginActivity.this, "닉네임과 패스워드를 입력하세요.", Toast.LENGTH_SHORT).show();
                        }
                        else {

                            int flag = 0; // 데이터베이스 적힌 아이디가 있는지 없는지 판단

                            for (int i = 0; i < Users_nick.size(); i++) {
                                String n = Users_nick.get(i);
                                String p = Users_pass.get(i);

                                if (n.equals(stname)) { //데이터베이스에 내 아이디가 있는경우
                                    flag = 1; // 아이디는 있어서 깃발 세워주기 없으면 0

                                    Toast.makeText(loginActivity.this, "기존 사용자는 '로그인' 버튼을 눌러주세요 ", Toast.LENGTH_SHORT).show();
                                }
                            }
                            if (flag == 0) {

                                Intent intent1 = new Intent(loginActivity.this, MenuActivity.class); // 로그인화면 -> 메뉴화면
                                intent1.putExtra("닉네임", stname);
                                intent1.putExtra("비밀번호", stpass);

                                startActivity(intent1); // 메뉴화면 시작
                               // finish(); // 로그인 엑티비티 종료

                            }

                        }
                    }
                });

                joinbt = (Button) findViewById(R.id.btnlogin2); // 로그인버튼 누를시
                joinbt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        stname = name.getText().toString();
                        stpass = pass.getText().toString();

                        if (stname.length() <= 0 || stpass.length() <= 0) {//빈값이 넘어올때의 처리

                            Toast.makeText(loginActivity.this, "닉네임과 패스워드를 입력하세요.", Toast.LENGTH_SHORT).show();
                        }
                        else {

                            int flag = 0; //아이디 있는지 없는지 판단

                            for (int i = 0; i < Users_nick.size(); i++) {
                                String n = Users_nick.get(i);
                                String p = Users_pass.get(i);

                                if (n.equals(stname)) { //데이터베이스에 내 아이디가 있는경우
                                    flag = 1; // 아이디는 있어서 깃발 세워주기
                                    mypass = p;  //내 아이디의 비밀번호 가져오기

                                    if (mypass.equals(stpass)) {//가져온 비번과 입력한 비번이 같으면 로그인완료

                                        Intent intent2 = new Intent(loginActivity.this, MenuActivity.class); // 로그인화면 -> 메뉴 화면
                                        intent2.putExtra("닉네임", stname);
                                        intent2.putExtra("비밀번호", stpass);

                                        startActivity(intent2);
                                     //   finish();
                                    } else { //틀리면 로그인 x
                                        Toast.makeText(loginActivity.this, "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }
                            if (flag == 0) {
                                Toast.makeText(loginActivity.this, "등록된 정보가 없습니다.\n신규 로그인은 '신규가입' 버튼을 눌러주세요", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });
            }

        }, 3000); //정지할 시간이 1초라면 1000을 입력한다.
        // 딜레이 끝

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

