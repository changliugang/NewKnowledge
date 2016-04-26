package com.changlg.cn.newknowledge.gson;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.changlg.cn.newknowledge.R;
import com.changlg.cn.newknowledge.gson.entity.Father;
import com.changlg.cn.newknowledge.gson.entity.FormData;
import com.changlg.cn.newknowledge.gson.entity.Person;
import com.changlg.cn.newknowledge.gson.entity.Point;
import com.changlg.cn.newknowledge.gson.entity.Son;
import com.changlg.cn.tapechat.gson.GsonUtil;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class GsonActivity extends AppCompatActivity {

    @InjectView(R.id.id_show_text)
    TextView ShowText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gson);
        ButterKnife.inject(this);

        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()//不导出实体中没有用@Expose注解的属性
                .setDateFormat("yyyy-MM-dd HH:mm:ss:SSS")//Date类型时间转化为特定格式
                .setPrettyPrinting()//格式化Json
                .serializeNulls()// 序列化null字段，如："name":null，默认null字段是不转换的
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)//会把字段首字母大写,注:对于实体上使用了@SerializedName注解的不会生效.
                .create();
        String name = null;
        Person person = new Person(1, name, 26, new Date());

        String personJson = GsonUtil.objToJson(person);
        Person personTran =  GsonUtil.jsonToObj(personJson, Person.class);
        ShowText.setText("GsonUtil:"+personJson + "\n" + personTran.toString());

        setUpListData();
//        showMap();
//        jsonMapWithValueList();
        specialJson();
    }


    private void showMap() {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
                .setPrettyPrinting()//格式化Json
                .serializeNulls()// 序列化null字段，如："name":null，默认null字段是不转换的
                .create();
        // key为object的map
        Map<Point, String> map1 = new LinkedHashMap<>();// 使用LinkedHashMap将结果按先进先出顺序排列
        map1.put(new Point(5, 6), "a");
        map1.put(new Point(8, 8), "b");
        String s = gson.toJson(map1);
//        String s = GsonUtil.objToJson(map1);
        Log.d("chang", s);

        Map<Point, String> retMap = gson.fromJson(s,
                new TypeToken<Map<Point, String>>() {
                }.getType());
        for (Point p : retMap.keySet()) {
            Log.d("chang", "key:" + p + " values:" + retMap.get(p));
        }

        // key为String的map
        Map<String, Point> StringkvMap = new LinkedHashMap<>();
        StringkvMap.put("name", new Point(1, 2));
        StringkvMap.put("age", new Point(4, 3));

//        String mapJson = gson.toJson(StringkvMap);
        String mapJson = GsonUtil.objToJson(StringkvMap);

        Log.d("GsonUtil", mapJson);

//        Map<String, Point> resultMap = gson.fromJson(mapJson,
//                new TypeToken<Map<String, Point>>() {
//                }.getType());
        Map<String, Point> resultMap = (Map<String, Point>) GsonUtil.jsonToMap(mapJson);
        for (String item : resultMap.keySet()) {
            Log.d("GsonUtil", "key:" + item + " values:" + resultMap.get(item));
        }

    }


    List<Father> fatherList;
    List<Son> sonList;

    private void jsonMapWithValueList() {



        Map<String, Object> map = new LinkedHashMap<>();
        map.put("fathers", fatherList);
        map.put("sons", sonList);

        Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
                .setPrettyPrinting()//格式化Json
                .serializeNulls()// 序列化null字段，如："name":null，默认null字段是不转换的
                .create();
        String str = gson.toJson(map);
        Log.d("chang", str);

        Map<String, Object> retMap = gson.fromJson(str,
                new TypeToken<Map<String, List<Object>>>() {
                }.getType());

        for (String key : retMap.keySet()) {
            Log.d("chang", "key:" + key + " values:" + retMap.get(key));
            if (key.equals("fathers")) {
                List<Father> stuList = (List<Father>) retMap.get(key);
                System.out.println(stuList);
                Log.d("chang", str);
            } else if (key.equals("sons")) {
                List<Son> tchrList = (List<Son>) retMap.get(key);
                System.out.println(tchrList);

            }
        }
    }

    private void setUpListData() {
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

        fatherList = new ArrayList<>();
        fatherList.add(father1);
        fatherList.add(father2);
        fatherList.add(father3);

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

        sonList = new ArrayList<>();
        sonList.add(son1);
        sonList.add(son2);
        sonList.add(son3);
    }


    private void specialJson() {

        FormData formData1 = new FormData();
        formData1.setFormName("fathers");
        formData1.setFormData(fatherList);

        FormData formData2 = new FormData();
        formData2.setFormName("sons");
        formData2.setFormData(sonList);

        List<FormData> list = new ArrayList<>();
        list.add(formData1);
        list.add(formData2);

        Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
                .setPrettyPrinting()//格式化Json
                .serializeNulls()// 序列化null字段，如："name":null，默认null字段是不转换的
                .create();
        String listJosn =  gson.toJson(list);

        Log.d("chang", listJosn);
        List<FormData> tableDatas2 = GsonUtil.jsonToList(listJosn, FormData.class);
        // 将json转为数据-->start
//        List<FormData> tableDatas2 = gson.fromJson(listJosn,
//                new TypeToken<List<FormData>>() {
//                }.getType());
        for (int i = 0; i < tableDatas2.size(); i++) {
            FormData entityData = tableDatas2.get(i);
            String tableName = entityData.getFormName();
            List tableData = entityData.getFormData();
            String s2 = gson.toJson(tableData);
            if (tableName.equals("sons")) {
                System.out.println("sons");
                List<Son> retStuList = gson.fromJson(s2,
                        new TypeToken<List<Son>>() {
                        }.getType());
                for (int j = 0; j < retStuList.size(); j++) {
                    System.out.println("GsonUtil.jsonToList:"+retStuList.get(j));
                }

            } else if (tableName.equals("fathers")) {
                System.out.println("fathers");
                List<Father> retTchrList = gson.fromJson(s2,
                        new TypeToken<List<Father>>() {
                        }.getType());
                for (int j = 0; j < retTchrList.size(); j++) {
                    System.out.println("GsonUtil.jsonToList:"+retTchrList.get(j));
                }
            }
        }

    }

}
