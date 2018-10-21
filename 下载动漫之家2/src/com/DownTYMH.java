package com;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.NetUtil;

/**
 * 本类用来下载天涯的漫画类帖子
 * @author k
 *
 */
public class DownTYMH {

	private static final String URL = "http://bbs.tianya.cn/";
	
	private static final String first_url = "post-develop-915308-";
	
	private static final String FOLDER = "D:\\output\\漫画\\天涯\\经济漫画\\";
	
	private static final Map<String,String> map = new HashMap<String,String>();
	
	public static void main(String[] args) {
		
		NetUtil.initAuth();
		
		int ys = getYS();
		int count = 1;
		
		for(int i=1;i<=ys;i++) {
			String t_url = URL+first_url+i+".shtml";
			
			String html = NetUtil.readbyUnit(t_url);
			
			initMap(t_url);
			
			List<String> target_dv = getDIV(html) ;
			
			for(String s:target_dv) {
				
				Pattern p = Pattern.compile("<img src=\"([^\"]+?)\" original=\"([^\"]+?)\"/>");
				Matcher m = p.matcher(s);
				while(m.find()) {
					String mhsrc = m.group(2);
					NetUtil.readAndWrite(mhsrc, count, "抢钱的境界", "jpg", FOLDER,map);
					count++;
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			//break;
		}
	}

	private static void initMap(String t_url) {
		map.put("Accept", "image/webp,image/*,*/*;q=0.8");
		map.put("Referer", t_url);
		map.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");
	}

	private static List<String> getDIV(String html) {
		
		List<String> list = new ArrayList<String>();
		
		String[] ss = html.split("\r\n");
		
		StringBuilder sb = new StringBuilder();
		
		int count = 0;
		boolean b = false;
		
		for(String s:ss) {
			if(s.contains("<div class=\"atl-item\"")) {
				b = true;
			}
			if(b&&s.contains("<div")) {
				count++;
			}
			if(b) {
				sb.append(s);
			}
			if(b&&s.contains("</div")) {
				count--;
				if(b&&count==0) {
					b = false;
					String str = sb.toString();
					if(str.contains("<strong class=\"host\">")&&str.contains("楼主")) {
						list.add(sb.toString());
					}
					sb = new StringBuilder();
				}
			}
		}
		return list;
	}

	private static int getYS() {
		String a_url = URL+first_url+"1.shtml";
		
		String html = NetUtil.read(a_url).toString();
		
		Pattern p = Pattern.compile("<a href=\"/([^\"]+?)\">([0-9]+?)</a>");
		
		Matcher m = p.matcher(html);
		
		int ys = 0;
		
		while(m.find()) {
			ys = Integer.parseInt(m.group(2));
		}
		
		return ys;
	}
	
}
