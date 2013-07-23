package com.shephertz.app42.android.pushservice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.shephertz.app42.paas.sdk.android.App42API;
import com.shephertz.app42.paas.sdk.android.App42CallBack;

public class GCMIntentService extends GCMBaseIntentService {

//	public static boolean isFromNotification = false;
	public static String notificationMessage = "";
	static String projectNo="<Your Project No>";

	public GCMIntentService() {
		super(projectNo);
	}

	@Override
	protected void onError(Context arg0, String arg1) {
		Log.i(TAG, "Device registered: regId = " + arg1);
	}

	@Override
	protected void onMessage(Context context, Intent intent) {

		Bundle b = intent.getExtras();
		String message = (String) b.get("message");
		Log.i(TAG, "Received message " + message);
		 App42API.buildLogService().setEvent("Message", "Delivered",  new App42CallBack() {
				
				@Override
				public void onSuccess(Object arg0) {
					// TODO Auto-generated method stub
				}
				@Override
				public void onException(Exception arg0) {
					System.out.println(" onMessage  Exception : " +arg0);
					  
				}
			});
		  
		notificationMessage = message;
		displayMessage(context, message);
		generateNotification(context, message);
		try{
			String methodName = ServiceContext.instance(context).getCallBackMethod();
			String gameObj = ServiceContext.instance(context).getGameObject();
			Log.i(TAG, "Method Name " + methodName);
			Log.i(TAG, "gameObj " + gameObj);
			
		App42PushActivity.messageReceived(message, ServiceContext.instance(context).getCallBackMethod(),
				ServiceContext.instance(context).getGameObject());	
		}
		catch(Exception e){
			gameClosedLog(e.toString());
			e.printStackTrace();
		}
		catch (Error error) {
			gameClosedLog(error.toString());
		}
			// TODO: handle exception
	}

	private static void gameClosedLog(String exception){
		App42API.buildLogService().setEvent("Message", "application is closed : "+exception,  new App42CallBack() {
			
			@Override
			public void onSuccess(Object arg0) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onException(Exception arg0) {
				System.out.println(" onMessage  Exception : " +arg0);
				  
			}
		});
	}
	@Override
	protected void onDeletedMessages(Context context, int total) {
		Log.i(TAG, "Received deleted messages notification");
		String message = "" + total + "Message deleted ";
		displayMessage(context, message);
		generateNotification(context, message);
	}

	@Override
	protected void onRegistered(Context context, String registrationId) {
	
		Log.i(TAG, "Device registered: regId = " + registrationId);
		App42Service.instance(context).regirsterPushOnApp42(context,
				ServiceContext.instance(context).getApp42UserId(), registrationId
			);

	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		Log.i(TAG, "onUnregistered "+arg1);
	}

	/**
	 * Issues a notification to inform the user that server has sent a message.
	 */
	public static void generateNotification(Context context, String message) {
		long when = System.currentTimeMillis();
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(android.R.drawable.stat_notify_sync,message,when);
		Intent notificationIntent = new Intent(context, App42PushActivity.class);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent intent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);
		notification.setLatestEventInfo(context, ServiceContext.instance(context).getGameObject(), message, intent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notificationManager.notify(0, notification);

	}

	/**
	 * Notifies UI to display a message.
	 * <p>
	 * This method is defined in the common helper because it's used both by the
	 * UI and the background service.
	 * 
	 * @param context
	 *            application's context.
	 * @param message
	 *            message to be displayed.
	 */
	public static void displayMessage(Context context, String message) {
		Intent intent = new Intent(ServiceConstants.DisplayMessageAction);
		intent.putExtra(ServiceConstants.NotificationMessage, message);
		context.sendBroadcast(intent);
	}

}
