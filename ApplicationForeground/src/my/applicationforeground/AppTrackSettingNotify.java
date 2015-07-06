package my.applicationforeground;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

public class AppTrackSettingNotify extends PreferenceActivity{
	private CheckBoxPreference notifyCheck;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.app_track_setting_notify);
		notifyCheck = (CheckBoxPreference) getPreferenceScreen().findPreference(
				"notification");
		setCheckBoxSummary(notifyCheck);
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(new OnSharedPreferenceChangeListener() {
			public void onSharedPreferenceChanged(SharedPreferences prefs,
					String key) {
				//nov21
//				Log.i("distance","changelistener"+String.valueOf(prefs.getBoolean("notification", true)));
				if (key.equals("notification")) {
					setCheckBoxSummary(notifyCheck);
					//nov21
//					Log.i("pref","changelistener"+String.valueOf(prefs.getBoolean("notification", true)));
				}
			}
		});
	}
	private void setCheckBoxSummary(CheckBoxPreference pref) {
//		if (pref.isChecked()) {
//			pref.setSummary("Enable");
//			//not show the notify
//			
//		} else {
//			pref.setSummary("Disable");
//		}
	}

}
