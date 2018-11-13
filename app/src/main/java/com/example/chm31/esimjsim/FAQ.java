package com.example.chm31.esimjsim;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class FAQ extends AppCompatActivity {

    private ArrayList<String> mGroupList = null;
    private LinkedHashMap<String,List<String>> mChildList = null;
    private ArrayList<String> mChildListContent = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        setLayout();

        mGroupList = new ArrayList<String>();
        mChildList = new LinkedHashMap<String,List<String>>();
        mGroupList.add("안심먹거리가 무엇인가요?");
        mGroupList.add("식당에 대한 정보는 어디서 가져오나요?");
        mGroupList.add("제 아이디인데 삭제가 되지 않아요");
        mGroupList.add("개발자가 누구인가요?");
        mGroupList.add("길찾기 기능은 없나요?");

        for(String question : mGroupList){
            if(question.contains("개발자가")){
                loadChild("충남대학교 컴퓨터공학과에 재학중인 전혜민, 최소영, 허아정 학생입니다.");
            }else if(question.contains("제 아이디")){
                loadChild("로그인 타입(카카오톡, 네이버, 페이스북)을 확인해보세요. \n작성된 글과 현재 로그인한 타입이 일치하지 않으면 삭제가 되지 않습니다.");
            }else if(question.contains("안심먹거리가")) {
                loadChild("안심먹거리란 이름 그대로 ‘국내 최고 유통 전문가가 보증해서 믿고 먹을 수 있는 먹거리'입니다. \n 가장 대표적인 인증파크로 'HACCP'이 있습니다.");
            }else if(question.contains("식당에 대한 정보는")){
                loadChild("전주시 공공데이터포털에서 가져오는 정보입니다.");
            }else if(question.contains("길찾기")){
                loadChild("지도에 있는 마커를 클릭하면 길찾기 기능을 사용할 수 있습니다.");
            }
            mChildList.put(question,mChildListContent);

        }
        setGroupIndicatorToRight();
        mListView.setAdapter(new BaseExpandableAdapter(this, mGroupList, mChildList));
        /*
        // 그룹 클릭 했을 경우 이벤트
        mListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                Toast.makeText(getApplicationContext(), "g click = " + groupPosition,
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // 차일드 클릭 했을 경우 이벤트
        mListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(getApplicationContext(), "c click = " + childPosition,
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });
*/
        // 그룹이 닫힐 경우 이벤트
        mListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });
        /*
        // 그룹이 열릴 경우 이벤트
        mListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(), "g Expand = " + groupPosition,
                        Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    /*
     * Layout
     */
    private ExpandableListView mListView;
    private void setGroupIndicatorToRight() {
        /* Get the screen width */
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;

        mListView.setIndicatorBounds(width - getDipsFromPixel(35), width
                - getDipsFromPixel(5));
    }
    private void setLayout(){
        mListView = (ExpandableListView) findViewById(R.id.FAQ_list);
    }
    public int getDipsFromPixel(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }
    private void loadChild(String ask) {
        mChildListContent = new ArrayList<String>();
        mChildListContent.add(ask);
    }
}
