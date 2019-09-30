package com.baidu.bce.videoplayer.demo.activity;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.bce.videoplayer.demo.R;
import com.baidu.bce.videoplayer.demo.adapter.DocCacheAdapter;

import java.io.File;
import java.util.ArrayList;

/**
 * 作者：yeweizheng on 2017/5/4 16:25
 * 包名：com.baidu.bce.videoplayer.demo.activity
 * 文件名：daxueke
 * 描述：
 */
public class DocCacheListActivity  extends ListActivity {
    private static final String ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/bmob/";
    //存储文件名称
    private ArrayList<String> names = null;
    //存储文件路径
    private ArrayList<String> paths = null;
    private View view;
    private EditText editText;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // finally change the color
            window.setStatusBarColor(0xfff7f7f7);
        }
        setContentView(R.layout.activity_list_of_doccache);
        initOtherUI();
        showFileDir(ROOT_PATH);
    }

    private void initOtherUI() {
        final ImageButton ibBack = (ImageButton) this.findViewById(R.id.ibtn_back);
        ibBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }

        });
        RelativeLayout rlback = (RelativeLayout) this.findViewById(R.id.rl_back);
        rlback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ibBack.performClick();
            }

        });
    }


    private void showFileDir(String path){
        names = new ArrayList<String>();
        paths = new ArrayList<String>();
        File file = new File(path);
        File[] files = file.listFiles();

        //如果当前目录不是根目录
        if (!ROOT_PATH.equals(path)){
            names.add("@1");
            paths.add(ROOT_PATH);

            names.add("@2");
            paths.add(file.getParent());
        }
        //添加所有文件
        for (File f : files){
            names.add(f.getName());
            paths.add(f.getPath());
        }
        this.setListAdapter(new DocCacheAdapter(this,names, paths));
    }
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String path = paths.get(position);
        File file = new File(path);
        // 文件存在并可读
        if (file.exists() && file.canRead()){
            if (file.isDirectory()){
                //显示子目录及文件
                showFileDir(path);
            }
            else{
                //处理文件
                fileHandle(file);
            }
        }
        //没有权限
        else{
            Resources res = getResources();
            new AlertDialog.Builder(this).setTitle("Message")
                    .setMessage(res.getString(R.string.no_permission))
                    .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
        }
        super.onListItemClick(l, v, position, id);
    }
    //对文件进行增删改
    private void fileHandle(final File file){
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 打开文件
                if (which == 0){
                    openFile(file);
                }
                //修改文件名
                else if(which == 1){
                    LayoutInflater factory = LayoutInflater.from(DocCacheListActivity.this);
                    view = factory.inflate(R.layout.rename_dialog, null);
                    editText = (EditText)view.findViewById(R.id.editText);
                    editText.setText(file.getName());

                    DialogInterface.OnClickListener listener2 = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            String modifyName = editText.getText().toString();
                            final String fpath = file.getParentFile().getPath();
                            final File newFile = new File(fpath + "/" + modifyName);
                            if (newFile.exists()){
                                //排除没有修改情况
                                if (!modifyName.equals(file.getName())){
                                    new AlertDialog.Builder(DocCacheListActivity.this)
                                            .setTitle("注意!")
                                            .setMessage("文件名已存在，是否覆盖？")
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    if (file.renameTo(newFile)){
                                                        showFileDir(fpath);
                                                        displayToast("重命名成功！");
                                                    }
                                                    else{
                                                        displayToast("重命名失败！");
                                                    }
                                                }
                                            })
                                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            })
                                            .show();
                                }
                            }
                            else{
                                if (file.renameTo(newFile)){
                                    showFileDir(fpath);
                                    displayToast("重命名成功！");
                                }
                                else{
                                    displayToast("重命名失败！");
                                }
                            }
                        }
                    };
                    AlertDialog renameDialog = new AlertDialog.Builder(DocCacheListActivity.this).create();
                    renameDialog.setView(view);
                    renameDialog.setButton("确定", listener2);
                    renameDialog.setButton2("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub

                        }
                    });
                    renameDialog.show();
                }
                //删除文件
                else{
                    new AlertDialog.Builder(DocCacheListActivity.this)
                            .setTitle("注意!")
                            .setMessage("确定要删除此文件吗？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(file.delete()){
                                        //更新文件列表
                                        showFileDir(file.getParent());
                                        displayToast("删除成功！");
                                    }
                                    else{
                                        displayToast("删除失败！");
                                    }
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                }
            }
        };
        //选择文件时，弹出增删该操作选项对话框
        String[] menu = {"打开文件","重命名","删除文件"};
        new AlertDialog.Builder(DocCacheListActivity.this)
                .setTitle("请选择要进行的操作!")
                .setItems(menu, listener)
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    private void displayToast(String message){
        Toast.makeText(DocCacheListActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 根据文件后缀名获得对应的MIME类型。
     * @param file
     */
    private String getMIMEType(File file)
    {
        String type="*/*";
        String fName=file.getName();
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if(dotIndex < 0){
            return type;
        }
    /* 获取文件的后缀名 */
        String end=fName.substring(dotIndex,fName.length()).toLowerCase();
        if(end=="")return type;
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for(int i=0;i<MIME_MapTable.length;i++){
            if(end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }
    /**
     * 打开文件
     * @param file
     */
    private void openFile(File file){
        //Uri uri = Uri.parse("file://"+file.getAbsolutePath());
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //设置intent的Action属性
        intent.setAction(Intent.ACTION_VIEW);
        //获取文件file的MIME类型
        String type = getMIMEType(file);
        //设置intent的data和Type属性。
        intent.setDataAndType(/*uri*/Uri.fromFile(file), type);
        //跳转
        startActivity(intent);
    }
    private final String[][] MIME_MapTable={
            //{后缀名，    MIME类型}
            {".3gp",    "video/3gpp"},
            {".apk",    "application/vnd.android.package-archive"},
            {".asf",    "video/x-ms-asf"},
            {".avi",    "video/x-msvideo"},
            {".bin",    "application/octet-stream"},
            {".bmp",      "image/bmp"},
            {".c",        "text/plain"},
            {".class",    "application/octet-stream"},
            {".conf",    "text/plain"},
            {".cpp",    "text/plain"},
            {".doc",    "application/msword"},
            {".docx",    "application/msword"},
            {".exe",    "application/octet-stream"},
            {".gif",    "image/gif"},
            {".gtar",    "application/x-gtar"},
            {".gz",        "application/x-gzip"},
            {".h",        "text/plain"},
            {".htm",    "text/html"},
            {".html",    "text/html"},
            {".jar",    "application/java-archive"},
            {".java",    "text/plain"},
            {".jpeg",    "image/jpeg"},
            {".jpg",    "image/jpeg"},
            {".js",        "application/x-javascript"},
            {".log",    "text/plain"},
            {".m3u",    "audio/x-mpegurl"},
            {".m4a",    "audio/mp4a-latm"},
            {".m4b",    "audio/mp4a-latm"},
            {".m4p",    "audio/mp4a-latm"},
            {".m4u",    "video/vnd.mpegurl"},
            {".m4v",    "video/x-m4v"},
            {".mov",    "video/quicktime"},
            {".mp2",    "audio/x-mpeg"},
            {".mp3",    "audio/x-mpeg"},
            {".mp4",    "video/mp4"},
            {".mpc",    "application/vnd.mpohun.certificate"},
            {".mpe",    "video/mpeg"},
            {".mpeg",    "video/mpeg"},
            {".mpg",    "video/mpeg"},
            {".mpg4",    "video/mp4"},
            {".mpga",    "audio/mpeg"},
            {".msg",    "application/vnd.ms-outlook"},
            {".ogg",    "audio/ogg"},
            {".pdf",    "application/pdf"},
            {".png",    "image/png"},
            {".pps",    "application/vnd.ms-powerpoint"},
            {".ppt",    "application/vnd.ms-powerpoint"},
            {".pptx",    "application/vnd.ms-powerpoint"},
            {".prop",    "text/plain"},
            {".rar",    "application/x-rar-compressed"},
            {".rc",        "text/plain"},
            {".rmvb",    "audio/x-pn-realaudio"},
            {".rtf",    "application/rtf"},
            {".sh",        "text/plain"},
            {".tar",    "application/x-tar"},
            {".tgz",    "application/x-compressed"},
            {".txt",    "text/plain"},
            {".wav",    "audio/x-wav"},
            {".wma",    "audio/x-ms-wma"},
            {".wmv",    "audio/x-ms-wmv"},
            {".wps",    "application/vnd.ms-works"},
            //{".xml",    "text/xml"},
            {".xml",    "text/plain"},
            {".z",        "application/x-compress"},
            {".zip",    "application/zip"},
            {"",        "*/*"}
    };
}