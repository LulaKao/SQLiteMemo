package com.example.sqlitememo;

public class Memo {
    private int id;
    private String memo;
    private String date;
    private String bg_color;
    private int remind;

    public Memo(int id, String date, String memo, int remind, String bg_color) {
        this.id = id;
        this.memo = memo;
        this.date = date;
        this.bg_color = bg_color;
        this.remind = remind;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBg_color() {
        return bg_color;
    }

    public void setBg_color(String bg_color) {
        this.bg_color = bg_color;
    }

    public int getRemind() {
        return remind;
    }

    public void setRemind(int remind) {
        this.remind = remind;
    }
}
