package com.baidu.bce.videoplayer.demo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.bce.videoplayer.demo.R;

import java.io.File;
import java.util.ArrayList;

/**
 * 作者：yeweizheng on 2017/5/6 14:34
 * 包名：com.baidu.bce.videoplayer.demo.adapter
 * 文件名：daxueke
 * 描述：
 */
public class DocCacheAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Bitmap directory, file;
    // 存储文件名称
    private ArrayList<String> names = null;
    // 存储文件路径
    private ArrayList<String> paths = null;

    // 参数初始化
    public DocCacheAdapter(Context context, ArrayList<String> na, ArrayList<String> pa) {
        names = na;
        paths = pa;
        directory = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.a);
        file = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.a);
        // 缩小图片
        directory = small(directory, 0.16f);
        file = small(file, 0.1f);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return names.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return names.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.file, null);
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.textView);
            holder.image = (ImageView) convertView.findViewById(R.id.imageView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        File f = new File(paths.get(position).toString());
        if (names.get(position).equals("@1")) {
            holder.text.setText("/");
            holder.image.setImageBitmap(directory);
        } else if (names.get(position).equals("@2")) {
            holder.text.setText("..");
            holder.image.setImageBitmap(directory);
        } else {
            holder.text.setText(f.getName());
            if (f.isDirectory()) {
                holder.image.setImageBitmap(directory);
            } else if (f.isFile()) {
                holder.image.setImageBitmap(file);
            } else {
                System.out.println(f.getName());
            }
        }
        return convertView;
    }

    private class ViewHolder {
        private TextView text;
        private ImageView image;
    }

    private Bitmap small(Bitmap map, float num) {
        Matrix matrix = new Matrix();
        matrix.postScale(num, num);
        return Bitmap.createBitmap(map, 0, 0, map.getWidth(), map.getHeight(),
                matrix, true);
    }
}