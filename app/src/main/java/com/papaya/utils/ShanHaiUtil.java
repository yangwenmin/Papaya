package com.papaya.utils;


import com.papaya.application.ConstValues;
import com.papaya.func_video.domain.CategoryStc;
import com.papaya.func_video.domain.VideoStc;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * UI工具类
 */
public class ShanHaiUtil {

    private static int picCount = ConstValues.videoUrlList.length;

    /**
     * 读取文件内容
     *
     * @param
     * @return
     */
    public static String parseFileToString(File docFile) throws Exception {

        int length = (int) docFile.length();
        byte[] buff = new byte[length];
        FileInputStream fin = new FileInputStream(docFile);
        fin.read(buff);
        fin.close();
        String result = new String(buff, "GB2312");
        if (result != null) {
            // 判断是否是乱码
            if (!(java.nio.charset.Charset.forName("GBK").newEncoder().canEncode(result))) {
                try {
                    result = new String(buff, "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }


    /**
     * 判断字符是否是中文
     *
     * @param c 字符
     * @return 是否是中文
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串是否是乱码
     *
     * @param strName 字符串
     * @return 是否是乱码
     */
    public static boolean isMessyCode(String strName) {
        Pattern p = Pattern.compile("\\s*|\t*|\r*|\n*");
        Matcher m = p.matcher(strName);
        String after = m.replaceAll("");
        String temp = after.replaceAll("\\p{P}", "");
        char[] ch = temp.trim().toCharArray();
        float chLength = ch.length;
        float count = 0;
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (!Character.isLetterOrDigit(c)) {
                if (!isChinese(c)) {
                    count = count + 1;
                }
            }
        }
        float result = count / chLength;
        if (result > 0.4) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 将字符串分割成数组
     *
     * @param result
     * @return
     */
    public static String[] splitStringToArray(String result) {

        String[] read;
        if (result.contains("\r\n")) {
            read = result.split("\r\n");
        } else if (result.contains("\r")) {
            read = result.split("\r");
        } else {
            read = result.split("\n");
        }
        return read;
    }

    /**
     * 将分类数组 转成分类集合
     *
     * @param readResult
     * @return
     */
    public static ArrayList<CategoryStc> parseStringArrayToList(String[] readResult) {

        Random rand = new Random();

        ArrayList<CategoryStc> categoryStcs = new ArrayList<>();

        CategoryStc stc;

        for (int i = 0; i < readResult.length; i++) {
            String s = readResult[i];
            // 分类
            stc = new CategoryStc();
            if (!(s.contains("_list") || s.contains("_LIST") || s.contains("双击提取文件夹中文件名") || s.contains("新建文件夹"))) {
                stc.setCategoryname(s);// 分类名称
                stc.setImageurl(ConstValues.videoUrlList[rand.nextInt(picCount)]);// 分类封面
                categoryStcs.add(stc);
            }
        }
        return categoryStcs;
    }


    /**
     * 将字符串数组转成视频对象集合
     *
     * @param readResult
     * @return
     */
    public static ArrayList<VideoStc> getVideoListByVideoArray(String categoryname, String[] readResult) {

        Random rand = new Random();

        ArrayList<VideoStc> videoStcs = new ArrayList<>();

        VideoStc stc;
        for (int i = 0; i < readResult.length; i++) {
            String s = readResult[i];
            stc = new VideoStc();
            if (!(s.contains("_list") || s.contains("_LIST") || s.contains("双击提取文件夹中文件名") || s.contains("新建文件夹"))) {
                stc.setVideourl(ConstValues.HTTPID + ConstValues.VIDEOPATH + categoryname + "/" + s);// 视频链接
                stc.setVideoname(s);// 视频名称
                stc.setImageurl(ConstValues.videoUrlList[rand.nextInt(picCount)]);// 视频封面
                videoStcs.add(stc);
            }
        }

        return videoStcs;
    }


}
