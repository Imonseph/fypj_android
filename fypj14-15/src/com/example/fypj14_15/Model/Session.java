package com.example.fypj14_15.Model;

public class Session {

	private int sessionId;
	private int childId;
	private String lastDated;
	private String engagemt;
	private int prompt;
	private String condition;
	private int flagCount;
	private int isGraded;
	
	// timePaused for resuming session
	private String timePaused;
	
	private final String[] engagemtList = new String[]{"active,on","active,non","passive,on","passive,non"};
	private final String[] columnNames = new String[]{"sessionID","childID","lastDated","engagemt","prompt","condition","flagCount","timePaused","isGraded"};
	
	// default constructor
	public Session(){}
	
	// primary constructor
	public Session(int sessionId, int childId, String lastDated, String engagemt, 
			int prompt, String condition, int flagCount, String timePaused, int isGraded) {
		
		this.sessionId = sessionId;
		this.childId = childId;
		this.lastDated = lastDated;
		this.engagemt = engagemt;
		this.prompt = prompt;
		this.condition = condition;
		this.flagCount = flagCount;
		this.timePaused = timePaused;
		this.isGraded = isGraded;
	}

	public int getSessionId() {
		return sessionId;
	}
	
	public int getChildId() {
		return childId;
	}	

	public String getLastDated() {
		return lastDated;
	}
	public void setLastDated(String lastDated) {
		this.lastDated = lastDated;
	}

	public String getEngagemt() {
		return engagemt;
	}
	
	public String getEngagemt(int index) {
		return engagemtList[index];
	}
	
	public int getEngagemtIndex() {
		
		for (int i=0; i<engagemtList.length; i++)
		{
			if (engagemt.equalsIgnoreCase(engagemtList[i]))
				return i;
		}
		return 0;
	}

	public void setEngagemt(int index) {
		this.engagemt = engagemtList[index];
	}

	public int getFlagCount() {
		return flagCount;
	}

	public void setFlagCount(int flagCount) {
		this.flagCount = flagCount;
	}

	public int getIsGraded() {
		return isGraded;
	}

	public void setIsGraded(int isGraded) {
		this.isGraded = isGraded;
	}

	public int getPrompt() {
		return prompt;
	}

	public void setPrompt(int prompt) {
		this.prompt = prompt;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	// ----------------------------------------------
	public String getTimePaused() {
		return timePaused;
	}
	public void setTimePaused(String timePaused) {
		this.timePaused = timePaused;
	}

	// ----------------------------------------------
	public String getSingleColumn(int colmId) {
		return columnNames[colmId];
	}
		
	public String[] getAllColumns() {
		return columnNames;
	}
}
