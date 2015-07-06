package my.appforedata;

import my.applicationforeground.RecordTemplate;

public class UnLockPhoneRecord extends RecordTemplate {

	private String unlockDate;
	private String lockDate;
	private String intervalDate;
	private long unlockMiliSeconds;
	private long lockMiliSeconds;
	private long intervalMiliSeconds;

	public UnLockPhoneRecord() {

	}

	// new UnLockPhoneRecord(offDate,onDate,intervalDate,
	// offTime,onTime,intervalMiliSeconds);
	public UnLockPhoneRecord(String unlockDate, String lockDate,
			String intervalDate, long unlockMiliSeconds, long lockMiliSeconds,
			long intervalMiliSeconds) {

		this.unlockDate = unlockDate;
		this.lockDate = lockDate;
		this.intervalDate = intervalDate;
		this.unlockMiliSeconds = unlockMiliSeconds;
		this.lockMiliSeconds = lockMiliSeconds;
		this.intervalMiliSeconds = intervalMiliSeconds;
	}
	
	public String getUnlockDate()
	{
		return this.unlockDate;
	}
	public String getLockDate()
	{
		return this.lockDate;
	}
	public String getIntervalDate()
	{
		return this.intervalDate;
	}
	public long getUnlockMiliSeconds()
	{
		return this.unlockMiliSeconds;
	}
	public long getLockMiliSeconds()
	{
		return this.lockMiliSeconds;
	}
	public long getIntervalMiliSeconds()
	{
		return this.intervalMiliSeconds;
	}
	public void setUnlockDate(String unlockDate){
		this.unlockDate = unlockDate;
	}
	public void setLockDate(String lockDate){
		this.lockDate = lockDate;
	}
	public void setIntervalDate(String intervalDate){
		this.intervalDate = intervalDate;
	}
	public void setUnlockMiliSeconds(long unlockMiliSeconds){
		this.unlockMiliSeconds = unlockMiliSeconds;
	}
	public void setLockMiliSeconds(long lockMiliSeconds){
		this.lockMiliSeconds = lockMiliSeconds;
	}
	public void setIntervalMiliSeconds(long intervalMiliSeconds){
		this.intervalMiliSeconds = intervalMiliSeconds;
	}
}
