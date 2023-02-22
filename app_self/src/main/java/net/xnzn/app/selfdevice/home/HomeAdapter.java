package net.xnzn.app.selfdevice.home;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import net.xnzn.app.selfdevice.R;

import java.util.List;

import me.jingbin.library.adapter.BaseByViewHolder;
import me.jingbin.library.adapter.BaseRecyclerAdapter;

public class HomeAdapter extends BaseRecyclerAdapter<HomeItem> {

    private List<HomeItem> homeItems;
    private Context context;

    public HomeAdapter(Context ctx, List<HomeItem> homeItems) {
        super(R.layout.layout_home_item);
        context = ctx;
        this.homeItems = homeItems;

//        options.placeholder(R.mipmap.place_holder)
//                .error(R.mipmap.error)
//                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
//                .skipMemoryCache(false)
//                .priority(Priority.NORMAL);
    }

//    RequestOptions options = new RequestOptions();


//    @Override
//    public int getItemCount() {
//        return homeItems == null ? 0 : homeItems.size();
//    }

    @Override
    protected void bindView(BaseByViewHolder<HomeItem> holder, HomeItem bean, int position) {

//        L.i("----------------- bindView -----------------");
//        ImageView ivImg = holder.getView(R.id.ivImg);
//        TextView tvContent = holder.getView(R.id.tvContent);
        TextView tvDesc = holder.getView(R.id.tvDesc);

//        Glide.with(context)
//                .load(homeItems.get(position).getResId())
//                .apply(options)
//                .into(ivImg);
        holder.setImageResource(R.id.ivImg, homeItems.get(position).getResId());
        holder.setText(R.id.tvContent, homeItems.get(position).getContent());
//        tvContent.setText(homeItems.get(position).getContent());
        if (TextUtils.isEmpty(homeItems.get(position).getDesc())) {
            tvDesc.setVisibility(View.GONE);
        } else {
            tvDesc.setVisibility(View.VISIBLE);
            holder.setText(R.id.tvDesc, homeItems.get(position).getDesc());
//            tvDesc.setText(homeItems.get(position).getDesc());
        }

//        ImageLoaderManager.getInstance().displayImageForView(ivImg, "");
//        ivImg.setImageResource();

//        holder.itemView.

    }
}
