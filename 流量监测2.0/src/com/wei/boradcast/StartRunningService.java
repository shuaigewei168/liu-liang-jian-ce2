package com.wei.boradcast;


import com.wei.service.ServiceToWeb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 用于循环每30分钟执行一次定时服务
 * @author Corsair
 *
 */
public class StartRunningService extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent intent3=new Intent(context,ServiceToWeb.class);
		context.startService(intent3);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
