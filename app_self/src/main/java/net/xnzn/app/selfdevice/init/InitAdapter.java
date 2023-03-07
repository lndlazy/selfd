package net.xnzn.app.selfdevice.init;

import android.widget.TextView;

import net.xnzn.app.selfdevice.R;

import me.jingbin.library.adapter.BaseByViewHolder;
import me.jingbin.library.adapter.BaseRecyclerAdapter;

public class InitAdapter extends BaseRecyclerAdapter<InitInfo> {

    public InitAdapter() {
        super(R.layout.aba_item_init);
    }

    @Override
    protected void bindView(BaseByViewHolder<InitInfo> holder, InitInfo item, int position) {
        TextView tvContent = holder.getView(R.id.tvContent);
        if (item.isSuccess()) {
            tvContent.setTextColor(tvContent.getContext().getResources().getColor(R.color.text_color_33));
        } else {
            tvContent.setTextColor(tvContent.getContext().getResources().getColor(R.color.color_f0));
        }
        tvContent.setText(item.getContent());
    }
}
