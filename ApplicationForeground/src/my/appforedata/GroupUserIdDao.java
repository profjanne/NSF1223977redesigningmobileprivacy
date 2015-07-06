package my.appforedata;

import java.util.ArrayList;
import java.util.List;

import my.appforedata.GroupUserIdTable.GroupUserColumns;
import my.applicationforeground.DataConstants;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;
import android.util.Log;

public class GroupUserIdDao {
	private static final String INSERT = "insert into "
			+ DataConstants.GROUPUSER_TABLE + "(" + BaseColumns._ID + ", "
			+ GroupUserColumns.GROUP + ", " + GroupUserColumns.USER + ", "
			+ GroupUserColumns.DEVICE + ", "
			+GroupUserColumns.DATE +") values (null,?, ?, ?,?)";

	private SQLiteDatabase db;
	private SQLiteStatement insertStatement;

	public GroupUserIdDao(SQLiteDatabase db) {
		this.db = db;
		insertStatement = db.compileStatement(GroupUserIdDao.INSERT);
	}

	public long save(GroupUserIdRecord entity) {
		insertStatement.clearBindings();
		insertStatement.bindString(1, entity.getGroupId());
		insertStatement.bindString(2, entity.getUserId());
		insertStatement.bindString(3, entity.getDeviceId());
		insertStatement.bindString(4, entity.getDate());
		long result=insertStatement.executeInsert();
		Log.i("check","wirte id to db:"+String.valueOf(result));
		return result;

	}

	public GroupUserIdRecord get(long id) {
		GroupUserIdRecord GroupUserIdRecord = null;
		Cursor c = db.query(DataConstants.GROUPUSER_TABLE, new String[] {
				BaseColumns._ID, GroupUserColumns.GROUP, GroupUserColumns.USER,
				GroupUserColumns.DEVICE, GroupUserColumns.DATE}, BaseColumns._ID + " = ?",
				new String[] { String.valueOf(id) }, null, null, null, "1");
		if (c.moveToFirst()) {
			GroupUserIdRecord = this.buildGroupUserIdRecordFromCursor(c);
		}
		if (!c.isClosed()) {
			c.close();
		}
		return GroupUserIdRecord;
	}

	public GroupUserIdRecord buildGroupUserIdRecordFromCursor(Cursor c) {
		GroupUserIdRecord GroupUserIdRecord = null;
		if (c != null) {
			GroupUserIdRecord = new GroupUserIdRecord();
			GroupUserIdRecord.setId(c.getLong(0));
			GroupUserIdRecord.setGroupId(c.getString(1));
			GroupUserIdRecord.setUserId(c.getString(2));
			GroupUserIdRecord.setDeviceId(c.getString(3));
			GroupUserIdRecord.setDate(c.getString(4));
		}
		return GroupUserIdRecord;
	}

	public List<GroupUserIdRecord> getAll() {
		List<GroupUserIdRecord> list = new ArrayList<GroupUserIdRecord>();
		Cursor c = db.query(DataConstants.GROUPUSER_TABLE, new String[] {
				BaseColumns._ID, GroupUserColumns.GROUP, GroupUserColumns.USER,
				GroupUserColumns.DEVICE, GroupUserColumns.DATE }, null, null, null, null,
				BaseColumns._ID, null);
		if (c.moveToFirst()) {
			do {
				GroupUserIdRecord GroupUserIdRecord = this
						.buildGroupUserIdRecordFromCursor(c);
				if (GroupUserIdRecord != null) {
					list.add(GroupUserIdRecord);
				}
			} while (c.moveToNext());
		}
		if (!c.isClosed()) {
			c.close();
		}
		return list;
	}
	
	public GroupUserIdRecord getLastRow() {
		int id = 999;
		GroupUserIdRecord record = null;
		Cursor cur = db.rawQuery("select " + BaseColumns._ID + " ,"
				+ DataConstants.GROUPIDDB + " ," + DataConstants.USERIDDB
				+ " from " + DataConstants.GROUPUSER_TABLE + " ORDER BY  " + BaseColumns._ID
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
		if (!cur.isClosed()) {
			cur.close();
		}
//		System.out.println("userpid:"+record.getUserId()+",group:"+record.getGroupId()+",after update dbid:"+String.valueOf(id));
		return record;
	}

//	public boolean exist() {
//		String sql = "select count(*) as c from " + DataConstants.DBMASTER
//				+ " where type ='table' and name ='"
//				+ DataConstants.GROUPUSER_TABLE.trim() + "' ";
//		Cursor cur = db
//				.rawQuery(
//						"SELECT * FROM sqlite_master WHERE name = 'DataConstants.GROUPUSER_TABLE' and type='table'",
//						null);
//		Log.i("check",
//				"check exist or notcur==null:" + String.valueOf(cur == null));
//		if (cur != null)
//			Log.i("check",
//					",curmovetofirst:" + String.valueOf(cur.moveToFirst()));
//		if (cur == null) {
//			return false;
//		}
//		return true;
//	}
	  
	public void update(GroupUserIdRecord type) {
		// TODO Auto-generated method stub

	}

	public void delete(GroupUserIdRecord type) {
		// TODO Auto-generated method stub

	}

	public void deleteAll(SQLiteDatabase db) {
		db.delete(DataConstants.GROUPUSER_TABLE, null, null);
	}
}
