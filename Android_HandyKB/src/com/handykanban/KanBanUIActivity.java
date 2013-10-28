package com.handykanban;

import java.util.Calendar;
import android.app.Activity;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

public class KanBanUIActivity extends Activity {
	
	private Spinner spinnerProject;
	private EditText editTextNote;
	private ToggleButton toggleButtonEditNote;
	private TextView textViewDate;
	
	private KeyListener stashedKL;

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onContextItemSelected(item);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kanban_window_layout);
		
		spinnerProject = (Spinner)findViewById(R.id.spinnerProject);
		editTextNote = (EditText)findViewById(R.id.editTextNote);
		toggleButtonEditNote = (ToggleButton)findViewById(R.id.toggleButtonEditNote);
		textViewDate = (TextView)findViewById(R.id.textViewDate);				

		toggleButtonEditNote.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				//update note editable state
				boolean on = ((ToggleButton)arg0).isChecked();
				if(on)
				{
					editTextNote.setKeyListener(stashedKL);
				}
				else
				{
					editTextNote.setKeyListener(null);
					//TODO send note content to db for update
				}
			}});
		
		textViewDate.setText(getCurrentDate());
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	protected void onResume() {
		super.onResume();
		initSpinnerProject();
		
		// update note editable state according to toggle button
		stashedKL = editTextNote.getKeyListener();
		if (!toggleButtonEditNote.isChecked()) {
			editTextNote.setKeyListener(null);
		}		
	}
	
	/**
	 * display today's date
	 */
	private String getCurrentDate()
	{
		Calendar cal = Calendar.getInstance();
		//year
		String dateStr = "  Date:"+
		    cal.get(Calendar.YEAR)+"-";
		//month
		switch(cal.get(Calendar.MONTH))
		{
		case Calendar.JANUARY:
			dateStr += "1";
			break;
		case Calendar.FEBRUARY:
			dateStr+="2";
			break;
		case Calendar.MARCH:
			dateStr+="3";
			break;
		case Calendar.APRIL:
			dateStr+="4";
			break;
		case Calendar.MAY:
			dateStr+="5";
			break;
		case Calendar.JUNE:
			dateStr+="6";
			break;
		case Calendar.JULY:
			dateStr+="7";
			break;
		case Calendar.AUGUST:
			dateStr+="8";
			break;
		case Calendar.SEPTEMBER:
			dateStr+="9";
			break;
		case Calendar.OCTOBER:
			dateStr+="10";
			break;
		case Calendar.NOVEMBER:
			dateStr+="11";
			break;
		case Calendar.DECEMBER:
			dateStr+="12";
			break;
			default:
		}
		//day of month
		dateStr +="-"+cal.get(Calendar.DAY_OF_MONTH)+"  ";
		//day of week
		switch(cal.get(Calendar.DAY_OF_WEEK))
		{
		case Calendar.SUNDAY:
			dateStr += "Sunday";
			break;
		case Calendar.MONDAY:
			dateStr += "Monday";
			break;
		case Calendar.TUESDAY:
			dateStr += "Tuesday";
			break;
		case Calendar.WEDNESDAY:
			dateStr += "Wednesday";
			break;
		case Calendar.THURSDAY:
			dateStr += "Thursday";
			break;
		case Calendar.FRIDAY:
			dateStr += "Friday";
			break;
		case Calendar.SATURDAY:
			dateStr += "Saturday";
			break;
			default:
		}
		return dateStr;
	}
	
	/**
	 * load projects info to spinner
	 */
	private void initSpinnerProject()
	{
		//TODO get projects information based on loggedin user
		
		
		initTaskInfo();
	}
	
	/**
	 * load and init task UI
	 */
	private void initTaskInfo()
	{
		//TODO init task ui
	}
	
	//for test
	private void loadAllProjectInfoToSpinner()
	{
		ArrayAdapter<Project> prj_adp = new ArrayAdapter<Project>(this, 
				                              android.R.layout.simple_spinner_item,
				                              HandyKBDBHelper.getDBHelperInstance().getAllProjects());
		prj_adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerProject.setAdapter(prj_adp);
		
		spinnerProject.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				Spinner s = (Spinner)arg0;
				Project p = (Project)s.getSelectedItem();
				LoginSession.getInstance().setActiveProject(p);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
			
		});
	}

	//TODO implement option menu depending on different loggedin user role
}
