package my.applicationforeground;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import my.appforedata.AppChangeLocationDao;
import my.appforedata.AppChangeLocationRecord;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class AppTrackFromDb {
	private Context context;
	private List<MapAppLocItem> allListApp;
	private SQLiteDatabase db;
	private Drawable iconDef;

	public AppTrackFromDb(Context context) {
		ServiceDbHelper dbHelper = new ServiceDbHelper(context);
		this.context = context;
		db = dbHelper.getReadableDatabase();
		allListApp = new ArrayList<MapAppLocItem>();
	}

	public AppTrackFromDb(Context context, Drawable icon) {
		ServiceDbHelper dbHelper = new ServiceDbHelper(context);
		this.context = context;
		db = dbHelper.getReadableDatabase();
		allListApp = new ArrayList<MapAppLocItem>();
		this.iconDef = icon;
	}

	public List<MapAppLocItem> getData(String pkgName) {
		String app = "app";
		String pkg = "pkg";
		AppInfoItem appInfoItem = new AppInfoItem(this.context);
		// get app pkg from database store all data in a hashmap
		List<AppChangeLocationRecord> allAppChangeLoc = readDb();
		Iterator<AppChangeLocationRecord> iteratorAll = allAppChangeLoc
				.iterator();

		List<String> pkgInstalledList = new ArrayList<String>();
		if (iteratorAll.hasNext()) {
			GetInstalledApp getApp = new GetInstalledApp(this.context);
			pkgInstalledList = getApp.intalledApp();
		}

		while (iteratorAll.hasNext()) {
			AppChangeLocationRecord r = iteratorAll.next();
			Drawable icon = null;
			app = r.getAppName();
			pkg = r.getPkgName();
			
			int i = 0;
			if (pkg != null && app != null && r.getPkgName().equals(pkgName)) {
				if (pkgInstalledList.contains(r.getPkgName())) {
					icon = appInfoItem.getIcon(r.getPkgName());
				} else {
					icon = this.iconDef;
				}
				MapAppLocItem item = new MapAppLocItem();
				item.setIcon(icon);
				item.setPkgName(pkg);
				item.setAppName(app);
				item.setActName(r.getActName());

				item.setLatitude(r.getLatitude());
				item.setLogitude(r.getLogitude());
				item.setStartDate(r.getStartDate());
				item.setStartMiliSeconds(r.getStartMiliSeconds());
				// show in the maps all the apps that tracking users
				allListApp.add(item);

			}

		}
		// get icon using codes

		return allListApp;
	}

	public List<MapAppLocItem> getData() {

		String app = "app";
		String pkg = "pkg";
		AppInfoItem appInfoItem = new AppInfoItem(this.context);
		// get app pkg from database store all data in a hashmap
		List<AppChangeLocationRecord> allAppChangeLoc = readDb();
		Iterator<AppChangeLocationRecord> iteratorAll = allAppChangeLoc
				.iterator();

		List<String> pkgInstalledList = new ArrayList<String>();
		if (iteratorAll.hasNext()) {
			GetInstalledApp getApp = new GetInstalledApp(this.context);
			pkgInstalledList = getApp.intalledApp();
		}
		while (iteratorAll.hasNext()) {
			AppChangeLocationRecord r = iteratorAll.next();
			Drawable icon = null;
			app = r.getAppName();
			pkg = r.getPkgName();
			if (pkgInstalledList.contains(r.getPkgName())) {
				icon = appInfoItem.getIcon(r.getPkgName());
			} else {
				icon = this.iconDef;
			}

			MapAppLocItem item = new MapAppLocItem();
			item.setIcon(icon);
			item.setPkgName(pkg);
			item.setAppName(app);
			item.setActName(r.getActName());

			item.setLatitude(r.getLatitude());
			item.setLogitude(r.getLogitude());
			item.setStartDate(r.getStartDate());
			item.setStartMiliSeconds(r.getStartMiliSeconds());
			// show in the maps all the apps that tracking users
			allListApp.add(item);

		}
		// get icon using codes

		return allListApp;
	}

	public List<AppChangeLocationRecord> readDb() {
		AppChangeLocationDao appChangeLocationDao = new AppChangeLocationDao(db);
		List<AppChangeLocationRecord> allAppChangeLoc = appChangeLocationDao
				.getAll();
		db.close();
		return allAppChangeLoc;
	}

	public Drawable getIcon(String pkg, int i) {
		Drawable icon = null;
		ApplicationInfo appInfo = null;
		PackageManager pm = this.context.getPackageManager();

		if (pkg != null) {
			try {
				appInfo = pm.getApplicationInfo(pkg, 0);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (appInfo != null) {
				icon = appInfo.loadIcon(pm);
			}

		}
		return icon;
	}

}
