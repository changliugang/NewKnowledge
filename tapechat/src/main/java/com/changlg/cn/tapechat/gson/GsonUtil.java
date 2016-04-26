package com.changlg.cn.tapechat.gson;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Gson工具类封装
 * Created by chang on 2016/4/18.
 */
public class GsonUtil {

    // 单例创建GsonUtil
    private static Gson gson = null;

    static {
        if (gson == null) {
            gson = new GsonBuilder().setPrettyPrinting().create();
        }
    }

    // 无参构造
    public GsonUtil() {
    }

    /**
     * 将对象转换成json格式字符串
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

    /**
     * 将json格式字符串转换成List集合
     * @param jsonString json格式字符串
     * @param clazz 集合元素泛型
     * @param <T> 集合对象
     * @return List集合
     */
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

    /**
     * 将json格式字符串转换成对象
     * @param jsonString json格式字符串
     * @param clazz 对象类型
     * @param <T> 对象
     * @return 指定对象
     */
    public static  <T extends Object> T jsonToObj(String jsonString, Class<?> clazz){
        Object obj = null;
        if (gson != null) {
            if(!TextUtils.isEmpty(jsonString)){
                obj = gson.fromJson(jsonString,clazz);
            }
        }
        return (T) obj;
    }

    /**
     * 将json格式转换成map对象
     * @param jsonString json格式字符串
     * @return map对象
     */
    public static Map<?,?> jsonToMap(String jsonString){
        Map<?,?> objMap = null;
        if (gson != null) {
            objMap =  gson.fromJson(jsonString,new TypeToken<Map<?,?>>(){}.getType());
        }
        return objMap;
    }

}
