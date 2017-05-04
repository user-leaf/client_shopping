package com.bjaiyouyou.thismall.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.activity.SystemPushMessageActivity;
import com.bjaiyouyou.thismall.activity.WebShowActivity;
import com.bjaiyouyou.thismall.client.Api4Home;
import com.bjaiyouyou.thismall.client.ClientAPI;
import com.bjaiyouyou.thismall.model.PushMessage;
import com.bjaiyouyou.thismall.user.CurrentUserManager;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;

/**
 * 系统消息列表适配器
 * author Qxh
 * created at 2017/4/14 14:20
 */
public class SystemPushMessageAdapter extends BaseSwipeAdapter implements View.OnClickListener{

    private List<PushMessage.DataBean> mList;
    private Context mContext;
    private SystemPushMessageActivity mActivity;
    private Api4Home mApi4Home;
    private int mPosition;

    public SystemPushMessageAdapter(List<PushMessage.DataBean> list, Context context, SystemPushMessageActivity activity,Api4Home api4Home) {
        mList = list;
        mContext = context;
        mActivity = activity;
        mApi4Home=api4Home;
    }

    /**
     * 追加数据集
     *
     * @param list
     */
    public void addData(List<PushMessage.DataBean> list) {
        if (mList != null) {
            mList.addAll(list);
        }
        this.notifyDataSetChanged();
    }
    /**
     * 重新设置数据
     *
     * @param list
     */
    public void reSetData(List<PushMessage.DataBean> list) {
        if (mList != null) {
            mList.clear();
            mList.addAll(list);
        }
        this.notifyDataSetChanged();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.sample1_push_message;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        View ret = null;
        ret = LayoutInflater.from(mContext).inflate(R.layout.item_system_push_message, null);
        return ret;
    }

    @Override
    public void fillValues(int position, View convertView) {
        //获得控件
        RelativeLayout rlOnClick= (RelativeLayout) convertView.findViewById(R.id.rl_push_message);
        TextView tvTitle= (TextView) convertView.findViewById(R.id.tv_push_message_title);
        TextView tvAbstract= (TextView) convertView.findViewById(R.id.tv_push_message_abstract);
        TextView tvTime= (TextView) convertView.findViewById(R.id.tv_push_message_time);
        TextView tvIsRead= (TextView) convertView.findViewById(R.id.tv_push_message_isread);
        SwipeLayout swipeLayout=(SwipeLayout) convertView.findViewById(getSwipeLayoutResourceId(position));
        TextView tvDelete = (TextView) convertView.findViewById(R.id.tv_push_message_delete);


        //当用户没有登录的时候禁止滑动
        if (!CurrentUserManager.isLoginUser()){
            swipeLayout.setSwipeEnabled(false);
        }

        //添加点击事件
        //点击条目跳转到Html5详情页
        rlOnClick.setOnClickListener(this);
        //侧滑删除
        tvDelete.setOnClickListener(this);

        //添加标记，标识删除或者点击的是哪一个条目
        rlOnClick.setTag(position);
        tvDelete.setTag(position);

        //展示数据
        swipeLayout.close();

        //填充数据
        PushMessage.DataBean pushMessage = mList.get(position);

        if (pushMessage != null) {
            String title=pushMessage.getTitle();
            String abstractString=pushMessage.getDesc();
            String time=pushMessage.getUpdated_at();
            if (!TextUtils.isEmpty(title)){
                tvTitle.setText(title);
            }
            if (!TextUtils.isEmpty(time)){
                tvTime.setText(time);
            }
            if (!TextUtils.isEmpty(abstractString)){
                tvAbstract.setText(abstractString);
            }
        }

        //处理未读标识是否显示
        //用户已经登录根据字段显示是否已读
        if (CurrentUserManager.isLoginUser()){
            int isRead=pushMessage.getIs_read();
            if (isRead==0){
                //未读
                tvIsRead.setVisibility(View.VISIBLE);
                //已读
            }else  if(isRead==1){
                tvIsRead.setVisibility(View.INVISIBLE);
            }
            //用户没有登录直接显示为已经读取
        }else {
            tvIsRead.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getCount() {
        return mList==null?0:mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 处理点击时间
     * @param v
     */
    @Override
    public void onClick(View v) {
        Object tag=v.getTag();
        boolean isClick=tag!=null&&tag instanceof Integer;
        if (isClick){
            mPosition= (int) tag;
            switch (v.getId()){
                //点击跳转详情页
                case R.id.rl_push_message:
                    intoDetails();
                    break;

                //点击删除消息
                case R.id.tv_push_message_delete:

                    deleteMessage();
                    break;

                default:
                    return;

            }
        }

    }

    /**
     * 删除消息
     */
    private void deleteMessage() {
//        ToastUtils.showShort("删除消息"+mPosition);
        String  id=mList.get(mPosition).getId();
        mApi4Home.deletePushMessage(id, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.e("deletePushMessage","删除失败");
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.e("deletePushMessage","删除成功");
                mList.remove(mPosition);
                notifyDataSetChanged();
                //实时的刷新数据
                mActivity.onRecycleViewRefresh();
                mActivity.isRefresh=true;
            }
        });

    }

    /**
     * 跳转消息详情
     */
    private void intoDetails() {
        String  id=mList.get(mPosition).getId();
        LogUtils.e("id",id);
        //根据Id跳转
        StringBuffer sb=new StringBuffer(ClientAPI.URL_WX_H5);
        sb.append("message_detail.html?id=");
        sb.append(id);
        sb.append("&token=");
        sb.append(CurrentUserManager.getUserToken());
        sb.append("&type=");
        sb.append("android");

        String webShowUrl=sb.toString().trim();
        WebShowActivity.actionStart(mActivity,webShowUrl, null);
    }

}