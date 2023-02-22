package net.xnzn.app.selfdevice.home;

public class HomeItem {

    private int resId;
    private String content;
    private String  desc;

    public HomeItem() {
    }

    public HomeItem(int resId, String content, String desc) {
        this.resId = resId;
        this.content = content;
        this.desc = desc;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
