package com;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.NetUtil;

/**
 * �����������ض���ѧ���������ľ���
 * @author k
 *
 */

public class DownYMYJ {

	private static final String BASEURL = "https://www.duanwenxue.com/yuju/youmei/list_";
	
	private static final String FOLDER = "E:/��������.txt";
	
	public static void main(String[] args) {
		
		NetUtil.initAuth();
		
		for(int i=1;i<=406;i++) {
		
			String url = BASEURL+i+".html";
			
			String html = NetUtil.readbyUnit(url);
			
			Pattern p = Pattern.compile("</strong>[^<]*<a target=\"_blank\" href=\"([^\"]+?)\">([^<]+?)</a>");
			
			Matcher m = p.matcher(html);
			
			while(m.find()) {
				String str = m.group(2).replace("\\?", "").trim()+"\r\n\r\n\r\n\r\n\r\n\r\n";
				
				File f = new File(FOLDER);
				
				try {
					FileOutputStream fos = new FileOutputStream(f,true);
					fos.write(str.getBytes());
					fos.flush();
					fos.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
