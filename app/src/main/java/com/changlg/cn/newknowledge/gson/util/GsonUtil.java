package com.changlg.cn.newknowledge.gson.util;

import android.text.TextUtils;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Gson工具类封装
 * Created by chang on 2016/4/18.
 */
public class GsonUtil {

    // 单例创建GsonUtil
    private static Gson gson = null;

    static {
        if (gson == null) {
            gson = new Gson();
        }
    }

    // 无参构造
    public GsonUtil() {
    }

    /**
     * 将对象转换成json格式
     * @param obj 指定对象
     * @return json格式字符串
     */
    public static String objToJson(Object obj){
        String jsonStr = null;
        if (gson != null) {
            jsonStr = gson.toJson(obj);
        }
        return jsonStr;
    }

    public static <T extends Object> T jsonToList(String jsonString, Class<?> clazz){
        List<Object> list = new ArrayList<>();
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(jsonString);
            for (int i = 0; i <jsonArray.length() ; i++) {
                String item = jsonArray.getString(i);
                Object obj = jsonToObj(item,clazz);
                list.add(obj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (T) list;
    }

    public static  <T extends Object> T jsonToObj(String jsonString, Class<?> clazz){
        Object obj = null;
        if (gson != null) {
            if(!TextUtils.isEmpty(jsonString)){
                obj = gson.fromJson(jsonString,clazz);
            }
        }
        return (T) obj;
    }

}
