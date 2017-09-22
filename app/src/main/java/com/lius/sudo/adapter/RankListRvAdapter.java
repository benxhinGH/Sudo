package com.lius.sudo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lius.sudo.R;
import com.lius.sudo.model.RankRvData;

import java.util.List;

/**
 * Created by UsielLau on 2017/9/22 0022 14:18.
 */

public class RankListRvAdapter extends RecyclerView.Adapter<RankListRvAdapter.ViewHolder> {

    private Context context;
    private List<RankRvData> datas;

    public RankListRvAdapter(Context context,List<RankRvData> datas){
        this.context=context;
        this.datas=datas;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder=new ViewHolder(LayoutInflater.from(context).inflate(
                R.layout.item_rank_rv,parent,false
        ));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.rankTv.setText(String.valueOf(datas.get(position).getRank()));
        holder.playerNameTv.setText(datas.get(position).getPlayerName());
        holder.gameTimeTv.setText(datas.get(position).getGameTime());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder{
        TextView rankTv;
        TextView playerNameTv;
        TextView gameTimeTv;

        public ViewHolder(View itemView) {
            super(itemView);
            rankTv=(TextView)itemView.findViewById(R.id.rank_tv);
            playerNameTv=(TextView)itemView.findViewById(R.id.player_name_tv);
            gameTimeTv=(TextView)itemView.findViewById(R.id.game_time_tv);
        }
    }
}
