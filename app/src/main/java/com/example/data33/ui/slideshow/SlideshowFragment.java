package com.example.data33.ui.slideshow;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.data33.R;
import com.example.data33.navigation;
import com.example.data33.search;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SlideshowFragment extends Fragment {

    private ListView listView;

    private LISTVIEW_ADAPTER adapter;
    private FirebaseDatabase database;
    DrawerLayout mDrawerLayout;
    String uid="",name="",key="";
    public ArrayList<String> args;

    Context context;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        mDrawerLayout = (DrawerLayout)getActivity().findViewById(R.id.drawer_layout);

        navigation nav=new navigation();
        uid=nav.uid;
        context=container.getContext();
        listView = root.findViewById(R.id.listView);
        adapter = new LISTVIEW_ADAPTER();
        database = FirebaseDatabase.getInstance();
        listView.setAdapter(adapter);
        uid="p";
        database_run(uid);
        final ImageButton nav_btn = (ImageButton) root.findViewById(R.id.nav_btn);
        nav_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        final ImageButton btnSearch = (ImageButton) root.findViewById(R.id.search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSearch = new Intent(getActivity(), search.class);
                startActivity(intentSearch);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Intent intent = new Intent(context.getApplicationContext(), com.example.data33.moredata.class);
                /* putExtra??? ??? ?????? ?????? ??????, ????????? ?????? ????????? ?????? ??? */
                ArrayList<LISTVIEW_ITEM> data = adapter.listViewItemList;
                intent.putExtra("hospital_name", data.get(position).getTitle());
                intent.putExtra("disease_name", data.get(position).getDis());
                intent.putExtra("number_booking", data.get(position).getBook());
                intent.putExtra("vaccine_types", data.get(position).getVac());
                intent.putExtra("hospital_address", data.get(position).getAdd());
                intent.putExtra("vaccine_total", data.get(position).getAdd());

                startActivity(intent);
            }
        });
        return root;
    }
    private void database_run(final String uid) {
        database.getReference("BBS").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.clear();
                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                    //Toast.makeText(getContext(), messageData.child("customer").getChildren().child("uid").getValue().toString(), Toast.LENGTH_SHORT).show();

                    for(DataSnapshot tmpdata : messageData.child("customer").getChildren()){
                        String tmp=tmpdata.child("uid").getValue().toString();
                        if(tmp.equals(uid))
                        {
                            String title = messageData.child("hospital_name").getValue().toString();
                            String booking = messageData.child("number_booking").getValue().toString();
                            String diseaseName = messageData.child("disease_name").getValue().toString();
                            String vaccineTypes = messageData.child("vaccine_types").getValue().toString();
                            String address = messageData.child("hospital_address").getValue().toString();
                            String distance  = messageData.child("distance").getValue().toString();
                            String total  = messageData.child("vaccine_total").getValue().toString();

                            adapter.addItem(title,booking,diseaseName,vaccineTypes,address,distance,total);
                            break;
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                listView.setSelection(adapter.getCount() - 1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

/*
    public void database_run(final LISTVIEW_ADAPTER adapter, final FirebaseDatabase database, final ListView listView)
    {
        database.getReference("BBS").orderByChild("total").equalTo("1234").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // ???????????? ????????? ??? ?????? ???????????? ????????????????????? List ??? ?????????????????? ????????? ????????????.
                adapter.clear();
                for (DataSnapshot messageData : dataSnapshot.getChildren()) {

                    String name = messageData.child("hospital_title").getValue().toString();
                    String type = messageData.child("hospital_address").getValue().toString();
                    adapter.addItem(name,type);
                }
                // notifyDataSetChanged??? ???????????? ListView ????????? ??????
                adapter.notifyDataSetChanged();
                // ListView ??? ????????? ??????????????? ???????????? ??????
                listView.setSelection(adapter.getCount() - 1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}*/