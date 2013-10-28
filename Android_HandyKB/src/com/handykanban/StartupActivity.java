package com.handykanban;


import java.util.Random;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class StartupActivity extends Activity {

	@Override
	protected void onResume() {
		super.onResume();
		//TODO jump to login page
		//test
		startActivity(new Intent(getApplicationContext(), AdminPage.class));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_startup);
		
		try {
        	// dbversion should be consistent with attribute "android:versionCode"
            int dbversion = 0;
            String dbname = getString(R.string.DB_NAME);
        	dbversion = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
			if( HandyKBDBHelper.createSingleton(getApplicationContext(), dbname, null, dbversion) != null)
			{
				//
			}
		}catch (Exception e){
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.startup, menu);
		return true;
	}

}
