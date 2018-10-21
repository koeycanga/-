package com;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.CutImage;
import utils.DounUtil;
import utils.NetUtil;

/**
 * e 本类用来下载结界师
 * @author k
 *
 */
public class DownJJS {

	private static final String FOLDER = "D:/output/漫画/结界师/";
	
	private static final String BASEURL = "https://manhua.dmzj.com";
	
	private static final String WB = "#@page=1";
	
	private static final Map<String,String> map = new HashMap<String,String>();
	
	public static void main(String[] args) {
		
		NetUtil.initAuth();
		
		initMap();
		
		List<String> mulist = new ArrayList<String>();
		
		getMuLuList(mulist);
		
		for(int i=0;i<mulist.size();i++) {
			
			int fileflag = i+1;
			
			if(fileflag<=30) {
				continue;
			}
			
			String html = NetUtil.readbyUnit(mulist.get(i), map);
			
			Pattern mp = Pattern.compile("<option\\s*value=\"(.*?)\"[^>]*>");
			
			Matcher mm = mp.matcher(html);
			
			int count = 1;
			
			while(mm.find()) {
				
				System.out.println(mm.group(1));
				
				String url = "https:"+mm.group(1);
				
				if(url.contains("images")) {
				
					NetUtil.readAndWritebyUnit(url, count, fileflag, "jpg", FOLDER,map);
					
					count++;
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}
			
			File cutf = new File(FOLDER+fileflag);
			File[] cff = cutf.listFiles();
			
			int realcount = 1;
			CutImage cui = new CutImage();
			for(int j=0;j<cff.length;j++){
				try {
					realcount = cui.cut(cff[j], FOLDER+fileflag, "png",realcount,1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			DounUtil.deleteTemp(FOLDER+fileflag);
			
			DounUtil.coressImg(FOLDER+fileflag+"/");
		
			DounUtil.deleteMapImg(FOLDER+fileflag+"/");
			
			ZipCompressor zc = new ZipCompressor(FOLDER+"finish/mwdtz_"+NetUtil.getName(fileflag,2)+".zip");     
	        zc.compress(FOLDER+fileflag+"/");
	        System.out.println("下载完"+fileflag+"话");
	        DounUtil.delete(FOLDER+fileflag);
		}
		
	}

	private static void getMuLuList(List<String> mulist) {
		String html = NetUtil.readbyUnit("https://manhua.dmzj.com/jjs/", map);
		
		Pattern p = Pattern.compile("<a title=\"([^\"]+)\" href=\"([^\"]+)\"\\s*[^>]*>([^<]+)</a>");
		
		Matcher m = p.matcher(html);
		
		while(m.find()) {
			if(m.group(3).contains("卷")) {
				mulist.add(BASEURL+m.group(2)+WB);
			}
		}
	}
	
	private static void initMap() {
		
		map.put("referer", "https://manhua.dmzj.com/jjs/");
		map.put("cookie", "cna=hVegC3UFJlkCAdpqdazFcOfS; t=0bc28150dc6f607156cd514a6eab52ee; afpCT=1");
		map.put("intervention", "<https://www.chromestatus.com/feature/5718547946799104>; level=\"warning\"");
		map.put("user-agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.162 Safari/537.36");
		
	}

}
