package my.appforedata;

import java.util.ArrayList;
import java.util.List;

import my.appforedata.OperationSetTable.OperationSetColumns;
import my.applicationforeground.DataConstants;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;

public class OperationSetDao implements DaoTemplate<OperationSetRecord>{
	private static final String INSERT = "insert into "
			+ DataConstants.OPERATIONSET_TABLE + "("+BaseColumns._ID+", " + OperationSetColumns.OPERATION+ ", " 
			+ OperationSetColumns.ENABLE + ", "
			+ OperationSetColumns.DATE + ", "
			+ OperationSetColumns.MILISECONDS+ ", "
			+ OperationSetColumns.LOGITUDE + ", "
			+ OperationSetColumns.LATITUDE 
			+ ") values (null,?, ?,?, ?, ?,?)";

	private SQLiteDatabase db;
	private SQLiteStatement insertStatement;

	public OperationSetDao(SQLiteDatabase db) {
		this.db = db;
		insertStatement = db.compileStatement(OperationSetDao.INSERT);
	}

	public long save(OperationSetRecord entity) {
		insertStatement.clearBindings();
		insertStatement.bindString(1, entity.getOperation());
		insertStatement.bindString(2, entity.getEnable());
		insertStatement.bindString(3, entity.getDate());
		insertStatement.bindLong(4, entity.getMiliSeconds());
		insertStatement.bindString(5, String.valueOf(entity.getLogitude()));
		insertStatement.bindString(6, String.valueOf(entity.getLatitude()));
		return insertStatement.executeInsert();
		
	}
	
	public OperationSetRecord get(long id) {
	      OperationSetRecord operationSetRecord = null;
	      Cursor c =
	               db.query(DataConstants.OPERATIONSET_TABLE, new String[] { BaseColumns._ID,
	            		   OperationSetColumns.OPERATION,
	            		   OperationSetColumns.ENABLE, OperationSetColumns.DATE, OperationSetColumns.MILISECONDS,
	            		   OperationSetColumns.LOGITUDE, OperationSetColumns.LATITUDE},
	                        BaseColumns._ID + " = ?", new String[] { String.valueOf(id) }, null, null, null, "1");
	      if (c.moveToFirst()) {
	         operationSetRecord = this.buildOperationSetRecordFromCursor(c);
	      }
	      if (!c.isClosed()) {
	         c.close();
	      }
	      return operationSetRecord;
	   }
	public OperationSetRecord buildOperationSetRecordFromCursor(Cursor c) {
		OperationSetRecord operationSetRecord = null;
	      if (c != null) {
	    	  operationSetRecord = new OperationSetRecord();
	    	  operationSetRecord.setId(c.getLong(0));
	    	  operationSetRecord.setOperation(c.getString(1));
	    	  operationSetRecord.setEnable(c.getString(2));
	    	  operationSetRecord.setDate(c.getString(3));
	    	  operationSetRecord.setMiliSeconds(c.getLong(4));
	    	  operationSetRecord.setLogitude(Double.parseDouble(c.getString(5)));
	    	  operationSetRecord.setLatitude(Double.parseDouble(c.getString(6)));
	      }
	      return operationSetRecord;
	   }
	public List<OperationSetRecord> getAll()
	{
		List<OperationSetRecord> list = new ArrayList<OperationSetRecord>();
	      Cursor c =
	               db.query(DataConstants.OPERATIONSET_TABLE, new String[] { BaseColumns._ID,
	            		   OperationSetColumns.OPERATION, OperationSetColumns.ENABLE, OperationSetColumns.DATE,
	            		   OperationSetColumns.MILISECONDS, OperationSetColumns.LOGITUDE, OperationSetColumns.LATITUDE,
	            		  },
	                        null,
	                        null, null, null, BaseColumns._ID, null);
	      if (c.moveToFirst()) {
	         do {
	        	 OperationSetRecord operationSetRecord = this.buildOperationSetRecordFromCursor(c);
	            if (operationSetRecord != null) {
	               list.add(operationSetRecord);
	            }
	         } while (c.moveToNext());
	      }
	      if (!c.isClosed()) {
	         c.close();
	      }
	      return list;
	}
	public List<OperationSetRecord> getAll(long flag)
	{
		List<OperationSetRecord> list = new ArrayList<OperationSetRecord>();
//		System.out.println("operationflag:"+flag);
	      Cursor c =
	               db.query(DataConstants.OPERATIONSET_TABLE, new String[] { BaseColumns._ID,
	            		   OperationSetColumns.OPERATION, OperationSetColumns.ENABLE, OperationSetColumns.DATE,
	            		   OperationSetColumns.MILISECONDS, OperationSetColumns.LOGITUDE, OperationSetColumns.LATITUDE,
	            		  },
	            		  BaseColumns._ID+"> ?", new String[] {String.valueOf(flag)}, null, null, BaseColumns._ID, null);
	      if (c.moveToFirst()) {
	         do {
	        	 OperationSetRecord operationSetRecord = this.buildOperationSetRecordFromCursor(c);
	            if (operationSetRecord != null) {
	               list.add(operationSetRecord);
	            }
	         } while (c.moveToNext());
	      }
	      if (!c.isClosed()) {
	         c.close();
	      }
	      return list;
	}
	public void update(OperationSetRecord type) {
		// TODO Auto-generated method stub
		
	}

	public void delete(OperationSetRecord type) {
		// TODO Auto-generated method stub
		
	}
	public void deleteAll(SQLiteDatabase db) {
		db.delete(DataConstants.OPERATIONSET_TABLE, null,
				null);
	}

}
