package net.xnzn.app.selfdevice.query;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.xnzn.app.selfdevice.R;
import net.xnzn.app.selfdevice.query.bean.OrderItemBean;
import net.xnzn.lib_utils.TimeUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import me.jingbin.library.ByRecyclerView;
import me.jingbin.library.adapter.BaseByViewHolder;
import me.jingbin.library.adapter.BaseRecyclerAdapter;
import rxhttp.wrapper.utils.LogUtil;

public class OrderFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "OrderFragment";
    protected EditText etSearch;
    protected TextView tvSearch;
    protected TextView tvFilter;
    protected ByRecyclerView byRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order, container, false);
        etSearch = view.findViewById(R.id.etSearch);
        tvSearch = view.findViewById(R.id.tvSearch);
        tvFilter = view.findViewById(R.id.tvFilter);
        byRecyclerView = view.findViewById(R.id.byRecyclerView);

        tvSearch.setOnClickListener(this);
        initData();
        return view;

    }

    private int orderId = -1;

    private void initData() {

        List<OrderItemBean> itemList = new ArrayList<>();
        itemList.add(new OrderItemBean("订单类型:自助餐", new Date(), "展厅食堂/档口1", "已完成", "12.9", 3, new String[2]));
        itemList.add(new OrderItemBean("订单类型:自助餐", new Date(), "展厅食堂/档口2", "已完成", "32", 5, new String[5]));
        itemList.add(new OrderItemBean("订单类型:自助餐", new Date(), "展厅食堂/档口8", "已完成", "22.9", 3, new String[3]));

        BaseRecyclerAdapter<OrderItemBean> itemAdapter = new BaseRecyclerAdapter<OrderItemBean>(R.layout.item_order_show) {

            @Override
            protected void bindView(BaseByViewHolder<OrderItemBean> holder, OrderItemBean bean, int position) {

                holder.setText(R.id.tvType, bean.getType());//订餐类型
                holder.setText(R.id.tvDate, "就餐日期:" + TimeUtil.getYMD(bean.getDate()));
                holder.setText(R.id.tvItemTitle, bean.getTitle());//档口
                holder.setText(R.id.tvStatus, bean.getStatus());//状态
                holder.setText(R.id.tvPrice, "￥" + bean.getPrice());
                holder.setText(R.id.tvCount, "共" + bean.getCount() + "件");

                ByRecyclerView picRecyclerView = holder.getView(R.id.itemPicRecyclerView);
                loadPic(picRecyclerView, bean.getUrls());
                holder.addOnClickListener(R.id.tvDetailComment);

            }
        };
        itemAdapter.setNewData(itemList);
        byRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        byRecyclerView.set
        byRecyclerView.setAdapter(itemAdapter);
        byRecyclerView.setOnItemChildClickListener((view, position) -> {
            Log.d(TAG, "onclick:::" + position + ",:" + view);

            Intent intent = new Intent(getActivity(), CommentActivity.class);
            intent.putExtra("id", itemList.get(position).getId());
            startActivity(intent);
        });

        //订单详情
        byRecyclerView.setOnItemClickListener((v, position) -> {
            Activity activity = getActivity();

            if (activity != null) {
                orderId = itemList.get(position).getId();
                QueryActivity queryActivity = (QueryActivity) activity;
                queryActivity.orderDetail(orderId);

            }

        });
    }

    private void loadPic(ByRecyclerView picRecyclerView, String[] pics) {

        if (getActivity() == null || pics == null || pics.length < 1)
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
        picRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        picRecyclerView.setAdapter(picAdapter);
        picAdapter.setNewData(Arrays.asList(pics));

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.tvSearch://搜索

                String searchContent = etSearch.getText().toString();

                break;

            case R.id.tvFilter://过滤
                break;

        }

    }
}