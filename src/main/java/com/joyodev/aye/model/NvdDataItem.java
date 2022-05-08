package com.joyodev.aye.model;

public class NvdDataItem{
	private CvssV3 cvssV3;
	private CvssV2 cvssV2;
	private String id;

	public CvssV3 getCvssV3(){
		return cvssV3;
	}

	public CvssV2 getCvssV2(){
		return cvssV2;
	}

	public String getId(){
		return id;
	}

	@Override
 	public String toString(){
		return 
			"NvdDataItem{" + 
			"cvss_v3 = '" + cvssV3 + '\'' + 
			",cvss_v2 = '" + cvssV2 + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}
