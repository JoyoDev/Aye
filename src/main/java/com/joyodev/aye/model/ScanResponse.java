package com.joyodev.aye.model;

import java.util.List;

public class ScanResponse {

	private String vulnerabilityType;

	private List<VulnerabilitiesItem> vulnerabilities;

	private String imageDigest;

	public String getVulnerabilityType(){
		return vulnerabilityType;
	}

	public List<VulnerabilitiesItem> getVulnerabilities(){
		return vulnerabilities;
	}

	public String getImageDigest(){
		return imageDigest;
	}

	@Override
 	public String toString(){
		return
			"Response{" +
			"vulnerability_type = '" + vulnerabilityType + '\'' +
			",vulnerabilities = '" + vulnerabilities + '\'' +
			",imageDigest = '" + imageDigest + '\'' +
			"}";
		}
}