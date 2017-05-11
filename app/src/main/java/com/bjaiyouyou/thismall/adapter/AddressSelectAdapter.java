package com.bjaiyouyou.thismall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.model.AddressInfo2;

import java.util.List;

/**
 * 地址选择页列表适配器
 *
 * @author JackB
 * @date 2016/6/12
 */
public class AddressSelectAdapter extends BaseAdapter {

    private Context mContext;
    private List<AddressInfo2.MemberAddressesBean> mList;

    public AddressSelectAdapter(Context context, List<AddressInfo2.MemberAddressesBean> list) {
        mContext = context;
        mList = list;
    }

    public void setData(List<AddressInfo2.MemberAddressesBean> list){
        mList = list;
        this.notifyDataSetChanged();
    }

    public void addData(List<AddressInfo2.MemberAddressesBean> list){
        if (mList != null) {
            mList.addAll(list);
            this.notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        int ret = 0;
        if (mList != null) {
            ret = mList.size();
        }
        return ret;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View ret = null;

        // 1. convertView复用
        if (convertView != null) {
            ret = convertView;
        } else {
            ret = LayoutInflater.from(mContext).inflate(R.layout.item_address_select, parent, false);
        }

        // 2. 减少findViewById()的次数
        ViewHolder holder = (ViewHolder) ret.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.name = (TextView) ret.findViewById(R.id.address_select_item_name);
            holder.tel = (TextView) ret.findViewById(R.id.address_select_item_tel);
            holder.address = (TextView) ret.findViewById(R.id.address_select_item_address);
        }

        // 3. 显示数据
        AddressInfo2.MemberAddressesBean addressInfo = mList.get(position);
        if (addressInfo != null) {
            holder.name.setText(addressInfo.getContact_person());
            holder.tel.setText(addressInfo.getContact_phone());
            holder.address.setText(addressInfo.getProvince()+addressInfo.getCity()+addressInfo.getDistrict()+addressInfo.getAddress_detail());
        }

        return ret;
    }

    class ViewHolder {
        private TextView name;
        private TextView tel;
        private TextView address;
    }
}
