package com.lius.sudo.tools;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lius.sudo.ArchiveDate;
import com.lius.sudo.DB.SudoOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 刘有泽 on 2016/8/16.
 */
public class DBUtil {
    public static List<ArchiveDate> getDataFromDB(Context context){
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
    public static SQLiteDatabase getDatabase(Context context){
        SudoOpenHelper sudoOpenHelper=new SudoOpenHelper(context,"Sudoku.db",null,1);
        SQLiteDatabase db=sudoOpenHelper.getWritableDatabase();
        return db;
    }
}
