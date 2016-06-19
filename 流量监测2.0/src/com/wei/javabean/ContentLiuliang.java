package com.wei.javabean;

/**
 * 流量信息的javabean
 * 
 * @author Corsair
 * 
 */
public class ContentLiuliang {

	private String uid;//接收字节数
	private String appname;//app名字
	private long sentbyte;//发送字节数
	private long receivebyte;//接收字节数
	private long lasttx;//上次发送字节数
	private long lastrx;//上次接受字节数



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
