package com;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.DounUtil;
import utils.NetUtil;

/**
 * 本类用来下载布卡漫画
 * @author k
 *
 */
public class DownBuKa {

	private static final Map<String,String> SESSIONMAP = new HashMap<String,String>();
	
	private static final String FILE_PATH = "D:\\output\\漫画\\末世凡人\\";
	
	private static final String BASE_URL  = "http://www.buka.cn/view/221518/"; 
	
	private static final String MH_NAME = "msfr";
	
	public static void main(String[] args) {
		NetUtil.initAuth();
		
	    //65537   65560	
		
		int fileflag = 1;
		
		for(int i=65537;i<=65580;i++) {
		
			File file = new File(FILE_PATH+fileflag+"\\");
			if(!file.exists()) {
				file.mkdir();
			}
			
			String ahtml = NetUtil.readbyUnit(BASE_URL+i+".html");
			
			String rex = "<img src=\"([^\"]+)\"\\s*( class=\"lazy\" data-original=\"([^\"]+)\" )*height=\"100%;\" alt=\"\"/>";
			
			Pattern p = Pattern.compile(rex);
			
			Matcher m = p.matcher(ahtml);
			
			int nc = 0;
			int count = 1;
			while(m.find()) {
				String url = "";
				if(nc==0) {
					url = m.group(1);
					nc++;
				}else {
					url = m.group(3);
				}
				
				String fname = FILE_PATH+fileflag+"//"+NetUtil.getName(count,4)+".jpg";
				
				NetUtil.readAndWrite(url, fname,SESSIONMAP);
				
				count++;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			ZipCompressor zc = new ZipCompressor(FILE_PATH+"finish/"+MH_NAME+"_"+NetUtil.getName(fileflag,4)+".zip");     
	        zc.compress(FILE_PATH+fileflag+"/");
	
	        DounUtil.delete(FILE_PATH+fileflag);
		
	        System.out.println("下载完第"+fileflag+"话");
	        
	        fileflag++;
		}
	}

}
