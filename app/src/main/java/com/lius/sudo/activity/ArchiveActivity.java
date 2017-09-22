package com.lius.sudo.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.lius.sudo.adapter.ArchiveRvAdapter;
import com.lius.sudo.database.SudokuOpenHelper;
import com.lius.sudo.R;
import com.lius.sudo.model.ArchiveRvData;
import com.lius.sudo.model.GameDataDBModel;
import com.lius.sudo.utilities.Util;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lius on 16-5-7.
 */
public class ArchiveActivity extends AppCompatActivity{

    private Toolbar toolbar;

    private RecyclerView archiveRv;
    private ArchiveRvAdapter adapter;

    private List<ArchiveRvData> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);
        findViews();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
        setListeners();

    }

    private void initView(){
        datas=getArchiveDatas();
        adapter=new ArchiveRvAdapter(this,datas);
        archiveRv.setLayoutManager(new LinearLayoutManager(this));
        archiveRv.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        archiveRv.setAdapter(adapter);
    }

    private List<ArchiveRvData> getArchiveDatas(){
        SudokuOpenHelper openHelper=new SudokuOpenHelper(this,"Sudoku.db",null,1);
        SQLiteDatabase db=openHelper.getReadableDatabase();
        Cursor cursor=db.query("Archive",null,null,null,null,null,null);
        int id;
        String time;
        String level;
        byte[] gameData;
        List<ArchiveRvData> datas=new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                id=cursor.getInt(cursor.getColumnIndex("id"));
                time=cursor.getString(cursor.getColumnIndex("archive_time"));
                gameData=cursor.getBlob(cursor.getColumnIndex("game_data"));
                ByteArrayInputStream bais=new ByteArrayInputStream(gameData);
                try {
                    ObjectInputStream ois=new ObjectInputStream(bais);
                    GameDataDBModel gddm=(GameDataDBModel)ois.readObject();
                    level= Util.getGameLevelText(gddm.getLevel());
                    datas.add(new ArchiveRvData(id,time,level));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }while (cursor.moveToNext());
        }
        return datas;
    }


    private void findViews(){
        archiveRv=(RecyclerView)findViewById(R.id.archive_rv);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
    }


    private void setListeners(){
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        adapter.setOnItemClickListener(new ArchiveRvAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent=new Intent(ArchiveActivity.this,GameActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("gameType","archiveGame");
                bundle.putInt("archiveId",datas.get(position).getArchiveId());
                intent.putExtra("extra",bundle);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_archive,menu);
        return true;
    }
}



