package com.handykanban;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.handykanban.Task.Priority;
import com.handykanban.Task.Status;

public class TaskDetalInfoActivity extends Activity implements OnClickListener {

	static final int CREATE_MODE = 1;
	static final int EDIT_MODE = 2;

	int task_mode; // 1 - create 2 - edit

	private Spinner spinnerTaskAssingee, spinnerTaskPri, spinnerTaskStatus;
	private EditText editTextTaskDetail, editTextTaskTitle;
	private Button buttonTaskUpdate;
	private Button buttonAssignToMe;

	private int tempTaskId;
	private User tempTaskOwner;
	private Task tempTask;
	private Status tempTaskOldStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_detal_info);
	}

	@Override
	protected void onResume() {
		super.onResume();

		spinnerTaskAssingee = (Spinner) findViewById(R.id.spinnerTaskAssingee);
		spinnerTaskPri = (Spinner) findViewById(R.id.spinnerTaskPriority);
		spinnerTaskStatus = (Spinner) findViewById(R.id.spinnerTaskStatus);

		editTextTaskDetail = (EditText) findViewById(R.id.editTextTaskDetail);
		editTextTaskTitle = (EditText) findViewById(R.id.editTextTaskTitle);

		buttonTaskUpdate = (Button) findViewById(R.id.buttonTaskUpdate);
		buttonAssignToMe = (Button) findViewById(R.id.buttonAssignToMe);

		buttonTaskUpdate.setOnClickListener(this);
		buttonAssignToMe.setOnClickListener(this);

		Bundle b = getIntent().getExtras();
		if (b != null) {
			switch (b.getInt("TASK_MODE")) {
			case CREATE_MODE: // create new task
				enterCreateNewMode();
				prepareCreateTaskInfo();
				break;

			case EDIT_MODE: {
				tempTaskId = b.getInt("TASK_ID");
				tempTask = HandyKBDBHelper.getDBHelperInstance().getTaskById(
						tempTaskId);

				tempTaskOldStatus = tempTask.getStatus();

				enterEditMode();
				showTaskInfoDisplay();
				break;
			}

			default:
			}
		}
	}

	private void restartSelf(int newTaskId)

	{

		Intent intent = getIntent();
		Bundle b = intent.getExtras();

		b.putInt("TASK_MODE", EDIT_MODE);
		b.putInt("TASK_ID", newTaskId);

		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

		finish();

		startActivity(intent);

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
			udpateTaskData();
			break;
		case R.id.buttonAssignToMe:
			updateTaskOwner();
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
				.getPriority()) - 1);

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
		tempTaskOwner = HandyKBDBHelper.getDBHelperInstance().getUserByTask(
				tempTask);

		ArrayAdapter<User> userAdp = new ArrayAdapter<User>(this,
				android.R.layout.simple_spinner_item, HandyKBDBHelper
						.getDBHelperInstance().getUsersByProject(
								tempTask.getProjectID()));
		userAdp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerTaskAssingee.setAdapter(userAdp);
		spinnerTaskAssingee.setSelection(userAdp.getPosition(tempTaskOwner));
	}

	private void prepareCreateTaskInfo() {

		editTextTaskTitle.setText(null);
		editTextTaskDetail.setText(null);

		// prepare task priority
		ArrayAdapter<CharSequence> adapter_task_priority = ArrayAdapter
				.createFromResource(this, R.array.taskinfo_priority,
						android.R.layout.simple_spinner_item);
		adapter_task_priority
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerTaskPri.setAdapter(adapter_task_priority);
		spinnerTaskPri.setSelection(1);

		// prepare task status
		ArrayAdapter<CharSequence> adapter_task_status = ArrayAdapter
				.createFromResource(this, R.array.taskinfo_status,
						android.R.layout.simple_spinner_item);
		adapter_task_status
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerTaskStatus.setAdapter(adapter_task_status);
		spinnerTaskStatus.setSelection(0);

		// prepare task Assingee, only PO can create task
		ArrayList<User> tempUsers = new ArrayList<User>();
		tempUsers.add(LoginSession.getInstance().getLoggedInUser());

		ArrayAdapter<User> userAdp = new ArrayAdapter<User>(this,
				android.R.layout.simple_spinner_item, tempUsers);
		userAdp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerTaskAssingee.setAdapter(userAdp);
		spinnerTaskAssingee.setSelection(userAdp.getPosition(LoginSession
				.getInstance().getLoggedInUser()));
	}

	private void udpateTaskData() {

		switch (task_mode) {
		case CREATE_MODE: {
			Task newTask = new Task();
			User tempUser = (User) spinnerTaskAssingee.getSelectedItem();
			int newTaskId;

			newTask.setTitle(editTextTaskTitle.getText().toString());
			newTask.setDetail(editTextTaskDetail.getText().toString());
			newTask.setPriority(Priority.intToPriority(spinnerTaskPri
					.getSelectedItemPosition() + 1));
			newTask.setStatus(Status.intToStatus(spinnerTaskStatus
					.getSelectedItemPosition()));

			newTask.setOwnerID(tempUser.getUserID());

			newTaskId = HandyKBDBHelper.getDBHelperInstance().addNewTask(
					newTask);

			restartSelf(newTaskId);

		}
		case EDIT_MODE: {
			User tempUser = (User) spinnerTaskAssingee.getSelectedItem();
			Calendar cal = Calendar.getInstance();
			String dateStr = cal.get(Calendar.YEAR) + "-"
					+ cal.get(Calendar.MONTH) + "-"
					+ cal.get(Calendar.DAY_OF_MONTH);

			if (tempTaskOldStatus.toString()
					.equals(getResources().getStringArray(
							R.array.taskinfo_priority)[1])) {
				// if task status is to do, we can only change it to ONGOING
				if (!tempTask
						.getStatus()
						.toString()
						.equals(getResources().getStringArray(
								R.array.taskinfo_priority)[2])) {
					Toast.makeText(this, R.string.taskinfo_status_ongoing,
							Toast.LENGTH_LONG).show();
					return;
				}

				// change the startDate
				tempTask.setStartDate(dateStr);
			} else if (tempTaskOldStatus.toString()
					.equals(getResources().getStringArray(
							R.array.taskinfo_priority)[2])) {
				// if task status is ONGOING, we can only change it to DONE
				if (!tempTask
						.getStatus()
						.toString()
						.equals(getResources().getStringArray(
								R.array.taskinfo_priority)[3])) {
					Toast.makeText(this, R.string.taskinfo_status_done,
							Toast.LENGTH_LONG).show();
					return;
				}

				// change the complete date
				tempTask.setCompleteDate(dateStr);
			}

			tempTask.setTitle(editTextTaskTitle.getText().toString());
			tempTask.setDetail(editTextTaskDetail.getText().toString());
			tempTask.setPriority(Priority.intToPriority(spinnerTaskPri
					.getSelectedItemPosition() + 1));
			tempTask.setStatus(Status.intToStatus(spinnerTaskStatus
					.getSelectedItemPosition()));

			tempTask.setOwnerID(tempUser.getUserID());

			HandyKBDBHelper.getDBHelperInstance().updateTaskInfo(tempTask);
		}
		default:
			break;
		}

	}

	private void updateTaskOwner() {

		// only status is in to do or ONGOING
		if ((!tempTaskOldStatus.toString().equals(
				getResources().getStringArray(R.array.taskinfo_priority)[1]))
				&& (!tempTaskOldStatus.toString().equals(
						getResources()
								.getStringArray(R.array.taskinfo_priority)[2]))) {
			Toast.makeText(this, R.string.task_assignme_reject,
					Toast.LENGTH_LONG).show();
			return;
		}

		User tempUser = (User) spinnerTaskAssingee.getSelectedItem();
		tempTask.setOwnerID(tempUser.getUserID());
		HandyKBDBHelper.getDBHelperInstance().updateTaskInfo(tempTask);
	}

	private void enterEditMode() {
		task_mode = EDIT_MODE;

		User LoginUser = LoginSession.getInstance().getLoggedInUser();

		if (LoginUser.getUserID() == tempTaskOwner.getUserID()) {
			if (LoginUser.isPoRole()) {
				spinnerTaskAssingee.setEnabled(false);
				spinnerTaskPri.setEnabled(true);
				spinnerTaskStatus.setEnabled(false);

				editTextTaskDetail.setEnabled(true);
				editTextTaskTitle.setEnabled(true);

				buttonAssignToMe.setVisibility(Button.INVISIBLE);
				buttonTaskUpdate.setVisibility(Button.VISIBLE);
			} else if (LoginUser.isDesignerRole()) {
				spinnerTaskAssingee.setEnabled(false);
				spinnerTaskPri.setEnabled(false);
				spinnerTaskStatus.setEnabled(true);

				editTextTaskDetail.setEnabled(false);
				editTextTaskTitle.setEnabled(false);

				buttonAssignToMe.setVisibility(Button.INVISIBLE);
				buttonTaskUpdate.setVisibility(Button.VISIBLE);
			}
		} else {
			spinnerTaskAssingee.setEnabled(false);
			spinnerTaskPri.setEnabled(false);
			spinnerTaskStatus.setEnabled(false);

			editTextTaskDetail.setEnabled(false);
			editTextTaskTitle.setEnabled(false);

			buttonAssignToMe.setEnabled(true);
			buttonTaskUpdate.setEnabled(false);
		}
	}

	private void enterCreateNewMode() {
		task_mode = CREATE_MODE;

		spinnerTaskAssingee.setEnabled(false);
		spinnerTaskPri.setEnabled(true);
		spinnerTaskStatus.setEnabled(false);

		editTextTaskDetail.setEnabled(true);
		editTextTaskTitle.setEnabled(true);

		buttonAssignToMe.setVisibility(Button.INVISIBLE);
		buttonTaskUpdate.setVisibility(Button.VISIBLE);
	}
}
