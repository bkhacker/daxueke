package com.baidu.bce.videoplayer.demo.home.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.bce.videoplayer.demo.R;
import com.baidu.bce.videoplayer.demo.activity.DocViewActivity;
import com.baidu.bce.videoplayer.demo.activity.WebViewActivity;
import com.baidu.bce.videoplayer.demo.adapter.DocAdapter;
import com.baidu.bce.videoplayer.demo.adapter.JobAdapter;

import com.baidu.bce.videoplayer.demo.entity.DocEntity;
import com.baidu.bce.videoplayer.demo.home.BaseFragment;
import com.baidu.bce.videoplayer.demo.util.L;
import com.baidu.bdocreader.BDocInfo;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;

/**
 * 作者：yeweizheng on 2017/3/23 09:11
 * 包名：com.baidu.bce.videoplayer.demo.home.fragment
 * 文件名：daxueke
 * 描述：
 */
public class CourseDocFragment extends BaseFragment {
    /**
     * UI
     */
    private View mContentView;

    private ListView doc_mListView;


    private List<String> mListTitle = new ArrayList<>();
    private List<String> mListUrl = new ArrayList<>();
    private List<BmobFile> mListFile = new ArrayList<>();
    private List<String> mListDownUrl = new ArrayList<>();

    BDocInfo docInfo;
  BmobFile file;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mContentView = inflater.inflate(R.layout.fragment_doc, null, false);


        initView();
        return mContentView;
    }

    private void initView() {
        doc_mListView = (ListView) mContentView.findViewById(R.id.doc_mListView);
        BmobQuery<DocEntity> query = new BmobQuery<DocEntity>();
        query.findObjects(new FindListener<DocEntity>() {
            @Override
            public void done(List<DocEntity> mList, BmobException e) {
                if (e == null) {
                    DocAdapter adapter = new DocAdapter(getActivity(), mList);
                    doc_mListView.setAdapter(adapter);
                    for (DocEntity doc : mList) {
                        mListTitle.add(doc.getDocName());
                        mListUrl.add(doc.getWebLink());
                      file = doc.getDownLink();
                     mListFile.add(file);
                       mListDownUrl.add(doc.getDownUrl());
                    }

                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });






         //点击事件
        doc_mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                L.i("position: " + position);
               Intent intent = new Intent(getActivity(), WebViewActivity.class);
                //传递数据
                intent.putExtra("title", mListTitle.get(position));
                intent.putExtra("url", mListUrl.get(position));
               intent.putExtra("file", mListFile.get(position));
                intent.putExtra("downUrl", mListDownUrl.get(position));
                startActivity(intent);
              /*  File saveFile = new File(Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), file.getFilename());
             //  File path = new File(FileUtils.getSDCardPath() + "/" + System.currentTimeMillis() );
                File path2 = new File(Environment.getExternalStoragePublicDirectory((Environment.DIRECTORY_DOWNLOADS)).getPath());
                for (BmobFile files : mListFile) {
                    files.download();
                }*/
           /*
                file.download(new DownloadFileListener() {
                    *//**
                     * data/user/com.baidu/...
                     *//*
                    @Override
                    public void onStart() {

                        Toast.makeText(getActivity(), "开始下载", Toast.LENGTH_LONG);
                    }

                    @Override
                    public void done(String savePath,BmobException e) {
                        if(e==null){
                            Toast.makeText(getActivity(), "下载成功,保存路径" + savePath, Toast.LENGTH_LONG);
                            Log.i("bmob","下载成功："+savePath+",");
                        }else{
                            Toast.makeText(getActivity(), "下载失败,保存路径"  + e.toString(), Toast.LENGTH_LONG);
                            Log.i("bmob","下载失败："+e+",");
                        }
                    }

                    @Override
                    public void onProgress(Integer value, long newworkSpeed) {
                        Log.i("bmob","下载进度："+value+","+newworkSpeed);
                    }

                });*/

              /*  Intent intent = new Intent(getActivity(), DocViewActivity.class);
                intent.putExtra("ONE_DOC", docInfo);
                startActivity(intent);*/
            }
        });


    }
}
