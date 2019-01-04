package com.ycyd.ytx.android.util;

import android.util.Log;

public class LogUtil {
    /**
     * 定义5种日志级别属性
     * */
    public static final int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int INFO = 3;
    public static final int WARN = 4;
    public static final int ERROR = 5;
    public static final int NODISPLAY = 6;
    /**定义一个标记,默认为VERBOSE,如果需要屏蔽所有日志就將VERBOSE換成 NODISPLAY*/
    public static int displayLevel = VERBOSE;
    public static void v(String tag,String s){
        //通过标记判断当前的日志级别
        if (displayLevel <= VERBOSE){
            Log.v(tag, s);
        }
    }
    public static void d(String tag,String s){
        //通过标记判断当前的日志级别
        if (displayLevel <= DEBUG){
            Log.d(tag, s);
        }
    }
    public static void i(String tag,String s){
        //通过标记判断当前的日志级别
        if (displayLevel <= INFO){
            Log.i(tag, s);
        }
    }
    public static void w(String tag,String s){
        //通过标记判断当前的日志级别
        if (displayLevel <= WARN){
            Log.w(tag, s);
        }
    }
    public static void e(String tag,String s){
        //通过标记判断当前的日志级别
        if (displayLevel <= ERROR){
            Log.e(tag, s);
        }
    }
}
