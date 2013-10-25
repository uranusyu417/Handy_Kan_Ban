package com.handykanban;

public class Task {
	public int taskID;
	public String title;
	public String detail;
	public Status status;
	public Priority priority;
	public String startDate;
	public String completeDate;
	public int ownerID;
	public int projectID;

	public enum Status
	{
		BACKLOG,
		TODO,
		ONGOING,
		DONE;
		
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
	}
	
	public enum Priority
	{
		P1,
		P2,
		P3;
		
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
	}
}