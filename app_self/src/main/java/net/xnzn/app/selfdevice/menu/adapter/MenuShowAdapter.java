package net.xnzn.app.selfdevice.menu.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.xnzn.app.selfdevice.R;
import net.xnzn.app.selfdevice.menu.bean.MenuFoodsDetailBean;

import java.util.List;

public class MenuShowAdapter extends RecyclerView.Adapter<MenuShowAdapter.VH> {

    private List<MenuFoodsDetailBean> lists;
    private Context context;

    final int VIEW_TYPE_TITLE = 1;
    final int VIEW_TYPE_CONTENT = 0;

    public MenuShowAdapter(Context context, List<MenuFoodsDetailBean> datas) {

        this.context = context;
        this.lists = datas;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = null;

        switch (viewType) {
            case VIEW_TYPE_TITLE:
                inflate = View.inflate(context, R.layout.menu_food_title_item, null);
                break;

            default:
                inflate = View.inflate(context, R.layout.menu_food_show_item, null);
                break;
        }

        return new VH(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {

//        int itemViewType = getItemViewType(position);

        switch (getItemViewType(position)) {
            case VIEW_TYPE_TITLE:

                holder.tvTit.setText(lists.get(position).getName());

                break;

            default:
                holder.tvName.setText(lists.get(position).getName());
                holder.tvSale.setText("月销" + lists.get(position).getSaleCount());
                holder.tvGoods.setText("好评率" + lists.get(position).getGoods());
                holder.tvPrice.setText("￥" + lists.get(position).getPrice());
                break;
        }

    }

    @Override
    public int getItemViewType(int position) {

        if (lists.get(position).getType() == -1) {
            return VIEW_TYPE_TITLE;
        } else
            return VIEW_TYPE_CONTENT;
//        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {

        return lists == null ? 0 : lists.size();
    }

    public static class VH extends RecyclerView.ViewHolder {

        TextView tvTit;
        TextView tvSale;
        TextView tvName;
        TextView tvGoods;
        TextView tvPrice;

        public VH(@NonNull View itemView) {
            super(itemView);

            tvTit = itemView.findViewById(R.id.tvTit);
            tvSale = itemView.findViewById(R.id.tvSale);
            tvName = itemView.findViewById(R.id.tvName);
            tvGoods = itemView.findViewById(R.id.tvGoods);
            tvPrice = itemView.findViewById(R.id.tvPrice);

        }
    }
} 