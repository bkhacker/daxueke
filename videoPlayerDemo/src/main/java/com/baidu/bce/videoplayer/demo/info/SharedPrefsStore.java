package com.baidu.bce.videoplayer.demo.info;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.baidu.bce.videoplayer.demo.adapter.HomeAdapter;
import com.baidu.bce.videoplayer.demo.entity.HomeEntity;
import com.baidu.bce.videoplayer.demo.entity.VideoEntity;
import com.baidu.bce.videoplayer.demo.entity.ZhiboInfo;
import com.baidu.bce.videoplayer.demo.util.L;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 此处仅为存储示例 您的项目中可能使用其他存储形式
 *
 * @author baidu
 */
public class SharedPrefsStore {
    static ArrayList<VideoInfo> infoList = new ArrayList<VideoInfo>();
    static ArrayList<ZhiboInfo> infoList2 = new ArrayList<ZhiboInfo>();
    private static final String MAIN_VIDEO_LIST_SPNAME = "video-main";
    private static final String CACHE_VIDEO_LIST_SPNAME = "video-cache";
    private static final String SETTINGS_SPNAME = "video-settings";

    private static final String KEY_VIDEOS_ARRAY = "videos";

    public static boolean addToMainVideo(Context context, VideoInfo info) {
        SharedPreferences spList = context.getSharedPreferences(MAIN_VIDEO_LIST_SPNAME, 0);
        String jsonStr = spList.getString(KEY_VIDEOS_ARRAY, "[]");
        try {
            JSONArray jsonArray = new JSONArray(jsonStr);
            jsonArray.put(info.toJson());
            SharedPreferences.Editor editor = spList.edit();
            editor.putString(KEY_VIDEOS_ARRAY, jsonArray.toString());
            editor.commit();
        } catch (JSONException e1) {
            e1.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean deleteMainVideo(Context context, VideoInfo info) {
        SharedPreferences spList = context.getSharedPreferences(MAIN_VIDEO_LIST_SPNAME, 0);
        String jsonStr = spList.getString(KEY_VIDEOS_ARRAY, "[]");
        try {
            JSONArray jsonArray = new JSONArray(jsonStr);
            JSONArray newJsonArray = new JSONArray();
            for (int i = 0; i < jsonArray.length(); ++i) {
                try {
                    JSONObject json = (JSONObject) jsonArray.get(i);
                    VideoInfo info2 = VideoInfo.fromJson(json);
                    if (info2.getUrl().equals(info.getUrl()) && info2.getTitle().equals(info.getTitle())) {
                        continue;
                    } else {
                        newJsonArray.put(json);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (newJsonArray.length() == jsonArray.length()) {
                return false;
            } else {
                SharedPreferences.Editor editor = spList.edit();
                editor.putString(KEY_VIDEOS_ARRAY, newJsonArray.toString());
                editor.commit();
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return false;
    }

    //直播
    public static boolean deleteMainVideo(Context context, ZhiboInfo info) {
        SharedPreferences spList = context.getSharedPreferences(MAIN_VIDEO_LIST_SPNAME, 0);
        String jsonStr = spList.getString(KEY_VIDEOS_ARRAY, "[]");
        try {
            JSONArray jsonArray = new JSONArray(jsonStr);
            JSONArray newJsonArray = new JSONArray();
            for (int i = 0; i < jsonArray.length(); ++i) {
                try {
                    JSONObject json = (JSONObject) jsonArray.get(i);
                    VideoInfo info2 = VideoInfo.fromJson(json);
                    if (info2.getUrl().equals(info.getUrl()) && info2.getTitle().equals(info.getTitle())) {
                        continue;
                    } else {
                        newJsonArray.put(json);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (newJsonArray.length() == jsonArray.length()) {
                return false;
            } else {
                SharedPreferences.Editor editor = spList.edit();
                editor.putString(KEY_VIDEOS_ARRAY, newJsonArray.toString());
                editor.commit();
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return false;
    }

    public static boolean addToCacheVideo(Context context, VideoInfo info) {
        SharedPreferences spList = context.getSharedPreferences(CACHE_VIDEO_LIST_SPNAME, 0);
        String jsonStr = spList.getString(KEY_VIDEOS_ARRAY, "[]");
        try {
            JSONArray jsonArray = new JSONArray(jsonStr);
            jsonArray.put(info.toJson());
            SharedPreferences.Editor editor = spList.edit();
            editor.putString(KEY_VIDEOS_ARRAY, jsonArray.toString());
            editor.commit();
        } catch (JSONException e1) {
            e1.printStackTrace();
            return false;
        }
        return true;
    }

    //直播添加到缓存？？
    public static boolean addToCacheVideo(Context context, ZhiboInfo info) {
        SharedPreferences spList = context.getSharedPreferences(CACHE_VIDEO_LIST_SPNAME, 0);
        String jsonStr = spList.getString(KEY_VIDEOS_ARRAY, "[]");
        try {
            JSONArray jsonArray = new JSONArray(jsonStr);
            jsonArray.put(info.toJson());
            SharedPreferences.Editor editor = spList.edit();
            editor.putString(KEY_VIDEOS_ARRAY, jsonArray.toString());
            editor.commit();
        } catch (JSONException e1) {
            e1.printStackTrace();
            return false;
        }
        return true;
    }


    public static boolean deleteCacheVideo(Context context, VideoInfo info) {
        SharedPreferences spList = context.getSharedPreferences(CACHE_VIDEO_LIST_SPNAME, 0);
        String jsonStr = spList.getString(KEY_VIDEOS_ARRAY, "[]");
        try {
            JSONArray jsonArray = new JSONArray(jsonStr);
            JSONArray newJsonArray = new JSONArray();
            for (int i = 0; i < jsonArray.length(); ++i) {
                try {
                    JSONObject json = (JSONObject) jsonArray.get(i);
                    VideoInfo info2 = VideoInfo.fromJson(json);
                    if (info2.getUrl().equals(info.getUrl()) && info2.getTitle().equals(info.getTitle())) {
                        continue;
                    } else {
                        newJsonArray.put(json);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (newJsonArray.length() == jsonArray.length()) {
                return false;
            } else {
                SharedPreferences.Editor editor = spList.edit();
                editor.putString(KEY_VIDEOS_ARRAY, newJsonArray.toString());
                editor.commit();
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return false;
    }

    public static ArrayList<VideoInfo> getAllMainVideoFromSP(Context context) {

        infoList.addAll(getMainSampleData(context));
      /*  // add sample data first


        // add user added data
        SharedPreferences spList = context.getSharedPreferences(MAIN_VIDEO_LIST_SPNAME, 0);
        String jsonStr = spList.getString(KEY_VIDEOS_ARRAY, "[]");
        try {
            JSONArray jsonArray = new JSONArray(jsonStr);
            for (int i = 0; i < jsonArray.length(); ++i) {
                try {
                    JSONObject json = (JSONObject) jsonArray.get(i);
                    VideoInfo info = VideoInfo.fromJson(json);
                    infoList.add(info);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }*/

        BmobQuery<VideoInfo> query = new BmobQuery<VideoInfo>();
        query.setLimit(50);
        query.findObjects(new FindListener<VideoInfo>() {
            @Override
            public void done(List<VideoInfo> mList, BmobException e) {
                if (e == null) {
                    Log.i("bmob", "videook：" + mList.size());
                    infoList = (ArrayList<VideoInfo>) mList;
                } else {
                    Log.i("bmob", "video失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });

        return infoList;
    }
    public static ArrayList<ZhiboInfo> getAllMainVideoFromSP2(Context context) {

        infoList.addAll(getMainSampleData(context));
      /*  // add sample data first


        // add user added data
        SharedPreferences spList = context.getSharedPreferences(MAIN_VIDEO_LIST_SPNAME, 0);
        String jsonStr = spList.getString(KEY_VIDEOS_ARRAY, "[]");
        try {
            JSONArray jsonArray = new JSONArray(jsonStr);
            for (int i = 0; i < jsonArray.length(); ++i) {
                try {
                    JSONObject json = (JSONObject) jsonArray.get(i);
                    VideoInfo info = VideoInfo.fromJson(json);
                    infoList.add(info);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }*/

        BmobQuery<ZhiboInfo> query = new BmobQuery<ZhiboInfo>();
        query.setLimit(50);
        query.findObjects(new FindListener<ZhiboInfo>() {
            @Override
            public void done(List<ZhiboInfo> mList, BmobException e) {
                if (e == null) {
                    Log.i("bmob", "videook：" + mList.size());
                    infoList2 = (ArrayList<ZhiboInfo>) mList;
                } else {
                    Log.i("bmob", "video失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
        return infoList2;
    }


    /**
     * 初次进入应用，SP无数据时，准备样例数据
     *
     * @return
     */
    public static ArrayList<VideoInfo> getMainSampleData(Context context) {


        ArrayList<VideoInfo> sampleList = new ArrayList<VideoInfo>();

        String title1 ="aaa";
        String url1 = "http://gkkskijidms30qudc3v.exp.bcevod.com/mda-gkkswvrb2zhp41ez/mda-gkkswvrb2zhp41ez.m3u8";
        VideoInfo info1 = new VideoInfo(title1, url1);
        info1.setCanDelete(false);
        info1.setImageUrl("baidu_cloud_bigger.jpg");
        sampleList.add(info1);

      /*  String title2 = "大学客宣传视频";
        String url2 = "http://gkkskijidms30qudc3v.exp.bcevod.com/mda-gkkswvrb2zhp41ez/mda-gkkswvrb2zhp41ez.m3u8";
        VideoInfo info2 = new VideoInfo(title1, url1);
        info1.setCanDelete(false);
        info1.setImageUrl("baidu_cloud_bigger.jpg");
        sampleList.add(info2);

        String title3 = "大学客宣传视频";
        String url3 = "http://gkkskijidms30qudc3v.exp.bcevod.com/mda-gkkswvrb2zhp41ez/mda-gkkswvrb2zhp41ez.m3u8";
        VideoInfo info3 = new VideoInfo(title1, url1);
        info1.setCanDelete(false);
        info1.setImageUrl("baidu_cloud_bigger.jpg");
        sampleList.add(info3);

        String title4 = "大学客宣传视频";
        String url4 = "http://gkkskijidms30qudc3v.exp.bcevod.com/mda-gkkswvrb2zhp41ez/mda-gkkswvrb2zhp41ez.m3u8";
        VideoInfo info4 = new VideoInfo(title1, url1);
        info1.setCanDelete(false);
        info1.setImageUrl("baidu_cloud_bigger.jpg");
        sampleList.add(info4);

        String title5 = "大学客宣传视频";
        String url5 = "http://gkkskijidms30qudc3v.exp.bcevod.com/mda-gkkswvrb2zhp41ez/mda-gkkswvrb2zhp41ez.m3u8";
        VideoInfo info5 = new VideoInfo(title1, url1);
        info1.setCanDelete(false);
        info1.setImageUrl("baidu_cloud_bigger.jpg");
        sampleList.add(info5);

        String title6 = "大学客宣传视频";
        String url6 = "http://gkkskijidms30qudc3v.exp.bcevod.com/mda-gkkswvrb2zhp41ez/mda-gkkswvrb2zhp41ez.m3u8";
        VideoInfo info6 = new VideoInfo(title1, url1);
        info1.setCanDelete(false);
        info1.setImageUrl("baidu_cloud_bigger.jpg");
        sampleList.add(info6);

        String title7 = "大学客宣传视频";
        String url7 = "http://gkkskijidms30qudc3v.exp.bcevod.com/mda-gkkswvrb2zhp41ez/mda-gkkswvrb2zhp41ez.m3u8";
        VideoInfo info7 = new VideoInfo(title1, url1);
        info1.setCanDelete(false);
        info1.setImageUrl("baidu_cloud_bigger.jpg");
        sampleList.add(info7);*/

        return sampleList;
    }


    public static ArrayList<VideoInfo> getAllCacheVideoFromSP(Context context) {
        ArrayList<VideoInfo> infoList = new ArrayList<VideoInfo>();
        SharedPreferences spList = context.getSharedPreferences(CACHE_VIDEO_LIST_SPNAME, 0);
        String jsonStr = spList.getString(KEY_VIDEOS_ARRAY, "[]");
        try {
            JSONArray jsonArray = new JSONArray(jsonStr);
            for (int i = 0; i < jsonArray.length(); ++i) {
                try {
                    JSONObject json = (JSONObject) jsonArray.get(i);
                    VideoInfo info = VideoInfo.fromJson(json);
                    infoList.add(info);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return infoList;
    }

    public static void setDefaultOrientation(Context context, boolean isPortrait) {
        SharedPreferences spList = context.getSharedPreferences(SETTINGS_SPNAME, 0);
        SharedPreferences.Editor editor = spList.edit();
        editor.putBoolean("isPortrait", isPortrait);
        editor.commit();
    }

    public static boolean isDefaultPortrait(Context context) {
        SharedPreferences spList = context.getSharedPreferences(SETTINGS_SPNAME, 0);
        return spList.getBoolean("isPortrait", true);
    }

    public static void setControllBar(Context context, boolean isSimple) {
        SharedPreferences spList = context.getSharedPreferences(SETTINGS_SPNAME, 0);
        SharedPreferences.Editor editor = spList.edit();
        editor.putBoolean("isSimple", isSimple);
        editor.commit();
    }

    public static boolean isControllBarSimple(Context context) {
        SharedPreferences spList = context.getSharedPreferences(SETTINGS_SPNAME, 0);
        return spList.getBoolean("isSimple", false);
    }

    public static void setPlayerFitMode(Context context, boolean isCrapping) {
        SharedPreferences spList = context.getSharedPreferences(SETTINGS_SPNAME, 0);
        SharedPreferences.Editor editor = spList.edit();
        editor.putBoolean("isCrapping", isCrapping);
        editor.commit();
    }

    public static boolean isPlayerFitModeCrapping(Context context) {
        SharedPreferences spList = context.getSharedPreferences(SETTINGS_SPNAME, 0);
        return spList.getBoolean("isCrapping", false);
    }
}
