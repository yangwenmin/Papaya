package com.kylin.core.banner;

import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.kylin.core.R;
import com.kylin.core.app.Latte;
import com.kylin.core.initbase.InitValues;

/**
 * Created by Sai on 15/8/4.
 * 本地图片Holder例子
 */
public class LocalImageHolderView extends Holder<String> {
    private ImageView imageView;

    public static final RequestOptions BANNER_OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)// 缓存SOURC和RESULT
            .dontAnimate()// 移除所有的动画
            // .fitCenter()// 该api可能 铺不满整个ImageView控件
            .centerCrop()// 按比例放大/缩小,铺满整个ImageView控件
            // .placeholder(R.drawable.shape_solid_gray_5)// 占位图
            // .transform(new CenterCrop(), new RoundedCorners(18))
            ;

    public LocalImageHolderView(View itemView) {
        super(itemView);
    }

    @Override
    protected void initView(View itemView) {
        imageView = itemView.findViewById(R.id.ivPost);
    }

    @Override
    public void updateUI(String data) {
        Glide.with(Latte.getApplicationContext())
                .load(data)
                .apply(BANNER_OPTIONS)
                .into(imageView);
    }
}
