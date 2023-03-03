package net.xnzn.app.selfdevice.sign.bean.Resp;

import java.io.Serializable;
import java.util.List;

public class BookInfo implements Serializable {
    /**
     * custId : 0
     * ifPlaceOrder : 0
     * intervalDate : string
     * intervalId : 0
     * intervalName : string
     * ordId : 0
     */

    private Long custId;
    private int ifPlaceOrder; //ifPlaceOrder	  是否已下单 1 是 2 否
    private String intervalDate;
    private Long intervalId;
    private String intervalName;
    private Long ordId;

    private String merchantName;
    private String shopstallName;

    // "ifPlaceOrder": 1,
    // "intervalState": 2,

    private int intervalState;  //餐次状态  1 可以下单  2 不可以下单
    private int ifWriteOff; // 1-已核销 2-未核销
    private int amount;


    //订单详情
    private List<Long> ordIdList;

    public Long getCustId() {
        return custId;
    }

    public void setCustId(Long custId) {
        this.custId = custId;
    }

    public int getIfPlaceOrder() {
        return ifPlaceOrder;
    }

    public void setIfPlaceOrder(int ifPlaceOrder) {
        this.ifPlaceOrder = ifPlaceOrder;
    }

    public String getIntervalDate() {
        return intervalDate;
    }

    public void setIntervalDate(String intervalDate) {
        this.intervalDate = intervalDate;
    }

    public Long getIntervalId() {
        return intervalId;
    }

    public void setIntervalId(Long intervalId) {
        this.intervalId = intervalId;
    }

    public String getIntervalName() {
        return intervalName;
    }

    public void setIntervalName(String intervalName) {
        this.intervalName = intervalName;
    }

    public Long getOrdId() {
        return ordId;
    }

    public void setOrdId(Long ordId) {
        this.ordId = ordId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getShopstallName() {
        return shopstallName;
    }

    public void setShopstallName(String shopstallName) {
        this.shopstallName = shopstallName;
    }

    public int getIntervalState() {
        return intervalState;
    }

    public void setIntervalState(int intervalState) {
        this.intervalState = intervalState;
    }

    public int getIfWriteOff() {
        return ifWriteOff;
    }

    public void setIfWriteOff(int ifWriteOff) {
        this.ifWriteOff = ifWriteOff;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public List<Long> getOrdIdList() {
        return ordIdList;
    }

    public void setOrdIdList(List<Long> ordIdList) {
        this.ordIdList = ordIdList;
    }

    @Override
    public String toString() {
        return "BookInfo{" +
                "custId=" + custId +
                ", ifPlaceOrder=" + ifPlaceOrder +
                ", intervalDate='" + intervalDate + '\'' +
                ", intervalId=" + intervalId +
                ", intervalName='" + intervalName + '\'' +
                ", ordId=" + ordId +
                ", merchantName='" + merchantName + '\'' +
                ", shopstallName='" + shopstallName + '\'' +
                ", intervalState=" + intervalState +
                ", ifWriteOff=" + ifWriteOff +
                ", amount=" + amount +
                ", ordIdList=" + ordIdList +
                '}';
    }
}
