package com.lius.sudo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lius.sudo.tools.DBUtil;

import java.util.List;

/**
 * Created by lius on 16-6-2.
 */
public class MyAdapter extends BaseAdapter{
    private List<ArchiveDate> data;
    private Context mContext;
    private LayoutInflater layoutInflater;
    public MyAdapter(Context context,List<ArchiveDate> data){
        mContext=context;
        this.data=data;

        layoutInflater=LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder=null;
        if(convertView==null){
            holder=new ViewHolder();
            convertView=layoutInflater.inflate(R.layout.listitem_layout,null);
            holder.playerNameTv=(TextView)convertView.findViewById(R.id.playername_tv);
            holder.levelTv=(TextView)convertView.findViewById(R.id.level_tv);
            holder.consumeTimeTv=(TextView)convertView.findViewById(R.id.timeused_tv);
            holder.archiveTimeTv=(TextView)convertView.findViewById(R.id.arctime_tv);
            holder.deleteImage=(ImageView)convertView.findViewById(R.id.delete_image);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        holder.playerNameTv.setText(data.get(position).getPlayerName());
        holder.levelTv.setText(data.get(position).getLevel());
        holder.consumeTimeTv.setText("已用时:"+"    "+data.get(position).getConsumeStrTime());
        holder.archiveTimeTv.setText("存档时间:"+" "+data.get(position).getArchiveTime());
        holder.deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteArchData(position);
            }
        });
        return convertView;
    }
    class ViewHolder{
        TextView playerNameTv;
        TextView levelTv;
        TextView consumeTimeTv;
        TextView archiveTimeTv;
        ImageView deleteImage;
    }
    private void deleteArchData(int position){
        int intId=data.get(position).getId();
        Log.d("MyAdapter","从data中获取到的id为"+intId);
        String id=Integer.toString(intId);
        Log.d("MyAdapter","转换后的id为"+id);
        DBUtil.getDatabase(mContext).delete("Sudoku","id=?",new String[]{id});
        data.remove(position);
        notifyDataSetChanged();
        Toast.makeText(mContext,"存档已删除",Toast.LENGTH_SHORT).show();
    }

}
