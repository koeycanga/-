package com;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64.Decoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.CutImage;
import utils.DounUtil;
import utils.NetUtil;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;

import net.coobird.thumbnailator.Thumbnails;

public class DownDMZJ {

	private static String username = "";  
	private static String password = "";  
	private static String host = "10.203.145.30";  
	private static String port = "8080"; 
	
	
	private static final String FOLDER = "D:/output/漫画/赌博破戒录/";
	
	private static final String BASEURL = "https://manhua.dmzj.com";
	
	private static final String WB = "#@page=1";
	
	private static final Map<String,String> map = new HashMap<String,String>();
	
	public static void main(String[] args) {
		
		DownDMZJ dm = new DownDMZJ();
		
		NetUtil.initAuth();
		
		map.put("Referer", "https://manhua.dmzj.com/dubopojielu");
		map.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.162 Safari/537.36");
		
		List<DLAMBean> mulist = new ArrayList<DLAMBean>();
		
		/*String html = NetUtil.readbyUnit("https://manhua.dmzj.com/dubopojielu/", map);// NetUtil.read("https://manhua.dmzj.com/dubopojielu/",map).toString();
		
		System.out.println(html);
		
		Pattern mp = Pattern.compile("<a title=\"[^\"]+?\" href=\"([^\"]+?)\"[^>*?]>([^<]+?)</a>");
		
		Matcher mm = mp.matcher(html);
		
		while(mm.find()) {
			DLAMBean db = new DLAMBean();
			db.setUrl(BASEURL+mm.group(1)+WB);
			String s = dm.getNumberWithoutStr(mm.group(2).trim());//mm.group(2).trim().replace("第", "").replace("卷", "").replace("VOL","");
			db.setFileflag(Integer.parseInt(s));
			if(db.getFileflag()>32) {
				mulist.add(db);
			}
		}*/
		
		int count = 1;
		for(int i=5996;i<=6008;i++) {
			DLAMBean db = new DLAMBean();
			db.setUrl(BASEURL+"/dubopojielu/"+i+".shtml"+WB);
			db.setFileflag(count);
			count++;
			mulist.add(db);
		}
		
		dm.beginDownLoad(mulist);
		
	}
	
	private static String getUrl() {
		
		File f = new File("D:\\output\\漫画\\赌博破戒录\\123.txt");
		
		BufferedReader br = null;
		
		StringBuilder sb = new StringBuilder();
		
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
			String line = null;
			while((line=br.readLine())!=null) {
				sb.append(line+"\r\n");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return sb.toString();
	}
	
	public void beginDownLoad(List<DLAMBean> mulist) {
		
		List<DLAMBean> list1 = new ArrayList<DLAMBean>();
		List<DLAMBean> list2 = new ArrayList<DLAMBean>();
		
		int yz = mulist.size()/2;
		for(int i=0;i<yz;i++) {
			list1.add(mulist.get(i));
		}
		for(int i=yz;i<mulist.size();i++) {
			list2.add(mulist.get(i));
		}
		
		DTforMWDTJ dt1 = new DTforMWDTJ(list1);
		DTforMWDTJ dt2 = new DTforMWDTJ(list2);
		
		dt1.start();
		//dt2.start();
	}

	 private String getNumberWithoutStr(String trim) {
		Pattern pattern = Pattern.compile("[^0-9]");
        Matcher matcher = pattern.matcher(trim);
        String num = matcher.replaceAll("");
		return num;
	}

	private void downImg(String address,int fileflag) {
			
		 	String str = NetUtil.readbyUnit(address, map);// readbyUnit(address);
			
		 	String t_utl = "https:";
		 	
			Pattern p = Pattern.compile("<option\\s*value=\"(//images.dmzj.com/.*?)\"[^>]*>");
			
			Matcher m = p.matcher(str);
			int count = 1;
			while(m.find()){
				String url = t_utl+m.group(1);
				url = "https://images.dmzj.com/d/%E8%B5%8C%E5%8D%9A%E7%A0%B4%E6%88%92%E5%BD%95/VOL01/004.jpg";
				readAndWrite(url,count,fileflag,"jpg",map);
				count++;
			}
			
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
			
			deleteTemp(FOLDER+fileflag);
			
			coressImg(FOLDER+fileflag+"/");
		
			deleteMapImg(FOLDER+fileflag+"/");
			
			ZipCompressor zc = new ZipCompressor(FOLDER+"finish/mwdtz_"+getName(fileflag)+".zip");     
	        zc.compress(FOLDER+fileflag+"/");
	        System.out.println("下载完"+fileflag+"话");
	        delete(FOLDER+fileflag);
	}

	public  void coressImg(String string) {
		int count = 1;
		File file = new File(string);
		if(file.isDirectory()){
			File[] ff = file.listFiles();
			for(int i=0;i<ff.length;i++){
				try {
					String fname = string+NetUtil.getName(count);
					Thumbnails.of(ff[i].getAbsolutePath())  
					.scale(1f)  
					.outputQuality(0.5f) 
					.outputFormat("jpg")  
					.toFile(fname);
					count++;
				} catch (IOException e) {
					e.printStackTrace();
				} 
			}
		}
	}
	
	public void deleteMapImg(String string) {
		 File file = new File(string);
			if(file.isDirectory()){
				File[] ff = file.listFiles();
				for(int i=0;i<ff.length;i++){
					if(ff[i].getName().contains("map")){
						ff[i].delete();
					}
				}
			}
		}
	
	private void deleteTemp(String string) {
		File file = new File(string);
		if(file.isDirectory()){
			File[] ff = file.listFiles();
			for(int i=0;i<ff.length;i++){
				if(!ff[i].getName().contains("map")){
					ff[i].delete();
				}
			}
		}
	}

	private void delete(String path) {
		File f = new File(path);
		if(f.isFile()&&!f.getName().contains("zip")){
			f.delete();
		}
		if(f.isDirectory()){
			File[] ff = f.listFiles();
			for(int i=0;i<ff.length;i++){
				if(ff[i].isFile()&&!ff[i].getName().contains("zip")){
					ff[i].delete();
				}
				if(ff[i].isDirectory()&&!ff[i].getName().equals("finish")){
					delete(ff[i].getAbsolutePath());
					ff[i].delete();
				}
			}
			f.delete();
		}
	}
	
	public static void initAuth(){
		Properties props = System.getProperties();
		props.setProperty("proxySet", "true");
		props.setProperty("http.proxyHost",host);
		props.setProperty("http.proxyPort",port);
		Authenticator.setDefault(new Authenticator(){
			protected PasswordAuthentication getPasswordAuthentication(){
				return new PasswordAuthentication(username,new String(password).toCharArray());
			}
		});
	}
	
	private String getName(int count){
		String name = "";
		if(count<10){
			name = "000"+count;
		}
		if(count>=10&&count<100){
			name = "00"+count;
		}
		if(count>=100&&count<1000){
			name = "0"+count;
		}
		if(count>=1000){
			name = ""+count;
		}
		return name;
	}
	
	private  Set<Cookie> setCookie(){
		Set<Cookie> cookies= new HashSet<Cookie>();
        Cookie c = new Cookie("manhua","CNZZDATA1000465408", "7C1482105027");
        cookies.add(c);
        
        c = new Cookie(".dmzj.com","Hm_lpvt_645dcc265dc58142b6dbfea748247f02", "1481962339");
        cookies.add(c);
        
        return cookies;
	}
	
	public  void readAndWritebyUnit(String address,int count,int fileflag,String format) {  
        WebClient wc = new WebClient(BrowserVersion.CHROME,host,Integer.parseInt(port));  
        URL link = null;
		try {
			link = new URL(address);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} 
        WebRequest request=new WebRequest(link); 
        wc.getOptions().setJavaScriptEnabled(true);  
        wc.getOptions().setCssEnabled(false); 
        wc.getOptions().setThrowExceptionOnScriptError(false); 
        wc.getOptions().setTimeout(10000); 
     
        request.setAdditionalHeader(":authority", "images.dmzj.com");
        request.setAdditionalHeader("cookie", "show_tip_1=0; Hm_lvt_645dcc265dc58142b6dbfea748247f02=1514191405,1514871403,1515220965,1515549229");
        request.setAdditionalHeader("Referer", "https://manhua.dmzj.com/dubopojielu/5996.shtml");
        request.setAdditionalHeader(":path", "/d/%E8%B5%8C%E5%8D%9A%E7%A0%B4%E6%88%92%E5%BD%95/VOL01/001.jpg");
        request.setAdditionalHeader(":scheme", "https");
        request.setAdditionalHeader("user-agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.162 Safari/537.36");
		
        
        Set<Cookie> cookies= setCookie();
        
        Iterator<Cookie> i = cookies.iterator();
        while (i.hasNext()) 
        {
          //  wc.getCookieManager().addCookie(i.next());
        }
        
        File ff = new File(FOLDER+fileflag);
        if(!ff.exists()){
			ff.mkdirs();
		}
		File f = new File(FOLDER+fileflag+"/"+getName(count)+"."+format);
		try {
			Page page = wc.getPage(request);
			InputStream is = page.getWebResponse().getContentAsStream();
			DataInputStream fis = new DataInputStream(is);
			DataOutputStream dos = new DataOutputStream(new FileOutputStream(f));
			byte[] b = new byte[4096];
			int lg;
			while((lg=fis.read(b))!=-1){
				//System.out.println("lg="+lg);
				dos.write(b,0,lg);
			}
			dos.flush();
			dos.close();
			fis.close();
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  
		wc.close();
    } 
	
	public  void readAndWrite(String address,int count,int fileflag,String format, Map<String, String> mp){
		 	File ff = new File(FOLDER+fileflag);
	        if(!ff.exists()){
				ff.mkdirs();
			}
			File f = new File(FOLDER+fileflag+"/"+getName(count)+"."+format);
			try {
				URL url = new URL(address);
				URLConnection uc = url.openConnection();
				System.setProperty("sun.net.client.defaultConnectTimeout", "10000");  
				System.setProperty("sun.net.client.defaultReadTimeout", "10000");  
				uc.setConnectTimeout(10000);
				uc.setReadTimeout(10000);
				
				uc.setRequestProperty(":authority", "images.dmzj.com");
				uc.setRequestProperty(":method", "GET");
				uc.setRequestProperty(":path", "/d/%E8%B5%8C%E5%8D%9A%E7%A0%B4%E6%88%92%E5%BD%95/VOL01/004.jpg");
				uc.setRequestProperty("accept", "image/webp,image/apng,image/*,*/*;q=0.8");
				uc.setRequestProperty("Upgrade-Insecure-Requests", "1");
				uc.setRequestProperty("cookie", "show_tip_1=0; Hm_lvt_645dcc265dc58142b6dbfea748247f02=1514191405,1514871403,1515220965,1515549229");
				uc.setRequestProperty("Referer", "https://manhua.dmzj.com/dubopojielu/5996.shtml");
				uc.setRequestProperty(":scheme", "https");
				uc.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.162 Safari/537.36");
				
				InputStream is = uc.getInputStream();
				DataInputStream fis = new DataInputStream(is);
				DataOutputStream dos = new DataOutputStream(new FileOutputStream(f));
				byte[] b = new byte[4096];
				int lg;
				while((lg=fis.read(b))!=-1){
					dos.write(b,0,lg);
				}
				dos.flush();
				dos.close();
				fis.close();
			} catch (FailingHttpStatusCodeException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}  
	}
	
	public  String readbyUnit(String address) {  
        WebClient wc = new WebClient(BrowserVersion.CHROME,host,Integer.parseInt(port));  
        wc.getOptions().setJavaScriptEnabled(true);  
        wc.getOptions().setCssEnabled(false); 
        wc.getOptions().setThrowExceptionOnScriptError(false); 
        wc.getOptions().setTimeout(10000); 
        HtmlPage page = null;
		try {
			page = wc.getPage(address);
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  
		String pageXml = "";
		if(page!=null){
			pageXml = page.asXml(); 
		}
		wc.close();
        if(pageXml.equals("")){
        	pageXml = readbyUnit(address);
        }
        return pageXml;
    } 
	
	public  StringBuffer read(String address){
		StringBuffer sb = new StringBuffer();
		try {
			URL url = new URL(address);
			URLConnection uc = url.openConnection();
			System.setProperty("sun.net.client.defaultConnectTimeout", "10000");  
			System.setProperty("sun.net.client.defaultReadTimeout", "10000");  
			uc.setConnectTimeout(10000);
			uc.setReadTimeout(10000);
			BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream(),"utf-8"));
			String line = null;
			while((line=br.readLine())!=null){
				sb.append(line+"\r\n");
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(sb.toString().equals("")){
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				sb = read(address);
			}
		}
		return sb;
	}
	
	class DTforMWDTJ extends Thread{
		private List<DLAMBean> mulist;
		
		public DTforMWDTJ(List<DLAMBean> mulist) {
			this.mulist = mulist;
		}
		
		public void run() {
			for(DLAMBean db:mulist) {
				downImg(db.getUrl(),db.getFileflag());
			}
		}
		
	}

}

