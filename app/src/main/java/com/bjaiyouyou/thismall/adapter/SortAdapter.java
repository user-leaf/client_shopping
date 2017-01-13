package com.bjaiyouyou.thismall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.client.ClientAPI;
import com.bjaiyouyou.thismall.model.ContactModel;
import com.bjaiyouyou.thismall.user.CurrentUserManager;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.bjaiyouyou.thismall.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 通讯录朋友页面列表适配器
 */
public class SortAdapter extends BaseAdapter implements SectionIndexer {
    private static final String TAG = SortAdapter.class.getSimpleName();

    private List<ContactModel> list = null;
    private Context mContext;

    // 存储已添加的联系人的位置(判断手机号，且号码应唯一)
    private static List<String> sSelectedTelList;

    public SortAdapter(Context mContext, List<ContactModel> list) {
        this.mContext = mContext;
        this.list = list;
        sSelectedTelList = new ArrayList<>();
    }

    /**
     * 设置数据
     *
     * @param list
     */
    public void setData(List<ContactModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    /**
     * 追加数据
     *
     * @param list
     */
    public void addDate(List<ContactModel> list) {
        if (this.list != null) {
            this.list.addAll(list);
            notifyDataSetChanged();
        }
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateDate(List<ContactModel> list) {
        this.list = list;
        notifyDataSetChanged();
        LogUtils.d(TAG, "updateData = "+ this.list.toString());
    }

    public int getCount() {
        int ret = 0;
        if (list != null) {
            ret = list.size();
        }
        return ret;
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder = null;
        final ContactModel mContent = list.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_contact, null);

            viewHolder.tvTitle = (TextView) view.findViewById(R.id.item_contact_title);
            viewHolder.tvLetter = (TextView) view.findViewById(R.id.item_contact_catalog);
            viewHolder.tvTel = (TextView) view.findViewById(R.id.item_contact_mobile);
            viewHolder.tvAdd = (TextView) view.findViewById(R.id.item_invite_mine_tv_sent);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        //根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);

        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setText(mContent.getSortLetters());
        } else {
            viewHolder.tvLetter.setVisibility(View.GONE);
        }

        viewHolder.tvTitle.setText(this.list.get(position).getName());
        viewHolder.tvTel.setText(this.list.get(position).getTel());

        final TextView tvAdd = viewHolder.tvAdd;
        tvAdd.setSelected(false);
        tvAdd.setText("邀请");

        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2016/5/17 判断是否发送、是否已是好友等等
                // TODO: 2016/5/17 本地保存选择状态

                // 发送短信请求
                String userToken = CurrentUserManager.getUserToken();
                StringBuilder sb = new StringBuilder(ClientAPI.API_POINT);

                sb.append("api/v1/auth/inviteRegisterByContacts")
                        .append("?token=").append(userToken);

                String url = sb.toString();

                OkHttpUtils.post()
                        .url(url)
                        .addParams("phone",list.get(position).getTel())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                ToastUtils.showException(e);

                            }

                            @Override
                            public void onResponse(String response, int id) {
                                if (!sSelectedTelList.contains(list.get(position).getTel())) {
                                    sSelectedTelList.add(list.get(position).getTel());
                                }

                                if (sSelectedTelList.contains(list.get(position).getTel())) {
                                    tvAdd.setSelected(true);
                                    tvAdd.setText("已邀请");
                                    tvAdd.setClickable(false);
                                }
                            }
                        });
            }
        });

        if (sSelectedTelList.contains(list.get(position).getTel())) {
            tvAdd.setSelected(true);
            tvAdd.setText("已邀请");
            tvAdd.setClickable(false);
        }

        // 已是会员
        tvAdd.setEnabled(true);
        tvAdd.setClickable(true);
        LogUtils.d(TAG, "register = "+this.list.get(position).isRegister());
        if (this.list.get(position).isRegister()){
            tvAdd.setText("已注册");
            tvAdd.setSelected(true);
            tvAdd.setEnabled(false);
            tvAdd.setClickable(false);
        }

        return view;
    }

    final static class ViewHolder {
        TextView tvLetter;
        TextView tvTitle;
        TextView tvTel;
        TextView tvAdd;
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return list.get(position).getSortLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    /**
     * 提取英文的首字母，非英文字母用#代替。
     *
     * @param str
     * @return
     */
    private String getAlpha(String str) {
        String sortStr = str.trim().substring(0, 1).toUpperCase();
        // 正则表达式，判断首字母是否是英文字母
        if (sortStr.matches("[A-Z]")) {
            return sortStr;
        } else {
            return "#";
        }
    }

    @Override
    public Object[] getSections() {
        return null;
    }
}