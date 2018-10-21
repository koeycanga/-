package com;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.CutImage;
import utils.DounUtil;
import utils.NetUtil;

public class DownKuKu {

	private static final Map<String,String> SESSIONMAP = new HashMap<String,String>();
	
	private static final String FILE_PATH = "D:\\output\\漫画\\金田一\\";
	
	private static final String BASE_URL  = "http://comic2.kukudm.com/comiclist/892"; 
	
	private static final String THE_BASE_URL  = "http://comic2.kukudm.com";
	
	private static final String MH_NAME = "jty_ten_six";
	
	public static void main(String[] args) {
		
		NetUtil.initAuth();
		
		List<String> list = new ArrayList<String>();
		
		getMuList(list);
		
		for(int j=0;j<list.size();j++) {
			
			if(j<153) {
				continue;
			}
			
			int fileflag = j+1;
			
			String aurl = list.get(j);
			
			File f = new File(FILE_PATH+fileflag);
			if(!f.exists()||!f.isDirectory()) {
				f.mkdir();
			}
			
			int end = getEnd(aurl);	
			
			int hs = getHs(aurl);
			
			for(int i=1;i<=end;i++) {
				
				String url = BASE_URL+"/"+hs+"/"+i+".htm";
				
				SESSIONMAP.put("Referer",url);
				SESSIONMAP.put("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.162 Safari/537.36");
				SESSIONMAP.put("Upgrade-Insecure-Requests", "1");
				SESSIONMAP.put("Cookie", "__cfduid=d9b100e8363db1fc858efdb7f1da98cff1527153330");
				SESSIONMAP.put("DNT", "1");
				
				String fname = FILE_PATH+fileflag+"\\"+ NetUtil.getName(i,4)+".jpg";
				
				File ff = new File(fname);
				if(!ff.exists()) {
					String html = NetUtil.readbyUnit(url,SESSIONMAP,true);
					
					Pattern p = Pattern.compile("<img src=\"([^\"]+)\"[^>]*>");
					
					Matcher m = p.matcher(html);
					
					if(m.find()) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						String imgurl = m.group(1).replace(" ", "%20");

						NetUtil.readAndWrite(imgurl, fname,SESSIONMAP);
					}
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			File cutf = new File(FILE_PATH+fileflag);
			File[] cff = cutf.listFiles();
			
			int realcount = 1;
			CutImage cui = new CutImage();
			for(int i=0;i<cff.length;i++){
				try {
					realcount = cui.cut_auto(cff[i], FILE_PATH+fileflag, "png",realcount);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			DounUtil.deleteTemp(FILE_PATH+fileflag);
			
			DounUtil.coressImg(FILE_PATH+fileflag+"/",0.8f);
			
			DounUtil.deleteMapImg(FILE_PATH+fileflag);
			
			ZipCompressor zc = new ZipCompressor(FILE_PATH+"finish/"+MH_NAME+"_"+NetUtil.getName(fileflag,3)+".zip");     
	        zc.compress(FILE_PATH+fileflag+"/");
	
	        DounUtil.delete(FILE_PATH+fileflag);
	        
	        fileflag++;
		}
		
		//startDown();
	}
	
	
	private static int getHs(String aurl) {
		String[] ss = aurl.split("/");
		return Integer.parseInt(ss[5]);
	}


	private static void getMuList(List<String> list) {
		
		File ff = new File("e:/jtyml.txt");
		if(ff.exists()) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(ff));
				String line = null;
				while((line=br.readLine())!=null) {
					if(!line.equals("")) {
						list.add(line);
					}
				}
				br.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			
			String html = NetUtil.read(BASE_URL).toString();
			
			//<A href='http://comic.kukudm.com/comiclist/892/50383/1.htm' target='_blank'>??</A>
			Pattern p = Pattern.compile("<[aA] href='([^']+)' target='_blank'>[^<]+</[aA]>");
			
			Matcher m = p.matcher(html);
			
			while(m.find()) {
				String url = m.group(1);
				if(!url.contains("http")) {
					list.add(THE_BASE_URL+url);
				}
			}
			
			File f = new File("e:/jtyml.txt");
			try {
				FileOutputStream fos = new FileOutputStream(f);
				for(String url:list) {
					fos.write((url+"\r\n").getBytes());
					fos.flush();
				}
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static  int getEnd(String url) {
		
		int res = 0;
		
		String html = NetUtil.readbyUnit(url,false);
		
		Pattern p = Pattern.compile("共([0-9]+)页");
	
		Matcher m = p.matcher(html);
		
		if(m.find()) {
			res = Integer.parseInt(m.group(1));
		}
		
		return res;
	}

	
	public static void startDown() {
		//new DTKUKU(15856,15860,15861).start();
		
		new DTKUKU(15686,15687,15872).start();
	}
}


class DTKUKU extends Thread{
	

	private static final Map<String,String> SESSIONMAP = new HashMap<String,String>();
	
	private static final String FILE_PATH = "D:\\output\\漫画\\金田一\\";
	
	private static final String BASE_URL  = "http://comic.kukudm.com/comiclist/862/"; 
	
	private static final String MH_NAME = "jty_dp";
	
	private int o_x;
	private int start;
	private int stop;
	
	public DTKUKU(int o_x,int start,int stop) {
		this.o_x = o_x;
		this.start = start;
		this.stop = stop;
	}
	
	public void run() {
		
		for(int j=start;j<=stop;j++) {
			
			int fileflag = j - o_x;
			
			File f = new File(FILE_PATH+fileflag);
			if(!f.exists()||!f.isDirectory()) {
				f.mkdir();
			}
			
			int hs = j;
			
			int end = getEnd(j);	
			
			for(int i=1;i<=end;i++) {
				
				String url = BASE_URL+hs+"/"+i+".htm";
				
				SESSIONMAP.put("Referer",url);
				SESSIONMAP.put("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.162 Safari/537.36");
				SESSIONMAP.put("Upgrade-Insecure-Requests", "1");
				
				String fname = FILE_PATH+fileflag+"\\"+ NetUtil.getName(i,4)+".jpg";
				
				File ff = new File(fname);
				if(!ff.exists()) {
					String html = NetUtil.readbyUnit(url,SESSIONMAP);
					
					Pattern p = Pattern.compile("<img src=\"([^\"]+)\"[^>]*>");
					
					Matcher m = p.matcher(html);
					
					if(m.find()) {
						String imgurl = m.group(1);

						NetUtil.readAndWrite(imgurl, fname,SESSIONMAP);
					}
				}
			}
			
			File cutf = new File(FILE_PATH+fileflag);
			File[] cff = cutf.listFiles();
			
			int realcount = 1;
			CutImage cui = new CutImage();
			for(int i=0;i<cff.length;i++){
				try {
					realcount = cui.cut_auto(cff[i], FILE_PATH+fileflag, "png",realcount);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			DounUtil.deleteTemp(FILE_PATH+fileflag);
			
			DounUtil.coressImg(FILE_PATH+fileflag+"/",0.8f);
			
			DounUtil.deleteMapImg(FILE_PATH+fileflag);
			
			ZipCompressor zc = new ZipCompressor(FILE_PATH+"finish/"+MH_NAME+"_"+NetUtil.getName(fileflag,3)+".zip");     
	        zc.compress(FILE_PATH+fileflag+"/");
	
	        DounUtil.delete(FILE_PATH+fileflag);
	        
	        fileflag++;
		}
	}
	
	private  int getEnd(int hs) {
		
		int res = 0;
		
		String url = BASE_URL+hs+"/1.htm";
		
		String html = NetUtil.readbyUnit(url);
		
		Pattern p = Pattern.compile("共([0-9]+)页");
	
		Matcher m = p.matcher(html);
		
		if(m.find()) {
			res = Integer.parseInt(m.group(1));
		}
		return res;
	}
	
}
