package com.shephertz.app42.android.pushservice;

import com.shephertz.app42.paas.sdk.android.App42API;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

import android.os.Bundle;
import android.util.Log;

public class App42PushActivity extends UnityPlayerActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("App42PushActivity", "Launched");
	}

	public static void messageReceived(String message, String method,
			String gameObject) {
		UnityPlayer.UnitySendMessage(gameObject, method, message);
		messageOpenLog(message);
		Log.i(" Message Sent : " , message);
	}

	private static void messageOpenLog(String message) {
		App42API.buildLogService().setEvent("Message", "Open  " + message,
				new App42CallBack() {

					@Override
					public void onSuccess(Object arg0) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onException(Exception arg0) {
						System.out.println(" onMessage  Exception : " + arg0);

					}
				});
	}

	public void setCurrentUser(String userId) {
		ServiceContext.instance(App42PushActivity.this).saveApp42UserId(userId);
	}

	public void intialize(String apiKey, String secretKey) {
		ServiceContext.instance(App42PushActivity.this).saveApiSecretKey(
				apiKey, secretKey);
	}

	public void setProjectNo(String projectNo) {
		GCMIntentService.projectNo = projectNo;
		ServiceContext.instance(App42PushActivity.this)
				.saveProjectNo(projectNo);

	}

	private void doRegistration() {
		App42Service.instance(App42PushActivity.this)
				.registerForPushNotification(
						App42PushActivity.this,
						ServiceContext.instance(App42PushActivity.this)
								.getApp42UserId());
	}

	public void registerForNotification(String callBackMethod,
			String gameObjectName) {
		
		ServiceContext.instance(App42PushActivity.this).saveUnityInfo(
				callBackMethod, gameObjectName);
		doRegistration();
	}

}
