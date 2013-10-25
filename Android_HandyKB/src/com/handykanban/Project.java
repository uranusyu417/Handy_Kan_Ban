package com.handykanban;

public class Project {
	private int projectID;
	private String name;
	private int maxOfToDo;
	private int maxOfOnGoing;
	private int maxOfDone;
	
	public Project(int id)
	{
		Project _prj = HandyKBDBHelper.getDBHelperInstance().getProjectByID(id);
		this.projectID = _prj.projectID;
		this.name = _prj.name;
		this.maxOfDone = _prj.maxOfDone;
		this.maxOfOnGoing = _prj.maxOfOnGoing;
		this.maxOfToDo = _prj.maxOfToDo;
	}
	
	public int getProjectID() {
		return projectID;
	}
	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getMaxOfToDo() {
		return maxOfToDo;
	}
	public void setMaxOfToDo(int maxOfToDo) {
		this.maxOfToDo = maxOfToDo;
	}
	public int getMaxOfOnGoing() {
		return maxOfOnGoing;
	}
	public void setMaxOfOnGoing(int maxOfOnGoing) {
		this.maxOfOnGoing = maxOfOnGoing;
	}
	public int getMaxOfDone() {
		return maxOfDone;
	}
	public void setMaxOfDone(int maxOfDone) {
		this.maxOfDone = maxOfDone;
	}

}
