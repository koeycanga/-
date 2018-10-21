package com.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.DLAMBean;
import com.ZipCompressor;

import utils.CutImage;
import utils.DounUtil;
import utils.NetUtil;

/**
 * 本类用来下载 乌龙派出所
 * @author k
 *
 */
public class DownWLPCS {
	
	public static void main(String[] args) {
		
		NetUtil.initAuth();
		
		String ahtml = NetUtil.readbyUnit("http://www.dm5.com/manhua-wulongpaichusuo/");
		
		String div = getTargetDIV(ahtml);
		
		List<DLAMBean> list = dealTargetLi(div);
		
		List<DLAMBean> list1 = new ArrayList<DLAMBean>();
		
		List<DLAMBean> list2 = new ArrayList<DLAMBean>();
		
		System.out.println(list.size());
		
		int lg = list.size()/2;
		
		for(int i=0;i<lg;i++) {
			list1.add(list.get(i));
		}
		
		for(int i=lg;i<list.size();i++) {
			list2.add(list.get(i));
		}
		
		new DTWLPCS(list1).start();
		
		new DTWLPCS(list2).start();
		
	}
	
	
	private static List<DLAMBean>  dealTargetLi(String html) {
		
		List<DLAMBean> list = new ArrayList<DLAMBean>();
		
		StringBuilder sb = new StringBuilder();
		
		String[] ss = html.split("\r\n");
		
		boolean b = false;
		
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
			    
			    list.add(db);
			    
				sb =  new StringBuilder();
				b = false;
			}
		}
		
		int lg = list.size();
		
		for(int i=0;i<list.size();i++) {
			list.get(i).setFileflag(lg);
			lg--;
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

class DTWLPCS extends Thread{
	
	private  final String TOMCAT_HOME = "F:\\Programs\\apache-tomcat-6.0.33\\webapps\\dm5\\";
	
	private  final Map<String,String> SESSIONMAP = new HashMap<String,String>();
	
	private  final Map<String,String> ALMAP = new HashMap<String,String>();
	
	private  final String FILE_PATH = "D:\\output\\漫画\\乌龙派出所\\";
	
	private List<DLAMBean> list;
	
	public DTWLPCS(List<DLAMBean> list) {
		this.list = list;
	}
	
	public void run() {
		for(int j=list.size()-1;j>=0;j--) {
			
			DLAMBean db = list.get(j);
			
			int cid = db.getCid();
			
			int end = db.getEnd();
			
			int fileflag = db.getFileflag();
		
			initSesssion(cid);
			
			File file = new File(FILE_PATH+fileflag+"\\");
			if(!file.exists()) {
				file.mkdir();
			}
			int count = 1;
			for(int i=1;i<=end;i++) {
				
				String nt = getRandName();
				
				String tfanme = TOMCAT_HOME+"test"+nt+".html";
				
				String url = getUrl(cid,i);
				
				dealTomcat(url,tfanme);
				
				String[] ss = getTomcatSS(nt);
				
				int s_count = ss.length;
				
				int[] res = dealDownAndWrite(ss,fileflag,count,s_count);
				
				int temp_count = res[0];
				s_count = res[1];
			
				while(s_count!=ss.length) {
					try {
						Thread.sleep(1000+getRandomInt(1000)+getRandomInt(500));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					deleteCountFile(fileflag,count,temp_count);
					
					url = getUrl(cid,i);
					
					dealTomcat(url,tfanme);
					
					ss = getTomcatSS(nt);
					
					s_count = ss.length;
					
					res = dealDownAndWrite(ss,fileflag,count,s_count);
					
					temp_count = res[0];
					s_count = res[1];
				}
				
				count = temp_count;
				
				File f = new File(tfanme);
				f.delete();
			}
			
			File cutf = new File(FILE_PATH+fileflag);
			File[] cff = cutf.listFiles();
			
			int realcount = 1;
			CutImage cui = new CutImage();
			for(int i=0;i<cff.length;i++){
				try {
					realcount = cui.cut(cff[i], FILE_PATH+fileflag, "png",realcount,1210);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			DounUtil.deleteTemp(FILE_PATH+fileflag);
			
			DounUtil.coressImg(FILE_PATH+fileflag+"/");
			
			DounUtil.deleteMapImg(FILE_PATH+fileflag);
			
			ZipCompressor zc = new ZipCompressor(FILE_PATH+"finish/wlpcs_"+NetUtil.getName(fileflag,3)+".zip");     
	        zc.compress(FILE_PATH+fileflag+"/");
	        System.out.println("下载完"+fileflag+"话  "+ALMAP.size());
	        ALMAP.clear();
	        DounUtil.delete(FILE_PATH+fileflag);
		}
	}
	
	private int[] dealDownAndWrite(String[] ss,int fileflag,int count,int s_count) {
		int[] res = new int[2];
		for(String s:ss) {
			
			String turl = s.trim().replace("amp;", "");
			
			if(ALMAP.get(turl)==null||ALMAP.get(turl).equals("")) {
			    						
				ALMAP.put(turl, "123");
				
				String fname = FILE_PATH+fileflag+"//"+NetUtil.getName(count,4)+".jpg";
				
				boolean isagin = NetUtil.readAndWriteWithBoolean(turl, fname,SESSIONMAP);
				
				if(isagin) {
					ALMAP.remove(turl);
					s_count--;
				}
				count++;
			}
			try {
				Thread.sleep(1000+getRandomInt(500)+getRandomInt(500));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		res[0] = count;
		res[1] = s_count;
		return res;
	}
	
	private void deleteCountFile(int fileflag,int count,int temp_count) {
		for(int i=count;i<temp_count;i++) {
			String fname1 = FILE_PATH+fileflag+"//"+NetUtil.getName(i,4)+".jpg";
			File f1 = new File(fname1);
			if(f1.exists()) {
				f1.delete();
			}
		}
	}
	
	private String[] getTomcatSS(String nt) {
		String[] ss = null ;
		String target = NetUtil.readbyUnitWithoutProxy("http://10.203.147.2:8080/dm5/test"+nt+".html");
		
		Pattern p = Pattern.compile("<body>([^<]+)</body>");
		Matcher m = p.matcher(target);
		
		
		if(m.find()) {
			 ss = m.group(1).trim().split(",");
		}
		return ss;
	}

	private void dealTomcat(String url,String tfname) {
		String cs = NetUtil.read(url,SESSIONMAP).toString();
		
		String html = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"><script type=\"text/JavaScript\"> var str = "+cs+";  document.write(str);</script></head><body></body></html>";
		
		write2TomcatFile(html,tfname);
	}
	
	private String getUrl(int cid,int i) {
		String aymhtml = getYM(cid,i);
		
		String key = getKey(aymhtml);
		
		String mid  = getTargetAttr(aymhtml,"var\\s*COMIC_MID\\s*=([^;]+)").replace("\"", "").trim();
		
	    String dt  = getTargetAttr(aymhtml,"var\\s*DM5_VIEWSIGN_DT\\s*=([^;]+)").replace("\"", "").trim().replace(" ", "+").replace(":", "%3A");
	    
	    String sign = getTargetAttr(aymhtml,"var\\s*DM5_VIEWSIGN\\s*=([^;]+)").replace("\"", "").trim();
		
		//chapterfun
		String url = "http://www.dm5.com/m"+cid+"/chapterfun.ashx?cid="+cid+"&page="+i+"&key="+key+"&language=1&gtk=6&_cid="+cid+"&_mid="+mid+"&_dt="+dt+"&_sign="+sign;
		
		return url;
	}
	
	private  String getRandName() {
		
		Random ran = new Random();
		
		return getTwoWS(ran)+getTwoWS(ran)+getTwoWS(ran)+getTwoWS(ran)+getTwoWS(ran)+getTwoWS(ran);
	}
	
    private  String getTwoWS(Random ran) {
		
    	int a = ran.nextInt(100);
    	
    	if(a<10) {
    		return "0"+a;
    	}else {
    		return ""+a;
    	}
	} 
	
	private  String getTargetAttr(String html,String regx) {
        String res = "";
		
		Pattern p1 =  Pattern.compile(regx);
		
		Matcher m1 = p1.matcher(html);
		
		if(m1.find()) {
			res = m1.group(1);
		}
		return res;
	}
	
	private  String getKey(String aymhtml) {
		Pattern p = Pattern.compile("<input\\s*type=\"hidden\"\\s*id=\"dm5_key\"\\s*value=\"([^\"]*)\"\\s*[/]*> ");
		
		Matcher m = p.matcher(aymhtml);
		
		if(m.find()) {
			return m.group(1);
		}
		
		return "";
	}

	private  String getYM(int cid,int i) {
		String addr = "http://www.dm5.com/m"+cid+"/#ipg"+i;
		
		String aymhtml = NetUtil.read(addr).toString();
		
		return aymhtml ;
	}
	
	private  int getRandomInt(int flag) {
		
		Random ran = new Random();
		
		return 100+ran.nextInt(flag);
		
	}
	
	private  void write2TomcatFile(String html, String tfanme) {
		File f = new File(tfanme);
		
		try {
			FileOutputStream fos = new FileOutputStream(f);
			OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");   
			osw.write(html);
			osw.flush();
			osw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private  void initSesssion(int cid) {
		SESSIONMAP.put("Host","www.dm5.com");
		SESSIONMAP.put("Referer","http://www.dm5.com/m"+cid+"/");
		SESSIONMAP.put("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.162 Safari/537.36");
		SESSIONMAP.put("X-Requested-With","XMLHttpRequest");
		SESSIONMAP.put("Cookie","__AdinAll_SSP_UID=3b01fc594b4365e47e85f0ca771ce62e; Hm_lvt_277b91cd106e4e17d0c6bca6022c99b4=1522374269; frombot=1; DM5_MACHINEKEY=5bca70e6-c6a6-4c76-b0f4-7b17b92cb7d2; SERVERID=node2; dm5cookieenabletest=1; __AdinAll_SSP_FRE_TIME=Mon, 02 Apr 2018 23:35:34 GMT; firsturl=http%3A%2F%2Fwww.dm5.com%2Fm872%2F; myTest=1522638377351; dm5imgcooke=865%7C53%2C872%7C10; image_time_cookie=872|636582639204881134|0; dm5imgpage=865|97:1:54:0,872|12:1:54:0; ComicHistoryitem_zh=History=9957,636505849406875795,110085,6,0,0,0,1|195,636517998698326090,414492,1,0,0,0,27|567,636525522582441924,403567,14,0,0,0,65|213,636506750074634063,2496,11,0,0,0,4|35340,636506754542828066,461730,2,0,0,0,1|860,636506755269014886,9835,6,0,0,0,1|9537,636506756926218474,566333,8,0,0,0,50|16506,636506763835991415,177414,11,0,0,0,1|2564,636506764767320189,29838,11,0,0,0,1|16974,636516926840374879,183078,1,0,0,0,1|40218,636518152508332733,571523,1,0,0,0,8|81,636582639204568609,872,12,0,0,0,10&ViewType=0; readhistory_time=1-81-872-12; __AdinAll_SSP_RECORD=4630-2209-1471-279042-139441-15979-617304-236766-1022081-216-1326-1574-1875-1812-2056252-142406-26492-60384-8627-1245; __AdinAll_SSP_FRE=127");
	}
	
}

