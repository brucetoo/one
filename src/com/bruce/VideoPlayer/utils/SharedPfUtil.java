package com.bruce.VideoPlayer.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Class Function:
 * Created By Bruce Too
 * On 2014-11-21 上午 10:19
 */
public class SharedPfUtil {

    public static String SharePfName = "online_video_config";
    public static SharedPreferences preferences;


    //需要保存的名字
    public interface KEY {
        String VIDEO_LAST_POSITION = "video_last_position";  //上次播放的位置

    }

    public static void saveLong(Context ct, String name, Long value) {
        if (preferences == null) {
            preferences = ct.getSharedPreferences(SharePfName, ct.MODE_PRIVATE);
        }

        preferences.edit().putLong(name, value).commit();
    }

    public static Long getLong(Context ct, String name) {
        if (preferences == null) {
            preferences = ct.getSharedPreferences(SharePfName, ct.MODE_PRIVATE);
        }
        return preferences.getLong(name, 0l);
    }
}
