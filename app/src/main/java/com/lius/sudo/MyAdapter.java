package com.lius.sudo;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by lius on 16-6-2.
 */
public class MyAdapter extends BaseAdapter{
    private List<ArchiveDate> data;
    private Context mContext;
    private LayoutInflater layoutInflater;
    private SharedPreferences preferences;
    public MyAdapter(Context context,List<ArchiveDate> data,SharedPreferences preferences){
        mContext=context;
        this.data=data;
        layoutInflater=LayoutInflater.from(context);
        this.preferences=preferences;
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
            holder.levelTextView=(TextView)convertView.findViewById(R.id.level_textview);
            holder.timeTextView=(TextView)convertView.findViewById(R.id.arctime_textview);
            holder.deleteImage=(ImageView)convertView.findViewById(R.id.delete_image);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        holder.levelTextView.setText(data.get(position).getLevel());
        holder.timeTextView.setText(data.get(position).getArchTime());
        holder.deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteArchData(position);
            }
        });
        return convertView;
    }
    class ViewHolder{
        TextView levelTextView;
        TextView timeTextView;
        ImageView deleteImage;
    }
    private void deleteArchData(int position){
        SharedPreferences.Editor editor=preferences.edit();
        String str=data.get(position).getPrefName();
        editor.remove(str);
        editor.commit();
        data.remove(position);
        notifyDataSetChanged();
        Toast.makeText(mContext,"已删除",Toast.LENGTH_SHORT).show();
    }
}
