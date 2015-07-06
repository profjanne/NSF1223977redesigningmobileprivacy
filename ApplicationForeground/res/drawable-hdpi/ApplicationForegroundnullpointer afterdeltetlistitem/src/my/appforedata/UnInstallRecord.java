package my.appforedata;
import  my.applicationforeground.RecordTemplate;
import my.applicationforeground.DataConstants;

public class UnInstallRecord extends RecordTemplate{
	
	private String pkgName;
	private String appName;
	private String date;
	private long miliSeconds;
	private double logitude;
	private double latitude;
//	1 install ;0 uninstall
	private String operation;
	
	public UnInstallRecord()
	{
		
	}
	public UnInstallRecord(String pkgName,String appName,String date,long miliSeconds,
			double logitude, double latitude, String operation)
	{
		this.pkgName=pkgName;
		this.appName=appName;
		this.date=date;
		this.miliSeconds=miliSeconds;
		this.logitude=logitude;
		this.latitude = latitude;
		this.operation = operation;
				
	}
	public String getPkgName()
	{
		return this.pkgName;			
	}
	public String getAppName()
	{
		return this.appName;			
	}
	public String getDate()
	{
		return this.date;			
	}
	public long getMiliSeconds()
	{
		return this.miliSeconds;
	}
	public double getLogitude()
	{
		return this.logitude;			
	}
	public double getLatitude()
	{
		return this.latitude;			
	}
	public String getOperation()
	{
		return this.operation;			
	}
	
	public void setPkgName(String pkgName)
	{
		this.pkgName=pkgName;			
	}
	public void setAppName(String appName)
	{
		this.appName=appName;			
	}
	public void setDate(String date)
	{
		this.date=date;			
	}
	public void setMiliSeconds(long miliSeconds)
	{
		this.miliSeconds=miliSeconds;
	}
	public void setLogitude(double logitude)
	{
		this.logitude=logitude;			
	}
	public void setLatitude(double latitude)
	{
		this.latitude=latitude;			
	}
	public void setOperation( String operation)
	{
		 this.operation=operation;			
	}


}
