package com.example.data33;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.data33.ui.slideshow.LISTVIEW_ADAPTER;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class search_result extends AppCompatActivity {

    private ListView listView;
    private TextView nobooking;
    private com.example.data33.ui.slideshow.LISTVIEW_ADAPTER adapter;
    private FirebaseDatabase database;
    DrawerLayout mDrawerLayout;
    String uid="",name="",key="";
    public ArrayList<String> args;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_slideshow);

        Intent intent=getIntent();
        String vaccine_name=intent.getStringExtra("vaccine_name");
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        navigation nav=new navigation();
        uid=nav.uid;
        listView = findViewById(R.id.listView);
        adapter = new com.example.data33.ui.slideshow.LISTVIEW_ADAPTER();
        database = FirebaseDatabase.getInstance();
        listView.setAdapter(adapter);
        database_search(vaccine_name);
        final ImageButton nav_btn = (ImageButton) findViewById(R.id.nav_btn);
        nav_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        final ImageButton btnSearch = (ImageButton) findViewById(R.id.search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSearch = new Intent( getApplicationContext(),search.class);
                startActivity(intentSearch);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), com.example.data33.moredata.class);
                /* putExtra의 첫 값은 식별 태그, 뒤에는 다음 화면에 넘길 값 */
                ArrayList<com.example.data33.ui.slideshow.LISTVIEW_ITEM> data = adapter.listViewItemList;
                intent.putExtra("hospital_name", data.get(position).getTitle());
                intent.putExtra("disease_name", data.get(position).getDis());
                intent.putExtra("number_booking", data.get(position).getBook());
                intent.putExtra("vaccine_types", data.get(position).getVac());
                intent.putExtra("hospital_address", data.get(position).getAdd());
                startActivity(intent);
            }
        });
    }
    public void database_search(final String vaccine_name)
    {
        database.getReference("BBS").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // 데이터를 읽어올 때 모든 데이터를 읽어오기때문에 List 를 초기화해주는 작업이 필요하다.
                adapter.clear();
                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                    if(messageData.child("vaccine_types").getValue().toString().equals(vaccine_name))
                    {
                        //String diseaseName = messageData.child("disease_name").getValue().toString();
                        //String vaccineTypes = messageData.child("vaccine_types").getValue().toString();
                        //String title = messageData.child("hospital_name").getValue().toString();

                        String title = messageData.child("hospital_name").getValue().toString();
                        String booking = String.valueOf(messageData.child("customer").getChildrenCount());
                        String diseaseName = messageData.child("disease_name").getValue().toString();
                        String vaccineTypes = messageData.child("vaccine_types").getValue().toString();
                        String address = messageData.child("hospital_address").getValue().toString();
                        String distance  = messageData.child("distance").getValue().toString();
                        String total  = messageData.child("vaccine_total").getValue().toString();

                        adapter.addItem(title,booking,diseaseName,vaccineTypes,address,distance,total);

                        /*
                        String booking = messageData.child("number_booking").getValue().toString();
                        String address = messageData.child("hospital_address").getValue().toString();
                        String distance = messageData.child("distance").getValue().toString();*/
                        /*---String title = messageData.child("hospital_name").getValue().toString();
                        String booking = messageData.child("number_booking").getValue().toString();
                        String diseaseName = messageData.child("disease_name").getValue().toString();
                        String vaccineTypes = messageData.child("vaccine_types").getValue().toString();
                        String address = messageData.child("hospital_address").getValue().toString();
                        String distance  = messageData.child("distance").getValue().toString();*/

                        Toast.makeText(getApplicationContext(), diseaseName+"\n"+vaccineTypes+"\n"+title, Toast.LENGTH_SHORT).show();
                    }
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
}