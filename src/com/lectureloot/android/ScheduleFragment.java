package com.lectureloot.android;

import com.lectureloot.android.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;


public class ScheduleFragment extends Fragment {
	
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);
		
//        // get the listview
       expListView = (ExpandableListView) rootView.findViewById(R.id.lvExp);
// 
//        // preparing list data
//        prepareListData();
// 
        listAdapter = new ExpandableListAdapter(rootView.getContext(), listDataHeader, listDataChild);
// 
//        // setting list adapter
        expListView.setAdapter(listAdapter);
		
		return rootView;
	}

	
//    private void prepareListData() {
//        listDataHeader = new ArrayList<String>();
//        listDataChild = new HashMap<String, List<String>>();
// 
//        // Adding child data
//        listDataHeader.add("CEN3031");
//        listDataHeader.add("CAP4053");
//        listDataHeader.add("EGN4641");
// 
//        // Adding child data
//        List<String> cen3031 = new ArrayList<String>();
//        cen3031.add("Dummy Data");
//
// 
//        List<String> cap4053 = new ArrayList<String>();
//        cap4053.add("Dummy Data");
//
// 
//        List<String> egn4641 = new ArrayList<String>();
//        egn4641.add("Dummy Data");
//
// 
//        listDataChild.put(listDataHeader.get(0), cen3031); // Header, Child data
//        listDataChild.put(listDataHeader.get(1), cap4053);
//        listDataChild.put(listDataHeader.get(2), egn4641);
//    }
//	
}
