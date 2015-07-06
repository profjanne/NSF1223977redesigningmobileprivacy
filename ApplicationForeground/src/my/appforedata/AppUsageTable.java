package my.appforedata;

import my.applicationforeground.DataConstants;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

public class AppUsageTable {
	public static class AppUsageColumns implements BaseColumns {

		public static final String PKGNAME = "pkgName";
		public static final String APPNAME = "appName";
		// added oct16
		public static final String ACTNAME = "actName";
		public static final String STARTDATE = "startDate";
		public static final String ENDDATE = "endDate";
		public static final String INTERVALDATE = "intervalDate";
		public static final String LOGITUDE = "logitude";
		public static final String LATITUDE = "latitude";
		public static final String STARTMILISECONDS = "startMiliSeconds";
		public static final String ENDMILISECONDS = "endMiliSeconds";
		public static final String INTERVALMILISECONDS = "intervalMiliSeconds";
		public static final String LOCCHANGE = "locChange";
	}

	public static void onCreate(SQLiteDatabase db) {
		// nov21
		// Log.i("createtable","create appusage table");
		
		StringBuilder sb = new StringBuilder();
		// UNINSTALL INSTALL APP TABLE
		sb.append("CREATE TABLE " + DataConstants.APPUSAGE_TABLE_NAME + " (");
		sb.append(BaseColumns._ID + " INTEGER PRIMARY KEY, ");
		sb.append(AppUsageColumns.PKGNAME + " TEXT, ");
		sb.append(AppUsageColumns.APPNAME + " TEXT, ");
		// added oct16
		sb.append(AppUsageColumns.ACTNAME + " TEXT, ");
		sb.append(AppUsageColumns.STARTDATE + " TEXT, ");
		sb.append(AppUsageColumns.ENDDATE + " TEXT, ");
		sb.append(AppUsageColumns.INTERVALDATE + " TEXT, ");
		// sb.append(AppUsageColumns.LOGITUDE + " REAL, ");
		// sb.append(AppUsageColumns.LATITUDE + " REAL, ");
		// OCT18 LONG-STRING in db
		sb.append(AppUsageColumns.LOGITUDE + " TEXT, ");
		sb.append(AppUsageColumns.LATITUDE + " TEXT, ");
		sb.append(AppUsageColumns.STARTMILISECONDS + " INTEGER, ");
		sb.append(AppUsageColumns.ENDMILISECONDS + " INTEGER, ");
		sb.append(AppUsageColumns.INTERVALMILISECONDS + " INTEGER, ");
		// oct17
		sb.append(AppUsageColumns.LOCCHANGE + " INTEGER, ");
		sb.append(DataConstants.GROUPIDDB + " TEXT, ");
		sb.append(DataConstants.USERIDDB + " TEXT ");
		sb.append(");");
		db.execSQL(sb.toString());
	}

	public static void onUpgrade(SQLiteDatabase db) {
		Log.i("update","updateappusage");
		StringBuilder sb = new StringBuilder();
		// UNINSTALL INSTALL APP TABLE
		sb.append("CREATE TEMP  TABLE IF NOT EXISTS "
				+ DataConstants.TEMP_APPUSAGE_TABLE_NAME + " (");
		sb.append(BaseColumns._ID + " INTEGER PRIMARY KEY, ");
		sb.append(AppUsageColumns.PKGNAME + " TEXT, ");
		sb.append(AppUsageColumns.APPNAME + " TEXT, ");
		// added oct16
		sb.append(AppUsageColumns.ACTNAME + " TEXT, ");
		sb.append(AppUsageColumns.STARTDATE + " TEXT, ");
		sb.append(AppUsageColumns.ENDDATE + " TEXT, ");
		sb.append(AppUsageColumns.INTERVALDATE + " TEXT, ");
		// sb.append(AppUsageColumns.LOGITUDE + " REAL, ");
		// sb.append(AppUsageColumns.LATITUDE + " REAL, ");
		// OCT18 LONG-STRING in db
		sb.append(AppUsageColumns.LOGITUDE + " TEXT, ");
		sb.append(AppUsageColumns.LATITUDE + " TEXT, ");
		sb.append(AppUsageColumns.STARTMILISECONDS + " INTEGER, ");
		sb.append(AppUsageColumns.ENDMILISECONDS + " INTEGER, ");
		sb.append(AppUsageColumns.INTERVALMILISECONDS + " INTEGER, ");
		// oct17
		sb.append(AppUsageColumns.LOCCHANGE + " INTEGER ");
		sb.append(");");
		db.execSQL(sb.toString());
		db.execSQL("INSERT INTO "
				+ DataConstants.TEMP_APPUSAGE_TABLE_NAME 
				+ " SELECT " +BaseColumns._ID +","+
				AppUsageColumns.PKGNAME+","+
				AppUsageColumns.APPNAME+","+ AppUsageColumns.ACTNAME+","+
				AppUsageColumns.STARTDATE+","+ AppUsageColumns.ENDDATE+","+
				AppUsageColumns.INTERVALDATE+","+ AppUsageColumns.LOGITUDE+","+
				AppUsageColumns.LATITUDE+","+
				AppUsageColumns.STARTMILISECONDS+","+
				AppUsageColumns.ENDMILISECONDS+","+
				AppUsageColumns.INTERVALMILISECONDS+","+
				AppUsageColumns.LOCCHANGE + " FROM "
				+ DataConstants.APPUSAGE_TABLE_NAME);
		
		db.execSQL("DROP TABLE "+DataConstants.APPUSAGE_TABLE_NAME);
		onCreate(db);
		db.execSQL("INSERT INTO "
				+ DataConstants.APPUSAGE_TABLE_NAME
				+ " SELECT " +AppUsageColumns.PKGNAME+","+
				AppUsageColumns.APPNAME+","+ AppUsageColumns.ACTNAME+","+
				AppUsageColumns.STARTDATE+","+ AppUsageColumns.ENDDATE+","+
				AppUsageColumns.INTERVALDATE+","+ AppUsageColumns.LOGITUDE+","+
				AppUsageColumns.LATITUDE+","+
				AppUsageColumns.STARTMILISECONDS+","+
				AppUsageColumns.ENDMILISECONDS+","+
				AppUsageColumns.INTERVALMILISECONDS+","+
				AppUsageColumns.LOCCHANGE + " FROM "
				+ DataConstants.TEMP_APPUSAGE_TABLE_NAME);
		db.execSQL("DROP TABLE "+DataConstants.TEMP_APPUSAGE_TABLE_NAME);
		// db.execSQL("DROP TABLE IF EXISTS " +
		// DataConstants.APPUSAGE_TABLE_NAME);
		// MovieTable.onCreate(db);
		// StringBuilder sb = new StringBuilder();
		// sb.append("ALTER TABLE " + DataConstants.APPUSAGE_TABLE_NAME + " (");
		// sb.append(" ADD COLUMN ");
		// sb.append(DataConstants.GROUPIDDB+" TEXT, ");
		// sb.append(" ADD COLUMN ");
		// sb.append(DataConstants.USERIDDB + " TEXT ");
		// sb.append(");");
		// db.execSQL(sb.toString());
		// db.execSQL("ALTER "+DataConstants.APPUSAGE_TABLE_NAME+" ADD COLUMN "+DataConstants.GROUPIDDB
		// +" TEXT");
		// db.execSQL("ALTER "+DataConstants.APPUSAGE_TABLE_NAME+" ADD COLUMN "+DataConstants.USERIDDB
		// +" TEXT");
	}
}
