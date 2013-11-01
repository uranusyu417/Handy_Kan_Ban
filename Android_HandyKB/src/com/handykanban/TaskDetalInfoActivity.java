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
				tempTaskOwner = HandyKBDBHelper.getDBHelperInstance()
						.getUserByTask(tempTask);

				tempTaskOldStatus = tempTask.getStatus();

				enterEditMode();
				showTaskInfoDisplay();
				break;
			}

			default:
			}
		}
	}

	private void showHintAndFinish()

	{
		Toast.makeText(this, R.string.taskinfo_dbupdate, Toast.LENGTH_SHORT)
				.show();
		finish();
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
			// int newTaskId;

			newTask.setTitle(editTextTaskTitle.getText().toString());
			newTask.setDetail(editTextTaskDetail.getText().toString());
			newTask.setPriority(Priority.intToPriority(spinnerTaskPri
					.getSelectedItemPosition() + 1));
			newTask.setStatus(Status.intToStatus(spinnerTaskStatus
					.getSelectedItemPosition()));

			newTask.setOwnerID(tempUser.getUserID());

			newTask.setProjectID(LoginSession.getInstance().getActiveProject()
					.getProjectID());

			// newTaskId = HandyKBDBHelper.getDBHelperInstance().addNewTask(
			HandyKBDBHelper.getDBHelperInstance().addNewTask(newTask);

			showHintAndFinish();

			return;
		}
		case EDIT_MODE: {
			User tempUser = (User) spinnerTaskAssingee.getSelectedItem();

			String dateStr = this.getCurrentDate();

			if (tempTaskOldStatus.toString().equals(
					getResources().getStringArray(R.array.taskinfo_status)[1])) {
				// if task status is to do, we can only change it to ONGOING
				if (!Status
						.intToStatus(
								spinnerTaskStatus.getSelectedItemPosition())
						.toString()
						.equals(getResources().getStringArray(
								R.array.taskinfo_status)[2])) {
					Toast.makeText(this, R.string.taskinfo_status_ongoing,
							Toast.LENGTH_LONG).show();
					return;
				}

				// change the startDate
				tempTask.setStartDate(dateStr);
			} else if (tempTaskOldStatus.toString().equals(
					getResources().getStringArray(R.array.taskinfo_status)[2])) {
				// if task status is ONGOING, we can only change it to DONE
				if (!Status
						.intToStatus(
								spinnerTaskStatus.getSelectedItemPosition())
						.toString()
						.equals(getResources().getStringArray(
								R.array.taskinfo_status)[3])) {
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

			showHintAndFinish();

			break;
		}
		default:
			break;
		}

	}

	private void updateTaskOwner() {

		// only status is in to do or ONGOING
		if ((!tempTaskOldStatus.toString().equals(
				getResources().getStringArray(R.array.taskinfo_status)[1]))
				&& (!tempTaskOldStatus.toString()
						.equals(getResources().getStringArray(
								R.array.taskinfo_status)[2]))) {
			Toast.makeText(this, R.string.task_assignme_reject,
					Toast.LENGTH_LONG).show();
			return;
		}

		User tempUser = LoginSession.getInstance().getLoggedInUser();
		tempTask.setOwnerID(tempUser.getUserID());
		HandyKBDBHelper.getDBHelperInstance().updateTaskInfo(tempTask);

		showHintAndFinish();
	}

	private void enterEditMode() {
		task_mode = EDIT_MODE;

		User LoginUser = LoginSession.getInstance().getLoggedInUser();
		
		if (tempTaskOldStatus.toString().equals(
				getResources().getStringArray(R.array.taskinfo_status)[3])) {
			// read only for everyone
			spinnerTaskAssingee.setEnabled(false);
			spinnerTaskPri.setEnabled(false);
			spinnerTaskStatus.setEnabled(false);

			editTextTaskDetail.setEnabled(false);
			editTextTaskTitle.setEnabled(false);

			buttonAssignToMe.setVisibility(Button.INVISIBLE);
			buttonTaskUpdate.setVisibility(Button.INVISIBLE);
			return;
		}

		if (LoginUser.isPoRole()) {
			spinnerTaskAssingee.setEnabled(false);
			spinnerTaskPri.setEnabled(true);
			spinnerTaskStatus.setEnabled(false);

			editTextTaskDetail.setEnabled(true);
			editTextTaskTitle.setEnabled(true);

			buttonAssignToMe.setVisibility(Button.INVISIBLE);
			buttonTaskUpdate.setVisibility(Button.VISIBLE);
		} else if (LoginUser.isDesignerRole()) {
			if (LoginUser.getUserID() == tempTaskOwner.getUserID()) {
				spinnerTaskAssingee.setEnabled(false);
				spinnerTaskPri.setEnabled(false);
				spinnerTaskStatus.setEnabled(true);

				editTextTaskDetail.setEnabled(false);
				editTextTaskTitle.setEnabled(false);

				buttonAssignToMe.setVisibility(Button.INVISIBLE);
				buttonTaskUpdate.setVisibility(Button.VISIBLE);
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

	private String getCurrentDate() {
		Calendar cal = Calendar.getInstance();

		// year
		String dateStr = cal.get(Calendar.YEAR) + "-";

		// month
		switch (cal.get(Calendar.MONTH)) {
		case Calendar.JANUARY:
			dateStr += "1";
			break;
		case Calendar.FEBRUARY:
			dateStr += "2";
			break;
		case Calendar.MARCH:
			dateStr += "3";
			break;
		case Calendar.APRIL:
			dateStr += "4";
			break;
		case Calendar.MAY:
			dateStr += "5";
			break;
		case Calendar.JUNE:
			dateStr += "6";
			break;
		case Calendar.JULY:
			dateStr += "7";
			break;
		case Calendar.AUGUST:
			dateStr += "8";
			break;
		case Calendar.SEPTEMBER:
			dateStr += "9";
			break;
		case Calendar.OCTOBER:
			dateStr += "10";
			break;
		case Calendar.NOVEMBER:
			dateStr += "11";
			break;
		case Calendar.DECEMBER:
			dateStr += "12";
			break;
		default:
		}
		// day of month
		dateStr += "-" + cal.get(Calendar.DAY_OF_MONTH);

		return dateStr;
	}
}
