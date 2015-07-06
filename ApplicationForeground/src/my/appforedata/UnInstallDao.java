package my.appforedata;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;
import android.util.Log;
import my.appforedata.UnInstallTable.UnInstallColumns;
import my.applicationforeground.RecordTemplate;
import my.applicationforeground.DataConstants;

public class UnInstallDao implements DaoTemplate<UnInstallRecord> {
	private static final String INSERT = "insert into "
			+ DataConstants.UN_INSTALL_TABLE_NAME + "(" + BaseColumns._ID
			+ ", " + UnInstallColumns.PKGNAME + ", " + UnInstallColumns.APPNAME
			+ ", " + UnInstallColumns.DATE + ", "
			+ UnInstallColumns.MILISECONDS + ", " + UnInstallColumns.LOGITUDE
			+ ", " + UnInstallColumns.LATITUDE + ", "
			+ UnInstallColumns.OPERATION + ") values (null,?, ?, ?,?, ?, ?,?)";

	private SQLiteDatabase db;
	private SQLiteStatement insertStatement;

	public UnInstallDao(SQLiteDatabase db) {
		this.db = db;
		insertStatement = db.compileStatement(UnInstallDao.INSERT);
	}

	public long save(UnInstallRecord entity) {
		insertStatement.clearBindings();
		// Log.i("index",String.valueOf(entity.getOpenTime()));
		insertStatement.bindString(1, entity.getPkgName());
		insertStatement.bindString(2, entity.getAppName());
		insertStatement.bindString(3, entity.getDate());
		insertStatement.bindLong(4, entity.getMiliSeconds());
		// OCT 23
		insertStatement.bindString(5, String.valueOf(entity.getLogitude()));
		insertStatement.bindString(6, String.valueOf(entity.getLatitude()));
		// insertStatement.bindDouble(5, entity.getLogitude());
		// insertStatement.bindDouble(6, entity.getLatitude());
		insertStatement.bindString(7, entity.getOperation());
		return insertStatement.executeInsert();

	}

	public UnInstallRecord get(long id) {
		UnInstallRecord unInstallRecord = null;
		Cursor c = db.query(DataConstants.UN_INSTALL_TABLE_NAME, new String[] {
				BaseColumns._ID, UnInstallColumns.PKGNAME,
				UnInstallColumns.APPNAME, UnInstallColumns.DATE,
				UnInstallColumns.MILISECONDS, UnInstallColumns.LOGITUDE,
				UnInstallColumns.LATITUDE, UnInstallColumns.OPERATION },
				BaseColumns._ID + " = ?", new String[] { String.valueOf(id) },
				null, null, null, "1");
		if (c.moveToFirst()) {
			unInstallRecord = this.buildUnInstallRecordFromCursor(c);
		}
		if (!c.isClosed()) {
			c.close();
		}
		return unInstallRecord;
	}

	public UnInstallRecord buildUnInstallRecordFromCursor(Cursor c) {
		UnInstallRecord unInstallRecord = null;
		if (c != null) {
			unInstallRecord = new UnInstallRecord();
			unInstallRecord.setId(c.getLong(0));
			unInstallRecord.setPkgName(c.getString(1));
			unInstallRecord.setAppName(c.getString(2));
			unInstallRecord.setDate(c.getString(3));
			unInstallRecord.setMiliSeconds(c.getLong(4));
			unInstallRecord.setLogitude(Double.parseDouble(c.getString(5)));
			unInstallRecord.setLatitude(Double.parseDouble(c.getString(6)));
			// unInstallRecord.setLogitude(c.getDouble(5));
			// unInstallRecord.setLatitude(c.getDouble(6));
			unInstallRecord.setOperation(c.getString(7));
		}
		return unInstallRecord;
	}

	public List<UnInstallRecord> getAll() {
		List<UnInstallRecord> list = new ArrayList<UnInstallRecord>();
		Cursor c = db.query(DataConstants.UN_INSTALL_TABLE_NAME, new String[] {
				BaseColumns._ID, UnInstallColumns.PKGNAME,
				UnInstallColumns.APPNAME, UnInstallColumns.DATE,
				UnInstallColumns.MILISECONDS, UnInstallColumns.LOGITUDE,
				UnInstallColumns.LATITUDE, UnInstallColumns.OPERATION }, null,
				null, null, null, BaseColumns._ID, null);
		if (c.moveToFirst()) {
			do {
				UnInstallRecord unInstallRecord = this
						.buildUnInstallRecordFromCursor(c);
				if (unInstallRecord != null) {
					list.add(unInstallRecord);
				}
			} while (c.moveToNext());
		}
		if (!c.isClosed()) {
			c.close();
		}
		return list;
	}

	public List<UnInstallRecord> getAll(long flag) {
		List<UnInstallRecord> list = new ArrayList<UnInstallRecord>();
//		System.out.println("uninstallflag:" + flag);
		Cursor c = db.query(DataConstants.UN_INSTALL_TABLE_NAME, new String[] {
				BaseColumns._ID, UnInstallColumns.PKGNAME,
				UnInstallColumns.APPNAME, UnInstallColumns.DATE,
				UnInstallColumns.MILISECONDS, UnInstallColumns.LOGITUDE,
				UnInstallColumns.LATITUDE, UnInstallColumns.OPERATION },
				BaseColumns._ID + "> ?", new String[] { String.valueOf(flag) },
				null, null, BaseColumns._ID, null);
		if (c.moveToFirst()) {
			do {
				UnInstallRecord unInstallRecord = this
						.buildUnInstallRecordFromCursor(c);
				if (unInstallRecord != null) {
					list.add(unInstallRecord);
				}
			} while (c.moveToNext());
		}
		if (!c.isClosed()) {
			c.close();
		}
		return list;
	}

	public void update(UnInstallRecord type) {
		// TODO Auto-generated method stub

	}

	public void delete(UnInstallRecord type) {
		// TODO Auto-generated method stub

	}

	public void deleteAll(SQLiteDatabase db) {
		db.delete(DataConstants.UN_INSTALL_TABLE_NAME, null, null);
	}

}
