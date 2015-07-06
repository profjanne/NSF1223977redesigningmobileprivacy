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

public class AppUsageDao implements DaoTemplate<AppUsageRecord> {
	
private static long flag=0;
	private static final String INSERT = "insert into "
			+ DataConstants.APPUSAGE_TABLE_NAME + "(" + BaseColumns._ID + ", "
			+ AppUsageColumns.PKGNAME + ", " + AppUsageColumns.APPNAME + ", "
			+ AppUsageColumns.ACTNAME + ", " + AppUsageColumns.STARTDATE + ", "
			+ AppUsageColumns.ENDDATE + ", " + AppUsageColumns.INTERVALDATE
			+ ", " + AppUsageColumns.LOGITUDE + ", " + AppUsageColumns.LATITUDE
			+ ", " + AppUsageColumns.STARTMILISECONDS + ", "
			+ AppUsageColumns.ENDMILISECONDS + ", "
			+ AppUsageColumns.INTERVALMILISECONDS + ", "
			+ AppUsageColumns.LOCCHANGE
			+ ") values (null,?, ?,?, ?,?, ?, ?,?,?,?,?,?)";

	private SQLiteDatabase db;
	private SQLiteStatement insertStatement;

	public AppUsageDao(SQLiteDatabase db) {
		this.db = db;
		insertStatement = db.compileStatement(AppUsageDao.INSERT);
	}

	public long save(AppUsageRecord entity) {
		insertStatement.clearBindings();
		// Log.i("index",String.valueOf(entity.getOpenTime()));
		insertStatement.bindString(1, entity.getPkgName());
		insertStatement.bindString(2, entity.getAppName());
		insertStatement.bindString(3, entity.getActName());
		insertStatement.bindString(4, entity.getStartDate());
		insertStatement.bindString(5, entity.getEndDate());
		insertStatement.bindString(6, entity.getIntervalDate());
		// oct18 change double to string
		// insertStatement.bindDouble(7, entity.getLogitude());
		// insertStatement.bindDouble(8, entity.getLatitude());
		// nov21
//		Log.i("USAGE",
//				"STORE DB LOGI TO STRING"
//						+ String.valueOf(entity.getLogitude()));
		insertStatement.bindString(7, String.valueOf(entity.getLogitude()));
		insertStatement.bindString(8, String.valueOf(entity.getLatitude()));
		insertStatement.bindLong(9, entity.getStartMiliSeconds());
		insertStatement.bindLong(10, entity.getEndMiliSeconds());
		insertStatement.bindLong(11, entity.getIntervalMiliSeconds());
		insertStatement.bindLong(12, entity.getLocChange());
		return insertStatement.executeInsert();

	}

	public AppUsageRecord buildAppUsageRecordFromCursor(Cursor c) {
		AppUsageRecord appUsageRecord = null;
		if (c != null) {
			appUsageRecord = new AppUsageRecord();
			appUsageRecord.setId(c.getLong(0));
			appUsageRecord.setPkgName(c.getString(1));
			appUsageRecord.setAppName(c.getString(2));
			// OCT16 ADD ACTIVITYLABEL
			appUsageRecord.setActName(c.getString(3));
			appUsageRecord.setStartDate(c.getString(4));
			appUsageRecord.setEndDate(c.getString(5));
			appUsageRecord.setIntervalDate(c.getString(6));
			// oct18 change double to string in db
			if (c.getString(7) != null && c.getString(8) != null) {
				appUsageRecord.setLogitude(Double.parseDouble(c.getString(7)));
				appUsageRecord.setLatitude(Double.parseDouble(c.getString(8)));
			} else {
				appUsageRecord.setLogitude(1.1);
				appUsageRecord.setLatitude(1.1);
			}
			// nov21
			// Log.i("USAGE","LOGI STR DOBU"+c.getString(7)+String.valueOf(Double.parseDouble(c.getString(7))));

			appUsageRecord.setStartMiliSeconds(c.getLong(9));
			appUsageRecord.setEndMiliSeconds(c.getLong(10));
			appUsageRecord.setIntervalMiliSeconds(c.getLong(11));
			appUsageRecord.setLocChange(c.getLong(12));
		}
		return appUsageRecord;
	}

	public AppUsageRecord get(long id) {
		AppUsageRecord appUsageRecord = null;
		Cursor c = db.query(DataConstants.APPUSAGE_TABLE_NAME,
				new String[] { BaseColumns._ID, AppUsageColumns.PKGNAME,
						AppUsageColumns.APPNAME, AppUsageColumns.ACTNAME,
						AppUsageColumns.STARTDATE, AppUsageColumns.ENDDATE,
						AppUsageColumns.INTERVALDATE, AppUsageColumns.LOGITUDE,
						AppUsageColumns.LATITUDE,
						AppUsageColumns.STARTMILISECONDS,
						AppUsageColumns.ENDMILISECONDS,
						AppUsageColumns.INTERVALMILISECONDS,
						AppUsageColumns.LOCCHANGE }, BaseColumns._ID + " = ?",
				new String[] { String.valueOf(id) }, null, null, null, "1");
		if (c.moveToFirst()) {
			appUsageRecord = this.buildAppUsageRecordFromCursor(c);
		}
		if (!c.isClosed()) {
			c.close();
		}
		return appUsageRecord;
	}

	public List<AppUsageRecord> getAll() {
		// nov21
		// Log.i("USAGE","dat getall usage");
		List<AppUsageRecord> list = new ArrayList<AppUsageRecord>();
		Cursor c = db.query(DataConstants.APPUSAGE_TABLE_NAME,
				new String[] { BaseColumns._ID, AppUsageColumns.PKGNAME,
						AppUsageColumns.APPNAME, AppUsageColumns.ACTNAME,
						AppUsageColumns.STARTDATE, AppUsageColumns.ENDDATE,
						AppUsageColumns.INTERVALDATE, AppUsageColumns.LOGITUDE,
						AppUsageColumns.LATITUDE,
						AppUsageColumns.STARTMILISECONDS,
						AppUsageColumns.ENDMILISECONDS,
						AppUsageColumns.INTERVALMILISECONDS,
						AppUsageColumns.LOCCHANGE },  null, null, null, null,
				BaseColumns._ID, null);
		if (c.moveToFirst()) {
			do {
				flag++;
				AppUsageRecord appUsageRecord = this
						.buildAppUsageRecordFromCursor(c);
				if (appUsageRecord != null) {
					list.add(appUsageRecord);
				}
			} while (c.moveToNext());
		}
		if (!c.isClosed()) {
			c.close();
		}
//		System.out.println("after read appusageflag:"+flag);
		return list;
	}

	public List<AppUsageRecord> getAll(long flag) {
		// nov21
		// Log.i("USAGE","dat getall usage");
		List<AppUsageRecord> list = new ArrayList<AppUsageRecord>();
		
//		System.out.println("appusageflag:"+flag);
		Cursor c = db.query(DataConstants.APPUSAGE_TABLE_NAME,
				new String[] { BaseColumns._ID, AppUsageColumns.PKGNAME,
						AppUsageColumns.APPNAME, AppUsageColumns.ACTNAME,
						AppUsageColumns.STARTDATE, AppUsageColumns.ENDDATE,
						AppUsageColumns.INTERVALDATE, AppUsageColumns.LOGITUDE,
						AppUsageColumns.LATITUDE,
						AppUsageColumns.STARTMILISECONDS,
						AppUsageColumns.ENDMILISECONDS,
						AppUsageColumns.INTERVALMILISECONDS,
						AppUsageColumns.LOCCHANGE },  BaseColumns._ID+"> ?", new String[] {String.valueOf(flag)}, null, null,
				BaseColumns._ID, null);
		if (c.moveToFirst()) {
			do {
				AppUsageRecord appUsageRecord = this
						.buildAppUsageRecordFromCursor(c);
				if (appUsageRecord != null) {
					list.add(appUsageRecord);
				}
			} while (c.moveToNext());
		}
		if (!c.isClosed()) {
			c.close();
		}
		return list;
	}

	public void update(AppUsageRecord type) {
		// TODO Auto-generated method stub

	}

	public void delete(AppUsageRecord type) {
		// TODO Auto-generated method stub

	}

	public void deleteAll(SQLiteDatabase db) {
		db.delete(DataConstants.APPUSAGE_TABLE_NAME, null, null);
	}
}
