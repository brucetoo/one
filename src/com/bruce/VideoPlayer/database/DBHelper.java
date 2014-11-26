package com.bruce.VideoPlayer.database;

import android.content.Context;
import com.bruce.VideoPlayer.exception.Logger;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * Class Function:
 * Created By Bruce Too
 * On 2014-11-19 下午 4:17
 */
public class DBHelper<T> {


    /**
     * 添加一条记录
     * @param ct
     * @param bean  实体
     * @return
     */
    public int create(Context ct,T bean) {
        SQLiteHelper db = new SQLiteHelper(ct);
        try {
            Dao dao = db.getDao(bean.getClass());
            return dao.create(bean);
        } catch (SQLException e) {
            Logger.e(e);
        } finally {
            if (db != null)
                db.close();
        }
        return -1;
    }
}
