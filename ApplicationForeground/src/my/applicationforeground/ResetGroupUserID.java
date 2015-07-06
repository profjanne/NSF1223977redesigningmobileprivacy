package my.applicationforeground;

import my.appforedata.GroupUserIdDao;
import my.appforedata.GroupUserIdRecord;
import my.appforedata.GroupUserIdTable;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ResetGroupUserID extends Activity {
	EditText edit;
	Button btn;
	String passwd="1";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reset_groupuserid);
		edit = (EditText) findViewById(R.id.etxtreset);
		btn = (Button) findViewById(R.id.btnreset);
		btn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				passwd = edit.getText().toString();
				checkPassword();
			}
		});
	}

	public void checkPassword() {
		if (passwd.equals(DataConstants.PASSWORD)) {
			//set appendflag true, so that when resume call initid can check the flag and append new id
			new InitGroupUserID().setInitFlag(true);
			finish();
		} else {
			checkAlert();
		}
	}

	public void checkAlert() {

		new AlertDialog.Builder(ResetGroupUserID.this)
				.setTitle("Password wrong, do you want to continue try?")
				.setPositiveButton("YES", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						edit.setText("");
						dialog.dismiss();
					}
				})
				.setNegativeButton("NO",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss(); // 取消对话框
								finish();
							}
						}).create().show();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
