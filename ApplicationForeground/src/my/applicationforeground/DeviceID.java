package my.applicationforeground;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

public class DeviceID {

	Context context;

	public DeviceID(Context context) {
		this.context = context;
	}

	public String getDeviceId() {
		TelephonyManager tManager = (TelephonyManager) this.context
				.getSystemService(this.context.TELEPHONY_SERVICE);
		String uuid = tManager.getDeviceId();
		if (uuid==null){
			Log.i("LOG","UUIDnull:"+uuid);
			return null;
		}
		String digestedNumber = md5(uuid);
//		Log.i("LOG","UUID:"+uuid+", MD5:"+digestedNumber);
		return digestedNumber;
	}

	public String md5(String in) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5");
			digest.reset();
			digest.update(in.getBytes());
			byte[] a = digest.digest();
			int len = a.length;
			StringBuilder sb = new StringBuilder(len << 1);
			for (int i = 0; i < len; i++) {
				sb.append(Character.forDigit((a[i] & 0xf0) >> 4, 16));
				sb.append(Character.forDigit(a[i] & 0x0f, 16));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
}
