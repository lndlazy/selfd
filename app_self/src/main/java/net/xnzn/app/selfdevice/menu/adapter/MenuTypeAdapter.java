package net.xnzn.app.selfdevice.menu.adapter;

import android.content.Context;
import android.view.View;

import net.xnzn.app.selfdevice.R;
import net.xnzn.app.selfdevice.menu.bean.MenuTypeBean;

import java.util.List;

import me.jingbin.library.adapter.BaseByViewHolder;
import me.jingbin.library.adapter.BaseRecyclerAdapter;

public class MenuTypeAdapter extends BaseRecyclerAdapter<MenuTypeBean> {

    private List<MenuTypeBean> lists;
    private int checkedPosition = 0;
    private Context context;

    public MenuTypeAdapter(Context ctx, List<MenuTypeBean> data) {
        super(R.layout.menu_type_select_item, data);
        this.context = ctx;
        this.lists = data;
    }

    public void setCheckedPosition(int checkedPosition) {
        this.checkedPosition = checkedPosition;
    }

    @Override
    protected void bindView(BaseByViewHolder<MenuTypeBean> holder, MenuTypeBean bean, int position) {

        if (lists == null)
            return;

        View line = holder.getView(R.id.vLine);

//        holder.setImageResource(R.id.iv, lists.get(position).getPicUrl());
        holder.setText(R.id.tvType, lists.get(position).getName());

        if (position == checkedPosition) {
            line.setVisibility(View.VISIBLE);
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.text_color_f5));
        } else {
            line.setVisibility(View.GONE);
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.white));
        }

    }
}