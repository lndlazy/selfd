package net.xnzn.app.selfdevice.menu.adapter;

import android.content.Context;

import net.xnzn.app.selfdevice.R;
import net.xnzn.app.selfdevice.menu.bean.MenuDateShowBean;

import java.util.List;

import me.jingbin.library.adapter.BaseByViewHolder;
import me.jingbin.library.adapter.BaseRecyclerAdapter;

public class MenuDateAdapter extends BaseRecyclerAdapter<MenuDateShowBean> {

    private List<MenuDateShowBean> lists;
    private int currentChoosePosition;
    private Context context;

    public MenuDateAdapter(Context context, List<MenuDateShowBean> data) {
        super(R.layout.menu_date_select_item, data);
        this.context = context;
        this.lists = data;
    }

    public void setCurrentChoosePosition(int currentChoosePosition) {
        this.currentChoosePosition = currentChoosePosition;
    }


    @Override
    protected void bindView(BaseByViewHolder<MenuDateShowBean> holder, MenuDateShowBean bean, int position) {

        if (context == null || lists == null || lists.get(position) == null)
            return;

//        holder.setImageResource(R.id.iv, lists.get(position).getPicUrl());
        if (lists.get(position).getDate() != null)
            holder.setText(R.id.tvDate, lists.get(position).getDate());

        if (lists.get(position).getWeek() != null)
            holder.setText(R.id.tvWeek, lists.get(position).getWeek());

        if (position == currentChoosePosition) {
//            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.theme_color));
            holder.itemView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.shape_theme_color_little));
            holder.setTextColor(R.id.tvDate, context.getResources().getColor(R.color.white));
            holder.setTextColor(R.id.tvWeek, context.getResources().getColor(R.color.white));

        } else {

            holder.itemView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.shape_gray_bg_cir));
            holder.setTextColor(R.id.tvDate, context.getResources().getColor(R.color.text_color_4d));
            holder.setTextColor(R.id.tvWeek, context.getResources().getColor(R.color.text_color_98));

        }

    }
}
