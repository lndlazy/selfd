package net.xnzn.app.selfdevice.charge.req;

import java.io.Serializable;

//充值
public class AddBalanceRequest implements Serializable {

    private String  custId;
    private int payType;
    private String authCode;
    private int amount;//分

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {


        this.amount = amount;
    }

    @Override
    public String toString() {
        return "AddBalanceRequest{" +
                "custId='" + custId + '\'' +
                ", payType=" + payType +
                ", authCode='" + authCode + '\'' +
                ", amount=" + amount +
                '}';
    }
}
