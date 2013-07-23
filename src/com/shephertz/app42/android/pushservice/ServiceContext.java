package com.shephertz.app42.android.pushservice;

import android.content.Context;
import android.content.SharedPreferences;

public class ServiceContext {

	private static ServiceContext mInstance = null;
	SharedPreferences sharedPreference;

	private ServiceContext(Context context) {
		sharedPreference = context.getSharedPreferences(
				ServiceConstants.AppName, context.MODE_PRIVATE);
	}

	/*
	 * instance of class
	 */
	public static ServiceContext instance(Context context) {
		if (mInstance == null) {
			mInstance = new ServiceContext(context);
		}
		return mInstance;
	}

	public String getApp42UserId() {
		return sharedPreference.getString(ServiceConstants.KEY_USER, null);
	}

	public void saveApp42UserId(String app42UserId) {
		SharedPreferences.Editor prefEditor = sharedPreference.edit();
		prefEditor.putString(ServiceConstants.KEY_USER, app42UserId);
		prefEditor.commit();
	}

	public String getApiKey() {
		return sharedPreference.getString(ServiceConstants.KEY_API, null);
	}

	public void saveApiSecretKey(String apiKey, String secretKey) {
		SharedPreferences.Editor prefEditor = sharedPreference.edit();
		prefEditor.putString(ServiceConstants.KEY_API, apiKey);
		prefEditor.putString(ServiceConstants.KEY_SECRET, secretKey);
		prefEditor.commit();

	}

	public String getSecretKey() {

		return sharedPreference.getString(ServiceConstants.KEY_SECRET, null);

	}

	public String getCallBackMethod() {
		return sharedPreference.getString(ServiceConstants.KEY_UNITY_METHOD,
				null);
	}

	public String getGameObject() {
		return sharedPreference.getString(ServiceConstants.KEY_GAME_OBJECT,
				null);
	}

	public void saveUnityInfo(String callbackMethod, String gameObject) {
		SharedPreferences.Editor prefEditor = sharedPreference.edit();
		prefEditor.putString(ServiceConstants.KEY_GAME_OBJECT, gameObject);
		prefEditor.putString(ServiceConstants.KEY_UNITY_METHOD, callbackMethod);
		prefEditor.commit();
	}

	public String getProjectNo() {
		return sharedPreference
				.getString(ServiceConstants.KEY_PROJECT_NO, null);
	}

	public void saveProjectNo(String projectNo) {
		SharedPreferences.Editor prefEditor = sharedPreference.edit();
		prefEditor.putString(ServiceConstants.KEY_PROJECT_NO, projectNo);
		prefEditor.commit();
	}
}
