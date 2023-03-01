package net.xnzn.app.selfdevice.sign;

import android.app.Dialog;
import android.nfc.Tag;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.xnzn.app.selfdevice.R;
import net.xnzn.app.selfdevice.sign.bean.SignBean;
import net.xnzn.app.selfdevice.ui.SelfCommonActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.jingbin.library.ByRecyclerView;
import me.jingbin.library.adapter.BaseByViewHolder;
import me.jingbin.library.adapter.BaseRecyclerAdapter;

public class SignActivity extends SelfCommonActivity {

    private static final String TAG = "SignActivity";
    protected ByRecyclerView singRecyclerView;
    protected BaseRecyclerAdapter<SignBean> adapter;
    protected BaseRecyclerAdapter<SignBean> dialogAdapter;

    @Override
    protected void initView() {
        super.initView();

        singRecyclerView = findViewById(R.id.singRecyclerView);
        tvBarContent.setText("首页> 就餐签到");
    }

    @Override
    protected boolean showTitleBar() {
        return false;
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
    protected int showView() {
        return R.layout.activity_sign;
    }

    @Override
    protected void initData() {

        countDown(10, 20);
        adapter = new BaseRecyclerAdapter<SignBean>(R.layout.item_sign) {

            @Override
            protected void bindView(BaseByViewHolder<SignBean> holder, SignBean bean, int position) {

//                holder.setVisible(R.id.tvFoodCode, )//取餐码
//                holder.setText(R.id.tvTitle,)
//                holder.setText(R.id.tvType,)//早餐or午餐or晚餐
//                holder.setText(R.id.tvDate,)
//                holder.setText(R.id.tvPrice,)
//                holder.setText(R.id.tvCount,)
//                holder.setText(R.id.tvSign,)//签到
                holder.addOnClickListener(R.id.tvMenuDetail);
                holder.addOnClickListener(R.id.tvSign);
                ByRecyclerView picRecyclerView = holder.getView(R.id.itemPicRecyclerView);

                loadPic(picRecyclerView, new String[]{"", "", ""});

            }
        };
        singRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        singRecyclerView.setOnItemChildClickListener(new ByRecyclerView.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.tvSign:
                        signAction();
                        break;
                    case R.id.tvMenuDetail:
                        List<SignBean> list = new ArrayList<>();
                        list.add(new SignBean());
                        list.add(new SignBean());
                        list.add(new SignBean());
                        foodsDetail(list);
                        break;
                }
            }
        });

        singRecyclerView.setAdapter(adapter);

        List<SignBean> list = new ArrayList<>();
        list.add(new SignBean());
        list.add(new SignBean());
        list.add(new SignBean());
        adapter.setNewData(list);
    }

    private Dialog dialog;
    private ByRecyclerView dRecyclerView;

    private void foodsDetail(List<SignBean> list) {

        if (dialog == null) {
            dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_order_detail);
            dialog.setCanceledOnTouchOutside(true);
            dialog.findViewById(R.id.tvClose).setOnClickListener(view1 -> dialog.dismiss());
        }

        if (dRecyclerView == null || dialogAdapter == null) {

            dRecyclerView = dialog.findViewById(R.id.dRecyclerView);
            dRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            dialogAdapter = new BaseRecyclerAdapter<SignBean>(R.layout.item_sign_dialog_item) {

                @Override
                protected void bindView(BaseByViewHolder<SignBean> holder, SignBean bean, int position) {

                    ImageView ivDialogImg = holder.getView(R.id.ivDialogImg);
//                holder.setText(R.id.tvDialogName, )
//                holder.setText(R.id.tvDialogFen, )
//                holder.setText(R.id.tvDialogCount, )
//                holder.setText(R.id.tvDialogMoney, )
                }
            };
            dRecyclerView.setAdapter(dialogAdapter);

        }

        if (dialogAdapter != null) {
            dialogAdapter.clear();
            dialogAdapter.setNewData(list);
        }

        if (dialog != null && !dialog.isShowing())
            dialog.show();

    }

    //TODO 签到
    private void signAction() {


    }

    private void loadPic(ByRecyclerView picRecyclerView, String[] urls) {

        if (urls == null || urls.length < 1)
            return;

        BaseRecyclerAdapter<String> picAdapter = new BaseRecyclerAdapter<String>(R.layout.item_pic) {

            @Override
            protected void bindView(BaseByViewHolder<String> holder, String bean, int position) {
//                Glide.with(getActivity())
//                        .load()
//                        .into()
                holder.setImageDrawable(R.id.ivImg, getResources().getDrawable(R.mipmap.ic_menu_food_show));
            }
        };

//        SpacesItemDecoration itemDecoration = new SpacesItemDecoration(getActivity(), SpacesItemDecoration.HORIZONTAL)
//                .setNoShowDivider(1, 1)
//                // 颜色，分割线间距，左边距(单位dp)，右边距(单位dp)
//                .setParam(R.color.translucent, -20, 0, 0);
//        picRecyclerView.addItemDecoration(itemDecoration);
        picRecyclerView.setLayoutManager(new LinearLayoutManager(SignActivity.this, RecyclerView.HORIZONTAL, false));
        picRecyclerView.setAdapter(picAdapter);
        picAdapter.setNewData(Arrays.asList(urls));
    }

    @Override
    protected void initLisitener() {

    }
}