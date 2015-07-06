package my.appforedata;

import java.util.ArrayList;
import java.util.List;

import my.appforedata.AppUsageTable.AppUsageColumns;
import my.applicationforeground.DataConstants;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;
import android.util.Log;

public class AppChangeLocationDao implements
		DaoTemplate<AppChangeLocationRecord> {

	private static final String INSERT = "insert into "
			+ DataConstants.APPCHANGELOCATION_TABLE_NAME + "("
			+ BaseColumns._ID + ", " + AppUsageColumns.PKGNAME + ", "
			+ AppUsageColumns.APPNAME + ", " + AppUsageColumns.ACTNAME + ", "
			+ AppUsageColumns.LOGITUDE + ", " + AppUsageColumns.LATITUDE + ", "
			+ AppUsageColumns.STARTDATE + ", "
			+ AppUsageColumns.STARTMILISECONDS
			+ ") values (null,?, ?,?, ?,?, ?, ?)";

	private SQLiteDatabase db;
	private SQLiteStatement insertStatement;

	public AppChangeLocationDao(SQLiteDatabase db) {
		this.db = db;
		insertStatement = db.compileStatement(AppChangeLocationDao.INSERT);
	}

	public long save(AppChangeLocationRecord entity) {
		insertStatement.clearBindings();
		// Log.i("index",String.valueOf(entity.getOpenTime()));
		insertStatement.bindString(1, entity.getPkgName());
		insertStatement.bindString(2, entity.getAppName());
		insertStatement.bindString(3, entity.getActName());
//		Log.i("USAGE", "appchange:" + String.valueOf(entity.getLogitude()));
		insertStatement.bindString(4, String.valueOf(entity.getLogitude()));
		insertStatement.bindString(5, String.valueOf(entity.getLatitude()));
		// insertStatement.bindDouble(4, entity.getLogitude());
		// insertStatement.bindDouble(5, entity.getLatitude());
		insertStatement.bindString(6, entity.getStartDate());
		insertStatement.bindLong(7, entity.getStartMiliSeconds());
		return insertStatement.executeInsert();

	}

	public AppChangeLocationRecord buildAppChangeLocationRecordFromCursor(
			Cursor c) {
		AppChangeLocationRecord AppChangeLocationRecord = null;
		if (c != null) {
			AppChangeLocationRecord = new AppChangeLocationRecord();
			AppChangeLocationRecord.setId(c.getLong(0));
			AppChangeLocationRecord.setPkgName(c.getString(1));
			AppChangeLocationRecord.setAppName(c.getString(2));
			// OCT16 ADD ACTIVITYLABEL
			AppChangeLocationRecord.setActName(c.getString(3));
			if (c.getString(4) == null||c.getString(5)==null) {
				AppChangeLocationRecord.setLogitude(Double.parseDouble(c
						.getString(4)));
				AppChangeLocationRecord.setLatitude(Double.parseDouble(c
						.getString(5)));
			}
			
			else{AppChangeLocationRecord.setLogitude(Double.parseDouble(c
					.getString(4)));
			AppChangeLocationRecord.setLatitude(Double.parseDouble(c
					.getString(5)));}
			// AppChangeLocationRecord.setLogitude(c.getDouble(4));
			// AppChangeLocationRecord.setLatitude(c.getDouble(5));
			AppChangeLocationRecord.setStartDate(c.getString(6));
			AppChangeLocationRecord.setStartMiliSeconds(c.getLong(7));
		}
		return AppChangeLocationRecord;
	}

	public AppChangeLocationRecord get(long id) {
		AppChangeLocationRecord AppChangeLocationRecord = null;
		Cursor c = db.query(DataConstants.APPCHANGELOCATION_TABLE_NAME,
				new String[] { BaseColumns._ID, AppUsageColumns.PKGNAME,
						AppUsageColumns.APPNAME, AppUsageColumns.ACTNAME,
						AppUsageColumns.STARTDATE, AppUsageColumns.LOGITUDE,
						AppUsageColumns.LATITUDE,
						AppUsageColumns.STARTMILISECONDS, }, BaseColumns._ID
						+ " = ?", new String[] { String.valueOf(id) }, null,
				null, null, "1");
		if (c.moveToFirst()) {
			AppChangeLocationRecord = this
					.buildAppChangeLocationRecordFromCursor(c);
		}
		if (!c.isClosed()) {
			c.close();
		}
		return AppChangeLocationRecord;
	}

	public List<AppChangeLocationRecord> getAll() {
		// nov21
		// Log.i("USAGE","dat getall usage");
		List<AppChangeLocationRecord> list = new ArrayList<AppChangeLocationRecord>();
		Cursor c = db.query(DataConstants.APPCHANGELOCATION_TABLE_NAME,
				new String[] { BaseColumns._ID, AppUsageColumns.PKGNAME,
						AppUsageColumns.APPNAME, AppUsageColumns.ACTNAME,
						AppUsageColumns.LOGITUDE, AppUsageColumns.LATITUDE,
						AppUsageColumns.STARTDATE,
						AppUsageColumns.STARTMILISECONDS, }, null, null, null,
				null, BaseColumns._ID, null);
		if (c.moveToLast()) {
			do {
				AppChangeLocationRecord AppChangeLocationRecord = this
						.buildAppChangeLocationRecordFromCursor(c);
				if (AppChangeLocationRecord != null) {
					list.add(AppChangeLocationRecord);
				}
			} while (c.moveToPrevious());
		}
		if (!c.isClosed()) {
			c.close();
		}
		return list;
	}

	// public List<AppChangeLocationRecord>getPkg(String pkg) {
	// List<AppChangeLocationRecord> list = new
	// ArrayList<AppChangeLocationRecord>();
	// Cursor c = db.query(DataConstants.APPCHANGELOCATION_TABLE_NAME, new
	// String[] {
	// BaseColumns._ID, AppUsageColumns.PKGNAME,
	// AppUsageColumns.APPNAME, AppUsageColumns.ACTNAME,
	// AppUsageColumns.STARTDATE,
	// AppUsageColumns.LOGITUDE,AppUsageColumns.LATITUDE,
	// AppUsageColumns.STARTMILISECONDS,
	// },
	// AppUsageColumns.PKGNAME + " = ? ", new String[] { pkg},
	// null, null, null, null);
	// if(c.moveToLast())
	// {
	// do
	// {
	// AppChangeLocationRecord AppChangeLocationRecord =
	// this.buildAppChangeLocationRecordFromCursor(c);
	// if(AppChangeLocationRecord!=null)
	// {
	// list.add(AppChangeLocationRecord);
	// }
	// }while(c.moveToPrevious());
	// }
	// if(!c.isClosed())
	// {
	// c.close();
	// }
	// return list;
	// }

	public void update(AppChangeLocationRecord type) {
		// TODO Auto-generated method stub

	}

	public void delete(AppChangeLocationRecord type) {
		// TODO Auto-generated method stub

	}

	public void deleteAll(SQLiteDatabase db) {
		db.delete(DataConstants.APPCHANGELOCATION_TABLE_NAME, null, null);
	}
}
