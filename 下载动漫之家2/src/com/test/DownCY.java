package com.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.NetUtil;

/**
 * 本类用来下载成语
 * @author k
 *
 */
public class DownCY {

	private static final String[] yy = {"A","B","C","D","E","F","G","H","J","K","L","M","N","O","P","Q","R","S","T","W","X","Y","Z"};
	
	private static final String BASE_URL = "http://chengyu.t086.com/";
	
	public static void main(String[] args) {
		
		NetUtil.initAuth();
		
		File file = new File("e:/成语.txt");
		
		file.delete();
		
		List<String> mulist = new ArrayList<String>();
		
		for(int i=0;i<yy.length;i++) {
			
			mulist.add(BASE_URL+"list/"+yy[i]+"_1.html");
			
		}
		
		String url = "";
		
		for(int i=0;i<mulist.size();i++) {
			
			url = mulist.get(i);
			
			
			dealCY(url,file);
			
		}
		
	}
	
	private static void dealCY(String url,File file) {
		
		String html = NetUtil.readbyUnit(url);
		
		String dv = NetUtil.getTargetDV(html, "class=\"title\"");
		
		Pattern p = Pattern.compile("<a href=\"/[^\"]+?\">([^<]+?)</a>");
		
		Matcher m = p.matcher(dv);
		
		while(m.find()) {
			write2File(m.group(1).trim(),file);
		}
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		url = getNextUrl(html);
		
		if(!"".equals(url)) {
	
			dealCY(url,file);
		}
	}

	private static void write2File(String trim, File file) {
		
		try {
			FileOutputStream fos = new FileOutputStream(file,true);
			fos.write((trim+"\r\n").getBytes());
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private static String getNextUrl(String html) {
		String res = "";
		
		Pattern p = Pattern.compile("<a href=\"([^\"]+?)\">\\s*下一页\\s*</a>");
		
		Matcher m = p.matcher(html);
		
		if(m.find()) {
			res = "http://chengyu.t086.com/list/"+m.group(1);
		}
		
		return res;
	}

}
