package com;

import java.io.File;
import java.util.ArrayList;
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
 * 本类用来下载机器猫大长篇
 * @author k
 *
 */
public class DownJQM_DCP {

	private static final String Tomcat_Web_path = "F:\\Programs\\apache-tomcat-6.0.33\\webapps\\temp\\jqm_dcp.html";
	
	private static final String FOLDER = "D:\\output\\漫画\\哆啦A梦\\";
    
    private static final Map<String,String> aleady_map = new HashMap<String,String>();
    
    private static final Map<String,String> map = new HashMap<String,String>();
    
    private static final Map<String,String> t_map = new HashMap<String,String>();
	
	public static void main(String[] args) {
		NetUtil.initAuth();
		
		List<DLAMBean> list = new ArrayList<DLAMBean>();
		
		dealMulist(list);
		
		initMap();
		
		for(DLAMBean db:list) {
			File f = new File(FOLDER+"finish/"+db.getMh_name()+".zip");
			if(!f.exists()) {
				downIMg(db.getEnd(),db.getMh_cid(),db.getMh_name());
			}
		}
		
		
	}

	private static void downIMg(int end,String mh_cid,String fileflag) {
		int count = 1;
		for(int i=1;i<=end;i++) {
			
			String url = "http://www.dm5.com/m"+mh_cid+"/chapterfun.ashx?cid="+mh_cid+"&page="+i+"&key=&language=1&gtk=6";
			
			System.out.println(url);
			
			String html = NetUtil.read(url,map).toString();
			
			String format = "utf-8";
			
			String tofile = "<html><head <meta charset=\""+format+"\">></head>" + 
					"<body>" + 
					"<div id=\"tdv\">" + 
					"</div>" + 
					"</body>" + 
					"<script>" + html+ 
					"document.getElementById(\"tdv\").innerText = d;" + 
					"</script>" + 
					"</html>";
			
			DounUtil.write2Tomcat(tofile,Tomcat_Web_path,format);
			
			String target = NetUtil.readbyUnitWithoutProxy("http://localhost:8080/temp/jqm_dcp.html"); 
			
			String regx = "<div[^>]+>([^<]+?)</div>";
			
			Pattern p = Pattern.compile(regx);
			
			Matcher m = p.matcher(target);
			
			if(m.find()) {
				String imgsrc = m.group(1);
				
				System.out.println(imgsrc);
				
				String[] ss = imgsrc.split(",");
				
				for(String as:ss){
					as = as.trim().replace("amp;", "").replace(" ", "%20");
					if(!as.equals("")){
						if(aleady_map.get(as)==null) {
							NetUtil.readAndWrite(as,count, fileflag, "jpg", FOLDER,t_map);
							count++;
							aleady_map.put(as,as);
						}
					}
				}
			}
			
			File f = new File(Tomcat_Web_path);
			f.delete();
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	
		if(end<100) {
			
			File cutf = new File(FOLDER+fileflag);
			File[] cff = cutf.listFiles();
			
			int realcount = 1;
			CutImage cui = new CutImage();
			for(int i=0;i<cff.length;i++){
				try {
					realcount = cui.cut(cff[i], FOLDER+fileflag, "png",realcount);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			DounUtil.deleteTemp(FOLDER+fileflag);
		
			DounUtil.coressImg(FOLDER+fileflag+"/");
			
			DounUtil.deleteMapImg(FOLDER+fileflag);
		}
		
		ZipCompressor zc = new ZipCompressor(FOLDER+"finish/"+fileflag+".zip");     
        zc.compress(FOLDER+fileflag+"/");
        System.out.println("下载完一话");
       
        DounUtil.delete(FOLDER+fileflag);
	}
	
	private static void initMap() {
		
		map.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		map.put("Cookie","DM5_MACHINEKEY=384df7f9-d527-40fa-b265-4a65d18bc0eb; SERVERID=node4; UM_distinctid=160bbc38afc78e-034f68554be723-704e2745-1fa400-160bbc38afd577; dm5cookieenabletest=1; __AdinAll_SSP_UID=95002eaaa528d6f47221ce36f08d2df8; __AdinAll_SSP_FRE_TIME=Thu, 04 Jan 2018 11:24:03 GMT; __atuvc=1%7C1; __AdinAll_SSP_FRE=10; myTest=1514982315255; CNZZDATA1264410469=1835730532-1514976341-http%253A%252F%252Fwww.dm5.com%252F%7C1514981741; __AdinAll_SSP_RECORD=2046-659543-32394-1342029-1563-6108-0-0-0-0-0-0-0-0-0-0-0-0-0-0; CNZZDATA30089965=cnzz_eid%3D354193805-1514974749-%26ntime%3D1514980149; CNZZDATA30087176=cnzz_eid%3D533721719-1514975627-%26ntime%3D1514981027; CNZZDATA30090267=cnzz_eid%3D751316945-1514975111-%26ntime%3D1514980511; CNZZDATA1261430596=1272658615-1514975865-%7C1514981265; __utma=1.1745416580.1514978643.1514978643.1514978643.1; __utmc=1; __utmz=1.1514978643.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); dm5imgcooke=241953%7C2%2C241955%7C19; ComicHistoryitem_zh=History=567,636506085388997282,241955,4,0,0,0,47&ViewType=0; readhistory_time=1-567-241955-4; image_time_cookie=241953|636506057231150613|1,241955|636506105966251152|6; dm5imgpage=241953|1:1:17:0,241955|4:1:55:0");
		map.put("Host", "www.dm5.com");
		map.put("Referer", "http://www.dm5.com/");
		
		t_map.put("Referer", "http://www.dm5.com/");
		t_map.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3135.4 Safari/537.36");
		t_map.put("Host", "manhua1025-61-174-50-98.cdndm5.com");
		//t_map.put("Cookie","DM5_MACHINEKEY=384df7f9-d527-40fa-b265-4a65d18bc0eb; SERVERID=node4; UM_distinctid=160bbc38afc78e-034f68554be723-704e2745-1fa400-160bbc38afd577; dm5cookieenabletest=1; __AdinAll_SSP_UID=95002eaaa528d6f47221ce36f08d2df8; __AdinAll_SSP_FRE_TIME=Thu, 04 Jan 2018 11:24:03 GMT; __atuvc=1%7C1; __AdinAll_SSP_FRE=10; myTest=1514982315255; CNZZDATA1264410469=1835730532-1514976341-http%253A%252F%252Fwww.dm5.com%252F%7C1514981741; __AdinAll_SSP_RECORD=2046-659543-32394-1342029-1563-6108-0-0-0-0-0-0-0-0-0-0-0-0-0-0; CNZZDATA30089965=cnzz_eid%3D354193805-1514974749-%26ntime%3D1514980149; CNZZDATA30087176=cnzz_eid%3D533721719-1514975627-%26ntime%3D1514981027; CNZZDATA30090267=cnzz_eid%3D751316945-1514975111-%26ntime%3D1514980511; CNZZDATA1261430596=1272658615-1514975865-%7C1514981265; __utma=1.1745416580.1514978643.1514978643.1514978643.1; __utmc=1; __utmz=1.1514978643.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); dm5imgcooke=241953%7C2%2C241955%7C19; ComicHistoryitem_zh=History=567,636506085388997282,241955,4,0,0,0,47&ViewType=0; readhistory_time=1-567-241955-4; image_time_cookie=241953|636506057231150613|1,241955|636506105966251152|6; dm5imgpage=241953|1:1:17:0,241955|4:1:55:0");
		
	}
	
	private static void dealMulist(List<DLAMBean> list) {
		String muurl = "http://www.dm5.com/manhua-duola-a-meng/";
		
		String muhtml = NetUtil.readbyUnit(muurl);
		
		Pattern mi_p = Pattern.compile("<li[^>]+?>[^<]+?<a class=\"tg\" href=\"/m([^\"/]+?)/\" title=\"([^\"]+?)\" target=\"_blank\">([^<]+?)</a>([^<]+?)</li>");
		
		Matcher mi_m = mi_p.matcher(muhtml);
		
		while(mi_m.find()) {
			
			String href = mi_m.group(1);
			String mhname = mi_m.group(2);
			String s_ys = mi_m.group(4).trim().replace("（", "").replace("）", "").replace("页", "");
			int ys = Integer.parseInt(s_ys);
		
			DLAMBean db = new DLAMBean();
			db.setMh_cid(href);
			db.setMh_name(mhname);
			db.setFileflag(ys);
			db.setEnd(ys);
			
			list.add(db);
		}
	}

}
