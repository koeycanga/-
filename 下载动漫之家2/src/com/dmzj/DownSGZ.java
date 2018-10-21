package com.dmzj;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ZipCompressor;

import utils.DounUtil;
import utils.NetUtil;

/**
 * 本类用来下载动漫之家的圣歌传
 * @author k
 *
 */
public class DownSGZ {

	private static final String FOLDER = "D:/output/漫画/圣歌传/";
	
	private static final String BASEURL = "https://manhua.dmzj.com";
	
	private static final Map<String,String> map = new HashMap<String,String>();
	
	public static void main(String[] args) {
		
		NetUtil.initAuth();
		
		map.put("Referer", "https://manhua.dmzj.com/shenggezhuan");
		map.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.162 Safari/537.36");
		
		
		//<a title="圣☆哥传-第01-02话" href="/shenggezhuan/6376.shtml">第01-02话</a>
		
		List<String> mulist = getMulist();
		
		for(int i=0;i<mulist.size();i++) {
			
			int fileflag = i+1;
			
			File f = new File(FOLDER+fileflag);
			if(!f.exists()) {
				f.mkdir();
			}
			
			String url = mulist.get(i);
			
			String str = NetUtil.readbyUnit(url, map);// readbyUnit(address);
			
		 	String t_utl = "https:";
		 	
			Pattern p = Pattern.compile("<option\\s*value=\"(//images.dmzj.com/.*?)\"[^>]*>");
			
			Matcher m = p.matcher(str);
			
			int count = 1;
			while(m.find()) {
				String address = t_utl+m.group(1);
				
				String fname = FOLDER+fileflag+"/"+NetUtil.getName(count, 3)+".jpg";
				
				NetUtil.readAndWritebyUnit(address, fname, map);
				
				count++;
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			ZipCompressor zc = new ZipCompressor(FOLDER+"finish/sgz_"+NetUtil.getName(fileflag,4)+".zip");     
	        zc.compress(FOLDER+fileflag+"/");

	        DounUtil.delete(FOLDER+fileflag);
			
	
		}
		
	}
	
	
	private static List<String> getMulist(){
		List<String> mulist = new ArrayList<String>();
		
		String html = NetUtil.readbyUnit("https://manhua.dmzj.com/shenggezhuan",map);
		
		Pattern p = Pattern.compile("<a title=\"[^\"]+\" href=\"([^\"]+)\"\\s*(class=\"color_red\")*>[^<]+</a>");
		
		Matcher m = p.matcher(html);
		
		while(m.find()) {
			if(m.group().contains("话")) {
				mulist.add(BASEURL+m.group(1));
			}
		}
		
		return mulist;
	}

}
