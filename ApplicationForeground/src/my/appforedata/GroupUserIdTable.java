package my.appforedata;

import my.applicationforeground.DataConstants;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

public class GroupUserIdTable {

	 public static class GroupUserColumns implements BaseColumns {

		   public static final String GROUP="groupId";
			public static final String USER="userId";
			public static final String DEVICE="deviceId";
			public static final String DATE="date";
		   }
	   

	   public static void onCreate(SQLiteDatabase db) {
		      StringBuilder sb = new StringBuilder();
		      Log.i("check","createtable groupuserid ");
//		      "CREATE TABLE IF NOT EXISTS "
		      sb.append("CREATE TABLE " + DataConstants.GROUPUSER_TABLE + " (");
		      sb.append(BaseColumns._ID + " INTEGER PRIMARY KEY, ");
		      sb.append(GroupUserColumns.GROUP + " TEXT, ");
		      sb.append(GroupUserColumns.USER + " TEXT, "); 
		      sb.append(GroupUserColumns.DEVICE + " TEXT, ");
		      sb.append(GroupUserColumns.DATE + " TEXT ");
		      sb.append(");");
		      db.execSQL(sb.toString());
		   }
}
