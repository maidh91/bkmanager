package com.cvteam.bkmanager;

import org.holoeverywhere.widget.TextView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class myFragment extends Fragment{
	String value;
	Boolean flag;
	
	public myFragment(){
	}
	public void setContent(String value,Boolean flag){
		this.value = value;
		this.flag = flag;
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view;
		if(flag)
			view = inflater.inflate(R.layout.view_pager_fragment,null,false);
		else
			view = inflater.inflate(R.layout.view_pager_fragment_non,null,false);
		TextView tv = (TextView) view.findViewById(R.id.textViewContent);
		tv.setText(value);
    	return view;
    }
}