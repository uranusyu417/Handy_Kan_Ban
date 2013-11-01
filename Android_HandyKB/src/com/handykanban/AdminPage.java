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

				if(n.getText().toString().length()<1)
				{
					//test
					//LoginSession.getInstance().setLoggedInUser(HandyKBDBHelper.getDBHelperInstance().getUserByID(1));
					//startActivity(new Intent(arg0.getContext(), KanBanUIActivity.class));
		
					n.setError("Please input Project Name");
					return;
				}
				if(m.getText().toString().length()<1)
				{
					m.setError("Please input max number");
					return;
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
				EditText p = (EditText)findViewById(R.id.editTextPassword);
				String username = n.getText().toString();
				String password = p.getText().toString();
						
				User u = new User();
				
				if(username.length()!=0)
				{
					u.setName(username);					
				}	
				else
			    {
					n.setError("Invalid User Name");
					return;
			    }
				
				if(password.length()!=0)
				{
					u.setPassword(password);
				}
				else
				{
					p.setError("Invalid Password");
					return;
				}

				if(HandyKBDBHelper.getDBHelperInstance().addNewUser(u))
				{	
					Toast.makeText(arg0.getContext(), "add user success", Toast.LENGTH_LONG).show();
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
		
		//Quit button
		btn = (Button)findViewById(R.id.buttonQuitAdmin);
		btn.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View arg0) {
				finish();
				
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
		
		Spinner t_prj = (Spinner) findViewById(R.id.spinnerRelationProject);
		t_prj.setAdapter(prj_adp);
		
		//load all user info
		ArrayAdapter<User> usr_adp = new ArrayAdapter<User>(this,
				android.R.layout.simple_spinner_item, HandyKBDBHelper
				.getDBHelperInstance().getAllUsers());
		usr_adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		Spinner t_owner = (Spinner)findViewById(R.id.spinnerRelationUser);
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
