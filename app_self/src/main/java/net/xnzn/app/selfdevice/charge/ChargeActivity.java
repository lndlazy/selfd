package net.xnzn.app.selfdevice.charge;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;

import net.xnzn.app.selfdevice.R;
import net.xnzn.app.selfdevice.UserInfo;
import net.xnzn.app.selfdevice.charge.req.AddBalanceRequest;
import net.xnzn.app.selfdevice.net.SelfApiService;
import net.xnzn.app.selfdevice.ui.SelfCommonActivity;
import net.xnzn.app.selfdevice.utils.SelfConstant;
import net.xnzn.lib_log.L;
import net.xnzn.lib_utils.ToastUtil;

import java.math.BigDecimal;
import java.util.Arrays;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import me.jingbin.library.ByRecyclerView;
import me.jingbin.library.adapter.BaseByViewHolder;
import me.jingbin.library.adapter.BaseRecyclerAdapter;

public class ChargeActivity extends SelfCommonActivity {

    private static final String TAG = "ChargeActivity";
    private TextView tvTotalLeft, tvMoney1, tvMoney2, tvMoney3, tvMoney4, tvShowCharge, tvCharge;
    private ByRecyclerView moneyRecyclerView;
    private String[] moneys = {"30元", "50元", "100元", "200元", "300元", "500元"};
    private int currentPosition = 0;
    protected TextView tvSeconds;
    protected TextView tvChargeMoney;
    protected TextView tvCancelCharge;
    protected ImageView ivLoading;
    private Dialog chargeDialog;
    private Dialog chargeFailDialog;

    protected ObjectAnimator rotation;
    protected TextView tvFailSeconds;

    private int current_charge_status;

    private static final int STATUS_FREE = 0;
    private static final int STATUS_CHARGING = 1;//充值中
    private static final int STATUS_CHARGE_START = 5;//开始充值, 用户点了立即充值按钮
    private static final int STATUS_CHARGE_FAIL = 2;//充值失败
    private static final int STATUS_CHARGE_SUCCESS = 3;//充值成功
    private static final int STATUS_CHARGE_CANCEL = 4;//取消充值
    protected EditText etInput;


    @Override
    protected void initView() {
        super.initView();
        tvTotalLeft = findViewById(R.id.tvTotalLeft);
        tvMoney1 = findViewById(R.id.tvMoney1);
        tvMoney2 = findViewById(R.id.tvMoney2);
        tvMoney3 = findViewById(R.id.tvMoney3);
        tvMoney4 = findViewById(R.id.tvMoney4);

        tvShowCharge = findViewById(R.id.tvShowCharge);
        tvCharge = findViewById(R.id.tvCharge);

        moneyRecyclerView = findViewById(R.id.moneyRecyclerView);

        BaseRecyclerAdapter<String> moneyAdapter = new BaseRecyclerAdapter<String>(R.layout.layout_charge_money_item) {

            @Override
            protected void bindView(BaseByViewHolder<String> holder, String bean, int position) {

                holder.setText(R.id.tvMoneyDetail, moneys[position]);
//                tvShowCharge.setText(moneys[position]);
                if (currentPosition == position) {
                    holder.setTextColor(R.id.tvMoneyDetail, getResources().getColor(R.color.theme_color));
                    holder.itemView.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_charge_money_selected_bg));
//                    holder.setBackgroundRes(R.id.tvMoneyDetail, getResources().getColor(R.color.text_color_1a));
                } else {
                    holder.setTextColor(R.id.tvMoneyDetail, getResources().getColor(R.color.text_color_1a));
                    holder.itemView.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_charge_money_bg));
//                    holder.setBackgroundRes(R.id.tvMoneyDetail,getResources().getDrawable(R.drawable.shape_charge_money_bg));
                }
            }
        };
        moneyRecyclerView.setAdapter(moneyAdapter);

//        MoneyAdapter moneyAdapter = new MoneyAdapter(this, Arrays.asList(moneys));
//        moneyRecyclerView.setAdapter(moneyAdapter);
        moneyRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        moneyAdapter.setNewData(Arrays.asList(moneys));
        moneyRecyclerView.setOnItemClickListener((v, position) -> {
            currentPosition = position;
            tvShowCharge.setText(moneys[position]);
            moneyAdapter.notifyDataSetChanged();
        });
    }


    @Override
    protected boolean showTitle() {
        return true;
    }

    @Override
    protected boolean showBar() {
        return true;
    }

    @Override
    protected boolean showTitleBar() {
        return false;
    }

    @Override
    protected int showView() {
        return R.layout.activity_charge;
    }

    @Override
    protected void initData() {

        current_charge_status = STATUS_FREE;

    }


    private long lastTime;
    private String lastCode = "";

    private synchronized void getCode2charge(String code) {

        if (current_charge_status != STATUS_CHARGE_START)//没有点击立即充值
            return;

        if (System.currentTimeMillis() - lastTime < SelfConstant.SCAN_INTERVAL && code.equals(lastCode)) {
            L.i(TAG + ",相同码连词扫描间隔小于最小时间秒");
            return;
        }

        lastCode = code;
        lastTime = System.currentTimeMillis();

        try {

            String money = moneys[currentPosition].replace("元", "");
            int iMoney = Integer.parseInt(money);
            chargeAction(iMoney, code);

        } catch (Exception e) {
            L.e(TAG + "充值出错:" + e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * 开始执行充值操作
     *
     * @param iMoney 充值金额  单位元
     * @param code   充值的二维码
     */
    private synchronized void chargeAction(int iMoney, String code) {

        if (current_charge_status == STATUS_CHARGING) {
            //正在充值，取消充值操作
            L.e(TAG + "，正在充值中，取消当前获取二维码的充值操作");
            return;
        }

        AddBalanceRequest balanceRequest = new AddBalanceRequest();
        BigDecimal bigDecimal = new BigDecimal(iMoney * 100);
        balanceRequest.setAmount(bigDecimal.intValue());
        balanceRequest.setCustId(UserInfo.id);
        balanceRequest.setPayType(10);
        balanceRequest.setAuthCode(code);
        current_charge_status = STATUS_CHARGING;
        SelfApiService.charge(balanceRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (resp) -> {

                            if (resp.isSuccess()) {
                                //充值成功
                                chargeSuccess();
                            } else {
                                chargeFail(resp.getMsg());
                            }
                        },
                        (error) -> {
                            chargeFail(error.getMessage());
                        }
                );


    }

    //充值失败
    private void chargeFail(String msg) {
        current_charge_status = STATUS_CHARGE_FAIL;
        disMissChargeDialog();
        ToastUtil.showShort("充值失败!" + msg);
        L.e(TAG + ",充值失败:" + msg);
        showChargeFailDialog();
    }

    //充值成功
    private void chargeSuccess() {
        current_charge_status = STATUS_CHARGE_SUCCESS;
        disMissChargeDialog();
    }

    @Override
    protected void initLisitener() {
        tvCharge.setOnClickListener(v -> {
//            showChargeFailDialog();
            startCharge();
        });
    }

    //显示充值界面
    private void showChargeDialog() {

        if (chargeDialog == null) {
            chargeDialog = new Dialog(this);
            chargeDialog.setContentView(R.layout.dialog_charge);
            tvSeconds = chargeDialog.findViewById(R.id.tvSeconds);
            tvChargeMoney = chargeDialog.findViewById(R.id.tvChargeMoney);
            ivLoading = chargeDialog.findViewById(R.id.ivLoading);
            tvCancelCharge = chargeDialog.findViewById(R.id.tvCancelCharge);
            rotation = ObjectAnimator.ofFloat(ivLoading, "rotation", 0f, 360f);
            rotation.setDuration(2000);
            rotation.setRepeatCount(30);
            tvCancelCharge.setOnClickListener(v -> {

                cancelCharge();

            });

            etInput = chargeDialog.findViewById(R.id.etInput);
            Log.d(TAG, "etInput::" + etInput);
            etInput.setInputType(InputType.TYPE_NULL);
            etInput.setOnKeyListener((v, keyCode, event) -> {

                if (KeyEvent.KEYCODE_ENTER == keyCode && event.getAction() == KeyEvent.ACTION_DOWN) {
                    String result = etInput.getText().toString();
                    Log.d(TAG, "扫描结果:" + result);

                    if (TextUtils.isEmpty(result))
                        return false;

                    etInput.setText("");
                    getCode2charge(result);

                }

                return false;
            });
        }

        if (!chargeDialog.isShowing())
            chargeDialog.show();

        if (rotation != null && !rotation.isRunning())
            rotation.start();

    }

    //取消充值
    private synchronized void cancelCharge() {

        switch (current_charge_status) {
            case STATUS_CHARGING://正在充值中，点击取消充值，可能会取消失败
                ToastUtil.showShort("正在充值中，无法取消");
                L.i(TAG + ",取消充值操作失败--->>>正在充值中，无法取消");
                break;
            case STATUS_CHARGE_SUCCESS://充值成功
                ToastUtil.showShort("已充值成功!");
                L.i(TAG + ",取消充值操作失败--->>>已充值成功");
                break;

            default:

                current_charge_status = STATUS_CHARGE_CANCEL;
                disMissChargeDialog();

                if (rotation != null)
                    rotation.cancel();
                break;
        }

    }

    //开始充值
    private void startCharge() {

        current_charge_status = STATUS_CHARGE_START;
        showChargeDialog();
        //AppUtil.

    }

    private void showChargeFailDialog() {

        if (chargeFailDialog == null) {
            chargeFailDialog = new Dialog(this);
            chargeFailDialog.setContentView(R.layout.dialog_charge_fail);
            tvFailSeconds = chargeFailDialog.findViewById(R.id.tvFailSeconds);
            TextView tvExit = chargeFailDialog.findViewById(R.id.tvExit);
            TextView tvContain = chargeFailDialog.findViewById(R.id.tvContain);

            //退出充值页面， 返回首页
            tvExit.setOnClickListener(v -> {
                disMissChargeDialog();
                finish();
            });
            tvContain.setOnClickListener(v -> {//继续充值
                disMissChargeDialog();
            });
        }

        if (chargeFailDialog != null && !chargeFailDialog.isShowing())
            chargeFailDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        disMissChargeDialog();

    }

    private void disMissChargeDialog() {
        if (chargeFailDialog != null && chargeFailDialog.isShowing())
            chargeFailDialog.dismiss();

        if (chargeDialog != null && chargeDialog.isShowing())
            chargeDialog.dismiss();
    }
}