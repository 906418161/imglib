package cn.com.cup.european.bean;

import java.io.Serializable;

public class HomeHotBean implements Serializable {

    private String mid;
    private String name;
    private String time;
    private String t1name;
    private String t1img;
    private String score;
    private String type;//0未开始1已结束
    private String t2name;
    private String t2img;

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getT1name() {
        return t1name;
    }

    public void setT1name(String t1name) {
        this.t1name = t1name;
    }

    public String getT1img() {
        return t1img;
    }

    public void setT1img(String t1img) {
        this.t1img = t1img;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getT2name() {
        return t2name;
    }

    public void setT2name(String t2name) {
        this.t2name = t2name;
    }

    public String getT2img() {
        return t2img;
    }

    public void setT2img(String t2img) {
        this.t2img = t2img;
    }
}
