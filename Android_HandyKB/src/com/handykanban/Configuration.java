package com.handykanban;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Configuration extends Activity {

	private Project current_project;
	//private String current_project_name;
	private TextView current_project_name;
	private TextView current_project_max_todo;
	private TextView current_project_max_ongoing;
	private TextView current_project_max_done;
    private Button button_OK, button_Cancel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configuration);
	    current_project = LoginSession.getInstance().getActiveProject();
	   // current_project_name = current_project.getName();
	   current_project_name = (TextView)findViewById(R.id.configuration_project_name);
	   current_project_max_todo = (TextView)findViewById(R.id.configuration_todo);
	   current_project_max_ongoing = (TextView)findViewById(R.id.configuration_ongoing);
	   current_project_max_done = (TextView)findViewById(R.id.configuration_done);
	   /*
	   System.out.println(current_project.getName());
	   System.out.println(current_project.getMaxOfToDo());
	   System.out.println(current_project.getMaxOfOnGoing());
	   System.out.println(current_project.getMaxOfDone());
	   */
       current_project_name.setText(current_project.getName());
	    current_project_max_todo.setText(String.valueOf(current_project.getMaxOfToDo()));
	    current_project_max_ongoing.setText(String.valueOf(current_project.getMaxOfOnGoing()));
	    current_project_max_done.setText(String.valueOf(current_project.getMaxOfDone()));

	   button_OK = (Button)findViewById(R.id.configuration_button_ok);
	    button_Cancel = (Button)findViewById(R.id.configuration_button_cancel);
	    
	    button_OK.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {

				current_project.setMaxOfToDo(Integer.parseInt(current_project_max_todo.getText().toString()));
				current_project.setMaxOfOnGoing(Integer.parseInt(current_project_max_ongoing.getText().toString()));
				current_project.setMaxOfDone(Integer.parseInt(current_project_max_done.getText().toString()));
				
				/*current_project.setMaxOfToDo(5);
				current_project.setMaxOfOnGoing(4);
				current_project.setMaxOfDone(3);
				*/
				if(HandyKBDBHelper.getDBHelperInstance().updateProject(current_project))
				{
					finish();
				}
				else
				{
					
				}
			}});
		
	    button_Cancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
                    finish();
				
			}});
			};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.configuration, menu);
		return true;
	}
	
	
	

}
