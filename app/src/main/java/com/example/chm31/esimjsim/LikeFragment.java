package com.example.chm31.esimjsim;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class LikeFragment extends Fragment implements OnMapReadyCallback {
    private Geocoder geocoder;
    SharedPreferences pref;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private SimpleAdapter adapter = null;
    private List<HashMap<String,String>> infoList = null;

    String ID;
    String loginType;
    String addr,title,tel,roadAddr,category,link,description;
    ArrayList<String> address_list = new ArrayList<>();
    ArrayList<String> place_list = new ArrayList<>();
    private MapView mapView = null;

    public LikeFragment(){

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_like, container, false);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        pref = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        ID = pref.getString("id","");
        loginType = pref.getString("login","");

        StrictMode.enableDefaults();

        final ListView listView = (ListView)rootView.findViewById(R.id.listview);
        infoList = new ArrayList<HashMap<String, String>>();

        final String[] from = new String[]{"title","tel","addr"};
        final int[] to = new int[]{R.id.listviewdata1,R.id.listviewdata2,R.id.listviewdata3};
        StrictMode.enableDefaults();

        final DatabaseReference list = databaseReference.child("member").child(loginType).child(ID).child("like");
        list.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                address_list.clear();
                place_list.clear();
                infoList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    addr= snapshot.child("addr").getValue(String.class);
                    tel= snapshot.child("tel").getValue(String.class);
                    title= snapshot.child("title").getValue(String.class);
                    roadAddr = snapshot.child("roadAddr").getValue(String.class);
                    link = snapshot.child("link").getValue(String.class);
                    category = snapshot.child("category").getValue(String.class);
                    description = snapshot.child("description").getValue(String.class);

                    HashMap<String, String> infoMap = new HashMap<String, String>();

                    address_list.add(addr);
                    place_list.add(title);
                    infoMap.put("addr", addr);
                    infoMap.put("tel", tel);
                    infoMap.put("title", title);
                    infoMap.put("roadAddr",roadAddr);
                    infoMap.put("link",link);
                    infoMap.put("category",category);
                    infoMap.put("description",description);

                    infoList.add(infoMap);
                }
                Collections.reverse(infoList);
                adapter = new SimpleAdapter(rootView.getContext(), infoList, R.layout.activity_list_item, from, to);
                listView.setAdapter(adapter);

                mapView = (MapView)rootView.findViewById(R.id.map_place);
                mapView.getMapAsync(LikeFragment.this);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity().getApplicationContext(),detail.class);
                intent.putExtra("title",infoList.get(position).get("title"));
                intent.putExtra("telephone",infoList.get(position).get("tel"));
                intent.putExtra("address",infoList.get(position).get("addr"));
                intent.putExtra("roadAddress",infoList.get(position).get("roadAddr"));
                intent.putExtra("category",infoList.get(position).get("category"));
                intent.putExtra("link",infoList.get(position).get("link"));
                intent.putExtra("description",infoList.get(position).get("description"));
                startActivity(intent);
            }
        });
        mapView = (MapView)rootView.findViewById(R.id.map_place);
        mapView.getMapAsync(this);
        return rootView;
    }
    @Override
    public void onMapReady(GoogleMap googleMap){
        googleMap.clear();
        geocoder = new Geocoder(getContext());
        for(int i=0; i<address_list.size(); i++){
            List<Address> addressList = null;
            try {
                addressList = geocoder.getFromLocationName(address_list.get(i), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(addressList.size()!=0){
                Address address = addressList.get(0);
                double latitude = address.getLatitude();
                double longitude = address.getLongitude();
                LatLng PLACE = new LatLng(latitude,longitude);

                MarkerOptions makerOptions = new MarkerOptions();
                makerOptions.position(PLACE);
                makerOptions.title(place_list.get(i));
                Marker marker = googleMap.addMarker(makerOptions);
                marker.showInfoWindow();

                googleMap.moveCamera(CameraUpdateFactory.newLatLng(PLACE));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
            }
        }

    }
    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }
    @Override
    public void onPause(){
        super.onPause();
        mapView.onPause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        if(mapView!=null){
            mapView.onCreate(savedInstanceState);
        }
    }
}