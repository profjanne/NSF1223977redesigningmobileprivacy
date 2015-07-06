package my.appforedata;

import com.yyl.myrmex.tlsupdater.Utilities;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;
import my.applicationforeground.DataConstants;

public final class UnInstallTable {

	public static class UnInstallColumns implements BaseColumns {

		public static final String PKGNAME = "pkgName";
		public static final String APPNAME = "appName";
		public static final String DATE = "date";
		public static final String MILISECONDS = "miliSeconds";
		public static final String LOGITUDE = "logitude";
		public static final String LATITUDE = "latitude";
		// 1 install ;0 uninstall
		public static final String OPERATION = "operation";

	}

	public static void onCreate(SQLiteDatabase db) {
		//nov21
//		Log.i("createtable", "create uninstall table");
		StringBuilder sb = new StringBuilder();
		// UNINSTALL INSTALL APP TABLE
		sb.append("CREATE TABLE " + DataConstants.UN_INSTALL_TABLE_NAME + " (");
		sb.append(BaseColumns._ID + " INTEGER PRIMARY KEY, ");
		sb.append(UnInstallColumns.PKGNAME + " TEXT, ");
		sb.append(UnInstallColumns.APPNAME + " TEXT, ");
		sb.append(UnInstallColumns.DATE + " TEXT, ");
		sb.append(UnInstallColumns.MILISECONDS + " INTEGER, ");
		sb.append(UnInstallColumns.LOGITUDE + " TEXT, ");
		sb.append(UnInstallColumns.LATITUDE + " TEXT, ");
		sb.append(UnInstallColumns.OPERATION + " TEXT, ");
		sb.append(DataConstants.GROUPIDDB + " TEXT, ");
		sb.append(DataConstants.USERIDDB + " TEXT ");
		sb.append(");");
		db.execSQL(sb.toString());
		Utilities ut = new Utilities();
		ut.writeToFile("phoneuse", sb.toString());

	}

}
