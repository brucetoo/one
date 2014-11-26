package com.bruce.VideoPlayer.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.bruce.VideoPlayer.R;
import com.bruce.VideoPlayer.VideoPlayerActivity;
import com.bruce.VideoPlayer.bean.MediaBean;
import com.bruce.VideoPlayer.utils.FileUtils;
import com.bruce.VideoPlayer.utils.PromptManager;
import com.bruce.VideoPlayer.utils.VideoUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Class Function:
 * Created By Bruce Too
 * On 2014-11-18 下午 3:42
 */
public class LocalVideoFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private ListView mTempListView;
    private ListView mListView;
    private VideoAdapter adapter;
    private View view;
    private ArrayList<MediaBean> fileArrayList;

    //A-Z
    private TextView first_letter_overlay;
    private ImageView alphabet_scroller;

    @Override
    protected void initData(Bundle savedInstanceState) {

        fileArrayList = new ArrayList<MediaBean>();

        getFileData();

        adapter = new VideoAdapter();
        first_letter_overlay = (TextView) view.findViewById(R.id.first_letter_overlay);
        alphabet_scroller = (ImageView) view.findViewById(R.id.alphabet_scroller);
        mListView = (ListView) view.findViewById(android.R.id.list);

        mListView.setAdapter(adapter);
        alphabet_scroller.setClickable(true);
        alphabet_scroller.setOnTouchListener(asOnTouch);
        mListView.setOnItemClickListener(this);
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.local_video_list, null);
        return view;
    }


    /**
     * A-Z 点击
     */
    private View.OnTouchListener asOnTouch = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:// 0
                    alphabet_scroller.setPressed(true);
                    first_letter_overlay.setVisibility(View.VISIBLE);
                    mathScrollerPosition(event.getY());
                    break;
                case MotionEvent.ACTION_UP:// 1
                    alphabet_scroller.setPressed(false);
                    first_letter_overlay.setVisibility(View.GONE);
                    break;
                case MotionEvent.ACTION_MOVE:
                    mathScrollerPosition(event.getY());
                    break;
            }
            return false;
        }
    };


    /**
     * 显示字符
     *
     * @param y
     */
    private void mathScrollerPosition(float y) {
        int height = alphabet_scroller.getHeight();
        float charHeight = height / 28.0f;
        char c = 'A';
        if (y < 0)
            y = 0;
        else if (y > height)
            y = height;

        int index = (int) (y / charHeight) - 1;
        if (index < 0)
            index = 0;
        else if (index > 25)
            index = 25;

        String key = String.valueOf((char) (c + index));
        first_letter_overlay.setText(key);

        int position = 0;
        if (index == 0)
            mListView.setSelection(0);
        else if (index == 25)
            mListView.setSelection(adapter.getCount() - 1);
        else {
            if (adapter != null && fileArrayList != null) {
                for (MediaBean item : fileArrayList) {
                    if (item.title_pinyin.startsWith(key)) {
                        mListView.setSelection(position);
                        break;
                    }
                    position++;
                }
            }
        }
    }

    /**
     * 读取手机中的视频文件
     */
    public void getFileData() {
        new AsyncTask<Void, File, Void>() {

            @Override
            protected void onPreExecute() {
                PromptManager.showProgressDialog(getActivity());
            }

            @Override
            protected Void doInBackground(Void... voids) {
                //getAllMedias(Environment.getExternalStorageDirectory());
                fileArrayList.addAll(VideoUtil.getInstance().initVideos(getActivity()));
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                PromptManager.closeProgressDialog();

                Collections.sort(fileArrayList, new Comparator<MediaBean>() {
                    @Override
                    public int compare(MediaBean mediaBean, MediaBean mediaBean2) {
                        return mediaBean.title_pinyin.compareToIgnoreCase(mediaBean2.title_pinyin);
                    }
                });
                adapter.notifyDataSetChanged();
            }
        }.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        MediaBean file = fileArrayList.get(i);
        Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
        intent.putExtra("path", file.path);
        startActivity(intent);
    }

    class VideoAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return fileArrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.local_video_item, null);
            }
            ((TextView) convertView.findViewById(R.id.title)).setText(fileArrayList.get(position).title);
            Long size = fileArrayList.get(position).size;
            ((TextView) convertView.findViewById(R.id.size)).setText(FileUtils.showFileSize(size));
            Long time = fileArrayList.get(position).time;
            SimpleDateFormat format1 = new SimpleDateFormat("mm:ss");
                    ((TextView) convertView.findViewById(R.id.time)).setText(String.valueOf(format1.format(time)));

         //  if(!TextUtils.isEmpty(fileArrayList.get(position).thumb_path)) {
               //略缩图
               //缩略图类型：MINI_KIND FULL_SCREEN_KIND MICRO_KIND
               Bitmap miniThumb = android.provider.MediaStore.Video.Thumbnails.getThumbnail(getActivity().getContentResolver(),fileArrayList.get(position)._id,
                       android.provider.MediaStore.Video.Thumbnails.MINI_KIND, null);

               ((ImageView) convertView.findViewById(R.id.image)).setImageBitmap(miniThumb);
        //   }
            return convertView;
        }
    }
}
