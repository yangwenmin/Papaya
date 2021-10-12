package com.papaya.func_video;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.papaya.R;
import com.papaya.application.ConstValues;
import com.papaya.func_video.domain.VideoStc;
import com.papaya.func_video.player.PlayerActivity;
import com.papaya.test.gsydemo.model.VideoModel;
import com.papaya.test.gsydemo.video.SampleCoverVideo;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.util.ArrayList;
import java.util.List;

public class VideoListAdapter extends BaseAdapter {

    private static final RequestOptions BANNER_OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)// 缓存SOURC和RESULT
            .dontAnimate()// 移除所有的动画
            // .fitCenter()// 该api可能 铺不满整个ImageView控件
            .centerCrop()// 按比例放大/缩小,铺满整个ImageView控件
            // .placeholder(R.drawable.shape_solid_gray_5)// 占位图
            // .transform(new CenterCrop(), new RoundedCorners(18))
            ;

    public static final String TAG = "SimpleListVideoModeAdapter";

    private List<VideoStc> list;
    private LayoutInflater inflater;
    private Context context;

    private StandardGSYVideoPlayer curPlayer;

    protected OrientationUtils orientationUtils;

    protected boolean isPlay;

    protected boolean isFull;

    public VideoListAdapter(Context context, ArrayList<VideoStc> videoStcs) {
        super();
        this.context = context;
        this.list = videoStcs;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_video_simple_mode1, null);
            holder.gsyVideoPlayer = (SampleCoverVideo) convertView.findViewById(R.id.video_item_player);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        VideoStc videoStc = list.get(position);

        // final String url = videoStc.getVideourl();

        // holder.gsyVideoPlayer.setUpLazy(videoStc.getVideourl(), true, null, null, videoStc.getVideoname());
        holder.gsyVideoPlayer.setUp(videoStc.getVideourl(), true,  videoStc.getVideoname());
        //增加title
        holder.gsyVideoPlayer.getTitleTextView().setVisibility(View.VISIBLE);
        holder.gsyVideoPlayer.getTitleTextView().setText(videoStc.getVideoname());
        holder.gsyVideoPlayer.getTitleTextView().setTextSize(14);

        //设置返回键
        holder.gsyVideoPlayer.getBackButton().setVisibility(View.GONE);
        //设置全屏按键功能
        holder.gsyVideoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.gsyVideoPlayer.startWindowFullscreen(context, false, true);
            }
        });


        //增加封面
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        Glide.with(convertView.getContext())
                .load(videoStc.getImageurl())
                .apply(BANNER_OPTIONS)
                .into(imageView);

        // imageView.setImageResource(R.mipmap.bg_splash_open);
        holder.gsyVideoPlayer.setThumbImageView(imageView);



        //防止错位设置
        holder.gsyVideoPlayer.setPlayTag(TAG);
        holder.gsyVideoPlayer.setLockLand(true);
        holder.gsyVideoPlayer.setPlayPosition(position);
        //是否根据视频尺寸，自动选择竖屏全屏或者横屏全屏，这个标志为和 setLockLand 冲突，需要和 orientationUtils 使用
        holder.gsyVideoPlayer.setAutoFullWithSize(false);
        //音频焦点冲突时是否释放
        holder.gsyVideoPlayer.setReleaseWhenLossAudio(false);
        //全屏动画
        holder.gsyVideoPlayer.setShowFullAnimation(true);
        //小屏时不触摸滑动
        holder.gsyVideoPlayer.setIsTouchWiget(false);
        //全屏是否需要lock功能
        return convertView;

    }

    class ViewHolder {
        SampleCoverVideo gsyVideoPlayer;
    }

}
