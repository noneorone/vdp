package com.noo.core.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;
import java.util.Map;

public class SharedPreferencesUtil {

    private SharedPreferences mPreferences = null;
    
    private static final Map<String, SharedPreferencesUtil> mMaps = new HashMap<String, SharedPreferencesUtil>();
    
    public static synchronized SharedPreferencesUtil getInstance(Context mContext, String str) {
    	if(mMaps.containsKey(str)) {
    		return mMaps.get(str);
    	}
    	SharedPreferencesUtil result = new SharedPreferencesUtil(mContext, str);
    	mMaps.put(str, result);
    	return result;
	}
    
    private SharedPreferencesUtil(Context mContext, String str) {
        mPreferences = mContext.getSharedPreferences(str, Context.MODE_PRIVATE);
    }
    
    public boolean setParam(String key, String value){
        Editor editor = mPreferences.edit();
        editor.putString(key,value);
        return editor.commit();
    }
    
    public boolean setParam(String key, boolean value){
    	Editor editor = mPreferences.edit();
    	editor.putBoolean(key,value);
    	return editor.commit();
    }
    
    public boolean setParam(String key, int value){
        Editor editor = mPreferences.edit();
        editor.putInt(key, value);
        return editor.commit();
    }
    
    public boolean setParam(String key, long value){
        Editor editor = mPreferences.edit();
        editor.putLong(key, value);
        return editor.commit();
    }
    
    public boolean getParam(String key, boolean defValue){
       return  mPreferences.getBoolean(key, defValue);
    }
    
    public String getParam(String key, String defValue){
    	return  mPreferences.getString(key, defValue);
    }
    
    public int getParam(String key, int defValue){
        return  mPreferences.getInt(key, defValue);
     }
    
    public long getParam(String key, long defValue){
        return  mPreferences.getLong(key, defValue);
    }
    
    public boolean removeParam(String key){
    	 Editor editor = mPreferences.edit();
    	 editor.remove(key);
    	 return editor.commit();
    }
    
    
}
