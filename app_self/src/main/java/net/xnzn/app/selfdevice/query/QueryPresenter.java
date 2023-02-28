package net.xnzn.app.selfdevice.query;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import net.xnzn.app.selfdevice.R;

import java.util.ArrayList;
import java.util.List;

import me.jingbin.library.ByRecyclerView;
import me.jingbin.library.adapter.BaseByViewHolder;
import me.jingbin.library.adapter.BaseRecyclerAdapter;

public class QueryPresenter {

    private QueryView queryView;

    public QueryPresenter(QueryView queryView) {
        this.queryView = queryView;
    }


    public void showDetailView(View view) {

        TextView tvDetailName = view.findViewById(R.id.tvDetailName);
        TextView tvTel = view.findViewById(R.id.tvTel);
        TextView tvDetailAddress = view.findViewById(R.id.tvDetailAddress);
        TextView tvDetailType = view.findViewById(R.id.tvDetailType);
        TextView tvDetailDate = view.findViewById(R.id.tvDetailDate);
        TextView tvOrderName = view.findViewById(R.id.tvOrderName);
        ByRecyclerView orderRecyclerView = view.findViewById(R.id.orderRecyclerView);
        TextView tvOrderMoney = view.findViewById(R.id.tvOrderMoney);
        TextView tvPeisong = view.findViewById(R.id.tvPeisong);//配送费
        TextView tvPackage = view.findViewById(R.id.tvPackage);//包装费
        TextView tvTicket = view.findViewById(R.id.tvTicket);//票券
        TextView tvPay = view.findViewById(R.id.tvPay);//实付金额

        TextView tvSendStyle = view.findViewById(R.id.tvSendStyle);//配送方式
        TextView tvUserName = view.findViewById(R.id.tvUserName);//收件人
        TextView tvUserPhone = view.findViewById(R.id.tvUserPhone);//手机电话
        TextView tvUserDesc = view.findViewById(R.id.tvUserDesc);//备注
        TextView tvOrderNum = view.findViewById(R.id.tvOrderNum);//订单编号
        TextView tvCreateTime = view.findViewById(R.id.tvCreateTime);//创建时间
        TextView tvPayTime = view.findViewById(R.id.tvPayTime);//支付时间
        TextView tvPayType = view.findViewById(R.id.tvPayType);//支付方式
        TextView tvDetailStatus = view.findViewById(R.id.tvDetailStatus);//订单状态


        if (queryView.getMyAppContext() == null)
            return;

        BaseRecyclerAdapter<String> adapter = new BaseRecyclerAdapter<String>(R.layout.item_order_comment) {

            @Override
            protected void bindView(BaseByViewHolder<String> holder, String bean, int position) {

            }
        };
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(queryView.getMyAppContext()));
        orderRecyclerView.setAdapter(adapter);

        List<String> list = new ArrayList<>();
        list.add("");
        list.add("");
        list.add("");
        adapter.setNewData(list);
    }
}