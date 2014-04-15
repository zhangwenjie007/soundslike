
package com.game.soundslike.ui.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.game.soundslike.R;
import com.game.soundslike.bean.MusicInfoBean;
import com.game.soundslike.constants.ConstantsParamers;
import com.game.soundslike.ui.activity.PlayActivity;

public class MusicListAdapter extends BaseAdapter{

	private List<MusicInfoBean> music_list = null;
	Context context;
	public MusicListAdapter(Context _context, List<MusicInfoBean> music_list){
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
			final MusicInfoBean music_info = music_list.get(idx);
			holder.title.setText(music_info.getName() + "-" + music_info.getBand());
			holder.content.setText(music_info.getDiscription());
			holder.icon.setImageResource(R.drawable.icon);
			
			// add listener
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent play_it =  new Intent();
					play_it.setClass(context, PlayActivity.class);
					// Ŀǰ�����ļ���3�֣����apk�� ������sd���� �������У�
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
