package my.applicationforeground;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.text.format.DateUtils;
import android.util.Log;

public class FormatTime {
	private long difSecond = 0;
	private long hms = 0;
	private long dd = 0;
	private String strFormat="strFomatdate";
	private String intervalDate="strdate";

	public long getTime(){
		return System.currentTimeMillis();
	}
	public FormatTime(){}
	public FormatTime(long miliseconds) {
		difSecond = miliseconds / 1000;
	}

	public String getStringYMDHMSFormateDate(){
		SimpleDateFormat sdfDate = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.SSS");// dd/MM/yyyy
//		Date now = new Date();
		Date now=new Date(System.currentTimeMillis());
		String date = sdfDate.format(now);
//		Date test=sdfDate.parse(date);
//		Log.i("check","strtodate:"+test.getTime()+",mili:"+String.valueOf(System.currentTimeMillis()));
		return date;
	}
	
	public String getStringYMDHMDate(long time){
		SimpleDateFormat sdfDate = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");// dd/MM/yyyy
//		Date now = new Date();
		Date now=new Date(time);
		String date = sdfDate.format(now);
//		Date test=sdfDate.parse(date);
//		Log.i("check","strtodate:"+test.getTime()+",mili:"+String.valueOf(System.currentTimeMillis()));
		return date;
	}
	public String getFormatDate() {

		if (difSecond > 0) {
			if (difSecond > 24 * 60 * 60) {
				dd = difSecond / (24 * 60 * 60);
			}
			hms = difSecond % (24 * 60 * 60);
			// hms=23 * 60 * 60+54*60+34;
			strFormat = DateUtils.formatElapsedTime(hms);
			// because the formate will give out mm:ss if time is little
			// so we add the hh self
			if (hms < 60 * 60) {
				strFormat = "00:" + strFormat;
			}
			intervalDate = String.valueOf(dd) + "::" + strFormat;
		}
		return intervalDate;
	}
}