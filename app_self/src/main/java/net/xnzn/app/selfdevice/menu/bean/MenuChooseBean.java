package net.xnzn.app.selfdevice.menu.bean;

public class MenuChooseBean {

    private String name;
    private String picUrl;
    private int id;

    public MenuChooseBean() {
    }

    public MenuChooseBean(int id,String name, String picUrl) {
        this.name = name;
        this.picUrl = picUrl;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "MenuChooseBean{" +
                "name='" + name + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", id=" + id +
                '}';
    }
}
