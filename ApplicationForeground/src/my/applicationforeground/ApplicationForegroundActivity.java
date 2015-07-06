package my.applicationforeground;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import my.appforedata.AppChangeLocationDao;
import my.appforedata.AppChangeLocationRecord;
import my.appforedata.AppUsageRecord;
import my.appforedata.AppendGroupUserID;
import my.appforedata.CheckTableDB;
import my.appforedata.GroupUserIdDao;
import my.appforedata.GroupUserIdRecord;
import my.appforedata.GroupUserIdTable;
import my.appforedata.LocationGet;
import my.appforedata.OperationSetDao;
import my.appforedata.OperationSetRecord;
import my.appforedata.PreferenceIDTableRead;
import my.appforedata.OperationSetTable.OperationSetColumns;

import com.yyl.myrmex.tlsupdater.TLSUpdater;

import android.R.color;
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
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
	private ImageButton imgBtnEmail;
	private static final int OPTIONS_MENU_SETTINGS = 0;
	private static final int OPTIONS_MENU_SEEID = 1;
	int group = 0, user = 0;
	LinearLayout layout;
	OnSharedPreferenceChangeListener listener;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// addPreferencesFromResource(R.xml.app_track_setting_notify);
		addPreferencesFromResource(R.layout.notify_set_preference);
		setContentView(R.layout.main);
		layout = (LinearLayout) findViewById(R.id.layoutMain);
		Intent intent = new Intent(ApplicationForegroundActivity.this,
				NewForeService.class);
		startService(intent);
		boolean delayok = checkSevenDaysDelay();
		// setTitle("AllTrackSurface");
		if (!delayok) {
			layout.setVisibility(View.INVISIBLE);
		}
		// closeBtn = (Button) findViewById(R.id.closeBtn);
		// mapBtn = (Button) findViewById(R.id.mapBtn);
		// mapBtn.setOnClickListener(new AppsTrackMapClickListener());
		imgBtn = (ImageButton) findViewById(R.id.imgBtn);
		imgBtnCall = (ImageButton) findViewById(R.id.imgBtnCall);
		imgBtnEmail = (ImageButton) findViewById(R.id.imgBtnEmail);
		imgBtn.setOnClickListener(new AppsTrackMapClickListener());
		imgBtnCall.setOnClickListener(new CallClickListener());
		imgBtnEmail.setOnClickListener(new EmailClickListener());

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		listener = new OnSharedPreferenceChangeListener() {
			public void onSharedPreferenceChanged(SharedPreferences prefs,
					String key) {
//				System.out.println("listener oncreat:key " + key);
				if (key.equals(Constants.ALERTKEY)
						|| key.equals(Constants.NOTVIB)) {
					boolean enable = prefs.getBoolean(key, true);
					writeSetChangeToDb(key);
//					System.out.println("listener write key: " + key
//							+ ",enable:" + String.valueOf(enable));
				}
			}
		};
		prefs.registerOnSharedPreferenceChangeListener(listener);
		// setNotification("notvib");
		// setNotification(Constants.ALERTKEY);
	}

	public void writeSetChangeToDb(String key) {
		double longitude = 1.1, latitude = 1.1;
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		boolean enable = prefs.getBoolean(key, true);
		ServiceDbHelper dbHelper = new ServiceDbHelper(getApplicationContext());
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		long dtMili = System.currentTimeMillis();
		SimpleDateFormat sdfDate = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.SSS");// dd/MM/yyyy
		Date now = new Date();
		String date = sdfDate.format(now);
		LocationGet locationGet = new LocationGet();
		Location location = locationGet.getStaticLocation();
		if (location != null) {
			longitude = location.getLongitude();
			latitude = location.getLatitude();
			SimpleDateFormat sdfDate2;
			sdfDate2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");// dd/MM/yyyy
		}
		OperationSetRecord record = new OperationSetRecord(key,
				String.valueOf(enable), date, dtMili, longitude, latitude);
		OperationSetDao dao = new OperationSetDao(db);
		dao.save(record);
		AppendGroupUserID append = new AppendGroupUserID(
				ApplicationForegroundActivity.this, db,
				DataConstants.OPERATIONSET_TABLE);
		append.save();
		//not to file
//		readSetFromDb(dao, db);
		db.close();
	}

	public void readSetFromDb(OperationSetDao dao, SQLiteDatabase db) {
		StringBuffer strbuf = new StringBuffer();
		long flag=new PreferenceIDTableRead(ApplicationForegroundActivity.this, DataConstants.OPERATIONSET_TABLE).getIDMax();
		List<OperationSetRecord> listRecord = dao.getAll(flag);
		Iterator<OperationSetRecord> iteratorAll = listRecord.iterator();
		while (iteratorAll.hasNext()) {
			OperationSetRecord r = iteratorAll.next();
			strbuf.append(String.valueOf(r.getId()) + "," + r.getOperation()
					+ "," + r.getEnable() + "," + r.getDate() + ","
					+ r.getMiliSeconds() + ","
					+ String.valueOf(r.getLogitude()) + ","
					+ String.valueOf(r.getLatitude()) + "," + "\n");
		}
		if (strbuf != null) {
			long max=listRecord.get(listRecord.size()-1).getId();
			long min=listRecord.get(0).getId();
			if(max < listRecord.get(0).getId()){
				min=max;
				max=listRecord.get(0).getId();
			}
			new PreferenceIDTableRead(ApplicationForegroundActivity.this, DataConstants.OPERATIONSET_TABLE).setIDMax(max);
			AppendGroupUserID getappend = new AppendGroupUserID(
					ApplicationForegroundActivity.this, db,
					DataConstants.OPERATIONSET_TABLE);
			String str="group user id\n";
			if (min < 2) {
				// strBufInstalled.append(str);
				str = getappend.getStringForDBRead();
				strbuf.insert(0, str);
			}
//			String str = getappend.getStringForDBRead();
//			if (str != null) {
//				strbuf.append(str);
//			}
//			strbuf.append(Constants.DBREADFLAG);
			writeDbReadOperation(strbuf);
		}
	}

	public void writeDbReadOperation(StringBuffer strBufAppUsage) {
		// Log.i("LOG","WRITEAPPTOFILE");
		if (FileUtil.isExternalStorageWritable()) {
			// File dir = FileUtil.getExternalFilesDirAllApiLevels(this
			// .getPackageName());
			// write in the android dir not the pkg dir
			File dir = FileUtil.getExternalDirAn();
			File file = new File(dir, "DbReadOperationSet.txt");
			try {
				// synchronized (DATA_LOCK)
				{
					if (!file.exists())
						file.createNewFile(); // ok if returns false, overwrite
					if ((file != null) && file.canWrite()) {
						Writer out = new BufferedWriter(new FileWriter(file,
								Constants.DBREADFILE), 1024);
						out.write(strBufAppUsage.toString());
						out.close();
						// result = true;
					}
				}
			} catch (IOException e) {
				Log.i("Log",
						"Error appending string data to file " + e.getMessage());
			}
		} else {
			Log.i("LOG", "EXTERNAL NOT WRITABLE");
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
//		System.out.println("RESUME NOTIFY VIB:"
//				+ String.valueOf(prefs.getBoolean(Constants.NOTVIB, true))
//				+ ",aler:" + String.valueOf(Constants.ALERTKEY));
		if (new InitGroupUserID(ApplicationForegroundActivity.this).initialID()) {
			startActivity(new Intent(ApplicationForegroundActivity.this,
					SetUpGroupUser.class));
		} else {
			boolean delayok = checkSevenDaysDelay();
			if (delayok) {
				layout.setVisibility(View.VISIBLE);
			}
			Display display = getWindowManager().getDefaultDisplay();
			Point size = new Point();
			int height = display.getHeight();
			int width = display.getWidth();
			int flag = 0;
			if (height > 1000) {
				// flag = 1;
				final BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 4;
				imgBtnCall.setImageResource(R.drawable.call);
				imgBtn.setImageResource(R.drawable.trackmaps);
				imgBtnEmail.setImageResource(R.drawable.sms);
			}

			else if (height > 720) {
				flag = 2;
				final BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 4;
				imgBtnCall.setImageResource(R.drawable.call);
				imgBtn.setImageResource(R.drawable.trackmaps);
				imgBtnEmail.setImageResource(R.drawable.sms);
			} else {
				flag = 3;
				final BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 2;
				Bitmap iconCall = BitmapFactory.decodeResource(getResources(),
						R.drawable.call, options);
				imgBtnCall.setImageBitmap(iconCall);
				final BitmapFactory.Options options2 = new BitmapFactory.Options();
				options2.inSampleSize = 2;
				Bitmap icon = BitmapFactory.decodeResource(getResources(),
						R.drawable.trackmaps, options2);
				imgBtn.setImageBitmap(icon);
				final BitmapFactory.Options options3 = new BitmapFactory.Options();
				options3.inSampleSize = 2;
				Bitmap iconSms = BitmapFactory.decodeResource(getResources(),
						R.drawable.sms, options2);
				imgBtnEmail.setImageBitmap(iconSms);
			}

		}
	}

	public boolean checkSevenDaysDelay() {
		boolean result = false;
		long starttime = new GetSharePrefer(ApplicationForegroundActivity.this)
				.getStartDate();
		long nowtime = System.currentTimeMillis();
		if (starttime == 0) {
			if (!new InitGroupUserID(ApplicationForegroundActivity.this)
					.initialID()) {
				new GetSharePrefer(ApplicationForegroundActivity.this)
						.setStartDate(System.currentTimeMillis());
			}
			result = false;
			return result;
		}
		long delaytime = (nowtime - starttime) / 1000;
		long sevenDaysSec = (long) (DataConstants.DELAYDATE * DataConstants.ONEDAYSECONDS);
		if (delaytime > sevenDaysSec)
			result = true;
		FormatTime format = new FormatTime();
//		System.out.println("result:" + result + "delaytime" + delaytime
//				+ "sevenDaysSec" + sevenDaysSec + "activitystarttime:"
//				+ starttime + ",strstarttime:"
//				+ format.getStringYMDHMDate(starttime) + ",nowtime:" + nowtime
//				+ ",strnowtime:" + format.getStringYMDHMDate(nowtime)
//				+ ",delay:" + DataConstants.DELAYDATE);
		return result;
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
		}

	}

	class EmailClickListener implements View.OnClickListener {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			// sendSMS();
			// sendEmail();
			shareToGMail(new String[] { "androidrecruit@gmail.com" },
					"Feedback", "Hi Investigators,\n");
		}

	}

	public void shareToGMail(String[] email, String subject, String content) {
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.putExtra(Intent.EXTRA_EMAIL, email);
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
		emailIntent.setType("plain/text");
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, content);
		final PackageManager pm = getPackageManager();
		final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent,
				0);
		ResolveInfo best = null;
		for (final ResolveInfo info : matches)
			if (info.activityInfo.packageName.endsWith(".gm")
					|| info.activityInfo.name.toLowerCase().contains("gmail"))
				best = info;
		// System.out.println(String.valueOf(best));
		// only gmail
		if (best != null)
			emailIntent.setClassName(best.activityInfo.packageName,
					best.activityInfo.name);
		startActivity(emailIntent);
	}

	public void sendEmail() {
		String to = Constants.emailAddress;
		String subject = "Feedback ";
		String message = "Hi Investigators,";

		Intent email = new Intent(Intent.ACTION_SEND);
		email.putExtra(Intent.EXTRA_EMAIL, new String[] { to });
		// email.putExtra(Intent.EXTRA_CC, new String[]{ to});
		// email.putExtra(Intent.EXTRA_BCC, new String[]{to});
		email.putExtra(Intent.EXTRA_SUBJECT, subject);
		email.putExtra(Intent.EXTRA_TEXT, message);

		// need this to prompts email client only
		// email.setType("message/rfc822");
		email.setType("text/plain");
		try {
			startActivity(Intent.createChooser(email,
					"Choose an Email client, we recommend Gmail:"));
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(ApplicationForegroundActivity.this,
					"There are no email clients installed.", Toast.LENGTH_SHORT)
					.show();
		}
	}

	public void sendSMS() {
		// SmsManager smsManager = SmsManager.getDefault();
		// Bundle bundle = getIntent().getExtras();
		// String phoneNum= bundle.getString(Constants.PhoneNum);
		// smsManager.sendTextMessage(phoneNum, null, "HI", null, null);
		String number = Constants.SMSNum; // The number on which you want to
											// send SMS
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms",
				number, null)));
		// startActivity(new
		// Intent(ApplicationForegroundActivity.this,SendSms.class));
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