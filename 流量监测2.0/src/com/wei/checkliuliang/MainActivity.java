package com.wei.checkliuliang;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.wei.javabean.ContentLiuliang;
import com.wei.service.ServiceToWeb;
import com.wei.services.DbOpenHelper;
import com.wei.services.SQLservice;
import com.wei.utils.ListViewAdapter;

import android.net.TrafficStats;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private DbOpenHelper helper;// db对象，但未获取操作权
	private SQLservice ser;// db服务
	private Boolean finishthread = true;
	private ListView listView;// 显示listview
	private long lasttx;// 用于记录上一次的tx值
	private long lastrx;// 用于记录上一次的rx值
	int i = 0;
	int k = 0;

	List<ContentLiuliang> contacts = new ArrayList<ContentLiuliang>();// 存放所有app流量信息
	// ExecutorService fixedThreadPool = Executors.newFixedThreadPool(20);//
	// 创建线程池

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			ListViewAdapter listViewAdapter = new ListViewAdapter(
					MainActivity.this, (List<ContentLiuliang>) msg.obj,
					R.layout.listview_item);// 创建自定义适配器
			listView.setAdapter(listViewAdapter);
			finishthread = true;
		    Intent intent=new Intent(MainActivity.this, ServiceToWeb.class);//打开后台服务
		    startService(intent);

		}
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listView = (ListView) findViewById(R.id.listview);
		helper = new DbOpenHelper(MainActivity.this);
		helper.getWritableDatabase();
		ser = new SQLservice(MainActivity.this);

	}

	/**
	 * 获取流量信息getAppTrafficLis，并且存进数据库
	 */
	public void getAppTrafficList() {
		PackageManager pm = this.getPackageManager();// 获取包管理器
		List<ApplicationInfo> pinfos = pm.getInstalledApplications(0);// 获取应用名字
		ContentLiuliang liuliangtext;// 过渡用的流量
		finishthread = false;// 初始化为false
		// contacts.clear();//清除原来的显示，避免重复显示,但是contacts.clear后屏幕在滑动加载空的contacts而闪退，所以后面用remove（0）
		i = contacts.size();// 获取当前的集合是否存在
		int k=0;//防止删除了应用后，remove少了程序
		// 遍历每个应用包信息
		for (ApplicationInfo info : pinfos) {
			int uid = info.uid; // 获得软件uid
			long tx = TrafficStats.getUidTxBytes(uid);// 发送的 上传的流量byte
			long rx = TrafficStats.getUidRxBytes(uid);// 下载的流量 byte
			if (rx < 0 || tx < 0) {
				continue;
			} else {
				liuliangtext = ser.find(uid);
				ser.closedb();
				if (i == 0||liuliangtext==null)// 如果该app流量信息还不存在或新添加的应用找不到
				{

					ContentLiuliang liuliang2 = new ContentLiuliang(
							String.valueOf(uid), String.valueOf(info
									.loadLabel(pm)), tx, rx, tx, rx);
					ser.save(liuliang2);// 添加流量信息到数据库
					contacts.add(liuliang2);
				} else// 如果存在则
				{
					
					if (tx == 0 && rx == 0) {
						ContentLiuliang liuliang2 = new ContentLiuliang(
								String.valueOf(uid), String.valueOf(info
										.loadLabel(pm)),
								liuliangtext.getSentbyte(),
								liuliangtext.getReceivebyte(), tx, rx);
						ser.updata(liuliang2);
						if(k<i) contacts.remove(0);// 清除第一个元素
						k++;
						contacts.add(liuliang2);

					} else {
						lasttx = liuliangtext.getLasttx();// 获取上一次的流量
						lastrx = liuliangtext.getLastrx();
						// Toast.makeText(this,String.valueOf(lasttx),1).show();
						ContentLiuliang liuliang2 = new ContentLiuliang(
								String.valueOf(uid), String.valueOf(info
										.loadLabel(pm)), tx
										+ liuliangtext.getSentbyte() - lasttx,
								rx + liuliangtext.getReceivebyte() - lastrx,
								tx, rx);
						ser.updata(liuliang2);
						if(k<i) contacts.remove(0);// 清除第一个元素
						k++;
						contacts.add(liuliang2);
					}

				}

			}

		}
		for(;k<i;k++)
		{
			contacts.remove(0);
		}

	}

	// 点击按钮
	public void showList(View v) {

		
		if (finishthread) {
			new Thread() {
				public void run() {
					getAppTrafficList();
					handler.sendMessage(handler.obtainMessage(10, contacts));// 把所有信息通过handle返回
				}
			}.start();
		}
 

		// if(finishthread)//上一个线程结束
		// {
		// fixedThreadPool.execute(new Runnable() {
		// public void run() {
		// //Toast.makeText(getApplicationContext(), "dianji", 1).show();
		// getAppTrafficList();
		// handler.sendMessage(handler.obtainMessage(10, contacts));//
		// 把所有信息通过handle返回
		//
		// }
		// });
		// }

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
