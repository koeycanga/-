package com.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.NetUtil;

public class DownDM5 {

	public static void main(String[] args) {
		
		String url = "http://www.dm5.com/m161694/";
		
		NetUtil.initAuth();
		
		String ahtml = NetUtil.read(url).toString();
		
		String key = getKey(ahtml);
		
		Pattern p =  Pattern.compile("<script type=\"text/javascript\">(.*?)</script>");
		
		Matcher m = p.matcher(ahtml);
		
		StringBuilder sb = new StringBuilder();
		
		while(m.find()) {
			String str = m.group(1);
			if(str.contains("DM5_COOKIEDOMAIN")) {
				sb.append(m.group(1)+"\r\n");
			}
		}
		
		String res = "key="+key+getTargetAttr(sb.toString(),"var\\s*DM5_CID\\s*=([^;]+)");
		
		res+= getTargetAttr(sb.toString(),"var\\s*DM5_VIEWSIGN_DT\\s*=([^;]+)");
		
		res+= getTargetAttr(sb.toString(),"var\\s*DM5_VIEWSIGN\\s*=([^;]+)");
		
		File f = new File("e:/test.txt");
		
		try {
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(ahtml.getBytes());
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	
    private static String getKey(String ahtml) {
		Pattern p = Pattern.compile("<input type=\"hidden\" id=\"dm5_key\" value=\"([^\"]*)\" /> ");
		
		Matcher m = p.matcher(ahtml);
		
		if(m.find()) {
			System.out.println("ур╣╫ак");
			return m.group(1);
		}
		
		return "";
	}


	//var\\s*DM5_CID\\s*=([^;]+);
	private static String getTargetAttr(String html,String regx) {
        String res = "";
		
		Pattern p1 =  Pattern.compile(regx);
		
		Matcher m1 = p1.matcher(html);
		
		if(m1.find()) {
			res = m1.group(1);
		}
		return res;
	}
	
}
