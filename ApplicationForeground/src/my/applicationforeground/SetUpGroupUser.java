package my.applicationforeground;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;

import my.appforedata.AppendGroupUserID;
import my.appforedata.GroupUserIdDao;
import my.appforedata.GroupUserIdRecord;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SetUpGroupUser extends Activity {
	EditText editTen, edit, editGroup;
	Button btn;
	boolean check = false;
	int userid = 0;
	String groupid = "0";
	String deviceID = "0";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup_group_user);
		editTen = (EditText) findViewById(R.id.etxtTen);
		edit = (EditText) findViewById(R.id.etxt);
		edit.setFocusable(true);
		editGroup = (EditText) findViewById(R.id.etxtGroup);
		editGroup.setFocusable(true);
		btn = (Button) findViewById(R.id.btnOK);
		btn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				String ten = editTen.getText().toString();
				String bit = edit.getText().toString();
				String bitgroup = editGroup.getText().toString();
				if (checkEmpty(editTen))
					ten = "0";
				if (checkEmpty(edit)) {
					showAlertBit(0);
					return;
				} else if ((checkEmpty(editTen) && bit.equals("0"))) {
					showAlertBit(1);
					return;
				} else if ((ten.equals("0") && bit.equals("0"))) {
					showAlertBit(2);
					return;
				}
				if (checkEmpty(editGroup)) {
					showAlertGroup();
					return;
				}
				userid = (new DataTypeTransfer().StrintToInt(ten)) * 10
						+ (new DataTypeTransfer().StrintToInt(bit));
				groupid = bitgroup;
				System.out.println("user" + userid + "group:" + groupid);
				checkAlert();
				// return to the previous activity start this activity
				// automatically
			}
		});
	}

	public void checkAlert() {

		new AlertDialog.Builder(SetUpGroupUser.this)
				.setTitle(
						"The userID is:" + String.valueOf(userid) + " "
								+ "groupID is:" + String.valueOf(groupid))
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						check = true;
						dialog.dismiss();
						getDeviceID();
						new Thread(new Runnable() {
							
							public void run() {
								// TODO Auto-generated method stub
								new AppendGroupUserID().setAppendID(String.valueOf(groupid),String.valueOf(userid));
								writeIDtoDb();
							}
						}).start();						
						setIDSharePreference();
						finish();
					}
				})
				.setNegativeButton("Reset",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								edit.setText("");
								editTen.setText("");
								editGroup.setText("");
								check = false;
								dialog.dismiss(); // 取消对话框
							}
						}).create().show();
	}

	/**
	 * GETDEVICEID THEN WRITE TO LOCAL DB
	 */
	
	public void getDeviceID() {
		deviceID = new DeviceID(SetUpGroupUser.this).getDeviceId();
		//if deviceID null or no content, we set the groupIDuserID as the deviceID
		if (deviceID == null || deviceID.length() < 1) {
			StringBuffer buf = new StringBuffer();
			buf.append("usergroupid");
			buf.append(userid);
			buf.append(groupid);
			deviceID = buf.toString();
		}
	}
/**
 * save enter groupid userid to db, we delete all before save
 * so that the gourpuser table just have only one row
 */
	
	public void writeIDtoDb() {

		ServiceDbHelper dbHelper = new ServiceDbHelper(getApplicationContext());
		// get the db connection
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String date=new FormatTime().getStringYMDHMSFormateDate();
		GroupUserIdRecord record = new GroupUserIdRecord(String.valueOf(groupid), String.valueOf(userid),
				deviceID,date);
		GroupUserIdDao dao = new GroupUserIdDao(db);
//		dao.deleteAll(db);
		dao.save(record);
		ArrayList<GroupUserIdRecord> listID = new ArrayList<GroupUserIdRecord>();
		listID = (ArrayList<GroupUserIdRecord>) dao.getAll();
		Iterator<GroupUserIdRecord> iterator = listID.iterator();
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("itemid,groupid,userid,deviceid,date" + "\n");
		while (iterator.hasNext()) {
			GroupUserIdRecord item = iterator.next();
			strbuf.append(item.getId());
			strbuf.append(",");
			strbuf.append(item.getGroupId());
			strbuf.append(",");
			strbuf.append(item.getUserId());
			strbuf.append(",");
			strbuf.append(item.getDeviceId());
			strbuf.append(",");
			strbuf.append(item.getDate());
			strbuf.append("\n");
		}
		strbuf.append(Constants.DBREADFLAG);
		// record=dao.get(0);

		// int group=record.getGroupId();
		// int user=record.getUserId();
		writeDbReadID(strbuf);
		db.close();
	}

	public void setIDSharePreference() {
		
		GetSharePrefer prefer=new GetSharePrefer(SetUpGroupUser.this);
		prefer.setUserID(userid);
		prefer.setGroupID(groupid);
	}
	public void writeDbReadID(StringBuffer strbuf) {

		if (FileUtil.isExternalStorageWritable()) {
			// File dir = FileUtil.getExternalFilesDirAllApiLevels(this
			// .getPackageName());
			// write in the android dir not the pkg dir
			File dir = FileUtil.getExternalDirAn();
			File file = new File(dir, "DbReadGroupUser.txt");
			try {
				// synchronized (DATA_LOCK)
				{
					if (!file.exists())
						file.createNewFile(); // ok if returns false, overwrite
					if ((file != null) && file.canWrite()) {
						Writer out = new BufferedWriter(new FileWriter(file,
								Constants.DBREADFILE), 1024);
						out.write(strbuf.toString());
						out.close();
						// result = true;
					}
				}
			} catch (IOException e) {
				Log.i("Log",
						"Error appending string data to file " + e.getMessage());
			}
		} else {
			Log.i("LOG", "EXTERNAL NOT WRITABLE");
		}
	}

	public void showAlertGroup() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				SetUpGroupUser.this);
		builder.setMessage("Group can not be empty!");
		editGroup.requestFocus();
		Dialog dialog = builder.create();
		dialog.show();
	}

	public void showAlertBit(int flag) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				SetUpGroupUser.this);
		switch (flag) {
		case 0:
			builder.setMessage("UserId units digit Can NOT be empty");
			break;
		case 1:
			builder.setMessage("UserId units digit is from 1-9, if you Do NOT enter the Tens digit");
			break;
		case 2:
			builder.setMessage("UserId units digit is from 0-9, if you HAVE entered the decimal number");
			break;

		default:
			break;
		}
		edit.requestFocus();
		Dialog dialog = builder.create();
		dialog.show();
	}

	private boolean checkEmpty(EditText etText) {
		if (etText.getText().toString().trim().length() > 0)
			return false;
		else
			return true;
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
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
