package com.example.fypj14_15.Model;

public class GroupItem {

	private int value;
	private boolean onIndexSelect;
	 
	public GroupItem (){
		value = 0;
		onIndexSelect = false;
	}
	
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public boolean getOnSelect() {
		return onIndexSelect;
	}
	public void setOnSelect(boolean onIndexSelect) {
		this.onIndexSelect = onIndexSelect;
	}
}
