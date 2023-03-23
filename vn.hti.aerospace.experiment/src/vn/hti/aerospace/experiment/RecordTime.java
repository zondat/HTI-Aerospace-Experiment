package vn.hti.aerospace.experiment;

public class RecordTime {
	
	private int year = 2023;
	private int month;
	private int day;
	private int hour;
	private int minute;
	private int second;
	
	public RecordTime() {
		
	}
	
	public RecordTime(int year, int month, int day, int hour, int minute, int second) {
		this.year = year;
		this.month = month;
		this.day = day;
		this.hour = hour;
		this.minute = minute;
		this.second = second;
	}
	
	public static RecordTime getTime(RecordTime startTime, int duration) {
		int newSecond = (startTime.getSecond() + duration) % 60;
		int durationInMinute = (startTime.getSecond() + duration) / 60;
		int newMinute = durationInMinute % 60;
		int durationInHour = durationInMinute / 60;
		int newHour = durationInHour % 24;
		
		return new RecordTime(startTime.getYear(), startTime.getMonth(), startTime.getDay(), newHour, newMinute, newSecond);
	}
	
	
	public int getYear() {
		return year;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
	
	public int getMonth() {
		return month;
	}
	
	public void setMonth(int month) {
		this.month = month;
	}
	
	public int getDay() {
		return day;
	}
	
	public void setDay(int day) {
		this.day = day;
	}
	
	public int getHour() {
		return hour;
	}
	
	public void setHour(int hour) {
		this.hour = hour;
	}
	
	public int getMinute() {
		return minute;
	}
	public void setMinute(int minute) {
		this.minute = minute;
	}
	public int getSecond() {
		return second;
	}
	public void setSecond(int second) {
		this.second = second;
	}
	
}