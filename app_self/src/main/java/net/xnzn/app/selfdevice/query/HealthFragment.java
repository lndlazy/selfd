package net.xnzn.app.selfdevice.query;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import net.xnzn.app.selfdevice.R;
import net.xnzn.app.selfdevice.query.bean.HealthRecord;
import net.xnzn.app.selfdevice.query.bean.YunUser;
import net.xnzn.app.selfdevice.query.presenter.HealthPresenter;
import net.xnzn.app.selfdevice.query.view.HealthView;
import net.xnzn.app.selfdevice.ui.BaseFragment;
import net.xnzn.app.selfdevice.utils.TimeUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import me.jingbin.library.ByRecyclerView;
import me.jingbin.library.adapter.BaseByViewHolder;
import me.jingbin.library.adapter.BaseRecyclerAdapter;

public class HealthFragment extends BaseFragment implements View.OnClickListener, HealthView {

    private TextView tvHeight;
    private TextView tvWeight;
    private TextView tvBMI;
    private TextView tvShape;
    private TextView tvBlood;
    private TextView tvHeartRate;
    private TextView tvHeight2;
    private TextView tvWeight2;
    private TextView tvBMI2;
    private TextView tvShape2;
    private TextView tvBlood2;
    private TextView tvHeartRate2;
    private RelativeLayout rlOne;
    private LinearLayout llTwo;
    private TextView tvMon;
    private ByRecyclerView rv;
    private HealthPresenter mPresenter = new HealthPresenter(this);
    private YunUser currentUser = null;
    protected BaseRecyclerAdapter<HealthRecord> adapter;

    private HealthRecord currentData = new HealthRecord();
    private HealthRecord chooseData = new HealthRecord();
    private List<HealthRecord> list = new ArrayList<>();

    public void setCurrentUser(YunUser currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_health;
    }

    @Override
    public void initView(View view) {
        rv = view.findViewById(R.id.rv);
        tvMon = view.findViewById(R.id.tvMon);
        llTwo = view.findViewById(R.id.llTwo);
        rlOne = view.findViewById(R.id.rlOne);
        tvHeartRate2 = view.findViewById(R.id.tvHeartRate2);
        tvBlood2 = view.findViewById(R.id.tvBlood2);
        tvShape2 = view.findViewById(R.id.tvShape2);
        tvBMI2 = view.findViewById(R.id.tvBMI2);
        tvWeight2 = view.findViewById(R.id.tvWeight2);
        tvHeight2 = view.findViewById(R.id.tvHeight2);
        tvHeartRate = view.findViewById(R.id.tvHeartRate);
        tvBlood = view.findViewById(R.id.tvBlood);
        tvShape = view.findViewById(R.id.tvShape);
        tvBMI = view.findViewById(R.id.tvBMI);
        tvWeight = view.findViewById(R.id.tvWeight);
        tvHeight = view.findViewById(R.id.tvHeight);
        view.findViewById(R.id.tvBack1).setOnClickListener(this);
        tvMon.setOnClickListener(this);
    }

    @Override
    public void initData() {

        if (currentUser != null) {
            //查询健康记录
            custId = currentUser.getCustId();
            loadData();
        }


        queryDate = TimeUtil.getCurrentYmd();
        String mon = TimeUtil.getCurrentMon();
        tvMon.setText(mon + "月");

        initRecycleView();
    }

    private void initRecycleView() {

        if (getActivity() == null)
            return;

        adapter = new BaseRecyclerAdapter<HealthRecord>(R.layout.item_health) {

            @Override
            protected void bindView(BaseByViewHolder<HealthRecord> holder, HealthRecord bean, int position) {
                holder.setText(R.id.tvRegisterDate, bean.getRegisterDate())
                        .addOnClickListener(R.id.tvChoose);
            }
        };
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(adapter);
        rv.setOnLoadMoreListener(new ByRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
//                loadData();
            }
        });
        rv.setOnItemClickListener(new ByRecyclerView.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (view.getId() == R.id.tvChoose) {
                    chooseData = list.get(position);
                    showData2();
                }
            }
        });

        adapter.setNewData(list);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tvMon:
                showDatePickDlg();
                break;

            case R.id.tvBack1:
                initData2();
                break;

        }
    }


    private int current = 1;
    private Long custId;
    private String queryDate;//查询月份

    public void showDatePickDlg() {

        if (getActivity() == null)
            return;

        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                int month = (monthOfYear + 1);
//                String temp;
//                if (month < 10) {
//                    temp = "0" + month;
//                } else {
//                    temp = month + "";
//                }
//                tvMon.setText(temp + "月");
//                queryDate = year + "-" + temp + "-" + "01";
//                current = 1;
//                adapter.getData().clear();
//                adapter.notifyDataSetChanged();
//                initData1();
//                loadData();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        DatePicker picker = datePickerDialog.getDatePicker();
        picker.setCalendarViewShown(false);
        datePickerDialog.show();
        ((ViewGroup) ((ViewGroup) picker.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);//不显示日期
    }


    public void initData1() {
        tvHeight.setText("0");
        tvWeight.setText("0");
        tvBMI.setText("0");
        tvShape.setText("无");
        tvBlood.setText("0/0");
        tvHeartRate.setText("0");
    }


    public void initData2() {
        rlOne.setVisibility(View.VISIBLE);
        llTwo.setVisibility(View.GONE);
        tvHeight2.setText("0");
        tvWeight2.setText("0");
        tvBMI2.setText("0");
        tvShape2.setText("无");
        tvBlood2.setText("0/0");
        tvHeartRate2.setText("0");
    }


    public void showData2() {
        tvHeight2.setText(chooseData.getHeight() + "");
        tvWeight2.setText(chooseData.getWeight() + "");
        tvBMI2.setText(chooseData.getBmi() + "");
        tvShape2.setText(chooseData.getShape() + "");
        tvBlood2.setText(chooseData.getBloodStr() + "");
        tvHeartRate2.setText(chooseData.getHeartRate() + "");
        rlOne.setVisibility(View.GONE);
        llTwo.setVisibility(View.VISIBLE);
    }

    private void loadData() {//查询记录
//        mPresenter.queryHealthRecords(current, custId, queryDate, size);
    }


//  private static class ItemAdapter extends BaseQuickAdapter<HealthRecord, BaseViewHolder> {
//
//    public ItemAdapter(@Nullable List<HealthRecord> data) {
//      super(R.layout.item_health, data);
//    }
//
//    @Override
//    protected void convert(@NonNull BaseViewHolder helper, HealthRecord item) {
//      helper.setText(R.id.tvRegisterDate, item.getRegisterDate())
//              .addOnClickListener(R.id.tvChoose);
//    }
//  }


}