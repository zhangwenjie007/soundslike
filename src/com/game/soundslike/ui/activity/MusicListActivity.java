package com.game.soundslike.ui.activity;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.game.soundslike.R;
import com.game.soundslike.bean.MusicInfoBean;
import com.game.soundslike.ui.adapter.MusicListAdapter;

public class MusicListActivity extends BaseActivity{

	private Context mContext;
	private List<MusicInfoBean> music_list = null;
	private ListView music_list_view = null;
	
	
	private Activity getActivity(){
		return MusicListActivity.this;
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		music_list = new ArrayList<MusicInfoBean>();
		setContentView(R.layout.activity_playlist);
		music_list_view = (ListView)findViewById(R.id.list_musics_toshow);
		MusicInfoBean info = new MusicInfoBean();
		info.setName("海贼王");
		info.setBand("牛逼乐队");
		info.setRawId(R.raw.kdxf);
		info.setDiscription("一首经典的海贼王片头曲，你猜猜吧!");
	
		music_list.add(info);
		info = new MusicInfoBean();
		info.setName("乔巴");
		info.setBand("无");
		info.setRawId(R.raw.zqxj);
		info.setDiscription("这是一只非常可爱的狸猫，可是他却不承认，so what do you think?");
		music_list.add(info);
		
		MusicListAdapter music_list_adapter =
				new MusicListAdapter(this, music_list);
		music_list_view.setAdapter(music_list_adapter);
		music_list_view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Toast.makeText(getActivity(), "you click index:" + arg2, Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}
