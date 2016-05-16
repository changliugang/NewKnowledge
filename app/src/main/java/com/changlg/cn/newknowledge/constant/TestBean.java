package com.changlg.cn.newknowledge.constant;

import com.changlg.cn.newknowledge.gson.entity.Father;
import com.changlg.cn.newknowledge.gson.entity.FormData;
import com.changlg.cn.newknowledge.gson.entity.Person;
import com.changlg.cn.newknowledge.gson.entity.Point;
import com.changlg.cn.newknowledge.gson.entity.Son;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * demo测试数据生成类
 * Created by chang on 2016/5/12.
 */
public class TestBean {


    public static Person gsonBean() {
        // 普通实体类
        return new Person(1, "Luck", 26, new Date());
    }

    public static Map<String, Object> gsonMapStringKeyListValue() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("fathers", TestBean.gsonMapFatherList());
        map.put("sons", TestBean.gsonMapSonList());
        return map;
    }

    public static Map<Point, String> gsonObjKeyMap() {
        Map<Point, String> objKeyMap = new LinkedHashMap<>();// 使用LinkedHashMap将结果按先进先出顺序排列
        objKeyMap.put(new Point(5, 6), "a");
        objKeyMap.put(new Point(8, 8), "b");
        return objKeyMap;
    }

    public static Map<String, Point> StringkvMap() {
        Map<String, Point> StringkvMap = new LinkedHashMap<>();
        StringkvMap.put("name", new Point(1, 2));
        StringkvMap.put("age", new Point(4, 3));
        return StringkvMap;
    }

    public static List<Father> gsonMapFatherList() {

        Father father1 = new Father();
        father1.setId(1);
        father1.setName("Tom");
        father1.setBirthDay(new Date());

        Father father2 = new Father();
        father2.setId(2);
        father2.setName("Dexter");
        father2.setBirthDay(new Date());

        Father father3 = new Father();
        father3.setId(3);
        father3.setName("Barry");
        father3.setBirthDay(new Date());

        List<Father> fatherList = new ArrayList<>();
        fatherList.add(father1);
        fatherList.add(father2);
        fatherList.add(father3);

        return fatherList;
    }

    public static List<Son> gsonMapSonList() {

        Son son1 = new Son();
        son1.setId(1);
        son1.setName("小明");
        son1.setTitle("大队长");

        Son son2 = new Son();
        son2.setId(2);
        son2.setName("小刚");
        son2.setTitle("中队长");

        Son son3 = new Son();
        son3.setId(3);
        son3.setName("小李");
        son3.setTitle("小队长");

        List<Son> sonList = new ArrayList<>();
        sonList.add(son1);
        sonList.add(son2);
        sonList.add(son3);

        return sonList;
    }

    public static List<FormData> gsonSpecialJsonList() {
        FormData formData1 = new FormData();
        formData1.setFormName("fathers");
        formData1.setFormData(gsonMapFatherList());

        FormData formData2 = new FormData();
        formData2.setFormName("sons");
        formData2.setFormData(gsonMapSonList());

        List<FormData> list = new ArrayList<>();
        list.add(formData1);
        list.add(formData2);

        return list;
    }

}
