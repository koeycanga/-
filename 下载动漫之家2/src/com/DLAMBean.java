package com;

public class DLAMBean {
//123
	private int fileflag;
	private int end;
	private String mh_cid;
	
	private int cid;
	
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	private String url;
	
	private String mh_name;
	
	public String getMh_name() {
		return mh_name;
	}
	public void setMh_name(String mh_name) {
		this.mh_name = mh_name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMh_cid() {
		return mh_cid;
	}
	public void setMh_cid(String mh_cid) {
		this.mh_cid = mh_cid;
	}
	public int getFileflag() {
		return fileflag;
	}
	public void setFileflag(int fileflag) {
		this.fileflag = fileflag;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
}
