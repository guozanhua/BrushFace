package com.wanding.face.jiabo.device.sqlite;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 本地数据库存储蓝牙设备
 */
public class RecordSQLiteOpenHelper extends SQLiteOpenHelper {
	
	private static final String name="bluetoothAdmin.db";
	private static final Integer version=1;//只能增加不能减小

	public RecordSQLiteOpenHelper(Context context) {
		super(context, name, null, version);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 创建表
	 * */
	@Override
	public void onCreate(SQLiteDatabase db) {
		//方式一
//		db.execSQL("create table bluetoothrecords(id integer primary key autoincrement,name varchar(200),address varchar(200),ischeck varchar(100))");
		//方式二
//		String sql1="create table bluetoothrecords(" +
//				" id Integer primary key autoincrement," +
//				" name varchar(200)," +
//				" address varchar(200)," +
//				" ischeck varchar(100)," +
//				" typeid int references types(typeid)," +
//				" photo varchar(50)" +
//				")";
		//方式二
		String sql2="create table bluetoothrecords(" +
				" id Integer primary key autoincrement," +
				" name varchar(200)," +
				" address varchar(200)," +
				" ischeck varchar(100)" +
				")";
		
		try {
			//执行
//			db.execSQL(sql1);
			db.execSQL(sql2);
			Log.e("执行创建表：", "建表成功！");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("执行创建表：", "建表失败！");
		}
		
	}

	/** 该方法在版本号version值改变时执行（一般用于重建表时使用）  */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//先移除表（如果有子表关联时，先移除子表，再移除主表）
		db.execSQL("drop table bluetoothrecords");
		//在执行建表方法
		onCreate(db);
	}

}
