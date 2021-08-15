package com.example.data33.ui.home;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.data33.GpsTracker;
import com.example.data33.R;
import com.example.data33.moredata;
import com.example.data33.search;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;
import static java.lang.Math.asin;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private HomeViewModel homeViewModel;
    DrawerLayout mDrawerLayout;
    SupportMapFragment mMapFragment;

    /*TestFragment frag; //TestFragment 참조변수
*/
    FragmentManager manager;
    private ListView listView;
    private com.example.data33.ui.home.LISTVIEW_ADAPTER adapter;
    private FirebaseDatabase database;


    public double latitude = 37.56;
    public double longitude = 126.97;
    public double mylatitude = 37.56;
    public double mylongitude = 126.97;
    String myaddress;
    private GoogleMap mMap;
    private GpsTracker gpsTracker;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};


    Context context;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
               // textView.setText(s);
            }
        });


        listView = (ListView) root.findViewById(R.id.list);
        final Button stock = (Button) root.findViewById(R.id.stock);
        final Button button6 = (Button) root.findViewById(R.id.distance4);
        final Button button7 = (Button) root.findViewById(R.id.mapview);
        final LinearLayout frag_list=(LinearLayout) root.findViewById(R.id.frag_list);
        final LinearLayout frag_map=(LinearLayout) root.findViewById(R.id.frag_map);
        final ImageButton nav_btn = (ImageButton) root.findViewById(R.id.nav_btn);
        final ImageButton btnSearch = (ImageButton) root.findViewById(R.id.search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSearch = new Intent(getActivity(), search.class);
                startActivity(intentSearch);
            }
        });
        mDrawerLayout = (DrawerLayout)getActivity().findViewById(R.id.drawer_layout);
        nav_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        stock.setTextColor(Color.parseColor("#8FD8E4"));
        button6.setTextColor(Color.GRAY);
        button7.setTextColor(Color.GRAY);
        frag_list.setVisibility(View.VISIBLE);
        frag_map.setVisibility(View.GONE);

        stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stock.setTextColor(Color.parseColor("#8FD8E4"));
                button6.setTextColor(Color.GRAY);
                button7.setTextColor(Color.GRAY);
                frag_list.setVisibility(View.VISIBLE);
                frag_map.setVisibility(View.GONE);
            }
        });
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button6.setTextColor(Color.parseColor("#8FD8E4"));
                stock.setTextColor(Color.GRAY);
                button7.setTextColor(Color.GRAY);
                frag_list.setVisibility(View.VISIBLE);
                frag_map.setVisibility(View.GONE);

            }
        });
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button7.setTextColor(Color.parseColor("#8FD8E4"));
                button6.setTextColor(Color.GRAY);
                stock.setTextColor(Color.GRAY);
                frag_map.setVisibility(View.VISIBLE);
                frag_list.setVisibility(View.GONE);

            }
        });

        context=container.getContext();
        database = FirebaseDatabase.getInstance();
        adapter = new com.example.data33.ui.home.LISTVIEW_ADAPTER();
        listView.setAdapter(adapter);

        mMapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);

        // 자신이 얻은 Reference에 이벤트를 붙여줌
        // 데이터의 변화가 있을 때 출력해옴
        database_run();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Intent intent = new Intent(context.getApplicationContext(), com.example.data33.moredata.class);
                /* putExtra의 첫 값은 식별 태그, 뒤에는 다음 화면에 넘길 값 */
                ArrayList<LISTVIEW_ITEM> data = adapter.listViewItemList;
                intent.putExtra("hospital_name", data.get(position).getTitle());
                intent.putExtra("disease_name", data.get(position).getDis());
                intent.putExtra("number_booking", data.get(position).getBook());
                intent.putExtra("vaccine_types", data.get(position).getVac());
                intent.putExtra("hospital_address", data.get(position).getAdd());
                intent.putExtra("distance", data.get(position).getDist());
                intent.putExtra("total", data.get(position).getDist());
                //intent.putExtra("key",data.get(position).getKey());
                startActivity(intent);
            }
        });

        if (!checkLocationServicesStatus()) {

            showDialogForLocationServiceSetting();
        }else {

            checkRunTimePermission();
        }
        TextView location=(TextView)root.findViewById(R.id.location);
        gpsTracker = new GpsTracker(context);
        mylatitude = gpsTracker.getLatitude();
        mylongitude = gpsTracker.getLongitude();
        myaddress=getCurrentAddress(mylatitude, mylongitude);
        location.setText(myaddress);
        return root;
    }
    /*public void firebase_add(String type,String name,String age){
        database = FirebaseDatabase.getInstance();
        DatabaseReference query = database.getReference("info");
        String key = query.push().getKey();

        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("age", age);
        map.put("type", type);
        map.put("uid","example");

        query.child(key).setValue(map);
    }*/
    @Override
    public void onPause() {
        super.onPause();
        mMapFragment.onStop();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapFragment.onStop();
    }
    private void database_run() {
        database.getReference("BBS").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // 데이터를 읽어올 때 모든 데이터를 읽어오기때문에 List 를 초기화해주는 작업이 필요하다.
                adapter.clear();
                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                    String title = messageData.child("hospital_name").getValue().toString();
                    String booking = String.valueOf(messageData.child("customer").getChildrenCount());
                    String diseaseName = messageData.child("disease_name").getValue().toString();
                    String vaccineTypes = messageData.child("vaccine_types").getValue().toString();
                    String tmp_address = messageData.child("hospital_address").getValue().toString();
                    String total = messageData.child("vaccine_total").getValue().toString();



                    latitude = getLocationFromAddress(context,tmp_address).latitude;
                    longitude = getLocationFromAddress(context,tmp_address).longitude;
                    String distance=String.valueOf(getDistance(mylatitude,mylongitude,latitude,longitude)/1000)+"km";

                    setMap(mMap,tmp_address,title);
                    adapter.addItem(title,booking,diseaseName,vaccineTypes,tmp_address,distance,total);

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


    public int getDistance(Double lat1, Double lon1, Double lat2 ,Double lon2 ){
        double R = 6372.8 * 1000;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.pow(sin(dLat / 2),2.0) + Math.pow(sin(dLon / 2),2.0) * cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2));
        double c = 2 * asin(sqrt(a));
        return Integer.parseInt(String.valueOf(Math.round(R * c)));
    }

    public void setMap(final GoogleMap googleMap, final String address, final String title){

        latitude = getLocationFromAddress(context,address).latitude;
        longitude = getLocationFromAddress(context,address).longitude;

        mMap = googleMap;
        LatLng SEOUL = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(SEOUL);
        markerOptions.title(title);
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 10));
    }
    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
// May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }
    @Override
    public void onMapReady(final GoogleMap googleMap) {

        mMap = googleMap;
        LatLng MapLoc = new LatLng(latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MapLoc, 15));
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if ( check_result ) {

                //위치 값을 가져올 수 있음
                ;
            }
            else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[1])) {

                    Toast.makeText(context, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();


                }else {

                    Toast.makeText(context, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();

                }
            }

        }
    }

    void checkRunTimePermission(){

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음



        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(context, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }


    public String getCurrentAddress( double latitude, double longitude) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(context, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(context, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }



        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(context, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        }

        Address address = addresses.get(0);
        return address.getAddressLine(0).toString()+"\n";

    }


    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }

                break;
        }
    }


    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

}
