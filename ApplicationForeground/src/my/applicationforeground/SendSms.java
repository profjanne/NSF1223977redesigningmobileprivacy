package my.applicationforeground;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SendSms extends Activity {
	Button btnSendSMS;
	EditText txtPhoneNumber;
	EditText txtMessage;
	private SharedPreferences prefs;
	private SharedPreferences prefsReply;
	String msg;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_sms_auto_reply);
		setContentView(R.layout.smslayout);
		btnSendSMS = (Button) findViewById(R.id.btnSendSMS);
		txtPhoneNumber = (EditText) findViewById(R.id.txtPhoneNo);
		txtMessage = (EditText) findViewById(R.id.txtMessage);
		btnSendSMS.setOnClickListener(new sendClickListener());
		// app = (SmsApp) getApplication();

	}

	class sendClickListener implements OnClickListener {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			String phoneNumber = Constants.SMSNum;
			msg = txtMessage.getText().toString();
			if (phoneNumber.length() > 0 && msg.length() > 0) {
				new Thread(new Runnable() {
					public void run() {
						sendSMS(Constants.SMSNum, msg);
					}
				}).start();
				finish();
			}
//			else
//				Toast.makeText(SendSms.this,
//						"Please enter phone number and message.",
//						Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	// check if phone in busy mode set by the settingsAutoSms

	public void sendSMS(String phoneNumber, String message) {
//		 PendingIntent pi = PendingIntent.getActivity(this, 0, new
//		 Intent(this,SendSms.class), 0);
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, null, null);
	}
}
