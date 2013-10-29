package com.handykanban;

import java.util.ArrayList;
import java.util.Calendar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

public class KanBanUIActivity extends Activity {
	
	private Spinner spinnerProject;
	private EditText editTextNote;
	private ToggleButton toggleButtonEditNote;
	private TextView textViewDate;
	private LinearLayout linearLayoutKanBanTasks;

	
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
		linearLayoutKanBanTasks = (LinearLayout)findViewById(R.id.linearLayoutKanBanTasks);
		
		//set spinner action to switch logged in project
		spinnerProject.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Spinner s = (Spinner)arg0;
				Project p = (Project)s.getSelectedItem();
				LoginSession.getInstance().setActiveProject(p);
				initTaskInfo();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// do nothing
			}

		});

		//set toggle button to control note editable or not
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
		
		// update note editable state according to toggle button
		stashedKL = editTextNote.getKeyListener();
		if (!toggleButtonEditNote.isChecked()) {
			editTextNote.setKeyListener(null);
		}	

		initSpinnerProject();			
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
		//load projects info into spinner
		User loggedinuser = LoginSession.getInstance().getLoggedInUser();
		ArrayList<Project> prjs = HandyKBDBHelper.getDBHelperInstance().getProjectsByUserID(loggedinuser.getUserID());
		ArrayAdapter<Project> prj_adp = new ArrayAdapter<Project>(this,
				android.R.layout.simple_spinner_item, prjs);
		prj_adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerProject.setAdapter(prj_adp);
		
		//initialize task info based on active project.
		initTaskInfo();
	}
	
	/**
	 * load and init task UI
	 */
	private void initTaskInfo()
	{
		//clear UI control first
		linearLayoutKanBanTasks.removeAllViews();
		
		Project active_prj = LoginSession.getInstance().getActiveProject();
		
		if(active_prj!=null)
		{			
			//todo
			ArrayList<Task> ts = HandyKBDBHelper.getDBHelperInstance().getTasksByProjectIDAndStatus(
                                                                       active_prj.getProjectID(), 
                                                                       Task.Status.TODO);
			TaskGroupLinearLayoutForKB tgll = new TaskGroupLinearLayoutForKB(this, ts);
			linearLayoutKanBanTasks.addView(tgll);
			
			//OnGoing
			ArrayList<Task> ots = HandyKBDBHelper.getDBHelperInstance().getTasksByProjectIDAndStatus(
                    active_prj.getProjectID(), 
                    Task.Status.ONGOING);
			TaskGroupLinearLayoutForKB otgll = new TaskGroupLinearLayoutForKB(this, ots);
			linearLayoutKanBanTasks.addView(otgll);
			
			//Done
			ArrayList<Task> dts = HandyKBDBHelper.getDBHelperInstance().getTasksByProjectIDAndStatus(
                    active_prj.getProjectID(), 
                    Task.Status.DONE);
			TaskGroupLinearLayoutForKB dtgll = new TaskGroupLinearLayoutForKB(this, dts);
			linearLayoutKanBanTasks.addView(dtgll);
		}
	}
	
	private void restartSelf()
	{
		Intent intent = getIntent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		finish();
		startActivity(intent);
	}
	

	//TODO implement option menu depending on different loggedin user role
}
