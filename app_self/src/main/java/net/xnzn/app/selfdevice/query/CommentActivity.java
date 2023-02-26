package net.xnzn.app.selfdevice.query;

import android.content.Context;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import net.xnzn.app.selfdevice.R;
import net.xnzn.app.selfdevice.query.bean.FoodsBean;
import net.xnzn.app.selfdevice.ui.SelfCommonActivity;
import net.xnzn.app.selfdevice.widget.RatingStar;
import net.xnzn.lib_log.L;

import java.util.ArrayList;
import java.util.List;

import me.jingbin.library.ByRecyclerView;
import me.jingbin.library.adapter.BaseByViewHolder;
import me.jingbin.library.adapter.BaseRecyclerAdapter;

public class CommentActivity extends SelfCommonActivity {

    private TextView tvShopName, tvSubmit;
    private EditText etComment;
    private ByRecyclerView foodsRecyclerView;

    private static final String TAG = "CommentActivity";

    @Override
    protected void initView() {
        super.initView();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        tvShopName = findViewById(R.id.tvShopName);
        tvSubmit = findViewById(R.id.tvSubmit);
        etComment = findViewById(R.id.etComment);
        foodsRecyclerView = findViewById(R.id.foodsRecyclerView);
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
        return R.layout.activity_comment;
    }

    List<RatingStar> ratingStars = new ArrayList<>();

    @Override
    protected void initData() {
        countDown(10, 20);

        List<FoodsBean> foodsBeanList = new ArrayList<>();
        foodsBeanList.add(new FoodsBean(1, "", "土豆牛腩/午餐"));
        foodsBeanList.add(new FoodsBean(2, "", "西红柿炒鸡蛋/午餐"));
        foodsBeanList.add(new FoodsBean(2, "", "青椒肉丝/午餐"));
        BaseRecyclerAdapter<FoodsBean> foodItemAdapter = new BaseRecyclerAdapter<FoodsBean>(R.layout.item_foods_comment) {

            @Override
            protected void bindView(BaseByViewHolder<FoodsBean> holder, FoodsBean bean, int position) {
                holder.setText(R.id.tvName, bean.getName());

                RatingStar rs = holder.getView(R.id.rs);

                ratingStars.add(rs);

//                holder.addOnClickListener(R.id.rs);
//                holder.addOnClickListener(R.id.tvName);
            }
        };
        foodsRecyclerView.setAdapter(foodItemAdapter);
        foodsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        foodItemAdapter.setNewData(foodsBeanList);

        foodsRecyclerView.setOnItemChildClickListener((view, position) -> {

            if (view instanceof RatingStar) {
                RatingStar ratingStar = (RatingStar) view;
                foodsBeanList.get(position).setStart(ratingStar.getmGrade());
                L.i(TAG + "=====>:" + ratingStar.getmGrade());
            }

            L.i(TAG + "=====>:" + view);

        });


    }

    @Override
    protected void initLisitener() {
        tvSubmit.setOnClickListener(v -> {
//            if (TextUtils.isEmpty(etComment.getText().toString()))
//                ToastUtil.showShort("请输入评价内容");
            submitComment();
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }


    //TODO 提交评论
    private void submitComment() {

        for (int i = 0; i < ratingStars.size(); i++) {
            System.out.println("ratingStars:::" + ratingStars.get(i).getmGrade());
        }

    }
}