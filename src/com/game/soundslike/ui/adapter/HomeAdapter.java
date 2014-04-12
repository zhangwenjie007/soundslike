package com.game.soundslike.ui.adapter;


/**
 * 2014.4.9
 * author       :  tom
 * description  :  用于主界面的 ViewPager的适配器
 */
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.game.soundslike.constants.ConstantsParamers;
import com.game.soundslike.ui.fragment.DescriptionFragment;
import com.game.soundslike.ui.fragment.PlayFragment;
import com.game.soundslike.ui.fragment.PlayListFragment;

public class HomeAdapter extends FragmentPagerAdapter {

	/**
     * @param fragmentManager
     */
    public HomeAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    public List<View> views;
    public Fragment playListFragment;
    public Fragment playFragment;
    public Fragment descriptionFragment;

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if (playListFragment == null) {
                    playListFragment = new PlayListFragment();
                }
                return playListFragment;
            case 1:
                if (playFragment == null) {
                    playFragment = new PlayFragment();
                }
                return playFragment;
            case 2:
                if (descriptionFragment == null) {
                    descriptionFragment = new DescriptionFragment();
                }
                return descriptionFragment;
        }
        throw new IllegalStateException("No fragment at position " + position);
    }

    @Override
    public int getCount() {
        return ConstantsParamers.FRAGMENT_IDX_MAX;
    } 
}
