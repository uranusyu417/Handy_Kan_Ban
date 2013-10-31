package com.handykanban;


import com.handykanban.Task.Priority;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TaskLinearLayoutForKanBan extends LinearLayout implements View.OnClickListener {
	Task task;
	
	TextView TaskTitle;
	/* if status TO-DO or BACKLOG, do not show owner */
	TextView TaskOwner;
	/* if status is DONE, use complete date
	 * ONGOING, start date
	 * otherwise, do not show date */
	TextView TaskDate;
	TextView TaskId;
	LinearLayout linearLayoutTaskForKBMain;
	
	public TaskLinearLayoutForKanBan(Context context, Task _task)
	{
		super(context);
		postInit(context, _task);
	}
	
	public TaskLinearLayoutForKanBan(Context context, AttributeSet attrs, Task _task)
	{
		super(context, attrs);
		postInit(context, _task);
	}
	
	/**
	 * initialize customized gui with data
	 * @param _task
	 */
	private void postInit(Context _context, Task _task)
	{
		this.task = _task;
		
		this.addView(inflate(_context, R.layout.task_for_kanban_layout, (ViewGroup)this.getChildAt(0)));
		this.TaskTitle = (TextView)findViewById(R.id.textViewTaskTitle);
		this.TaskOwner = (TextView)findViewById(R.id.textViewTaskOwner);
		this.TaskDate = (TextView)findViewById(R.id.textViewTaskDate);
		this.TaskId = (TextView)findViewById(R.id.textViewTaskId);
		this.linearLayoutTaskForKBMain = (LinearLayout)findViewById(R.id.linearLayoutTaskForKBMain);
		
		linearLayoutTaskForKBMain.setOnClickListener(this);
		
		//fill with data
		TaskId.setText(String.valueOf(task.getTaskID()));
		if(_task.getStatus() == Task.Status.TODO)
		{
			TaskTitle.setText(_task.getTitle());
			TaskOwner.setVisibility(INVISIBLE);
			TaskDate.setVisibility(INVISIBLE);
		}
		else if(_task.getStatus() == Task.Status.ONGOING)
		{
			TaskTitle.setText(_task.getTitle());
			User u = new User(_task.getOwnerID());
			TaskOwner.setText(u.getName());
			TaskDate.setText(_context.getString(R.string.Start_Date) +":  "+_task.getStartDate());
		}
		else if(_task.getStatus() == Task.Status.DONE)
		{
			TaskTitle.setText(_task.getTitle());
			User u = new User(_task.getOwnerID());
			TaskOwner.setText(u.getName());
			TaskDate.setText(_context.getString(R.string.Complete_Date) +":  "+_task.getCompleteDate());
		}
		else
		{
			//do nothing
		}
		
		if(_task.getPriority() == Priority.P1)
		{
			linearLayoutTaskForKBMain.setBackgroundColor(Color.RED);
		}
		else if(_task.getPriority() == Priority.P2)
		{
			linearLayoutTaskForKBMain.setBackgroundColor(Color.YELLOW);
		}
		else if(_task.getPriority() == Priority.P3)
		{
			linearLayoutTaskForKBMain.setBackgroundColor(Color.GREEN);
		}
		else
		{
			//do nothing
		}
	}

	@Override
	public void onClick(View v) {
		int taskid = Integer.parseInt(TaskId.getText().toString());
		Intent intent = new Intent(v.getContext(), TaskDetalInfoActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("TASK_MODE", TaskDetalInfoActivity.EDIT_MODE);
		bundle.putInt("TASK_ID", taskid);
		intent.putExtras(bundle);
		v.getContext().startActivity(intent);
	}
}
