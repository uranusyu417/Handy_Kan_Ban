package com.handykanban;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.handykanban.Task.Priority;
import com.handykanban.Task.Status;

public class TaskDetalInfoActivity extends Activity implements OnClickListener {

	private Spinner spinnerTaskAssingee, spinnerTaskPri, spinnerTaskStatus;
	private EditText editTextTaskDetail, editTextTaskTitle;
	private Button buttonTaskUpdate;
	private Button buttonAssignToMe;

	private static int taskId = 1;
	private static Task tempTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_detal_info);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		tempTask = HandyKBDBHelper.getDBHelperInstance().getTaskById(taskId);

		spinnerTaskAssingee = (Spinner) findViewById(R.id.spinnerTaskAssingee);
		spinnerTaskPri = (Spinner) findViewById(R.id.spinnerTaskPriority);
		spinnerTaskStatus = (Spinner) findViewById(R.id.spinnerTaskStatus);

		editTextTaskDetail = (EditText) findViewById(R.id.editTextTaskDetail);
		editTextTaskTitle = (EditText) findViewById(R.id.editTextTaskTitle);

		buttonTaskUpdate = (Button) findViewById(R.id.buttonTaskUpdate);
		buttonAssignToMe = (Button) findViewById(R.id.buttonAssignToMe);

		buttonTaskUpdate.setOnClickListener(this);		
		buttonAssignToMe.setOnClickListener(this);
		
		showTaskInfoDisplay();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.task_detal_info, menu);
		return true;
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttonTaskUpdate:
			storeTaskData();
			break;
		case R.id.buttonAssignToMe:
			storeTaskData();
			break;
		default:
			break;
		}
	}

	/**
	 * get all from database
	 */
	private void showTaskInfoDisplay() {

		editTextTaskTitle.setText(tempTask.getTitle());
		editTextTaskDetail.setText(tempTask.getDetail());

		// prepare task priority
		ArrayAdapter<CharSequence> adapter_task_priority = ArrayAdapter
				.createFromResource(this, R.array.taskinfo_priority,
						android.R.layout.simple_spinner_item);
		adapter_task_priority
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerTaskPri.setAdapter(adapter_task_priority);
		spinnerTaskPri.setSelection(Priority.PriorityToInt(tempTask
				.getPriority())-1);

		// prepare task status
		ArrayAdapter<CharSequence> adapter_task_status = ArrayAdapter
				.createFromResource(this, R.array.taskinfo_status,
						android.R.layout.simple_spinner_item);
		adapter_task_status
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerTaskStatus.setAdapter(adapter_task_status);
		spinnerTaskStatus
				.setSelection(Status.StatusToInt(tempTask.getStatus()));

		// prepare task Assingee
		User tempUser = HandyKBDBHelper.getDBHelperInstance().getUserByTask(
				tempTask);
		ArrayAdapter<User> userAdp = new ArrayAdapter<User>(this,
				android.R.layout.simple_spinner_item, HandyKBDBHelper
						.getDBHelperInstance().getUsersByProject(
								tempTask.getProjectID()));
		userAdp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerTaskAssingee.setAdapter(userAdp);
		spinnerTaskAssingee.setSelection(userAdp.getPosition(tempUser));
	}

	private void storeTaskData() {
		User tempUser = (User) spinnerTaskAssingee.getSelectedItem();

		tempTask.setTitle(editTextTaskTitle.getText().toString());
		tempTask.setDetail(editTextTaskDetail.getText().toString());
		tempTask.setPriority(Priority.intToPriority(spinnerTaskPri
				.getSelectedItemPosition()+1));
		tempTask.setStatus(Status.intToStatus(spinnerTaskStatus
				.getSelectedItemPosition()));

		tempTask.setOwnerID(tempUser.getUserID());

		HandyKBDBHelper.getDBHelperInstance().updateTaskInfo(tempTask);
	}
}
