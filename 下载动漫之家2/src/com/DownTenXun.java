package com;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.DounUtil;
import utils.NetUtil;

/**
 * 本类用来下载腾讯漫画
 * @author k
 *
 */
public class DownTenXun {

	private static final Map<String,String> SESSIONMAP = new HashMap<String,String>();
	private static final Map<String,String> MH_SESSIONMAP = new HashMap<String,String>();
	
	private static final String BASE_FOLDER = "D:/output/漫画/海贼王/";
	
	private static final String TOMCATPATH = "F:/Programs/apache-tomcat-6.0.33/webapps/ROOT/tengxun.html";
	
	private static final String MH_NAME = "jty";
	
	public static void main(String[] args) {
		NetUtil.initAuth();
		
		for(int i=258;i<=925;i++){
			int fileflag =i;
			
			String url = "http://ac.qq.com/ComicView/index/id/505430/cid/"+fileflag;
			
			initMap(url);
			
			File ff = new File(BASE_FOLDER+fileflag+"/");
			
			if(!ff.exists()){
				ff.mkdirs();
			}
			String html = NetUtil.readbyUnit(url);
			
			Pattern p = Pattern.compile("var\\s*DATA\\s*=\\s*'(.*?)'\\s*,");
			
			Matcher m = p.matcher(html);
			
			String data = "";
			
			if(m.find()){
				data = m.group(1);
			}
			
			String base = "<!DOCTYPE html><html><head><script type=\"text/javascript\">var DATA = '";
			
			base+= data+"';";
			
			base+=readQQlog();
			
			base+="PICTURE = DATA.picture;for(var i=0;i<PICTURE.length;i++){document.write('<p>'+PICTURE[i].url+'<p/>');}</script></head><body></body></html>";
			
			writeToFile(base);
			
			String target = NetUtil.readbyUnitWithoutProxy("http://localhost:8080/tengxun.html");
			
			Pattern p2= Pattern.compile("<p>([^<]+?)</p>");
			
			Matcher m2 = p2.matcher(target);
			
			int count = 1;
			
			while(m2.find()){
				String src  = m2.group(1).replace("&amp;", "&").trim();
				if(src.contains("http")){
					//src = src.replace("/0", "");
					System.out.println(src);
					initMHMAP(src,url);
					String filename = BASE_FOLDER+fileflag+"/"+NetUtil.getName(count)+".jpg";
					NetUtil.readAndWritebyUnit(src, filename, MH_SESSIONMAP);
					count++;
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			compress(ff,fileflag);
		}
	}

	private static void initMHMAP(String src,String url) {
		MH_SESSIONMAP.put(":authority", "manhua.qpic.cn");
		MH_SESSIONMAP.put(":method", "GET");
		MH_SESSIONMAP.put(":path", src);
		MH_SESSIONMAP.put("referer",url);
		MH_SESSIONMAP.put("user-agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.162 Safari/537.36");
	}
	
	private static void initMap(String url) {
		//SESSIONMAP.put("", "");
		SESSIONMAP.put("Host", "ac.tc.qq.com");
		SESSIONMAP.put("Cookie", "pgv_pvi=7126989824; RK=SLJsV+2FeT; ptcz=f258b6c755c7aeec2ead3fcba8016de66f460b06624898e2e7bf53ca45cc3f1e; pt2gguin=o0034774397");
		SESSIONMAP.put("Referer", url);
		SESSIONMAP.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
	}

	private static void compress(File folder,int fileflag){
		File[] ff = folder.listFiles();
		if(ff.length>0){
			for(int i=0;i<ff.length;i++){
				if(ff[i].getName().contains("webp")){
					ff[i].delete();
				}
			}
			ZipCompressor zc = new ZipCompressor(BASE_FOLDER+"finish/"+MH_NAME+"_"+NetUtil.getName(fileflag)+".zip");     
	        zc.compress(folder.getAbsolutePath()+"/");
		}
        DounUtil.delete(BASE_FOLDER);
	}
	
	private static void writeToFile(String base) {
		File file = new File(TOMCATPATH);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(base.getBytes());
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String readQQlog() {
		String line = "";
		File file = new File("QQ.txt");
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			line = br.readLine();
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return line;
	}

}
