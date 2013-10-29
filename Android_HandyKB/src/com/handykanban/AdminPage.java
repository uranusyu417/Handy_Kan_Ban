package com.handykanban;
import com.handykanban.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
				if(n.getText().toString().length()<=1)
				{
					//test
					LoginSession.getInstance().setLoggedInUser(HandyKBDBHelper.getDBHelperInstance().getUserByID(1));
					startActivity(new Intent(arg0.getContext(), KanBanUIActivity.class));
				}
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
				restartSelf();
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
				restartSelf();
			}
			
		});
		
		//add task
		btn = (Button)findViewById(R.id.buttonAddTask);
		btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				EditText t_title = (EditText)findViewById(R.id.editTextTaskTitle);
				RadioGroup t_pri = (RadioGroup)findViewById(R.id.radioGroupTaskPriority);
				RadioGroup t_status = (RadioGroup)findViewById(R.id.radioGroupTaskStatus);
				Spinner t_prj = (Spinner)findViewById(R.id.spinnerTaskProject);
				Spinner t_owner = (Spinner)findViewById(R.id.spinnerTaskOwner);
				
				Task t = new Task();
				t.setTitle(t_title.getText().toString());
				switch(t_pri.getCheckedRadioButtonId())
				{
				case R.id.radioP1:
					t.setPriority(Task.Priority.P1);
					break;
				case R.id.radioP2:
					t.setPriority(Task.Priority.P2);
					break;
				case R.id.radioP3:
					t.setPriority(Task.Priority.P3);
					break;
					default:break;
				}
				switch(t_status.getCheckedRadioButtonId())
				{
				case R.id.radioBacklog:
					t.setStatus(Task.Status.BACKLOG);
					break;
				case R.id.radioTodo:
					t.setStatus(Task.Status.TODO);
					break;
				case R.id.radioOnGo:
					t.setStatus(Task.Status.ONGOING);
					break;
				case R.id.radioDone:
					t.setStatus(Task.Status.DONE);
					break;
					default:return;
				}
				User u = (User)t_owner.getSelectedItem();
				t.setOwnerID(u.getUserID());
				Project p = (Project)t_prj.getSelectedItem();
				t.setProjectID(p.getProjectID());
				if(HandyKBDBHelper.getDBHelperInstance().addNewTask(t))
				{
					Toast.makeText(arg0.getContext(), "add task success", Toast.LENGTH_LONG).show();
				}
				restartSelf();
			}
			
		});
		
		//add relation
		btn = (Button)findViewById(R.id.buttonAddRelation);
		btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Spinner r_prj = (Spinner) findViewById(R.id.spinnerRelationProject);
				Spinner r_user = (Spinner)findViewById(R.id.spinnerRelationUser);
				RadioGroup r_role = (RadioGroup)findViewById(R.id.radioGroupRole);
				Project p = (Project)r_prj.getSelectedItem();
				User u = (User)r_user.getSelectedItem();
				switch(r_role.getCheckedRadioButtonId())
				{
				case R.id.radioPO: u.setRole(User.Role.PO); break;
				case R.id.radioDesigner: u.setRole(User.Role.Designer); break;
				default:return;
				}
				if(HandyKBDBHelper.getDBHelperInstance().addNewUserProjectRole(u.getUserID(), p.getProjectID(), u.getRole()))
				{
					Toast.makeText(arg0.getContext(), "add relation success", Toast.LENGTH_LONG).show();
				}
			}
			
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		// load all project info
		ArrayAdapter<Project> prj_adp = new ArrayAdapter<Project>(this,
				android.R.layout.simple_spinner_item, HandyKBDBHelper
						.getDBHelperInstance().getAllProjects());
		prj_adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner t_prj = (Spinner) findViewById(R.id.spinnerTaskProject);
		t_prj.setAdapter(prj_adp);
		
		t_prj = (Spinner) findViewById(R.id.spinnerRelationProject);
		t_prj.setAdapter(prj_adp);
		
		//load all user info
		ArrayAdapter<User> usr_adp = new ArrayAdapter<User>(this,
				android.R.layout.simple_spinner_item, HandyKBDBHelper
				.getDBHelperInstance().getAllUsers());
		usr_adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner t_owner = (Spinner)findViewById(R.id.spinnerTaskOwner);
		t_owner.setAdapter(usr_adp);
		
		t_owner = (Spinner)findViewById(R.id.spinnerRelationUser);
		t_owner.setAdapter(usr_adp);
	}
	
	private void restartSelf()
	{
		Intent intent = getIntent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		finish();
		startActivity(intent);
	}

}
