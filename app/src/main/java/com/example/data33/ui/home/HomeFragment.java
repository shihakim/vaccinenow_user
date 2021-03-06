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

    /*TestFragment frag; //TestFragment ????????????
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

        // ????????? ?????? Reference??? ???????????? ?????????
        // ???????????? ????????? ?????? ??? ????????????
        database_run();
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
                // ???????????? ????????? ??? ?????? ???????????? ????????????????????? List ??? ?????????????????? ????????? ????????????.
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

            // ?????? ????????? PERMISSIONS_REQUEST_CODE ??????, ????????? ????????? ???????????? ??????????????????

            boolean check_result = true;


            // ?????? ???????????? ??????????????? ???????????????.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if ( check_result ) {

                //?????? ?????? ????????? ??? ??????
                ;
            }
            else {
                // ????????? ???????????? ????????? ?????? ????????? ??? ?????? ????????? ??????????????? ?????? ???????????????.2 ?????? ????????? ????????????.

                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[1])) {

                    Toast.makeText(context, "???????????? ?????????????????????. ?????? ?????? ???????????? ???????????? ??????????????????.", Toast.LENGTH_LONG).show();


                }else {

                    Toast.makeText(context, "???????????? ?????????????????????. ??????(??? ??????)?????? ???????????? ???????????? ?????????. ", Toast.LENGTH_LONG).show();

                }
            }

        }
    }

    void checkRunTimePermission(){

        //????????? ????????? ??????
        // 1. ?????? ???????????? ????????? ????????? ???????????????.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. ?????? ???????????? ????????? ?????????
            // ( ??????????????? 6.0 ?????? ????????? ????????? ???????????? ???????????? ????????? ?????? ????????? ?????? ???????????????.)


            // 3.  ?????? ?????? ????????? ??? ??????



        } else {  //2. ????????? ????????? ????????? ?????? ????????? ????????? ????????? ???????????????. 2?????? ??????(3-1, 4-1)??? ????????????.

            // 3-1. ???????????? ????????? ????????? ??? ?????? ?????? ????????????
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[0])) {

                // 3-2. ????????? ???????????? ?????? ?????????????????? ???????????? ????????? ????????? ???????????? ????????? ????????????.
                Toast.makeText(context, "??? ?????? ??????????????? ?????? ?????? ????????? ???????????????.", Toast.LENGTH_LONG).show();
                // 3-3. ??????????????? ????????? ????????? ?????????. ?????? ????????? onRequestPermissionResult?????? ???????????????.
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                // 4-1. ???????????? ????????? ????????? ??? ?????? ?????? ???????????? ????????? ????????? ?????? ?????????.
                // ?????? ????????? onRequestPermissionResult?????? ???????????????.
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }


    public String getCurrentAddress( double latitude, double longitude) {

        //????????????... GPS??? ????????? ??????
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7);
        } catch (IOException ioException) {
            //???????????? ??????
            Toast.makeText(context, "???????????? ????????? ????????????", Toast.LENGTH_LONG).show();
            return "???????????? ????????? ????????????";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(context, "????????? GPS ??????", Toast.LENGTH_LONG).show();
            return "????????? GPS ??????";

        }



        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(context, "?????? ?????????", Toast.LENGTH_LONG).show();
            return "?????? ?????????";

        }

        Address address = addresses.get(0);
        return address.getAddressLine(0).toString()+"\n";

    }


    //??????????????? GPS ???????????? ?????? ????????????
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("?????? ????????? ????????????");
        builder.setMessage("?????? ???????????? ???????????? ?????? ???????????? ???????????????.\n"
                + "?????? ????????? ???????????????????");
        builder.setCancelable(true);
        builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
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

                //???????????? GPS ?????? ???????????? ??????
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d("@@@", "onActivityResult : GPS ????????? ?????????");
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
