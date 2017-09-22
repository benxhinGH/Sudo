package com.lius.sudo.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lius.sudo.fragment.RankListFragment;
import com.lius.sudo.utilities.Util;

/**
 * Created by UsielLau on 2017/9/22 0022 14:11.
 */

public class RankViewPagerAdapter extends FragmentPagerAdapter {


    public RankViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        RankListFragment fragment=new RankListFragment();
        Bundle args=new Bundle();
        args.putInt("level",position+1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title= Util.getGameLevelText(position+1);
        return title;
    }
}
