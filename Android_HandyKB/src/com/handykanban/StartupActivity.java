package com.handykanban;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class StartupActivity extends Activity {


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

				startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class),1);
				
			}
		}catch (Exception e){
		}
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//shutdown DB
		HandyKBDBHelper.getDBHelperInstance().close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.startup, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		finish();
	}

}
