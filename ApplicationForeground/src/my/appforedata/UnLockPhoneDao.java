package my.appforedata;

import java.util.ArrayList;
import java.util.List;

import my.appforedata.UnLockPhoneTable.UnLockPhoneColumns;
import my.applicationforeground.DataConstants;

import android.R.string;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;
import android.util.Log;

public class UnLockPhoneDao implements DaoTemplate<UnLockPhoneRecord> {
	private static final String INSERT = "insert into "
			+ DataConstants.UN_LOCKPHONE_TABLE_NAME + "(" + BaseColumns._ID
			+ "," + UnLockPhoneColumns.UNLOCKDATE + ", "
			+ UnLockPhoneColumns.LOCKDATE + ", "
			+ UnLockPhoneColumns.INTERVALDATE + ", "
			+ UnLockPhoneColumns.UNLOCKMILISECONDS + ", "
			+ UnLockPhoneColumns.LOCKMILISECONDS + ", "
			+ UnLockPhoneColumns.INTERVALMILISECONDS
			+ ")values(null,?,?,?,?,?,?)";
	private SQLiteDatabase db;
	private SQLiteStatement insertStatement;

	public UnLockPhoneDao(SQLiteDatabase db) {
		this.db = db;
		// no two threads can use the same SQLiteStatement at the same time!
		insertStatement = db.compileStatement(UnLockPhoneDao.INSERT);
	}

	public long save(UnLockPhoneRecord entity) {
		//nov21
//		Log.i("unlockdao","save");
		insertStatement.clearBindings();
		insertStatement.bindString(1, entity.getUnlockDate());
		insertStatement.bindString(2, entity.getLockDate());
		insertStatement.bindString(3, entity.getIntervalDate());
		insertStatement.bindLong(4, entity.getUnlockMiliSeconds());
		insertStatement.bindLong(5, entity.getLockMiliSeconds());
		insertStatement.bindLong(6, entity.getIntervalMiliSeconds());
		return insertStatement.executeInsert();
	}

	public UnLockPhoneRecord buildRecordFromCursor(Cursor c) {
		UnLockPhoneRecord unLockRecord = null;
		if (c != null) {
			unLockRecord = new UnLockPhoneRecord();
			unLockRecord.setId(c.getLong(0));
			unLockRecord.setUnlockDate(c.getString(1));
			unLockRecord.setLockDate(c.getString(2));
			unLockRecord.setIntervalDate(c.getString(3));
			unLockRecord.setUnlockMiliSeconds(c.getLong(4));
			unLockRecord.setLockMiliSeconds(c.getLong(5));
			unLockRecord.setIntervalMiliSeconds(c.getLong(6));
		}
		return unLockRecord;
	}

	public UnLockPhoneRecord get(long id) {
		UnLockPhoneRecord unLockPhoneRecord;
		Cursor c = db.query(DataConstants.UN_LOCKPHONE_TABLE_NAME,
				new String[] {BaseColumns._ID, UnLockPhoneColumns.UNLOCKDATE,
				UnLockPhoneColumns.LOCKDATE, UnLockPhoneColumns.INTERVALDATE,
				UnLockPhoneColumns.UNLOCKMILISECONDS, UnLockPhoneColumns.LOCKMILISECONDS,
				UnLockPhoneColumns.INTERVALMILISECONDS},
				BaseColumns._ID+"=?", new String[] {String.valueOf(id)}, 
				null, null, null,"1");
		unLockPhoneRecord = this.buildRecordFromCursor(c);
		return unLockPhoneRecord;
	}

	public List<UnLockPhoneRecord> getAll() {
		//nov21
//		Log.i("createtable","unlockphone getall");
		List<UnLockPhoneRecord> list = new ArrayList<UnLockPhoneRecord>();
		Cursor c = db.query(DataConstants.UN_LOCKPHONE_TABLE_NAME,
				new String[] { BaseColumns._ID, UnLockPhoneColumns.UNLOCKDATE,
						UnLockPhoneColumns.LOCKDATE,
						UnLockPhoneColumns.INTERVALDATE,
						UnLockPhoneColumns.UNLOCKMILISECONDS,
						UnLockPhoneColumns.LOCKMILISECONDS,
						UnLockPhoneColumns.INTERVALMILISECONDS }, null,null,
				null, null, BaseColumns._ID, null);
		if(c.moveToFirst())
		{
			do{
				UnLockPhoneRecord unLockPhoneRecord = this.buildRecordFromCursor(c);
				if(unLockPhoneRecord!=null)
				{
					list.add(unLockPhoneRecord);
				}
			}while(c.moveToNext());
		}
		if(!c.isClosed()){
			c.close();
		}
		return list;
	}

	public List<UnLockPhoneRecord> getAll(long flag) {
		//nov21
//		Log.i("createtable","unlockphone getall");
//		System.out.println("unlock flag:"+flag);
		List<UnLockPhoneRecord> list = new ArrayList<UnLockPhoneRecord>();
		Cursor c = db.query(DataConstants.UN_LOCKPHONE_TABLE_NAME,
				new String[] { BaseColumns._ID, UnLockPhoneColumns.UNLOCKDATE,
						UnLockPhoneColumns.LOCKDATE,
						UnLockPhoneColumns.INTERVALDATE,
						UnLockPhoneColumns.UNLOCKMILISECONDS,
						UnLockPhoneColumns.LOCKMILISECONDS,
						UnLockPhoneColumns.INTERVALMILISECONDS }, BaseColumns._ID+">?", new String[]{String.valueOf(flag)},
				null, null, BaseColumns._ID, null);
		if(c.moveToFirst())
		{
			do{
				UnLockPhoneRecord unLockPhoneRecord = this.buildRecordFromCursor(c);
				if(unLockPhoneRecord!=null)
				{
					list.add(unLockPhoneRecord);
				}
			}while(c.moveToNext());
		}
		if(!c.isClosed()){
			c.close();
		}
		return list;
	}

	public void update(UnLockPhoneRecord type) {
		// TODO Auto-generated method stub

	}

	public void delete(UnLockPhoneRecord type) {
		// TODO Auto-generated method stub

	}

}
