package com.lius.sudo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.lius.sudo.model.ArchiveDate;
import com.lius.sudo.MainActivity;
import com.lius.sudo.adapter.MyAdapter;
import com.lius.sudo.R;
import com.lius.sudo.DB.DBUtil;

import java.util.List;

/**
 * Created by lius on 16-5-7.
 */
public class ArchiveActivity extends AppCompatActivity{

    private ListView archive_listview;
    private List<ArchiveDate> archive_list;
    private MyAdapter myAdapter;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.archive_activity_layout);


        archive_list= DBUtil.getArchiveDataFromDB(this);


        myAdapter=new MyAdapter(this,archive_list);


        archive_listview=(ListView)findViewById(R.id.archive_listview);
        archive_listview.setAdapter(myAdapter);
        archive_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ArchiveDate ad=archive_list.get(i);
                String level=ad.getLevel();
                switch (level){
                    case "入门级":
                        StartActivity.level=1;
                        break;
                    case "初级":
                        StartActivity.level=2;
                        break;
                    case "普通":
                        StartActivity.level=3;
                        break;
                    case "高级":
                        StartActivity.level=4;
                        break;
                    case "骨灰级":
                        StartActivity.level=5;
                        break;
                    default:
                        break;
                }
                String data=ad.getNumber()+ad.getColor();
                Intent intent=new Intent(ArchiveActivity.this,MainActivity.class);
                intent.putExtra("flag","1");
                intent.putExtra("data",data);
                intent.putExtra("consumetime",ad.getConsumeIntTime());
                intent.putExtra("id",ad.getId());
               // Log.d("ArchiveActivity","数据为"+data);
                startActivity(intent);
                finish();
            }
        });
        backButton=(Button)findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}



