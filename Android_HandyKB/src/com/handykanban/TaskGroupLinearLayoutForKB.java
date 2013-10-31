package com.handykanban;

import java.util.ArrayList;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TaskGroupLinearLayoutForKB extends LinearLayout {
	TextView HeaderStatus;
	TextView HeaderCount;
	LinearLayout main;

	/**
	 * constructor
	 * @param context
	 * @param _taskGroup a group of tasks that have the same status
	 */
	public TaskGroupLinearLayoutForKB(Context context, ArrayList<Task> _taskGroup) {
		super(context);
		postInit(context, _taskGroup);
	}
	
	private void postInit(Context context, ArrayList<Task> _taskGroup)
	{
		if(_taskGroup.size()==0)
		{
			//do nothing as no any task
			return ;
		}
		this.addView(inflate(context, R.layout.task_group_layout, (ViewGroup)this.getChildAt(0)));
		this.HeaderStatus = (TextView)findViewById(R.id.textViewHeaderStatus);
		this.HeaderCount = (TextView)findViewById(R.id.textViewHeaderCount);
		this.main = (LinearLayout)findViewById(R.id.linearLayoutMain);
		
		this.HeaderStatus.setText(_taskGroup.get(0).getStatus().toString());
		
		String cntstr = "";
		if(_taskGroup.get(0).getStatus() == Task.Status.TODO)
		{
			cntstr = "    ("+_taskGroup.size()+"/"+LoginSession.getInstance().getActiveProject().getMaxOfToDo();
			cntstr += ")";
			this.HeaderCount.setText(cntstr);
		}
		else if(_taskGroup.get(0).getStatus() == Task.Status.ONGOING)
		{
			cntstr = "    ("+_taskGroup.size()+"/"+LoginSession.getInstance().getActiveProject().getMaxOfOnGoing();
			cntstr += ")";
			this.HeaderCount.setText(cntstr);
		}
		else
		{
			this.HeaderCount.setVisibility(INVISIBLE);
		}
		
		// generate task ui items
		for(int i=0;i<_taskGroup.size();i++)
		{
			main.addView(new TaskLinearLayoutForKanBan(context, _taskGroup.get(i)));
			TextView space = new TextView(context);
			space.setText("");
			space.setHeight(10);
			main.addView(space);
		}
	}

}
