package com.lius.sudo.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lius.sudo.R;
import com.lius.sudo.adapter.RankListRvAdapter;
import com.lius.sudo.database.SudokuOpenHelper;
import com.lius.sudo.model.RankRvData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by UsielLau on 2017/9/22 0022 14:15.
 */

public class RankListFragment extends Fragment {

    private RecyclerView rankListRv;
    private RankListRvAdapter adapter;
    private List<RankRvData> datas;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_rank_list,container,false);
        rankListRv=(RecyclerView)rootView.findViewById(R.id.rank_list_rv);
        Bundle bundle=getArguments();
        int level=bundle.getInt("level");
        datas=getRankDataByLevel(level);
        adapter=new RankListRvAdapter(getContext(),datas);
        rankListRv.setLayoutManager(new LinearLayoutManager(getContext()));
        rankListRv.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        rankListRv.setAdapter(adapter);
        return rootView;
    }

    private List<RankRvData> getRankDataByLevel(int level){
        SudokuOpenHelper openHelper=new SudokuOpenHelper(getContext(),"Sudoku.db",null,1);
        SQLiteDatabase db=openHelper.getReadableDatabase();
        Cursor cursor=db.query("Rank",null,"level = ?",new String[]{String.valueOf(level)},null,null,"game_seconds_time ASC");
        List<RankRvData> res=new ArrayList<>();
        int rank=0;
        String playerName;
        String gameTime;
        if(cursor.moveToFirst()){
            do{
                rank++;
                playerName=cursor.getString(cursor.getColumnIndex("player_name"));
                gameTime=cursor.getString(cursor.getColumnIndex("game_time"));
                res.add(new RankRvData(rank,playerName,gameTime));
            }while (cursor.moveToNext());
        }
        return res;
    }


}
