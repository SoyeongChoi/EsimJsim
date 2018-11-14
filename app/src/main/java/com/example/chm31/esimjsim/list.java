package com.example.chm31.esimjsim;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class list extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private SimpleAdapter adapter = null;
    private List<HashMap<String, String>> list = null;
    ArrayList<item> list_itemArrayList;
    public static final int LOAD_SUCCESS = 101;
    private ProgressDialog progressDialog = null;
    boolean Lat_api = false, Lng_api = false, Title = false, Addr = false, Tel = false;
    String tel = null, title = null, addr = null, lat = null, lng = null;
    String temp = null;
    String test;
    String telephone = null;
    String src_title = null;
    private int check = 0;
    List<HashMap<String,String>> arrayList;
    SearchView search;

    private boolean in = false;
    HashMap<String, String> storeinfoMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        list = new ArrayList<HashMap<String, String>>();
        Intent intent = getIntent();

        String url_str = intent.getStringExtra("str");
        temp = intent.getStringExtra("gu");
        readWeatherData();

        final ListView listView = (ListView)findViewById(R.id.listview_f);
        list_itemArrayList = new ArrayList<item>();
        StrictMode.enableDefaults();
        try {

            URL url = new URL(url_str);
            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();

            int i = 0;

            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("apiLat")) {
                            Lat_api = true;
                        }
                        if (parser.getName().equals("apiLng")) {
                            Lng_api = true;
                        }
                        if (parser.getName().equals("apiNewAddress")) {
                            Addr= true;
                        }
                        if (parser.getName().equals("apiName")) {
                            Title = true;
                        }
                        if (parser.getName().equals("apiTel")) {
                            Tel = true;
                        }
                        break;

                    case XmlPullParser.TEXT:
                        if (Lat_api) {
                            lat = parser.getText();
                            Lat_api = false;
                        }
                        if (Lng_api) {
                            lng = parser.getText();
                            Lng_api = false;
                        }
                        if (Addr) {
                            addr = parser.getText();
                            Addr = false;
                        }
                        if (Title) {
                            title = parser.getText();
                            Title = false;
                        }
                        if (Tel) {
                            tel = parser.getText();
                            Tel = false;
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("list")) {

                            HashMap<String,String> map = new HashMap<String,String>();

                            map.put("title",title);
                            map.put("lat",lat);
                            map.put("lng",lng);
                            map.put("addr",addr);
                            map.put("tel",tel);

                            list.add(map);
                            list_itemArrayList.add(new item(title,tel,addr,lat,lng));

                        }
                        break;
                }
                parserEvent = parser.next();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] from = new String[]{"title","tel","addr"};
        int[] to = new int[]{R.id.listviewdata1,R.id.listviewdata2,R.id.listviewdata3};
        adapter = new SimpleAdapter(this, list, R.layout.activity_list_item,from,to);
        arrayList = new ArrayList<HashMap<String,String>>();
        arrayList.addAll(list);

        search = (SearchView)findViewById(R.id.search);
        search.setOnQueryTextListener(this);
        listView.setAdapter(adapter);
        listView.setClickable(true);
        storeinfoMap= new HashMap<String, String>();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                detail fd = new detail();
                String temp = list.get(position).get("tel");
                telephone = temp;
                temp = "전주" + temp;
                src_title = list.get(position).get("title");
                try{
                    String text = URLEncoder.encode(temp,"UTF-8");
                    doJSONParser(text);

                    /*
                    String title = removeTag(StoreInfo.getString("title"));
                String category = StoreInfo.getString("category");
                String address = StoreInfo.getString("address");
                String roadAddress = StoreInfo.getString("roadAddress");
                String link = StoreInfo.getString("link");
                String description = StoreInfo.getString("description");
                String telephone = StoreInfo.getString("telephone");

                     */
                }catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                }

            }
        });


    }

    private List<WeatherSample> weatherSamples = new ArrayList<>();
    private   String clientId = "_FyckC8puLnMx_ni02Uf";
    private String clientSecret = "itxUCPEAvx";
    private  String apiURL = "https://openapi.naver.com/v1/search/local.json?query=";

    public void doJSONParser(final String keyword) throws UnsupportedEncodingException {

        int display = 2;
        if ( keyword == null) return;

        in = true;
        Thread thread = new Thread(new Runnable() {

            public void run() {

                String result;
                try{

                    URL url = new URL(apiURL+keyword+"&display="+30);
                    HttpURLConnection con = (HttpURLConnection)url.openConnection();
                    // con.setReadTimeout(3000);
                    // con.setConnectTimeout(3000);
                    // con.setDoOutput(true);
                    // con.setDoInput(true);
                    con.setRequestMethod("GET");
                    con.setRequestProperty("X-Naver-Client-Id", clientId);
                    con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
                    // con.setUseCaches(false);
                    // con.connect();
                    // post request

                    String postParams = "source=ko&target=en&text=" + keyword;
                    //     con.setDoOutput(true);
                    //    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                    //   wr.writeBytes(postParams);
                    //  wr.flush();
                    //  wr.close();

                    int responseCode = con.getResponseCode();
                    BufferedReader br;
                    if(responseCode==200) { // &#xc815;&#xc0c1; &#xd638;&#xcd9c;
                        br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    } else {  // &#xc5d0;&#xb7ec; &#xbc1c;&#xc0dd;
                        br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                    }
                    String inputLine;

                    StringBuffer response = new StringBuffer();
                    while ((inputLine = br.readLine()) != null) {
                        response.append(inputLine);
                    }
                    br.close();
                    test = response.toString();
                    con.disconnect();
                } catch (Exception e) {
                    System.out.println(e);

                }
                if(jsonParser(test)){
                    Message message;
                    if(check == 0){

                        message = mHandler.obtainMessage(LOAD_SUCCESS);
                    }else{

                        message = mHandler.obtainMessage(1);
                    }
                    mHandler.sendMessage(message);
                }

            }
        });
        thread.start();
    }

    private final MyHandler mHandler = new MyHandler(list.this);

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        search(newText);
        return true;
    }


    private class MyHandler extends Handler {
        private final WeakReference<list> weakReference;

        public MyHandler(list list) {
            weakReference = new WeakReference<list>(list);
        }

        @Override
        public void handleMessage(Message msg) {

            list list= weakReference.get();

            if (list != null) {
                switch (msg.what) {

                    case LOAD_SUCCESS:
                        break;
                    case 1:
                        AlertDialog.Builder alt_bld = new AlertDialog.Builder(list.this);

                        alt_bld.setMessage(Html.fromHtml("제공되는 상세정보가 없습니다.\n 인터넷 검색을 하시겠습니까? "));
                        alt_bld.setPositiveButton("cancel",null);
                        alt_bld.setNegativeButton("ok",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                                intent.putExtra(SearchManager.QUERY, src_title);
                                startActivity(intent);
                            }
                        });
                        alt_bld.show();
                        check =0;
                        break;

                }
            }
        }
    }
    public boolean jsonParser(String jsonString){

        if (jsonString == null ) return false;
        try {
            JSONObject root = new JSONObject(jsonString);
            JSONArray real = root.getJSONArray("items");
            if(real.length()>0){
                for(int i = 0; i < 1; i++){
                    JSONObject StoreInfo = real.getJSONObject(i);
                    String title = removeTag(StoreInfo.getString("title"));
                    String category = StoreInfo.getString("category");
                    String address = StoreInfo.getString("address");
                    String roadAddress = StoreInfo.getString("roadAddress");
                    String link = StoreInfo.getString("link");
                    String description = StoreInfo.getString("description");
                    String telephone = StoreInfo.getString("telephone");
                    String mapx_d = StoreInfo.getString("mapx");
                    String mapy_d = StoreInfo.getString("mapy");

                    storeinfoMap.put("title",title);
                    storeinfoMap.put("category",category);
                    storeinfoMap.put("address",address);
                    storeinfoMap.put("roadAddress",roadAddress);
                    storeinfoMap.put("mapx",mapx_d);
                    storeinfoMap.put("mapy",mapy_d);
                    storeinfoMap.put("link",link);
                    storeinfoMap.put("description",description);
                    storeinfoMap.put("telephone",telephone);
                }
                Intent intent2 = new Intent(getApplication(), detail.class);

                String i_title = storeinfoMap.get("title");
                String i_category = storeinfoMap.get("category");
                String i_telephone = telephone;
                String i_address = storeinfoMap.get("address");
                String i_roadAddress = storeinfoMap.get("roadAddress");
                String i_lik =storeinfoMap.get("link");
                String i_description = storeinfoMap.get("description");
                intent2.putExtra("title",i_title);
                intent2.putExtra("category",i_category);
                intent2.putExtra("telephone",i_telephone);
                intent2.putExtra("address",i_address);
                intent2.putExtra("roadAddress",i_roadAddress);
                intent2.putExtra("link",i_lik);
                intent2.putExtra("description",i_description);

                startActivity(intent2);
            }else{

                check = 1;
            }

            return true;
        } catch (JSONException e) {

            Log.d("Error", e.toString() );
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    public String removeTag(String html) throws Exception {
        String noHTMLString = html.replaceAll("\\<.*?\\>", "");

// Remove Carriage return from java String
        noHTMLString = noHTMLString.replaceAll("<([bip])>.*?</\1>", "");
// Remove New line from java string and replace html break
        noHTMLString = noHTMLString.replaceAll("\n", " ");
        noHTMLString = noHTMLString.replaceAll("<(.*?)\\>"," ");//Removes all items in brackets
        noHTMLString = noHTMLString.replaceAll("<(.*?)\\\n"," ");//Must be undeneath
        noHTMLString = noHTMLString.replaceFirst("(.*?)\\>", " ");
        noHTMLString = noHTMLString.replaceAll("&nbsp;"," ");
        noHTMLString = noHTMLString.replaceAll("&amp;"," ");
        noHTMLString = noHTMLString.replaceAll("&quot;"," ");
        return noHTMLString;

    }

    private void readWeatherData() {
        InputStream is = null;
        if(temp.equals("1")){
            is = getResources().openRawResource(R.raw.restaurant);
        }else if(temp.equals("2")){
            is = getResources().openRawResource(R.raw.restaurant2);
        }else{
            is = getResources().openRawResource(R.raw.restaurant3);
        }
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("EUC-KR"))
        );

        String line = "";
        try{
            while( (line = reader.readLine())!=null){
                String[] tokens = line.split(",");
                WeatherSample sample = new WeatherSample();
                sample.setName(tokens[1]);
                sample.setAddress(tokens[3]);
                sample.setTel(tokens[4]);
                sample.setType(tokens[6]);
                weatherSamples.add(sample);
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("title", sample.getName());
                map.put("addr",sample.getAddress());
                map.put("tel",sample.getTel());

                list.add(map);
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }
    private void search(String charText) {
        list.clear();

        if(charText.length() == 0){
            list.addAll(arrayList);
        }else{
            for(int i=0; i < arrayList.size(); i++){
                if(arrayList.get(i).get("title").contains(charText)||arrayList.get(i).get("addr").contains(charText)){
                    list.add(arrayList.get(i));
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}
