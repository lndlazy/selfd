package net.xnzn.app.selfdevice.query;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import net.xnzn.app.selfdevice.R;
import net.xnzn.app.selfdevice.query.bean.YingYangResponse;
import net.xnzn.app.selfdevice.query.bean.YunUser;
import net.xnzn.app.selfdevice.query.presenter.NutritionPresenter;
import net.xnzn.app.selfdevice.query.view.NutritionView;
import net.xnzn.app.selfdevice.ui.BaseFragment;
import net.xnzn.app.selfdevice.widget.LineView;

import java.util.ArrayList;
import java.util.List;

import me.jingbin.library.ByRecyclerView;
import me.jingbin.library.adapter.BaseByViewHolder;
import me.jingbin.library.adapter.BaseRecyclerAdapter;

/**
 * 营养分类fragment
 */
public class NutritionFragment extends BaseFragment implements View.OnClickListener, NutritionView {
    private static final String TAG = "NutritionFragment";
    private YunUser currentUser = null;
    private TextView tvName, tvDept, tvTime, tvDay, tvWeek;
    private ByRecyclerView rv;
    private ImageView ivHead;
    private NutritionPresenter mPresenter = new NutritionPresenter(this);
    protected BaseRecyclerAdapter<Item> adapter;
    private List<Item> itemsData = new ArrayList<>();
    protected LineView lineView;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_nutrition;
    }

    public void setCurrentUser(YunUser currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public void initView(View view) {
        tvName = view.findViewById(R.id.tv_name);
        tvDept = view.findViewById(R.id.tv_dept);
        tvTime = view.findViewById(R.id.tv_time);
        tvDay = view.findViewById(R.id.tv_day);
        tvWeek = view.findViewById(R.id.tv_week);
        rv = view.findViewById(R.id.rv);
        ivHead = view.findViewById(R.id.iv_head);
        tvDay.setOnClickListener(this);
        lineView = view.findViewById(R.id.line_view);

    }

    @Override
    public void initData() {

        if (getActivity() == null)
            return;

        if (currentUser != null) {
            tvName.setText(currentUser.getCustName());
            tvDept.setText("部门：" + currentUser.getOrgFullName());
            if (TextUtils.isEmpty(currentUser.getCustPhotoUrl())) {
                Glide.with(this).load(R.mipmap.ic_head_pic).into(ivHead);
            } else {
                Glide.with(this).load(currentUser.getCustPhotoUrl()).into(ivHead);
            }
            //查当天数据营养数据
            mPresenter.queryData(1, currentUser.getCustId());
        }

        adapter = new BaseRecyclerAdapter<Item>(R.layout.item_yy) {
            @Override
            protected void bindView(BaseByViewHolder<Item> holder, Item item, int position) {
                holder.setText(R.id.tv_name, item.name)
                        .setText(R.id.tv_real, item.real)
                        .setText(R.id.tv_recommend, item.recommend)
                        .setText(R.id.tv_index, item.recommendIndex + "");
            }
        };
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(adapter);
        adapter.setNewData(itemsData);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_day:
                tvDay.setTextColor(getResources().getColor(R.color.color_a31));
                tvWeek.setTextColor(getResources().getColor(R.color.text_color_666));
                tvDay.setBackgroundResource(R.color.white);
                tvWeek.setBackgroundResource(R.mipmap.yy_select_bg);
                mPresenter.queryData(1, currentUser.getCustId());
                break;
            case R.id.tv_week:
                tvDay.setTextColor(getResources().getColor(R.color.text_color_666));
                tvWeek.setTextColor(getResources().getColor(R.color.color_a31));
                tvDay.setBackgroundResource(R.mipmap.yy_select_left_bg);
                tvWeek.setBackgroundResource(R.color.white);
                mPresenter.queryData(2, currentUser.getCustId());
                break;

        }
    }


    private static class Item {

        public Item(String name, String real, String recommend, double recommendIndex) {
            this.name = name;
            this.real = real;
            this.recommend = recommend;
            this.recommendIndex = recommendIndex;
        }

        private String name;
        private String real;
        private String recommend;
        private double recommendIndex;


        @Override
        public String toString() {
            return "Item{" +
                    "name='" + name + '\'' +
                    ", real='" + real + '\'' +
                    ", recommend='" + recommend + '\'' +
                    ", recommendIndex=" + recommendIndex +
                    '}';
        }
    }

    //更新折线图数据
    private void setLineData(YingYangResponse data) {

        if (data == null)
            return;

        lineView.setDrawDotLine(true); //optional
        lineView.setShowPopup(LineView.SHOW_POPUPS_All); //optional
        ArrayList<String> bottomTextList = new ArrayList<>();
        bottomTextList.add("热量");
        bottomTextList.add("蛋白质");
        bottomTextList.add("脂肪");
        bottomTextList.add("碳水化合物");
        bottomTextList.add("膳食纤维");
        bottomTextList.add("胆固醇");
        lineView.setBottomTextList(bottomTextList);
        lineView.setColorArray(new int[]{
                Color.parseColor("#FE0000"),
                Color.parseColor("#009943"),
                Color.BLACK, Color.parseColor("#333333")});


        ArrayList<ArrayList<Float>> dataLists = new ArrayList<>();
        ArrayList<Float> temp1 = new ArrayList<>();
        temp1.add(data.getCalories());
        temp1.add(data.getProtein());
        temp1.add(data.getFat());
        temp1.add(data.getCarbohydrate());
        temp1.add(data.getDietaryFiber());
        temp1.add(data.getCholesterol());
        dataLists.add(temp1);

        ArrayList<Float> temp2 = new ArrayList<>();
        temp2.add(data.getReferenceCalories());
        temp2.add(data.getReferenceProtein());
        temp2.add(data.getReferenceFat());
        temp2.add(data.getReferenceCarbohydrate());
        temp2.add(data.getReferenceDietaryFiber());
        temp2.add(data.getReferenceCholesterol());
        dataLists.add(temp2);
        Log.d(TAG, dataLists.toString());

        lineView.setFloatDataList(dataLists); //or lineView.setFloatDataList(floatDataLists)

    }

    //更新表格
    private void updateTableData(YingYangResponse data) {

        if (data == null)
            return;

        //构造数据表格数据
        double caloriesIndex = (double) (Math.round(data.getCaloriesIndex() * 100)) / 100;
        double proteinIndex = (double) (Math.round(data.getProteinIndex() * 100)) / 100;
        double fatIndex = (double) (Math.round(data.getFatIndex() * 100)) / 100;
        double carbohydrateIndex = (double) (Math.round(data.getCarbohydrateIndex() * 100)) / 100;
        double dietaryFiberIndex = (double) (Math.round(data.getDietaryFiberIndex() * 100)) / 100;
        double cholesterolIndex = (double) (Math.round(data.getCholesterolIndex() * 100)) / 100;

        Item item1 = new Item("热量", data.getCalories() + "cal", data.getReferenceCalories() + "cal", caloriesIndex);
        Item item2 = new Item("蛋白质", data.getProtein() + "g", data.getReferenceProtein() + "g", proteinIndex);
        Item item3 = new Item("脂肪", data.getFat() + "g", data.getReferenceFat() + "g", fatIndex);
        Item item4 = new Item("碳水化合物", data.getCarbohydrate() + "g", data.getReferenceCarbohydrate() + "g", carbohydrateIndex);
        Item item5 = new Item("膳食纤维", data.getDietaryFiber() + "g", data.getReferenceDietaryFiber() + "g", dietaryFiberIndex);
        Item item6 = new Item("胆固醇", data.getCholesterol() + "mg", data.getReferenceCholesterol() + "mg", cholesterolIndex);


        List<Item> tempData = new ArrayList<>();
        tempData.add(item1);
        tempData.add(item2);
        tempData.add(item3);
        tempData.add(item4);
        tempData.add(item5);
        tempData.add(item6);

        Log.d(TAG, tempData.toString());
        adapter.getData().clear();
        adapter.getData().addAll(tempData);
        adapter.notifyDataSetChanged();

    }


}