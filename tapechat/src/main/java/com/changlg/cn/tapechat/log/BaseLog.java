package com.changlg.cn.tapechat.log;

import android.util.Log;

/**
 * base log
 * Created by chang on 2016/4/11.
 */
public class BaseLog {

    /**
     * Android's max limit for a log entry is ~4076 bytes,
     * so 4000 bytes is used as chunk size since default charset
     * is UTF-8
     */
    private static final int CHUNK_SIZE = 4000;

    public static void printDefault(int type, String tag, String msg) {

        byte[] bytes = msg.getBytes();
        int length = bytes.length;
        if (length <= CHUNK_SIZE) {
            printSub(type, tag, msg);
            return;
        }
        /*
        Log is too large a number of times the output is not
        finished, the CHUNK_SIZE group, multiple log output
         */
        for (int i = 0; i < length; i += CHUNK_SIZE) {
            int count = Math.min(length - i, CHUNK_SIZE);
            //create a new String with system's default charset (which is UTF-8 for Android)
            printSub(type, tag, new String(bytes, i, count));
        }
    }

    private static void printSub(int type, String tag, String sub) {
        switch (type) {
            case Loglg.V:
                Log.v(tag, sub);
                break;
            case Loglg.D:
                Log.d(tag, sub);
                break;
            case Loglg.I:
                Log.i(tag, sub);
                break;
            case Loglg.W:
                Log.w(tag, sub);
                break;
            case Loglg.E:
                Log.e(tag, sub);
                break;
            case Loglg.A:
                Log.wtf(tag, sub);
                break;
        }
    }

}
