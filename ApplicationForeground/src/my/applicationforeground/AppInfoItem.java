package my.applicationforeground;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;

public class AppInfoItem {
	Context context;
	public AppInfoItem(Context context){
		this.context=context;
	}
	public Drawable getIcon(String pkg) {
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
