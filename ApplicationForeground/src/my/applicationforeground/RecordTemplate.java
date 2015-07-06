package my.applicationforeground;

public class RecordTemplate {
	public long id;
	private String userID;
	private String groupID;

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getGroupId() {
		return this.groupID;
	}

	public void setGroupId(String groupID) {
		this.groupID = groupID;
	}

	public String getUserId() {
		return this.userID;
	}

	public void setUserId(String userID) {
		this.userID = userID;
	}
}
