package com.example.fypj14_15.Model;

public class ListItem {

	private int icon;
	private String name;
	private String Id;
	private String Status;
	
	public ListItem(int icon, String name, String id, String status)
	{
		super();
		this.icon = icon;
		this.name = name;
		this.Id = id;
		this.Status = status;
	}
	
	// getters and setters
	public int getIcon() {
		return icon;
	}
	public void setIcon(int icon) {
		this.icon = icon;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}
}
