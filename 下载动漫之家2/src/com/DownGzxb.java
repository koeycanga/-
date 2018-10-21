package com;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import utils.DounUtil;
import utils.NetUtil;

public class DownGzxb {

	//+"index.html?p=1"
	
	private static final String BaseURL = "http://www.omanhua.com";
	
	private static final String FILEPATH = "D:/output/漫画/工作细胞/";
	
	private static final Map<String,String> map = new HashMap<String,String>();
	
	public static void main(String[] args) {
	
		NetUtil.initAuth();
		
		List<String> mulist = getMulist();
		
		for(int i=0;i<mulist.size();i++) {
			
			if(i!=5) {
				continue;
			}
			
			int fileflag = i+1;
			
			File f = new File(FILEPATH+fileflag);
			
			if(!f.exists()) {
				f.mkdir();
			}
			
			String url = mulist.get(i);
			
			String html = NetUtil.readbyUnit(url);
			
			int end = getEnd(html);
			
			for(int j=1;j<=end;j++) {
				
				String mhhtml = NetUtil.readbyUnit(url+"index.html?p="+j);
				
				// <img alt="工作细胞 18话" id="mangaFile" src="http://pic.fxdm.cc/tu/undefined/工作细胞/18话/01.jpg" class="mangaFile" data-tag="mangaFile" imgw="1256" data-bd-imgshare-binded="1"/>
				
				Pattern p = Pattern.compile(" <img alt=\"([^\"]+?)\" id=\"mangaFile\" src=\"([^\"]+?)\" class=\"mangaFile\" data-tag=\"mangaFile\" imgw=\"[^\"]+\" data-bd-imgshare-binded=\"1\"/>");
				
				Matcher m = p.matcher(mhhtml);
				
				if(m.find()) {
					
					initmap(url+"index.html?p="+j);
					
					String fname = FILEPATH+fileflag+"/"+NetUtil.getName(j,3)+".jpg";
					
					NetUtil.readAndWritebyUnit(m.group(2), fname, map);
				}
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			ZipCompressor zc = new ZipCompressor(FILEPATH+"finish/gzxb_"+NetUtil.getName(fileflag,4)+".zip");     
	        zc.compress(FILEPATH+fileflag+"/");

	        DounUtil.delete(FILEPATH+fileflag);
		}
	}

	
	private static void initmap(String string) {
		map.put("Accept", "image/webp,image/*,*/*;q=0.8");
		map.put("Accept-Encoding", "gzip, deflate, sdch");
		map.put("Accept-Language", "zh-CN,zh;q=0.8");
		map.put("Host", "pic.fxdm.cc");
		map.put("Proxy-Connection", "keep-alive");
		map.put("Referer", string);
		map.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
	}


	private static int getEnd(String html) {
		
		String res = "0";
		
		Pattern p = Pattern.compile("<option value=\"([^\"]+?)\">[^<]+?</option>");
		
		Matcher m = p.matcher(html);
		
		while(m.find()) {
			res = m.group(1);
		}
		
		return Integer.parseInt(res);
	}


	private static List<String> getMulist(){
		List<String> mulist = new ArrayList<String>();
		
		String html = NetUtil.readbyUnit("http://www.omanhua.com/comic/14311/");
		
		String dv = NetUtil.getTargetDV(html, "class=\"subBookList\"");
		
		Pattern  p = Pattern.compile("<a href=\"([^\"]+?)\" title=\"[^\"]+\" target=\"_blank\"\\s*(class=\"new\")*>[^<]+?</a>");
		
		Matcher m = p.matcher(dv);
		
		while(m.find()) {
			mulist.add(BaseURL+m.group(1));
		}
		
		Collections.reverse(mulist);
		
		return mulist;
	}
}
