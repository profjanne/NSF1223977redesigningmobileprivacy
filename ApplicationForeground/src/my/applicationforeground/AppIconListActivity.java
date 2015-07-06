package my.applicationforeground;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import my.appforedata.AppChangeLocationDao;
import my.appforedata.AppChangeLocationRecord;
import my.applicationforeground.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class AppIconListActivity extends Activity implements
		OnItemClickListener, Handler.Callback {

	private ListView listview = null;
	private List<MapAppLocItem> listAppIcon = null;
	ProgressDialog _busyDialog = null;
	private SQLiteDatabase db;
	private Map<String, String> mapAppPkg;
	Dialog dialog;
	Handler handlerUI = new Handler(this);
	private static final int OPTIONS_MENU_ALPHA = 0;
	private static final int OPTIONS_MENU_TIME = 1;
	List<MapAppLocItem> listItem = new ArrayList<MapAppLocItem>();
Button btn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("RutgersAndroidStudy-Click item to see history on map.");
		
		// nov21
		// Log.i("nove", "oncreate listview appfore");
		setContentView(R.layout.show_app_icon_list);
		listview = (ListView) findViewById(R.id.listviewApp);
		listAppIcon = new ArrayList<MapAppLocItem>();
		mapAppPkg = new HashMap<String, String>();
		ServiceDbHelper dbHelper = new ServiceDbHelper(getApplicationContext());
		db = dbHelper.getReadableDatabase();
		getAppIcon();
		btn=(Button)findViewById(R.id.btnAllMap);
		btn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getApplicationContext(),AllAppTrackMapActivity.class));
			}
		});
		// to continued assignment for the listAppIcon*****************
	}

	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if (msg.what > 0) {
			showList();
		}
		else{
			AlertDialog.Builder builder = new AlertDialog.Builder(
					AppIconListActivity.this);
			builder.setTitle("No Location Access Records Now");
//			builder.setMessage("");
			Dialog dialog = builder.create();
			dialog.show();
		}
		return false;
	}

	public void showList() {
		if (listAppIcon != null && listAppIcon.size() > 0) {
			// set the list to show the icon and app nov 02
			AppAdapter adapter = new AppAdapter(AppIconListActivity.this,
					listAppIcon);
			listview.setAdapter(adapter);
			listview.setOnItemClickListener(AppIconListActivity.this);
		} else {
			showDialog();
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	public void getAppIcon() {
		// showBusyDialog();
		// new getAppIconTask().execute("start");
		new Thread(new Runnable() {

			public void run() {
				// TODO Auto-generated method stub
				// Message msg = Message.obtain(handlerUI, "ok");
				Message msg = Message.obtain(handlerUI);
				listAppIcon = getData();
				if (listAppIcon != null&&listAppIcon.size() > 0) {
					msg = Message.obtain(handlerUI, 1);
				} else {
					msg = Message.obtain(handlerUI, 0);
				}
				handlerUI.sendMessage(msg);
			}
		}).start();
	}

	public List<MapAppLocItem> getData() {
		String app = "app";
		String pkg = "pkg";
		// Drawable icon = null;

		List<MapAppLocItem> listEndUninstallItem = new ArrayList<MapAppLocItem>();
		// get app pkg from database store all data in a hashmap
		AppChangeLocationDao appChangeLocationDao = new AppChangeLocationDao(db);
		List<AppChangeLocationRecord> allAppChangeLoc = appChangeLocationDao
				.getAll();
		Iterator<AppChangeLocationRecord> iteratorAll = allAppChangeLoc
				.iterator();
		List<String> pkgInstalledList = new ArrayList<String>();

		if (iteratorAll.hasNext()) {
			GetInstalledApp getApp = new GetInstalledApp(
					AppIconListActivity.this);
			pkgInstalledList = getApp.intalledApp();
		}

		while (iteratorAll.hasNext()) {
			Drawable icon;
			AppChangeLocationRecord r = iteratorAll.next();
			app = r.getAppName();
			pkg = r.getPkgName();
			int i = 0;
			if (pkg != null && app != null) {
				if (pkgInstalledList.contains(pkg)) {
					icon = getIcon(pkg, i);
				} else {
					icon = (Drawable) new BitmapDrawable(
							BitmapFactory.decodeResource(getResources(),
									R.drawable.ic_launcher));
				}

				// show in the maps all the apps that tracking users
				if (mapAppPkg.put(r.getAppName(), r.getPkgName()) == null) {
					MapAppLocItem item = new MapAppLocItem();
					item.setIcon(icon);
					item.setPkgName(pkg);
					item.setAppName(app);
					item.setActName(r.getActName());
					item.setLogitude(r.getLatitude());
					item.setLatitude(r.getLatitude());
					item.setStartDate(r.getStartDate());
					item.setStartMiliSeconds(r.getStartMiliSeconds());
					if (pkgInstalledList.contains(pkg)) {
						listItem.add(item);
					} else {
						listEndUninstallItem.add(item);
					}
				}
			}
		}
		// get icon using codes
		db.close();
		if (listItem.size() > 1) {
			MapAppLocItem item = new MapAppLocItem();
			item = listItem.get(0);
			listItem.remove(0);
			Collections.sort(listItem, new MyComparator());
			listItem.add(0, item);
		}
		if (listEndUninstallItem.size() > 0) {
			Collections.sort(listEndUninstallItem, new MyComparator());
			listItem.addAll(listEndUninstallItem);
		}
		return listItem;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, OPTIONS_MENU_ALPHA, 0, "OrderByAlphabetic");
		menu.add(0, OPTIONS_MENU_TIME, 1, "OrderByTime");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case OPTIONS_MENU_ALPHA:
			if (listItem.size() > 0) {
				Collections.sort(listItem, new MyComparator());
				System.out.print("order");
				listAppIcon = listItem;
				showList();
			}
			break;
		case OPTIONS_MENU_TIME:
			if (listItem.size() > 0) {
				Collections.sort(listItem, new MyTimeComparator());
				listAppIcon = listItem;
				showList();
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public Drawable getIcon(String pkg, int i) {
		Drawable icon = null;
		ApplicationInfo appInfo = null;
		PackageManager pm = this.getPackageManager();

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

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		listItem.clear();
		// nov21
//		Log.i("nove", "ondetroy listview appfore");
		if (dialog != null) {
			dialog.dismiss();
		}
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(AppIconListActivity.this,
				AppTrackMapActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("pkg", listAppIcon.get(position).getPkgName());
		bundle.putString("app", listAppIcon.get(position).getAppName());
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		Bitmap bitmap = ((BitmapDrawable) listAppIcon.get(position).getIcon())
				.getBitmap();
		bitmap.compress(Bitmap.CompressFormat.PNG, 50, bs);
		bundle.putByteArray("icon", bs.toByteArray());

		intent.putExtras(bundle);
		startActivity(intent);
	}

	public void showDialog() {
		new AlertDialog.Builder(AppIconListActivity.this)
				.setTitle("Database has no location access record!")
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss(); // 取消对话框
						// go back to main activity for test we shall change it
						// to noting
						startActivity(new Intent(AppIconListActivity.this,
								ApplicationForegroundActivity.class));
					}

				}).create().show();
	}

}

class MyComparator implements Comparator {
	public int compare(Object arg0, Object arg1) {
		String s1 = ((MapAppLocItem) arg0).getAppName();
		String s2 = ((MapAppLocItem) arg1).getAppName();

		return s1.compareTo(s2);
	}
}

class MyTimeComparator implements Comparator {
	public int compare(Object arg0, Object arg1) {
		int a = 0;
		long s1 = ((MapAppLocItem) arg0).getStartMiliSeconds();
		long s2 = ((MapAppLocItem) arg1).getStartMiliSeconds();
		if ((s2 - s1) > 0)
			a = 1;
		else if (s2 - s1 < 0)
			a = -1;
		else
			a = 0;
		return a;
	}
}