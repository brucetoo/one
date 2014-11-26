package com.bruce.VideoPlayer.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Class Function:
 * Created By Bruce Too
 * On 2014-11-19 下午 12:00
 */
public class PromptManager {
        private static ProgressDialog dialog;

        public static void showProgressDialog(Context context) {
            dialog = new ProgressDialog(context);
            dialog.setMessage("请等候，正在更新视频文件……");
            dialog.show();
        }

    public static void closeProgressDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
