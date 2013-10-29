package com.handykanban;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.handykanban.Task.Priority;
import com.handykanban.Task.Status;

public class HandyKBDBHelper extends SQLiteOpenHelper {
	
	private static final String TAG="HandyKBDBHelper";
	
	private static final String TaskTableName="KanBanTasks";
	private static final String ProjectTableName="KanBanProjects";
	private static final String UserTableName = "KanBanUsers";
	private static final String UserRoleTableName = "KanBanUserRoles";
	private static final String NoteTableName = "KanBanNotes";
	
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
	
	// used for executing SQL
	private SQLiteDatabase db = null;

	private HandyKBDBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		db = getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		createDBTables(arg0);

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public synchronized void close() {
		super.close();
		db.close();
	}

	/**
	 * get associated user object with specific task
	 * @param _task 
	 * @return
	 */
	public User getUserByTask(Task _task)
	{
		return getUserByID(_task.getOwnerID());
	}
	
	/**
	 * get user object by user id
	 * @param _userId
	 * @return a user object that has no project info, i.e. role
	 */
	public User getUserByID(int _userId)
	{
		User _user = null;
		
		try
		{
			Cursor cur = db.query(UserTableName, null, "UserID=?", 
					              new String[] {String.valueOf(_userId)}, null, null, null);
			if(cur.moveToNext())
			{
				_user = new User();
				_user.setUserID(_userId);
				_user.setName(cur.getString(cur.getColumnIndexOrThrow("Name")));
			}
			
			cur.close();
			
		}
		catch(Exception e)
		{
			Log.e(TAG, e.toString());
		}
		
		return _user;
	}
	
	/***
	 * get user object with project role info
	 * @param _userId User ID
	 * @param _prj Project object
	 * @return user object
	 */
	public User getUserByIDandProject(int _userId, Project _prj)
	{
        User _user = null;
		
		try
		{
			Cursor cur = db.query(UserTableName, null, "UserID="+_userId, 
					              null, null, null, null);
			if(cur.moveToNext())
			{
				_user = new User();
				_user.setUserID(_userId);
				_user.setName(cur.getString(cur.getColumnIndexOrThrow("Name")));
				//get role info
				Cursor r = db.query(UserRoleTableName, null, "UserID="+_userId+" AND ProjectID="+_prj.getProjectID(), 
			               null, null, null, null);
				if(r.moveToNext())
				{
					_user.setRole(User.Role.intToRole(r.getInt(r.getColumnIndex("Role"))));
				}
				r.close();
			}
			
			cur.close();
			
		}
		catch(Exception e)
		{
			Log.e(TAG, e.toString());
		}
		
		return _user;
	}
	
	/**
	 * get all user objects belonging to one project
	 * @param _prj
	 * @return
	 */
	public ArrayList<User> getUsersByProject(Project _prj)
	{
		ArrayList<User> users = new ArrayList<User>();
		//get user IDs first
		Cursor usr_prj = db.query(UserRoleTableName, null, "ProjectID="+_prj.getProjectID(), 
                                  null, null, null, null);
		while(usr_prj.moveToNext())
		{
			users.add(getUserByIDandProject(usr_prj.getInt(usr_prj.getColumnIndex("UserID")), _prj));
		}
		return users;
	}
	
	/**
	 * get all user objects belonging to one project
	 * @param _prjId
	 * @return
	 */
	public ArrayList<User> getUsersByProject(int _prjId)
	{
		Project _p = getProjectByID(_prjId);
		
		if(_p != null)
		{
			return(getUsersByProject(_p));
		}
		else
		{
			return null;
		}
	}
	
	/***
	 * get specific project object
	 * @param _prjId
	 * @return
	 */
	public Project getProjectByID(int _prjId)
	{
		Project _p = null;
		Cursor cur = db.query(ProjectTableName, null, "ProjectID=?", 
				             new String[]{String.valueOf(_prjId)}, null, null, null);
		if(cur.moveToNext())
		{
			_p =  new Project();
			_p.setProjectID(_prjId);
			_p.setName(cur.getString(cur.getColumnIndex("Name")));
			_p.setMaxOfDone(cur.getInt(cur.getColumnIndex("MaxOfDone")));
			_p.setMaxOfOnGoing(cur.getInt(cur.getColumnIndex("MaxOfOnGoing")));
			_p.setMaxOfToDo(cur.getInt(cur.getColumnIndex("MaxOfToDo")));
		}
		cur.close();
		
		return _p;
	}
		
	private void createDBTables(SQLiteDatabase db) {
		// task table
		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ TaskTableName
				+ " "
				+ "( TaskID INTEGER PRIMARY KEY ASC AUTOINCREMENT, "
				+ "  Title TEXT NOT NULL, "
				+ "  Detail TEXT, "
				+ "  Priority INTEGER NOT NULL CHECK ( Priority BETWEEN 1 AND 3 ), "
				+ "  StartDate TEXT, "
				+ "  CompleteDate TEXT, "
				+ "  OwnerID INTEGER, "
				+ "  Status INTEGER NOT NULL CHECK ( Status BETWEEN 0 AND 3 ), "
				+ "  ProjectID INTEGER NOT NULL );");
		// project table
		db.execSQL("CREATE TABLE IF NOT EXISTS " 
		        + ProjectTableName + " "
				+ "( ProjectID INTEGER PRIMARY KEY ASC AUTOINCREMENT, "
				+ "  Name TEXT NOT NULL, "
				+ "  MaxOfToDo INTEGER DEFAULT 4 NOT NULL, "
				+ "  MaxOfOnGoing INTEGER DEFAULT 4 NOT NULL, "
				+ "  MaxOfDone INTEGER DEFAULT 4 NOT NULL );");
		// user table
		db.execSQL("CREATE TABLE IF NOT EXISTS " 
		        + UserTableName + " "
				+ "( UserID INTEGER PRIMARY KEY ASC AUTOINCREMENT, "
				+ "  Name TEXT NOT NULL, " 
				+ "  Password TEXT );");
		// user role table
		db.execSQL("CREATE TABLE IF NOT EXISTS " 
		        + UserRoleTableName + " "
				+ "( UserID INTEGER , " 
		        + "  ProjectID INTEGER, "
				+ "  Role INTEGER NOT NULL CHECK ( Role BETWEEN 0 AND 1 ), "
				+ "  PRIMARY KEY (UserID, ProjectID)  );");
		// note table
		db.execSQL("CREATE TABLE IF NOT EXISTS " 
		        + NoteTableName + " "
				+ "( NoteID INTEGER PRIMARY KEY ASC AUTOINCREMENT , "
				+ "  ProjectID INTEGER, " 
				+ "  Content TEXT NOT NULL, "
				+ "  Date TEXT NOT NULL  );");
	}
	
	/**
	 * get all project objects
	 * @return
	 */
	public ArrayList<Project> getAllProjects()
	{
		ArrayList<Project> projects = new ArrayList<Project>();
		Cursor cur = db.query(ProjectTableName, null, null, null, null, null, "Name");
		while(cur.moveToNext())
		{
			Project _p =  new Project();
			_p.setProjectID(cur.getInt(cur.getColumnIndex("ProjectID")));
			_p.setName(cur.getString(cur.getColumnIndex("Name")));
			_p.setMaxOfDone(cur.getInt(cur.getColumnIndex("MaxOfDone")));
			_p.setMaxOfOnGoing(cur.getInt(cur.getColumnIndex("MaxOfOnGoing")));
			_p.setMaxOfToDo(cur.getInt(cur.getColumnIndex("MaxOfToDo")));
			projects.add(_p);
		}
		return projects;
	}
	
	/**
	 * insert a new project record into DB
	 * @param p
	 * @return true if successful
	 */
	public boolean addNewProject(Project p)
	{
		ContentValues cv = new ContentValues();
		cv.put("Name", p.getName());
		cv.put("MaxOfDone", p.getMaxOfDone());
		cv.put("MaxOfOnGoing", p.getMaxOfOnGoing());
		cv.put("MaxOfToDo", p.getMaxOfToDo());
		if(db.insert(ProjectTableName, null, cv)>-1)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * get task object from database by task id
	 * @return Task
	 */
	public Task getTaskById(int _taskId)
	{	
		Task _task = null;
		
		Cursor cur = db.query(TaskTableName, null, "TaskID=?", 
	             new String[]{String.valueOf(_taskId)}, null, null, null);	

		if(cur.moveToNext())
		{
			_task =  new Task();
			_task.setTaskID(_taskId);
			_task.setCompleteDate(cur.getString(cur.getColumnIndex("CompleteDate")));
			_task.setDetail(cur.getString(cur.getColumnIndex("Detail")));
			_task.setOwnerID(cur.getInt(cur.getColumnIndex("OwnerID")));
			_task.setPriority(Priority.intToPriority(cur.getInt(cur.getColumnIndex("Priority"))));
			_task.setProjectID(cur.getInt(cur.getColumnIndex("ProjectID")));
			_task.setStartDate(cur.getString(cur.getColumnIndex("StartDate")));
			_task.setStatus(Status.intToStatus(cur.getInt(cur.getColumnIndex("Status"))));
			_task.setTitle(cur.getString(cur.getColumnIndex("Title")));
		}
		cur.close();		
		
		return _task;
	}	
	

	/**
	 * update task record
	 * @return true if successful
	 */
	public boolean updateTaskInfo(Task _task)
	{
		ContentValues c = new ContentValues();
		
		c.put("TaskID", _task.getTaskID());
		c.put("Title", _task.getTitle());
		c.put("Detail", _task.getDetail());
		c.put("Priority", Priority.PriorityToInt(_task.getPriority()));
		c.put("StartDate", _task.getStartDate());
		c.put("CompleteDate", _task.getCompleteDate());
		c.put("OwnerID", _task.getOwnerID());
		c.put("Status", Status.StatusToInt(_task.getStatus()));
		c.put("ProjectID", _task.getProjectID());
		
		db.update(TaskTableName, c, "TaskID=?", new String[] {Integer.toString(_task.getTaskID())});
		
		return true;
	}	
	
}
