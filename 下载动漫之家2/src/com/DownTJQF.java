package com;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.CutImage;
import utils.DounUtil;
import utils.NetUtil;

/**
 * 本类用来下载 特级囚犯
 * @author k
 *
 */
public class DownTJQF {
	
	public static void main(String[] args) {
		
		NetUtil.initAuth();
		
		String ahtml = NetUtil.readbyUnit("http://www.dm5.com/manhua-tejiqiufan/");
		
		String div = getTargetDIV(ahtml);
		
		//System.out.println(div);
		
		List<DLAMBean> list = dealTargetLi(div,331);
		
		List<DLAMBean> list1 = new ArrayList<DLAMBean>();
		List<DLAMBean> list2 = new ArrayList<DLAMBean>();
		
		int lg = list.size()/2;
		
		for(int i=0;i<lg;i++) {
			list1.add(list.get(i));
		}
		
		for(int i=lg;i<list.size();i++) {
			list2.add(list.get(i));
		}
		
		new DTTJQF(list1).start();
		
		new DTTJQF(list2).start();
	}
	
	
	private static List<DLAMBean>  dealTargetLi(String html,int count) {
		
		List<DLAMBean> list = new ArrayList<DLAMBean>();
		
		StringBuilder sb = new StringBuilder();
		
		String[] ss = html.split("\r\n");
		
		boolean b = false;
		
		//int count = 331;
		
		for(String s:ss) {
			if(s.contains("<li")) {
				b = true;
			}
			if(b) {
				sb.append(s+"\r\n");
			}
			if(b&&s.contains("</li")) {
				
				DLAMBean db = new DLAMBean();
				
				String res = sb.toString();
				
			    Pattern p1 = Pattern.compile("<a href=\"/m([0-9]+)/\"[^>]+target=\"_blank\">");
			    Matcher m1 = p1.matcher(res);
			    if(m1.find()) {
			    	db.setCid(Integer.parseInt(m1.group(1)));
			    }
			    
			    Pattern p2 = Pattern.compile("<span>([^<]+)</span>");
			    Matcher m2 = p2.matcher(res);
			    if(m2.find()) {
			    	db.setEnd(Integer.parseInt(m2.group(1).trim().replace("（", "").replace("）", "").replace("P", "")));
			    }
				
			    db.setFileflag(count);
			    
			    list.add(db);
			    
			    count--;
			    
				sb =  new StringBuilder();
				b = false;
			}
		}
		
		return list;
	}
	
	private static String getTargetDIV(String html) {
		StringBuilder sb = new StringBuilder();
		
		String[] ss = html.split("\r\n");
		
		boolean b = false;
		
		for(String s:ss) {
			if(s.contains("<div")&&s.contains("id=\"chapterlistload\"")) {
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
		res = res.replace("<div id=\"chapterlistload\">", "")
				.replace("</div>", "");
				
		return res;
	}
}


class DTTJQF extends Thread{
	
	private List<DLAMBean> list;
	
	private  final String TOMCAT_HOME = "F:\\Programs\\apache-tomcat-6.0.33\\webapps\\dm5\\";
	
	private  final String FILE_PATH = "D:\\output\\漫画\\特级囚犯\\";
	
	private  final Map<String,String> SESSIONMAP = new HashMap<String,String>();
	
	private  final Map<String,String> ALMAP = new HashMap<String,String>();
	
	public DTTJQF(List<DLAMBean> list) {
		this.list = list;
	}
	
	public void run() {
		for(int j=list.size()-1;j>=0;j--) {
			
			DLAMBean db = list.get(j);
			
			int cid = db.getCid();
			
			int end = db.getEnd();
			
			int fileflag = db.getFileflag();
		
			if(fileflag<163) {
				continue;
			}
			
			if(fileflag>=167&&fileflag<329) {
				continue;
			}
			
			initSesssion(cid);
			
			File file = new File(FILE_PATH+fileflag+"\\");
			if(!file.exists()) {
				file.mkdir();
			}
			int count = 1;
			for(int i=1;i<=end;i++) {
				
				String url = "http://www.dm5.com/m"+cid+"/chapterfun.ashx?cid="+cid+"&page="+i+"&key=&language=1&gtk=6&_cid=169330&_mid=10364&_dt=2018-04-04+07%3A58%3A59&_sign=c8c0ce6609fd9f1d17b3bad8180c2a5a";
				
				String cs = NetUtil.read(url,SESSIONMAP).toString();
				
				String html = "<html><head><script type=\"text/JavaScript\">var nt = "+System.currentTimeMillis()+"; var str = "+cs+";  document.write(str);</script></head><body></body></html>";
				
				String nt = getRandName();
				
				String tfanme = TOMCAT_HOME+"test"+nt+".html";
				
				write2TomcatFile(html,tfanme);
				
				String target = NetUtil.readbyUnitWithoutProxy("http://10.203.147.2:8080/dm5/test"+nt+".html");
				
				Pattern p = Pattern.compile("<body>([^<]+)</body>");
				Matcher m = p.matcher(target);
				
				if(m.find()) {
					
					String[] ss = m.group(1).trim().split(",");
					
					for(String s:ss) {
						
						String turl = s.trim().replace("amp;", "");
						
						if(ALMAP.get(turl)==null||ALMAP.get(turl).equals("")) {
						    
							ALMAP.put(turl, "123");
							
							String fname = FILE_PATH+fileflag+"//"+NetUtil.getName(count,4)+".jpg";
							
							NetUtil.readAndWrite(turl, fname,SESSIONMAP);
							
							count++;
							
						}
						try {
							Thread.sleep(1500+getRandomInt()+getRandomInt());
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				
				}
				File f = new File(tfanme);
				f.delete();
			}
			
			File cutf = new File(FILE_PATH+fileflag);
			File[] cff = cutf.listFiles();
			
			int realcount = 1;
			CutImage cui = new CutImage();
			for(int i=0;i<cff.length;i++){
				try {
					realcount = cui.cut(cff[i], FILE_PATH+fileflag, "png",realcount,1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			DounUtil.deleteTemp(FILE_PATH+fileflag);
			
			DounUtil.coressImg(FILE_PATH+fileflag+"/");
			
			DounUtil.deleteMapImg(FILE_PATH+fileflag);
			
			ZipCompressor zc = new ZipCompressor(FILE_PATH+"finish/tjqf_"+NetUtil.getName(fileflag,3)+".zip");     
	        zc.compress(FILE_PATH+fileflag+"/");
	        
	        int fg = fileflag;
	        if(fg!=1) {
	        	fg = fg+6 ;
	        }
	        System.out.println("下载完第"+fg+"话 "+ALMAP.size());
	        
	        ALMAP.clear();
	        DounUtil.delete(FILE_PATH+fileflag);
		}
	}
	
	private int getRandomInt() {
		Random ran = new Random();
		return 100+ran.nextInt(1000);
	}

	public  String getRandName() {
			
		Random ran = new Random();
		
		return getTwoWS(ran)+getTwoWS(ran)+getTwoWS(ran)+getTwoWS(ran)+getTwoWS(ran);
	}

	private  String getTwoWS(Random ran) {
		
		return (10+ran.nextInt(90))+"";
	}

	
	private  void write2TomcatFile(String html, String tfanme) {
		File f = new File(tfanme);
		
		try {
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(html.getBytes());
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void initSesssion(int cid) {
		SESSIONMAP.put("Host","www.dm5.com");
		SESSIONMAP.put("Referer","http://www.dm5.com/m"+cid+"/");
		SESSIONMAP.put("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.162 Safari/537.36");
		SESSIONMAP.put("X-Requested-With","XMLHttpRequest");
		SESSIONMAP.put("Cookie","__AdinAll_SSP_UID=3b01fc594b4365e47e85f0ca771ce62e; Hm_lvt_277b91cd106e4e17d0c6bca6022c99b4=1522374269; frombot=1; DM5_MACHINEKEY=5bca70e6-c6a6-4c76-b0f4-7b17b92cb7d2; SERVERID=node2; dm5cookieenabletest=1; __AdinAll_SSP_FRE_TIME=Mon, 02 Apr 2018 23:35:34 GMT; firsturl=http%3A%2F%2Fwww.dm5.com%2Fm872%2F; myTest=1522638377351; dm5imgcooke=865%7C53%2C872%7C10; image_time_cookie=872|636582639204881134|0; dm5imgpage=865|97:1:54:0,872|12:1:54:0; ComicHistoryitem_zh=History=9957,636505849406875795,110085,6,0,0,0,1|195,636517998698326090,414492,1,0,0,0,27|567,636525522582441924,403567,14,0,0,0,65|213,636506750074634063,2496,11,0,0,0,4|35340,636506754542828066,461730,2,0,0,0,1|860,636506755269014886,9835,6,0,0,0,1|9537,636506756926218474,566333,8,0,0,0,50|16506,636506763835991415,177414,11,0,0,0,1|2564,636506764767320189,29838,11,0,0,0,1|16974,636516926840374879,183078,1,0,0,0,1|40218,636518152508332733,571523,1,0,0,0,8|81,636582639204568609,872,12,0,0,0,10&ViewType=0; readhistory_time=1-81-872-12; __AdinAll_SSP_RECORD=4630-2209-1471-279042-139441-15979-617304-236766-1022081-216-1326-1574-1875-1812-2056252-142406-26492-60384-8627-1245; __AdinAll_SSP_FRE=127");
		
	}
	
}

