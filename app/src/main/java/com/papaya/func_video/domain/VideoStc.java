package com.papaya.func_video.domain;

import java.io.Serializable;

/**
 * Created by ywm on 2021/9/7
 */
public class VideoStc implements Serializable {
    private String category;// 分类
    private String imageurl;//
    private String videoname;
    private String videourl;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getVideoname() {
        return videoname;
    }

    public void setVideoname(String videoname) {
        this.videoname = videoname;
    }
}
