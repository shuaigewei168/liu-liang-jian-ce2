package com.wei.services;

/**
 * �������ݿ⣬�������ݿ����֣��汾���Լ���
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbOpenHelper extends SQLiteOpenHelper {

	public DbOpenHelper(Context context) {
		super(context, "liuliang1.db", null, 1);
		// ���ݿ����֣�Ĭ��ϵͳ���ع�꣬�汾Ϊ1����0

	}

	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table totalll(personid integer primary key autoincrement,uid varchar(26),name varchar(26),sentbyte varchar(26),receivebyte varchar(26),lasttx varchar(26),lastrx varchar(26))");
		// �������ݱ��person
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
