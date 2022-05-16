package com.zql.frameworks.app.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liu Hanhong on 15/4/17.
 */
public class FragmentAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> mFragments = new ArrayList<>();

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public FragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        mFragments.addAll(fragments);
    }

    public void addFragment(Fragment fragment) {
        mFragments.add(fragment);
    }

    public void removeFragment(Fragment fragment) {
        mFragments.remove(fragment);
    }

    public void setFragments(List<Fragment> fragments) {
        mFragments = fragments;
    }

    public List<Fragment> getFragments() {
        return mFragments;
    }

    public void clear() {
        for (Fragment fragment : mFragments) {
            if (fragment != null && fragment.isAdded()) {
                fragment.onDestroy();
            }
        }
        mFragments.clear();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

}
