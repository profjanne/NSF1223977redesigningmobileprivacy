package my.applicationforeground;

import java.util.ArrayList;

import my.appforedata.CheckTableDB;
import my.appforedata.GroupUserIdDao;
import my.appforedata.GroupUserIdRecord;
import my.appforedata.GroupUserIdTable;
import android.R.bool;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class InitGroupUserID {
	Context context;
	private static boolean appendFlag = false;

	public InitGroupUserID() {
	}

	public InitGroupUserID(Context context) {
		this.context = context;
	}

	public void setInitFlag(boolean flag) {
		this.appendFlag = flag;
	}

	public boolean getInitFlag() {
		return this.appendFlag;
	}

	/**
	 * parameter is 1 creat talbe and setupid parameter is 2
	 * dataconstants.INITAPPENDNEWID table exist but we need to append a new id
	 * 
	 * @param append
	 * @return
	 */
	public boolean initialID() {
		ServiceDbHelper dbHelper = new ServiceDbHelper(this.context);
		boolean result = false;
		// get the db connection
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		// table not exist then initial the userid and groupid
		//for new version groupusertable creat when install open the app
		//so not need to check the groupuser table now
//		if (!groupUserIDtableExist(db)) {
//			//table not exist so create table first
//			GroupUserIdTable.onCreate(db);
//			result = true;
//		}
		// table exists get the appendflag to see if reset id namely append new id
		if (appendFlag) {
			appendFlag = false;
			result = true;
		}
		// table exists but, db record zero so setup id
		else {
			GroupUserIdRecord record = new GroupUserIdRecord();
			GroupUserIdDao dao = new GroupUserIdDao(db);
			ArrayList<GroupUserIdRecord> list = new ArrayList<GroupUserIdRecord>();
			list = (ArrayList<GroupUserIdRecord>) dao.getAll();

			System.out.println("listsize:" + list.size());
			if (list != null && list.size() < 1) {
				result = true;
				Log.i("check","idtable exst but none record");
			}
		}
		// table exists but not content initial userid groupid also
		// else{
		// GroupUserIdRecord record = new GroupUserIdRecord();
		// GroupUserIdDao dao = new GroupUserIdDao(db);
		// ArrayList<GroupUserIdRecord> list = new
		// ArrayList<GroupUserIdRecord>();
		// list = (ArrayList<GroupUserIdRecord>) dao.getAll();
		//
		// System.out.println("listsize:" + list.size());
		// if (list != null && list.size() < 1) {
		// startActivity(new Intent(getApplicationContext(),
		// SetUpGroupUser.class));
		// Log.i("check","list is null:"+String.valueOf(list.size()));
		// }
		//
		// }
		// GroupUserIdTable.onCreate(db);
		// GroupUserIdRecord record = new GroupUserIdRecord();
		// GroupUserIdDao dao = new GroupUserIdDao(db);
		// ArrayList<GroupUserIdRecord> list = new
		// ArrayList<GroupUserIdRecord>();
		// list = (ArrayList<GroupUserIdRecord>) dao.getAll();
		//
		// System.out.println("listsize:" + list.size());
		// if (list != null && list.size() < 1)
		// {
		// startActivity(new Intent(getApplicationContext(),
		// SetUpGroupUser.class));
		// }

		db.close();
		return result;
	}

	public boolean groupUserIDtableExist(SQLiteDatabase db) {
		return new CheckTableDB(db).tableIsExist(DataConstants.GROUPUSER_TABLE);
	}

}
