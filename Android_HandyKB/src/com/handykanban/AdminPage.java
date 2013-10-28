package com.handykanban;
import com.handykanban.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class AdminPage extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.db_admin_layout);
		
		//add project
		Button btn = (Button)findViewById(R.id.buttonAddProject);
		btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				EditText n = (EditText)findViewById(R.id.editTextProjectName);
				EditText m = (EditText)findViewById(R.id.editTextProjectMax);
				Project p = new Project();
				p.setName(n.getText().toString());
				int max = Integer.parseInt(m.getText().toString());
				p.setMaxOfDone(max);
				p.setMaxOfOnGoing(max);
				p.setMaxOfToDo(max);
				if(HandyKBDBHelper.getDBHelperInstance().addNewProject(p))
				{
					Toast.makeText(arg0.getContext(), "add project success", Toast.LENGTH_LONG).show();
				}
			}
			
		});
		
		
		//add user
		btn = (Button)findViewById(R.id.buttonAddUser);
		btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				EditText n = (EditText)findViewById(R.id.editTextUserName);
				User u = new User();
				u.setName(n.getText().toString());
				u.setPassword("password");
				if(HandyKBDBHelper.getDBHelperInstance().addNewUser(u))
				{
					Toast.makeText(arg0.getContext(), "add user success", Toast.LENGTH_LONG).show();
				}
			}
			
		});
		
		//add task
		btn = (Button)findViewById(R.id.buttonAddTask);
		btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}

}
