package com.app.isobenaoya.googlemapsapplication01;

/**
 * Created by IsobeNaoya on 2015/07/09.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper{

    static final String DATABASE_NAME = "myDatabase.db";
    static final int DATABASE_VERSION = 1;
    static final String TABLE_NAME = "myData";
    static final String ID = "_id";
    //�Z��
    static final String REG_ADDR = "regAddr";
    //�R���r�j�̎��
    static final String REG_SHOP = "regShop";
    //�o�^��
    static final String REG_NAME = "regName";
    //�o�^��
    static final String REG_TIME = "regTime";
    //��������
    static final String REG_SHOPNUM = "regShopNum";

    public MyDBHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        String query = "create table " + TABLE_NAME + "(" +
                ID + " INTEGER PRIMARY KEY autoincrement," +
                REG_ADDR + " TEXT,"+
                REG_SHOP + " TEXT,"+
                REG_NAME + " TEXT,"+
                REG_TIME + " TEXT,"+
                REG_SHOPNUM + " TEXT);";
        db.execSQL(query);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion,int newVersion){
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }

    //id�����Ƀf�[�^���폜
    public void deleteToDB(SQLiteDatabase db,String id){
        db.execSQL("delete from "+TABLE_NAME
                + " where "
                + ID + " = '" + id + "'");
    }
}
