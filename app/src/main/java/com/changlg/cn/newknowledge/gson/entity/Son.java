package com.changlg.cn.newknowledge.gson.entity;

/**
 * Created by chang on 2016/4/19.
 */
public class Son {
    private int id;

    private String name;

    private String title;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Teacher [id=" + id + ", name=" + name + ", title=" + title
                + "]";
    }
}
