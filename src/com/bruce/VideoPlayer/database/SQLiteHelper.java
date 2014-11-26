package com.bruce.VideoPlayer.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.bruce.VideoPlayer.bean.MediaBean;
import com.bruce.VideoPlayer.exception.Logger;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Class Function:
 * Created By Bruce Too
 * On 2014-11-19 下午 4:05
 */
public class SQLiteHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    // 数据库名称
    private static final String DATABASE_NAME = "Media.db";
    // 数据库version
    private static final int DATABASE_VERSION = 1;

    //Dao 第二个参数是主键类型
    private Dao<MediaBean, Integer> mediaDao = null;

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {

        try {
            //建立User表
            TableUtils.createTable(connectionSource, MediaBean.class);
            //初始化DAO
            mediaDao = getDao(MediaBean.class);
        } catch (SQLException e) {
            Logger.e(TAG, e.toString());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i2) {
        try {
            //销毁表
            TableUtils.dropTable(connectionSource, MediaBean.class, true);
        } catch (SQLException e) {
            Logger.e(TAG,e.toString());
        }
    }
}
