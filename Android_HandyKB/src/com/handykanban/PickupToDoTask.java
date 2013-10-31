package com.handykanban;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handykanban.BacklogAdapter.ViewHolder;
import com.handykanban.Task.Status;


public class PickupToDoTask extends Activity {

	private ListView lvwTodo; //listview of pick up to-do task
	private TextView tv_show; //textview to show the summary
	private  ArrayList<Task> updateSourceList=null;
	private  ArrayList<Task> originalSourceList=null; //mark which record was updated.
	private  BacklogAdapter mAdapter=null;
	private Button bt_update;
	private Button bt_addNewTask;
	private int todoTaskThreshold; //the checked item count.

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pickup_to_do_task);
		updateSourceList=null;
		originalSourceList=null;
		mAdapter=null;
		todoTaskThreshold = LoginSession.getInstance().getActiveProject().getMaxOfToDo();
	}
	
	@Override
	protected void onResume() {
		super.onResume();	
		//
	    lvwTodo = (ListView)findViewById(R.id.pickupToDoTaskList);
	    tv_show = (TextView) findViewById(R.id.textView_toDoSummary);
	    tv_show.setText("current/max: 0/0");

	    //	init data from DB.      
	    if(false == readDataFromDB())
	    {
	    	Toast.makeText(this, (CharSequence)("Fail to read data from DB!"), Toast.LENGTH_LONG).show();
	    	return;
	    }
	    
	    //set adapter
	    if(mAdapter == null){
	        mAdapter = new BacklogAdapter(updateSourceList, this);
	    }
	    lvwTodo.setAdapter(mAdapter);	
	    tv_show.setText("current/max: " + mAdapter.todoTaskCount() + "/" + todoTaskThreshold);
	   
	    //install message handler
	    lvwTodo.setOnItemClickListener(new OnItemClickListener() {
	    	@Override
            public void onItemClick(AdapterView arg0, View arg1, int position, long arg3) {
	    		// check whether current to-do tasks count had exceed the threshold
	    		if((mAdapter.isItemChecked(position)==false) && // going to check the box.
	    				(mAdapter.todoTaskCount() >= todoTaskThreshold)) // and there's no room for to-do task. 
	    		{
	    			Toast.makeText(lvwTodo.getContext(), (CharSequence)("To-Do task count exceeds the threshold, " 
	    		                  +	"remove one before pick up new!"), Toast.LENGTH_LONG).show();
	    			return;
	    		}
	    		ViewHolder holder = (ViewHolder) arg1.getTag();
	    		holder.cb.toggle();
	    		
	    		if (holder.cb.isChecked() == true) {
	    			// update list
		    		updateSourceList.get(position).setStatus(Status.TODO);
		    		System.out.println("source-"+position+"="+updateSourceList.get(position).getStatus());  
                } else {
                	updateSourceList.get(position).setStatus(Status.BACKLOG);
                	System.out.println("source-"+position+"="+updateSourceList.get(position).getStatus());  
                }
	    		
	    		// update status to adapter.
	    		mAdapter.getIsSelected().put(position, holder.cb.isChecked()); 
                // show chosen count
	    		tv_show.setText("current/max: " + mAdapter.todoTaskCount() + "/" + todoTaskThreshold);
	    	}
	    	
	    });
	    
	    bt_update = (Button) findViewById(R.id.buttonUpdateTodo);				
	    bt_update.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				//update data to database.
				//System.out.println("Button was clicked!!");
				HandyKBDBHelper db = HandyKBDBHelper.getDBHelperInstance() ;
				for(int i=0; i<updateSourceList.size(); i++)
				{
					// data was changed, need to update to database..
					Task t = updateSourceList.get(i);
					if(!t.equals(originalSourceList.get(i))){
						//write to database;						
						Log.d("XYZ","original-"+i+"="+originalSourceList.get(i).getStatus() + 
								" / update-"+i+"="+updateSourceList.get(i).getStatus());  
						db.updateTaskInfo(t);
					}
				}
				finish();
			}
		});
	    
	    
	    bt_addNewTask = (Button) findViewById(R.id.button_newTask);				
	    bt_addNewTask.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				//creat new task data to database.
				Log.d("XYZ","The add new task buuton was clicked!");
                Intent intent = new Intent();
                Bundle b=new Bundle();
                b.putInt("TASK_MODE", TaskDetalInfoActivity.CREATE_MODE);
                intent.setClass(arg0.getContext(), TaskDetalInfoActivity.class);
                startActivity(intent);               
			}
		});	    
	}	    

	private boolean readDataFromDB()
	{
		
		// get tasks from DB.
		HandyKBDBHelper db = HandyKBDBHelper.getDBHelperInstance() ;
		if(db == null){
			Toast.makeText(this, (CharSequence)("Fail to access the database!"), Toast.LENGTH_LONG).show();
			return false;
		}
		
		if(updateSourceList == null){
		   updateSourceList = new ArrayList<Task>();
		}
		else{
		   updateSourceList.clear();
		}		
		updateSourceList.addAll(db.getTasksByProjectIDAndStatus(LoginSession.getInstance().getActiveProject().getProjectID(), Task.Status.TODO));
		updateSourceList.addAll(db.getTasksByProjectIDAndStatus(LoginSession.getInstance().getActiveProject().getProjectID(), Task.Status.BACKLOG));
		
		//add debug data here.
		/*
		  
	    Task t1 = new Task();
	    t1.setTitle("Task1");
	    t1.setDetail("This is the first task");
	    t1.setPriority(Priority.P1);
	    t1.setTaskID(1);
	    t1.setStatus(Status.TODO);
	    updateSourceList.add(t1);
	    
	    Task t2 = new Task();
	    t2.setTitle("Task2");
	    t2.setDetail("This is the 2nd task");
	    t2.setPriority(Priority.P2);
	    t2.setTaskID(1);
	    t2.setStatus(Status.BACKLOG);
	    updateSourceList.add(t2);
	    
	    Task t3 = new Task();
	    t3.setTitle("Task3");
	    t3.setDetail("This is the 3 task");
	    t3.setPriority(Priority.P3);
	    t3.setTaskID(1);
	    t3.setStatus(Status.BACKLOG);
	    updateSourceList.add(t3);
	    
	    Task t4 = new Task();
	    t4.setTitle("Task4");
	    t4.setDetail("This is the 4 task");
	    t4.setPriority(Priority.P1);
	    t4.setTaskID(1);
	    t4.setStatus(Status.TODO);
	    updateSourceList.add(t4);
	    
	    Task t5 = new Task();
	    t5.setTitle("Task5");
	    t5.setDetail("This is the 5 task");
	    t5.setPriority(Priority.P2);
	    t5.setTaskID(1);
	    t5.setStatus(Status.TODO);
	    updateSourceList.add(t5);
	    
	    Task t6 = new Task();
	    t6.setTitle("Task6");
	    t6.setDetail("This is the 6 task");
	    t6.setPriority(Priority.P3);
	    t6.setTaskID(1);
	    t6.setStatus(Status.BACKLOG);
	    updateSourceList.add(t6);	  
	    
	    Task t7 = new Task();
	    t7.setTitle("Task7");
	    t7.setDetail("This is the 7 task");
	    t7.setPriority(Priority.P1);
	    t7.setTaskID(1);
	    t7.setStatus(Status.BACKLOG);
	    updateSourceList.add(t7);
	    
	    Task t8 = new Task();
	    t8.setTitle("Task8");
	    t8.setDetail("This is the 8 task");
	    t8.setPriority(Priority.P2);
	    t8.setTaskID(1);
	    t8.setStatus(Status.BACKLOG);
	    updateSourceList.add(t8);
	    
	    Task t9 = new Task();
	    t9.setTitle("Task9");
	    t9.setDetail("This is the 9 task");
	    t9.setPriority(Priority.P3);
	    t9.setTaskID(1);
	    t9.setStatus(Status.BACKLOG);
	    updateSourceList.add(t9);	
	    
	    todoTaskThreshold = 4;
	    */
	    //end	   	    
	    
	    
	    if(originalSourceList==null){
	       originalSourceList = new ArrayList<Task>();
	    }
	    else{
	       originalSourceList.clear();
	    }
	    for(int i=0; i<updateSourceList.size(); i++){
            Task temp = new Task(updateSourceList.get(i));  
            originalSourceList.add(temp);
	    }
	    	    
	    return true;
	}

	
}
