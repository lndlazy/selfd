package net.xnzn.app.selfdevice.menu.bean;

public class MenuTypeBean {

  private int typeId;
  private String name;

  public MenuTypeBean() {
  }

  public MenuTypeBean(int typeId, String name) {
    this.typeId = typeId;
    this.name = name;
  }

  public int getTypeId() {
    return typeId;
  }

  public void setTypeId(int typeId) {
    this.typeId = typeId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "MenuTypeBean{" +
            "typeId=" + typeId +
            ", name='" + name + '\'' +
            '}';
  }
}