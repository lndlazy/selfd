package net.xnzn.app.selfdevice.query;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.xnzn.app.selfdevice.R;
import net.xnzn.app.selfdevice.ui.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import me.jingbin.library.ByRecyclerView;
import me.jingbin.library.adapter.BaseByViewHolder;
import me.jingbin.library.adapter.BaseRecyclerAdapter;

public class OrderDetailFragment extends BaseFragment {


    ByRecyclerView orderRecyclerView;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_order_detail;
    }

    @Override
    public void initView(View view) {
        orderRecyclerView = view.findViewById(R.id.orderRecyclerView);


    }

    @Override
    public void initData() {

        if (getActivity() == null)
            return;

        BaseRecyclerAdapter<String> adapter = new BaseRecyclerAdapter<String>(R.layout.item_order_comment) {

            @Override
            protected void bindView(BaseByViewHolder<String> holder, String bean, int position) {

            }
        };
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        orderRecyclerView.setAdapter(adapter);

        List<String> list = new ArrayList<>();
        list.add("");
        list.add("");
        list.add("");
        adapter.setNewData(list);
    }
}