package net.xnzn.app.selfdevice.init;

import net.xnzn.leniu_http.contant.PayType;
import net.xnzn.leniu_http.yunshitang.config.YunBaseInfoCache;
import net.xnzn.leniu_http.yunshitang.config.YunDeviceCache;
import net.xnzn.leniu_http.yunshitang.model.response.YunBaseInfo;
import net.xnzn.leniu_http.yunshitang.pay.common.CommonPayRequest;
import net.xnzn.lib_commin_ui.utils.ArithHelper;
import net.xnzn.lib_utils.TimeUtil;

import java.util.List;

public class DeviceManage {


    private static DeviceManage instance = new DeviceManage();

    public static DeviceManage getInstance() {
        return instance;
    }

    //设备是否在线
    private boolean online = false;
    //最新在线时间
    private long lastOnlineTime = 0;


    public static final int WAIT_PLATE = 1;
    public static final int WAIT_SURE_PAY = 2;
    public static final int WAIT_PAY = 3;
    public static final int PAYING = 4;
    public static final int PAY_FINISH = 5;
    private int status = 1; //1:等待盘子，2：待确认付款 3：待支付 4：支付中 5：支付完成

    public void setStatus(int status) {
        this.status = status;
    }



    public boolean isWaitSurePay() {
        return WAIT_SURE_PAY == status;
    }

    public boolean isWaitPay() {
        return WAIT_PAY == status;
    }

    public int getStatus() {
        return status;
    }

    private DeviceManage() {
        lastOnlineTime = System.currentTimeMillis();
    }


    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
        if (online) {
            lastOnlineTime = System.currentTimeMillis();
        }
    }



    private YunBaseInfo.Interval getCurrentInterval() {
        //当前餐次
        YunBaseInfo baseInfo = YunBaseInfoCache.get();
        if (baseInfo == null) {
            //餐次没有，直接不展示
            return null;
        }

        //取当前餐次
        List<YunBaseInfo.Interval> intervals = baseInfo.getCctime();
        if (intervals == null || intervals.size() == 0) {
            return null;
        }
        for (int i = 0; i < intervals.size(); i++) {
            YunBaseInfo.Interval interval = intervals.get(i);
            if (interval.isBelong()) {
                return interval;
            }
        }

        return null;
    }




    public CommonPayRequest newRequest(){
        YunBaseInfo.Interval interval = getCurrentInterval();

        CommonPayRequest request = new CommonPayRequest
                .Builder()
                .deviceOrderId("CS" + System.currentTimeMillis())
                .payType(PayType.ACCOUNT.getCode())
//                                .consumeMode(DeviceManage.getInstance().getCurrentConsumeMode())
                .consumeMode(1)
                .totalAmount(ArithHelper.div(ShopCart.getInstance().getTotalAmount(), 100))
                .devicePayTime(TimeUtil.getYmdHms(System.currentTimeMillis()))
                .products(ShopCart.getInstance().getProducts())
                .build();


        //设备信息
        request.setIntervalId(interval!=null?interval.getIntervalId():null);
        request.setIntervalName(interval!=null?interval.getIntervalName():null);
        request.setCanteenId(YunDeviceCache.get().getCanteenId());
        request.setCanteenName(YunDeviceCache.get().getCanteenName());
        request.setShopstallId(YunDeviceCache.get().getShopstallId());
        request.setShopstallName(YunDeviceCache.get().getStname());
        request.setDeviceNum(YunDeviceCache.get().getMchNum());
        request.setDeviceName(YunDeviceCache.get().getMchName());

        request.setOnline(true);

        return request;

    }


}
