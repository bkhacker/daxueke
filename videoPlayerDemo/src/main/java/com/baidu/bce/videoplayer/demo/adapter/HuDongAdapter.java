package com.baidu.bce.videoplayer.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.bce.videoplayer.demo.R;
import com.baidu.bce.videoplayer.demo.entity.Chat;
import com.baidu.bce.videoplayer.demo.info.VideoInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yeweizheng on 2017/4/22 20:23
 * 包名：com.baidu.bce.videoplayer.demo.adapter
 * 文件名：daxueke extends BaseAdapter
 * 描述：
 */
public class HuDongAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater inflater;
    private List<Chat> messages;
    private VideoInfo data;
    private int width, height;
    private WindowManager wm;
    ViewHolder holder;



    public HuDongAdapter(Context mContext, List<Chat> mList) {
        this.mContext = mContext;
        this.messages = mList;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if(convertView == null){
            convertView = inflater.inflate(R.layout.chat_list_item, null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);


            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        Chat chat = messages.get(position);
        holder.tv_name.setText(chat.getName()+"：");
        holder.tv_content.setText(chat.getContent());


        return convertView;
    }

    class ViewHolder{
        TextView tv_name;
        TextView tv_content;
        TextView tv_time;
    }


}
