package com.lius.sudo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lius.sudo.R;
import com.lius.sudo.model.RankItemData;

import java.util.List;

/**
 * Created by 刘有泽 on 2016/8/17.
 */
public class RankLvAdapter extends BaseAdapter{
    private List<RankItemData> rankItemDatas;
    private LayoutInflater layoutInflater;

    public RankLvAdapter(List<RankItemData> rankItemDatas, Context context){
        this.rankItemDatas=rankItemDatas;
        layoutInflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return rankItemDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return rankItemDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            holder=new ViewHolder();
            convertView=layoutInflater.inflate(R.layout.ranklistitem_layout,null);
            holder.rankTv=(TextView)convertView.findViewById(R.id.rank_tv);
            holder.playerTv=(TextView)convertView.findViewById(R.id.playername_tv);
            holder.consumeTimeTv=(TextView)convertView.findViewById(R.id.consumetime_tv);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        holder.rankTv.setText(Integer.toString(rankItemDatas.get(position).getRank()));
        holder.playerTv.setText(rankItemDatas.get(position).getPlayerName());
        holder.consumeTimeTv.setText(rankItemDatas.get(position).getConsumeTime());
        return convertView;
    }
    class ViewHolder{
        TextView rankTv;
        TextView playerTv;
        TextView consumeTimeTv;
    }
}
