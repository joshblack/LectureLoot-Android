package com.lectureloot.android;

import java.util.ArrayList;

public class NewCourse {

	private ArrayList<String> deptCodes = new ArrayList<String>();
	
	public NewCourse() {
		this.deptCodes.add("CEN");
		this.deptCodes.add("EGN");
		this.deptCodes.add("CAP");
	}

	public ArrayList<String> getDeptCodes() {
		return deptCodes;
	}

	public void setDeptCodes(ArrayList<String> deptCodes) {
		this.deptCodes = deptCodes;
	}
	
	
}
