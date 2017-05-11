package com.bjaiyouyou.thismall.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.model.BankCard;
import com.daimajia.swipe.SwipeLayout;

import java.util.List;

/**
 * 
 * @author Alice
 *Creare 2016/6/14 14:06
 *
  * @author Alice
  *Creare 2016/6/21 14:26
  * 修改为可以侧滑的
  *
  */

public class BankCardAdapter extends BaseAdapter implements View.OnClickListener{
    private Context context;
    private List<BankCard> data;
    private SwipeLayout sample1;
    private LinearLayout llYinHangKa;

    public BankCardAdapter(Context context, List<BankCard> data,LinearLayout llYinHangKa) {
        this.context = context;
        this.data = data;
        this.llYinHangKa=llYinHangKa;
    }

    @Override
    public int getCount() {
        if (data.size()==0){
            llYinHangKa.setVisibility(View.GONE);
        }
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BankCard bankCard=data.get(position);
        ViewHolder  holder =null;
        if (convertView==null){
            holder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.item_bank_card,null);

            //侧滑删除效果
            sample1 = (SwipeLayout) convertView.findViewById(R.id.sample1);
            sample1.setShowMode(SwipeLayout.ShowMode.PullOut);
            sample1.addDrag(SwipeLayout.DragEdge.Right, sample1.findViewById(R.id.bottom_wrapper_2));


            holder.topIt = (TextView) convertView.findViewById(R.id.tv_bangdingPayment_setingdefault);
            holder.delete = (TextView) convertView.findViewById(R.id.tv_bangdingPayment_delete);
            holder.topIt.setTag(position);
            holder.delete.setTag(position);

            holder.topIt.setOnClickListener(this);
            holder.delete.setOnClickListener(this);

            holder.mIvIsQuickPayment= (ImageView) convertView.findViewById(R.id.iv_bankcard_item_quickly);
            holder.mTvBankName= (TextView) convertView.findViewById(R.id.tv_bank_name);
            holder.mTvCardName= (TextView) convertView.findViewById(R.id.tv_card_name);
            holder.mTvCardId= (TextView) convertView.findViewById(R.id.tv_card_num);
            holder.mbg= (RelativeLayout) convertView.findViewById(R.id.rl_bankcard_item);
            holder.ivName= (ImageView) convertView.findViewById(R.id.iv_bankcard_name);

            convertView.setTag(holder);
        }else {
            holder=(ViewHolder) convertView.getTag();
        }
        /**
         * 是否是快捷方式
         */

        if (bankCard.isQuicklyPayment()){
            holder.mIvIsQuickPayment.setVisibility(View.VISIBLE);
        }else {
            holder.mIvIsQuickPayment.setVisibility(View.INVISIBLE);
        }
        String bankName=bankCard.getBankName();
        holder.mTvBankName.setText(bankName);
        /**
         * 根据银行设置背景图片
         */
        if ("中国银行".equals(bankName)){
//            holder.mbg.setBackground(context.getResources().getDrawable(R.mipmap.list_bg_boc));
            holder.ivName.setImageResource(R.mipmap.list_icon_boc);
        }else if("建设银行".equals(bankName)){
//            holder.mbg.setBackground(context.getResources().getDrawable(R.mipmap.list_bg_ccb));
            holder.ivName.setImageResource(R.mipmap.list_icon_ccb);

        }
        holder.mTvCardName.setText(bankCard.getCardName());
//        获取卡号
        String  id=bankCard.getCardId();
//        替换卡号前十二位为星号
        StringBuffer stringBuffer=new StringBuffer();
//
        stringBuffer.append("****").append(" ").append("****").append(" ").append("****").append(" ").
                append(id.substring(id.length()-4,id.length()));//获取卡号的最后四位

        holder.mTvCardId.setText(stringBuffer.toString());
        return convertView;
    }


    class ViewHolder{
//        银行名称
        private TextView mTvBankName;
//        银行卡类型
        private TextView mTvCardName;
//        银行卡号
        private TextView mTvCardId;
//        是否是快捷支付
        private ImageView mIvIsQuickPayment;
//        背景
        private RelativeLayout mbg;
        // 置顶（设为默认）
        private TextView topIt;
        // 删除
        private TextView delete;
//        银的标识图
        private ImageView ivName;
    }

//    处理侧滑的点击事件
    @Override
    public void onClick(View v) {
        int indext= (int) v.getTag();
        switch (v.getId()){
            case R.id.tv_bangdingPayment_setingdefault:
                Toast.makeText(context,indext+"设置为默认支付方式",Toast.LENGTH_SHORT).show();
//                通知服务器
                break;
            case R.id.tv_bangdingPayment_delete:
                Toast.makeText(context,"删除"+indext,Toast.LENGTH_SHORT).show();
                data.remove(indext);
                notifyDataSetChanged();
                break;
        }

    }

}
