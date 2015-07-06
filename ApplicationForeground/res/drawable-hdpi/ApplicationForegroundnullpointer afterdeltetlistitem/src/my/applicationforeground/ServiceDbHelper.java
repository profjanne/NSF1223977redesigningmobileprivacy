package my.applicationforeground;

import my.appforedata.AppChangeLocationTable;
import my.appforedata.AppUsageTable;
import my.appforedata.GroupUserIdTable;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ServiceDbHelper extends SQLiteOpenHelper {
	public static final int VERSION = 1;
	public static final int VERSIONUPDATE = 2;

	public ServiceDbHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	public ServiceDbHelper(Context context) {
		super(context, DataConstants.OTHERDATABASE, null,
				DataConstants.DBVERSIONUPDATE);
		// nov21
		// Log.i("usage","servicedbhelper");
	}

	@Override
	public synchronized SQLiteDatabase getWritableDatabase() {
		// TODO Auto-generated method stub
		// nov21
		// Log.i("xinzexi","getwritabledatabase servicedbhelper");
		return super.getWritableDatabase();
	}

	@Override
	public synchronized SQLiteDatabase getReadableDatabase() {
		// TODO Auto-generated method stub
		return super.getReadableDatabase();
	}

	@Override
	public synchronized void close() {
		// TODO Auto-generated method stub
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		// nov21
		Log.i("check", "create usagetable in servicedbhelper");
		AppUsageTable.onCreate(db);
		AppChangeLocationTable.onCreate(db);
		GroupUserIdTable.onCreate(db);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}

	public void onUpgrade(SQLiteDatabase db) {
		onUpgrade(db, DataConstants.DBVERSION, DataConstants.DBVERSIONUPDATE);
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		super.onOpen(db);
		// nov21
		// Log.i("usage","servicedb onopen");
	}

}
