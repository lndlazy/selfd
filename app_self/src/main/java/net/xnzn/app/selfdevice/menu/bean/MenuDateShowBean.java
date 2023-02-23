package net.xnzn.app.selfdevice.menu.bean;

public class MenuDateShowBean {

    private int id;
    private String date;
    private String week;

    public MenuDateShowBean() {
    }

    public MenuDateShowBean(int id, String date, String week) {
        this.id = id;
        this.date = date;
        this.week = week;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    @Override
    public String toString() {
        return "DateShowBean{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", week='" + week + '\'' +
                '}';
    }
}