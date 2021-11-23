package schedule;

public class UserScheduleBean {
	private String userId;
	private String lectureNumber;
	
	public UserScheduleBean(String userId, String lectureName){
		this.setUserId(userId);
		this.setLectureNumber(lectureName);
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getLectureNumber() {
		return lectureNumber;
	}
	public void setLectureNumber(String lectureNumber) {
		this.lectureNumber = lectureNumber;
	}
}
