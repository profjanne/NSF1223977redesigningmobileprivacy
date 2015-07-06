package my.applicationforeground;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import my.appforedata.AppendGroupUserID;
import my.appforedata.LockGet;
import my.appforedata.PreferenceIDTableRead;
import my.appforedata.UnLockPhoneDao;
import my.appforedata.UnLockPhoneRecord;

import android.R.string;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.Toast;

/*
 * receive the phone lock(screen_off) and unlock(user_present) intent and write to the file
 * we calulate the interval between the continuous on-off to see the phone user time
 * also we write the interval and on-off time instantly when we receive the intent
 * attention: the off intent will creat new instance every time so we must use static to share data
 * between different intent(on-off,on-on...)*/
public class AopenReceiver extends BroadcastReceiver {
	private String installPkg = "s";
	private String strPkg = "s";
	private long time, now = 1;
	private static long onTime, offTime, intervalMiliSeconds;
	private static String onDate = "onDate", offDate = "offDate",
			intervalDate = "intervaldate";
	private String operation = "operation";
	private static int timer = 0;
	// format is the hh:mm:ss inteval for using a phone
	private String strDiff, format;
	private long difference = 0, hms = 0, dd = 0, difSecond = 0;
	private static int i, firstOff = 0;
	private int j = 0;
	Context context;

	public AopenReceiver() {
		//nov21
//		Log.i("LOG", "CONSTRUCTOR OPENPHONE");
//		Log.i("intent", String.valueOf(i) + "constructorj:" + String.valueOf(j));
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String strTest;
		// long intervalMiliSeconds;

		this.context = context;
		//nov21
//		Log.i("context", "open context:" + context.toString());
//		Log.i("LOG", "ONRECEIVE");
		time = System.currentTimeMillis();

		String currentDateTimeString = DateFormat.getDateTimeInstance().format(
				new Date());
		// String strPkg = intent.getData().getSchemeSpecificPart();
		String strPkg = String.valueOf(intent.getData());
		// each time screen off means the end of this time of using the phone
		// the interval is between latest
		// user_present and the screen_off
		//firstoff is because we record the open phoen time, so first should be user present not screen off
		
		if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)
				&& (firstOff != 0)) {
			j++;
			LockGet lockGet=new LockGet();
			lockGet.setLock(false);
			operation = "screen   off ";
			offTime = time;
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss.SSS");
			Date now = new Date();
			// now = new Date(milliseconds)
			Date test = new Date(offTime);
			strTest = simpleDateFormat.format(test);
			offDate = simpleDateFormat.format(now);
			// calculate the interval of this time of using the phone
			if (offTime > onTime) {
				difference = offTime - onTime;
				intervalMiliSeconds = offTime - onTime;
				long mili = difference % 1000;
				difSecond = difference / 1000;
				if (difSecond > 0) {
					if (difSecond > 24 * 60 * 60) {
						dd = difSecond / (24 * 60 * 60);
					}
					hms = difSecond % (24 * 60 * 60);
					// hms=23 * 60 * 60+54*60+34;
					format = DateUtils.formatElapsedTime(hms);
					// because the formate will give out mm:ss if time is little
					// so we add the hh self
					if (hms < 60 * 60) {
						format = "00:" + format;
					}
					intervalDate = String.valueOf(dd) + "::" + format;
					// store the on off interval and date to the database
					AsqliteHelper dbHelper = new AsqliteHelper(this.context);
					SQLiteDatabase db = dbHelper.getWritableDatabase();
					UnLockPhoneRecord unLockPhoneRecord = new UnLockPhoneRecord(
							onDate, offDate, intervalDate, onTime, offTime,
							intervalMiliSeconds);
					UnLockPhoneDao unLockPhoneDao = new UnLockPhoneDao(db);
					unLockPhoneDao.save(unLockPhoneRecord);
					//jan7 append ID to table
					AppendGroupUserID append = new AppendGroupUserID(
							this.context, db, DataConstants.UN_LOCKPHONE_TABLE_NAME);
					append.save();
					//read db*******************
					//not to file
					/*
					long flag=new PreferenceIDTableRead(this.context, DataConstants.UN_LOCKPHONE_TABLE_NAME).getIDMax();
					List<UnLockPhoneRecord> list = unLockPhoneDao.getAll(flag);
					Iterator<UnLockPhoneRecord> allIterator = list.iterator();
					StringBuffer strBufUnlock = new StringBuffer();
					int i = 0;
					while (allIterator.hasNext()) {
						UnLockPhoneRecord r = allIterator.next();
						strBufUnlock.append(r.getId()+","+r.getUnlockDate() + ","
								+ r.getLockDate() + ","
								+ r.getIntervalDate()+","
								+ String.valueOf(r.getUnlockMiliSeconds())+","
								+String.valueOf(r.getLockMiliSeconds())+","
								+String.valueOf(r.getIntervalMiliSeconds())
								+ "\n");
					}
					if(strBufUnlock!=null&&strBufUnlock.length()>0){
						long max=list.get(list.size()-1).getId();
						long min=list.get(0).getId();
						if(max<list.get(0).getId()){
							min=max;
							max=list.get(0).getId();
						}
						new PreferenceIDTableRead(this.context, DataConstants.UN_LOCKPHONE_TABLE_NAME).setIDMax(max);
						AppendGroupUserID getappid=new AppendGroupUserID(this.context, db, DataConstants.UN_LOCKPHONE_TABLE_NAME);
						String str="group user id\n";
						if (min < 2) {
							// strBufInstalled.append(str);
							str = getappid.getStringForDBRead();
							strBufUnlock.insert(0, str);
						}
//						String str=getappid.getStringForDBRead();
//						if(str!=null){
//							strBufUnlock.append(str);
//						}
//						strBufUnlock.append(Constants.DBREADFLAG);
						writeAllUnlock(strBufUnlock);
					}*/
					db.close();
				}
				// nearly impossible happen that the interval using a phone is
				// some miliseconds
			}
			if (firstOff > 1000) {
				firstOff = 1;
			}

		}
		// each time receive the user_present:unlock the phone, the constructor
		// is called
		// and the new instance is made so the field value can not share between
		// two user_present
		// so must use static or must write the strbuff each time for eavey
		// user_present
		else if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
			firstOff++;
			LockGet lockGet=new LockGet();
			lockGet.setLock(true);
			operation = "unlock phone ";
			onTime = time;
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss.SSS");
			Date now = new Date();
			this.onDate = simpleDateFormat.format(now);
		}
	}

	public void writeAllUnlock(StringBuffer strBufUnlock) {
		if (FileUtil.isExternalStorageWritable()) {
			// read data from database and log out
			// ********************************************
			File dir = FileUtil.getExternalDirAn();
			File file = new File(dir, "DbReadUnLockPhone.txt");
			try {
				// synchronized (DATA_LOCK)
				{
					if (!file.exists())
						file.createNewFile(); // ok if returns false, overwrite
					if ((file != null) && file.canWrite()) {
						Writer out = new BufferedWriter(new FileWriter(file,
								Constants.DBREADFILE), 1024);
						out.write(strBufUnlock.toString());
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
