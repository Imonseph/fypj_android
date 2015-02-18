package com.example.fypj14_15.Model;

public class Profile {

	private int id;
	private String name;
	private String gender;
	private String priDiagnos;
	private String secDiagnos;
	private String remarks;
	private String inspector;
	private String venue;
	private String activity;
	private int adultNo;
	private int childrenNo;
	
	private String status;
	
	private final String[] columnNames = new String[]{"childID","name","gender","priDiagnos",
			"secDiagnos","remarks", "inspector","venue","activity","adultNo","childrenNo","status"};
	
	// default constructor
	public Profile(){}
	
	// primary constructor
	public Profile(int id, String name, String gender, String priDiagnos, String secDiagnos, String remarks,
			String inspector, String venue, String activity, int adultNo, int childrenNo, String status) {
		
		this.id = id;
		this.name = name;
		this.gender = gender;
		this.priDiagnos = priDiagnos;
		this.secDiagnos = secDiagnos;
		this.remarks = remarks;
		this.inspector = inspector;
		this.venue = venue;
		this.activity = activity;
		this.adultNo = adultNo;
		this.childrenNo = childrenNo;
		this.status = status;
	}
	
	// getters and setters
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPriDiagnos() {
		return priDiagnos;
	}
	public void setPriDiagnos(String priDiagnos) {
		this.priDiagnos = priDiagnos;
	}

	public String getSecDiagnos() {
		return secDiagnos;
	}
	public void setSecDiagnos(String secDiagnos) {
		this.secDiagnos = secDiagnos;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getInspector() {
		return inspector;
	}
	public void setInspector(String inspector) {
		this.inspector = inspector;
	}

	public String getVenue() {
		return venue;
	}
	public void setVenue(String venue) {
		this.venue = venue;
	}

	public String getActivity() {
		return activity;
	}
	public void setActivity(String activity) {
		this.activity = activity;
	}

	public int getAdultNo() {
		return adultNo;
	}
	public void setAdultNo(int adultNo) {
		this.adultNo = adultNo;
	}

	public int getChildrenNo() {
		return childrenNo;
	}
	public void setChildrenNo(int childrenNo) {
		this.childrenNo = childrenNo;
	}
	
	// --------------------------------------------
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	// --------------------------------------------
	public String getSingleColumn(int colmId) {
		return columnNames[colmId];
	}
	
	public String[] getAllColumns() {
		return columnNames;
	}
}
