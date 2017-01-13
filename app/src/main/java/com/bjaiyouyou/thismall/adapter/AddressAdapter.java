package com.bjaiyouyou.thismall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.model.AddressInfo;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.bjaiyouyou.thismall.utils.ToastUtils;

import java.util.List;

/**
 * 收货地址列表适配器（旧）
 *
 * @author kanbin
 * @date 2016/6/6
 */
public class AddressAdapter extends BaseAdapter implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private Context mContext;
    private List<AddressInfo> mList;
    // 记录选择位置，实现单选
    private int clickPosition = -1;

    public AddressAdapter(Context context, List<AddressInfo> list) {
        mContext = context;
        mList = list;
    }

    /**
     * 设置数据集
     *
     * @param list
     */
    public void setData(List<AddressInfo> list) {
        mList = list;
        this.notifyDataSetChanged();
    }

    /**
     * 追加数据集
     *
     * @param list
     */
    public void addData(List<AddressInfo> list) {
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

        // 1. ConvertView复用
        if (convertView != null) {
            ret = convertView;
        } else {
            ret = LayoutInflater.from(mContext).inflate(R.layout.item_address, parent, false);
        }

        // 2. 减少findViewById的次数
        ViewHolder holder = (ViewHolder) ret.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.tvName = (TextView) ret.findViewById(R.id.address_item_tv_name);
            holder.tvTel = (TextView) ret.findViewById(R.id.address_item_tv_tel);
            holder.tvAddress = (TextView) ret.findViewById(R.id.address_item_address);
            holder.rbtnIsDefault = (RadioButton) ret.findViewById(R.id.address_item_isdefault);
            holder.tvEdit = (TextView) ret.findViewById(R.id.address_item_tv_edit);
            holder.tvDelete = (TextView) ret.findViewById(R.id.address_item_tv_delete);

            holder.rbtnIsDefault.setOnCheckedChangeListener(this);
            holder.tvEdit.setOnClickListener(this);
            holder.tvDelete.setOnClickListener(this);

            ret.setTag(holder);
        }

        // 3. 显示数据
        AddressInfo addressInfo = mList.get(position);
        holder.tvName.setText(addressInfo.getName());
        holder.tvTel.setText(addressInfo.getTel());
        holder.tvAddress.setText(addressInfo.getAddress());


        // 4. 为带有点击事件的按钮设置Tag，用于告诉监听器，到底点击哪一个
        holder.rbtnIsDefault.setTag(position);
        holder.tvEdit.setTag(position);
        holder.tvDelete.setTag(position);

//        holder.rbtnIsDefault.setChecked(addressInfo.isDefault());   // 在设置完tag之后，否则
        // 单选
        if (clickPosition == position){
            holder.rbtnIsDefault.setChecked(true);
        }else {
            holder.rbtnIsDefault.setChecked(false);
        }
        return ret;
    }

    @Override
    public void onClick(View v) {
        Object tag = v.getTag();

        Integer position;
        switch (v.getId()) {
            case R.id.address_item_tv_edit: // 编辑
                if (tag != null && tag instanceof Integer) {
                    position = (Integer) tag;
                    ToastUtils.showShort("address_item_tv_edit: " + position);
                }
                break;

            case R.id.address_item_tv_delete: // 删除
                if (tag != null && tag instanceof Integer) {
                    position = (Integer) tag;
                    if (mList.get(position)!= null) {
                        boolean remove = mList.remove(mList.get(position));
                        LogUtils.d("AddressAdapter","remove: "+remove);
                        notifyDataSetChanged();
                        LogUtils.d("AddressAdapter","list: "+mList.toString());
                    }
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Object tag = buttonView.getTag();//buttonView就只是用于获取位置了
        if (tag != null && tag instanceof Integer) {
            Integer position = (Integer) tag;
//            AddressInfo addressInfo = mList.get(position);
//            addressInfo.setDefault(isChecked);

            // 保存选择的位置，并刷新（getView()重新执行），实现单选
            if (isChecked){
                clickPosition = position;
            }
            notifyDataSetChanged();
        }

    }

    private class ViewHolder {
        // 收货人姓名
        private TextView tvName;
        // 电话
        private TextView tvTel;
        // 地址
        private TextView tvAddress;
        // 设为默认地址
        private RadioButton rbtnIsDefault;
        // 编辑
        private TextView tvEdit;
        // 删除
        private TextView tvDelete;
    }
}
