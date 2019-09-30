package com.baidu.bce.videoplayer.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.bce.videoplayer.demo.R;
import com.baidu.bce.videoplayer.demo.entity.HomeEntity;

import java.util.List;

/**
 * 作者：yeweizheng on 2017/3/19 13:04
 * 包名：com.baidu.bce.videoplayer.demo.adapter
 * 文件名：daxueke
 * 描述：
 */
public class HomeAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<HomeEntity> mList;
    private HomeEntity data;
    private int width, height;
    private WindowManager wm;

    public HomeAdapter(Context mContext, List<HomeEntity> mList) {
        this.mContext = mContext;
        this.mList = mList;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_of_list_home, null);
            viewHolder.tile = (TextView) convertView.findViewById(R.id.home_tv_title);
            viewHolder.content = (TextView) convertView.findViewById(R.id.home_tv_content);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        data = mList.get(position);
        viewHolder.tile.setText(data.getTitle());
        viewHolder.content.setText(data.getContent());
        return convertView;
    }

    class ViewHolder {
        private TextView tile;
        private TextView content;
    }
}
