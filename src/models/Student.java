package models;

public class Student {
	private String FirstName;
	private String LastName;
	private String ID;
	private String Faculty;
	
	public Student(String firstName, String lastName ,String faculty,String ID) {
		this.FirstName = firstName;
		this.LastName = lastName;
		this.ID = ID;
		this.Faculty = faculty;
	}
	
	public String getLastName() {
		return LastName;
	}

	public void setLastName(String lastName) {
		LastName = lastName;
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
	public void setID(String lastName) {
		ID = lastName;
	}
	public String getFaculty() {
		return Faculty;
	}
	public void setFaculty(String faculty) {
		Faculty = faculty;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getFirstName()+" "+getLastName()+" "+getID()+" "+getFaculty();
	}
	
}
