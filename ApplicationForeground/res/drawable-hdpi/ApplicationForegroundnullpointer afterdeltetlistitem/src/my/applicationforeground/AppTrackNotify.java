package my.applicationforeground;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.NotificationCompat;

public class AppTrackNotify {
	Context context;
	Bitmap icon;
	String allLabels;
	boolean vibration;

	public AppTrackNotify(Context context, Drawable icon, String allLabels,
			boolean vib) {
		this.context = context;
		this.icon = ((BitmapDrawable) icon).getBitmap();
		this.allLabels = allLabels;
		this.vibration = vib;
	}

	public void Notify() {
		NotificationManager notificationManager = (NotificationManager) this.context
				.getSystemService(this.context.NOTIFICATION_SERVICE);
		Intent notifyIntent = new Intent(this.context,
				AppIconListActivity.class);
		// notifyIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent appIntent = PendingIntent.getActivity(this.context, 0,
				notifyIntent, 0);
		NotificationCompat.Builder bl = new NotificationCompat.Builder(
				this.context);
		bl.setAutoCancel(true);
		bl.setLargeIcon(this.icon);
		bl.setSmallIcon(R.drawable.redpin);
		Notification notification = bl.build();
		// notification.icon=R.drawable.redpin;
		notification.tickerText = "Your location is accessed by " + this.allLabels;
		if (this.vibration) {
			notification.defaults = Notification.DEFAULT_ALL;
		} else {
			notification.defaults = Notification.FLAG_SHOW_LIGHTS;
		}
		notification.setLatestEventInfo(this.context, this.allLabels, "accesses your location, click to see details", appIntent);
		notificationManager.notify(0, notification);
	}
}
