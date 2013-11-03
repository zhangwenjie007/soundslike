package com.game.soundslike.adapter;


/**
 * 这个adapter用于程序 主体的架构viewpager。
 * 程序可以动态扩展。在{@link MainActivity}里添加
 * view。添加到这个HomeAdapter的对象 a，把a设置成
 * viewpager的适配器，就可以让程序多一个界面了。
 */
import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class HomeAdapter extends PagerAdapter {

	public List<View> views;

	public HomeAdapter(List<View> arg1) {
		views = arg1;
	}

	@Override
	public int getCount() {
		return views.size();
	}

	@Override
	public Object instantiateItem(View container, int position) {
		((ViewPager) container).addView(views.get(position));
		return views.get(position);
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView(views.get(position));
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

}
