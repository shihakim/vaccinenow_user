package com.example.data33.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.data33.R;

import java.util.ArrayList;

public class LISTVIEW_ADAPTER extends BaseAdapter {

    private TextView title;
    private TextView booking;
    private TextView diseasename;
    private TextView vaccinename;
    private TextView distance;
    private TextView address;
    private TextView total;

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    public ArrayList<LISTVIEW_ITEM> listViewItemList = new ArrayList<LISTVIEW_ITEM>();

    // ListViewAdapter의 생성자
    public LISTVIEW_ADAPTER() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴
    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        title = (TextView) convertView.findViewById(R.id.title);
        booking = (TextView) convertView.findViewById(R.id.total2);
        diseasename = (TextView) convertView.findViewById(R.id.diseasename);
        vaccinename = (TextView) convertView.findViewById(R.id.vaccinename);
        address = (TextView) convertView.findViewById(R.id.distance);
        distance = (TextView) convertView.findViewById(R.id.distance1);
        total = (TextView) convertView.findViewById(R.id.total2);

        LISTVIEW_ITEM LISTVIEWITEM = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        title.setText(LISTVIEWITEM.getTitle());
        booking.setText(LISTVIEWITEM.getBook());
        diseasename.setText(LISTVIEWITEM.getDis());
        vaccinename.setText(LISTVIEWITEM.getVac());
        address.setText(LISTVIEWITEM.getAdd());
        distance.setText(LISTVIEWITEM.getDist());
        total.setText(LISTVIEWITEM.getTotal());

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 지정한 위치(position)에 있는 데이터 리턴
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    // 아이템 데이터 추가를 위한 함수.
    public void addItem(String title, String booking,String diseasename,String vaccinename,String address,String distance,String total) {
        LISTVIEW_ITEM item = new LISTVIEW_ITEM();

        item.setTitle(title);
        item.setBook(booking);
        item.setDis(diseasename);
        item.setVac(vaccinename);
        item.setAdd(address);
        item.setDist(distance);
        item.setTotal(total);

        listViewItemList.add(0,item);
    }

    public void clear(){
        listViewItemList.clear();
    }
}