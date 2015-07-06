package my.appforedata;

import my.applicationforeground.RecordTemplate;

public class OperationSetRecord extends RecordTemplate{
	private String operation;
	private String enable;
	private String date;
	private long miliSeconds;
	private double logitude;
	private double latitude;
	
	public OperationSetRecord()
	{
		
	}
	public OperationSetRecord(String operation,String enable,String date,long miliSeconds,
			double logitude, double latitude)
	{
		this.operation=operation;
		this.enable=enable;
		this.date=date;
		this.miliSeconds=miliSeconds;
		this.logitude=logitude;
		this.latitude = latitude;
				
	}
	public String getEnable()
	{
		return this.enable;			
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
	
	public void setEnable(String enable)
	{
		this.enable=enable;			
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
