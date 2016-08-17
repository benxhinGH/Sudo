package com.lius.sudo.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lius.sudo.model.ArchiveDate;
import com.lius.sudo.model.RankItemData;
import com.lius.sudo.tools.TimerUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 刘有泽 on 2016/8/16.
 */
public class DBUtil {
    public static List<ArchiveDate> getArchiveDataFromDB(Context context){
        List<ArchiveDate> archiveDates=new ArrayList<>();
        SudoOpenHelper sudoOpenHelper=new SudoOpenHelper(context,"Sudoku.db",null,1);
        SQLiteDatabase db=sudoOpenHelper.getWritableDatabase();
        Cursor cursor=db.query("Sudoku",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                int id=cursor.getInt(cursor.getColumnIndex("id"));
                String playerName=cursor.getString(cursor.getColumnIndex("player"));
                String level=cursor.getString(cursor.getColumnIndex("level"));
                String archiveTime=cursor.getString(cursor.getColumnIndex("archivetime"));
                int consumeIntTime=cursor.getInt(cursor.getColumnIndex("consumetime"));
                String consumeStrTime= TimerUtil.translateToStrTime(consumeIntTime);
                String number=cursor.getString(cursor.getColumnIndex("number"));
                String color=cursor.getString(cursor.getColumnIndex("color"));
                ArchiveDate archiveDate=new ArchiveDate(id,playerName,level,archiveTime,
                        consumeStrTime,consumeIntTime,number,color);
                archiveDates.add(archiveDate);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return archiveDates;
    }
    public static List<RankItemData> getRankItemDataFromDB(Context context,String level){
        List<RankItemData> rankItemDatas=new ArrayList<>();
        SQLiteDatabase db=DBUtil.getDatabase(context);
        Cursor cursor=db.query("Rank",null,"level=?",new String[]{level},null,null,"consumeinttime");
        if(cursor.moveToFirst()){
            int rank=1;
            do{
                String player=cursor.getString(cursor.getColumnIndex("player"));
                String consumeStrTime=cursor.getString(cursor.getColumnIndex("consumestrtime"));
                RankItemData rankItemData=new RankItemData(rank,player,consumeStrTime);
                rankItemDatas.add(rankItemData);
                rank++;
            }while (cursor.moveToNext());
        }
        cursor.close();
        return rankItemDatas;

    }
    public static SQLiteDatabase getDatabase(Context context){
        SudoOpenHelper sudoOpenHelper=new SudoOpenHelper(context,"Sudoku.db",null,1);
        SQLiteDatabase db=sudoOpenHelper.getWritableDatabase();
        return db;
    }
}
