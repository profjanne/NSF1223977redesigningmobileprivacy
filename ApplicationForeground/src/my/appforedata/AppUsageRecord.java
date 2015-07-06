package my.appforedata;

import my.applicationforeground.RecordTemplate;

public class AppUsageRecord extends RecordTemplate{
	private String pkgName;
	private String appName;
	public  String actName;
	private String startDate;
	private String endDate;
	private String intervalDate;
	private double logitude;
	private double latitude;
	private long startMiliSeconds;
	private long endMiliSeconds;
	private long intervalMiliSeconds;
	private long locChange;
	public AppUsageRecord()
	{
		
	}
	public AppUsageRecord(String pkgName,String appName,String actName,String startDate, String endDate,
			String intervalDate,double logitude, double latitude, 
			long startMiliSeconds,long endMiliSeconds,long intervalMiliSeconds,long locChange)
	{
		this.pkgName=pkgName;
		this.appName=appName;
		this.actName=actName;
		this.startDate=startDate;
		this.endDate=endDate;
		this.intervalDate=intervalDate;
		this.logitude = logitude;
		this.latitude = latitude;
		this.startMiliSeconds = startMiliSeconds;
		this.endMiliSeconds=endMiliSeconds;
		this.intervalMiliSeconds = intervalMiliSeconds;
		this.locChange=locChange;
				
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
	public String getEndDate()
	{
		return this.endDate;			
	}
	public String getIntervalDate()
	{
		return this.intervalDate;			
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
	public long getEndMiliSeconds()
	{
		return this.endMiliSeconds;			
	}
	public long getIntervalMiliSeconds()
	{
		return this.intervalMiliSeconds;			
	}
	public long getLocChange()
	{
		return this.locChange;			
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
	public void setStartDate(String startDate)
	{
		this.startDate=startDate;			
	}
	public void setEndDate(String endDate)
	{
		this.endDate=endDate;			
	}
	public void setIntervalDate(String intervalDate)
	{
		this.intervalDate=intervalDate;			
	}
	public void setLogitude(double logitude)
	{
		this.logitude=logitude;			
	}
	public void setLatitude(double latitude)
	{
		this.latitude=latitude;			
	}
	public void setStartMiliSeconds(long startMiliSeconds)
	{
		this.startMiliSeconds=startMiliSeconds;			
	}
	public void setEndMiliSeconds(long endMiliSeconds)
	{
		this.endMiliSeconds=endMiliSeconds;			
	}
	public void setIntervalMiliSeconds(long intervalMiliSeconds)
	{
		this.intervalMiliSeconds=intervalMiliSeconds;			
	}
	public void setLocChange(long locChange)
	{
		this.locChange=locChange;			
	}
}
