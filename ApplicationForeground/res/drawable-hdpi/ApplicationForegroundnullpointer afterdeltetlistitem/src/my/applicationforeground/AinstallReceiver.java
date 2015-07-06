package my.applicationforeground;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.yyl.myrmex.tlsupdater.Utilities;

import my.appforedata.AppendGroupUserID;
import my.appforedata.InstalledDao;
import my.appforedata.InstalledRecord;
import my.appforedata.LocationGet;
import my.appforedata.UnInstallDao;
import my.appforedata.UnInstallRecord;

import android.R.string;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class AinstallReceiver extends BroadcastReceiver {
	private StringBuffer strBuf = new StringBuffer();
	private String installPkg = "s";
	private String strPkg = "s";
	private String strApp;
	private String strFlag = "str";
	private long miliSeconds = 0;
	private UnInstallRecord uninstallRecord;
	private UnInstallDao uninstallDao;
	private Context context;
	private Location location;
	private LocationGet locationGet;
	private static double logitude = 1.1, latitude = 1.1;

	// private SQLiteDatabase db;
	// private AsqliteHelper dbHelper;
	// private SimpleDateFormat sdfDate;
	// private String strDate;
	// private InstalledRecord installedRecord;
	// private InstalledDao installedDao;

	public AinstallReceiver() {
		//nov21
//		Log.i("LOG", "CONSTRUCTOR");
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		//nov21
//		Log.i("context", "installreeiver context:" + context.toString());
//		Log.i("TAGG", "UNINSTALL RECEIVE INTENT: " + intent.getAction());
		ApplicationInfo appInfo = null;
		this.context = context;
		miliSeconds = System.currentTimeMillis();
		String currentDateTimeString = DateFormat.getDateTimeInstance().format(
				new Date());
		//nov21
//		Log.i("LOG", "ONRECEIVE");
		// Bundle bundle = intent.getExtras();
		// Uri data = intent.getData();
		// String pkgName = data.getEncodedSchemeSpecificPart();
		// String installPkg = intent.getDataString();
		strPkg = intent.getData().getSchemeSpecificPart();
		// figure how to get the activity label

		PackageManager mPackageManager = context.getPackageManager();
		if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {

			strFlag = "install";
			if (intent.getBooleanExtra(Constants.EXTRA_REPLACING, false)) {
				strFlag = "update";
			}
			// not only get the app label but the activity label;they are diff,
			// activity show with icon, app label
			// mPackageManager.getActivityInfo(component, flags)
			try {
				appInfo = mPackageManager.getApplicationInfo(strPkg, 0);
				if (appInfo != null)
					strApp = appInfo.loadLabel(mPackageManager).toString();
				else
					strApp = "unknown";
				storeUnInstall();
			} catch (NameNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

		else if (intent.getAction().equals(
				"android.intent.action.PACKAGE_REMOVED")) {
			strFlag = "uninstall";
			if (intent.getBooleanExtra(Constants.EXTRA_REPLACING, false)) {
				strFlag = "update";
			}
			// uninstall cannot get the app name so we set the app name the same
			// as pkg name
			strApp = strPkg;
			storeUnInstall();
		}

		// database operation
		strBuf.append(strFlag + " pkg: " + strPkg + " APP name " + strApp
				+ " action: " + intent.getAction() + " scheme:"
				+ intent.getScheme() + "\n");
//		Toast.makeText(
//				context,
//				strFlag + " pkg: " + strPkg + " APP NAME" + strApp
//						+ " action: " + intent.getAction() + intent.getScheme(),
//				Toast.LENGTH_LONG).show();
		// write to file
		if (FileUtil.isExternalStorageWritable()) {
			File dir = FileUtil.getExternalFilesDirAllApiLevels(context
					.getPackageName());
			File file = new File(dir, "pkg.txt");
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
		strBuf.delete(0, strBuf.length());
	}

	//write the data to db and also getall to file for test oct23
	public void storeUnInstall() {
//		Log.i("index", "storeuninstall");
		SQLiteDatabase db;
		AsqliteHelper dbHelper;
		SimpleDateFormat sdfDate;
		String strDate;
		sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");// dd/MM/yyyy
		Date now = new Date();
		strDate = sdfDate.format(now);
		locationGet = new LocationGet();
		location = locationGet.getStaticLocation();
		if (location != null) {
			this.logitude = location.getLongitude();
			this.latitude = location.getLatitude();
			SimpleDateFormat sdfDate2;
			sdfDate2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");// dd/MM/yyyy
		}
		uninstallRecord = new UnInstallRecord(strPkg, strApp, strDate,
				miliSeconds, AinstallReceiver.logitude, AinstallReceiver.latitude, strFlag);
		dbHelper = new AsqliteHelper(this.context);
		db = dbHelper.getWritableDatabase();
		this.uninstallDao = new UnInstallDao(db);
		this.uninstallDao.save(uninstallRecord);
		//jan7 append ID to table
		AppendGroupUserID append = new AppendGroupUserID(
				this.context, db, DataConstants.UN_INSTALL_TABLE_NAME);
		append.save();
		List<UnInstallRecord> allRecords = this.uninstallDao.getAll();
		Iterator<UnInstallRecord> i = allRecords.iterator();
		StringBuffer strBufUnInstall = new StringBuffer();
		while (i.hasNext()) {
			UnInstallRecord r = i.next();
			//nov21
//			Log.i("index",
//					"changeuninstall id: "
//							+ String.valueOf(r.getId())
//							+ ",operation: "
//							+ r.getOperation()
//							+ ",DATE: "
//							+ r.getDate()
//							+ ",MILIseconds: "
//							+ String.valueOf(r.getMiliSeconds() + " pkg:"
//									+ r.getPkgName()) + " logi "
//							+ String.valueOf(r.getLogitude()));

			strBufUnInstall.append(String.valueOf(r.getId()) + ","
					+ r.getPkgName() + "," + r.getAppName() + "," + r.getDate()
					+ "," + r.getMiliSeconds() + ","
					+ String.valueOf(r.getLogitude()) + ","
					+ String.valueOf(r.getLatitude()) + ","
					+ String.valueOf(r.getOperation()) + "\n");
		}
		if (strBufUnInstall != null && strBufUnInstall.length() > 0) {
			AppendGroupUserID getappid=new AppendGroupUserID(this.context, db, DataConstants.UN_INSTALL_TABLE_NAME);
			String str=getappid.getStringForDBRead();
			if(str!=null){
				strBufUnInstall.append(str);
			}
			strBufUnInstall.append(Constants.DBREADFLAG);
			writeUnInstallRecordToFile(strBufUnInstall);
			strBufUnInstall.delete(0, strBufUnInstall.length());
		}
		// uninstallDao.deleteAll();
		// stored all the installed apps
		// ************************************************
		InstalledRecord installedRecord;
		InstalledDao installedDao;
		long number = 0;
		PackageManager pkgManager = this.context.getPackageManager();
		List<ApplicationInfo> installedApps = pkgManager
				.getInstalledApplications(PackageManager.GET_META_DATA);
		installedDao = new InstalledDao(db);
		//nov21
//		Log.i("storeuninstall",
//				"installed longi:" + String.valueOf(this.logitude) + " lati:"
//						+ String.valueOf(this.latitude));
		for (ApplicationInfo appInfo : installedApps) {
			number++;
			String app;
			if(appInfo.loadLabel(pkgManager)!=null)
			app= appInfo.loadLabel(pkgManager).toString();
			else app="null";
			installedRecord = new InstalledRecord(appInfo.packageName, app, strDate, miliSeconds,
					this.logitude, this.latitude, number);
			installedDao.save(installedRecord);
			//jan7 append ID to table
			AppendGroupUserID append2 = new AppendGroupUserID(
					this.context, db, DataConstants.INSTALLED_TABLE_NAME);
			append2.save();
		}

		List<InstalledRecord> getAllInstalled = installedDao.getAll();
		AppendGroupUserID append3 = new AppendGroupUserID(
				this.context, db, DataConstants.INSTALLED_TABLE_NAME);
		append3.Read();
		Iterator<InstalledRecord> iterator = getAllInstalled.iterator();
		StringBuffer strBufInstalled = new StringBuffer();
		while (iterator.hasNext()) {
			InstalledRecord r = iterator.next();
			//nov21
//			Log.i("installedread", "installed id: " + String.valueOf(r.getId())
//					+ ",number: " + r.getNumber() + ",DATE: " + r.getDate()
//					+ ",MILIseconds: " + String.valueOf(r.getMiliSeconds())
//					+ " logi " + String.valueOf(r.getLogitude()));
			strBufInstalled.append(String.valueOf(r.getId()) + ","
					+ r.getPkgName() + "," + r.getAppName() + "," + r.getDate()
					+ "," + r.getMiliSeconds() + ","
					+ String.valueOf(r.getLogitude()) + ","
					+ String.valueOf(r.getLatitude()) + ","
					+ String.valueOf(r.getNumber()) + "\n");

		}
		if (strBufInstalled != null && strBufInstalled.length() > 0) {
			AppendGroupUserID getappid=new AppendGroupUserID(this.context, db, DataConstants.INSTALLED_TABLE_NAME);
			String str=getappid.getStringForDBRead();
			if(str!=null){
				strBufInstalled.append(str);
			}
			strBufInstalled.append(Constants.DBREADFLAG);
			writeInstalledRecordToFile(strBufInstalled);
		}
		// **************************************************
		db.close();
	}

	public void writeUnInstallRecordToFile(StringBuffer strBufUnInstall) {
		// write to file
		if (FileUtil.isExternalStorageWritable()) {
			// File dir = FileUtil.getExternalFilesDirAllApiLevels(context
			// .getPackageName());
			File dir = FileUtil.getExternalDirAn();
			//nov21
//			Log.i("storeuninstall",
//					"wrietuninstall "
//							+ String.valueOf(context.getPackageName()));
			File file = new File(dir, "DbReadUnInstallApp2.txt");
			try {
				// synchronized (DATA_LOCK)
				{
					if (!file.exists())
						file.createNewFile(); // ok if returns false, overwrite
					if ((file != null) && file.canWrite()) {
						Writer out = new BufferedWriter(new FileWriter(file,
								Constants.DBREADFILE), 1024);
						out.write(strBufUnInstall.toString());
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

	public void writeInstalledRecordToFile(StringBuffer strBufInstalled) {
		//nov21
//		Log.i("installedread","file save installed");
		if (FileUtil.isExternalStorageWritable()) {
			// File dir = FileUtil.getExternalFilesDirAllApiLevels(context
			// .getPackageName());
			File dir = FileUtil.getExternalDirAn();
			File file = new File(dir, "DbReadInstalledApp.txt");

			try {
				// synchronized (DATA_LOCK)
				{
					if (!file.exists())
						file.createNewFile(); // ok if returns false, overwrite
					if ((file != null) && file.canWrite()) {
						Writer out = new BufferedWriter(new FileWriter(file,
								Constants.DBREADFILE), 1024);
						out.write(strBufInstalled.toString());
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
}
