package com;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.CutImage;
import utils.DounUtil;
import utils.NetUtil;

public class DownGGMH {

	
	
	public static void main(String[] args) {
		DownGGMH dg = new DownGGMH();
		dg.startDownLoad();
	}

	
	private void initMap(String url) {
		
		//SESSIONMAP.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		/*SESSIONMAP.put("Accept-Encoding", "gzip, deflate, sdch");
		SESSIONMAP.put("Accept-Language", "zh-CN,zh;q=0.8");
	    SESSIONMAP.put("Cookie", "__jsluid=4f351145c33ad134654d05f3903d19ec");
		SESSIONMAP.put("Host", "img.tu.pgu.cc");
		SESSIONMAP.put("Proxy-Connection", "keep-alive");
		SESSIONMAP.put("Upgrade-Insecure-Requests", "1");
		SESSIONMAP.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
	   */
	}


	
	
	public void startDownLoad() {
		new DTGGMH(244282,244292,244298).start();
		
	}
	
}

class DTGGMH extends Thread{
	
	private static final Map<String,String> SESSIONMAP = new HashMap<String,String>();
	
	private static final String FILE_PATH = "D:\\output\\漫画\\美食侦探王\\";
	
	private static final String BASE_URL  = "http://www.gugu5.com/n/3661/"; 
	
	private static final String WB = "?p=";
	
	private static final String MH_NAME = "msztw";
	
	private int o_x;
	private int start;
	private int stop;
	
	public DTGGMH(int o_x,int start,int stop) {
		this.o_x = o_x;
		this.start = start;
		this.stop = stop;
	}
	
	private int getEnd(String url) {
		
		int res = 0;
		
		String html = NetUtil.readbyUnit(url);
		
		Pattern p = Pattern.compile("<option value=\"([^\"]+)\">([^<]+)</option>");

		Matcher m = p.matcher(html);
		
		while(m.find()) {
			res = Integer.parseInt(m.group(1));
		}
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return res;
		
	}
	
	public void run() {
		for(int i=start;i<=stop;i++) {
			int fileflag = i - o_x ;
			File file = new File(FILE_PATH+fileflag);
			if(!file.exists()||!file.isDirectory()) {
				file.mkdir();
			}
			
			String url = BASE_URL+i+".html";
			
			int end = getEnd(url);
			
			for(int j=1;j<=end;j++) {
				
				String imgurl = url+WB+j;
				
				String html = NetUtil.readbyUnit(imgurl);
				
				Pattern p = Pattern.compile("<img\\s*src=\"([^\"]+)\"[^>]+name=\"qTcms_pic\"[^>]+>");
				
				Matcher m = p.matcher(html);
				
				if(m.find()) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}	
					String iurl = m.group(1);
					System.out.println(iurl);
					String fname = FILE_PATH+fileflag+"\\"+NetUtil.getName(j,4)+".jpg";
					NetUtil.readAndWritebyUnit(iurl,fname,SESSIONMAP);
				}
				
			}
			
			
			File cutf = new File(FILE_PATH+fileflag);
			File[] cff = cutf.listFiles();
			
			int realcount = 1;
			CutImage cui = new CutImage();
			for(int n=0;n<cff.length;n++){
				try {
					realcount = cui.cut_auto(cff[n], FILE_PATH+fileflag, "png",realcount);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			DounUtil.deleteTemp(FILE_PATH+fileflag);
			
			DounUtil.coressImg(FILE_PATH+fileflag+"/",0.8f);
			
			DounUtil.deleteMapImg(FILE_PATH+fileflag);
			
			ZipCompressor zc = new ZipCompressor(FILE_PATH+"finish/"+MH_NAME+"_"+NetUtil.getName(fileflag,3)+".zip");     
	        zc.compress(FILE_PATH+fileflag+"/");
	        System.out.println("下载完"+fileflag+"话  ");

	        DounUtil.delete(FILE_PATH+fileflag);
			
		}
	}
	
}
