package com.bjaiyouyou.thismall.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.model.ContactModel;
import com.bjaiyouyou.thismall.widget.SideBar;

import java.util.List;

/**
 * 邀请页-我邀请的
 * <p/>
 * User: kanbin
 * Date: 2016/8/24
 */
public class InviteMineAdapter extends BaseAdapter implements SectionIndexer {

    private Context mContext;
    private List<ContactModel> mList;

    public InviteMineAdapter(Context context, List<ContactModel> list) {
        mContext = context;
        mList = list;
    }

    /**
     * 设置数据
     *
     * @param list
     */
    public void setData(List<ContactModel> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    /**
     * 追加数据
     *
     * @param list
     */
    public void addDate(List<ContactModel> list) {
        if (this.mList != null) {
            this.mList.addAll(list);
            notifyDataSetChanged();
        }
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateDate(List<ContactModel> list) {
        this.mList = list;
        notifyDataSetChanged();
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
        final ContactModel mContent = mList.get(position);
        // 1. convertView的复用
        if (convertView != null) {
            ret = convertView;
        } else {
            ret = LayoutInflater.from(mContext).inflate(R.layout.item_invite_mine, parent, false);
        }

        // 2. 减少findViewById()的次数
        ViewHolder holder = (ViewHolder) ret.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.tvLetter = (TextView) ret.findViewById(R.id.item_invite_mine_catalog);
            holder.name = (TextView) ret.findViewById(R.id.item_invite_mine_tv_name);
            holder.vip = (ImageView) ret.findViewById(R.id.item_invite_mine_iv_vip);

            ret.setTag(holder);
        }

        //根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);

        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            holder.tvLetter.setVisibility(View.VISIBLE);
            holder.tvLetter.setText(mContent.getSortLetters());
        } else {
            holder.tvLetter.setVisibility(View.GONE);
        }

        // 如果是*就隐藏
        if (SideBar.markVip.equals(mContent.getSortLetters())){
            holder.tvLetter.setVisibility(View.GONE);
        }

        // init
        holder.name.setText("");
        holder.vip.setVisibility(View.INVISIBLE);

        // 3. 显示内容
        ContactModel contactModel = mList.get(position);
        String name = contactModel.getName();
        String tel = contactModel.getTel();

        if (contactModel != null) {
            if (!TextUtils.isEmpty(name)) {
                // 有昵称则显示昵称
                holder.name.setText(name);
            }else {
                // 没有昵称就显示手机号
                holder.name.setText(tel);
            }

            holder.vip.setVisibility(contactModel.isVip() ? View.VISIBLE : View.INVISIBLE);
        }
        return ret;
    }

    class ViewHolder {
        TextView tvLetter;
        TextView name;
        ImageView vip;
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return mList.get(position).getSortLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = mList.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public Object[] getSections() {
        return null;
    }
}
