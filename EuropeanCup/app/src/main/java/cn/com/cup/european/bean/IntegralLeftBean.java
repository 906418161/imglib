package cn.com.cup.european.bean;

import java.util.List;

public class IntegralLeftBean {
    private String id;
    private String name;
    private boolean isSelect = false;
    private List<IntegralRightBean>list;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<IntegralRightBean> getList() {
        return list;
    }

    public void setList(List<IntegralRightBean> list) {
        this.list = list;
    }
}
