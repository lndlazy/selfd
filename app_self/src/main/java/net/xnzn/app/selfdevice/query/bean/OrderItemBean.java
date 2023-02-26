package net.xnzn.app.selfdevice.query.bean;

import java.util.Arrays;
import java.util.Date;

public class OrderItemBean {

  private String type;
  private Date date;
  private String title;
  private String status;
  private String price;
  private int count;
  private String[] urls;

  public OrderItemBean() {
  }

  public OrderItemBean(String type, Date date, String title, String status, String price, int count, String[] urls) {
    this.type = type;
    this.date = date;
    this.title = title;
    this.status = status;
    this.price = price;
    this.count = count;
    this.urls = urls;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public String[] getUrls() {
    return urls;
  }

  public void setUrls(String[] urls) {
    this.urls = urls;
  }

  @Override
  public String toString() {
    return "OrderItemBean{" +
            "type='" + type + '\'' +
            ", date=" + date +
            ", title='" + title + '\'' +
            ", status='" + status + '\'' +
            ", price='" + price + '\'' +
            ", count=" + count +
            ", urls=" + Arrays.toString(urls) +
            '}';
  }
}