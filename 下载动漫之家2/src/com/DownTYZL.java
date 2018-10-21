package com;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import utils.NetUtil;

/**
 * 本类用来下载 中华文本库的 文章
 * @author k
 *
 */
public class DownTYZL {

	private static final String BASEURL = "http://www.chinadmd.com/file/6eass6epppo6p63serx6zert";
	
	private static final String FILENAME = "E:/闲聊神州房市走势.txt";
	
	private static final int end = 124;
	
	public static void main(String[] args) {
		
		NetUtil.initAuth();
		
		for(int i=1;i<=end;i++) {
		
			String html = NetUtil.readbyUnit(BASEURL+"_"+i+".html");
			
			String targetDV = getTargetDV(html,"<div class=\"tofu-txt\">").trim().replace("<p>", "").replace("</p>", "\r\n").replace("<br/>", "");
			
			String[] ss = targetDV.split("\r\n");
			
			StringBuilder sb = new StringBuilder();
			
			for(String s:ss) {
				s = "    "+s.trim()+"\r\n";
				sb.append(s);
			}
			
			File f = new File(FILENAME);
			
			try {
				FileOutputStream fos = new FileOutputStream(f,true);
				fos.write(sb.toString().getBytes());
				fos.flush();
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

	private static String getTargetDV(String html,String regex) {
		
		String[] ss = html.split("\r\n");
		
		boolean b = false;
		
		StringBuilder sb = new StringBuilder();
		
		for(String s:ss) {
			if(s.contains(regex)) {
				b = true;
				continue;
			}
			if(b&&s.contains("</div")) {
				break;
			}
			if(b) {
				sb.append(s);
			}
			
		}
		return sb.toString();
	}

}
