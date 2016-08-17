package com.lius.sudo.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 刘有泽 on 2016/8/9.
 */
public class SudoOpenHelper extends SQLiteOpenHelper{

    //Sudoku表建表语句
    public static final String CREATE_SUDOKU="create table Sudoku("
            +"id integer primary key autoincrement,"
            +"player text,"
            +"level text,"
            +"archivetime text,"
            +"consumetime integer,"
            +"number text,"
            +"color text)";
    //Rank表建表语句
    public static final String CREATE_RANK="create table Rank("
            +"id integer primary key autoincrement,"
            +"player text,"
            +"level text,"
            +"consumestrtime text,"
            +"consumeinttime integer)";
    public SudoOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SUDOKU);//创建Sudoku表
        db.execSQL(CREATE_RANK);//创建Rank表
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
