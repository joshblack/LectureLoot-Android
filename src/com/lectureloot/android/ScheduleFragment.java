package com.lectureloot.android;

/**
 * @author Austin Bruch, Justin Rafanan
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;


public class ScheduleFragment extends Fragment {
	
    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private List<String> listDataHeader;
    private HashMap<String, List<Course>> listDataChild;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);
		
		TextView userDisplay = (TextView)rootView.findViewById(R.id.username);
		//TODO: Set Display name dynamically based on Singleton User Model
		userDisplay.setText("Justin Rafanan's Schedule");
		userDisplay.setTypeface(null, Typeface.BOLD_ITALIC);
		userDisplay.setTextSize(25);
		
//        // get the listview
       expListView = (ExpandableListView) rootView.findViewById(R.id.lvExp);
// 
//        // preparing list data
        prepareListData();
// 
        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);
// 
//        // setting list adapter
        expListView.setAdapter(listAdapter);
		
		return rootView;
	}
	
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<Course>>();
        
        Course cen3031 = new Course();
        cen3031.setCourseCode("CEN3031");
        cen3031.setCourseTitle("INTRO SOFTWARE ENGR");
        cen3031.setSectionNumber(Integer.toString(5842));
        cen3031.setCredits(Integer.toString(3));
        cen3031.setInstructor("Bermudez, Manuel E");
        cen3031.setMeetingDays1("MWF");
        cen3031.setPeriod1(Integer.toString(6));
        cen3031.setRoom1("LIT 109");
        cen3031.setMeetingDays2("W");
        cen3031.setPeriod2(Integer.toString(7));
        cen3031.setRoom2("CSE E116");
        cen3031.setMeetingDays3(null);
        cen3031.setPeriod3(null);
        cen3031.setRoom3(null);
        
        List<Course> cen3031List = new ArrayList<Course>();
        cen3031List.add(cen3031);
        
        // Adding child data for CAP4053
        Course cap4053 = new Course("CAP4053", "AI FOR COMPUTER GAMES","133E","3","Anthony,Lisa, Dankel,Douglas D,II","MWF",null,null,"5", null, null, "CSE E119", null, null);
        List<Course> cap4053List = new ArrayList<Course>();
        cap4053List.add(cap4053);
        
        // Adding child data for EGN4641
        Course egn4641 = new Course("EGN4641", "ENG ENTREPRENEURSHIP","11AF","3","Sander, Erik J","R",null,null,"3-5", null, null, "NEB 0102", null, null);
        List<Course> egn4641List = new ArrayList<Course>();
        egn4641List.add(egn4641);
 
        Course newCourse = new Course();
        newCourse.setCourseCode("Add a Course");
        List<Course> newCourseList = new ArrayList<Course>();
        newCourseList.add(newCourse);
        
        // Adding child data 
        listDataHeader.add(cen3031.getCourseCode());
        listDataHeader.add(cap4053.getCourseCode());
        listDataHeader.add(egn4641.getCourseCode());
        listDataHeader.add(newCourse.getCourseCode());
 

        listDataChild.put(listDataHeader.get(0), cen3031List); // Header, Child data
        listDataChild.put(listDataHeader.get(1), cap4053List);
        listDataChild.put(listDataHeader.get(2), egn4641List);
        listDataChild.put(listDataHeader.get(3), newCourseList);
        
    }
}
