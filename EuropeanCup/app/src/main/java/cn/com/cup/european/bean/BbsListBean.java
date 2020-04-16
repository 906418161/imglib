package cn.com.cup.european.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BbsListBean implements Serializable {

    private String bid;
    private String userId;
    private String headimg;
    private String name;
    private String isLike;
    private String isCollection;
    private String isZan;
    private String type;
    private String fine;
    private String title;
    private String content;
    private String img;
    private String time;
    private String theme;
    private String pl;
    private String zan;
    private String zuo;
    private String you;
    private String hit;
    private List<String> imgs;

    public String getHit() {
        return hit;
    }

    public void setHit(String hit) {
        this.hit = hit;
    }

    public String getIsCollection() {
        return isCollection;
    }

    public void setIsCollection(String isCollection) {
        this.isCollection = isCollection;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<String> getImgs() {
        if (imgs == null) {
            setImgs(new ArrayList<>());
        }
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        if (!img.equals("")) {
            setImgs(Arrays.asList(img.split(",")));
        }
        this.img = img;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsLike() {
        return isLike;
    }

    public void setIsLike(String isLike) {
        this.isLike = isLike;
    }

    public String getIsZan() {
        return isZan;
    }

    public void setIsZan(String isZan) {
        this.isZan = isZan;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFine() {
        return fine;
    }

    public void setFine(String fine) {
        this.fine = fine;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getPl() {
        return pl;
    }

    public void setPl(String pl) {
        this.pl = pl;
    }

    public String getZan() {
        return zan;
    }

    public void setZan(String zan) {
        this.zan = zan;
    }

    public String getZuo() {
        return zuo;
    }

    public void setZuo(String zuo) {
        this.zuo = zuo;
    }

    public String getYou() {
        return you;
    }

    public void setYou(String you) {
        this.you = you;
    }
}
