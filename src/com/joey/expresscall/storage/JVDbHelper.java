package com.joey.expresscall.storage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.joey.general.utils.MyLog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * 数据库操作的单例，该数据库封装了包括账号、设备两张表 account、device
 * <dl>
 * 这两张表都只有两行，账号表的两列是 id和text 分别存储账号的用户名和序列化对象。
 * </dl>
 * <dl>
 * 设备表 的两列是id和text 分别存储云视通号和序列化对象。
 * </dl>
 * 两张表的主键都是id
 * 
 * @author Joey
 */
public class JVDbHelper {

	public final static String SQL_NAME = "joey.db";
	public static final int SQL_VERSIOIN = 4;
	public static final String ACCOUNT_TABLE = "account";
	public static final String FILE_TABLE = "file";
	public static final String GROUP_TABLE = "group";
	public static final String CALL_TABLE = "call";
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_DATA = "data";
	public static final String ACCOUNT_ID = "accountId";
	private static JVDbHelper dbHelper;
	private SQLiteHelper mSqlHelper;

	private JVDbHelper() {

	}

	public static JVDbHelper getInstance() {
		if (dbHelper != null)
			return dbHelper;
		synchronized (JVDbHelper.class) {
			if (dbHelper != null)
				return dbHelper;
			dbHelper = new JVDbHelper();
			return dbHelper;
		}
	}

	public void init(Context context) {
		MyLog.i("init");
		mSqlHelper = new SQLiteHelper(context, SQL_NAME, null, SQL_VERSIOIN);
	}

	/**
	 * 添加一个设备
	 * @param bean
	 * @param accountId
	 * @param table
	 * @return
	 */
	public boolean insert(BaseBean bean, String accountId,String table) {
		// 插入新的数据之前, 先干掉旧的数据
		delete(bean, accountId,table);
		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
		try {
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(
					arrayOutputStream);
			objectOutputStream.writeObject(bean);
			objectOutputStream.flush();
			byte data[] = arrayOutputStream.toByteArray();
			// String objStr = new String(data,"UTF-8");
			objectOutputStream.close();
			arrayOutputStream.close();
			SQLiteDatabase database = mSqlHelper.getWritableDatabase();
			MyLog.d("");
			database.execSQL(
					"insert into "+table+" (id,data,accountId) values(?,?,?)",
					new Object[] { bean.getId(), data, accountId });
			database.close();
			return true;
		} catch (Exception e) {
			MyLog.e("error = ", e.getMessage());

		}
		return false;
	}

	/**
	 * 删除一个对象
	 * @param serial
	 * @param accountId
	 * @param table
	 * @return
	 */
	public boolean delete(BaseBean serial, String accountId,String table) {
		SQLiteDatabase db = mSqlHelper.getWritableDatabase();
		try {
			String sqlStr = String.format(
					"delete from "+table+" where id='%s' and accountId='%s'",
					serial.getId(), accountId);
			MyLog.i("delete sqlStr = " + sqlStr);
			db.execSQL(sqlStr);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			MyLog.e("error = ", e.getMessage());
		}
		return false;
	}

	/**
	 * 更新某个对象
	 * @param bean
	 * @param accountId
	 * @param table
	 * @return
	 */
	public boolean update(BaseBean bean, String accountId,String table) {
		return insert(bean, accountId,table);
	}

	/**
	 * 删除某张表
	 * @param accountId
	 * @param table
	 * @return
	 */
	public boolean clear(String accountId,String table) {
		SQLiteDatabase db = mSqlHelper.getWritableDatabase();
		try {
			db.execSQL("delete from "+table+" where accountId='" + accountId + "'");
			return true;
		} catch (Exception e) {
			MyLog.e("error = ", e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 添加一个账号
	 * 
	 * @param account
	 * @return
	 */
	public boolean insertAccount(JVAccount account) {
		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
		try {
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(
					arrayOutputStream);
			objectOutputStream.writeObject(account);
			objectOutputStream.flush();
			byte data[] = arrayOutputStream.toByteArray();
			// String objStr = new String(data,"UTF-8");
			objectOutputStream.close();
			arrayOutputStream.close();
			SQLiteDatabase database = mSqlHelper.getWritableDatabase();
			MyLog.d("");
			database.execSQL("insert into account (id,data) values(?,?)",
					new Object[] { account.getUsername(), data });
			// MyLog.d("result = "+result);
			database.close();
			return true;
			// return result;
		} catch (Exception e) {
			MyLog.e("error = ", e.getMessage());

		}
		return false;
	}

	/**
	 * 删除一个账号
	 * 
	 * @param account
	 * @return
	 */
	public boolean deleteAccount(JVAccount account) {
		SQLiteDatabase db = mSqlHelper.getWritableDatabase();
		try {
			db.execSQL("delete from account where id='" + account.getUsername()
					+ "'");
			return true;
		} catch (Exception e) {
			MyLog.e("error = ", e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 更新某个账号
	 * 
	 * @param account
	 * @return
	 */
	public boolean updateAccount(JVAccount account) {
		return insertAccount(account);
	}

	/**
	 * 从数据看表table中获取columnKey这一列的序列化数据
	 * 
	 * @param table
	 *            数据库表
	 * @param columnKey
	 *            列表在数据库存储的key
	 * @param conditions
	 *            查询条件
	 * @return
	 */
	public Serializable[] getList(String table, String columnKey,
			String... conditions) {
		Serializable list[];
		SQLiteDatabase database = mSqlHelper.getReadableDatabase();
		String sqlStr = String.format("select * from %s", table);
		if (conditions != null && conditions.length > 0) {
			sqlStr = String.format("select * from %s where %s='%s'", table,
					conditions[0], conditions[1]);
			MyLog.i("conditions != null" + sqlStr);
		}
		Cursor cursor = database.rawQuery(sqlStr, null);
		if (cursor != null) {
			MyLog.i("cursor length is" + cursor.getCount() + "position = "
					+ cursor.getPosition());
			list = new Serializable[cursor.getCount()];
			int i = 0;
			while (cursor.moveToNext()) {
				if (cursor.getColumnIndex("accountId") > 0) {
					String account = cursor.getString(cursor
							.getColumnIndex("accountId"));
					MyLog.i("accountId = " + account);
				}
				byte data[] = cursor.getBlob(cursor.getColumnIndex(columnKey));
				ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(
						data);
				try {
					ObjectInputStream inputStream = new ObjectInputStream(
							arrayInputStream);
					Serializable object = (Serializable) inputStream
							.readObject();
					list[i] = object;
					MyLog.fmt("object [%d] %s", i, object.toString());
					i++;
					MyLog.i("i =" + i);
					inputStream.close();
					arrayInputStream.close();
				} catch (Exception e) {
					MyLog.e("error = ", e.getMessage());
					e.printStackTrace();
				}
			}
			return list;
		}
		return null;

	}

	/**
	 * 从数据看表table中获取columnKey这一列的第index行的序列化数据
	 * 
	 * @param table
	 *            数据库表格
	 * @param columnKey
	 *            数据表格列key
	 * @param row
	 *            行数
	 * @return
	 */
	public Serializable getObject(String table, String columnKey,
			String... conditions) {
		Serializable object = null;
		SQLiteDatabase database = mSqlHelper.getReadableDatabase();
		String sqlStr = String.format("select * from %s ", table);
		if (conditions != null && conditions.length > 0) {
			if (conditions.length % 2 == 0) {
				sqlStr += "where";
				for (int i = 0; i < conditions.length; i = i + 2) {
					sqlStr = String.format(sqlStr + " %s='%s' and ",
							conditions[i], conditions[i + 1]);
				}
				sqlStr = sqlStr.substring(0, sqlStr.length() - " and".length());
			}
		}
		MyLog.i("sqlStr = " + sqlStr);
		Cursor cursor = database.rawQuery(sqlStr, null);
		if (cursor.moveToFirst()) {
			byte data[] = cursor.getBlob(cursor.getColumnIndex(columnKey));
			ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(
					data);
			try {
				ObjectInputStream inputStream = new ObjectInputStream(
						arrayInputStream);
				object = (Serializable) inputStream.readObject();
				MyLog.fmt("object %s", object.toString());
				inputStream.close();
				arrayInputStream.close();
			} catch (Exception e) {
				MyLog.e("error = ", e.getMessage());
				e.printStackTrace();
			}

			return object;
		}
		return null;
	}

	private class SQLiteHelper extends SQLiteOpenHelper {

		public static final String CREATE_ACCOUNT_TABLE = "create table if not exists account ("
				+ "id text primary key, " + "data text)";

		public final static String CREATE_DEVICE_TABLE = "create table if not exists file ("
				+ "keyid integer primary key autoincrement,"
				+ "id text,"
				+ "data text, accountId text, foreign key(accountId) references  account(id) on delete cascade on update cascade)";

		public final static String CREATE_CALL_TABLE = "create table if not exists call ("
				+ "keyid integer primary key autoincrement,"
				+ "id text,"
				+ "data text, accountId text, foreign key(accountId) references  account(id) on delete cascade on update cascade)";

		public final static String CREATE_GROUP_TABLE = "create table if not exists group ("
				+ "keyid integer primary key autoincrement,"
				+ "id text,"
				+ "data text, accountId text, foreign key(accountId) references  account(id) on delete cascade on update cascade)";

		public SQLiteHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			MyLog.i("lala", "onCreate");
			db.execSQL(CREATE_ACCOUNT_TABLE);
			db.execSQL(CREATE_DEVICE_TABLE);
			db.execSQL(CREATE_CALL_TABLE);
			db.execSQL(CREATE_GROUP_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("drop table if exists account");
			db.execSQL("drop table if exists file");
			db.execSQL("drop table if exists group");
			db.execSQL("drop table if exists call");

			onCreate(db);
		}

		@Override
		public SQLiteDatabase getWritableDatabase() {
			return super.getWritableDatabase();
		}

		@Override
		public void onOpen(SQLiteDatabase db) {
			super.onOpen(db);
			MyLog.i("onOpen");
			if (!db.isReadOnly()) {
				// Enable foreign key constraints
				db.execSQL("PRAGMA foreign_keys=ON;");
			}
		}

	}

}
