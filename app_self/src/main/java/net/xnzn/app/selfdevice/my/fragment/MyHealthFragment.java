package net.xnzn.app.selfdevice.my.fragment;

import android.view.View;
import android.widget.TextView;

import net.xnzn.app.selfdevice.R;
import net.xnzn.app.selfdevice.ui.BaseFragment;
import net.xnzn.app.selfdevice.widget.CustomMyItemEditView;
import net.xnzn.app.selfdevice.widget.CustomMyItemView;

public class MyHealthFragment extends BaseFragment implements View.OnClickListener {

    protected CustomMyItemEditView cmiHealthHeight;
    protected CustomMyItemEditView cmiHealthWeight;
    protected CustomMyItemEditView cmiHealthAge;
    protected CustomMyItemView cmiHealthBMI;
    protected CustomMyItemView cmiHealthBody;
    protected CustomMyItemView cmiHealthHeart;
    protected CustomMyItemView cmiHealthBlood;
    protected CustomMyItemView cmiHealthWork;
    protected CustomMyItemView cmiHospital;
    protected CustomMyItemView cmiHospitalDate;
    protected CustomMyItemView cmiPregnant;
    protected CustomMyItemView cmiPregnantDate;
    protected CustomMyItemView cmiGuoMing;
    protected CustomMyItemView cmiSlowSick;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_my_health;
    }

    @Override
    public void initView(View view) {

        cmiHealthHeight = view.findViewById(R.id.cmiHealthHeight);
        cmiHealthWeight = view.findViewById(R.id.cmiHealthWeight);
        cmiHealthAge = view.findViewById(R.id.cmiHealthAge);

        cmiHealthBMI = view.findViewById(R.id.cmiHealthBMI);
        cmiHealthBody = view.findViewById(R.id.cmiHealthBody);
        cmiHealthHeart = view.findViewById(R.id.cmiHealthHeart);
        cmiHealthBlood = view.findViewById(R.id.cmiHealthBlood);
        cmiHealthWork = view.findViewById(R.id.cmiHealthWork);
        cmiHospital = view.findViewById(R.id.cmiHospital);
        cmiHospitalDate = view.findViewById(R.id.cmiHospitalDate);
        cmiPregnant = view.findViewById(R.id.cmiPregnant);
        cmiPregnantDate = view.findViewById(R.id.cmiPregnantDate);
        cmiGuoMing = view.findViewById(R.id.cmiGuoMing);
        cmiSlowSick = view.findViewById(R.id.cmiSlowSick);

        TextView tvSaveNow = view.findViewById(R.id.tvSaveNow);
        tvSaveNow.setOnClickListener(this);

    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tvSaveNow:
                break;
        }
    }
}