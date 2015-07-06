package my.appforedata;

import java.util.Iterator;
import java.util.List;

import my.applicationforeground.NewForeService;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class LocationGet {
	private double logitude;
	private double latitude;
	private Context context;
	private static Location location;

	public LocationGet() {

	}

	public LocationGet(Context context) {
		this.context = context;
//		this.getLocation();
	}

	public double getLogitude() {
		return  this.location.getLongitude();
	}

//	public Location getLocation() {
//		return this.location;
//	}

	public double getLatitude() {
		return this.location.getLatitude();
	}

	public void setLogitude(double logitude) {
		this.logitude = logitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	public void setStaticLocation(Location location) {
		synchronized ("location") {
			this.location = location;
		}
		
	}
	
	public Location getStaticLocation()
	{
		return this.location;
	}

	public Location getLocation() {
		LocationManager lm = (LocationManager) this.context
				.getSystemService(android.content.Context.LOCATION_SERVICE);
		List<String> enProviders = lm.getProviders(true);
		Iterator<String> iEnProviders = enProviders.iterator();
		if (enProviders.isEmpty()) {
			//nov21
//			Log.i("LOGG", "ALLPROVICERS EMPTY");

		}
		while (iEnProviders.hasNext()) {
			String com = (String) iEnProviders.next();
			//nov21
//			Log.i("create",
//					"location get enPROVIDER  "
//							+ com
//							+ String.valueOf(com
//									.equals(LocationManager.GPS_PROVIDER))
//							+ "en equenet "
//							+ String.valueOf(com
//									.equals(LocationManager.NETWORK_PROVIDER)));
			if (com.equals(LocationManager.GPS_PROVIDER)) {
				this.location = lm
						.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			}
			if (com.equals(LocationManager.NETWORK_PROVIDER)&&location==null) {
				this.location = lm
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				//nov21
//				Log.i("stop","location get network:"+String.valueOf(location));
			}
		}
		//nov21
//		Log.i("stop","location get:"+String.valueOf(location));
		return this.location;
	}

}
