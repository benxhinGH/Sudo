package com.lius.sudo.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.lius.sudo.R;
import com.lius.sudo.adapter.RankViewPagerAdapter;

/**
 * Created by 刘有泽 on 2016/8/17.
 */
public class RankActivity extends AppCompatActivity{

    private Toolbar toolbar;

    private TabLayout topTab;
    private ViewPager viewPager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        findViews();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
        setListeners();
    }

    private void findViews(){
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        topTab=(TabLayout)findViewById(R.id.top_tab);
        viewPager=(ViewPager)findViewById(R.id.view_pager);
    }

    private void initViews(){
        viewPager.setAdapter(new RankViewPagerAdapter(getSupportFragmentManager()));
        topTab.setTabMode(TabLayout.MODE_FIXED);
        topTab.setupWithViewPager(viewPager);
    }

    private void setListeners(){
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
