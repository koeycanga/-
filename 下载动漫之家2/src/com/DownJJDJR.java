package com;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.DounUtil;
import utils.NetUtil;

/**
 * 本类用来下载风之动漫  进击的巨人漫画
 * @author k
 *
 */
public class DownJJDJR {

	private static final String BASE_URL = "http://manhua.fzdm.com/117/";
	
	private static final String FOLDER = "D:/output/东京食尸鬼/";
	
	private static final String IMAGE_URL = "http://183.91.33.78/p1.xiaoshidi.net/";
	
	public static void main(String[] args) {
		
		NetUtil.initAuth();
		
		List<DLAMBean> list = new ArrayList<DLAMBean>();
		
		dealMUList(list);
		
		for(DLAMBean db:list) {
		  
			File f = new File(FOLDER+"finish/"+db.getMh_name()+".zip");
			if(!f.exists()) {
				int count = 1;
				
				DownAndWiite2(db.getUrl(),db.getMh_name(),db.getUrl(),count);
				
				DounUtil.coressImg_CM(FOLDER+db.getMh_name()+"/");
				
				DounUtil.deleteTemp(FOLDER+db.getMh_name());
				
				ZipCompressor zc = new ZipCompressor(FOLDER+"finish/"+db.getMh_name()+".zip");     
		        zc.compress(FOLDER+db.getMh_name()+"/");
		        System.out.println("下载完一话");
		       
		        DounUtil.delete(FOLDER+db.getMh_name());
			}
		}
		
	}
	
	private static String getScriptIMGURL(String ahtml) {
		Pattern p = Pattern.compile("var\\s*mhurl\\s*=\\s*\"([^\"]+)\"");
		
		Matcher m = p.matcher(ahtml);
		
		if(m.find()) {
			return IMAGE_URL+m.group(1);
		}
		
		return "";
	}
	
	private static void DownAndWiite2(String url,String filename,String org_url,int count) {
		String ahtml = NetUtil.read(url).toString();;
		
		String imgsrc = getScriptIMGURL(ahtml);
		
		String nextsrc = getNextSrc(ahtml,org_url);//201609061113
		
		NetUtil.readAndWrite(imgsrc, count,filename , "jpg", FOLDER);
		
		count++;
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if(!"".equals(nextsrc)) {
			DownAndWiite2(nextsrc,filename,org_url,count);
		}
		
	}
	
	private static void DownAndWiite(String url,String filename,String org_url,int count) {
		String ahtml = NetUtil.readbyUnit(url);
		
		String imgsrc = getImgSrc(ahtml);
		
		String nextsrc = getNextSrc(ahtml,org_url);
		
		NetUtil.readAndWrite(imgsrc, count,filename , "jpg", FOLDER);
		
		count++;
		
		if(!"".equals(nextsrc)) {
			DownAndWiite(nextsrc,filename,org_url,count);
		}
		
	}
	
	private static String getNextSrc(String ahtml,String org_url) {
		
		String res = "";
		
		Pattern p = Pattern.compile("<a href='([^']+)'[^>]+>[^<]+</a>");
		
		Matcher m = p.matcher(ahtml);
		
		while(m.find()) {
			String af = m.group();
			if(af.contains("下一页")) {
				res = org_url+m.group(1);
				break;
			}
		}
		return res;
	}


	private static String getImgSrc(String ahtml) {
		
		String res = "";
		
		Pattern p = Pattern.compile("<img src=\"([^\"]+?)\"[^>]+>");
		
		Matcher m = p.matcher(ahtml);
		
		while(m.find()) {
			if(m.group().contains("onerror")) {
				res = m.group(1);
				break;
			}
			
		}
		return res;
	}


	private static void dealMUList(List<DLAMBean> list) {
		String html = NetUtil.read(BASE_URL).toString();
		
		Pattern p = Pattern.compile("<a href=\"([^\"]+?)\" title=\"([^\"]+?)\">([^<]+?)</a>");
		
		Matcher m = p.matcher(html);
		
		
		while(m.find()) {
			DLAMBean db = new DLAMBean();
			if(m.group(3).contains("RE")) {
				db.setUrl(BASE_URL+m.group(1));
				db.setMh_name(m.group(3));
				list.add(db);
			}
		}
		
		Collections.reverse(list);
	}

}
