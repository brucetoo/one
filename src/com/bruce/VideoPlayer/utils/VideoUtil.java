package com.bruce.VideoPlayer.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import com.bruce.VideoPlayer.bean.MediaBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Class Function:
 * Created By Bruce Too
 * On 2014-11-19 下午 4:53
 */
public class VideoUtil {

    //单例
    public static VideoUtil instance = new VideoUtil();

    public static VideoUtil getInstance() {
        return instance;
    }

    private VideoUtil() {
    }


    private List<MediaBean> videoList = new ArrayList<MediaBean>();


    public List<MediaBean> initVideos(Context context) {
        videoList.clear();


        // MediaStore.Video.Thumbnails.DATA:视频缩略图的文件路径
        String[] thumbColumns = {MediaStore.Video.Thumbnails.DATA,
                MediaStore.Video.Thumbnails.VIDEO_ID};

        // MediaStore.Video.Media.DATA：视频文件路径；
        // MediaStore.Video.Media.DISPLAY_NAME : 视频文件名，如 testVideo.mp4
        // MediaStore.Video.Media.TITLE: 视频标题 : testVideo
        Cursor cur = context.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Video.Media.TITLE,
                        MediaStore.Video.Media.DATA, MediaStore.Video.Media._ID, MediaStore.Video.Media.SIZE,MediaStore.Video.Media.DURATION}, null, null, null);
        try {
            if (cur != null) {
                while (cur.moveToNext()) {
                    MediaBean m = new MediaBean();
                    m.title = cur.getString(0);
                    m.path = cur.getString(1);
                    m._id = cur.getInt(2);
                    m.size = cur.getLong(3);
                    m.time = cur.getLong(4);
                    m.title_pinyin = PinyinUtils.chineneToSpell(m.title.charAt(0) + "");

                    Cursor cur1 = context.getContentResolver().query(
                            MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                            thumbColumns, MediaStore.Video.Thumbnails.VIDEO_ID
                                    + "=" + m._id, null, null);

                    if (cur1.moveToFirst()) {
                      m.thumb_path = (cur1.getString(cur1
                                .getColumnIndex(MediaStore.Video.Thumbnails.DATA)));
                    }

                    videoList.add(m);
                }
            }
        } catch (Exception e) {
        } finally {
            if (cur != null)
                cur.close();
        }

        return videoList;
    }


}
