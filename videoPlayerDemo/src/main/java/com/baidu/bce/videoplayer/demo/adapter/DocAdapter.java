package com.baidu.bce.videoplayer.demo.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.bce.videoplayer.demo.R;
import com.baidu.bce.videoplayer.demo.entity.DocEntity;
import com.baidu.bce.videoplayer.demo.entity.PartJobEntity;
import com.baidu.bce.videoplayer.demo.util.PicassoUtils;

import java.util.List;

/**
 * 作者：yeweizheng on 2017/3/31 17:24
 * 包名：com.baidu.bce.videoplayer.demo.adapter
 * 文件名：daxueke
 * 描述：文档的适配器
 */
public class DocAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<DocEntity> mList;
    private DocEntity data;
    private int width, height;
    private WindowManager wm;

    public DocAdapter(Context mContext, List<DocEntity> mList) {
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
            convertView = inflater.inflate(R.layout.item_of_list_doc, null);
            viewHolder.doc_iv_img = (ImageView) convertView.findViewById(R.id.doc_iv_img);
            viewHolder.doc_tv_title = (TextView) convertView.findViewById(R.id.doc_tv_title);
            viewHolder.doc_tv_desc = (TextView) convertView.findViewById(R.id.doc_tv_desc);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        data = mList.get(position);
        viewHolder.doc_tv_title.setText(data.getDocName());
        viewHolder.doc_tv_desc.setText(data.getDesc());

        //加载图片
        //Picasso.with(mContext).load(data.getImgUrl()).into(viewHolder.iv_img);
        if (!TextUtils.isEmpty(data.getDocImg())) {
            //加载图片
            PicassoUtils.loadImageViewSize(mContext, data.getDocImg(), width / 3, 250, viewHolder.doc_iv_img);
        }
        return convertView;
    }

    class ViewHolder {
        private ImageView doc_iv_img;
        private TextView doc_tv_title;
        private TextView doc_tv_desc;


    }
}
