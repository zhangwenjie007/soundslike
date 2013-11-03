package com.game.soundslike.adapter;


/**
 * ���adapter���ڳ��� ����ļܹ�viewpager��
 * ������Զ�̬��չ����{@link MainActivity}�����
 * view����ӵ����HomeAdapter�Ķ��� a����a���ó�
 * viewpager�����������Ϳ����ó����һ�������ˡ�
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
