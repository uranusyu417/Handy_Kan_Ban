package com.handykanban;

public class Task {
	private int taskID;
	private String title;
	private String detail;
	private Status status;
	private Priority priority;
	private String startDate;
	private String completeDate;
	private int ownerID;
	private int projectID;
	
	/***
	 * empty object
	 */
	public Task()
	{
		
	}
	/***
	* Copy constructor
	*/
	public Task(Task t)
	{
		taskID  = t.taskID;
		title   = t.title;
		detail  = t.detail;
		status  = t.status;
		priority= t.priority;
		startDate   = t.startDate;
		completeDate= t.completeDate;
		ownerID     = t.ownerID;
		projectID   = t.projectID;
	}
	
	// check whether two tasks are equal	   
	public boolean equals(Task o) {
	        // Return true if the objects are identical.
	        // (This is just an optimization, not required for correctness.)     
	        if (this == o) {       
	           return true;     
	        }     
	        
	        // Return false if the other object has the wrong type.     
	        // This type may be an interface depending on the interface's specification.     
	        if (!(o instanceof Task)) 
	        {       
	           return false;     
	        }     
	        // Cast to the appropriate type.     
	        // This will succeed because of the instanceof, and lets us access private fields.     
	        Task t = (Task) o;     
	        // Check each field. Primitive fields, reference fields, and nullable reference     
	        // fields are all treated differently.     
	        return ((taskID == t.taskID) &&       
	        		(title   == t.title) &&
					(detail  == t.detail) &&
					(status  == t.status) &&
					(priority== t.priority) &&
					(startDate   == t.startDate) &&
					(completeDate== t.completeDate) &&
					(ownerID     == t.ownerID) &&
					(projectID   == t.projectID));  
	}
	
	public int getTaskID() {
		return taskID;
	}

	public void setTaskID(int taskID) {
		this.taskID = taskID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getCompleteDate() {
		return completeDate;
	}

	public void setCompleteDate(String completeDate) {
		this.completeDate = completeDate;
	}

	public int getOwnerID() {
		return ownerID;
	}

	public void setOwnerID(int ownerID) {
		this.ownerID = ownerID;
	}

	public int getProjectID() {
		return projectID;
	}

	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}

	public enum Status
	{
		BACKLOG,
		TODO,
		ONGOING,
		DONE,
		UNKNOWN;
		
		public String toString()
		{
			switch(this)
			{
			case BACKLOG: return "BACKLOG";
			case TODO: return "TODO";
			case ONGOING: return "ONGOING";
			case DONE: return "DONE";
			default: return "";
			}
		}
		public static Status intToStatus(int s)
		{
			switch(s)
			{
			case 0: return BACKLOG;
			case 1: return TODO;
			case 2: return ONGOING;
			case 3: return DONE;
			default: return UNKNOWN;
			}
		}
		public static int StatusToInt(Status s)
		{
			switch(s)
			{
			case BACKLOG: return 0;
			case TODO: return 1;
			case ONGOING: return 2;
			case DONE: return 3;
			default: return -1;
			}
		}
	}
	
	public enum Priority
	{
		P1,
		P2,
		P3,
		UNKNOWN;
		
		public String toString()
		{
			switch(this)
			{
			case P1: return "P1";
			case P2: return "P2";
			case P3: return "P3";
			default: return "";
			}
		}
		public static Priority intToPriority(int p)
		{
			switch(p)
			{
			case 1: return P1;
			case 2: return P2;
			case 3: return P3;
			default: return UNKNOWN;
			}
		}
		public static int PriorityToInt(Priority p)
		{
			switch(p)
			{
			case P1: return 1;
			case P2: return 2;
			case P3: return 3;
			default: return -1;
			}
		}
	}
	
}
