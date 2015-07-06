package my.applicationforeground;

import my.appforedata.AppChangeLocationRecord;
import android.graphics.drawable.Drawable;


public class MapAppLocItem extends AppChangeLocationRecord{

	private Drawable icon;
	
	public void setIcon(Drawable icon){
		this.icon=icon;
	}
	public Drawable getIcon(){
		return this.icon;
	}
}
