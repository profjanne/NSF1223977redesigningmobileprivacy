package my.appforedata;

import my.applicationforeground.DataConstants;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CheckTableDB {
	SQLiteDatabase db;
	public CheckTableDB(SQLiteDatabase db){
		this.db=db;
	}
	 public boolean tableIsExist(String tableName){
         boolean result = false;
         if(tableName == null){
                 return false;
         }
         
         Cursor cursor = null;
         try {
      	   String sql = "select count(*) as c from " + DataConstants.DBMASTER
     				+ " where type ='table' and name ='"+ tableName.trim() + "' ";
      	   cursor = db.rawQuery(sql, null);
      	 Log.i("check",
  				"check exist table ");
                 if(cursor.moveToNext()){
                         int count = cursor.getInt(0);
                         Log.i("check",
                  				"check exist table getint:"+String.valueOf(count));
                         if(count>0){
                                 result = true;
                                 
                         }
                 }
                 
         } catch (Exception e) {
                 // TODO: handle exception
         }               
         return result;
 }
}
