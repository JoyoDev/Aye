package com.joyodev.aye.model;

public class CvssV3 {

	private double baseScore;

	private double impactScore;

	private double exploitabilityScore;

	public double getBaseScore(){
		return baseScore;
	}

	public double getImpactScore(){
		return impactScore;
	}

	public double getExploitabilityScore(){
		return exploitabilityScore;
	}

	@Override
 	public String toString(){
		return 
			"CvssV3{" + 
			"base_score = '" + baseScore + '\'' + 
			",impact_score = '" + impactScore + '\'' + 
			",exploitability_score = '" + exploitabilityScore + '\'' + 
			"}";
		}
}
