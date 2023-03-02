package net.xnzn.app.selfdevice.menu;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;

import net.xnzn.app.selfdevice.R;
import net.xnzn.app.selfdevice.menu.bean.MenuChooseBean;
import net.xnzn.app.selfdevice.ui.SelfCommonActivity;
import net.xnzn.app.selfdevice.utils.SelfConstant;

import java.util.ArrayList;
import java.util.List;

import me.jingbin.library.ByRecyclerView;
import me.jingbin.library.adapter.BaseByViewHolder;
import me.jingbin.library.adapter.BaseRecyclerAdapter;

public class MenuChooseActivity extends SelfCommonActivity {

    private ByRecyclerView byRecyclerView;
    private ImageView ivBack;
    private TextView tvCount;
    private List<MenuChooseBean> chooseBeanList = new ArrayList<>();

    @Override
    protected boolean showTitle() {
        return true;
    }

    @Override
    protected boolean showBar() {
        return false;
    }

    @Override
    protected boolean showTitleBar() {
        return false;
    }

    @Override
    protected void initView() {
        super.initView();
        byRecyclerView = findViewById(R.id.byRecyclerView);
        ivBack = findViewById(R.id.ivBack);
        tvCount = findViewById(R.id.tvCount);
        in_title.setBackgroundResource(R.drawable.shape_theme_title);
    }

    @Override
    protected int showView() {
        return R.layout.activity_menu_choose;
    }

    @Override
    protected void initData() {
        chooseBeanList.add(new MenuChooseBean(1, "自助餐菜谱", ""));
        chooseBeanList.add(new MenuChooseBean(2, "自助餐菜谱", ""));
        chooseBeanList.add(new MenuChooseBean(3, "自助餐菜谱", ""));
        chooseBeanList.add(new MenuChooseBean(4, "自助餐菜谱", ""));
        byRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        BaseRecyclerAdapter<MenuChooseBean> chooseAdapter = new BaseRecyclerAdapter<MenuChooseBean>(R.layout.menu_choose_item) {


            @Override
            protected void bindView(BaseByViewHolder<MenuChooseBean> holder, MenuChooseBean bean, int position) {
                holder.setText(R.id.tvMenuName, chooseBeanList.get(position).getName());
            }
        };

//        SpacesItemDecoration itemDecoration2 = new SpacesItemDecoration(this)
////                .setNoShowDivider(1, 1)
//                // 颜色，分割线间距，左边距(单位dp)，右边距(单位dp)
//                .setParam(R.color.translucent, 30, 0, 0);
//
////        recyclerView.addItemDecoration(itemDecoration);
//        byRecyclerView.addItemDecoration(itemDecoration2);

        byRecyclerView.setAdapter(chooseAdapter);
        chooseAdapter.setNewData(chooseBeanList);
        byRecyclerView.setOnItemClickListener((v, position) -> {
            startActivity(MenuShowActivity.class);
            //BG-31881908220060
        });

    }

    @Override
    protected void initLisitener() {

        ivBack.setOnClickListener(view -> finish());
    }


}
