package com.example.chm31.esimjsim;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment{

    private Button wansan, ducjin,total;
    boolean Lat_api = false, Lng_api = false, Title = false, Addr = false, Tel = false;
    String tel = null, title = null, addr = null, lat = null, lng = null;
    private List<HashMap<String,String>> list = null;
    ArrayList<item> list_itemArrayList;
    List<HashMap<String,String>> arrayList;
    Intent i;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        wansan= rootView.findViewById(R.id.wansan);
        ducjin = rootView.findViewById(R.id.ducjin);
        total = rootView.findViewById(R.id.total);
        list_itemArrayList = new ArrayList<item>();
        StrictMode.enableDefaults();

        list_itemArrayList = new ArrayList<item>();
        list = new ArrayList<HashMap<String,String>>();
        final String[] url_str = {null};
        wansan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url_str[0] = "http://openapi.jeonju.go.kr/rest/chakhanpriceservice/getChakHan?serviceKey=bzSNoAg0PRCkN55%2Br4HAAn8JW7RKd8%2B3oOUST6I113ZxsLdw8magLcWKMb16ZokU8Bv735iH%2FjO0MCgUuJvJhw%3D%3D&pageNo=1&startPage=1&numOfRows=100&pageSize=100&address=완산구";
                i = new Intent(getActivity(), list.class);
                i.putExtra("str",url_str[0]);
                i.putExtra("gu","3");
                startActivity(i);
            }
        });

        ducjin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url_str[0] = "http://openapi.jeonju.go.kr/rest/chakhanpriceservice/getChakHan?serviceKey=bzSNoAg0PRCkN55%2Br4HAAn8JW7RKd8%2B3oOUST6I113ZxsLdw8magLcWKMb16ZokU8Bv735iH%2FjO0MCgUuJvJhw%3D%3D&pageNo=1&startPage=1&numOfRows=100&pageSize=100&address=덕진구";
                i = new Intent(getActivity(), list.class);
                i.putExtra("str",url_str[0]);
                i.putExtra("gu","2");
                startActivity(i);
            }
        });

        total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url_str[0] = "http://openapi.jeonju.go.kr/rest/chakhanpriceservice/getChakHan?serviceKey=bzSNoAg0PRCkN55%2Br4HAAn8JW7RKd8%2B3oOUST6I113ZxsLdw8magLcWKMb16ZokU8Bv735iH%2FjO0MCgUuJvJhw%3D%3D&pageNo=1&startPage=1&numOfRows=100&pageSize=100";
                i = new Intent(getActivity(), list.class);
                i.putExtra("str",url_str[0]);
                i.putExtra("gu","1");
                startActivity(i);
            }
        });


        //defining Cards
        //Add Click listener to the cards
        return rootView;
    }


}