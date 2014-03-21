package com.lectureloot.android;

// author: David Bai

public class Wager {

	private int wagerId; 		
	private int wagerSessionCode;
	private int wagerPerMeeting;
	private int totalMeetings;
	private int totalWager;
	
	public Wager () {
		
	}
	
	public Wager(int wagerId, int sessionId, int unitValue, int totalValue, int pointsLost){
			//TODO: This is the data that I can get from the server. 
			// Somehow you have to turn this into a wager. Good luck.
	}
			
	public Wager(int wagerSessionCode, int wagerPerMeeting, int totalMeetings, int totalWager) {
		
		this.wagerSessionCode = wagerSessionCode;
		this.wagerPerMeeting = wagerPerMeeting;
		this.totalMeetings = totalMeetings;
		this.totalWager = totalWager;
	}

	public String getWagerSessionCodeString() {
		return String.valueOf(wagerSessionCode);
	}
	
	public int getWagerSessionCode() {
		return wagerSessionCode;
	}
	
	public int getWagerId(){
		return wagerId;
	}

	public void setWagerSessionCode(int wagerSessionCode) {
		this.wagerSessionCode = wagerSessionCode;
	}

	public int getWagerPerMeeting() {
		return wagerPerMeeting;
	}

	public void setWagerPerMeeting(int wagerPerMeeting) {
		this.wagerPerMeeting = wagerPerMeeting;
	}

	public int getTotalMeetings() {
		return totalMeetings;
	}

	public void setTotalMeetings(int totalMeetings) {
		this.totalMeetings = totalMeetings;
	}

	public int getTotalWager() {
		return totalWager;
	}

	public void setTotalWager(int totalWager) {
		this.totalWager = totalWager;
	}	

	public void setWagerId(int wagerId){
		this.wagerId = wagerId;
	}
	
}
