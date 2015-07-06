package my.applicationforeground;

import android.os.Environment;

public class DataConstants {

	private static final String APP_PACKAGE_NAME = "my.applicationforeground";
	public static final String UN_INSTALL_TABLE_NAME = "UnInstallApp";
	public static final String INSTALLED_TABLE_NAME = "InstalledApp";
	public static final String UN_LOCKPHONE_TABLE_NAME = "UnLockPhone";
	// below is in the APPUSE.DB OTHERDATABASE
	public static final String APPUSAGE_TABLE_NAME = "AppUsage";
	public static final String APPCHANGELOCATION_TABLE_NAME = "ActChangeLocation";
	public static final String GROUPUSER_TABLE = "GroupUserID";
	public static final String OPERATIONSET_TABLE = "OperationSet";
	public static final String RUNSERV_TABLE_NAME = "RunningService";
	public static final String PASSWORD = "2013LPCHIhqf";
	public static final String IDSHARE = "GROUPUSER";
	public static final String USERID = "UserID";
	public static final String GROUPID = "GroupID";
	public static final String GROUPIDDB = "groupID";
	public static final String USERIDDB = "userID";
	public static final String TEMP_UN_INSTALL_TABLE_NAME = "UnInstallAppTEMP";
	public static final String TEMP_INSTALLED_TABLE_NAME = "InstalledAppTEMP";
	public static final String TEMP_UN_LOCKPHONE_TABLE_NAME = "UnLockPhoneTEMP";
	// below is in the APPUSE.DB OTHERDATABASE
	public static final String TEMP_APPUSAGE_TABLE_NAME = "AppUsageTEMP_";
	public static final String TEMP_APPCHANGELOCATION_TABLE_NAME = "ActChangeLocationTEMP";
	public static final String TEMP_GROUPUSER_TABLE = "GroupUserIDTEMP";
	public static final String DBMASTER="sqlite_master";
	public static final int INITCREATIDTABLE=1;
	public static final int INITAPPENDNEWID=2;
	public static final int DBVERSION=1;
	public static final int DBVERSIONUPDATE=2;
	// private static final String EXTERNAL_DATA_DIR_NAME = "mymoviesdata";
	// public static final String EXTERNAL_DATA_PATH =
	// Environment.getExternalStorageDirectory() + "/" +
	// DataConstants.EXTERNAL_DATA_DIR_NAME;

	public static final String DATABASE_NAME = "phoneuse.db";
	public static final String OTHERDATABASE = "APPUSE.db";
	public static final String DATABASE_PATH = Environment.getDataDirectory()
			+ "/data/" + DataConstants.APP_PACKAGE_NAME + "/databases/"
			+ DataConstants.DATABASE_NAME;
	public static final String STARTDATE = "startDate";
	public static final long ONEDAYSECONDS=86400;
	//10minutes
	public static final double DELAYDATE=7;
	private DataConstants() {
	}
}
