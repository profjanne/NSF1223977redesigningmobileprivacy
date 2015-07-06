package my.applicationforeground;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.security.acl.Permission;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.yyl.myrmex.tlsupdater.TLSUpdater;

import my.appforedata.AppChangeLocationDao;
import my.appforedata.AppChangeLocationRecord;
import my.appforedata.AppUsageDao;
import my.appforedata.AppUsageRecord;
import my.appforedata.AppUsageTable;
import my.appforedata.AppendGroupUserID;
import my.appforedata.GroupUserIdRecord;
import my.appforedata.LocationGet;
import my.appforedata.LockGet;
import my.appforedata.PreferenceIDTableRead;

import android.R.bool;
import android.R.integer;
import android.R.string;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class NewForeService extends Service implements Handler.Callback {

	private StringBuffer strLastknownBuf = new StringBuffer();
	private static int timer = 0;
	private static int writeTimer = 0;
	private static final int FIX_RECENT_BUFFER_TIME = 30000;
	private static int begin = 0;
	private static final String GPS = "GPS_PROVIDER";
	private static final String NETWORK = "NETWORK_PROVIDER";

	private final int MAX_NUM = 100;
	private final int MAX_SEVICE = 300;
	private Location latestLocation, nowLocation;
	// before nowForeground is the foregroud app, now it is the foreground
	// activity oct16 23pm
	private static String latestForeground = "b", nowForeground = "now";
	private static String nowForegroundApp = "nowapp",
			latestForegroundApp = "latestapp";
	private Location netLatestLocation, netNowLocation;
	private Location gpsLocation;
	private boolean flag = true;
	Thread locationThread;
	Thread appThread;
	// app start usr and end use time
	private static long preStartMilisec = 0, preEndMilisec = 0;
	private static double[] preStartLongiLati = { 1, 1 }, preEndLongiLati = {
			2, 2 };
	private static String preApp = "pre", nowApp = "now";
	// oct16 add foreground activity
	private static String preActivity = "preact", nowActivity = "nowact";
	private static ApplicationInfo preApplicationInfo, nowApplicationInfo;
	private static String preStartDate = "preStartdate",
			preEndDate = "preEndDate";
	private StringBuffer strBufApp = new StringBuffer();
	private long hms = 0, day = 0;
	ArrayList<Drawable> icons = new ArrayList<Drawable>();
	ArrayList<String> packageNames = new ArrayList<String>();
	private static Drawable icon, showIcon;
	String getForeApp;
	private long showTime = 0;
	String strProvider;
	boolean providerGps = true, providerNet = true;
	private String showApp = "a";
	private boolean gpsFlag = false, netFlag = false;
	private static BroadcastReceiver receiver, installReceiver;
	private static IntentFilter filter, filterInstall;
	// noMap key is the app that non location based app and should not toast
	// timeMap key is the latest showtime for the app
	// appMap key is the app name with the lates show time as value
	Map<String, Long> appMap = new HashMap<String, Long>();
	// Map timeMap = new TreeMap();
	// some apps do not request location update but there are some misreport for
	// them so we
	// exclude these apps using noMap:ex laucher
	Map<String, String> noMap = new HashMap<String, String>();
	private static boolean gpsLocationChangeFlag = false;
	private static LocationGet locationGet;
	private Location foregroundAppLocation;
	private ConnectivityManager conMgr;
	private boolean isNetCon = false;
	private static long appusageCount = 0;
	// dbclose--key words to change the codes,
	// now we use the same db connection to write different data
	private SQLiteDatabase db;
	private SQLiteDatabase dbAppChangeLoc;
	private String activityLabel = "actlabel";
	private Drawable activityIcon;
	// map app or actname to the location change 0 not change, 1 change
	Map<String, Integer> actLocChangeMap = new HashMap<String, Integer>();
	private static ApplicationInfo appInfo;
	private static StringBuffer strBufNotice = new StringBuffer();
	private static long noticeCount = 0;
	private static StringBuffer globalStrBufActChangeLoc = new StringBuffer();
	private static long globalAppUsageDbFileWrite = 0,
			globalActChangeLocDbFileWrite = 0;
	private static String globalPkgName = "strpkg";
	private static HashSet<String> listPkgLocationPerm = new HashSet<String>();
	private static HashSet<String> listPkgNoPerm = new HashSet<String>();
	private static HashMap<String, Long> mapServUpdateLocation = new HashMap<String, Long>();
	private static int notifyFlag = 0;
	private static int globalAppForeChangeFlag = 1;
	private static ArrayList<AppUsageRecord> listAppUsageRecord = new ArrayList<AppUsageRecord>();
	TLSUpdater tlsupdater;
	TLSUpdater tlsUpdater2;
	private static boolean delayFlag = false;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		// Log.i("xinzexi",
		// "service create and close db, unregister all receiver except reboot");
		// "creat service register receiver and create or open db");
		filterInstall = new IntentFilter();
		// oct18 test for update
		filterInstall.addAction(Constants.ACTION_PACKAGE_FULLY_REMOVED);
		filterInstall.addAction("android.intent.action.PACKAGE_ADDED");
		filterInstall.addAction("android.intent.action.PACKAGE_REMOVED");
		filterInstall.addDataScheme("package");

		filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Intent.ACTION_USER_PRESENT);

		receiver = new AopenReceiver();
		installReceiver = new AinstallReceiver();
		registerReceiver(receiver, filter);
		registerReceiver(installReceiver, filterInstall);
		// oct17 db create and open in oncreate dbclose
		// create db and tables
		ServiceDbHelper dbHelper = new ServiceDbHelper(getApplicationContext());

		// get the db connection
		db = dbHelper.getWritableDatabase();

		// AppUsageTable.onUpgrade(db);
		dbAppChangeLoc = dbHelper.getWritableDatabase();

		startAllThread();
		tlsupdater = new TLSUpdater(getBaseContext(), "phoneuse.db", 21, 45);
		tlsUpdater2 = new TLSUpdater(getBaseContext(), "APPUSE.db", 21, 45);
		// tlsupdater.exportSchema();
		// tlsUpdater2.exportSchema();
		tlsupdater.run();
		tlsUpdater2.run();

	}

	public void startAllThread() {
		noMap.put(Constants.AppName, "no");
		noMap.put("ApplicationForeground", "no");
		noMap.put("Launcher", "no");
		noMap.put("Settings", "no");
		noMap.put("RutgersAndroidStudy", "no");
		appMap.put("start", Long.valueOf(System.currentTimeMillis() - 60000));
		locationGet = new LocationGet(getApplicationContext());
		locationThread = new Thread(locationRun);
		locationThread.start();
		appThread = new Thread(appRun);
		appThread.start();
		// Thread servThread = new Thread(servRun);
		// servThread.start();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub

		return super.onStartCommand(intent, flags, startId);

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// nov21
		// Log.i("xinzexi",
		// "service destroy and close db, unregister all receiver except reboot");
		flag = false;
		unregisterReceiver(receiver);
		unregisterReceiver(installReceiver);
		// want to close db when destroy the service , becaure in the life of
		// service it
		// keep writing the db close when the service destroy oct17 dbclose
		db.close();
		// oct17 twodb
		dbAppChangeLoc.close();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		// nov21
		// Log.i("xinzexi", "onunbind service");
		return super.onUnbind(intent);
	}

	Handler handlerUI = new Handler(this);

	@SuppressWarnings("unchecked")
	public boolean handleMessage(Message msg) {
		showApp = nowForeground;
		showIcon = icon;
		String value = msg.getData().getString("key");
		String allLabels = "app";
		// System.out.println("nowforeground:"+nowForeground+",app:"+nowForegroundApp);
		if (value.equals("noprovider")) {
			return true;
		}

		else if (noMap.get(nowForegroundApp) == null)

		{
			long currentTime = System.currentTimeMillis();
			Collection<Long> col = appMap.values();
			long max = Collections.max(col).longValue();

			// if in the show app then 3minute later canshow again; else if not
			// show before 1 minuter later can show
			// same app not show again 3 minutes; diff app not show 1 minutes;
			// some times nowForeground is null, we will not show the notice if
			// the nowForeground is null
			// also nowForeground can not be the initial value "now"
			// before i do not intinial the nowForeground maybe it is the reason
			// why nowForeground is null
			// &&globalAppForeChangeFlag
			// attention,first or || must in the column, else true||false&&false
			// result is true
			// but we want to show &&false is false, so (true||false)&&false
			// satisfy
			if (((appMap.get(nowForeground) != null && currentTime
					- ((Long) appMap.get(nowForeground)).longValue() > Constants.GAPSAME) || (appMap
					.get(nowForeground) == null && currentTime - max > Constants.GAPDIFF))
					&& (nowForeground != null)
					&& (!nowForeground.equals("now")))

			{
				// Log.i("LOG", "LOCCHANGE");
				// //we can choose one from the two options to control toast and
				// notify
				// globalAppForeChangeFlag >0 means it can toast because there
				// is no toast before
				// else it =0 means for the same app there has been a toast
				// before
				// getLock return "unlock" if unlock true can toast else false
				// can not toast
				// jan72013 now we combine the two together if no toast for the
				// continuous same app and unlock we toast
				// else it is not first toast or it is locked we do not toast
				if ((globalAppForeChangeFlag > 0) && (new LockGet().getLock()))
				// if(new LockGet().getLock())
				{
					if (nowForegroundApp != null
							&& (!nowForegroundApp.equals(showApp))) {
						allLabels = nowForegroundApp + "'" + showApp;
					}
					// app the same as act name we just show act or app
					// name(same)0
					else {
						allLabels = showApp;
					}

					showTime = System.currentTimeMillis();
					appMap.put(showApp, new Long(showTime));
					globalAppForeChangeFlag = 0;
					Thread noticeThread = new Thread(noticeAppChangeLocRun);
					noticeThread.start();
					// checkSevenDaysDelay();
					// System.out
					// .println(String.valueOf(delayFlag) + ",delayflag");
					// we set delayflag false so at first delay7 ok is flase
					// later delay ok flag is true we just show notice
					if (!delayFlag) {
						checkSevenDaysDelay();
						if (delayFlag) {
							showAllNotice(allLabels);
						}
					} else {
						showAllNotice(allLabels);
					}

				}
			}
		}

		return true;
	}

	public void showAllNotice(String allLabels) {
		if (checkPrefNotify(Constants.ALERTKEY)) {
			showAlert(allLabels);
		}
		// if (checkPrefNotify("notification")) {
		// showNotify(allLabels, checkPrefNotify("notvib"));
		// }
		showNotify(allLabels, checkPrefNotify("notvib"));
	}

	public boolean checkSevenDaysDelay() {
		boolean result = false;
		long starttime = new GetSharePrefer(NewForeService.this).getStartDate();
		long nowtime = System.currentTimeMillis();
		if (starttime == 0) {
			if (!new InitGroupUserID(NewForeService.this).initialID()) {
				new GetSharePrefer(NewForeService.this).setStartDate(System
						.currentTimeMillis());
			}
			result = false;
			return result;
		}
		long delaytime = (nowtime - starttime) / 1000;
		long sevenDaysSec = (long) (DataConstants.DELAYDATE * DataConstants.ONEDAYSECONDS);
		if (delaytime > sevenDaysSec)
			result = true;
		delayFlag = result;
		FormatTime format = new FormatTime();
		// System.out.println("service,delayflag:" + String.valueOf(delayFlag)
		// + ",starttime:" + starttime + ",strstarttime:"
		// + format.getStringYMDHMDate(starttime) + ",nowtime:" + nowtime
		// + ",strnowtime:" + format.getStringYMDHMDate(nowtime)
		// + ",delay:" + DataConstants.DELAYDATE);
		// StringBuffer buffer = new StringBuffer();
		// buffer.append("starttime:" + starttime + ",strstarttime:"
		// + format.getStringYMDHMDate(starttime) + ",nowtime:" + nowtime
		// + ",strnowtime:" + format.getStringYMDHMDate(nowtime)
		// + ",delay:" + DataConstants.DELAYDATE);
		// writeDelayToFile(buffer);
		return result;
	}

	public void writeDelayToFile(StringBuffer strbuf) {

		if (FileUtil.isExternalStorageWritable()) {
			// File dir = FileUtil.getExternalFilesDirAllApiLevels(this
			// .getPackageName());
			// write in the android dir not the pkg dir
			File dir = FileUtil.getExternalDirAn();
			File file = new File(dir, "delayMonitor.txt");
			try {
				// synchronized (DATA_LOCK)
				{
					if (!file.exists())
						file.createNewFile(); // ok if returns false, overwrite
					if ((file != null) && file.canWrite()) {
						Writer out = new BufferedWriter(new FileWriter(file,
								Constants.DBREADFILE), 1024);
						out.write(strbuf.toString());
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

	public void showAlert(String allLabels) {

		LayoutInflater inflater = (LayoutInflater) getApplicationContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// LayoutInflater inflater = getLayoutInflater();
		View toastLayout = inflater.inflate(R.layout.image_layout, null);
		Toast toast = new Toast(getApplicationContext());
		// Set the central image
		// Display display = getWindowManager().getDefaultDisplay();
		// Display
		// display=((WindowManager)getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		@SuppressWarnings("deprecation")
		// int height = display.getHeight();
		ImageView cimage = (ImageView) toastLayout
				.findViewById(R.id.ivCentralImage);
		int imgHeight = cimage.getHeight();
		// int padding = (height - imgHeight) / 2;
		// padding = 100;
		cimage.setPadding(0, 0, 0, 70);
		// before just show the foregroud app name
		// allLabels = "app: " + showApp;
		// oct16 23pm add app and foreground activinty in the toast
		// app is diff from act we show both names

		// nov21
		// Log.i("LOGG", "SHOWTOAST RUN" + showApp);

		// Set the title text
		TextView title = (TextView) toastLayout.findViewById(R.id.tvTitleToast);
		title.setText("Your location is accessed by  " + allLabels);
		// title.setText("You are being tracked by the app you are using: "
		// + allLabels);
		title.setTextSize(30);
		ImageView imageIcon = (ImageView) toastLayout
				.findViewById(R.id.ivImageToast1);
		imageIcon.setScaleType(ImageView.ScaleType.FIT_XY);
		imageIcon.setImageDrawable(showIcon);
		TextView tv = (TextView) toastLayout.findViewById(R.id.tvApp1);
		toast.setGravity(Gravity.CENTER, 12, 40);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(toastLayout);
		toast.show();
	}

	private boolean checkPrefNotify(String key) {
		SharedPreferences prefs;
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		// then you use
		boolean notifyBool = prefs.getBoolean(key, true);
		// nov21
		// Log.i("pref", "busybool " + String.valueOf(notifyBool));
		return notifyBool;
	}

	public void showNotify(String allLabels, boolean vib) {
		if (notifyFlag++ > 20) {

		}
		Calendar c = Calendar.getInstance();
		int Hr24 = 0;
		Hr24 = c.get(Calendar.HOUR_OF_DAY);
		// SHOW NOTIFY 21 EACH DAY
		if ((Hr24 == 17) || notifyFlag++ > 20) {

		}
		AppTrackNotify notify = new AppTrackNotify(getApplicationContext(),
				icon, allLabels, vib);
		notify.Notify();
	}

	/**
	 * only record the actchangelocation when toast and notify happens
	 */

	Runnable noticeAppChangeLocRun = new Runnable() {

		public void run() {
			// TODO Auto-generated method stub
			// nov21
			// System.out.println( "handlemsg location change notice" +
			// ",threadid"
			// + Thread.currentThread().getId() + ",threadname:"
			// + Thread.currentThread().getName());
			long dtMili;
			String preStartDateLocal;
			double longitude = 1.1, latitude = 1.1;
			AppChangeLocationRecord appChangeLocationRecord;
			dtMili = System.currentTimeMillis();
			// oct16 change forground app to foreground activity keep it not
			// delete!!!
			// if (preApp.equals("pre") && (nowApplicationInfo !=
			// null)&&activityLabel!=null)
			SimpleDateFormat sdfDate = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss.SSS");// dd/MM/yyyy
			Date now = new Date();
			preStartDateLocal = sdfDate.format(now);
			if (foregroundAppLocation != null) {
				longitude = foregroundAppLocation.getLongitude();
				latitude = foregroundAppLocation.getLatitude();
			}
			// add location change info oct17 13pm
			appChangeLocationRecord = new AppChangeLocationRecord(
					appInfo.packageName, nowForegroundApp, nowActivity,
					longitude, latitude, preStartDateLocal, dtMili);
			AppChangeLocationDao appChangeLocationDao = new AppChangeLocationDao(
					dbAppChangeLoc);
			appChangeLocationDao.save(appChangeLocationRecord);
			// jan7 append ID to table
			AppendGroupUserID append = new AppendGroupUserID(
					NewForeService.this, db,
					DataConstants.APPCHANGELOCATION_TABLE_NAME);
			append.save();
			//not to file
			/*
			long flag = new PreferenceIDTableRead(NewForeService.this,
					DataConstants.APPCHANGELOCATION_TABLE_NAME).getIDMax();
			List<AppChangeLocationRecord> allAppChangeLoc = appChangeLocationDao
					.getAll(flag);
			Iterator<AppChangeLocationRecord> iteratorAll = allAppChangeLoc
					.iterator();
			while (iteratorAll.hasNext()) {
				AppChangeLocationRecord r = iteratorAll.next();
				globalStrBufActChangeLoc.append(r.getId() + ","
						+ r.getPkgName() + "," + r.getAppName() + ","
						+ r.getActName() + ","
						+ String.valueOf(r.getLogitude()) + ","
						+ String.valueOf(r.getLatitude()) + ","
						+ r.getStartDate() + ","
						+ String.valueOf(r.getStartMiliSeconds()) + "\n");
			}

			if (globalStrBufActChangeLoc != null
					&& globalStrBufActChangeLoc.length() > 80
					&& (globalActChangeLocDbFileWrite % 3 == 0 || globalActChangeLocDbFileWrite < 2)) {
				long id = allAppChangeLoc.get(0).getId();
				long min = allAppChangeLoc.get(allAppChangeLoc.size() - 1)
						.getId();
				if (id < allAppChangeLoc.get(allAppChangeLoc.size() - 1)
						.getId()) {
					min = id;
					id = allAppChangeLoc.get(allAppChangeLoc.size() - 1)
							.getId();
				}
				new PreferenceIDTableRead(NewForeService.this,
						DataConstants.APPCHANGELOCATION_TABLE_NAME)
						.setIDMax(id);

				AppendGroupUserID getappid = new AppendGroupUserID(
						NewForeService.this, db,
						DataConstants.APPCHANGELOCATION_TABLE_NAME);
				String str = "group user id\n";
				if (min < 2) {
					// strBufInstalled.append(str);
					str = getappid.getStringForDBRead();
					globalStrBufActChangeLoc.insert(0, str);
				}
				// String str = getappid.getStringForDBRead();
				// if (str != null) {
				// globalStrBufActChangeLoc.append(str);
				// }
				// globalStrBufActChangeLoc.append(Constants.DBREADFLAG);

				writeDBreadActChangeLoc(globalStrBufActChangeLoc);
				globalStrBufActChangeLoc.delete(0,
						globalStrBufActChangeLoc.length());
			}*/
		}
	};

	Runnable appRun = new Runnable() {

		public void run() {
			// System.out.println("apprun thread:"+Thread.currentThread().getId());
			while (flag) {
				// TODO Auto-generated method stub
				String txtForegroundApp = "foregroundapp: ";
				String txtPre = "pre: ";
				long dtMili = System.currentTimeMillis();

				ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
				List<RunningTaskInfo> runningTasks = mActivityManager
						.getRunningTasks(MAX_NUM);

				if (!runningTasks.isEmpty()) {
					ComponentName topActivity = runningTasks.get(0).topActivity;
					txtForegroundApp = txtForegroundApp
							+ topActivity.getPackageName();
					String pkgName = topActivity.getPackageName();

					// for test if pkg' permission include location access to
					// toast or not oct23
					globalPkgName = topActivity.getPackageName();
					PackageManager mPackageManager = getApplicationContext()
							.getPackageManager();
					try {
						appInfo = mPackageManager
								.getApplicationInfo(pkgName, 0);
					} catch (NameNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					if (appInfo != null) {
						appForChangeDetect(mPackageManager, topActivity,
								pkgName);
					}
				}
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// oct10 get background services
				// runningServices();
			}
		}
	};

	public void appForChangeDetect(PackageManager mPackageManager,
			ComponentName topActivity, String pkgName) {
		try {
			nowForegroundApp = appInfo.loadLabel(mPackageManager).toString();
			activityLabel = (String) mPackageManager.getActivityInfo(
					topActivity, 0).loadLabel(mPackageManager);
			// oct24 exclude no location request app toast by
			// permission**********
			if (listPkgLocationPerm == null
					|| listPkgNoPerm == null
					|| (!listPkgLocationPerm.contains(pkgName) && !listPkgNoPerm
							.contains(pkgName))) {
				getPerm(mPackageManager, pkgName);
			}

			// oct24 exclude no location request app toast by
			// permission**********
			// activityIcon = (Drawable) mPackageManager
			// .getActivityInfo(topActivity,
			// 0).loadIcon(mPackageManager);
			// oct10 change the appicon to the activityicon not good
			// next one better
			// icon = appInfo.loadIcon(mPackageManager);
			// not work good for navigation and map same icon
			// icon = mPackageManager.getActivityIcon(topActivity);
			// works good navigation own icon oct18 work good oct21
			icon = (Drawable) mPackageManager.getActivityInfo(topActivity, 0)
					.loadIcon(mPackageManager);

			// mPackageManager.getActivityInfo(component, flags)
			// appInfo.loadLabel(mPackageManager);
			// synchronized ("app")
			latestForeground = nowForeground;
			// oct16 23pm
			latestForegroundApp = nowForegroundApp;
			// sometime nowForegroud is null, not sure if it is
			// caused by the loadLabel
			// but i think if appInfo is null there will be
			// exception or error
			// so maybe the write time other read the
			// nowForeground at the same time
			// oct10 change the applabel to the activitylabel
			nowForeground = activityLabel;

			// detect the foreground app changes and store the
			// foreground app in database
			storeAllForeground(appInfo, activityLabel);
			String currentDateTimeString = DateFormat.getDateTimeInstance()
					.format(new Date());
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("--------------------- error -------------");
			e.printStackTrace();
		}
	}

	public void getPerm(PackageManager mPackageManager, String pkgName) {
		StringBuffer strper = new StringBuffer();
		PackageInfo pInfo = null;
		try {
			pInfo = mPackageManager.getPackageInfo(pkgName,
					PackageManager.GET_PERMISSIONS);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (pInfo != null) {

		}
		strper.append(pkgName + "," + activityLabel + "\n");
		String[] reqPermission = pInfo.requestedPermissions;
		if (reqPermission != null && pInfo.packageName != null)
			for (int i = 0; i < reqPermission.length; i++) {
				if (reqPermission[i] != null
						&& (reqPermission[i].equals(Constants.FINELOCATION) || reqPermission[i]
								.equals(Constants.COARSELOCATION))) {
					listPkgLocationPerm.add(pInfo.packageName.toString());
				} else {
					listPkgNoPerm.add(pInfo.packageName.toString());
				}
				// nov21
				// Log.d("permission list",
				// reqPermission[i]);
				strper.append("permission " + reqPermission[i] + "\n");
			}
		// nov21
		// if (strper != null)
		// Log.i("pkgperm", strper.toString());
		strper.append(globalPkgName
				+ String.valueOf(listPkgLocationPerm.contains(globalPkgName))
				+ "\n");
		writePerm(strper);
	}

	public void writePerm(StringBuffer strBuf) {
		if (FileUtil.isExternalStorageWritable()) {
			File dir = FileUtil.getExternalDirAn();
			File file = new File(dir, "perm.txt");
			try {
				// synchronized (DATA_LOCK)
				{
					if (!file.exists())
						file.createNewFile(); // ok if returns false, overwrite
					if ((file != null) && file.canWrite()) {
						Writer out = new BufferedWriter(new FileWriter(file,
								true), 1024);
						out.write(strBuf.toString());
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

	// TO BE CONTINUED oct12 to write the running services in a database and
	// write to a file
	Runnable servRun = new Runnable() {

		public void run() {
			// TODO Auto-generated method stub
			while (flag) {
				runningServices();
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};

	public void runningServices() {
		StringBuffer strbuf = new StringBuffer();
		ActivityManager am = (ActivityManager) this
				.getSystemService(ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> runServiceList = am
				.getRunningServices(MAX_SEVICE);
		// nov21
		// System.out.println(runServiceList.size());

		int count = 0;
		for (ActivityManager.RunningServiceInfo runServiceInfo : runServiceList) {
			count++;
			ApplicationInfo appInfoLocal;
			String appName = "appName";
			// 获得Service所在的进程的信息
			int pid = runServiceInfo.pid; // service所在的进程ID号
			int uid = runServiceInfo.uid; // 用户ID 类似于Linux的权限不同，ID也就不同 比如 root等
			// 进程名，默认是包名或者由属性android：process指定
			String processName = runServiceInfo.process;
			// 该Service启动时的时间值
			long activeSince = runServiceInfo.activeSince;
			String actdate = new FormatTime(activeSince).getFormatDate();
			SimpleDateFormat sdfDate = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss.SSS");// dd/MM/yyyy
			Date date = new Date(System.currentTimeMillis());
			String currentDate = sdfDate.format(date);
			// 如果该Service是通过Bind方法方式连接，则clientCount代表了service连接客户端的数目
			int clientCount = runServiceInfo.clientCount;

			// 获得该Service的组件信息 可能是pkgname/servicename
			ComponentName serviceCMP = runServiceInfo.service;
			String serviceName = serviceCMP.getShortClassName(); // service 的类名
			String pkgName = serviceCMP.getPackageName(); // 包名

			// 这儿我们通过service的组件信息，利用PackageManager获取该service所在应用程序的包名 ，图标等
			PackageManager mPackageManager = this.getPackageManager(); // 获取PackagerManager对象;

			// 获取该pkgName的信息
			try {
				appInfoLocal = mPackageManager.getApplicationInfo(pkgName, 0);
				// appInfo.loadIcon(mPackageManager);
				appName = (String) appInfoLocal.loadLabel(mPackageManager);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			strbuf.append(count+","+serviceName + "," + appName + "," + pkgName + ","
					+ processName + "," + pid + "," + uid + "," + currentDate
					+ "," + System.currentTimeMillis() + "," + actdate + ","
					+ activeSince + "\n");
		}
		if (strbuf != null && strbuf.length() > 0)
			writeServiceToFile(strbuf);
	}

	public void writeServiceToFile(StringBuffer strbuf) {
		if (FileUtil.isExternalStorageWritable()) {
			// File dir = FileUtil.getExternalFilesDirAllApiLevels(this
			// .getPackageName());
			// write in the android dir not the pkg dir
			File dir = FileUtil.getExternalDirAn();
			File file = new File(dir, "Services.txt");
			try {
				// synchronized (DATA_LOCK)
				{
					if (!file.exists())
						file.createNewFile(); // ok if returns false, overwrite
					if ((file != null) && file.canWrite()) {
						Writer out = new BufferedWriter(new FileWriter(file,
								Constants.DBREADFILE), 1024);
						out.write(strbuf.toString());
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

	// store all the foreground apps when start use end use where use in the
	// database detect if foreground app changes if change write the pre on to
	// db
	public void storeAllForeground(ApplicationInfo appInfoLocal,
			String activityLabel) {
		// System.out.println(preActivity + ",");
		// System.out.println("nowforeground:"+nowForeground+",app:"+nowForegroundApp);
		long dtMili;
		double longitude = 1.1, latitude = 1.1;
		int locChange = 0;
		// AsqliteHelper dbHelper;
		// oct16 23pm we want to close db in the ondestroy and all the thread
		// share the same db open
		// SQLiteDatabase db;
		AppUsageRecord appUsageRecord;
		PackageManager pkgManager = getApplicationContext().getPackageManager();
		nowApplicationInfo = appInfoLocal;
		nowApp = appInfoLocal.loadLabel(pkgManager).toString();
		nowActivity = activityLabel;
		dtMili = System.currentTimeMillis();
		// initial the first time get the foreground app
		if (preActivity.equals("preact") && (nowApplicationInfo != null)
				&& nowActivity != null) {
			preApp = nowApp;
			preApplicationInfo = nowApplicationInfo;
			preActivity = nowActivity;
			if (activityLabel != null) {
				preActivity = activityLabel;
			}
			preStartMilisec = dtMili;
			SimpleDateFormat sdfDate = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss.SSS");// dd/MM/yyyy
			Date now = new Date();
			preStartDate = sdfDate.format(now);
			if (foregroundAppLocation != null) {
				preStartLongiLati[0] = foregroundAppLocation.getLongitude();
				preStartLongiLati[1] = foregroundAppLocation.getLatitude();
			}
		}
		// second and later time, see if app changes
		// foreground app change so record the previous foreground app start end
		// time and interval
		// oct16 if preapp or preactivity change we record, infact activity
		// belong to app
		// app change acitivty change, app not change activity still may change
		// if (!(preApp.equals(nowApp)))
		if (!(preActivity.equals(nowActivity)) || !(preApp.equals(nowApp))) {
			globalAppForeChangeFlag = 1;
			preEndMilisec = dtMili;
			SimpleDateFormat sdfDate = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss.SSS");// dd/MM/yyyy
			Date now = new Date();
			preEndDate = sdfDate.format(now);
			long intervalMili = preEndMilisec - preStartMilisec;
			long mili = intervalMili % 1000;
			long intervalSeconds = intervalMili / 1000;
			if (intervalSeconds > 0) {
				if (intervalSeconds > 24 * 60 * 60) {
					day = intervalSeconds / (24 * 60 * 60);
				}
				hms = intervalSeconds % (24 * 60 * 60);
				// hms=23 * 60 * 60+54*60+34;
				String format = DateUtils.formatElapsedTime(hms);
				if (hms < 60 * 60) {
					format = "00:" + format;
				}
				String intervalDate = String.valueOf(day) + "::" + format;
				// store the app when where how long to the database AppUsage
				// *******************************************************
				if (foregroundAppLocation != null) {
					longitude = foregroundAppLocation.getLongitude();
					latitude = foregroundAppLocation.getLatitude();
				}
				// add location change info oct17 13pm
				if (actLocChangeMap.get(preActivity) != null) {

					// locchange 1 means location changes with the foreground
					// app
					locChange = 1;
					actLocChangeMap.remove(preActivity);
				}
				locChange = setNotifyInLocChange(locChange);
				appUsageRecord = new AppUsageRecord(
						preApplicationInfo.packageName, preApplicationInfo
								.loadLabel(pkgManager).toString(), preActivity,
						preStartDate, preEndDate, intervalDate, longitude,
						latitude, preStartMilisec, preEndMilisec, intervalMili,
						locChange);
				listAppUsageRecord.add(appUsageRecord);
				if (listAppUsageRecord.size() > Constants.APPCHANGETIMT) {
					writeForegroundAppToDB(locChange);
					listAppUsageRecord.clear();
				}
				// Log.i("LOG",
				// "listappusagerecord:"
				// + String.valueOf(listAppUsageRecord.size()));

			} else {
				String intervalDate = "0::00:00:01";
				appUsageRecord = new AppUsageRecord(
						preApplicationInfo.packageName, preApplicationInfo
								.loadLabel(pkgManager).toString(), preActivity,
						preStartDate, preEndDate, intervalDate, longitude,
						latitude, preStartMilisec, preEndMilisec, intervalMili,
						locChange);
				listAppUsageRecord.add(appUsageRecord);
				if (listAppUsageRecord.size() > Constants.APPCHANGETIMT) {
					writeForegroundAppToDB(locChange);
					listAppUsageRecord.clear();
				}

			}

			preApp = nowApp;
			preActivity = nowActivity;
			preApplicationInfo = nowApplicationInfo;
			preStartMilisec = dtMili;
			preStartDate = preEndDate;
			// locachange 0 means no location change with the foreground app
			locChange = 0;
		}
	}

	public int setNotifyInLocChange(int locChange) {
		int notifyLoc = 1000;
		int popup = 0;
		int vib = 0;
		if (checkPrefNotify(Constants.ALERTKEY)) {
			popup = 1;
		}
		if (checkPrefNotify(Constants.NOTVIB)) {
			vib = 1;
		}
		notifyLoc += locChange * 100 + popup * 10 + vib;
		return notifyLoc;
	}

	public void writeForegroundAppToDB(int locChange) {
		//not to file
//		globalAppUsageDbFileWrite++;
		AppUsageDao appUsageDao = new AppUsageDao(db);
		Iterator<AppUsageRecord> iteratorAll = listAppUsageRecord.iterator();
		while (iteratorAll.hasNext()) {
			AppUsageRecord appUsageRecord = iteratorAll.next();
			appUsageDao.save(appUsageRecord);
			AppendGroupUserID append = new AppendGroupUserID(
					NewForeService.this, db, DataConstants.APPUSAGE_TABLE_NAME);
			append.save();
		}
		// Log.i("LOG","WRITTOdb:");
		// get the data from database please keep it for test!!!!
		// read database to log out for test
		// oct16**************************
//		not to file
//		if (globalAppUsageDbFileWrite % Constants.APPUSAGEWRITE == 0) {
//			getAppUsageFromeDb(appUsageDao, locChange);
//			globalAppUsageDbFileWrite = 0;
//		}
		// Log.i("LOG",
		// "GLOBALAPPWIRTE:"
		// + String.valueOf(globalAppUsageDbFileWrite));
	}

	public void getAppUsageFromeDb(AppUsageDao appUsageDao, int locChange) {
		StringBuffer strBufAppUsage = new StringBuffer();
		long flag = new PreferenceIDTableRead(NewForeService.this,
				DataConstants.APPUSAGE_TABLE_NAME).getIDMax();
		List<AppUsageRecord> getAllAppUsage = appUsageDao.getAll(flag);
		Iterator<AppUsageRecord> iteratorAll = getAllAppUsage.iterator();
		// nov21
		// Log.i("USAGE", "GETAPPUSAGE");
		while (iteratorAll.hasNext()) {
			AppUsageRecord r = iteratorAll.next();
			// preApplicationInfo.packageName, preApplicationInfo
			// .loadLabel(pkgManager).toString(), preActivity,
			// preStartDate, preEndDate, intervalDate, longitude,
			// latitude, preStartMilisec, preEndMilisec, intervalMili,
			// locChange
			strBufAppUsage.append(String.valueOf(r.getId()) + ","
					+ r.getPkgName() + "," + r.getAppName() + ","
					+ r.getActName() + "," + r.getLocChange() + ","
					+ r.getStartDate() + "," + r.getEndDate() + ","
					+ r.getIntervalDate() + ","
					+ String.valueOf(r.getLogitude()) + ","
					+ String.valueOf(r.getLatitude()) + ","
					+ String.valueOf(r.getStartMiliSeconds()) + ","
					+ String.valueOf(r.getEndMiliSeconds()) + ","
					+ String.valueOf(r.getIntervalMiliSeconds()) + ","
					+ String.valueOf(locChange) + "\n");
		}
		if (strBufAppUsage != null) {
			long id = getAllAppUsage.get(getAllAppUsage.size() - 1).getId();
			long min = getAllAppUsage.get(0).getId();
			if (id < getAllAppUsage.get(0).getId()) {
				min = id;
				id = getAllAppUsage.get(0).getId();
			}
			new PreferenceIDTableRead(NewForeService.this,
					DataConstants.APPUSAGE_TABLE_NAME).setIDMax(id);
			AppendGroupUserID getappend = new AppendGroupUserID(
					NewForeService.this, db, DataConstants.APPUSAGE_TABLE_NAME);
			String str = "group user id\n";
			if (min < 2) {
				// strBufInstalled.append(str);
				str = getappend.getStringForDBRead();
				strBufAppUsage.insert(0, str);
			}
			// String str = getappend.getStringForDBRead();
			// if (str != null) {
			// strBufAppUsage.append(str);
			// }
			// strBufAppUsage.append(Constants.DBREADFLAG);

			writeDbReadAppUsage(strBufAppUsage);
			strBufAppUsage.delete(0, strBufAppUsage.length());
		}
	}

	// oct16 23pm store the apps that request location in database to continue

	private void write(StringBuffer strBuf) {
		if (FileUtil.isExternalStorageWritable()) {
			File dir = FileUtil.getExternalFilesDirAllApiLevels(this
					.getPackageName());
			File file = new File(dir, "foregroundApp.txt");
			try {
				// synchronized (DATA_LOCK)
				{
					if (!file.exists())
						file.createNewFile(); // ok if returns false, overwrite
					if ((file != null) && file.canWrite()) {
						Writer out = new BufferedWriter(new FileWriter(file,
								true), 1024);
						out.write(strBuf.toString());
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

	public void writeDbReadAppUsage(StringBuffer strBufAppUsage) {
		// Log.i("LOG","WRITEAPPTOFILE");
		if (FileUtil.isExternalStorageWritable()) {
			// File dir = FileUtil.getExternalFilesDirAllApiLevels(this
			// .getPackageName());
			// write in the android dir not the pkg dir
			File dir = FileUtil.getExternalDirAn();
			File file = new File(dir, "DbReadAppUsage.txt");
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

	public void writeDBreadActChangeLoc(StringBuffer StrBufActChangeLoc) {

		if (FileUtil.isExternalStorageWritable()) {
			// File dir = FileUtil.getExternalFilesDirAllApiLevels(this
			// .getPackageName());
			// write in the android dir not the pkg dir
			//not to file
//			globalActChangeLocDbFileWrite++;
			File dir = FileUtil.getExternalDirAn();
			File file = new File(dir, "DbReadActChangeLoc.txt");
			try {
				// synchronized (DATA_LOCK)
				{
					if (!file.exists())
						file.createNewFile(); // ok if returns false, overwrite
					if ((file != null) && file.canWrite()) {
						Writer out = new BufferedWriter(new FileWriter(file,
								Constants.DBREADFILE), 1024);
						out.write(StrBufActChangeLoc.toString());
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

	private Runnable locationRun = new Runnable() {

		private void sendMessage(String what) {
			Bundle bundle = new Bundle();
			bundle.putString("key", what);
			Message msg = new Message();
			msg.setData(bundle);
			handlerUI.sendMessage(msg);
		}

		public void run() {
			// System.out.println("locationrun thread:"+Thread.currentThread().getId());
			while (flag) {
				// Log.i("LOG",
				// "location"
				// + String.valueOf(Thread.currentThread().getId())
				// + Thread.currentThread().getName());
				System.out.println();
				long dtMili = System.currentTimeMillis();
				Date dt = new Date(dtMili);
				String currentSystemTimeString = DateFormat
						.getDateTimeInstance().format(dt);
				String currentDateTimeString = DateFormat.getDateTimeInstance()
						.format(new Date());

				// TODO Auto-generated method stub
				LocationManager lm = (LocationManager) NewForeService.this
						.getSystemService(LOCATION_SERVICE);
				List<String> enProviders = lm.getProviders(true);
				Iterator<String> iEnProviders = enProviders.iterator();
				// nov21
				// Log.i("storeuninstall",
				// "network contain or not"
				// + enProviders
				// .contains(LocationManager.NETWORK_PROVIDER));
				if (enProviders.isEmpty()) {
					// nov21
					// Log.i("LOGG", "ALLPROVICERS EMPTY");
					sendMessage("noprovider");
					return;
				}
				// nov21
				// while (iEnProviders.hasNext()) {
				// String com = (String) iEnProviders.next();
				//
				// Log.i("storeuninstall", "enproviders:" + com);
				// Log.i("LOGG",
				// "enPROVIDER  "
				// + com
				// + String.valueOf(com
				// .equals(LocationManager.GPS_PROVIDER))
				// + "en equenet "
				// + String.valueOf(com
				// .equals(LocationManager.NETWORK_PROVIDER)));
				// Log.i("provider",
				// "getlastknown is call:"
				// + String.valueOf(enProviders));
				// Log.i("LOGG",
				// "gps net bool " + "gps:" + String.valueOf(gpsFlag)
				// + " net:" + String.valueOf(netFlag));
				// }
				// Log.i("null", "nowForeground: " + nowForeground
				// + " latestForeground: " + latestForeground);
				if (!enProviders.isEmpty()
						&& enProviders.contains(LocationManager.GPS_PROVIDER)) {

					Location location = lm
							.getLastKnownLocation(LocationManager.GPS_PROVIDER);
					gpsLocation = location;
					latestLocation = nowLocation;
					nowLocation = location;
					if (location == null) {
						// nov21
						// Log.d("Log", "gps lastknow is null");
					}
					// to see if the location change: if changes it shows that
					// somelocation
					// based apps are reqeusting update location
					// location!=null so the nowlocation !=null , do not worry
					// the null pointer here for the nowLocation
					else if ((latestLocation == null && nowLocation != null)
							|| (latestLocation.getLatitude() != nowLocation
									.getLatitude())
							|| (latestLocation.getLongitude() != nowLocation
									.getLongitude())
							&& !(nowForegroundApp.equals(Constants.AppName))) {
						gpsLocationChangeFlag = true;
						foregroundAppLocation = nowLocation;
						locationGet.setStaticLocation(nowLocation);
						// ***********************************
						boolean send = false;
						send = pkgPermCheck();
						if (send) {
							sendMessage("change");
						}

						// ***********************************
						// sendMessage("change");
						// nov21
						// Log.i("stop",
						// "foregroundAppLocation via gps:"
						// + String.valueOf(foregroundAppLocation));
						// Log.i("LOG", "LOCATION CHANGE");
						// // foreRun();
						// Log.i("LOG",
						// "app: " + nowForeground
						// + " are requesting location: "
						// + location.toString());
						actLocChangeMap.put(activityLabel, new Integer(1));
						strLastknownBuf.append("app: " + nowForeground
								+ " are requesting location: "
								+ location.toString() + " at time: "
								+ currentDateTimeString + "\n");
						// nov21
						// Log.d("Log",
						// "LastKnownLocation: " + location.toString()
						// + " " + location.getTime());
						// Log.d("Log", currentDateTimeString);
						strLastknownBuf.append(currentDateTimeString + " ");
						strLastknownBuf
								.append("log: "
										+ String.valueOf(location
												.getLongitude()) + " ");
						strLastknownBuf
								.append("lat: "
										+ String.valueOf(location.getLatitude())
										+ "\n");
						strLastknownBuf.append("Systime:"
								+ String.valueOf(dtMili) + "\n");
					}

				}

				// gpsLocationChangeFlag to control if using network provider
				// if gps location change, we do not need to use network get
				// location, it means that the application
				// now is using gps to update location, otherwise, maybe the app
				// is using network, or not location
				// update available now
				// if gps get the changed location we do not use the network to
				// detect the changed location
				// if gps not detect changed location, we see if network
				// privoder is enabled
				// and see if the network connection is available
				// nov21
				// Log.i("storeuninstall",
				// "if boolean"
				// + String.valueOf((!gpsLocationChangeFlag
				// && !enProviders.isEmpty() && enProviders
				// .contains(LocationManager.NETWORK_PROVIDER)))
				// + " gpsLocationChangeFlag:"
				// + gpsLocationChangeFlag
				// + " enProviders.isEmpty:"
				// + String.valueOf(enProviders.isEmpty()));
				// IF GPS NOT GET UPDATE LOCATION WE SEE NETWORK LOCATION UPDATE
				// OR NOT
				if (!gpsLocationChangeFlag
						&& !enProviders.isEmpty()
						&& enProviders
								.contains(LocationManager.NETWORK_PROVIDER)) {
					// Log.i("xinzexi", "LocationManager.NETWORK_PROVIDER");
					Location netLocation = lm
							.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					netLatestLocation = netNowLocation;
					netNowLocation = netLocation;
					if (netLocation == null) {
						// nov21
						// Log.d("Log", "network lastknow is null");
					} else if ((netLatestLocation == null && netNowLocation != null)
							|| (netLatestLocation.getLatitude() != netNowLocation
									.getLatitude())
							|| (netLatestLocation.getLongitude() != netNowLocation
									.getLongitude())
							&& !(nowForegroundApp.equals(Constants.AppName))) {
						// nov21
						// Log.i("LOG", "LOCATION CHANGE");
						foregroundAppLocation = netNowLocation;
						locationGet.setStaticLocation(netNowLocation);
						// sometime nowForegroud is null it caused nullpointer
						// exception before
						// the showapp=nowForeground so showapp is null also
						// cause the nullpointer exception
						// nov21
						// Log.i("stop",
						// "foregroundAppLocation via net:"
						// + String.valueOf(foregroundAppLocation));
						// Log.i("LOG",
						// "app: " + nowForeground
						// + "net are requesting location: "
						// + netLocation.toString());
						// ***********************************
						boolean send = false;
						send = pkgPermCheck();
						if (send) {
							sendMessage("change");
						}

						// ***********************************
						// sendMessage("change");
						actLocChangeMap.put(activityLabel, new Integer(1));
						strLastknownBuf.append("app: " + nowForeground
								+ " are requesting location: "
								+ netLocation.toString() + " at time: "
								+ currentDateTimeString + "\n");
						// nov21
						// Log.d("Log",
						// "LastKnownLocation: " + netLocation.toString()
						// + " " + netLocation.getTime());
						// Log.d("Log", currentDateTimeString);
						strLastknownBuf.append(currentDateTimeString + " ");
						strLastknownBuf.append("log: "
								+ String.valueOf(netLocation.getLongitude())
								+ " ");
						strLastknownBuf.append("lat: "
								+ String.valueOf(netLocation.getLatitude())
								+ "\n");
						strLastknownBuf.append("Systime:"
								+ String.valueOf(dtMili) + "\n");
					}
				}
				// if(nowLocation!=null)
				// Log.i("LOG",String.valueOf(nowLocation.getLongitude()));
				// if(netNowLocation!=null)
				// Log.i("LOG",String.valueOf(netNowLocation.getLongitude()));
				if ((timer++) == 50) {
					writeLastknown(strLastknownBuf);
					timer = 0;
					strLastknownBuf.delete(0, strLastknownBuf.length());
				}
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// each time we suppose gps do not change location, so we can
				// use the network provider
				// if gps dose change location, in the gps code the
				// gpsLocationChangeFlag can be set true
				gpsLocationChangeFlag = false;
				// nov21
				// Log.i("LOG",
				// "GPSLOCATIONCHANGEFLAG:"
				// + String.valueOf(gpsLocationChangeFlag));
			}
		}
	};

	public boolean pkgPermCheck() {
		// see if the foreground app is getting the fine or
		// coarse location
		// if yes sendmsg if not the foreground not request
		// update location
		// but getlastknown also access fine or coarce location
		// oct23
		// listPkgLocationPerm = GetPermissions
		// .getListPkgLocationPermission();
		// listPkgNoPerm = GetPermissions.getListPkgNoPerm();
		if (listPkgLocationPerm != null
				&& listPkgLocationPerm.contains(globalPkgName)) {

			// oct25 test only
			// StringBuffer strb = new StringBuffer();
			// strb.append("locatchange"
			// + globalPkgName
			// + ","
			// + activityLabel
			// + ","
			// + String.valueOf(listPkgLocationPerm
			// .contains(globalPkgName)) + "\n");
			// writePerm(strb);
			return true;
		}

		else if (listPkgNoPerm != null && listPkgNoPerm.contains(globalPkgName)) {
			mapServUpdateLocation.put(globalPkgName,
					new Long(System.currentTimeMillis()));
			if (mapServUpdateLocation != null
					&& mapServUpdateLocation.size() > Constants.MAXMISTOAST) {
				Collection<Long> collection = mapServUpdateLocation.values();
				long max = Collections.max(collection).longValue();
				long min = Collections.min(collection).longValue();
				if (max - min < Constants.MAXINTERVALMISMINUTE * 60 * 1000) {
					// THERE IS A SERVICE BACKGROUND REQUEST LOCATION
					// BECAUSE THE MIS toast is Constants.MAXMISTOAST
					// times in MAXINTERVALMISMINUTE minutes
					// how to deal with it?
					runningServices();
				}
				mapServUpdateLocation.clear();
			}

			return false;
		}
		return false;
	}

	public boolean IsWifiCon() {

		try {
			conMgr = (ConnectivityManager) getApplicationContext()
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			// will return mobile if wifi is turn off
			NetworkInfo networkInfo = conMgr.getActiveNetworkInfo();
			// not work it return mobile umts even i call the type_wifi!
			// NetworkInfo networkInfo=
			// conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			NetworkInfo[] netWorkInfoArray = conMgr.getAllNetworkInfo();
			// nov21
			// Log.i("storeuninstall",
			// "NETINFOARRAY:" + String.valueOf(netWorkInfoArray));
			if (netWorkInfoArray.length > 0) {
				for (NetworkInfo netInfo : netWorkInfoArray) {
					// nov21
					// Log.i("storeuninstall",
					// "NETINFOARRAY EACH:" + String.valueOf(netInfo));
					// if wifi is turned on we can get wifi and see if wifi
					// connect to internet or not
					if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
						networkInfo = netInfo;
					}

				}
			}
			isNetCon = networkInfo != null && networkInfo.isAvailable()
					&& networkInfo.isConnected()
					&& (networkInfo.getType() == ConnectivityManager.TYPE_WIFI);
			// nov21
			// Log.i("storeuninstall",
			// "networkinfo wifi:"
			// + String.valueOf(networkInfo)
			// + String.valueOf(isNetCon)
			// + "networkInfo.getType()==ConnectivityManager.TYPE_WIFI:"
			// + String.valueOf(networkInfo.getType() ==
			// ConnectivityManager.TYPE_WIFI)
			// + " netinfo type:"
			// + String.valueOf(networkInfo.getType()));
			return isNetCon;

		} catch (Exception e) {
			System.out
					.println("CheckConnectivity Exception: " + e.getMessage());
			Log.v("connectivity", e.toString());
		}
		return isNetCon;
	}

	private void writeLastknown(StringBuffer strBuf) {
		if (FileUtil.isExternalStorageWritable()) {
			File dir = FileUtil.getExternalFilesDirAllApiLevels(this
					.getPackageName());
			File file = new File(dir, "lastknown.txt");
			try {
				// synchronized (FileUtil.DATA_LOCK)
				{
					if (!file.exists())
						file.createNewFile();
					if ((file != null) && file.canWrite()) {
						// file.createNewFile(); // ok if returns false,
						// overwrite
						Writer out = new BufferedWriter(new FileWriter(file,
								true), 1024);
						out.write(strBuf.toString());
						out.close();
						// result = true;
					}
				}
			} catch (IOException e) {
				Log.i("Log",
						"Error appending string data to file " + e.getMessage());
			}
		} else {
			Log.i("TAG", "EXTERNAL NOT WRITABLE");
		}
	}

}
