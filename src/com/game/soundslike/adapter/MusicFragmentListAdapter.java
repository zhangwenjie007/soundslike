
package com.game.soundslike.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.provider.SyncStateContract.Constants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.game.soundslike.R;
import com.game.soundslike.activity.MainActivity;
import com.game.soundslike.activity.PlayActivity;
import com.game.soundslike.constants.ConstantsParamers;
import com.game.soundslike.info.MusicFragmentInfo;

public class MusicFragmentListAdapter extends BaseAdapter{

	private List<MusicFragmentInfo> music_list = null;
	Context context;
	public MusicFragmentListAdapter(Context _context, List<MusicFragmentInfo> music_list){
		this.context = _context;
		this.music_list = music_list;
	}

	@Override
	public int getCount() {
		return music_list.size();
	}
	@Override
	public Object getItem(int arg0) {
		 return null;
	}
	@Override
	public long getItemId(int arg0) {
		return 0;
	}
	
	
	private class ViewHolder extends View{
		public ImageView icon;
		public TextView title;
		public TextView content;
		
		public ViewHolder(Context context, View view) {
			super(context);
			if(null != view){
				icon = (ImageView)view.findViewById(R.id.music_alpha);
				title = (TextView)view.findViewById(R.id.tv_title);
				content = (TextView)view.findViewById(R.id.tv_description);
				
			}
		}
	}
	
	@Override
	public View getView(int idx, View view, ViewGroup viewGroup) {
		
		ViewHolder holder = null;
		if(view == null){
			LayoutInflater inflater = LayoutInflater.from(context);
			view = inflater.inflate(R.layout.music_info_item, null);
			holder = new ViewHolder(context, view);	
			final MusicFragmentInfo music_info = music_list.get(idx);
			holder.title.setText(music_info.getName() + "-" + music_info.getBand());
			holder.content.setText(music_info.getDiscription());
			holder.icon.setImageResource(R.drawable.icon);
			
			// add listener
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent play_it =  new Intent(context, MainActivity.class);
					play_it.putExtra(ConstantsParamers.CURRENT_PAGE_IDX, "play_list");
					// 目前音乐文件有3种，打包到apk中 ；本地sd卡中 ；网络中；
					if(music_info.getRawId() != -1){
						play_it.putExtra(ConstantsParamers.MUSIC_INFO, music_info);
						context.startActivity(play_it);
					}
				}
			});
		}
		return view;
	}

}
