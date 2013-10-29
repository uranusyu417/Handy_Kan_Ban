package com.handykanban;



public class User {
	
	/***
	 * use user id to get info from DB, and return an object without role info
	 * @param id
	 */
	public User(int id)
	{
		User _u = HandyKBDBHelper.getDBHelperInstance().getUserByID(id);
		this.userID = id;
		this.name = _u.name;
		this.role = _u.role;
	}
	
	/***
	 * empty object
	 */
	public User()
	{
		
	}

	private int userID;
	private String name;
	private Role role;
	
	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

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
		public static Role intToRole(int r)
		{
			switch(r)
			{
			case 0: return Designer;
			case 1: return PO;
			default: return UNKNOWN;
			}
		}
		public static int RoleToInt(Role r)
		{
			switch(r)
			{
			case Designer: return 0;
			case PO: return 1;
			default: return -1;
			}
		}
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public boolean equals(Object o) {
		User _u = (User)o;
		return userID==_u.userID;
	}
}
