package com.lius.sudo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lius.sudo.R;
import com.lius.sudo.model.ArchiveRvData;
import com.lius.sudo.utilities.Util;

import java.util.List;

/**
 * Created by UsielLau on 2017/9/21 0021 20:37.
 */

public class ArchiveRvAdapter extends RecyclerView.Adapter<ArchiveRvAdapter.ViewHolder> {

    private Context context;
    private List<ArchiveRvData> datas;

    private OnItemClickListener onItemClickListener;

    public ArchiveRvAdapter(Context context,List<ArchiveRvData> datas){
        this.context=context;
        this.datas=datas;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder=new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_archive_rv,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.archiveTimeTv.setText(datas.get(position).getArchiveTime());
        holder.levelTv.setText(datas.get(position).getLevel());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null){
                    onItemClickListener.onClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView archiveTimeTv;
        TextView levelTv;
        public ViewHolder(View itemView) {
            super(itemView);
            archiveTimeTv=(TextView)itemView.findViewById(R.id.archive_time_tv);
            levelTv=(TextView)itemView.findViewById(R.id.level_tv);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onClick(int position);
    }

}
