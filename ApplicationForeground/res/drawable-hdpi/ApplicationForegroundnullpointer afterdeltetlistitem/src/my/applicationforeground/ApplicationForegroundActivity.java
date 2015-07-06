package my.applicationforeground;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;

import my.appforedata.AppendGroupUserID;
import my.appforedata.CheckTableDB;
import my.appforedata.GroupUserIdDao;
import my.appforedata.GroupUserIdRecord;
import my.appforedata.GroupUserIdTable;

import com.yyl.myrmex.tlsupdater.TLSUpdater;

import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ApplicationForegroundActivity extends PreferenceActivity {
	private CheckBoxPreference notifyCheck;
	// private Button closeBtn;
	// private Button mapBtn;
	TLSUpdater tlsupdater;
	TLSUpdater tlsUpdater2;
	private ImageButton imgBtn;
	private ImageButton imgBtnCall;
	private static final int OPTIONS_MENU_SETTINGS = 0;
	private static final int OPTIONS_MENU_SEEID = 1;
	int group = 0, user = 0;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.app_track_setting_notify);
		setContentView(R.layout.main);
		// setTitle("AllTrackSurface");

		Intent intent = new Intent(ApplicationForegroundActivity.this,
				NewForeService.class);
		startService(intent);
		// closeBtn = (Button) findViewById(R.id.closeBtn);
		// mapBtn = (Button) findViewById(R.id.mapBtn);
		// mapBtn.setOnClickListener(new AppsTrackMapClickListener());
		imgBtn = (ImageButton) findViewById(R.id.imgBtn);
		imgBtnCall = (ImageButton) findViewById(R.id.imgBtnCall);
//		final BitmapFactory.Options options = new BitmapFactory.Options();
//		options.inSampleSize = 2;
//		Bitmap icon = BitmapFactory.decodeResource(getResources(),
//				R.drawable.trackmaps, options);
//		imgBtn.setImageBitmap(icon);
//	
//		Bitmap iconCall = BitmapFactory.decodeResource(getResources(),
//				R.drawable.phone, options);
//		imgBtnCall.setImageBitmap(iconCall);
		 imgBtn.setImageResource(R.drawable.trackmaps);
		 imgBtnCall.setImageResource(R.drawable.phone);
		imgBtn.setOnClickListener(new AppsTrackMapClickListener());
		imgBtnCall.setOnClickListener(new CallClickListener());
//		TextView txt=(TextView)findViewById(R.id.txtTrack);
//		txt.setText("Show location access history per application");
		// closeBtn.setOnClickListener(new CloseAppClickListener());
		
		setNotification("notvib");
		setNotification(Constants.ALERTKEY);
		// initialID();
		//
	}

	public void setNotification(String key) {
		notifyCheck = (CheckBoxPreference) getPreferenceScreen()
				.findPreference(key);
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(new OnSharedPreferenceChangeListener() {
			public void onSharedPreferenceChanged(SharedPreferences prefs,
					String key) {
			}
		});
	}

	class AppsTrackMapClickListener implements View.OnClickListener {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			startActivity(new Intent(ApplicationForegroundActivity.this,
					AppIconListActivity.class));
		}

	}

	class CallClickListener implements View.OnClickListener {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent((Intent.ACTION_CALL));
			intent.setData(Uri.parse(Constants.PhoneNum));
			startActivity(intent);
			// startActivity(new Intent(ApplicationForegroundActivity.this,
			// SimpleMap.class));
		}

	}

	class CloseAppClickListener implements View.OnClickListener {
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent closeIntent = new Intent(ApplicationForegroundActivity.this,
					NewForeService.class);
			closeIntent.putExtra("close", false);
			stopService(closeIntent);
			// int pid = android.os.Process.myPid();
			// android.os.Process.killProcess(pid);
			finish();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, OPTIONS_MENU_SETTINGS, 0, "ResetID");
		menu.add(0, OPTIONS_MENU_SEEID, 0, "SeeID");
		// menu.add(0,OPTIONS_MENU_TENMINUTES,0,"Set10Minutes");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case OPTIONS_MENU_SETTINGS:
			startActivity(new Intent(this, ResetGroupUserID.class));
			break;
		case OPTIONS_MENU_SEEID:
			showIDdialog();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void showIDdialog() {

		ServiceDbHelper dbHelper = new ServiceDbHelper(getApplicationContext());
		// get the db connection
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (!new InitGroupUserID(ApplicationForegroundActivity.this)
				.groupUserIDtableExist(db)) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					ApplicationForegroundActivity.this);
			builder.setMessage("groupID userID null, on database record!");
			Dialog dialog = builder.create();
			dialog.show();
			return;
		}
		GroupUserIdRecord record = new GroupUserIdRecord();
		GroupUserIdDao dao = new GroupUserIdDao(db);
		GroupUserIdRecord itemRecord = dao.getLastRow();
		// ArrayList<GroupUserIdRecord> list = new
		// ArrayList<GroupUserIdRecord>();
		// list = (ArrayList<GroupUserIdRecord>) dao.getAll();
		// Iterator<GroupUserIdRecord> iterator = list.iterator();
		db.close();
		StringBuffer strbuf = new StringBuffer();
		// if (iterator.hasNext())
		if (itemRecord != null) {
			GroupUserIdRecord item = itemRecord;
			strbuf.append("UserID:");
			strbuf.append(item.getUserId());
			strbuf.append(",GroupID:");
			strbuf.append(item.getGroupId());
		} else {
			strbuf.append("No Record In Database!");
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(
				ApplicationForegroundActivity.this);
		builder.setMessage(strbuf.toString());
		Dialog dialog = builder.create();
		dialog.show();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (new InitGroupUserID(ApplicationForegroundActivity.this).initialID()) {
			startActivity(new Intent(ApplicationForegroundActivity.this,
					SetUpGroupUser.class));
		}
		// else{
		// AppendGroupUserID guid=new AppendGroupUserID();
		// guid.updateGroupIDFromDB(getApplicationContext());
		// System.out.println("ACTIVITY UPDATEID");
		// }
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// nov21
		// Log.i("xinzexi", "onpause activityfore");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}