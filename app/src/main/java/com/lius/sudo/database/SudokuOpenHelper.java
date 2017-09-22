package com.lius.sudo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by UsielLau on 2017/9/21 0021 19:26.
 */

public class SudokuOpenHelper extends SQLiteOpenHelper {

    private static final String CREATE_ARCHIVE_TABLE="create table Archive(" +
            "id integer primary key autoincrement," +
            "archive_time text," +
            "game_data BLOB)";
    private static final String CREATE_RANK_TABLE="create table Rank(" +
            "id integer primary key autoincrement," +
            "player_name text," +
            "level int," +
            "game_time text," +
            "game_seconds_time integer," +
            "play_time text)";


    public SudokuOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ARCHIVE_TABLE);
        db.execSQL(CREATE_RANK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
