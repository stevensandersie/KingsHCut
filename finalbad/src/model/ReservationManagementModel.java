package model;

import java.sql.Date;
import java.sql.Time;

public class ReservationManagementModel {
	
	private String id, status;
	private Date date;
	private Time start, end;
	public ReservationManagementModel(String id, Date date, Time start, Time end, String status) {
		super();
		this.id = id;
		this.status = status;
		this.date = date;
		this.start = start;
		this.end = end;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Time getStart() {
		return start;
	}
	public void setStart(Time start) {
		this.start = start;
	}
	public Time getEnd() {
		return end;
	}
	public void setEnd(Time end) {
		this.end = end;
	}
	
	
}
