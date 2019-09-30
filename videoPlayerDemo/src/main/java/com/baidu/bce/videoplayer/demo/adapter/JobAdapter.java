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
import com.baidu.bce.videoplayer.demo.entity.PartJobEntity;
import com.baidu.bce.videoplayer.demo.util.PicassoUtils;

import java.util.List;

/**
 * 作者：yeweizheng on 2017/3/31 17:24
 * 包名：com.baidu.bce.videoplayer.demo.adapter
 * 文件名：daxueke
 * 描述：实习的适配器
 */
public class JobAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<PartJobEntity> mList;
    private PartJobEntity data;
    private int width, height;
    private WindowManager wm;

    public JobAdapter(Context mContext, List<PartJobEntity> mList) {
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
            convertView = inflater.inflate(R.layout.item_of_list_partjob, null);
            viewHolder.iv_job_img = (ImageView) convertView.findViewById(R.id.iv_job_img);
            viewHolder.tv_job_name = (TextView) convertView.findViewById(R.id.tv_job_name);
            viewHolder.tv_job_salary = (TextView) convertView.findViewById(R.id.tv_job_salary);
            viewHolder.tv_job_company = (TextView) convertView.findViewById(R.id.tv_job_company);
            viewHolder.tv_job_address = (TextView) convertView.findViewById(R.id.tv_job_address);
            viewHolder.tv_job_experience = (TextView) convertView.findViewById(R.id.tv_job_experience);
            viewHolder.tv_job_degree = (TextView) convertView.findViewById(R.id.tv_job_degree);
            viewHolder.tv_job_lun = (TextView) convertView.findViewById(R.id.tv_job_lun);
            viewHolder.tv_job_scale = (TextView) convertView.findViewById(R.id.tv_job_scale);
            viewHolder.tv_job_industry = (TextView) convertView.findViewById(R.id.tv_job_industry);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        data = mList.get(position);
        viewHolder.tv_job_name.setText(data.getName());
        viewHolder.tv_job_salary.setText(data.getSalary());
        viewHolder.tv_job_company.setText(data.getCompany());
        viewHolder.tv_job_address.setText(data.getAddress());
        viewHolder.tv_job_experience.setText(data.getExperience());
        viewHolder.tv_job_degree.setText(data.getDegree());
        viewHolder.tv_job_lun.setText(data.getLun());
        viewHolder.tv_job_scale.setText(data.getScale());
        viewHolder.tv_job_industry.setText(data.getIndustry());
        //加载图片
        //Picasso.with(mContext).load(data.getImgUrl()).into(viewHolder.iv_img);
        if(!TextUtils.isEmpty(data.getImage())){
            //加载图片
            PicassoUtils.loadImageViewSize(mContext, data.getImage(), width/3, 250, viewHolder.iv_job_img);
        }
        return convertView;
    }

    class ViewHolder {
        private ImageView iv_job_img;
        private TextView tv_job_name;
        private TextView tv_job_salary;
        private TextView tv_job_company;
        private TextView tv_job_address;
        private TextView tv_job_experience;
        private TextView tv_job_degree;
        private TextView tv_job_lun;
        private TextView tv_job_scale;
        private TextView tv_job_industry;


    }
}
