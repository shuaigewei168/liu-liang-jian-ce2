package com.wei.javabean;

/**
 * ������Ϣ��javabean
 * 
 * @author Corsair
 * 
 */
public class ContentLiuliang {

	private String uid;//�����ֽ���
	private String appname;//app����
	private long sentbyte;//�����ֽ���
	private long receivebyte;//�����ֽ���
	private long lasttx;//�ϴη����ֽ���
	private long lastrx;//�ϴν����ֽ���



	public ContentLiuliang(String uid, String appname, long sentbyte,
			long receivebyte, long lasttx, long lastrx) {
		super();
		this.uid = uid;
		this.appname = appname;
		this.sentbyte = sentbyte;
		this.receivebyte = receivebyte;
		this.lasttx = lasttx;
		this.lastrx = lastrx;
	}

	public long getLasttx() {
		return lasttx;
	}

	public void setLasttx(long lasttx) {
		this.lasttx = lasttx;
	}

	public long getLastrx() {
		return lastrx;
	}

	public void setLastrx(long lastrx) {
		this.lastrx = lastrx;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
	
	public String getAppname() {
		return appname;
	}

	public void setAppname(String appname) {
		this.appname = appname;
	}

	public long getSentbyte() {
		return sentbyte;
	}

	public void setSentbyte(long sentbyte) {
		this.sentbyte = sentbyte;
	}

	public long getReceivebyte() {
		return receivebyte;
	}

	public void setReceivebyte(long receivebyte) {
		this.receivebyte = receivebyte;
	}

}
