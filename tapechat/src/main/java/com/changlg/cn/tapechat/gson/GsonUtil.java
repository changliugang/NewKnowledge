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
 * Gson tool
 * Created by chang on 2016/4/18.
 */
public class GsonUtil {

    private static Gson gson = null;

    static {
        if (gson == null) {
            gson = new GsonBuilder().setPrettyPrinting().create();
        }
    }

    public GsonUtil() {
    }

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

    public static Map<?,?> jsonToMap(String jsonString){
        Map<?,?> objMap = null;
        if (gson != null) {
            objMap =  gson.fromJson(jsonString,new TypeToken<Map<?,?>>(){}.getType());
        }
        return objMap;
    }

}
