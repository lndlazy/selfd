package net.xnzn.app.selfdevice.menu.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import net.xnzn.app.selfdevice.R;
import net.xnzn.app.selfdevice.menu.bean.MenuFoodsDetailBean;
import net.xnzn.app.selfdevice.utils.DensityUtil;
import net.xnzn.lib_commin_ui.recyclerview.MultiItemTypeAdapter;

import java.util.List;

import me.jingbin.library.adapter.BaseByViewHolder;
import me.jingbin.library.adapter.BaseRecyclerAdapter;
import me.jingbin.library.stickyview.StickyHeaderHandler;

public class MenuFoodsAdapter extends BaseRecyclerAdapter<MenuFoodsDetailBean> {

    private List<MenuFoodsDetailBean> lists;

    //    public MenuFoodsAdapter(Context context, List<MenuFoodsDetailBean> datas) {
//        super(context, datas);
//    }
    private Context context;

    public MenuFoodsAdapter(Context context, List<MenuFoodsDetailBean> data) {
        super(R.layout.menu_food_show_item, data);
        this.lists = data;
        this.context = context;
    }


    @Override
    protected void bindView(BaseByViewHolder<MenuFoodsDetailBean> holder, MenuFoodsDetailBean bean, int position) {

        if (lists == null)
            return;

//        ImageView ivUrl = holder.getView(R.id.ivUrl);
//        TextView tvName = holder.getView(R.id.tvName);
        ChipGroup cgTips = holder.getView(R.id.cgTips);
//        TextView tvSale = holder.getView(R.id.tvSale);
//        ImageView ivUrl = holder.getView(R.id.ivUrl);

//        holder.setImageResource(R.id.iv, lists.get(position).getPicUrl());
        holder.setText(R.id.tvName, lists.get(position).getName());
        holder.setText(R.id.tvSale, "月销" + lists.get(position).getSaleCount());
        holder.setText(R.id.tvGoods, "好评率" + lists.get(position).getGoods());
        holder.setText(R.id.tvPrice, "￥" + lists.get(position).getPrice());

        if (bean != null && bean.getTips() != null && bean.getTips().length > 0) {
            String[] tips = bean.getTips();
            ChipGroup.LayoutParams layoutParams = new ChipGroup.LayoutParams(ChipGroup.LayoutParams.WRAP_CONTENT, DensityUtil.dip2px(context.getApplicationContext(), 40));

            for (int i = 0; i < tips.length; i++) {
                Chip ch = (Chip) View.inflate(context, R.layout.item_menu_tips, null);
                ch.setText(tips[i]);
                cgTips.addView(ch, layoutParams);

            }
        }

    }
}