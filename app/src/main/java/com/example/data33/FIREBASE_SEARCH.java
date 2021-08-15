/*package com.example.data33;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class FIREBASE_SEARCH extends AppCompatActivity {

    private EditText searchEdt;
    private Button searchBtn,pageNext;
    private ListView listView;
    private LISTVIEW_ADAPTER adapter;
    private FirebaseDatabase database;
    private Button button6;
    private Button button7;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.);

        searchEdt = (EditText) findViewById(R.id.searchEdt);
        searchBtn = (Button) findViewById(R.id.searchbtn);
        pageNext = (Button) findViewById(R.id.pageNext);
        listView = (ListView) findViewById(R.id.listView);
        button6 = (Button) findViewById(R.id.button6);

        adapter = new LISTVIEW_ADAPTER();
        database = FirebaseDatabase.getInstance();
        listView.setAdapter(adapter);

        // 자신이 얻은 Reference에 이벤트를 붙여줌
        // 데이터의 변화가 있을 때 출력해옴
        database_run();

        pageNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent = new Intent(FIREBASE_SEARCH.this,FIREBASE_ADD.class);
                //startActivity(intent);
            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search=searchEdt.getText().toString().trim();
                database_search(search);
                searchEdt.setText("");
                //검색해주는 함수 실행
            }
        });
    }
    public void firebase_add(String type,String name,String age,String total){
        database = FirebaseDatabase.getInstance();
        DatabaseReference query = database.getReference("info");
        String key = query.push().getKey();

        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("age", age);
        map.put("type", type);
        map.put("total",total);

        query.child(key).setValue(map);
    }
    public void database_run()
    {
        database.getReference("info").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // 데이터를 읽어올 때 모든 데이터를 읽어오기때문에 List 를 초기화해주는 작업이 필요하다.
                adapter.clear();
                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                    String name = messageData.child("name").getValue().toString();
                    String type = messageData.child("type").getValue().toString();
                    adapter.addItem(name,type);
                }
                // notifyDataSetChanged를 안해주면 ListView 갱신이 안됨
                adapter.notifyDataSetChanged();
                // ListView 의 위치를 마지막으로 보내주기 위함
                listView.setSelection(adapter.getCount() - 1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void database_search(final String searchWord)
    {
        database.getReference("info").orderByChild("name").equalTo(searchWord).orderByChild("total").startAt(1).limitToFirst(4).addValueEventListener(new ValueEventListener() {
            //        database.getReference("info").orderByChild("total").startAt(1).limit(4).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // 데이터를 읽어올 때 모든 데이터를 읽어오기때문에 List 를 초기화해주는 작업이 필요하다.
                adapter.clear();
                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                    String name = messageData.child("name").getValue().toString();
                    String type = messageData.child("type").getValue().toString();
                    adapter.addItem(name,type);

                    //info의 key가 name인 정보들 중 edittext의 값과 일치하는 정보를 가져옴.

                    //
                }
                // notifyDataSetChanged를 안해주면 ListView 갱신이 안됨
                adapter.notifyDataSetChanged();
                // ListView 의 위치를 마지막으로 보내주기 위함
                listView.setSelection(adapter.getCount() - 1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}*/