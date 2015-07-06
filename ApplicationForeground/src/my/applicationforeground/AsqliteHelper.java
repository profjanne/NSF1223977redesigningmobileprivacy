package my.applicationforeground;


import my.appforedata.AppUsageTable;
import my.appforedata.InstalledTable;
import my.appforedata.UnInstallTable;
import my.appforedata.UnLockPhoneTable;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AsqliteHelper extends SQLiteOpenHelper{
	private static final int VERSION=1;

	public AsqliteHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	public AsqliteHelper(Context context, String name){
		this(context, name, VERSION);
	}
	public AsqliteHelper(Context context, String name,int version){
		this(context, name, null, version);
	}
	
	public  AsqliteHelper(Context context) {
	      super(context, DataConstants.DATABASE_NAME, null, VERSION);
	   }

	@Override
	public synchronized SQLiteDatabase getWritableDatabase() {
		// TODO Auto-generated method stub
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
		//nov21
//		Log.i("LOG","CREAT SQLITE IN HELPER");
		UnInstallTable.onCreate(db);
		InstalledTable.onCreate(db);
		UnLockPhoneTable.onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		//nov21
//		Log.i("LOG","update "+String.valueOf(newVersion)+" old:"+String.valueOf(oldVersion)+" db:"+String.valueOf(db));
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		super.onOpen(db);
	}

}
