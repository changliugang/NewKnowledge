package com.changlg.cn.newknowledge.gson;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.changlg.cn.newknowledge.R;
import com.changlg.cn.newknowledge.constant.TestBean;
import com.changlg.cn.newknowledge.gson.entity.Father;
import com.changlg.cn.newknowledge.gson.entity.FormData;
import com.changlg.cn.newknowledge.gson.entity.Person;
import com.changlg.cn.newknowledge.gson.entity.Point;
import com.changlg.cn.newknowledge.gson.entity.Son;
import com.changlg.cn.newknowledge.toolbar.BaseToolbarActivity;
import com.changlg.cn.tapechat.gson.GsonUtil;
import com.changlg.cn.tapechat.log.Loglg;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 测试GsonUtil
 */
public class GsonActivity extends BaseToolbarActivity {

    private static final String TAG = "GsonActivity";
    
    @InjectView(R.id.id_show_text)
    TextView ShowText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gson);
        ButterKnife.inject(this);
        // GsonBuilder的常用配置
//        Gson gson = new GsonBuilder()
//                .excludeFieldsWithoutExposeAnnotation()//不导出实体中没有用@Expose注解的属性
//                .setDateFormat("yyyy-MM-dd HH:mm:ss:SSS")//Date类型时间转化为特定格式
//                .setPrettyPrinting()//格式化Json
//                .serializeNulls()// 序列化null字段，如："name":null，默认null字段是不转换的
//                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)//会把字段首字母大写,注:对于实体上使用了@SerializedName注解的不会生效.
//                .create();

        // 实体转Json
        String personJson = GsonUtil.objToJson(TestBean.gsonBean());
        Person personTran = GsonUtil.jsonToObj(personJson, Person.class);// Json转实体

        ShowText.setText("GsonUtil:" + personJson + "\n" + personTran.toString());
        showMap();
        jsonMapWithValueList();
        specialJson();// 略复杂的json解析，要灵活运用
    }

    // 默认点击NavigationIcon是关闭当前Activity,如果有界面需要自定义，复写
    // onOptionsItemSelected并结尾return true
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            Toast.makeText(GsonActivity.this, "点我干嘛", Toast.LENGTH_SHORT).show();
        return true;
    }

    /**
     * Json解析为Map
     */
    private void showMap() {
        // 配置Gson
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
                .setPrettyPrinting()//格式化Json
                .serializeNulls()// 序列化null字段，如："name":null，默认null字段是不转换的
                .create();

        // key为String的map
        // 实体类转换为Json
        String mapJson = GsonUtil.objToJson(TestBean.StringkvMap());
        Loglg.d("GsonUtil", mapJson);
        // Json转换为实体类
        Map<String, Point> resultMap = (Map<String, Point>) GsonUtil.jsonToMap(mapJson);
        // 输出Log
        for (String item : resultMap.keySet()) {
            Loglg.d("GsonUtil", "key:" + item + " values:" + resultMap.get(item));
        }

        // key为object的map( 当Map的Key为实体类的时候，GsonUtil就不适用了，可自行解析。当然这种情况并不常见)
        // 实体类转换为Json
        String s = gson.toJson(TestBean.gsonObjKeyMap());
        Loglg.d(TAG, s);
        // Json转换为实体类
        Map<Point, String> retMap = gson.fromJson(s,
                new TypeToken<Map<Point, String>>() {
                }.getType());
        // 输出Log
        for (Point p : retMap.keySet()) {
            Loglg.d(TAG, "key:" + p + " values:" + retMap.get(p));
        }

    }

    /**
     * Key为String，Value为List的实体解析
     */
    private void jsonMapWithValueList() {
        // 实体类转换为Json
        String str = GsonUtil.objToJson(TestBean.gsonMapStringKeyListValue());
        Loglg.d(TAG, str);
        // Json转换为实体类
        Map<String, Object> retMap = (Map<String, Object>) GsonUtil.jsonToMap(str);
        // 输出Log
        for (String key : retMap.keySet()) {
            Log.d(TAG, "key:" + key + " values:" + retMap.get(key));
            if (key.equals("fathers")) {
                List<Father> stuList = (List<Father>) retMap.get(key);
                Loglg.d(TAG,stuList.toString());
            } else if (key.equals("sons")) {
                List<Son> tchrList = (List<Son>) retMap.get(key);
                Loglg.d(TAG, tchrList.toString());
            }
        }
    }

    /**
     * 一个特殊的Json结构解析，旨在告诉大家要灵活运用工具
     */
    private void specialJson() {
        // 实体类转换为Json
        String listJosn = GsonUtil.objToJson(TestBean.gsonSpecialJsonList());
        Loglg.d(TAG, listJosn);
        // Json转换为实体类
        List<FormData> tableDatas2 = GsonUtil.jsonToList(listJosn, FormData.class);
        // 输出Log
        for (int i = 0; i < tableDatas2.size(); i++) {
            FormData entityData = tableDatas2.get(i);
            String tableName = entityData.getFormName();
            List tableData = entityData.getFormData();
            String s2 =GsonUtil.objToJson(tableData);
            if (tableName.equals("sons")) {
                List<Son> retStuList = GsonUtil.jsonToList(s2, Son.class);
                for (int j = 0; j < retStuList.size(); j++) {
                    Loglg.d("GsonUtil", "sons:" + retStuList.get(j));
                }
            } else if (tableName.equals("fathers")) {
                List<Father> retTchrList = GsonUtil.jsonToList(s2, Father.class);
                for (int j = 0; j < retTchrList.size(); j++) {
                    Loglg.d("GsonUtil", "fathers:" + retTchrList.get(j));
                }
            }
        }
    }
}
