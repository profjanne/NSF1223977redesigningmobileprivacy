package my.appforedata;

import java.util.ArrayList;
import java.util.List;

import my.appforedata.AppUsageTable.AppUsageColumns;
import my.applicationforeground.DataConstants;
import my.applicationforeground.GetSharePrefer;
import my.applicationforeground.NewForeService;
import my.applicationforeground.RecordTemplate;
import my.applicationforeground.ServiceDbHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * only used by setupgroupuserid class, this help to setup groupid userid not
 * save id to database so do not pass parameter
 */

public class AppendGroupUserID {
	private String appendStr;
	private SQLiteDatabase db;
	private SQLiteStatement insertStatement;
	Context context;
	String table;
	private static String userID = "0";
	private static String groupID = "0";
	private static int flag=0;

	public AppendGroupUserID() {
	}

	public AppendGroupUserID(Context context, SQLiteDatabase db, String table) {
		this.db = db;
		this.table = table;
		this.context = context;

	}

	/**
	 * save help append ID TO OTHER TABLE EXCEPT GROUPUSERID TABLE, GROUPUSERID
	 * TABLE DATA IS INSERTED BY SETUPGROUPUSER CLASS
	 */

	public void save() {
		// INSERT INTO this.table (DataConstants.GROUPIDDB,
		// DataConstants.USERIDDB) VALUES (userID,groupID);
		 if(this.groupID==null||this.userID==null||this.groupID.equals("0")||this.userID.equals("0")){
		 updateGroupIDFromDB();
//		 System.out.println("SAVE IN APPEND groupid:"+groupID+",userid:"+userID+",we need to update id");
		 }
		try {
			ContentValues cv = new ContentValues();
			cv.put(DataConstants.GROUPIDDB, this.groupID);
			cv.put(DataConstants.USERIDDB, this.userID);
//			System.out.println("table:" + this.table + ",GROUP:" + groupID
//					+ ",user:" + userID);
			// db.insert(this.table, null, cv);
			db.update(this.table, cv, BaseColumns._ID + " = (SELECT max("
					+ BaseColumns._ID + ") FROM " + this.table + ")", null);
			// EACH CLASS CALLING HTE METHOD HAS TO CLOSE DB THEMSELVE
			// db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// need to shield jan8 here
//		 Read();
	}

	// public GroupUserIdRecord geGroupUserIdRecord(){
	// GroupUserIdRecord record=null;
	// ServiceDbHelper helper=new ServiceDbHelper(this.context);
	// SQLiteDatabase db= helper.getWritableDatabase();
	//
	// return record;
	// }

	public void setAppendID(String group, String user) {
		synchronized (this) {
			this.groupID = group;
			this.userID = user;
		}
	}

	public void Read() {
		Cursor cur = db.rawQuery("select " + DataConstants.GROUPIDDB + " ,"
				+ DataConstants.USERIDDB + " from " + this.table, null);
		int i = 0;

		if (cur.moveToFirst()) {
			do {
				i++;

//				Log.i("LOG","i:" + String.valueOf(i) + ",table:" + this.table
//								+ ",groupid:" + cur.getString(0) + ",user:"
//								+ cur.getString(1));
				if (this.table.equals(DataConstants.INSTALLED_TABLE_NAME)) {
					break;
				}
			} while (cur.moveToNext());
		}
		if (cur!=null&&!cur.isClosed()) {
			cur.close();
		}
	}

	// public void ReadFirstRowID() {
	// int id = 1000;
	// Cursor cur = db.rawQuery("select " + BaseColumns._ID + " ,"
	// + DataConstants.GROUPIDDB + " ," + DataConstants.USERIDDB
	// + " from " + this.table + " ORDER BY  " + BaseColumns._ID
	// + " ASC LIMIT 1", null);
	// int i = 0;
	// if (cur.moveToFirst()) {
	// id = cur.getInt(0);
	// setAppendID(cur.getString(1), cur.getString(2));
	// }
	// Log.i("Log", "idfirst:" + String.valueOf(id) + ",table:" + this.table
	// + ",groupid:" + cur.getString(1) + ",user:" + cur.getString(2));
	// }
	//
	// public void ReadLastRowID() {
	// int id = 1000;
	// Cursor cur = db.rawQuery("select " + BaseColumns._ID + " ,"
	// + DataConstants.GROUPIDDB + " ," + DataConstants.USERIDDB
	// + " from " + this.table + " ORDER BY  " + BaseColumns._ID
	// + " DESC LIMIT 1", null);
	// int i = 0;
	// if (cur.moveToFirst()) {
	// id = cur.getInt(0);
	// setAppendID(cur.getString(1), cur.getString(2));
	// }
	// Log.i("Log", "idlastreadlastrowidtosetid:" + String.valueOf(id)
	// + ",table:" + this.table + ",groupid:" + cur.getString(1)
	// + ",user:" + cur.getString(2));
	// }

	/**
	 * get groupuserid from each table to see if the last row groupuser id
	 * 
	 * @return
	 */

	public GroupUserIdRecord getGroupIDFromDB() {
		int id = 999;
		GroupUserIdRecord record = null;
		Cursor cur = db.rawQuery("select " + BaseColumns._ID + " ,"
				+ DataConstants.GROUPIDDB + " ," + DataConstants.USERIDDB
				+ " from " + this.table + " ORDER BY  " + BaseColumns._ID
				+ " DESC LIMIT 1", null);
		int i = 0;
		if (cur.moveToFirst()) {
			id = cur.getInt(0);
//			Log.i("Log", "getgroupidfromdb:" + String.valueOf(id) + ",table:"
//					+ this.table + ",groupid:" + cur.getString(1) + ",user:"
//					+ cur.getString(2));
			record = new GroupUserIdRecord(cur.getString(1), cur.getString(2),
					"deviceId", "date");
			record.setId(id);
		}
		if (cur!=null&&!cur.isClosed()) {
			cur.close();
		}
//		System.out.println("userpid:"+record.getUserId()+",group:"+record.getGroupId()+",after update dbid:"+String.valueOf(id));
		return record;
	}

	/**
	 * set the static groupid userid via reading the table groupusertable
	 */
	public void updateGroupIDFromDB() {
		System.out.println("groupid:"+groupID+",userid:"+userID+",we need to update id");
		ServiceDbHelper helper = new ServiceDbHelper(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		int id = 999;
		GroupUserIdRecord record = null;
		//get the last row data
		Cursor cur = db.rawQuery("select " + BaseColumns._ID + " ,"
				+ DataConstants.GROUPIDDB + " ," + DataConstants.USERIDDB
				+ " from " + DataConstants.GROUPUSER_TABLE + " ORDER BY  "
				+ BaseColumns._ID + " DESC LIMIT 1", null);
		int i = 0;
		String group = null;
		String user = null;
		if (cur.moveToFirst()) {
			id = cur.getInt(0);
			group = cur.getString(1);
			user = cur.getString(2);
			if (group != null && user != null)
				setAppendID(group, user);
		}
		if (cur!=null&&!cur.isClosed()) {
			cur.close();
		}
		db.close();
		System.out.println("groupid:"+groupID+",userid:"+userID+",after update dbid:"+String.valueOf(id));
	}
	/**
	 * set the static groupid userid via reading table groupusertable
	 */
	public void updateGroupIDFromDB(Context context) {
		System.out.println("groupid:"+groupID+",userid:"+userID+",we need to update id");
		ServiceDbHelper helper = new ServiceDbHelper(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		int id = 999;
		GroupUserIdRecord record = null;
		Cursor cur = db.rawQuery("select " + BaseColumns._ID + " ,"
				+ DataConstants.GROUPIDDB + " ," + DataConstants.USERIDDB
				+ " from " + DataConstants.GROUPUSER_TABLE + " ORDER BY  "
				+ BaseColumns._ID + " DESC LIMIT 1", null);
		int i = 0;
		String group = null;
		String user = null;
		if (cur.moveToFirst()) {
			id = cur.getInt(0);
			group = cur.getString(1);
			user = cur.getString(2);
			if (group != null && user != null)
				setAppendID(group, user);
		}
		if (cur!=null&&!cur.isClosed()) {
			cur.close();
		}
		db.close();
		System.out.println("groupid:"+groupID+",userid:"+userID+",after update dbid:"+String.valueOf(id));
	}

	public String getStringForDBRead() {
		String group = null;
		GroupUserIdRecord record = getGroupIDFromDB();
		if (record != null) {
			group = "id:" + record.getId() + ",group:" + record.getGroupId()
					+ ",user:" + record.getUserId() + "\n";
		}
		return group;
	}
}
