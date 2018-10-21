package com;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.DounUtil;
import utils.NetUtil;

public class Down57MH {

	private static final Map<String,String> SESSIONMAP = new HashMap<String,String>();
	
	private static final String FILE_PATH = "D:\\output\\Âþ»­\\ÃÀÊ³ÕìÌ½Íõ\\";
	
	private static final String BASE_URL  = "http://www.57mh.com/1503/"; 
	
	private static final String WB = "?p=";
	
	private static final String MH_NAME = "btjcs";
	
	public static void main(String[] args) {
		
		NetUtil.initAuth();
		
		for(int i=1;i<=16;i++) {
			
			int fileflag = i;
			
			File file = new File(FILE_PATH+fileflag);
			if(!file.exists()||!file.isDirectory()) {
				file.mkdir();
			}
			
			String url = BASE_URL+"0"+i+".html";
			
			SESSIONMAP.put("Referer", url);
			SESSIONMAP.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.162 Safari/537.36");
			
			String html = NetUtil.readbyUnit(url,SESSIONMAP);
			
			int end = getPageEnd(html);
			
			try {
				Thread.sleep(2000+getRandomInt()+getRandomInt());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			int count = 1;
			
			for(int j=1;j<=end;j++) {
				
				SESSIONMAP.put("Referer", url+WB+j);
				SESSIONMAP.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.162 Safari/537.36");
				
				String phtml = NetUtil.readbyUnit(url+WB+j,SESSIONMAP);
				
				Pattern p = Pattern.compile("<img id=\"manga\" src=\"([^\"]+)\" alt=\"[^\"]+\"/>");
				
				Matcher m = p.matcher(phtml);
				
				if(m.find()) {
					String imgurl = m.group(1);
					String filename = FILE_PATH+fileflag+"\\"+NetUtil.getName(count,3)+".jpg";
					NetUtil.readAndWrite(imgurl, filename,SESSIONMAP);
					
					count++;
				}
				
				try {
					Thread.sleep(2000+getRandomInt()+getRandomInt());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			ZipCompressor zc = new ZipCompressor(FILE_PATH+"finish/"+MH_NAME+"_"+NetUtil.getName(fileflag,4)+".zip");     
	        zc.compress(FILE_PATH+fileflag+"/");
	
	        DounUtil.delete(FILE_PATH+fileflag);

		}
	
		
	}

	private static int getRandomInt() {
		
		Random ran = new Random();
		
		return 100+ran.nextInt(1000);
		
	}
	
	private static int getPageEnd(String html) {
		
		int res = 0;
		
		Pattern p = Pattern.compile("<a href=\"[^\"]+\">([^<]+?)</a>");
		
		Matcher m = p.matcher(html);
		
		while(m.find()) {
			if(m.group().contains("?p=")) {
				res = Integer.parseInt(m.group(1).replace("\r\n", "").trim());
			}
		}
		return res;
	}

}
