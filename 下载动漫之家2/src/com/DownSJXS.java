package com;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.NetUtil;

/**
 * 本类用来下载 手机小说下载网 的文章
 * @author k
 *
 */
public class DownSJXS {

	private static final String MULU = "http://www.sjtxt.la/book/7357/";
	
	private static final String FILENAME = "E:/绝对权力.txt";
	
	public static void main(String[] args) {
		
		NetUtil.initAuth();
		
		List<DLAMBean> list = new ArrayList<DLAMBean>();
		
		dealMULU(list);
		
		for(DLAMBean db:list) {
			String html = NetUtil.read(db.getUrl()).toString();
			
			String[] ss = html.split("\r\n");
			
			StringBuilder sb = new StringBuilder();
			
			boolean b = false;
			
			for(String s:ss) {
				if(s.contains("<div")&&s.contains("content1")) {
					b = true;
					continue;
				}
				if(s.contains("</div>")&&b) {
					break;
				}
				if(b) {
					sb.append(s);
				}
			}

			String nr = sb.toString().replace("<br />", "\r\n").replace("&nbsp;", " ");
			
			File f = new File(FILENAME);
			
			try {
				FileOutputStream fos = new FileOutputStream(f,true);
				fos.write(nr.getBytes());
				fos.flush();
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private static void dealMULU(List<DLAMBean> list) {
		String html = NetUtil.read(MULU).toString();
		
		Pattern p = Pattern.compile("<li><a href=\"([^\"]+?)\">([^<]+?)</a></li>");
		
		Matcher m = p.matcher(html);
		
		boolean b = false;
		
		while(m.find()) {
			if(m.group(2).contains("卷一")) {
				b = true;
			}
			if(b) {
				DLAMBean db = new DLAMBean();
				db.setUrl(MULU+m.group(1));
				list.add(db);
			}
		}
	}
	
}
