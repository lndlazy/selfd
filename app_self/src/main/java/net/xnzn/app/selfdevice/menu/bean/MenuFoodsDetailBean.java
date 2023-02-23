package net.xnzn.app.selfdevice.menu.bean;

import java.util.Arrays;

public class MenuFoodsDetailBean {

  private int foodsId;
  private String name;
  private String url;

  private int type;//类别
  private String[] tips;//评价标签

  private String saleCount;//月销
  private String goods;//好评
  private String price;//价格

  public MenuFoodsDetailBean() {
  }

  public MenuFoodsDetailBean(int foodsId, String name, String url, int type, String[] tips, String saleCount, String goods, String price) {
    this.foodsId = foodsId;
    this.name = name;
    this.type = type;
    this.url = url;
    this.tips = tips;
    this.saleCount = saleCount;
    this.goods = goods;
    this.price = price;
  }

  public int getFoodsId() {
    return foodsId;
  }

  public void setFoodsId(int foodsId) {
    this.foodsId = foodsId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String[] getTips() {
    return tips;
  }

  public void setTips(String[] tips) {
    this.tips = tips;
  }

  public String getSaleCount() {
    return saleCount;
  }

  public void setSaleCount(String saleCount) {
    this.saleCount = saleCount;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public String getGoods() {
    return goods;
  }

  public void setGoods(String goods) {
    this.goods = goods;
  }

  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  @Override
  public String toString() {
    return "MenuFoodsDetailBean{" +
            "foodsId=" + foodsId +
            ", name='" + name + '\'' +
            ", url='" + url + '\'' +
            ", type=" + type +
            ", tips=" + Arrays.toString(tips) +
            ", saleCount='" + saleCount + '\'' +
            ", goods='" + goods + '\'' +
            ", price='" + price + '\'' +
            '}';
  }
}