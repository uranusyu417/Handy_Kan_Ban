/**
 * 
 */
package com.handykanban;

/**
 * @author ebbbyyy
 * 
 * this singleton class is used to store runtime information,
 * e.g. login user, selected project...
 *
 */


public class LoginSession {
	/**
	 * singleton
	 */
	private static LoginSession session_instance = null;
	
	private User LoggedInUser;
	
	private Project ActiveProject;
	
	/**
	 * empty constructor
	 */
	private LoginSession()
	{
		
	}

	/**
	 * get singleton of LoginSession
	 * @return instance
	 */
	public static LoginSession getInstance()
	{
		if(session_instance == null)
		{
			session_instance = new LoginSession();
		}
		
		return session_instance;
	}
	

	public Project getActiveProject() {
		return ActiveProject;
	}

	public void setActiveProject(Project activeProject) {
		ActiveProject = activeProject;
		updateLoggedInUser();
	}

	public User getLoggedInUser() {
		return LoggedInUser;
	}

	public void setLoggedInUser(User loggedInUser) {
		LoggedInUser = loggedInUser;
	}
	
	private void updateLoggedInUser()
	{
		//TODO user info need to be updated according to project change
	}
}
