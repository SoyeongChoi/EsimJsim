package com.example.chm31.esimjsim;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyReview extends Activity {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    SharedPreferences pref;
    String loginType;
    String ID;

    ListView listView;

    String name, rating, title, date, content, addr, key, type;
    ImageView trash;

    private MyReviewAdapter adapter = null;
    private List<HashMap<String, String>> reviewList = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myreview);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        pref = getSharedPreferences("pref", MODE_PRIVATE);
        loginType = pref.getString("login", "");
        ID = pref.getString("id", "");
        listView = findViewById(R.id.listView);

        reviewList = new ArrayList<HashMap<String, String>>();
        StrictMode.enableDefaults();

        final DatabaseReference review_list = databaseReference.child("member").child(loginType).child(ID).child("review");

        review_list.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reviewList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    addr=snapshot.getKey();
                    for(DataSnapshot s : snapshot.getChildren()){
                        key = s.getKey();
                        title = s.child("title").getValue(String.class);
                        name = s.child("name").getValue(String.class);
                        rating = s.child("rating").getValue(String.class);
                        date = s.child("date").getValue(String.class);
                        content = s.child("content").getValue(String.class);

                        HashMap<String, String> infoMap = new HashMap<String, String>();

                        infoMap.put("key", key);
                        infoMap.put("title", title);
                        infoMap.put("name", name);
                        infoMap.put("rating", rating);
                        infoMap.put("date", date);
                        infoMap.put("content", content);
                        infoMap.put("addr", addr);
                        infoMap.put("type", "Lodge");

                        reviewList.add(infoMap);
                    }
                }
                adapter = new MyReviewAdapter(MyReview.this,reviewList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("DATABASE ERROR");
            }
        });
    }
}