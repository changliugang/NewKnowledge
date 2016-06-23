package com.changlg.cn.tapechat.log;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Json format Log
 * Created by chang on 2016/4/11.
 */
public class JsonLog {

    public static void printJson(String tag, String msg, String headString) {
        String message;
        try {
            if (msg.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(msg);
                // toString function can make JSONObject or JSONArray contains Json information become more easy to read
                message = jsonObject.toString(Loglg.JSON_INDENT);
            } else if (msg.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(msg);
                message = jsonArray.toString(Loglg.JSON_INDENT);
            } else {
                message = msg;
            }
        } catch (JSONException e) {
            message = msg;
        }
        Util.printLine(tag, true);
        message = headString + Loglg.LINE_SEPARATOR +message;
        String[] lines = message.split( Loglg.LINE_SEPARATOR);
        for (String line:lines) {
            Log.d(tag, "|| "+line);
        }
        Util.printLine(tag, false);
    }
}
