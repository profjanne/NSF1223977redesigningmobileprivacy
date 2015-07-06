package my.appforedata;

import my.applicationforeground.DataConstants;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

public class InstalledTable {

	  public static class InstalledColumns implements BaseColumns {

		   public static final String PKGNAME="pkgName";
			public static final String APPNAME="appName";
			public static final String DATE="date";
			public static final String MILISECONDS="miliSeconds";
			public static final String LOGITUDE="logitude";
			public static final String LATITUDE="latitude";
			public static final String NUMBER="number";
			
		   }
	   

	   public static void onCreate(SQLiteDatabase db) {
		 //nov21
//		   Log.i("createtable","create installed table");
		      StringBuilder sb = new StringBuilder();
		      //UNINSTALL INSTALL APP TABLE
		      sb.append("CREATE TABLE " + DataConstants.INSTALLED_TABLE_NAME + " (");
		      sb.append(BaseColumns._ID + " INTEGER PRIMARY KEY, ");
		      sb.append(InstalledColumns.PKGNAME + " TEXT, ");
		      sb.append(InstalledColumns.APPNAME + " TEXT, "); 
		      sb.append(InstalledColumns.DATE + " TEXT, ");
		      sb.append(InstalledColumns.MILISECONDS + " INTEGER, ");
		      sb.append(InstalledColumns.LOGITUDE + " TEXT, "); 
		      sb.append(InstalledColumns.LATITUDE + " TEXT, ");
//		      sb.append(InstalledColumns.LOGITUDE + " REAL, "); 
//		      sb.append(InstalledColumns.LATITUDE + " REAL, ");
		      sb.append(InstalledColumns.NUMBER + " INTEGER, ");
				sb.append(DataConstants.GROUPIDDB + " TEXT, ");
				sb.append(DataConstants.USERIDDB + " TEXT ");
		      sb.append(");");
		      db.execSQL(sb.toString());
		   }
}
