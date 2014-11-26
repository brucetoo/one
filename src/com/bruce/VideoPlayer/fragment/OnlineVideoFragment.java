package com.bruce.VideoPlayer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.bruce.VideoPlayer.OnlineVideoPlayerActivity;
import com.bruce.VideoPlayer.R;

/**
 * Class Function:
 * Created By Bruce Too
 * On 2014-11-18 下午 3:42
 */
public class OnlineVideoFragment extends BaseFragment {

    private View view;
    private ListView onlineList;
    private OnlineAdapter adapter;

    @Override
    protected void initData(Bundle savedInstanceState) {

        onlineList = (ListView) view.findViewById(R.id.online_list);
        adapter = new OnlineAdapter();
        onlineList.setAdapter(adapter);
        onlineList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), OnlineVideoPlayerActivity.class);
                switch (i) {
                    case 0:
                        // intent.putExtra("path","http://192.168.56.1:8080/test.avi");
                        intent.putExtra("path", "http://live.3gv.ifeng.com/zixun.m3u8");
                        startActivity(intent);
                        break;
                    case 1:
                        intent.putExtra("path", "http://live.3gv.ifeng.com/live/zixun.m3u8?fmt=x264_0k_mpegts&size=320x240");
                        startActivity(intent);
                        break;
                }
            }
        });

    }

    /**
     * 获取视频列表中的视频信息
     */
    private void initVideoInfo(String url) {
        /*Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever(getActivity());
        int kind = MediaStore.Video.Thumbnails.MINI_KIND;
        try {
          *//*  if (Build.VERSION.SDK_INT >= 14) {
                retriever.setDataSource(url, new HashMap<String, String>());
            } else {*//*
                retriever.setDataSource(url);
          //  }
            bitmap = retriever.getFrameAtTime(0);
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (Exception ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }
        if (kind == android.provider.MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }*/
    }


    @Override
    protected View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.online_video, null);
        return view;
    }


    class OnlineAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 2;
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
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.online_video_item, null);
            }

            if (i == 1) {
                ((TextView) convertView.findViewById(R.id.online_title)).setText("凤凰卫视");
            } else {
                ((TextView) convertView.findViewById(R.id.online_title)).setText("凤凰资讯");
            }

            return convertView;
        }
    }

}
