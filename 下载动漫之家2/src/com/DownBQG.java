package com;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.NetUtil;

/**
 * 本类用来下载 笔趣阁  的文章
 * @author k
 *
 */
public class DownBQG {

	private static final Map<String,String> map = new HashMap<String,String>();
	
	private static final String BASE_URL = "http://www.biquge.cm";
	
	private static final String TXT_URL = "http://www.biquge.cm/8/8507/";
	
	private static final String FILE_NAME = "e:/马前卒.txt";
	
	public static void main(String[] args) {
		
		NetUtil.initAuth();
		
		map.put("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
		
		List<String> mululist = new ArrayList<String>();
		
		getMulu(mululist);
		
		int count = 1;
		for(String url:mululist) {
			String html = NetUtil.read(url,map,"gbk").toString();
			
			String tdv = getTargetDIV(html);
			
			write2File(tdv);
			
			System.out.println("完成第"+count+"章");
			
			count++;
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private static void write2File(String tdv) {
		
		File f = new File(FILE_NAME);
		
		try {
			FileOutputStream fos = new FileOutputStream(f,true);
			fos.write(tdv.getBytes());
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private static String getTargetDIV(String html) {
		StringBuilder sb = new StringBuilder();
		
		String[] ss = html.split("\r\n");
		
		boolean b = false;
		
		for(String s:ss) {
			if(s.contains("<div")&&s.contains("id=\"content\"")) {
				b = true;
			}
			if(b) {
				sb.append(s+"\r\n");
			}
			if(b&&s.contains("</div")) {
				break;
			}
		}
		String res = sb.toString();
		res = res.replace("<div id=\"content\">", "").replace("&nbsp;&nbsp;&nbsp;&nbsp;", "  ").replace("<br />", "")
				.replace("找本站搜索\"笔趣阁CM\" 或输入网址:www.biquge.cm</div>", "");
				
		return res;
	}

	private static void getMulu(List<String> mululist) {
		
		String html = NetUtil.read(TXT_URL,map,"gbk").toString();
		
		Pattern p = Pattern.compile("<dd><a href=\"([^\"]+?)\">([^<]+?)</a></dd>");
		
		Matcher m = p.matcher(html);
		
		while(m.find()) {
			mululist.add(BASE_URL+m.group(1));
		}
	}
	
}
