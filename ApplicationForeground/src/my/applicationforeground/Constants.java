package my.applicationforeground;

import android.R.string;

public final class Constants {
	public final static String LOG_TAG="TAG";
	   private static final String APP_PACKAGE_NAME = "my.applicationforeground";
	   public static final String UNINSTALLAPP = "UnInstallApp.txt";
	   public static final String INSTALLEDAPP="InstalledApp.txt";
	   public static final String UNLOCKPHONE="UnLockPhone.txt";
	   public static final String APPUSAGE="AppUsage.txt";
	   public static final String ACTCHANGELOCATION="ActChangeLocation.txt";
	   public static final String EXTRA_REPLACING="android.intent.extra.REPLACING";
	   public static final String EXTRA_DATA_REMOVED= "android.intent.extra.DATA_REMOVED";
	   public static final String  ACTION_PACKAGE_FULLY_REMOVED= "android.intent.action.PACKAGE_FULLY_REMOVED";
	   public static final boolean DBREADFILE=true;
	   public static final String DBREADFLAG="dbread"+"\n";
	   public static final String FINELOCATION="android.permission.ACCESS_FINE_LOCATION";
	   public static final String COARSELOCATION="android.permission.ACCESS_COARSE_LOCATION" ;
	   public static final int MAXMISTOAST=8; 
	   public static final int MAXINTERVALMISMINUTE=6; 
	   public static final String EXT_STORAGE_PATH_ANDROID_APPFORE_CONSTANTS = "/Android/appfore";
	   //we calculate the max log diff and max lat diff, then get the distance from (0,0)
	   public static final int ZOOM_0=14;//3MILES 40.622292,-74.4104;0
	   public static final int ZOOM_1=12;//24MILES-5;1
	   public static final int ZOOM_2=11;//toms river50-25
	   public static final int ZOOM_3=10;//103MILES-50
	   public static final int ZOOM_4=7;//FROM EDISON TO SOUTH CAROLINA 100 230
	   public static final int ZOOM_5=5;//2370MILES  6.751896,-75.410156 ;38.68551,-101.403809
	   //Salisbury, MD, United States for 9;virginia beach for 8;
	   public static final int[] ZOOMDISTANCE={2,6,26,50,71,139,258,607,2505,3694,5003,8500,20000};
	   public static final int[] ZOOMARRAY={16,14,12,11,10,9,8,7,5,4,3,2,1};
	   //APPUSAGE CHANGE 15 TIMES, WE READ DB AND WRITE ALL THE APPS IN FILE
	   public static final String PhoneNum="tel:7327435882";
	   public static final String SMSNum="9089126235";
	   public static final String emailAddress="androidrecruit@gmail.com";
	   public static final String AppName="RutgersAndroidStudy";
	   public static final String ALERTKEY="alert";
	   public static final String NOTVIB="notvib";
	   public static final long GAPSAME=300000;//5minutes
	   public static final long GAPDIFF=60000;//1 minutes
	   public static final int APPCHANGETIMT=5;
	   public static final int APPUSAGEWRITE=3;
	private Constants()
	{}
}
