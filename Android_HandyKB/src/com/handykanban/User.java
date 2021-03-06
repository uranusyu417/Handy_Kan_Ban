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
		this.password = _u.password;
	}
	
	/***
	 * empty object
	 */
	public User()
	{
		
	}

	@Override
	public String toString() {
		return name;
	}

	private int userID;
	private String name;
	private Role role;
	private String password;
	
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public Boolean isPoRole(){
		return(role.toString().equals("PO"));
	}
	
	public Boolean isDesignerRole(){
		return(role.toString().equals("Designer"));
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
	public boolean equals(Object o) {
		User _u = (User)o;
		return userID==_u.userID;
	}
}
