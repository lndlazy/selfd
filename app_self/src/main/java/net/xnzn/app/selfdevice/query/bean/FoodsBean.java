package net.xnzn.app.selfdevice.query.bean;

public class FoodsBean {

    private int id;
    private String url;
    private String name;
    private int start;

    public FoodsBean() {
    }

    public FoodsBean(int id, String url, String name) {
        this.id = id;
        this.url = url;
        this.name = name;
    }

    public FoodsBean(int id, String url, String name, int start) {
        this.id = id;
        this.url = url;
        this.name = name;
        this.start = start;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    @Override
    public String toString() {
        return "FoodsBean{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", name='" + name + '\'' +
                ", start=" + start +
                '}';
    }
}