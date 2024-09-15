package com.java.reward.program.modal;


import java.util.Map;


public class RewardPointResponse {

	
	private Map<String, Integer> monthlyPoints;
	
	private int totalPoints;

	public RewardPointResponse(Map<String, Integer> monthlyPoints, int totalPoints) {
		this.monthlyPoints = monthlyPoints;
		this.totalPoints = totalPoints;
	}

	public Map<String, Integer> getMonthlyPoints() {
		return monthlyPoints;
	}

	public void setMonthlyPoints(Map<String, Integer> monthlyPoints) {
		this.monthlyPoints = monthlyPoints;
	}

	public int getTotalPoints() {
		return totalPoints;
	}

	public void setTotalPoints(int totalPoints) {
		this.totalPoints = totalPoints;
	}

}