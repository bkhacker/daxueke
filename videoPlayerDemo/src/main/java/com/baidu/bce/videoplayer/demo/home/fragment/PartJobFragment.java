package com.baidu.bce.videoplayer.demo.home.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.baidu.bce.videoplayer.demo.R;
import com.baidu.bce.videoplayer.demo.activity.JobDetailsActivity;
import com.baidu.bce.videoplayer.demo.adapter.JobAdapter;
import com.baidu.bce.videoplayer.demo.entity.PartJobEntity;
import com.baidu.bce.videoplayer.demo.home.BaseFragment;
import com.baidu.bce.videoplayer.demo.util.L;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


/**
 * 作者：yeweizheng on 2017/3/1 22:34
 * 包名：youdu.com.daxueke.home.fragment
 * 文件名：daxueke
 * 描述：
 */
public class PartJobFragment extends BaseFragment {
    /**
     * UI
     */
    private View mContentView;
    private ListView partjob_listview;



    /**
     * 数据
     */
    private List<String> mListName = new ArrayList<>();
    private List<String> mListDesc = new ArrayList<>();
    private List<String> mListSalary = new ArrayList<>();
    private List<String> mListAddress = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mContentView = inflater.inflate(R.layout.fragment_partjob_layout, null, false);
        initView();
        return mContentView;
    }



    private void initView() {

        partjob_listview = (ListView) mContentView.findViewById(R.id.partjob_listview);
        BmobQuery<PartJobEntity> query = new BmobQuery<PartJobEntity>();
        query.setLimit(50);
        query.findObjects(new FindListener<PartJobEntity>() {
            @Override
            public void done(List<PartJobEntity> mList, BmobException e) {
                if (e == null) {
                    JobAdapter adapter = new JobAdapter(getActivity(), mList);
                    partjob_listview.setAdapter(adapter);
                    L.i("查询成功：共" + mList.size() + "条数据。");
                    for (PartJobEntity job : mList) {
                        String name=job.getName();
                        String desc = job.getDesc();
                        String salary = job.getSalary();
                        String address = job.getAddress();
                        mListSalary.add(salary);
                        mListAddress.add(address);
                        mListName.add(name);
                        mListDesc.add(desc);
                        Log.i("bmob", job.getName()+job.getDesc()+job.getSalary()+job.getAddress());

                    }
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });

        //点击事件
        partjob_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                L.i("position: " + position);
                Intent intent = new Intent(getActivity(), JobDetailsActivity.class);
                //传递数据
                intent.putExtra("title", mListName.get(position));
                intent.putExtra("salary", mListSalary.get(position));
                intent.putExtra("desc", mListDesc.get(position));
                intent.putExtra("address", mListAddress.get(position));
                startActivity(intent);
            }
        });


    }
}
