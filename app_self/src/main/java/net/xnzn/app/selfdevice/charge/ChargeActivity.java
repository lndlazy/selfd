package net.xnzn.app.selfdevice.charge;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;

import net.xnzn.app.selfdevice.R;
import net.xnzn.app.selfdevice.ui.SelfCommonActivity;

import java.util.Arrays;

import me.jingbin.library.ByRecyclerView;
import me.jingbin.library.adapter.BaseByViewHolder;
import me.jingbin.library.adapter.BaseRecyclerAdapter;

public class ChargeActivity extends SelfCommonActivity {

    private TextView tvTotalLeft, tvMoney1, tvMoney2, tvMoney3, tvMoney4, tvShowCharge, tvCharge;
    private ByRecyclerView moneyRecyclerView;
    private String[] moneys = {"30元", "50元", "100元", "200元", "300元", "500元"};
    private int currentPosition = 0;
    protected TextView tvSeconds;
    protected TextView tvChargeMoney;
    protected TextView tvCancelCharge;
    protected ImageView ivLoading;
    protected Dialog chargeDialog;
    protected ObjectAnimator rotation;

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

        BaseRecyclerAdapter moneyAdapter = new BaseRecyclerAdapter<String>(R.layout.layout_charge_money_item) {

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
    protected void countDownFinish() {

    }

    @Override
    protected void showCountDownTime(int time) {

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
    protected boolean showTimeTitle() {
        return false;
    }

    @Override
    protected int showView() {
        return R.layout.activity_charge;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initLisitener() {
        tvCharge.setOnClickListener(v -> {
            showChargeDialog();
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
                if (chargeDialog != null && chargeDialog.isShowing()) {
                    chargeDialog.dismiss();
                }

                if (rotation != null)
                    rotation.cancel();

            });
        }

        if (!chargeDialog.isShowing())
            chargeDialog.show();

        if (rotation != null && !rotation.isRunning())
            rotation.start();

    }

    //开始充值
    private void startCharge() {

    }
}