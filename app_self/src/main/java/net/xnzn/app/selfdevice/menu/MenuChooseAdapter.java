package net.xnzn.app.selfdevice.menu;

import net.xnzn.app.selfdevice.R;
import net.xnzn.app.selfdevice.menu.bean.MenuChooseBean;

import java.util.List;

import me.jingbin.library.adapter.BaseByViewHolder;
import me.jingbin.library.adapter.BaseRecyclerAdapter;

public class MenuChooseAdapter extends BaseRecyclerAdapter<MenuChooseBean> {

    private List<MenuChooseBean> lists;

    public MenuChooseAdapter(List<MenuChooseBean> data) {
        super(R.layout.menu_choose_item, data);
        this.lists = data;
    }

    @Override
    protected void bindView(BaseByViewHolder<MenuChooseBean> holder, MenuChooseBean bean, int position) {

        if (lists== null)
            return;

//        holder.setImageResource(R.id.iv, lists.get(position).getPicUrl());
        holder.setText(R.id.tvMenuName, lists.get(position).getName());
    }
}
