package com.handykanban;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class HandyKBDBHelper extends SQLiteOpenHelper {
	
	private static HandyKBDBHelper dbhelper_instance = null;
	
	public static HandyKBDBHelper getDBHelperInstance()
	{
		return dbhelper_instance;
	}
	
	public static HandyKBDBHelper createSingleton(Context context, String name, CursorFactory factory,
			int version)
	{
		if(dbhelper_instance == null)
		{
			dbhelper_instance = new HandyKBDBHelper(context, name, factory, version);
			return dbhelper_instance;
		}
		else
		{
			return dbhelper_instance;
		}
	}

	private HandyKBDBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * get associated user object with specific task
	 * @param _task 
	 * @return
	 */
	public User getUserByTask(Task _task)
	{
		//TODO implement this
		return null;
	}
	
	/**
	 * get user object by user id
	 * @param _userId
	 * @return
	 */
	public User getUserByID(int _userId)
	{
		//TODO implement
		return null;
	}
	
	public Project getProjectByID(int _prjId)
	{
		//TODO implement
		return null;
	}

}
