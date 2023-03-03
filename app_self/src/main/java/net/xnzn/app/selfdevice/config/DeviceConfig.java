package net.xnzn.app.selfdevice.config;

public class DeviceConfig {


    private Long loginUserId;
    private String loginUserName;


    //1.==餐盘参数
    //忽略非乐牛餐盘，乐牛盘会加密
    private boolean ignoreNotLeniuPlate = false;
    //忽略crc错误餐盘，和卡片冲突、不计价、不显示
    private boolean ignoreCrcErrorPlate = false;

    //启动分时段模式，是否从时段菜谱获取菜品信息
    private boolean queryIntervalFood = false;
    //读盘错误，语音提示
    private boolean closePlateErrorVoice = false;
    //允许已结算餐盘通过，是否允许结算后，新增菜品进行二次支付
    private boolean addNewPlatePay = false;

    //支付成功后，不写盘，仅测试中使用
    private boolean notWritePlate = false;

    //2.==支付参数
    private boolean supportOfflinePay = false;
    //支付成功结果页显示时间
    private int paySuccessShowTime = 3;


    //3.==显示参数
    private boolean hiddleTotalBalance = false;
    private boolean hiddleSubBalance = false;
    private boolean hiddleWalletBalance = false;
    private boolean hiddleNrv = false;

    //4==算法参数
    private int faceScore = 80;
    private int faceSize = 100;


    //5.==天线参数
    private int airType = 1;//1:荣睿天线 2:乐牛天线

    //6.==工作模式
    private int workMode = 1;//1:808餐台,2:托盘录入模式


    public int getPaySuccessShowTime() {
        return paySuccessShowTime;
    }

    public void setPaySuccessShowTime(int paySuccessShowTime) {
        this.paySuccessShowTime = paySuccessShowTime;
    }

    public boolean isNotWritePlate() {
        return notWritePlate;
    }

    public void setNotWritePlate(boolean notWritePlate) {
        this.notWritePlate = notWritePlate;
    }

    public Long getLoginUserId() {
        return loginUserId;
    }

    public void setLoginUserId(Long loginUserId) {
        this.loginUserId = loginUserId;
    }

    public String getLoginUserName() {
        return loginUserName;
    }

    public void setLoginUserName(String loginUserName) {
        this.loginUserName = loginUserName;
    }


    public int getWorkMode() {
        return workMode;
    }

    public void setWorkMode(int workMode) {
        this.workMode = workMode;
    }

    public int getFaceScore() {
        return faceScore;
    }

    public void setFaceScore(int faceScore) {
        this.faceScore = faceScore;
    }

    public int getFaceSize() {
        return faceSize;
    }

    public void setFaceSize(int faceSize) {
        this.faceSize = faceSize;
    }

    public boolean isIgnoreNotLeniuPlate() {
        return ignoreNotLeniuPlate;
    }

    public void setIgnoreNotLeniuPlate(boolean ignoreNotLeniuPlate) {
        this.ignoreNotLeniuPlate = ignoreNotLeniuPlate;
    }

    public boolean isIgnoreCrcErrorPlate() {
        return ignoreCrcErrorPlate;
    }

    public void setIgnoreCrcErrorPlate(boolean ignoreCrcErrorPlate) {
        this.ignoreCrcErrorPlate = ignoreCrcErrorPlate;
    }

    public boolean isQueryIntervalFood() {
        return queryIntervalFood;
    }

    public void setQueryIntervalFood(boolean queryIntervalFood) {
        this.queryIntervalFood = queryIntervalFood;
    }

    public boolean isClosePlateErrorVoice() {
        return closePlateErrorVoice;
    }

    public void setClosePlateErrorVoice(boolean closePlateErrorVoice) {
        this.closePlateErrorVoice = closePlateErrorVoice;
    }

    public boolean isAddNewPlatePay() {
        return addNewPlatePay;
    }

    public void setAddNewPlatePay(boolean addNewPlatePay) {
        this.addNewPlatePay = addNewPlatePay;
    }

    public boolean isSupportOfflinePay() {
        return supportOfflinePay;
    }

    public void setSupportOfflinePay(boolean supportOfflinePay) {
        this.supportOfflinePay = supportOfflinePay;
    }

    public boolean isHiddleTotalBalance() {
        return hiddleTotalBalance;
    }

    public void setHiddleTotalBalance(boolean hiddleTotalBalance) {
        this.hiddleTotalBalance = hiddleTotalBalance;
    }

    public boolean isHiddleSubBalance() {
        return hiddleSubBalance;
    }

    public void setHiddleSubBalance(boolean hiddleSubBalance) {
        this.hiddleSubBalance = hiddleSubBalance;
    }

    public boolean isHiddleWalletBalance() {
        return hiddleWalletBalance;
    }

    public void setHiddleWalletBalance(boolean hiddleWalletBalance) {
        this.hiddleWalletBalance = hiddleWalletBalance;
    }

    public boolean isHiddleNrv() {
        return hiddleNrv;
    }

    public void setHiddleNrv(boolean hiddleNrv) {
        this.hiddleNrv = hiddleNrv;
    }

    public int getAirType() {
        return airType;
    }

    public void setAirType(int airType) {
        this.airType = airType;
    }

}
