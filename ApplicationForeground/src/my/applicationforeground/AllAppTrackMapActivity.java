package my.applicationforeground;

import java.security.AllPermission;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import my.applicationforeground.AppTrackMapActivity.MyTimeComparator;
import my.applicationforeground.AppTrackMapActivity.OverlayFriend;
import my.applicationforeground.AppTrackMapActivity.getAppTrackTask;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class AllAppTrackMapActivity extends MapActivity {

	Bundle bundle;
	String pkg = "pkg", app = "app";
	Drawable marker;
	Drawable marker2;
	private List<MapAppLocItem> allListApp = null;
	MapView mapView;
	GeoPoint endGeoPoint;
	Bitmap bmp;
	Drawable icon;
	int zoomSize = 0;
	private HashMap<Integer, Integer> mapZoom;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if ((keyCode == KeyEvent.KEYCODE_BACK)
				||(keyCode == KeyEvent.KEYCODE_HOME)) {
//			System.out.println(keyCode+"ALLTRACKMAP");
			Intent a = new Intent(this, AppIconListActivity.class);
			a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(a);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}


	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.app_track_map_activity);
		setTitle("RutgersAndroidStudy-Click back button to see list of apps.");
		Intent intent = getIntent();
		marker = (Drawable) new BitmapDrawable(BitmapFactory.decodeResource(
				getResources(), R.drawable.redpin));
		marker2 = getResources().getDrawable(R.drawable.marker);
		mapView = (MapView) findViewById(R.id.mapViewId);
		mapView.setBuiltInZoomControls(true);
		int i = 0;
		mapZoom = new HashMap<Integer, Integer>();
		while (i < Constants.ZOOMARRAY.length) {
			mapZoom.put(new Integer(i), new Integer(Constants.ZOOMARRAY[i]));
			i++;
		}
		if (intent != null) {
			getAppTrack();
		}
	}

	public void getAppTrack() {
		new getAppTrackTask().execute("start");
	}

	class getAppTrackTask extends
			AsyncTask<String, Integer, List<MapAppLocItem>> {

		@Override
		protected List<MapAppLocItem> doInBackground(String... str) {
			AppTrackFromDb appTrackFromDb = new AppTrackFromDb(
					AllAppTrackMapActivity.this);
			// nov21
			// Log.i("track", "pkg:" + pkg);
			return appTrackFromDb.getData();
			// TODO Auto-generated method stub
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			publishProgress(values[0]);
		}

		@Override
		protected void onPostExecute(List<MapAppLocItem> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			allListApp = result;
			Collections.sort(allListApp, new MyTimeComparator());
			// set the list to show the icon and app nov 02
			if (allListApp != null && allListApp.size() > 0) {
				showMap();
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						AllAppTrackMapActivity.this);
				builder.setTitle("No Location Access Records Now!");
				// builder.setMessage("");
				Dialog dialog = builder.create();
				dialog.show();
			}

		}

		public void showMap() {
			OverlayFriend overlayFriend = new OverlayFriend(marker,
					AllAppTrackMapActivity.this);
			Iterator<MapAppLocItem> iterator = allListApp.iterator();
			Double lat = null;
			Double log = null;
			int zoomId = 0;
			float max = 1;
			while (iterator.hasNext()) {
				MapAppLocItem item = new MapAppLocItem();
				item = iterator.next();
				lat = item.getLatitude() * 1e6;
				log = item.getLogitude() * 1e6;
				endGeoPoint = new GeoPoint(lat.intValue(), log.intValue());
				if (item != null && item.getAppName() != null) {
					app = item.getAppName();
				}
				overlayFriend
						.addOverlay(new OverlayItem(endGeoPoint, app, " "));
				app = "app";
			}
			// overlayFriend2.addOverlay(new OverlayItem(endGeoPoint, app,
			// " "));
			if (overlayFriend != null && overlayFriend.size() > 0) {
				List<Overlay> mapOverlays = mapView.getOverlays();
				mapOverlays.add(overlayFriend);
				// mapOverlays.add(overlayFriend2);
				mapView.getController().setCenter(endGeoPoint);
				max = maxDistance(allListApp) / 1609;
				zoomId = setZoom((int) max);
				if (lat != null && log != null)
					mapView.getController().setZoom(
							mapZoom.get(zoomId).intValue());
			} else
				Toast.makeText(getApplicationContext(), "Error",
						Toast.LENGTH_LONG).show();

		}
	}

	public float maxDistance(List<MapAppLocItem> listApp) {
		if (listApp != null && listApp.size() > 0) {
			double latestLat = 1, latestLog = 1;
			int size = listApp.size() - 1, zoom = 14;
			float[] temp = { 0 };
			float max = 1;
			if (size > 0) {
				latestLat = listApp.get(size).getLatitude();
				latestLog = listApp.get(size).getLogitude();
				Iterator<MapAppLocItem> iterator = listApp.iterator();
				while (iterator.hasNext()) {
					MapAppLocItem item = new MapAppLocItem();
					item = iterator.next();
					Location.distanceBetween(item.getLatitude(),
							item.getLogitude(), latestLat, latestLog, temp);
					// nov21
					// Log.i("distance","distance meter:"+String.valueOf(temp[0])+",max meter:"+String.valueOf(max)
					// +",lat:"+String.valueOf(item.getLatitude())+",lat:"+String.valueOf(item.getLogitude()));
					if (temp[0] > max) {
						max = temp[0];
					}
				}
				// zoom = setZoom((int) max);
			}
			return max;
		} else
			return 0;
	}

	public int setZoom(int max) {
		// TreeSet<Integer> set = new TreeSet<integer>();
		// for (int i = 0; i < Constants.ZOOMARRAY.length; i++) {
		// set.add(new Integer(Constants.ZOOMARRAY[i]));
		// }
		// set.add(new Integer((int)max));
		// nov21
		// Log.i("distance","int max:"+String.valueOf(max));
		for (int i = 0; i < Constants.ZOOMDISTANCE.length; i++) {
			if (max < Constants.ZOOMDISTANCE[i]) {
				// nov21
				// Log.i("distance","int max:"+String.valueOf(max)+",zoomarry:"+String.valueOf(
				// Constants.ZOOMARRAY[i]));
				return i;
			}
		}
		return 0;
	}

	class OverlayFriend extends ItemizedOverlay<OverlayItem> {

		private ArrayList<OverlayItem> overlayItems = new ArrayList<OverlayItem>();
		private Context context;

		public OverlayFriend(Drawable defaultMaker) {
			super(boundCenter(defaultMaker));
		}

		/**
		 * @param defaultMaker
		 *            指定标记所使用的默认图片
		 * @param context
		 */
		public OverlayFriend(Drawable defaultMaker, Context context) {
			// boundCenter(defaultMaker)：将要标记的经纬度点放在defaultMaker的正下方
			super(boundCenter(defaultMaker));
			this.context = context;

		}

		// 用于将生成好的overlayItem对象添加到List当中
		public void addOverlay(OverlayItem overlayItem) {
			overlayItems.add(overlayItem);
			// 为新添加进来的overlayItem统一执行相关的操作（具体待考证。。。）
			populate();
		}

		// 用于创建一个OverlayItem对象
		@Override
		protected OverlayItem createItem(int i) {
			// TODO Auto-generated method stub
			return overlayItems.get(i);
		}

		// 返回当前的Overlay当中所包含的overlayItem对象
		@Override
		public int size() {
			// TODO Auto-generated method stub
			return overlayItems.size();
		}

		// 当用户点击标记执行的操作
		@SuppressLint("UseValueOf")
		@Override
		public boolean onTap(int index) {
			OverlayItem item = overlayItems.get(index);
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			// nov21
			// Log.i("track", "item index:" + String.valueOf(index));
			if (allListApp.get(index) != null) {
				builder.setTitle(item.getTitle() + "\n"
						+ allListApp.get(index).getStartDate());
				// builder.setMessage();
				if (allListApp.get(index).getIcon() != null)
					icon = allListApp.get(index).getIcon();
				builder.setIcon(icon);
			}

			// int dex = 0;
			// while (dex < allListApp.size()) {
			// if (allListApp.get(dex) != null
			// && ((allListApp.get(dex).getLatitude() - allListApp
			// .get(index).getLatitude()) < 0.000001)
			// && ((allListApp.get(dex).getLogitude() - allListApp
			// .get(index).getLogitude()) < 0.000001)) {
			// builder.setMessage("clickToSeeMoreApps");
			// builder.setPositiveButton("moreapps", new
			// DialogInterface.OnClickListener(){
			//
			// public void onClick(DialogInterface dialog, int which) {
			// // TODO Auto-generated method stub
			// }});
			// break;
			// }
			// dex++;
			// }

			Dialog dialog = builder.create();
			dialog.show();
			// builder.setMessage("Latitude:"+String.valueOf(geoPoint.getLatitudeE6()));

			return true;
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * sort the list old to latest
	 * 
	 * @author fhq
	 * 
	 */
	class MyTimeComparator implements Comparator {
		public int compare(Object arg0, Object arg1) {
			int a = 0;
			long s1 = ((MapAppLocItem) arg0).getStartMiliSeconds();
			long s2 = ((MapAppLocItem) arg1).getStartMiliSeconds();
			if ((s2 - s1) > 0)
				a = -1;
			else if (s2 - s1 < 0)
				a = 1;
			else
				a = 0;
			return a;
		}
	}
}
