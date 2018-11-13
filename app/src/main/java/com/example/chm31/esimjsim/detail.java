package com.example.chm31.esimjsim;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class detail extends AppCompatActivity implements View.OnClickListener,OnMapReadyCallback {
    private Geocoder geocoder;
    TextView header1;
    TextView header2;
    int count = 0;
    ImageView favorite;
    Button review_button;

    ListView review_list;
    ConstraintLayout frame;
    LinearLayout info;
    LinearLayout review;

    ReviewAdapter adapter;
    ArrayList<review_item> list_itemArrayList;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    int i=0;

    SharedPreferences pref;
    String loginType;
    String ID;
    String name;
    TextView review_count;
    String remove;

    TextView place_title,place_addr,place_link,place_category,place_roadAddress,place_tel,place_description;
    String title = null, category = null, address = null, roadAddress = null, link = null,description = null,tel = null, map_x = null, map_y = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        list_itemArrayList = new ArrayList<review_item>();

        final Intent intent = getIntent();

        place_title = findViewById(R.id.place_title);
        place_addr = findViewById(R.id.place_addr);
        place_link = findViewById(R.id.place_link);
        place_category = findViewById(R.id.place_category);
        place_roadAddress = findViewById(R.id.place_roadAddress);
        place_tel = findViewById(R.id.place_tel);
        place_description = findViewById(R.id.place_description);
        String check = intent.getStringExtra("content");

        header1 = (TextView)findViewById(R.id.header_review);
        header2 = (TextView)findViewById(R.id.header_detail);
        favorite = (ImageView)findViewById(R.id.imageView_favorite);
        review_list = (ListView)findViewById(R.id.review_list);
        review_button = (Button)findViewById(R.id.review_button);
        review_count = (TextView)findViewById(R.id.review_count);

        header1.setOnClickListener(this);
        header2.setOnClickListener(this);
        favorite.setOnClickListener(this);
        review_button.setOnClickListener(this);

        title = intent.getStringExtra("title");
        category = intent.getStringExtra("category");
        tel = intent.getStringExtra("telephone");
        address = intent.getStringExtra("address");
        roadAddress = intent.getStringExtra("roadAddress");
        link = intent.getStringExtra("link");
        description = intent.getStringExtra("description");
        map_x = intent.getStringExtra("mapx");
        map_y = intent.getStringExtra("mapy");

        if(title.equals("")){
            title = "정보 없음";
        }if(category.equals("")){
            category = "정보 없음";
        }if(link.equals("")){
            link = "정보 없음";
        }if(tel.equals("")){
            tel = "정보 없음";
        }if(roadAddress.equals("")){
            roadAddress = "정보 없음";
        }if(description.equals("")){
            description = "정보 없음";
        }if(category.equals("")){
            category = "정보 없음";
        }
        place_title.setText(title);
        place_addr.setText(address);
        place_category.setText(category);
        place_tel.setText(tel);
        place_roadAddress.setText(roadAddress);
        place_link.setText(link);
        try {
            place_description.setText(removeTag(description));
        } catch (Exception e) {
            e.printStackTrace();
        }


        pref = getSharedPreferences("pref", MODE_PRIVATE);
        loginType=pref.getString("login","");
        ID=pref.getString("id","");
        name=pref.getString("name","");

        adapter = new ReviewAdapter(this,list_itemArrayList);
        review_list.setAdapter(adapter);


        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment)fragmentManager.findFragmentById(R.id.map_place);
        mapFragment.getMapAsync(this);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mConditionRef = mDatabase.child("review").child(address);

        mConditionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list_itemArrayList = new ArrayList<review_item>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String content = snapshot.child("content").getValue(String.class);
                    String name = snapshot.child("name").getValue(String.class);
                    String rating = snapshot.child("rating").getValue(String.class);
                    String time = snapshot.child("date").getValue(String.class);
                    String p = snapshot.child("title").getValue(String.class);
                    String id = snapshot.child("ID").getValue(String.class);
                    String lg_type = snapshot.child("login_type").getValue(String.class);
                    if(p!=null&&p.equals(title)){
                        Collections.reverse(list_itemArrayList);
                        list_itemArrayList.add(new review_item(name,content,time,Float.parseFloat(rating),address,id,lg_type));
                        Collections.reverse(list_itemArrayList);
                        adapter = new ReviewAdapter(detail.this,list_itemArrayList);
                        review_list.setAdapter(adapter);
                    }
                    String str = "리뷰 "+list_itemArrayList.size();
                    SpannableStringBuilder ssb = new SpannableStringBuilder(str);
                    ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#5F00FF")),3,str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    TextView review_count = (TextView)findViewById(R.id.review_count);
                    review_count.setText(ssb);

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        adapter = new ReviewAdapter(detail.this,list_itemArrayList);
        review_list.setAdapter(adapter);

        String str = "리뷰 "+list_itemArrayList.size();
        SpannableStringBuilder ssb = new SpannableStringBuilder(str);
        ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#5F00FF")),3,str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        review_count.setText(ssb);
        ////////////////////////////Like///////////////////////////////

        final DatabaseReference like_check = databaseReference.child("member").child(loginType).child(ID).child("like");

        like_check.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //lodge_like_size = dataSnapshot.getChildrenCount();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if(snapshot.child("tel").getValue(String.class)!=null){
                        if (snapshot.child("tel").getValue(String.class).equals(tel)) {
                            remove = snapshot.getKey();
                            favorite.setImageResource(R.drawable.like);
                            count = 1;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        review = (LinearLayout)findViewById(R.id.review);
        review.setVisibility(View.INVISIBLE);
        header2.setBackgroundResource(R.drawable.border);
        info = (LinearLayout)findViewById(R.id.info);

    }

    @Override
    public void onClick(View v) {

        frame = (ConstraintLayout)findViewById(R.id.frame);
        info = (LinearLayout)findViewById(R.id.info);
        favorite = (ImageView)findViewById(R.id.imageView_favorite);
        review = (LinearLayout)findViewById(R.id.review);

        if(v.getId() == R.id.header_review){
            header1.setBackgroundResource(R.drawable.border);
            header2.setBackgroundResource(0);
            info.setVisibility(View.INVISIBLE);
            review.setVisibility(View.VISIBLE);

        }else if(v.getId() == R.id.header_detail){
            header2.setBackgroundResource(R.drawable.border);
            header1.setBackgroundResource(0);
            info.setVisibility(View.VISIBLE);
            review.setVisibility(View.INVISIBLE);

        }else if(v.getId() == R.id.imageView_favorite){
            if(count==1){
                favorite.setImageResource(R.drawable.like_blank);
                count=0;
                databaseReference.child("/member/"+loginType+"/"+ID+"/like/"+remove).removeValue();
            }else if(count==0){
                favorite.setImageResource(R.drawable.like);
                count=1;
                final DatabaseReference push = databaseReference.child("member").child(loginType).child(ID).child("like").push();
                push.child("title").setValue(title);
                push.child("category").setValue(category);
                push.child("addr").setValue(address);
                push.child("link").setValue(link);
                push.child("tel").setValue(tel);
                push.child("roadAddr").setValue(roadAddress);
                push.child("description").setValue(description);
            }
        }else if(v.getId() == R.id.review_button){
            Intent intent = new Intent(this,review_write.class);
            startActivityForResult(intent,1);
        }
    }

    public String removeTag(String html) throws Exception {
        String noHTMLString = html.replaceAll("\\<.*?\\>", "");
        noHTMLString = noHTMLString.replaceAll("<([bip])>.*?</\1>", "");
        noHTMLString = noHTMLString.replaceAll("\n", " ");
        noHTMLString = noHTMLString.replaceAll("<(.*?)\\>"," ");//Removes all items in brackets
        noHTMLString = noHTMLString.replaceAll("<(.*?)\\\n"," ");//Must be undeneath
        noHTMLString = noHTMLString.replaceFirst("(.*?)\\>", " ");
        noHTMLString = noHTMLString.replaceAll("&nbsp;"," ");
        noHTMLString = noHTMLString.replaceAll("&amp;"," ");
        noHTMLString = noHTMLString.replaceAll("&quot;"," ");
        noHTMLString = noHTMLString.replaceAll("&#39;"," ");
        return noHTMLString;

    }
    public void openDial(View view){
        TextView quiry_ = (TextView)findViewById(R.id.place_tel);
        Intent intent = new Intent(Intent.ACTION_DIAL);
        String receiver =quiry_.getText().toString();
        intent.setData(Uri.parse("tel:"+receiver));
        startActivity(intent);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String rating = data.getStringExtra("rating");
                String content = data.getStringExtra("content");
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String time = sdf.format(date);
                DatabaseReference push = databaseReference.child("review").child(address).push();
                push.child("name").setValue(name);
                push.child("content").setValue(content);
                push.child("rating").setValue(rating);
                push.child("date").setValue(time);
                push.child("title").setValue(title);
                push.child("ID").setValue(ID);
                push.child("login_type").setValue(loginType);
                DatabaseReference myreview = databaseReference.child("member").child(loginType).child(ID).child("review").child(address).child(push.getKey());
                myreview.child("name").setValue(name);
                myreview.child("content").setValue(content);
                myreview.child("rating").setValue(rating);
                myreview.child("date").setValue(time);
                myreview.child("title").setValue(title);

            }
        }
    }
    public void onPause() {
        super.onPause();
        String str = "리뷰 "+list_itemArrayList.size();
        SpannableStringBuilder ssb = new SpannableStringBuilder(str);
        ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#5F00FF")),3,str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        review_count.setText(ssb);
    }
    @Override
    public void onMapReady(GoogleMap googleMap){
        geocoder = new Geocoder(this);
        List<Address> addressList = null;
        try {
            addressList = geocoder.getFromLocationName(address, 1);
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
            makerOptions.title("click");
            Marker marker = googleMap.addMarker(makerOptions);
            marker.showInfoWindow();

            googleMap.moveCamera(CameraUpdateFactory.newLatLng(PLACE));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(17));

        }
    }

}
