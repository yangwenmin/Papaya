package com.papaya.func_video.haoping;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.papaya.R;
import com.papaya.application.ConstValues;
import com.papaya.func_video.domain.VideoStc;

import java.util.List;

/**
 * Created by y
 * On 2016/02/07 01:20
 */
public class VideoHaopingAdapter extends BaseAdapter {

    public static final String TAG = "VideoHaopingAdapter";

    private static final RequestOptions BANNER_OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)// 缓存SOURC和RESULT
            .dontAnimate()// 移除所有的动画
            // .fitCenter()// 该api可能 铺不满整个ImageView控件
            // .centerCrop()// 按比例放大/缩小,铺满整个ImageView控件
            .placeholder(R.drawable.shape_solid_gray_5)// 占位图
            .transform(new CenterCrop(), new RoundedCorners(18));

    private Context context;

    private List<VideoStc> lst;


    public VideoHaopingAdapter(Context context, List<VideoStc> lst) {
        this.context = context;
        this.lst = lst;
    }

    @Override
    public int getCount() {
        return lst.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(context);

            convertView = mInflater.inflate(R.layout.item_videodetail, null);
            viewHolder.titleTv = (TextView)convertView.findViewById(R.id.item_videodetail_tv_title);
            viewHolder.fengmianImg = (ImageView)convertView.findViewById(R.id.item_videodetail_img_fengmian);





            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        VideoStc stc = lst.get(position);

        viewHolder.titleTv.setText(stc.getVideoname());
        Glide.with(convertView.getContext())
                .load(stc.getImageurl())
                .apply(BANNER_OPTIONS)
                .into(viewHolder.fengmianImg);

        return convertView;
    }

    class ViewHolder {
        TextView titleTv;
        ImageView fengmianImg;
    }
}
