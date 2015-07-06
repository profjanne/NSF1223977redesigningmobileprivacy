package my.applicationforeground;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class GetSharePrefer {
	Context context;

	public GetSharePrefer(Context context) {
		this.context = context;
	}

	public void setUserID(int user) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this.context);
		prefs.edit().putString(DataConstants.USERID,String.valueOf(user)).commit();
	}

	public void setGroupID(String group) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this.context);
		prefs.edit().putString(DataConstants.GROUPID,String.valueOf(group)).commit();
	}

	public String getUserID() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this.context);
		String userID = prefs.getString(DataConstants.USERID, "0");
		return userID;
	}

	public String getGroupID() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this.context);
		String groupID = prefs.getString(DataConstants.GROUPID, "0");
		return groupID;
	}
	public void setStartDate(long miliseconds) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this.context);
		prefs.edit().putLong(DataConstants.STARTDATE, new Long(miliseconds))
				.commit();
	}
	public long getStartDate() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this.context);
		long time = prefs.getLong(DataConstants.STARTDATE, new Long(0));
		return time;
	}
}
