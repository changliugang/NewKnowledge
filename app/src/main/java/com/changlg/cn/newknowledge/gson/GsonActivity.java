package com.changlg.cn.newknowledge.gson;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.changlg.cn.newknowledge.R;
import com.changlg.cn.newknowledge.gson.entity.Person;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

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
        String personJson = gson.toJson(person, Person.class);
        Person personTran = gson.fromJson(personJson, Person.class);
        ShowText.setText(personJson + "\n" + personTran.toString());
    }


}
