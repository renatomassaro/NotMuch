package com.fernando.notmuch.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.fernando.notmuch.Fragments.ChatFragment;
import com.fernando.notmuch.Fragments.FriendsFragment;

public class TabAdapter extends FragmentStatePagerAdapter {

    private String[]  tabTitles = {"CHATS", "FRIENDS"};

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch(position) {
            case 0:
                fragment = new ChatFragment();
                break;
            case 1:
                fragment = new FriendsFragment();
                break;
        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
