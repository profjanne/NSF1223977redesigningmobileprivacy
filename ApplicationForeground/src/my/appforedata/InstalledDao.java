package my.appforedata;

import java.util.ArrayList;
import java.util.List;

import my.appforedata.InstalledTable.InstalledColumns;
import my.applicationforeground.DataConstants;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;
import android.util.Log;

public class InstalledDao implements DaoTemplate<InstalledRecord> {
	private static final String INSERT = "insert into "
			+ DataConstants.INSTALLED_TABLE_NAME + "(" + BaseColumns._ID + ", "
			+ InstalledColumns.PKGNAME + ", " + InstalledColumns.APPNAME + ", "
			+ InstalledColumns.DATE + ", " + InstalledColumns.MILISECONDS
			+ ", " + InstalledColumns.LOGITUDE + ", "
			+ InstalledColumns.LATITUDE + ", " + InstalledColumns.NUMBER
			+ ") values (null,?, ?, ?,?, ?, ?,?)";

	private SQLiteDatabase db;
	private SQLiteStatement insertStatement;

	public InstalledDao(SQLiteDatabase db) {
		this.db = db;
		insertStatement = db.compileStatement(InstalledDao.INSERT);
	}

	public long save(InstalledRecord entity) {
		insertStatement.clearBindings();
		// nov21
		// Log.i("installedread","save installed");
		insertStatement.bindString(1, entity.getPkgName());
		insertStatement.bindString(2, entity.getAppName());
		insertStatement.bindString(3, entity.getDate());
		insertStatement.bindLong(4, entity.getMiliSeconds());
		insertStatement.bindString(5, String.valueOf(entity.getLogitude()));
		insertStatement.bindString(6, String.valueOf(entity.getLatitude()));
		// insertStatement.bindDouble(5, entity.getLogitude());
		// insertStatement.bindDouble(6, entity.getLatitude());
		insertStatement.bindLong(7, entity.getNumber());
		return insertStatement.executeInsert();

	}

	public InstalledRecord get(long id) {
		InstalledRecord InstalledRecord = null;
		Cursor c = db.query(DataConstants.INSTALLED_TABLE_NAME, new String[] {
				BaseColumns._ID, InstalledColumns.PKGNAME,
				InstalledColumns.APPNAME, InstalledColumns.DATE,
				InstalledColumns.MILISECONDS, InstalledColumns.LOGITUDE,
				InstalledColumns.LATITUDE, InstalledColumns.NUMBER },
				BaseColumns._ID + " = ?", new String[] { String.valueOf(id) },
				null, null, null, "1");

		if (c.moveToFirst()) {
			InstalledRecord = this.buildInstalledRecordFromCursor(c);
		}
		if (!c.isClosed()) {
			c.close();
		}
		return InstalledRecord;
	}

	public InstalledRecord buildInstalledRecordFromCursor(Cursor c) {
		InstalledRecord installedRecord = null;
		if (c != null) {
			installedRecord = new InstalledRecord();
			installedRecord.setId(c.getLong(0));
			installedRecord.setPkgName(c.getString(1));
			installedRecord.setAppName(c.getString(2));
			installedRecord.setDate(c.getString(3));
			installedRecord.setMiliSeconds(c.getLong(4));
			installedRecord.setLogitude(Double.parseDouble(c.getString(5)));
			installedRecord.setLatitude(Double.parseDouble(c.getString(6)));
			// installedRecord.setLogitude(c.getDouble(5));
			// installedRecord.setLatitude(c.getDouble(6));
			installedRecord.setNumber(c.getLong(7));
		}
		return installedRecord;
	}

	public List<InstalledRecord> getAll() {
		List<InstalledRecord> list = new ArrayList<InstalledRecord>();
		Cursor c = db.query(DataConstants.INSTALLED_TABLE_NAME, new String[] {
				BaseColumns._ID, InstalledColumns.PKGNAME,
				InstalledColumns.APPNAME, InstalledColumns.DATE,
				InstalledColumns.MILISECONDS, InstalledColumns.LOGITUDE,
				InstalledColumns.LATITUDE, InstalledColumns.NUMBER }, null,
				null, null, null, BaseColumns._ID, null);
		if (c.moveToFirst()) {
			do {
				InstalledRecord installedRecord = this
						.buildInstalledRecordFromCursor(c);
				if (installedRecord != null) {
					list.add(installedRecord);
				}
			} while (c.moveToNext());
		}
		if (!c.isClosed()) {
			c.close();
		}
		return list;
	}

	public List<InstalledRecord> getAll(long flag) {
		List<InstalledRecord> list = new ArrayList<InstalledRecord>();
//		System.out.println("installedflag:" + flag);
		Cursor c = db.query(DataConstants.INSTALLED_TABLE_NAME, new String[] {
				BaseColumns._ID, InstalledColumns.PKGNAME,
				InstalledColumns.APPNAME, InstalledColumns.DATE,
				InstalledColumns.MILISECONDS, InstalledColumns.LOGITUDE,
				InstalledColumns.LATITUDE, InstalledColumns.NUMBER },
				BaseColumns._ID + "> ?", new String[] { String.valueOf(flag) },
				null, null, BaseColumns._ID, null);
		if (c.moveToFirst()) {
			do {
				flag++;
				InstalledRecord installedRecord = this
						.buildInstalledRecordFromCursor(c);
				if (installedRecord != null) {
					list.add(installedRecord);
				}
			} while (c.moveToNext());
		}
		if (!c.isClosed()) {
			c.close();
		}
		return list;
	}

	public void update(InstalledRecord type) {
		// TODO Auto-generated method stub

	}

	public void delete(InstalledRecord type) {
		// TODO Auto-generated method stub

	}

	public void deleteAll(SQLiteDatabase db) {
		db.delete(DataConstants.INSTALLED_TABLE_NAME, null, null);
	}
}
