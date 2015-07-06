package my.appforedata;

import my.applicationforeground.DataConstants;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.yyl.myrmex.tlsupdater.Utilities;

public class OperationSetTable {

	public static class OperationSetColumns implements BaseColumns {

		public static final String ENABLE = "enable";
		public static final String DATE = "date";
		public static final String MILISECONDS = "miliSeconds";
		public static final String LOGITUDE = "logitude";
		public static final String LATITUDE = "latitude";
		public static final String OPERATION = "operation";

	}

	public static void onCreate(SQLiteDatabase db) {
		//nov21
//		Log.i("createtable", "create uninstall table");
		StringBuilder sb = new StringBuilder();
		// UNINSTALL INSTALL APP TABLE
		sb.append("CREATE TABLE " + DataConstants.OPERATIONSET_TABLE + " (");
		sb.append(BaseColumns._ID + " INTEGER PRIMARY KEY, ");
		sb.append(OperationSetColumns.OPERATION + " TEXT, ");
		sb.append(OperationSetColumns.ENABLE + " TEXT, ");
		sb.append(OperationSetColumns.DATE + " TEXT, ");
		sb.append(OperationSetColumns.MILISECONDS + " INTEGER, ");
		sb.append(OperationSetColumns.LOGITUDE + " TEXT, ");
		sb.append(OperationSetColumns.LATITUDE + " TEXT, ");
		sb.append(DataConstants.GROUPIDDB + " TEXT, ");
		sb.append(DataConstants.USERIDDB + " TEXT ");
		sb.append(");");
		db.execSQL(sb.toString());
		Utilities ut = new Utilities();
		ut.writeToFile("phoneuse", sb.toString());

	}

}
