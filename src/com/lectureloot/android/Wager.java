package com.lectureloot.android;

// author: David Bai

public class Wager {

	private int wagerSessionCode;
	private int wagerPerMeeting;
	private int totalMeetings;
	private int totalWager;
	private int currentWagerLost;
	
	
	public Wager () {
		
	}
	
	public Wager(int wagerSessionCode, int wagerPerMeeting, int totalMeetings, int totalWager, int currentWagerLost) {
		
		this.wagerSessionCode = wagerSessionCode;
		this.wagerPerMeeting = wagerPerMeeting;
		this.totalMeetings = totalMeetings;
		this.totalWager = totalWager;
		this.currentWagerLost = currentWagerLost;
	}

	public String getWagerSessionCodeString() {
		return String.valueOf(wagerSessionCode);
	}
	
	public int getWagerSessionCode() {
		return wagerSessionCode;
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

	public int getCurrentWagerLost() {
		return currentWagerLost;
	}

	public void setCurrentWagerLost(int currentWagerLost) {
		this.currentWagerLost = currentWagerLost;
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
	
}
