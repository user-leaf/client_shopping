package shop.imake.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import shop.imake.R;
import shop.imake.model.MineFriend;

import java.util.List;

/**
 *
 * @author Alice
 *Creare 2016/6/22 16:51
 * 我的好友适配器
 *
 */
public class MineFriendAdapter extends MyBaseAdapter<MineFriend>{
    private List<MineFriend> datas;
    private Context context;
    public MineFriendAdapter(List<MineFriend> datas, Context context) {
        super(datas, context);
        this.datas=datas;
        this.context=context;
    }

    @Override
    public View getItemView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView==null){
            convertView=getInflater().inflate(R.layout.item_mine_friend_lv,null);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        if (holder==null){
            holder=new ViewHolder();
            holder.tvId= (TextView) convertView.findViewById(R.id.tv_mine_friend_id);
            holder.tvName= (TextView) convertView.findViewById(R.id.tv_mine_friend_name);
            convertView.setTag(holder);
        }
//        获得并且填充数据
        MineFriend friend=datas.get(position);
        holder.tvId.setText(friend.getFriendId());
        holder.tvName.setText(friend.getFriendName());
        return convertView;
    }
    class ViewHolder{
        TextView tvId;
        TextView tvName;
    }
}
