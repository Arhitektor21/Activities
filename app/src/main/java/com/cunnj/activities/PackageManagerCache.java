package com.cunnj.activities;

import android.content.ComponentName;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import java.util.HashMap;
import java.util.Map;

public class PackageManagerCache {
	public static PackageManagerCache instance = null;
	protected Map<String,MyPackageInfo> packageInfos;
	protected Map<ComponentName,MyActivityInfo> activityInfos;
	protected PackageManager pm;
	
	public static PackageManagerCache getPackageManagerCache(PackageManager pm) {
		if(instance == null) {
			instance = new PackageManagerCache(pm);
		}
		return instance;
	}
	
	private PackageManagerCache(PackageManager pm) {
		this.pm = pm;
		this.packageInfos = new HashMap<>();
		this.activityInfos = new HashMap<>();
	}
	
	MyPackageInfo getPackageInfo(String packageName) throws NameNotFoundException {
		MyPackageInfo myInfo;
		
		synchronized(packageInfos) {
			if (packageInfos.containsKey(packageName)) {
				return packageInfos.get(packageName);
			}
			
			PackageInfo info;
			try {
				info = pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
			} catch (NameNotFoundException e) {
				throw e;
			}
			
			myInfo = new MyPackageInfo(info, pm, this);
			packageInfos.put(packageName, myInfo);
		}
		
		return myInfo;
	}
	
	MyActivityInfo getActivityInfo(ComponentName activityName) {
		MyActivityInfo myInfo;
		
		synchronized(activityInfos) {
			if (activityInfos.containsKey(activityName)) {
				return activityInfos.get(activityName);
			}
			
			myInfo = new MyActivityInfo(activityName, pm);
			activityInfos.put(activityName, myInfo);
		}
		
		return myInfo;
	}
}
