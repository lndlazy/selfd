package net.xnzn.app.selfdevice.query.bean;

public class YunUser {

  private long custId;
  private int custLimitId;
  private Long merchantId;
  private String custNum;
  private String custName;
  private String mobile;
  private String idCard;
  private String custPhotoUrl;
  //人员类别
  private int psnType;
  //人员状态
  private int custState;
  //卡物理号
  private String serialNum;
  private int cardType;
  private int cardStatus;
  private int photoState;
  //账户状态
  private int accStatus;
  //更新时间
  private String crtime;
  private String uptime;
  //hrcode
  private String idtemporary;
  private String orgFullName;

  private int subsidyBal;
  private int walletBal;
  private int accBal;
  //南钢新增字段
  private String birthday;


  private int nuClearMode;//核身类型

  private String panTime;//自助餐绑盘时间

  private long startWeight;//自助餐进入重量

  private long endWeight;//自助餐人离开重量

  private String panId;//自助餐盘子Id

  private long totalPrice;//自助餐消费总计

  private long totalWeigh;//自助餐已拿记录总重量

  private Long personalAccBal;//个人余额

  private int panType;//自助餐绑定托盘类型-托盘，切菜卡，清零卡，校准卡

  private int weighingValidTime;//自助餐绑定有效时间

  private int bindStatu;//0在线绑盘  1离线绑盘

  private String openid;//自助餐优化后新增字段

  private String macOrdId;//设备订单号-自助餐优化后新增字段
 
  private Integer bindType;//自助餐绑定类型1设备2微信-自助餐优化后新增字段
 
  private Integer payType;//支付方式-自助餐优化后新增字段
 
  private String payTime;//-自助餐优化后新增字段

  public YunUser() {
  }

  public YunUser(long custId, int custLimitId, Long merchantId, String custNum, String custName, String mobile, String idCard, String custPhotoUrl, int psnType, int custState, String serialNum, int cardType, int cardStatus, int photoState, int accStatus, String crtime, String uptime, String idtemporary, String orgFullName, int subsidyBal, int walletBal, int accBal, String birthday, int nuClearMode, String panTime, long startWeight, long endWeight, String panId, long totalPrice, long totalWeigh, Long personalAccBal, int panType, int weighingValidTime, int bindStatu, String openid, String macOrdId, Integer bindType, Integer payType, String payTime) {
    this.custId = custId;
    this.custLimitId = custLimitId;
    this.merchantId = merchantId;
    this.custNum = custNum;
    this.custName = custName;
    this.mobile = mobile;
    this.idCard = idCard;
    this.custPhotoUrl = custPhotoUrl;
    this.psnType = psnType;
    this.custState = custState;
    this.serialNum = serialNum;
    this.cardType = cardType;
    this.cardStatus = cardStatus;
    this.photoState = photoState;
    this.accStatus = accStatus;
    this.crtime = crtime;
    this.uptime = uptime;
    this.idtemporary = idtemporary;
    this.orgFullName = orgFullName;
    this.subsidyBal = subsidyBal;
    this.walletBal = walletBal;
    this.accBal = accBal;
    this.birthday = birthday;
    this.nuClearMode = nuClearMode;
    this.panTime = panTime;
    this.startWeight = startWeight;
    this.endWeight = endWeight;
    this.panId = panId;
    this.totalPrice = totalPrice;
    this.totalWeigh = totalWeigh;
    this.personalAccBal = personalAccBal;
    this.panType = panType;
    this.weighingValidTime = weighingValidTime;
    this.bindStatu = bindStatu;
    this.openid = openid;
    this.macOrdId = macOrdId;
    this.bindType = bindType;
    this.payType = payType;
    this.payTime = payTime;
  }

  public long getCustId() {
    return custId;
  }

  public void setCustId(long custId) {
    this.custId = custId;
  }

  public int getCustLimitId() {
    return custLimitId;
  }

  public void setCustLimitId(int custLimitId) {
    this.custLimitId = custLimitId;
  }

  public Long getMerchantId() {
    return merchantId;
  }

  public void setMerchantId(Long merchantId) {
    this.merchantId = merchantId;
  }

  public String getCustNum() {
    return custNum;
  }

  public void setCustNum(String custNum) {
    this.custNum = custNum;
  }

  public String getCustName() {
    return custName;
  }

  public void setCustName(String custName) {
    this.custName = custName;
  }

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public String getIdCard() {
    return idCard;
  }

  public void setIdCard(String idCard) {
    this.idCard = idCard;
  }

  public String getCustPhotoUrl() {
    return custPhotoUrl;
  }

  public void setCustPhotoUrl(String custPhotoUrl) {
    this.custPhotoUrl = custPhotoUrl;
  }

  public int getPsnType() {
    return psnType;
  }

  public void setPsnType(int psnType) {
    this.psnType = psnType;
  }

  public int getCustState() {
    return custState;
  }

  public void setCustState(int custState) {
    this.custState = custState;
  }

  public String getSerialNum() {
    return serialNum;
  }

  public void setSerialNum(String serialNum) {
    this.serialNum = serialNum;
  }

  public int getCardType() {
    return cardType;
  }

  public void setCardType(int cardType) {
    this.cardType = cardType;
  }

  public int getCardStatus() {
    return cardStatus;
  }

  public void setCardStatus(int cardStatus) {
    this.cardStatus = cardStatus;
  }

  public int getPhotoState() {
    return photoState;
  }

  public void setPhotoState(int photoState) {
    this.photoState = photoState;
  }

  public int getAccStatus() {
    return accStatus;
  }

  public void setAccStatus(int accStatus) {
    this.accStatus = accStatus;
  }

  public String getCrtime() {
    return crtime;
  }

  public void setCrtime(String crtime) {
    this.crtime = crtime;
  }

  public String getUptime() {
    return uptime;
  }

  public void setUptime(String uptime) {
    this.uptime = uptime;
  }

  public String getIdtemporary() {
    return idtemporary;
  }

  public void setIdtemporary(String idtemporary) {
    this.idtemporary = idtemporary;
  }

  public String getOrgFullName() {
    return orgFullName;
  }

  public void setOrgFullName(String orgFullName) {
    this.orgFullName = orgFullName;
  }

  public int getSubsidyBal() {
    return subsidyBal;
  }

  public void setSubsidyBal(int subsidyBal) {
    this.subsidyBal = subsidyBal;
  }

  public int getWalletBal() {
    return walletBal;
  }

  public void setWalletBal(int walletBal) {
    this.walletBal = walletBal;
  }

  public int getAccBal() {
    return accBal;
  }

  public void setAccBal(int accBal) {
    this.accBal = accBal;
  }

  public String getBirthday() {
    return birthday;
  }

  public void setBirthday(String birthday) {
    this.birthday = birthday;
  }

  public int getNuClearMode() {
    return nuClearMode;
  }

  public void setNuClearMode(int nuClearMode) {
    this.nuClearMode = nuClearMode;
  }

  public String getPanTime() {
    return panTime;
  }

  public void setPanTime(String panTime) {
    this.panTime = panTime;
  }

  public long getStartWeight() {
    return startWeight;
  }

  public void setStartWeight(long startWeight) {
    this.startWeight = startWeight;
  }

  public long getEndWeight() {
    return endWeight;
  }

  public void setEndWeight(long endWeight) {
    this.endWeight = endWeight;
  }

  public String getPanId() {
    return panId;
  }

  public void setPanId(String panId) {
    this.panId = panId;
  }

  public long getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(long totalPrice) {
    this.totalPrice = totalPrice;
  }

  public long getTotalWeigh() {
    return totalWeigh;
  }

  public void setTotalWeigh(long totalWeigh) {
    this.totalWeigh = totalWeigh;
  }

  public Long getPersonalAccBal() {
    return personalAccBal;
  }

  public void setPersonalAccBal(Long personalAccBal) {
    this.personalAccBal = personalAccBal;
  }

  public int getPanType() {
    return panType;
  }

  public void setPanType(int panType) {
    this.panType = panType;
  }

  public int getWeighingValidTime() {
    return weighingValidTime;
  }

  public void setWeighingValidTime(int weighingValidTime) {
    this.weighingValidTime = weighingValidTime;
  }

  public int getBindStatu() {
    return bindStatu;
  }

  public void setBindStatu(int bindStatu) {
    this.bindStatu = bindStatu;
  }

  public String getOpenid() {
    return openid;
  }

  public void setOpenid(String openid) {
    this.openid = openid;
  }

  public String getMacOrdId() {
    return macOrdId;
  }

  public void setMacOrdId(String macOrdId) {
    this.macOrdId = macOrdId;
  }

  public Integer getBindType() {
    return bindType;
  }

  public void setBindType(Integer bindType) {
    this.bindType = bindType;
  }

  public Integer getPayType() {
    return payType;
  }

  public void setPayType(Integer payType) {
    this.payType = payType;
  }

  public String getPayTime() {
    return payTime;
  }

  public void setPayTime(String payTime) {
    this.payTime = payTime;
  }

  @Override
  public String toString() {
    return "YunUser{" +
            "custId=" + custId +
            ", custLimitId=" + custLimitId +
            ", merchantId=" + merchantId +
            ", custNum='" + custNum + '\'' +
            ", custName='" + custName + '\'' +
            ", mobile='" + mobile + '\'' +
            ", idCard='" + idCard + '\'' +
            ", custPhotoUrl='" + custPhotoUrl + '\'' +
            ", psnType=" + psnType +
            ", custState=" + custState +
            ", serialNum='" + serialNum + '\'' +
            ", cardType=" + cardType +
            ", cardStatus=" + cardStatus +
            ", photoState=" + photoState +
            ", accStatus=" + accStatus +
            ", crtime='" + crtime + '\'' +
            ", uptime='" + uptime + '\'' +
            ", idtemporary='" + idtemporary + '\'' +
            ", orgFullName='" + orgFullName + '\'' +
            ", subsidyBal=" + subsidyBal +
            ", walletBal=" + walletBal +
            ", accBal=" + accBal +
            ", birthday='" + birthday + '\'' +
            ", nuClearMode=" + nuClearMode +
            ", panTime='" + panTime + '\'' +
            ", startWeight=" + startWeight +
            ", endWeight=" + endWeight +
            ", panId='" + panId + '\'' +
            ", totalPrice=" + totalPrice +
            ", totalWeigh=" + totalWeigh +
            ", personalAccBal=" + personalAccBal +
            ", panType=" + panType +
            ", weighingValidTime=" + weighingValidTime +
            ", bindStatu=" + bindStatu +
            ", openid='" + openid + '\'' +
            ", macOrdId='" + macOrdId + '\'' +
            ", bindType=" + bindType +
            ", payType=" + payType +
            ", payTime='" + payTime + '\'' +
            '}';
  }
}