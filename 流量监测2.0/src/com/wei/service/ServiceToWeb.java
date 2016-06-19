package com.wei.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.wei.boradcast.StartRunningService;
import com.wei.checkliuliang.MainActivity;
import com.wei.services.DbOpenHelper;
import com.wei.services.SQLservice;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

public class ServiceToWeb extends Service {

	private SQLservice sqlservice;
	private DbOpenHelper dbOpenHelper;
	private String content;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Toast.makeText(getApplicationContext(), (String) msg.obj, 0).show();
		}
	};

	// private File file;
	// private ArrayList<ArrayList<String>>bill2List=new
	// ArrayList<ArrayList<String>>();
	// private String[] title = { "ʱ��","APP����", "��������", "��������" };

	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * ��ʱ�������ݵ�������
	 */
	public int onStartCommand(Intent intent, int flags, int startId) {
		Toast.makeText(getApplicationContext(), "�������", 0).show();
		new Thread(new Runnable() {
			public void run() {
				// initData();
				String json = getdata();// ��ȡ���ݿ����ݱ��JSON
				senttoweb(json);// ����JSON
			}
		}).start();
		AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);// ��ȡϵͳʱ�ӹ�����
		int halfhour = 10000;// ��Сʱ�ĺ�����
		long alarmtime = SystemClock.elapsedRealtime() + halfhour;// ����ʱ��+��ʱʱ��
		Intent brocastintent = new Intent(this, StartRunningService.class);
		PendingIntent p = PendingIntent.getBroadcast(this, 0, brocastintent, 0);
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, alarmtime, p);
		return super.onStartCommand(intent, flags, startId);
	}

	protected void senttoweb(String json) {
		String urlPath = "http://192.168.1.114:8080/Web3/servlet/GetData";
		URL url;
		try {
			url = new URL(urlPath);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("ser-Agent", "Fiddler");
			conn.setRequestProperty("Content-Type", "application/json");
			OutputStream os = conn.getOutputStream();
			os.write(json.getBytes("utf-8"));
			int code = conn.getResponseCode();
			if (code == 200) {
				InputStream is = conn.getInputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					json = new String(buffer, 0, len, "utf-8");

				}
				Message msg = handler.obtainMessage();
				msg.obj = json;
				handler.sendMessage(msg);
			} else {
				Message msg = handler.obtainMessage();
				msg.obj = "�Ҳ���404";
				handler.sendMessage(msg);
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected String getdata() {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from totalll", null);
		String data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		content = "";
		// JSONObject arrayjsonObject = new JSONObject();
		// int i=0;
		while (cursor.moveToNext()) {
			try {
				// i++;
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("data", data);// ��ǰʱ��
				jsonObject.put("appname", cursor.getString(2));// appname
				jsonObject.put("sentbyte", String.valueOf(cursor.getLong(3)));// sentbyte
				jsonObject.put("getbyte", String.valueOf(cursor.getLong(4)));// getbyte
				content = content + String.valueOf(jsonObject);
				// arrayjsonObject.put("App",jsonObject);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		// String content = String.valueOf(arrayjsonObject);
		db.close();
		cursor.close();
		return content;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		dbOpenHelper = new DbOpenHelper(getApplicationContext());
		dbOpenHelper.getWritableDatabase();
		Log.i("ServiceToWeb", "11111");
	}

	// /**
	// * ��ʼ����д�����ݵ�EXCEL
	// */
	// public void initData() {
	// file = new File(getSDPath() + "/LiuLiang");
	// //Toast.makeText(getApplicationContext(), getSDPath(), 1).show();
	// Log.i("ServiceToWeb",file.toString());
	// makeDir(file);
	// ExcelUtils.initExcel(file.toString() + "/liuliang.xls",
	// title);//��ʼ��excel��û�����ݵ�
	// ExcelUtils.writeObjListToExcel(getBillData(), getSDPath() +
	// "/LiuLiang/liuliang.xls", this);
	// //������д����
	// }
	//
	//
	// /**
	// * ��ȡSD��·��
	// * @return
	// */
	// public String getSDPath() {
	// File sdDir = null;
	// boolean sdCardExist =
	// Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	// if (sdCardExist) {
	// sdDir = Environment.getExternalStorageDirectory();
	// }
	// String dir = sdDir.toString();
	// return dir;
	//
	// }
	//
	// /**
	// * ����APP���ƣ����������ͽ�������
	// */
	// public ArrayList<ArrayList<String>> findData() {
	// SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
	// Cursor cursor = db.rawQuery("select * from totalll",
	// null);
	// while (cursor.moveToNext()) {
	// ArrayList<String> beanList=new ArrayList<String>();
	// beanList.add(new SimpleDateFormat("yyyy-MM-dd").format(new
	// Date()));//��ǰʱ��
	// beanList.add(cursor.getString(2));//appname
	// beanList.add(String.valueOf(cursor.getLong(3)));//sentbyte
	// beanList.add(String.valueOf(cursor.getLong(4)));//getbyte
	// bill2List.add(beanList);
	// }
	// db.close();
	// cursor.close();
	// return bill2List;
	// }
	//
	// /**
	// * ��ȡ��������ݿ������
	// * @return
	// */
	// private ArrayList<ArrayList<String>> getBillData() {
	// bill2List.clear();
	// return findData();
	//
	// }
	//
	// public static void makeDir(File dir) {
	// if (!dir.getParentFile().exists()) {
	// makeDir(dir.getParentFile());
	// }
	// dir.mkdirs();
	// }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
