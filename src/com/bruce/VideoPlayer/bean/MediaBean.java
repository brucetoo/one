package com.bruce.VideoPlayer.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.File;

/**
 * Class Function:
 * Created By Bruce Too
 * On 2014-11-19 下午 2:56
 */
@DatabaseTable (tableName = "VideoPlayer")
public class MediaBean {
    @DatabaseField (generatedId = true)
    public int _id;
    /** 视频标题 */
    @DatabaseField (canBeNull = false)
    public String title;
    /** 视频标题拼音 */
    @DatabaseField (canBeNull = false)
    public String title_pinyin;
    /** 视频路径 */
    @DatabaseField (canBeNull = false)
    public String path;
    /** 最后一次访问时间 */
    @DatabaseField(defaultValue = "")
    public long last_access_time;
    /** 大小 */
    @DatabaseField(defaultValue = "0")
    public long size;
    /**略缩图*/
    public String thumb_path;

    public long time;
    public MediaBean() {

    }

    public MediaBean(File f) {
   /*     title = f.getName();
        path = f.getAbsolutePath();
        last_access_time = f.lastModified();
        file_size = f.length();
        try {
            title_pinyin = PinyinUtils.chineneToSpell(f.getName().charAt(0) +"");
        } catch (Exception e) {

        }*/
    }

}
