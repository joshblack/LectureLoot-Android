package com.lectureloot.android;

import java.util.Date;

public class Sessions {

	private int SessionId;
	private Date startDate;
	private Date endDate;

	
	public Sessions()
	{

	}

	public Sessions(int SessionId, Date startDate, Date endDate)
	{
		this.SessionId = SessionId;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public boolean equals(Object o){
		Sessions s;
		try{
			s = (Sessions) o;
		} catch (ClassCastException e){ return false; }
		return (SessionId == s.SessionId && startDate.equals(s.startDate) && endDate.equals(s.endDate));
	}
	
	public int getSessionId() {
		return SessionId;
	}

	public void setSessionId(int sessionId) {
		SessionId = sessionId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}



