package net.xnzn.app.selfdevice.my.fragment;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import net.xnzn.app.selfdevice.R;
import net.xnzn.app.selfdevice.my.bean.AddressBean;
import net.xnzn.app.selfdevice.ui.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import me.jingbin.library.ByRecyclerView;
import me.jingbin.library.adapter.BaseByViewHolder;
import me.jingbin.library.adapter.BaseRecyclerAdapter;

public class MyAddressFragment extends BaseFragment {

    private static final String TAG = "MyAddressFragment";
    private TextView tvNoAddress;
    private TextView tvAddAddress;
    private ByRecyclerView addressRecyclerView;
    protected BaseRecyclerAdapter<AddressBean> adapter;
    protected ConstraintLayout clEditAddress;
    protected ConstraintLayout clShowAddress;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_my_address;
    }

    @Override
    public void initView(View view) {
        tvNoAddress = view.findViewById(R.id.tvNoAddress);
        tvAddAddress = view.findViewById(R.id.tvAddAddress);
        addressRecyclerView = view.findViewById(R.id.addressRecyclerView);

        clShowAddress = view.findViewById(R.id.clShowAddress);
        clEditAddress = view.findViewById(R.id.clEditAddress);
        clEditAddress.setVisibility(View.GONE);

//        noAddressView();
        hasAddressView();
    }


    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "====onResume===");
        showShowPage();
    }


    @Override
    public void initData() {

        if (getActivity() == null)
            return;

        adapter = new BaseRecyclerAdapter<AddressBean>(R.layout.item_address) {
            @Override
            protected void bindView(BaseByViewHolder<AddressBean> holder, AddressBean bean, int position) {

//                holder.setVisible(R.id.tvChoose, true);
//                holder.setText(R.id.tvAddressDesc, );
//                holder.setText(R.id.tvAddressName, );
//                holder.setText(R.id.tvAddressTel, );
                holder.addOnClickListener(R.id.ivDelete);
                holder.addOnClickListener(R.id.ivEdit);
            }
        };

        addressRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<AddressBean> addressList = new ArrayList<>();
        addressList.add(new AddressBean());
        addressList.add(new AddressBean());
        addressList.add(new AddressBean());
        adapter.setNewData(addressList);
        addressRecyclerView.setAdapter(adapter);
        tvAddAddress.setOnClickListener(v -> {
            showEditPage();
        });

        addressRecyclerView.setOnItemChildClickListener(new ByRecyclerView.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(View view, int position) {
                if (view.getId() == R.id.ivDelete) {
                    //删除地址
                } else if (view.getId() == R.id.ivEdit) {
                    //编辑地址
                    showEditPage();
                }
            }
        });
    }

    private void noAddressView() {
        tvNoAddress.setVisibility(View.VISIBLE);
        addressRecyclerView.setVisibility(View.GONE);
    }

    private void hasAddressView() {
        tvNoAddress.setVisibility(View.GONE);
        addressRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showEditPage() {
        clEditAddress.setVisibility(View.VISIBLE);
        clShowAddress.setVisibility(View.GONE);
    }

    private void showShowPage() {
        clEditAddress.setVisibility(View.GONE);
        clShowAddress.setVisibility(View.VISIBLE);
    }
}