package my.appforedata;

import my.applicationforeground.RecordTemplate;

public class GroupUserIdRecord extends RecordTemplate {

	private String groupId;
	private String userId;
	private String deviceId;
	private String date;

	public GroupUserIdRecord() {

	}

	public GroupUserIdRecord(String groupId, String userId, String deviceId,String date) {
		this.groupId = groupId;
		this.userId = userId;
		this.deviceId = deviceId;
		this.date=date;
	}

	public String getGroupId() {
		return this.groupId;
	}

	public void setGroupId(String groupID) {
		this.groupId = groupID;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userID) {
		this.userId = userID;
	}

	public String getDeviceId() {
		return this.deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getDate() {
		return this.date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
