package com.wei.services;

/**
 * 创建数据库，包括数据库名字，版本，以及表单
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbOpenHelper extends SQLiteOpenHelper {

	public DbOpenHelper(Context context) {
		super(context, "liuliang1.db", null, 1);
		// 数据库名字，默认系统返回光标，版本为1大于0

	}

	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table totalll(personid integer primary key autoincrement,uid varchar(26),name varchar(26),sentbyte varchar(26),receivebyte varchar(26),lasttx varchar(26),lastrx varchar(26))");
		// 生成数据表格person
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
