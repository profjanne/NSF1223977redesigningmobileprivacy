package my.applicationforeground;

public class DataTypeTransfer {
	public DataTypeTransfer(){}
	public int StrintToInt(String str){
		String num=str;
		Integer in=Integer.decode(num);
		int it=in.intValue();
		System.out.println(it+1);
		System.out.println(it>3);
		return it;
	}
	

}
