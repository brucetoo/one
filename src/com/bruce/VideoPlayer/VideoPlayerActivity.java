package com.bruce.VideoPlayer;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.*;
import android.widget.ImageView;
import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

/**
 * Class Function:
 * Created By Bruce Too
 * On 2014-11-18 下午 3:34
 */
public class VideoPlayerActivity extends Activity {

    private String path;
    private VideoView mVideoView;
    private View mVolumeBrightnessLayout; //显示布局
    private ImageView mOperationBg;       //进度背景
    private ImageView mOperationPercent;  //进度显示
    private AudioManager mAudioManager;

    /**
     * 最大声音
     */
    private int mMaxVolume;
    /**
     * 当前声音
     */
    private int mVolume = -1;
    /**
     * 当前亮度
     */
    private float mBrightness = -1f;
    /**
     * 当前缩放模式
     */
    private int mLayout = VideoView.VIDEO_LAYOUT_ZOOM;
    private GestureDetector mGestureDetector;
    private MediaController mMediaController;


    /**
     * 定时隐藏 handler
     */
    private Handler mDismissHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mVolumeBrightnessLayout.setVisibility(View.GONE);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (!LibsChecker.checkVitamioLibs(this))
            return;

        setContentView(R.layout.activity_video_player);

        path = getIntent().getStringExtra("path");
        mVideoView = (VideoView) findViewById(R.id.surface_view);
        mVolumeBrightnessLayout = findViewById(R.id.operation_volume_brightness);
        mOperationBg = (ImageView) findViewById(R.id.operation_bg);
        mOperationPercent = (ImageView) findViewById(R.id.operation_percent);

        mAudioManager = (AudioManager) this.getSystemService(AUDIO_SERVICE);
        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);//获取系统最大音量
      //  mVideoView.setVideoURI(Uri.parse("http://cn.daroonsoft.com/images/cctv_1.png"));
        mVideoView.setVideoPath(path); //视频路径
        mVideoView.requestFocus();  //获取焦点


        mMediaController = new MediaController(this); //视频控制
        mVideoView.setMediaController(mMediaController);

        mGestureDetector = new GestureDetector(this, new VideoGestureListener());
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //手势操作拦截点击事件
        if (mGestureDetector.onTouchEvent(event)) {
            return true;
        }

        switch (event.getAction()) {  //event.getAction() & MotionEvent.ACTION_MASK 处理多点触控的
            case MotionEvent.ACTION_UP:
                endGesture();
                break;
        }

        return super.onTouchEvent(event);
    }


    /**
     * 手势结束隐藏图标
     */
    private void endGesture() {
        //结束手势时,当前声音变量和亮度变量清空
        mVolume = -1;
        mBrightness = -1f;

        // 隐藏
        mDismissHandler.removeMessages(0);
        mDismissHandler.sendEmptyMessageDelayed(0, 500);
    }


    /**
     * 手势监听
     */
    //SimpleOnGestureListener 能有效的选择性重写方法,而不像OnGestureListener需要重写所有方法
    class VideoGestureListener extends GestureDetector.SimpleOnGestureListener {
        /**
         * 双击放大
         *
         * @param e
         * @return
         */
        @Override
        public boolean onDoubleTap(MotionEvent e) {

            if (mLayout == VideoView.VIDEO_LAYOUT_ZOOM) {
                mLayout = VideoView.VIDEO_LAYOUT_ORIGIN;
            } else {
                mLayout++;
            }

            if (mVideoView != null) {
                mVideoView.setVideoLayout(mLayout, 0);
            }
            return true;
        }


        /**
         * 滑动右边 声音调节
         * 滑动左边 屏幕亮度调节
         * <p/>
         * 区别下 getX getRawX distanceX
         * getX 是view的触摸位置坐标,这两个值不会超过点击view的长度和宽度
         * getRawX 是点击位置相对于屏幕的实际位置,已左上角为原点
         * distanceX  e1.getX 和 e2.getX 的水平X 距离,而不是两点的直线距离
         *
         * @return
         */
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

            float mOldX = e1.getX();
            float mOldY = e1.getY();

            float y = e2.getRawY(); //获取手势结束点在屏幕中的y坐标
            Display disp = getWindowManager().getDefaultDisplay();

            int windowWidth = disp.getWidth();
            int windowHeight = disp.getHeight();

            if (mOldX > windowWidth * 4.0 / 5) { //大于屏幕 4/5 操作右边
                //音量调节
                operateVolume((mOldY - y) / windowHeight);

            } else if (mOldX < windowWidth * 1.0 / 5) {//小于 1/5 操作左边
                //亮度调节
                operateBrightness((mOldY - y) / windowHeight);
            }

            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    /**
     * 调节亮度
     *
     * @param progress 亮度比例
     */
    private void operateBrightness(float progress) {

        //获取屏幕亮度，且微调
        if (mBrightness < 0) {
            mBrightness = getWindow().getAttributes().screenBrightness;
            if (mBrightness <= 0.00f)
                mBrightness = 0.50f;
            if (mBrightness < 0.01f)
                mBrightness = 0.01f;

            mOperationBg.setImageResource(R.drawable.video_brightness_bg);
            mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
        }

        //设置亮度
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = mBrightness + progress;
        if (lp.screenBrightness > 1.0f) {
            lp.screenBrightness = 1.0f;
        } else if (lp.screenBrightness < 0.01f) {
            lp.screenBrightness = 0.01f;
        }
        getWindow().setAttributes(lp);

        //设置进度条
        ViewGroup.LayoutParams lps = mOperationPercent.getLayoutParams();
        lps.width = (int)(findViewById(R.id.operation_full).getLayoutParams().width * lp.screenBrightness); //获取进度占背景的比例
        mOperationPercent.setLayoutParams(lps);
    }

    /**
     * 调节音量
     *
     * @param volume 音量比例
     */
    private void operateVolume(float volume) {

        //初始化声音（获取系统当前声音）
        if (mVolume == -1) {
            mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (mVolume < 0) {
                mVolume = 0;
            }
            mOperationBg.setImageResource(R.drawable.video_volumn_bg);
            mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
        }
        //得到调节后的声音大小
        int index = (int) (mMaxVolume * volume) + mVolume;

        if (index > mMaxVolume) {
            index = mMaxVolume;
        }
        if (index < 0) {
            index = 0;
        }
        //音量调节
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
        //进度条调节
        ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
        lp.width = findViewById(R.id.operation_full).getLayoutParams().width * index / mMaxVolume;
        mOperationPercent.setLayoutParams(lp);
    }

}
