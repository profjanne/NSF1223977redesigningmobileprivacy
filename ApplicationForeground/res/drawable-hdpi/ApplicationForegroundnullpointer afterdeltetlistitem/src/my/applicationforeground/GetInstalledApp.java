package my.applicationforeground;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

public class GetInstalledApp {

	Context context;
	List<String> pkgList = new ArrayList<String>();

	public GetInstalledApp(Context context) {
		this.context = context;
	}

	public List<String> intalledApp() {
		PackageManager pkgManager = this.context.getPackageManager();
		List<ApplicationInfo> installedApps = pkgManager
				.getInstalledApplications(PackageManager.GET_META_DATA);
		for (ApplicationInfo appInfo : installedApps) {
			String pkg;
			pkg = appInfo.packageName;
			pkgList.add(pkg);
		}
		return pkgList;
	}
}
