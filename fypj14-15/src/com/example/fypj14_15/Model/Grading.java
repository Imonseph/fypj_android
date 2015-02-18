package com.example.fypj14_15.Model;

public class Grading {

	private int gradingID;
	private int sessionID;
	private int[] qnAns = new int[7];
	
	private final String[] columnNames = 
			new String[]{"gradingID","sessionID","qn1ans","qn2ans",	"qn3ans","qn4ans","qn5ans","qn6ans"};

	// default constructor
	public Grading(){}
	
	// primary constructor
	public Grading(int gradingID,int sessionID,int qnAns[]) {
		this.gradingID = gradingID;
		this.sessionID = sessionID;
		this.qnAns = qnAns;
	}
	
	// secondary constructor
		public Grading(int gradingID, int sessionID, int qn1Ans, int qn2Ans, int qn3Ans, int qn4Ans, int qn5Ans, int qn6Ans) {
			this.gradingID = gradingID;
			this.sessionID = sessionID;
			qnAns[0] = qn1Ans; qnAns[1] = qn2Ans; qnAns[2] = qn3Ans; 
			qnAns[3] = qn4Ans; qnAns[4] = qn5Ans; qnAns[5] = qn6Ans;
		}
	
	// getters and setters
	
	public int getGradingID() {
		return gradingID;
	}
	public void setGradingID(int gradingID) {
		this.gradingID = gradingID;
	}
	public int getSessionID() {
		return sessionID;
	}
	public void setSessionID(int sessionID) {
		this.sessionID = sessionID;
	}
	public int getQn1Ans() {
		return qnAns[0];
	}
	public void setQn1Ans(int qn1Ans) {
		this.qnAns[0] = qn1Ans;
	}
	public int getQn2Ans() {
		return qnAns[1];
	}
	public void setQn2Ans(int qn2Ans) {
		this.qnAns[1] = qn2Ans;
	}
	public int getQn3Ans() {
		return qnAns[2];
	}
	public void setQn3Ans(int qn3Ans) {
		this.qnAns[2] = qn3Ans;
	}
	public int getQn4Ans() {
		return qnAns[3];
	}
	public void setQn4Ans(int qn4Ans) {
		this.qnAns[3] = qn4Ans;
	}
	public int getQn5Ans() {
		return qnAns[4];
	}
	public void setQn5Ans(int qn5Ans) {
		this.qnAns[4] = qn5Ans;
	}
	public int getQn6Ans() {
		return qnAns[5];
	}
	public void setQn6Ans(int qn6Ans) {
		this.qnAns[5] = qn6Ans;
	}
	
	// ----------------------------------------------
	public String getSingleColumn(int colmId) {
		return columnNames[colmId];
	}
		
	public String[] getAllColumns() {
		return columnNames;
	}
}
