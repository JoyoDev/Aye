package com.joyodev.aye.model;

import java.util.List;

public class VulnerabilitiesItem{
	private String severity;
	private List<NvdDataItem> nvdData;
	private String jsonMemberPackage;
	private String packagePath;
	private String packageCpe23;
	private List<Object> vendorData;
	private String packageType;
	private String packageCpe;
	private String url;
	private boolean willNotFix;
	private String feed;
	private String packageVersion;
	private String fix;
	private String vuln;
	private String feedGroup;
	private String packageName;

	public String getSeverity(){
		return severity;
	}

	public List<NvdDataItem> getNvdData(){
		return nvdData;
	}

	public String getJsonMemberPackage(){
		return jsonMemberPackage;
	}

	public String getPackagePath(){
		return packagePath;
	}

	public String getPackageCpe23(){
		return packageCpe23;
	}

	public List<Object> getVendorData(){
		return vendorData;
	}

	public String getPackageType(){
		return packageType;
	}

	public String getPackageCpe(){
		return packageCpe;
	}

	public String getUrl(){
		return url;
	}

	public boolean isWillNotFix(){
		return willNotFix;
	}

	public String getFeed(){
		return feed;
	}

	public String getPackageVersion(){
		return packageVersion;
	}

	public String getFix(){
		return fix;
	}

	public String getVuln(){
		return vuln;
	}

	public String getFeedGroup(){
		return feedGroup;
	}

	public String getPackageName(){
		return packageName;
	}

	@Override
 	public String toString(){
		return 
			"VulnerabilitiesItem{" + 
			"severity = '" + severity + '\'' + 
			",nvd_data = '" + nvdData + '\'' + 
			",package = '" + jsonMemberPackage + '\'' + 
			",package_path = '" + packagePath + '\'' + 
			",package_cpe23 = '" + packageCpe23 + '\'' + 
			",vendor_data = '" + vendorData + '\'' + 
			",package_type = '" + packageType + '\'' + 
			",package_cpe = '" + packageCpe + '\'' + 
			",url = '" + url + '\'' + 
			",will_not_fix = '" + willNotFix + '\'' + 
			",feed = '" + feed + '\'' + 
			",package_version = '" + packageVersion + '\'' + 
			",fix = '" + fix + '\'' + 
			",vuln = '" + vuln + '\'' + 
			",feed_group = '" + feedGroup + '\'' + 
			",package_name = '" + packageName + '\'' + 
			"}";
		}
}