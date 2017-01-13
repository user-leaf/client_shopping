package com.bjaiyouyou.thismall.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bjaiyouyou.thismall.Constants;
import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.activity.AddressAddActivity;
import com.bjaiyouyou.thismall.activity.AddressManagerNewActivity;
import com.bjaiyouyou.thismall.activity.OrderMakeActivity;
import com.bjaiyouyou.thismall.client.ClientAPI;
import com.bjaiyouyou.thismall.model.AddressInfoNew;
import com.bjaiyouyou.thismall.user.CurrentUserManager;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.bjaiyouyou.thismall.utils.ToastUtils;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;

/**
 * 新的收货地址列表适配器
 *
 * @author kanbin
 * @date 2016/6/12
 */
public class AddressNewAdapter extends BaseSwipeAdapter implements View.OnClickListener {

    private Context mContext;
    private List<AddressInfoNew.MemberAddressesBean> mList;
    // 记录选择位置，实现单选
    private int clickPosition = -1;
    private SwipeLayout sample1;
    private String pageName; // 从上一页传过来的flag，用于标记哪一页，如果是确认订单页，则条目点击后finish

    public AddressNewAdapter(Context context, List<AddressInfoNew.MemberAddressesBean> list, String pageName) {
        mContext = context;
        mList = list;
        this.pageName = pageName;
    }

    /**
     * 设置数据集
     *
     * @param list
     */
    public void setData(List<AddressInfoNew.MemberAddressesBean> list) {
        mList = list;
        this.notifyDataSetChanged();
    }

    /**
     * 追加数据集
     *
     * @param list
     */
    public void addData(List<AddressInfoNew.MemberAddressesBean> list) {
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
    public int getSwipeLayoutResourceId(int position) {
        return R.id.sample1;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        View ret = null;

        ret = LayoutInflater.from(mContext).inflate(R.layout.item_address_new, null);
        SwipeLayout swipeLayout = (SwipeLayout) ret.findViewById(getSwipeLayoutResourceId(position));

        return ret;
    }

    @Override
    public void fillValues(int position, View convertView) {

        ImageView ivTopSide = (ImageView) convertView.findViewById(R.id.address_item_iv_mark_top);
        ImageView ivBottomSide = (ImageView) convertView.findViewById(R.id.address_item_iv_mark_bottom);

        Button btnClear = (Button) convertView.findViewById(R.id.address_item_btn_clear);

        TextView tvName = (TextView) convertView.findViewById(R.id.address_item_tv_name);
        TextView tvTel = (TextView) convertView.findViewById(R.id.address_item_tv_tel);
        TextView tvAddress = (TextView) convertView.findViewById(R.id.address_item_address);

        ImageView edit = (ImageView) convertView.findViewById(R.id.address_item_iv_edit);
        TextView topIt = (TextView) convertView.findViewById(R.id.item_topit);
        TextView delete = (TextView) convertView.findViewById(R.id.item_delete);

        SwipeLayout swipeLayout = (SwipeLayout) convertView.findViewById(R.id.sample1);

        btnClear.setOnClickListener(this);
        edit.setOnClickListener(this);
        topIt.setOnClickListener(this);
        delete.setOnClickListener(this);

        // 3. 显示数据
        AddressInfoNew.MemberAddressesBean memberAddressesBean = mList.get(position);

        // init
        swipeLayout.close();
        topIt.setVisibility(memberAddressesBean.isIs_default() ? View.GONE : View.VISIBLE);

        // 控制默认地址的花边的显示与隐藏
        if (memberAddressesBean.isIs_default()) {
            ivTopSide.setVisibility(View.VISIBLE);
            ivBottomSide.setVisibility(View.VISIBLE);
        } else {
            ivTopSide.setVisibility(View.INVISIBLE);
            ivBottomSide.setVisibility(View.INVISIBLE);
        }

        tvName.setText(memberAddressesBean.getContact_person());
        tvTel.setText(memberAddressesBean.getContact_phone());
        tvAddress.setText(memberAddressesBean.getProvince() + memberAddressesBean.getCity() + memberAddressesBean.getDistrict() + memberAddressesBean.getAddress_detail());

        // 4. 为带有点击事件的按钮设置Tag，用于告诉监听器，到底点击哪一个
        btnClear.setTag(position);
        edit.setTag(position);
        topIt.setTag(position);
        delete.setTag(position);
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View ret = null;
//
//        // 1. ConvertView复用
//        if (convertView != null) {
//            ret = convertView;
//        } else {
//            ret = LayoutInflater.from(mContext).inflate(R.layout.item_address_new, parent, false);
//
//        }
//
//        // 2. 减少findViewById的次数
//        ViewHolder holder = (ViewHolder) ret.getTag();
//        if (holder == null) {
//            holder = new ViewHolder();
//
//            sample1 = (SwipeLayout) ret.findViewById(R.id.sample1);
//            sample1.setShowMode(SwipeLayout.ShowMode.PullOut);
//            sample1.addDrag(SwipeLayout.DragEdge.Right, sample1.findViewById(R.id.bottom_wrapper_2));
//
//            holder.ivTopSide = (ImageView) ret.findViewById(R.id.address_item_iv0);
//            holder.ivBottomSide = (ImageView) ret.findViewById(R.id.address_item_iv1);
//
//            holder.btnClear = (Button) ret.findViewById(R.id.address_item_btn_clear);
//
//            holder.tvName = (TextView) ret.findViewById(R.id.address_item_tv_name);
//            holder.tvTel = (TextView) ret.findViewById(R.id.address_item_tv_tel);
//            holder.tvAddress = (TextView) ret.findViewById(R.id.address_item_address);
////            holder.tvAddressProvince = (TextView) ret.findViewById(R.id.address_item_address_province);
////            holder.tvAddressCity = (TextView) ret.findViewById(R.id.address_item_address_city);
////            holder.tvAddressCounty = (TextView) ret.findViewById(R.id.address_item_address_county);
////            holder.tvAddressStreet = (TextView) ret.findViewById(R.id.address_item_address_street);
//
//            holder.edit = (ImageView) ret.findViewById(R.id.item_iv_edit);
//            holder.topIt = (TextView) ret.findViewById(R.id.item_topit);
//            holder.delete = (TextView) ret.findViewById(R.id.item_delete);
//
//            holder.btnClear.setOnClickListener(this);
//            holder.edit.setOnClickListener(this);
//            holder.topIt.setOnClickListener(this);
//            holder.delete.setOnClickListener(this);
//
//            ret.setTag(holder);
//        }
//
//        // 3. 显示数据
//        AddressInfoNew.MemberAddressesBean memberAddressesBean = mList.get(position);
//
//        // 控制默认地址的花边的显示与隐藏
//        if (memberAddressesBean.isIs_default()) {
//            holder.ivTopSide.setVisibility(View.VISIBLE);
//            holder.ivBottomSide.setVisibility(View.VISIBLE);
//        } else {
//            holder.ivTopSide.setVisibility(View.INVISIBLE);
//            holder.ivBottomSide.setVisibility(View.INVISIBLE);
//        }
//
//        holder.tvName.setText(memberAddressesBean.getContact_person());
//        holder.tvTel.setText(memberAddressesBean.getContact_phone());
//        holder.tvAddress.setText(memberAddressesBean.getProvince() + memberAddressesBean.getCity() + memberAddressesBean.getDistrict() + memberAddressesBean.getAddress_detail());
//
//
//        // 4. 为带有点击事件的按钮设置Tag，用于告诉监听器，到底点击哪一个
//        holder.btnClear.setTag(position);
//        holder.edit.setTag(position);
//        holder.topIt.setTag(position);
//        holder.delete.setTag(position);
//
//        return ret;
//    }

    @Override
    public void onClick(View v) {
        Object tag = v.getTag();

        Integer position;
        switch (v.getId()) {

            case R.id.address_item_btn_clear: // 透明图层
                if (OrderMakeActivity.TAG.equals(pageName)) {
                    if (tag != null && tag instanceof Integer) {
                        position = (Integer) tag;
                        AddressInfoNew.MemberAddressesBean memberAddressesBean = mList.get(position);
                        if (memberAddressesBean != null) {
                            Intent intent = new Intent();
                            intent.putExtra("name", memberAddressesBean.getContact_person());
                            intent.putExtra("tel", memberAddressesBean.getContact_phone());

                            String address = memberAddressesBean.getProvince() + memberAddressesBean.getCity() + memberAddressesBean.getDistrict() + memberAddressesBean.getAddress_detail();
                            intent.putExtra("address", address);
                            intent.putExtra("isDefault", memberAddressesBean.isIs_default());
                            ((AddressManagerNewActivity)mContext).setResult(Activity.RESULT_OK, intent);
                            ((AddressManagerNewActivity)mContext).finish();
                        }

                    }
                }
                break;

            case R.id.address_item_iv_edit: // 编辑
                if (tag != null && tag instanceof Integer) {
                    position = (Integer) tag;
                    if (mList.get(position) != null) {
//                        ToastUtils.showShort("编辑");
                        Intent intent = new Intent(mContext, AddressAddActivity.class);
                        intent.putExtra("pageType", Constants.ADDRESS_EDIT);
                        intent.putExtra("address", mList.get(position));
                        mContext.startActivity(intent);
                    }
                }
                break;
            case R.id.item_delete: // 删除
                doDelete(v);
                break;
            case R.id.item_topit: // 置顶(设为默认地址)
                setDefaultAddress(v);
                break;
        }
    }

    /**
     * 执行删除
     *
     * @param v
     */
    private void doDelete(View v) {
        Object tag = v.getTag();
        final Integer position;
        if (tag != null && tag instanceof Integer) {
            position = (Integer) tag;

            long id = mList.get(position).getId();
            String userToken = CurrentUserManager.getUserToken();
            StringBuilder sb = new StringBuilder(ClientAPI.API_POINT);
            sb.append("api/v1/member/deleteAddress/")
                    .append("" + id)
                    .append("?token=")
                    .append(userToken);
            String url = sb.toString();

            LogUtils.d("AddressNewAdapter", "delete url= " + url);

            // 数据请求
            OkHttpUtils.get()
                    .url(url)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ToastUtils.showShort("删除失败" + e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (mList.get(position) != null) {
                                mList.remove(mList.get(position));
                                notifyDataSetChanged();
                            }

                        }
                    });
        }

    }

    /**
     * 设为默认地址
     * @param v
     */
    private void setDefaultAddress(View v) {
        Object tag = v.getTag();
        Integer position;

        if (tag != null && tag instanceof Integer) {
            position = (Integer) tag;
            AddressInfoNew.MemberAddressesBean memberAddressesBean = mList.get(position);

            if (memberAddressesBean != null) {

                // 本地修改为默认地址
                for (AddressInfoNew.MemberAddressesBean mab :mList) {
                    mab.setIs_default(false);
                }

                memberAddressesBean.setIs_default(true);
                this.notifyDataSetChanged();

                // 传给服务器（使用了修改地址接口）
                String userToken = CurrentUserManager.getUserToken();
                StringBuilder sb = new StringBuilder(ClientAPI.API_POINT);

                sb.append("api/v1/member/editAddress/")
                        .append(memberAddressesBean.getId())
                        .append("?token=")
                        .append(userToken);
                String url = sb.toString();

                OkHttpUtils.post()
                        .url(url)
                        .addParams("is_default", "" + 1)
                        .addParams("contact_person", memberAddressesBean.getContact_person())
                        .addParams("contact_phone", memberAddressesBean.getContact_phone())
                        .addParams("province", memberAddressesBean.getProvince())
                        .addParams("city", memberAddressesBean.getCity())
                        .addParams("district", memberAddressesBean.getDistrict())
                        .addParams("address_detail", memberAddressesBean.getAddress_detail())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                ToastUtils.showException(e);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                            }
                        });
            }
        }
    }

    private class ViewHolder {

        // 上边花纹
        private ImageView ivTopSide;
        // 下边花纹
        private ImageView ivBottomSide;

        // 收货人姓名
        private TextView tvName;
        // 电话
        private TextView tvTel;
        // 地址
        private TextView tvAddress;
        private TextView tvAddressProvince;
        private TextView tvAddressCity;
        private TextView tvAddressCounty;
        private TextView tvAddressStreet;

        // 设为默认地址
        private RadioButton rbtnIsDefault;
        // 编辑
        private TextView tvEdit;
        // 删除
        private TextView tvDelete;

        // 置顶（设为默认）
//        private ImageView topIt;
        private TextView topIt;
        // 编辑
        private ImageView edit;
        // 删除
//        private ImageView delete;
        private TextView delete;
        // 透明图层
        private Button btnClear;
    }
}
