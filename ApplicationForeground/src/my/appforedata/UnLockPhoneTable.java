package my.appforedata;

import my.applicationforeground.DataConstants;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

public class UnLockPhoneTable {
	public static class UnLockPhoneColumns implements BaseColumns
	{
		public static final String UNLOCKDATE="unlockDate";
		public static final String LOCKDATE="lockDate";
//		THE INTERVAL OF OPEN THE PHONR OR UNLOCK TIME
		public static final String INTERVALDATE="intervalDate";
		public static final String UNLOCKMILISECONDS="unlockMiliSeconds";
		public static final String LOCKMILISECONDS="lockMiliSeconds";
		public static final String INTERVALMILISECONDS="intervalMiliSeconds";
	}
	public static void onCreate(SQLiteDatabase db)
	{
		//nov21
//		Log.i("createtable","unlockphone create");
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE "+DataConstants.UN_LOCKPHONE_TABLE_NAME+" (");
		sb.append(BaseColumns._ID + " INTEGER PRIMARY KEY, ");
		sb.append(UnLockPhoneColumns.UNLOCKDATE+" TEXT, ");
		sb.append(UnLockPhoneColumns.LOCKDATE+" TEXT, ");
		sb.append(UnLockPhoneColumns.INTERVALDATE+" TEXT, ");
		sb.append(UnLockPhoneColumns.UNLOCKMILISECONDS+" INTEGER, ");
		sb.append(UnLockPhoneColumns.LOCKMILISECONDS+" INTEGER, ");
		sb.append(UnLockPhoneColumns.INTERVALMILISECONDS+" INTEGER, ");
		sb.append(DataConstants.GROUPIDDB + " TEXT, ");
		sb.append(DataConstants.USERIDDB + " TEXT ");
		sb.append(");");
		db.execSQL(sb.toString());
	}

}
