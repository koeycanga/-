package com;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.NetUtil;

/**
 * 本类用来下在表情包网的表情图片
 * @author k
 *
 */
public class DownBQB {

	private static final String BASEURL = "http://qq.yh31.com";
	
	private static final String FOLDER = "d:/output/表情包/";
	
	private static final Map<String,String> map = new HashMap<String,String>();
	
	private static final Map<String,String> src_map = new HashMap<String,String>();
	
	public static void main(String[] args) {
		NetUtil.initAuth();
		
		for(int i=1;i<=19;i++) {
			String src = "http://qq.yh31.com/zjbq/List_"+i+".html";
			String html = NetUtil.read(src).toString();
			
			Pattern p = Pattern.compile("<a href=\"([^\"]+?)\" target=\"_blank\">([^<]+?)</a>");
			
			Matcher m = p.matcher(html);
			
			while(m.find()) {
				String img_src = BASEURL+m.group(1);
				if(img_src.contains("/zjbq")) {
					String foldername = m.group(2);
					src_map.put(img_src, foldername);
				}
			}
		}
		
		for(String key:src_map.keySet()) {
			String foldername = FOLDER+src_map.get(key).replace(".", "")+"/";
			
			File f = new File(foldername);
			
			if(!f.exists()) {
				f.mkdirs();
			}
			
			int count = 1;
			
			dealDownBQ(key,foldername,count);
		}
		
		/*String src = "http://qq.yh31.com/zjbq/1020669.html";
		
		int count = 3259;
		
		dealDownBQ(src,count);*/
		
 	}
	
	
	private static void dealDownBQ(String src,String folder,int count) {
		String html = NetUtil.read(src).toString();
		
		//System.out.println(html);
		
		//<img src="/tp/zjbq/201710172231086079.gif" alt="">
		Pattern p = Pattern.compile("<img(.*?)src=\"([^\\.]+?)\\.(jpg|png|gif)\"[^>]*>"); //Pattern.compile("<img\\s*(alt=\"\")*src=\"([^\\.]+?)\\.(jpg|png|gif)\"\\s*(alt=\"\")*\\s*/>");
		
		Matcher m = p.matcher(html);
		
		while(m.find()) {
			
			String url = BASEURL+m.group(2)+"."+m.group(3);
			System.out.println(url);
			if(!url.contains("ontop3")) {
				String fname = folder+NetUtil.getName(count,5)+"."+m.group(3);
				
				NetUtil.readAndWrite(url, fname);
				
				count++;
			
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		String next_src = getNextPageSrc(html);
		if(!"".equals(next_src)&&map.get(next_src)==null) {
			map.put(next_src, next_src);
			dealDownBQ(next_src,folder,count);
		}
	}
	
	private static void dealDownBQ(String src,int count) {
		String html = NetUtil.read(src).toString();
		
		//System.out.println(html);
		
		//<img src="/tp/zjbq/201710172231086079.gif" alt="">
		Pattern p = Pattern.compile("<img\\s*(alt=\"\")*src=\"([^\\.]+?)\\.(jpg|png|gif)\"\\s*(alt=\"\")*\\s*/>");
		
		Matcher m = p.matcher(html);
		
		while(m.find()) {
			
			String url = BASEURL+m.group(2)+"."+m.group(3);
			System.out.println(url);
			
			String fname = FOLDER+NetUtil.getName(count,5)+"."+m.group(3);
			
			NetUtil.readAndWrite(url, fname);
			
			count++;
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		String next_src = getNextPageSrc(html);
		if(!"".equals(next_src)&&map.get(next_src)==null) {
			map.put(next_src, next_src);
			dealDownBQ(next_src,count);
		}
	}

	private static void wirterTofile(String html) {
		File f = new File("e:/test.txt");
		
		try {
			FileOutputStream fos = new FileOutputStream(f,true);
			fos.write(html.getBytes());
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String getNextPageSrc(String html) {
		
		String res = "";
		
		String[] ss = html.split("\r\n");
		
		for(String s:ss) {
			if(s.contains("下一页")) {
				
				Pattern p = Pattern.compile("<a\\s.*?href=\"([^\"]+)\"[^>]*>");
				
				Matcher m = p.matcher(s);
				
				if(m.find()) {
					res = BASEURL + m.group(1);
				}
				break;
			}
		}
		return res;
	}

}
