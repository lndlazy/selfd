package net.xnzn.app.selfdevice.login.bean;

import java.io.Serializable;

public class ScanBean implements Serializable {


  /**
   * s : 1
   * y : 3
   * p : 155566310741450752
   * t : 1677838699651
   */

  private String s;
  private String y;
  private String p;
  private String t;

  public String getS() {
    return s;
  }

  public void setS(String s) {
    this.s = s;
  }

  public String getY() {
    return y;
  }

  public void setY(String y) {
    this.y = y;
  }

  public String getP() {
    return p;
  }

  public void setP(String p) {
    this.p = p;
  }

  public String getT() {
    return t;
  }

  public void setT(String t) {
    this.t = t;
  }

  @Override
  public String toString() {
    return "ScanBean{" +
            "s='" + s + '\'' +
            ", y='" + y + '\'' +
            ", p='" + p + '\'' +
            ", t='" + t + '\'' +
            '}';
  }
}