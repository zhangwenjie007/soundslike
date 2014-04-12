/**
 * @date 2014.4.9
 * @author tom
 * @description 这个Fragment在主界面，用于展示声音列表，带有下来功能
 * 包含:1.安装包里自带的小音乐
 *     2.本地下载的音乐(需要下拉刷新)
 */
package com.game.soundslike.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.game.soundslike.R;

public class PlayListFragment extends Fragment {
    
    /**
     * Fragment的生命周期的相关方法
     */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
        View contentView = inflater.inflate(R.layout.activity_playlist, null);
        Toast.makeText(this.getActivity(), "playList", Toast.LENGTH_SHORT).show();
        
        return contentView;
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    
}
