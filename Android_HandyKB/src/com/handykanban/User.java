package com.handykanban;

public class User {
	
	public User(int id)
	{
		User _u = HandyKBDBHelper.getDBHelperInstance().getUserByID(id);
		this.userID = id;
		this.name = _u.name;
		this.role = _u.role;
	}

	public int userID;
	public String name;
	public Role role;
	
	public enum Role
	{
		PO,
		Designer,
		UNKNOWN;
		
		public String toString()
		{
			switch(this)
			{
			case PO: return "PO";
			case Designer: return "Designer";
			default: return "UNKNOWN";
			}
		}
	}
}
