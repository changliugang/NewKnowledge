package com.changlg.cn.newknowledge.gson.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Gson测试实体类，人
 * Created by chang on 2016/4/18.
 */
public class Person {

    private int id;
    @Expose
    private String name;
    @Expose
    private int age;
    @Expose
    @SerializedName("bday")// 命名序列化后的字段名为bday
    private Date birthday;
    @Expose
    private double height;

    public Person() {
    }

    public Person(int id, String name, int age, Date birthday) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.birthday = birthday;
    }

    public Person(int id, String name, int age, Date birthday, double height) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.birthday = birthday;
        this.height = height;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", birthday=" + birthday +
                '}';
    }
}
