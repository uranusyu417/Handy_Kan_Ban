package com.handykanban;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TaskLinearLayoutForKanBan extends LinearLayout {
	Task task;
	
	TextView TaskTitle;
	/* if status TO-DO or BACKLOG, do not show owner */
	TextView TaskOwner;
	/* if status is DONE, use complete date
	 * ONGOING, start date
	 * otherwise, do not show date */
	TextView TaskDate;
	TextView TaskId;
	
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
	}
	
	//TODO implement item click 

}
