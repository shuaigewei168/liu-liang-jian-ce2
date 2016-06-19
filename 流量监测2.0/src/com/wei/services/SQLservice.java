package com.wei.services;

/**
 * 数据库增删改查
 */
import java.util.List;

import com.wei.javabean.ContentLiuliang;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SQLservice {

	private DbOpenHelper helper;

	public SQLservice(Context context) {
		super();
		this.helper = new DbOpenHelper(context);
	}

	/**
	 * 添加应用uid,名称，发送和接受流量
	 */
	public void save(ContentLiuliang liuliang)//
	{
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL(
				"insert into totalll(uid,name,sentbyte,receivebyte,lasttx,lastrx) values(?,?,?,?,?,?)",
				new Object[] { liuliang.getUid(), liuliang.getAppname(),
						liuliang.getSentbyte(), liuliang.getReceivebyte(),
						liuliang.getLasttx(), liuliang.getLastrx() });
		db.close();
	}

	/**
	 * 删除
	 */
	public void delete(Integer id) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("delete from totalll where personid=?", new Object[] { id });
		db.close();
	}

	/**
	 * 更新应用uid,名称，发送和接受流量
	 */
	public void updata(ContentLiuliang liuliang) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL(
				"update totalll set name=?,sentbyte=?,receivebyte=?,lasttx=?,lastrx=? where uid=?",
				new Object[] { liuliang.getAppname(), liuliang.getSentbyte(),
						liuliang.getReceivebyte(), liuliang.getLasttx(),
						liuliang.getLastrx(), liuliang.getUid() });
		db.close();
	}

	// public Person find(Integer id)
	// {
	// SQLiteDatabase db=helper.getReadableDatabase();
	// Cursor cursor=db.rawQuery("select * from person where personid=?",
	// new String[]{id.toString()});
	// if(cursor.moveToFirst())
	// {
	// int personid=cursor.getInt(cursor.getColumnIndex("personid"));
	// String name=cursor.getString(cursor.getColumnIndex("name"));
	// String phone=cursor.getString(cursor.getColumnIndex("phone"));
	// return new Person(name,phone,personid);
	// }
	// cursor.close();
	// return null;
	// }

	/**
	 * 查找全部元素
	 */
	public ContentLiuliang find(int id) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from totalll where uid=?",
				new String[] { String.valueOf(id) });

		if (cursor.moveToFirst()) {
			String uid = cursor.getString(1);
			String appname = cursor.getString(2);
			long sentbyte = cursor.getLong(3);
			long receivebyte = cursor.getLong(4);
			long lasttx = cursor.getLong(5);
			long lastrx = cursor.getLong(6);
			return new ContentLiuliang(uid, appname, sentbyte, receivebyte,
					lasttx, lastrx);
		}
		cursor.close();
		db.close();
		return null;
	}
	


	/**
	 * 统计
	 */
	public int count() {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select count (*) from totalll", null);
		cursor.moveToFirst();
		int result = cursor.getInt(0);
		cursor.close();
		db.close();
		return result;

	}

	public void closedb() {
		SQLiteDatabase db = helper.getReadableDatabase();
		db.close();
	}

}
