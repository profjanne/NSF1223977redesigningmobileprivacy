package my.appforedata;

import java.text.DecimalFormat;

import my.applicationforeground.DataTypeTransfer;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class ReadSpecificStringFromDb {
	private Context context;
	private SQLiteDatabase mSQLiteDatabase = null;

	public ReadSpecificStringFromDb(Context context) {
		this.context = context;
	}

	public boolean readUploadFromLocal(String imageName, String table,
			SQLiteDatabase mSQLiteDatabase) {
		// mSQLiteDatabase = this.context.openOrCreateDatabase(
		// Constants.DATABASE_NAME, this.context.MODE_PRIVATE, null);
		this.mSQLiteDatabase = mSQLiteDatabase;
		Cursor cur = mSQLiteDatabase.rawQuery(
				"select x,y,scope,description from " + table
						+ " info where image_name ='" + imageName + "'", null);
		if (cur != null) {
			if (cur.moveToFirst()) {
				do {
					DecimalFormat myformat = new DecimalFormat("#0.000");
				} while (cur.moveToNext());
			}
			return true;
		}
		return false;
	}
}
