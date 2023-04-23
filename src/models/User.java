package models;

import java.io.Serializable;

public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String Role;
	String UserName;
	String password;
	private String FirstName;
	private String LastName;
	private String ID;
	private String Faculty;
	
	public User(String userName, String password) {
		
		UserName = userName;
		this.password = password;
	}
	public User(String userName, String password,String role) {
		
		Role = role;
		UserName = userName;
		this.password = password;
	}
	public User(String userName, String password ,String role , String firstName,String LastName, String iD) {
		super();
		this.Role = role;
		this.UserName = userName;
		this.password = password;
		this.FirstName = firstName;
		this.LastName = LastName;
		this.ID = iD;
		
	}
	public String getRole() {
		return Role;
	}
	public void setRole(String role) {
		Role = role;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFirstName() {
		return FirstName;
	}
	public void setFirstName(String firstName) {
		FirstName = firstName;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getFaculty() {
		return Faculty;
	}
	public void setFaculty(String faculty) {
		Faculty = faculty;
	}
	public String getLastName() {
		return LastName;
	}
	public void setLastName(String lastName) {
		LastName = lastName;
	}
	

}
