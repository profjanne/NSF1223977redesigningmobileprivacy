package my.applicationforeground;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.Manifest.permission;
import android.R.string;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.util.Log;

public class GetPermissions {
	Context context;
	private static HashSet<String> listPkgPermissions = new HashSet<String>();
	private static HashSet<String> listPkgNoPerm = new HashSet<String>();

	public GetPermissions(Context context) {
		this.context = context;
		new Thread(new Runnable() {
			public void run() {
				setPermissions();
			}
		}).start();
	}

	public GetPermissions(Context context, String str) {
		this.context = context;
	}

	public void setPermissions() {
		StringBuffer strbuf = new StringBuffer();
		StringBuffer strbufS = new StringBuffer();
		PackageManager p = this.context.getPackageManager();
		final List<PackageInfo> appinstall = p
				.getInstalledPackages(PackageManager.GET_PERMISSIONS
						| PackageManager.GET_RECEIVERS
						| PackageManager.GET_SERVICES
						| PackageManager.GET_PROVIDERS);

		for (PackageInfo pInfo : appinstall) {
			// PermissionInfo[] permission=pInfo.permissions;
			String[] reqPermission = pInfo.requestedPermissions;
			ServiceInfo[] services = pInfo.services;
			// ProviderInfo[] providers=pInfo.providers;

			int versionCode = pInfo.versionCode;
			Log.d("versionCode-package ", Integer.toString(versionCode));
			Log.d("Installed Applications", pInfo.applicationInfo.loadLabel(p)
					.toString());
			Log.d("packegename", pInfo.packageName.toString());
			strbuf.append("versionCode-package " + ","
					+ Integer.toString(versionCode) + "\n");
			strbuf.append("Installed Applications" + ","
					+ pInfo.applicationInfo.loadLabel(p).toString() + "\n");
			strbuf.append("packegename" + "," + pInfo.packageName.toString()
					+ "\n");
			if (reqPermission != null && pInfo.packageName != null)
				for (int i = 0; i < reqPermission.length; i++) {
					if (reqPermission[i] != null
							&& (reqPermission[i].equals(Constants.FINELOCATION) || reqPermission[i]
									.equals(Constants.COARSELOCATION))) {
						listPkgPermissions.add(pInfo.packageName.toString());
					} else {
						listPkgNoPerm.add(pInfo.packageName.toString());
					}
					Log.d("permission list", reqPermission[i]);
					strbuf.append("permission list" + "," + reqPermission[i]
							+ "\n");
				}
			if (services != null)
				for (int i = 0; i < services.length; i++) {
					String name = services[i].name;
					strbufS.append("services list" + ","
							+ String.valueOf(services[i]) + "m" + name + "\n");
				}
		}

		write(strbuf);
		writeServ(strbufS);

	}

	public static HashSet<String> getListPkgLocationPermission() {
		return listPkgPermissions;
	}

	public static HashSet<String> getListPkgNoPerm() {
		return listPkgNoPerm;
	}

	public void write(StringBuffer strBuf) {
		if (FileUtil.isExternalStorageWritable()) {
			File dir = FileUtil.getExternalDirAn();
			File file = new File(dir, "permissions.txt");
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

	public void writeServ(StringBuffer strBuf) {
		if (FileUtil.isExternalStorageWritable()) {
			File dir = FileUtil.getExternalDirAn();
			File file = new File(dir, "services.txt");
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
}
