package com.wei.boradcast;

import com.wei.service.ServiceToWeb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * ¿ª»ú¹ã²¥
 * @author Corsair
 *
 */
public class StartBoot extends BroadcastReceiver {

	public void onReceive(Context context, Intent intent) {
		Intent intent2=new Intent(context,ServiceToWeb.class);
		context.startService(intent2);
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
