package net.xnzn.app.selfdevice.config;

import android.text.TextUtils;

public class MetaInfoConfig {

    public String merchantId;
    public String merchantName;
    public String partnerId;
    public String isvName;
    public String appId;
    private String logicGroupID;
    public String authToken;
    public String deviceNum;
    public String industryBizType = "business";  // todo 高校=college; 企业=business  ,从ISV后台下发
    public String serviceID = "pay";
    public String alipayUrl;

    public MetaInfoConfig() {
    }

    public MetaInfoConfig(String merchantId, String merchantName, String partnerId, String isvName, String appId, String logicGroupID, String deviceNum) {
        this.merchantId = merchantId;
        this.merchantName = merchantName;
        this.partnerId = partnerId;
        this.isvName = isvName;
        this.appId = appId;
        this.logicGroupID = logicGroupID;
        this.deviceNum = deviceNum;
    }


    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getIsvName() {
        return isvName;
    }

    public void setIsvName(String isvName) {
        this.isvName = isvName;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getLogicGroupID() {
        return getIndustryBizType() + "_" + getMerchantId() + "_01";
    }

    public void setLogicGroupID(String logicGroupID) {
        this.logicGroupID = logicGroupID;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getDeviceNum() {
        return deviceNum;
    }

    public void setDeviceNum(String deviceNum) {
        this.deviceNum = deviceNum;
    }

    public String getIndustryBizType() {
        return industryBizType;
    }

    public void setIndustryBizType(String industryBizType) {
        this.industryBizType = industryBizType;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public String getAlipayUrl() {
        return alipayUrl;
    }

    public void setAlipayUrl(String alipayUrl) {
        this.alipayUrl = alipayUrl;
    }

    public String checkIsvInfo() {
        if (TextUtils.isEmpty(merchantId)) {
            return "请配置【 merchantId 】";
        }
        if (TextUtils.isEmpty(merchantName)) {
            return "请配置【 merchantName 】";
        }
        if (TextUtils.isEmpty(partnerId)) {
            return "请配置【 partnerId 】";
        }
        if (TextUtils.isEmpty(isvName)) {
            return "请配置【 isvName 】";
        }
        if (TextUtils.isEmpty(appId)) {
            return "请配置【 appId 】";
        }
        if (TextUtils.isEmpty(logicGroupID)) {
            return "请配置【 logicGroupID 】";
        }
//        if (!logicGroupID.equals("TEST") && !logicGroupID.startsWith(industryBizType)) {
//            return "高校/企业团餐的【 logicGroupID 】参考格式为 ： " + industryBizType + "_" + merchantId + "_01";
//        }
        if (TextUtils.isEmpty(logicGroupID)) {
            return "请配置 【高校/企业团餐的 logicGroupID 】";
        }

        return "";
    }


}
