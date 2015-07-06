package my.appforedata;

import my.applicationforeground.DataConstants;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceIDTableRead {
	private String table;
	private Context context;
	private long id;

	public PreferenceIDTableRead(Context context,String table) {
		this.table = table;
		this.context=context;
	}
	public void setIDMax(long id){
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this.context);
		prefs.edit().putLong(this.table,id).commit();
	}
	public long getIDMax(){
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this.context);
		long id = prefs.getLong(this.table, 0);
		return id;
	}
}
