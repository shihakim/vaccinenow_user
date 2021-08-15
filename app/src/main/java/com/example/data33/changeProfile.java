package com.example.data33;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class changeProfile extends AppCompatActivity {
    String uid="T1Ro2C1bwhW3o9lgVK08Mg3aFig1";
    String key;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hospital_profile);
        database = FirebaseDatabase.getInstance();
        search(uid);
        final EditText editText_name = (EditText)findViewById(R.id.editText_name);
        final EditText editText_hospital_call = (EditText) findViewById(R.id.editText_hospital_call);


        final ImageButton nav_btn = (ImageButton) findViewById(R.id.nav_btn);
        nav_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(changeProfile.this, navigation.class);
                startActivity(intent);
            }
        });
        final ImageButton btnSearch = (ImageButton)findViewById(R.id.search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSearch = new Intent(changeProfile.this, search.class);
                startActivity(intentSearch);
            }
        });

        Button finish = (Button)findViewById(R.id.finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change_hospital(editText_name.getText().toString(),editText_hospital_call.getText().toString(),uid);
            }
        });
    }
    public void change_hospital(String hospital_name, String hospital_call, String uid){
        DatabaseReference query = database.getReference("info");

        Map<String, Object> map = new HashMap<>();
        map.put("hospital_name", hospital_name);
        map.put("hospital_call", hospital_call);
        map.put("uid", uid);
        map.put("type", "admin");


        query.child(key).setValue(map);
    }
    public void search(final String uid){
        database.getReference("info").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                    if(messageData.child("uid").getValue().toString().equals(uid)){
                        key = messageData.getKey();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
