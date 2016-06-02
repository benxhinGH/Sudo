package com.lius.sudo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by lius on 16-5-7.
 */
public class ArchiveActivity extends Activity{

    private ListView archive_listview;
    private List<ArchiveDate> archive_list=new ArrayList<>();
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.archive_activity_layout);
        final SharedPreferences preferences=getSharedPreferences("sudoarchdata",MODE_PRIVATE);
        getDataFromPref(preferences);
        myAdapter=new MyAdapter(this,archive_list,preferences);
        archive_listview=(ListView)findViewById(R.id.archive_listview);
        archive_listview.setAdapter(myAdapter);
        archive_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String archTime=archive_list.get(i).getPrefName();
                String data=preferences.getString(archTime,"");
                Intent intent=new Intent(ArchiveActivity.this,MainActivity.class);
                intent.putExtra("flag","1");
                intent.putExtra("data",data);
                startActivity(intent);
                finish();
            }
        });
    }
    private void getDataFromPref(SharedPreferences sp){

        Map<String,?> map=sp.getAll();
        if(map.isEmpty()){
            archive_list.clear();
            return;
        }
        Set<String> stringSet=map.keySet();
        Object[] objects=stringSet.toArray();

        for(int i=0;i<objects.length;++i){
            String str=objects[i].toString();
            String time=str.substring(0,19);
            String level=str.substring(19);
            ArchiveDate archiveDate=new ArchiveDate(time,level);
            archive_list.add(archiveDate);
        }
    }
}



