package my.applicationforeground;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver{

	Context context;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		this.context = context;
		context.startService(new Intent(context,NewForeService.class));
//		context.startService(new Intent(context,ApplicationForegroundActivity.class));
		//nov21
//		Log.i("bootreceiver","boot service again");
	}

}
