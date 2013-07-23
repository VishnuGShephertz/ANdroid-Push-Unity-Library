package com.shephertz.app42.android.pushservice;

import android.content.Context;
import android.os.Handler;

import com.google.android.gcm.GCMRegistrar;
import com.shephertz.app42.paas.sdk.android.App42API;
import com.shephertz.app42.paas.sdk.android.App42Exception;
import com.shephertz.app42.paas.sdk.android.push.PushNotificationService;

public class App42Service {

	private PushNotificationService pushService;
	private static App42Service mInstance = null;
  
	private App42Service(Context context) {
		App42API.initialize(
				context,
				ServiceContext.instance(context).getApiKey(),
				ServiceContext.instance(context).getSecretKey());
				this.pushService = App42API.buildPushNotificationService();
	}

	
	/*
	 * instance of class
	 */
	public static App42Service instance(Context coontext) {

		if (mInstance == null) {
			mInstance = new App42Service(coontext);
		}

		return mInstance;
	}

	public void regirsterPushOnApp42(final Context context,
			final String userID, final String deviceId) {
		final Handler callerThreadHandler = new Handler();
		new Thread() {
			@Override
			public void run() {
				try {
					pushService.storeDeviceToken(userID, deviceId);
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
						App42PushActivity.messageReceived("Registration done with user "+userID,ServiceContext.instance(context).getCallBackMethod(),
								ServiceContext.instance(context).getGameObject());
						}
					});
				} catch (final App42Exception e) {
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
						
						}
					});

				}
			}
		}.start();
	}

	/*
	 * This function allows to register device for PushNotification service
	 */
	public void registerForPushNotification(Context context, String userID) {
		GCMRegistrar.checkDevice(context);
		GCMRegistrar.checkManifest(context);
		final String deviceId = GCMRegistrar.getRegistrationId(context);
		if (deviceId.equals("")) {
			try {
				GCMRegistrar.register(context, ServiceContext.instance(context).getProjectNo());
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				regirsterPushOnApp42(context, userID, deviceId);
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
}
