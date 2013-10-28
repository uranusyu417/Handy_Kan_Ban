package com.handykanban;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class TaskDetalInfoActivity extends Activity {

	Spinner spinnerTaskAssingee, spinnerTaskPri, spinnerTaskStatus;
	EditText editTextTaskDetail, editTextTaskTitle;
	Button buttonTaskUpdate, buttonAssignToMe;
	
	Task temp_task = new Task();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_detal_info);
		
		showTaskInfoDisplay();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.task_detal_info, menu);
		return true;
	}
	
	
	/**
	 * get all from database
	 */
	private void showTaskInfoDisplay()
	{
		spinnerTaskAssingee = (Spinner)findViewById(R.id.spinnerTaskAssingee);
		spinnerTaskPri = (Spinner)findViewById(R.id.spinnerTaskPriority);
		spinnerTaskStatus = (Spinner)findViewById(R.id.spinnerTaskStatus);
		
		editTextTaskDetail = (EditText)findViewById(R.id.editTextTaskDetail);
		editTextTaskTitle = (EditText)findViewById(R.id.editTextTaskTitle);
		
		buttonTaskUpdate = (Button)findViewById(R.id.buttonTaskUpdate);
		buttonAssignToMe = (Button)findViewById(R.id.buttonAssignToMe);	
		
	}
	
	private void storeTaskData()
	{
	
	}
}
