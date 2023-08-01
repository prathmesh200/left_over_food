package com.example.project;

import android.graphics.Bitmap;

import java.util.List;

public class HandelRecyclerView {

    private int imageView;
    private int imageView2;
    int i;
    private String textView1;
    private String textView2;
    private String textView3;
    private DB_DONAR db_donar;
    private String type,name;

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HandelRecyclerView(String name, String type, int i, DB_DONAR db_donar, int imageView, int imageView2, String textView1, String textView2, String textView3) {
        this.name = name;
        this.type = type;
        this.i = i;
        this.db_donar = db_donar;
        this.imageView = imageView;
        this.imageView2 = imageView2;
        this.textView1 = textView1;
        this.textView2 = textView2;
        this.textView3 = textView3;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getImageView2() {
        return imageView2;
    }

    public void setImageView2(int imageView2) {
        this.imageView2 = imageView2;
    }

    public DB_DONAR getDb_donar() {
        return db_donar;
    }

    public void setDb_donar(DB_DONAR db_donar) {
        this.db_donar = db_donar;
    }

    public int getImageView() {
        return imageView;
    }

    public void setImageView(int imageView) {
        this.imageView = imageView;
    }

    public String getTextView1() {
        return textView1;
    }

    public void setTextView1(String textView1) {
        this.textView1 = textView1;
    }

    public String getTextView2() {
        return textView2;
    }

    public void setTextView2(String textView2) {
        this.textView2 = textView2;
    }

    public String getTextView3() {
        return textView3;
    }

    public void setTextView3(String textView3) {
        this.textView3 = textView3;
    }
}
