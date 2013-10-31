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
	    
        mAdapter = new BacklogAdapter(updateSourceList, this);

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
						//Log.d("XYZ","original-"+i+"="+originalSourceList.get(i).getStatus() + 
						//		" / update-"+i+"="+updateSourceList.get(i).getStatus());  
						db.updateTaskInfo(t);
					}
				}
				finish();
			}
		});
	    
	    
	    bt_addNewTask = (Button) findViewById(R.id.button_newTask);				
	    bt_addNewTask.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				//creat new task data to database.		

    			Intent intent = new Intent(v.getContext(), TaskDetalInfoActivity.class);
                Bundle b=new Bundle();
                b.putInt("TASK_MODE", TaskDetalInfoActivity.CREATE_MODE);
                intent.putExtras(b);
                v.getContext().startActivity(intent);
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
