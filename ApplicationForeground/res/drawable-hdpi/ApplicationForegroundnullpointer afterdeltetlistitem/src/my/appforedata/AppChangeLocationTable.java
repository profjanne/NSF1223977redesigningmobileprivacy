package my.appforedata;

import my.applicationforeground.DataConstants;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

public class AppChangeLocationTable {
	public static class AppUsageColumns implements BaseColumns {

		public static final String PKGNAME = "pkgName";
		public static final String APPNAME = "appName";
		// added oct16
		public static final String ACTNAME = "actName";
		public static final String STARTDATE = "startDate";
		public static final String LOGITUDE = "logitude";
		public static final String LATITUDE = "latitude";
		public static final String STARTMILISECONDS = "startMiliSeconds";
	}

	public static void onCreate(SQLiteDatabase db) {
		// nov21
		// Log.i("createtable","create appchanglocation table");
		StringBuilder sb = new StringBuilder();
		// UNINSTALL INSTALL APP TABLE
		sb.append("CREATE TABLE " + DataConstants.APPCHANGELOCATION_TABLE_NAME
				+ " (");
		sb.append(BaseColumns._ID + " INTEGER PRIMARY KEY, ");
		sb.append(AppUsageColumns.PKGNAME + " TEXT, ");
		sb.append(AppUsageColumns.APPNAME + " TEXT, ");
		// added oct16
		sb.append(AppUsageColumns.ACTNAME + " TEXT, ");
		sb.append(AppUsageColumns.LOGITUDE + " TEXT, ");
		sb.append(AppUsageColumns.LATITUDE + " TEXT, ");
		// sb.append(AppUsageColumns.LOGITUDE + " REAL, ");
		// sb.append(AppUsageColumns.LATITUDE + " REAL, ");
		sb.append(AppUsageColumns.STARTDATE + " TEXT, ");
		sb.append(AppUsageColumns.STARTMILISECONDS + " INTEGER, ");
		sb.append(DataConstants.GROUPIDDB + " TEXT, ");
		sb.append(DataConstants.USERIDDB + " TEXT ");
		sb.append(");");
		db.execSQL(sb.toString());
	}

}
