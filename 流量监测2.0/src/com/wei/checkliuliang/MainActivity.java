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

	private DbOpenHelper helper;// db���󣬵�δ��ȡ����Ȩ
	private SQLservice ser;// db����
	private Boolean finishthread = true;
	private ListView listView;// ��ʾlistview
	private long lasttx;// ���ڼ�¼��һ�ε�txֵ
	private long lastrx;// ���ڼ�¼��һ�ε�rxֵ
	int i = 0;
	int k = 0;

	List<ContentLiuliang> contacts = new ArrayList<ContentLiuliang>();// �������app������Ϣ
	// ExecutorService fixedThreadPool = Executors.newFixedThreadPool(20);//
	// �����̳߳�

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			ListViewAdapter listViewAdapter = new ListViewAdapter(
					MainActivity.this, (List<ContentLiuliang>) msg.obj,
					R.layout.listview_item);// �����Զ���������
			listView.setAdapter(listViewAdapter);
			finishthread = true;
		    Intent intent=new Intent(MainActivity.this, ServiceToWeb.class);//�򿪺�̨����
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
	 * ��ȡ������ϢgetAppTrafficLis�����Ҵ�����ݿ�
	 */
	public void getAppTrafficList() {
		PackageManager pm = this.getPackageManager();// ��ȡ��������
		List<ApplicationInfo> pinfos = pm.getInstalledApplications(0);// ��ȡӦ������
		ContentLiuliang liuliangtext;// �����õ�����
		finishthread = false;// ��ʼ��Ϊfalse
		// contacts.clear();//���ԭ������ʾ�������ظ���ʾ,����contacts.clear����Ļ�ڻ������ؿյ�contacts�����ˣ����Ժ�����remove��0��
		i = contacts.size();// ��ȡ��ǰ�ļ����Ƿ����
		int k=0;//��ֹɾ����Ӧ�ú�remove���˳���
		// ����ÿ��Ӧ�ð���Ϣ
		for (ApplicationInfo info : pinfos) {
			int uid = info.uid; // ������uid
			long tx = TrafficStats.getUidTxBytes(uid);// ���͵� �ϴ�������byte
			long rx = TrafficStats.getUidRxBytes(uid);// ���ص����� byte
			if (rx < 0 || tx < 0) {
				continue;
			} else {
				liuliangtext = ser.find(uid);
				ser.closedb();
				if (i == 0||liuliangtext==null)// �����app������Ϣ�������ڻ�����ӵ�Ӧ���Ҳ���
				{

					ContentLiuliang liuliang2 = new ContentLiuliang(
							String.valueOf(uid), String.valueOf(info
									.loadLabel(pm)), tx, rx, tx, rx);
					ser.save(liuliang2);// ���������Ϣ�����ݿ�
					contacts.add(liuliang2);
				} else// ���������
				{
					
					if (tx == 0 && rx == 0) {
						ContentLiuliang liuliang2 = new ContentLiuliang(
								String.valueOf(uid), String.valueOf(info
										.loadLabel(pm)),
								liuliangtext.getSentbyte(),
								liuliangtext.getReceivebyte(), tx, rx);
						ser.updata(liuliang2);
						if(k<i) contacts.remove(0);// �����һ��Ԫ��
						k++;
						contacts.add(liuliang2);

					} else {
						lasttx = liuliangtext.getLasttx();// ��ȡ��һ�ε�����
						lastrx = liuliangtext.getLastrx();
						// Toast.makeText(this,String.valueOf(lasttx),1).show();
						ContentLiuliang liuliang2 = new ContentLiuliang(
								String.valueOf(uid), String.valueOf(info
										.loadLabel(pm)), tx
										+ liuliangtext.getSentbyte() - lasttx,
								rx + liuliangtext.getReceivebyte() - lastrx,
								tx, rx);
						ser.updata(liuliang2);
						if(k<i) contacts.remove(0);// �����һ��Ԫ��
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

	// �����ť
	public void showList(View v) {

		
		if (finishthread) {
			new Thread() {
				public void run() {
					getAppTrafficList();
					handler.sendMessage(handler.obtainMessage(10, contacts));// ��������Ϣͨ��handle����
				}
			}.start();
		}
 

		// if(finishthread)//��һ���߳̽���
		// {
		// fixedThreadPool.execute(new Runnable() {
		// public void run() {
		// //Toast.makeText(getApplicationContext(), "dianji", 1).show();
		// getAppTrafficList();
		// handler.sendMessage(handler.obtainMessage(10, contacts));//
		// ��������Ϣͨ��handle����
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
