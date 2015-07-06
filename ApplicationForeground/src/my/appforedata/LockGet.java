package my.appforedata;

public class LockGet {

	private static boolean unlock=true;
	public LockGet(){}
	public void setLock(boolean unlock){
		this.unlock=unlock;
	}
	public boolean getLock(){
		return unlock;
	}
}
