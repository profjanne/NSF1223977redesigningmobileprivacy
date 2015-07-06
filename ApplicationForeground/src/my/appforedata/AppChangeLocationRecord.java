package my.appforedata;

import my.applicationforeground.RecordTemplate;

public class AppChangeLocationRecord extends RecordTemplate{
	private String pkgName;
	private String appName;
	public  String actName;
	
	private double logitude;
	private double latitude;
	private String startDate;
	private long startMiliSeconds;
	public AppChangeLocationRecord()
	{
		
	}
	public AppChangeLocationRecord(String pkgName,String appName,String actName,
			double logitude, double latitude, 
			String date,long miliSeconds)
	{
		this.pkgName=pkgName;
		this.appName=appName;
		this.actName=actName;
		this.logitude = logitude;
		this.latitude = latitude;
		this.startDate=date;
		this.startMiliSeconds=miliSeconds;
				
	}
	
	public String getPkgName()
	{
		return this.pkgName;			
	}
	public String getAppName()
	{
		return this.appName;			
	}
	public String getActName()
	{
		return this.actName;			
	}
	
	public String getStartDate()
	{
		return this.startDate;			
	}
	public double getLogitude()
	{
		return this.logitude;			
	}
	public double getLatitude()
	{
		return this.latitude;			
	}
	
	public long getStartMiliSeconds()
	{
		return this.startMiliSeconds;			
	}
	public void setPkgName(String pkgName)
	{
		this.pkgName=pkgName;			
	}
	public void setAppName(String appName)
	{
		this.appName=appName;			
	}
	public void setActName(String actName)
	{
		 this.actName=actName;			
	}
	public void setStartDate(String date)
	{
		this.startDate=date;			
	}
	public void setLogitude(double logitude)
	{
		this.logitude=logitude;			
	}
	public void setLatitude(double latitude)
	{
		this.latitude=latitude;			
	}
	public void setStartMiliSeconds(long miliSeconds)
	{
		this.startMiliSeconds=miliSeconds;			
	}
}
