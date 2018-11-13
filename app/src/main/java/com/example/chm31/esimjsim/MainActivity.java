package com.example.chm31.esimjsim;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView mMainNav;
    private RelativeLayout mMainFrame;
    private HomeFragment homeFragment;
    private LikeFragment likeFragment;
    private MoreFragment moreFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMainFrame = (RelativeLayout) findViewById(R.id.main_frame);
        mMainNav = (BottomNavigationView) findViewById(R.id.main_nav);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.main_nav);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);

        homeFragment = new HomeFragment();
        likeFragment = new LikeFragment();
        moreFragment = new MoreFragment();

        setFragment(homeFragment);
        mMainNav.getMenu().findItem(R.id.nav_home).setIcon(R.drawable.home2);

        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch ((item.getItemId())) {

                    case R.id.nav_home:
                        mMainNav.setItemBackgroundResource(R.drawable.bottomline);
                        setFragment(homeFragment);
                        mMainNav.getMenu().findItem(R.id.nav_home).setIcon(R.drawable.home2);
                        mMainNav.getMenu().findItem(R.id.nav_like).setIcon(R.drawable.heart);
                        mMainNav.getMenu().findItem(R.id.nav_more).setIcon(R.drawable.more);
                        return true;

                    case R.id.nav_like:
                        mMainNav.setItemBackgroundResource(R.drawable.bottomline);
                        setFragment(likeFragment);
                        mMainNav.getMenu().findItem(R.id.nav_home).setIcon(R.drawable.home);
                        mMainNav.getMenu().findItem(R.id.nav_like).setIcon(R.drawable.heart2);
                        mMainNav.getMenu().findItem(R.id.nav_more).setIcon(R.drawable.more);
                        return true;

                    case R.id.nav_more:
                        mMainNav.setItemBackgroundResource(R.drawable.bottomline);
                        setFragment(moreFragment);
                        mMainNav.getMenu().findItem(R.id.nav_home).setIcon(R.drawable.home);
                        mMainNav.getMenu().findItem(R.id.nav_like).setIcon(R.drawable.heart);
                        mMainNav.getMenu().findItem(R.id.nav_more).setIcon(R.drawable.more2);
                        return true;

                    default:
                        return false;
                }
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }
}