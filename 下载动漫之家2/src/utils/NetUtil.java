package utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;

public class NetUtil {

	private static String username = "";  
	private static String password = "";  
	private static String host = "10.203.74.189";  
	private static String port = "8080"; 
	
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
	
	public static String getName(int count){
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
	
	public static String getName(int count,int ws){
		String name = "";
		
		int lg = String.valueOf(count).length();
		
		int cz = ws - lg;
		
		for(int i=0;i<cz;i++) {
			name+="0";
		}
		
		name+=count;
		
		return name;
	}
	
	private static Set<Cookie> setCookie(){
		Set<Cookie> cookies= new HashSet<Cookie>();
        Cookie c = new Cookie("manhua","CNZZDATA1000465408", "7C1482105027");
        cookies.add(c);
        
        c = new Cookie(".dmzj.com","Hm_lpvt_645dcc265dc58142b6dbfea748247f02", "1481962339");
        cookies.add(c);
        
        return cookies;
	}
	
	
	public static void readAndWritebyUnit(String address,String filename,Map<String,String> map) {  
        WebClient wc = new WebClient(BrowserVersion.CHROME,host,Integer.parseInt(port));  
        URL link = null;
		try {
			link = new URL(address);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} 
        WebRequest request=new WebRequest(link); 
        wc.getOptions().setJavaScriptEnabled(false);  
        wc.getOptions().setCssEnabled(false); 
        wc.getOptions().setThrowExceptionOnScriptError(false); 
        wc.getOptions().setTimeout(10000); 
        for(String s:map.keySet()) {
        	request.setAdditionalHeader(s, map.get(s));
        }
        
		File f = new File(filename);
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
			wc.close();
		} catch (FailingHttpStatusCodeException e) {
			
			e.printStackTrace();
		} catch (MalformedURLException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}finally {
			if(f.length()<=0) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				readAndWritebyUnit(address,filename,map);
			}
		}
		
    } 
	
	public static void readAndWritebyUnit(String address,int count,int fileflag,String format,String FOLDER,Map<String,String> map) {  
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
        for(String s:map.keySet()) {
        	request.setAdditionalHeader(s, map.get(s));
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
	
	public static void readAndWritebyUnit(String address,int count,int fileflag,String format,String FOLDER) {  
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
        request.setAdditionalHeader("Accept", "image/webp,image/*,*/*;q=0.8");
        request.setAdditionalHeader("Accept-Encoding", "gzip, deflate, sdch");
        request.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6,zh-TW;q=0.4");
        request.setAdditionalHeader("Cache-Control", "max-age=0");
        request.setAdditionalHeader("Cookie", "show_tip_1=0; RORZ_7f25_saltkey=ZB9j1J0D; RORZ_7f25_lastvisit=1482102763; RORZ_7f25_lastact=1482106365%09api.php%09js; pt_s_198bb240=vt=1482107013112&cad=; pt_198bb240=uid=bycb6M9weDrQdHF7M63lpQ&nid=1&vid=kZcWv0vyxzg5iteQoozNqQ&vn=1&pvn=1&sact=1482107033923&to_flag=0&pl=nNm-nGOhZFGdV9qHmfngNQ*pt*1482107013112; Hm_lvt_645dcc265dc58142b6dbfea748247f02=1481961375,1481961578,1481962335,1482107012; Hm_lpvt_645dcc265dc58142b6dbfea748247f02=1482107336");
        request.setAdditionalHeader("Host", "images.dmzj.com");
        request.setAdditionalHeader("If-Modified-Since", "Mon, 16 Jun 2014 08:32:24 GMT");
        request.setAdditionalHeader("If-None-Match", "\"539eab98-1e6a10\"");
        request.setAdditionalHeader("Proxy-Connection", "keep-alive");
        request.setAdditionalHeader("Referer", "http://manhua.dmzj.com/gyhjk/2044.shtml");
        request.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");
        
        
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
	
	public static void readAndWrite(String address,int count,String filename,String format,String FOLDER){
	 	File ff = new File(FOLDER+filename);
        if(!ff.exists()){
			ff.mkdirs();
		}
		File f = new File(FOLDER+filename+"/"+getName(count)+"."+format);
		try {
			URL url = new URL(address);
			URLConnection uc = url.openConnection();
			System.setProperty("sun.net.client.defaultConnectTimeout", "10000");  
			System.setProperty("sun.net.client.defaultReadTimeout", "10000");  
			uc.setConnectTimeout(10000);
			uc.setReadTimeout(10000);
			uc.setRequestProperty("Accept", "image/webp,image/*,*/*;q=0.8");
			uc.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");
			uc.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6,zh-TW;q=0.4");
			uc.setRequestProperty("Cache-Control", "max-age=0");
			uc.setRequestProperty("Cookie", "show_tip_1=0; RORZ_7f25_saltkey=ZB9j1J0D; RORZ_7f25_lastvisit=1482102763; RORZ_7f25_lastact=1482106365%09api.php%09js; pt_s_198bb240=vt=1482107013112&cad=; pt_198bb240=uid=bycb6M9weDrQdHF7M63lpQ&nid=1&vid=kZcWv0vyxzg5iteQoozNqQ&vn=1&pvn=1&sact=1482107033923&to_flag=0&pl=nNm-nGOhZFGdV9qHmfngNQ*pt*1482107013112; Hm_lvt_645dcc265dc58142b6dbfea748247f02=1481961375,1481961578,1481962335,1482107012; Hm_lpvt_645dcc265dc58142b6dbfea748247f02=1482107336");
			uc.setRequestProperty("Host", "images.dmzj.com");
			uc.setRequestProperty("If-Modified-Since", "Mon, 16 Jun 2014 08:32:24 GMT");
			uc.setRequestProperty("If-None-Match", "\"539eab98-1e6a10\"");
			uc.setRequestProperty("Proxy-Connection", "keep-alive");
			uc.setRequestProperty("Referer", "http://manhua.dmzj.com/gyhjk/2044.shtml");
			uc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");
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
	
	public static void readAndWrite(String address,int count,int fileflag,String format,String FOLDER){
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
				uc.setRequestProperty("Accept", "image/webp,image/*,*/*;q=0.8");
				uc.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");
				uc.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6,zh-TW;q=0.4");
				uc.setRequestProperty("Cache-Control", "max-age=0");
				uc.setRequestProperty("Cookie", "show_tip_1=0; RORZ_7f25_saltkey=ZB9j1J0D; RORZ_7f25_lastvisit=1482102763; RORZ_7f25_lastact=1482106365%09api.php%09js; pt_s_198bb240=vt=1482107013112&cad=; pt_198bb240=uid=bycb6M9weDrQdHF7M63lpQ&nid=1&vid=kZcWv0vyxzg5iteQoozNqQ&vn=1&pvn=1&sact=1482107033923&to_flag=0&pl=nNm-nGOhZFGdV9qHmfngNQ*pt*1482107013112; Hm_lvt_645dcc265dc58142b6dbfea748247f02=1481961375,1481961578,1481962335,1482107012; Hm_lpvt_645dcc265dc58142b6dbfea748247f02=1482107336");
				uc.setRequestProperty("Host", "images.dmzj.com");
				uc.setRequestProperty("If-Modified-Since", "Mon, 16 Jun 2014 08:32:24 GMT");
				uc.setRequestProperty("If-None-Match", "\"539eab98-1e6a10\"");
				uc.setRequestProperty("Proxy-Connection", "keep-alive");
				uc.setRequestProperty("Referer", "http://manhua.dmzj.com/gyhjk/2044.shtml");
				uc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");
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
	
	public static void readAndWrite(String address,String fname){
	 	boolean isagin = false;
		File f = new File(fname);
		InputStream is = null;
		DataInputStream fis = null;
		DataOutputStream dos = null;
		try {
			URL url = new URL(address);
			URLConnection uc = url.openConnection();
			System.setProperty("sun.net.client.defaultConnectTimeout", "10000");  
			System.setProperty("sun.net.client.defaultReadTimeout", "10000");  
			uc.setConnectTimeout(10000);
			uc.setReadTimeout(10000);
	
			int contentLength = uc.getContentLength();
			//System.out.println(contentLength);
			is = uc.getInputStream();
			fis = new DataInputStream(is);
			dos = new DataOutputStream(new FileOutputStream(f));
			byte[] b = new byte[4096];
			int lg;
			while((lg=fis.read(b))!=-1){
				dos.write(b,0,lg);
			}
			dos.flush();
		} catch (Exception e) {
			isagin = true;
			e.printStackTrace();
		}finally {
			try {
				if(dos!=null)dos.close();
				if(fis!=null)fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(isagin) {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				readAndWrite( address, fname);
			}
		}
}
	
	public static boolean readAndWriteWithBoolean(String address,String fname,Map<String,String> map){
	 	boolean isagin = false;
		File f = new File(fname);
		InputStream is = null;
		DataInputStream fis = null;
		DataOutputStream dos = null;
		try {
			URL url = new URL(address);
			URLConnection uc = url.openConnection();
			System.setProperty("sun.net.client.defaultConnectTimeout", "10000");  
			System.setProperty("sun.net.client.defaultReadTimeout", "10000");  
			uc.setConnectTimeout(10000);
			uc.setReadTimeout(10000);
	
			for(String s:map.keySet()) {
				uc.setRequestProperty(s,map.get(s));
			}
			
			int contentLength = uc.getContentLength();
			//System.out.println(contentLength);
			is = uc.getInputStream();
			fis = new DataInputStream(is);
			dos = new DataOutputStream(new FileOutputStream(f));
			byte[] b = new byte[4096];
			int lg;
			while((lg=fis.read(b))!=-1){
				dos.write(b,0,lg);
			}
			dos.flush();
		} catch (Exception e) {
			isagin = true;
			e.printStackTrace();
		}finally {
			try {
				if(dos!=null)dos.close();
				if(fis!=null)fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return isagin;
}
	
	public static void readAndWrite(String address,String fname,Map<String,String> map){
	 	boolean isagin = false;
		File f = new File(fname);
		InputStream is = null;
		DataInputStream fis = null;
		DataOutputStream dos = null;
		try {
			URL url = new URL(address);
			URLConnection uc = url.openConnection();
			System.setProperty("sun.net.client.defaultConnectTimeout", "10000");  
			System.setProperty("sun.net.client.defaultReadTimeout", "10000");  
			uc.setConnectTimeout(10000);
			uc.setReadTimeout(10000);
			
			for(String s:map.keySet()) {
				uc.setRequestProperty(s,map.get(s));
			}
	
			int contentLength = uc.getContentLength();
			//System.out.println(contentLength);
			is = uc.getInputStream();
			fis = new DataInputStream(is);
			dos = new DataOutputStream(new FileOutputStream(f));
			byte[] b = new byte[4096];
			int lg;
			while((lg=fis.read(b))!=-1){
				dos.write(b,0,lg);
			}
			dos.flush();
		} catch (Exception e) {
			isagin = true;
			e.printStackTrace();
		}finally {
			try {
				if(dos!=null)dos.close();
				if(fis!=null)fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(isagin) {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				readAndWrite( address, fname,map);
			}
		}
	}
	
	public static void readAndWrite(String address,String fname,Map<String,String> map,int cs){
	 	boolean isagin = false;
		File f = new File(fname);
		InputStream is = null;
		DataInputStream fis = null;
		DataOutputStream dos = null;
		try {
			URL url = new URL(address);
			URLConnection uc = url.openConnection();
			System.setProperty("sun.net.client.defaultConnectTimeout", "10000");  
			System.setProperty("sun.net.client.defaultReadTimeout", "10000");  
			uc.setConnectTimeout(10000);
			uc.setReadTimeout(10000);
			
			for(String s:map.keySet()) {
				uc.setRequestProperty(s,map.get(s));
			}
	
			int contentLength = uc.getContentLength();
			//System.out.println(contentLength);
			is = uc.getInputStream();
			fis = new DataInputStream(is);
			dos = new DataOutputStream(new FileOutputStream(f));
			byte[] b = new byte[4096];
			int lg;
			while((lg=fis.read(b))!=-1){
				dos.write(b,0,lg);
			}
			dos.flush();
		} catch (Exception e) {
			isagin = true;
			e.printStackTrace();
		}finally {
			try {
				if(dos!=null)dos.close();
				if(fis!=null)fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(isagin&&cs>0) {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				readAndWrite( address, fname,map,cs-1);
			}
		}
	}
	
	public static void readAndWrite(String address,int count,int fileflag,String format,String FOLDER,Map<String,String> map){
	 	boolean isagin = false;
		File ff = new File(FOLDER+fileflag);
        if(!ff.exists()){
			ff.mkdirs();
		}
		File f = new File(FOLDER+fileflag+"/"+getName(count)+"."+format);
		InputStream is = null;
		DataInputStream fis = null;
		DataOutputStream dos = null;
		try {
			URL url = new URL(address);
			URLConnection uc = url.openConnection();
			System.setProperty("sun.net.client.defaultConnectTimeout", "10000");  
			System.setProperty("sun.net.client.defaultReadTimeout", "10000");  
			uc.setConnectTimeout(10000);
			uc.setReadTimeout(10000);
			
			for(String s:map.keySet()) {
				uc.setRequestProperty(s,map.get(s));
			}
			
			int contentLength = uc.getContentLength();
			//System.out.println(contentLength);
			is = uc.getInputStream();
			fis = new DataInputStream(is);
			dos = new DataOutputStream(new FileOutputStream(f));
			byte[] b = new byte[4096];
			int lg;
			while((lg=fis.read(b))!=-1){
				dos.write(b,0,lg);
			}
			dos.flush();
		} catch (Exception e) {
			isagin = true;
			e.printStackTrace();
		}finally {
			try {
				if(dos!=null)dos.close();
				if(fis!=null)fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(isagin) {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				readAndWrite( address, count, fileflag, format, FOLDER, map);
			}
		}
}
	
	
	public static void readAndWrite(String address,int count,String fileflag,String format,String FOLDER,Map<String,String> map){
	 	boolean isagin = false;
		File ff = new File(FOLDER+fileflag);
        if(!ff.exists()){
			ff.mkdirs();
		}
		File f = new File(FOLDER+fileflag+"/"+getName(count)+"."+format);
		InputStream is = null;
		DataInputStream fis = null;
		DataOutputStream dos = null;
		try {
			URL url = new URL(address);
			URLConnection uc = url.openConnection();
			System.setProperty("sun.net.client.defaultConnectTimeout", "10000");  
			System.setProperty("sun.net.client.defaultReadTimeout", "10000");  
			uc.setConnectTimeout(10000);
			uc.setReadTimeout(10000);
			
			for(String s:map.keySet()) {
				uc.setRequestProperty(s,map.get(s));
			}
			
			int contentLength = uc.getContentLength();
			//System.out.println(contentLength);
			is = uc.getInputStream();
			fis = new DataInputStream(is);
			dos = new DataOutputStream(new FileOutputStream(f));
			byte[] b = new byte[4096];
			int lg;
			while((lg=fis.read(b))!=-1){
				dos.write(b,0,lg);
			}
			dos.flush();
		} catch (Exception e) {
			isagin = true;
			e.printStackTrace();
		}finally {
			try {
				if(dos!=null)dos.close();
				if(fis!=null)fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(isagin) {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				readAndWrite( address, count, fileflag, format, FOLDER, map);
			}
		}
}
	
	public static int readAndWriteWithCode(String address,int count,String fileflag,String format,String FOLDER,Map<String,String> map){
	 	int code = -1;
		boolean isagin = false;
		File ff = new File(FOLDER+fileflag);
        if(!ff.exists()){
			ff.mkdirs();
		}
		File f = new File(FOLDER+fileflag+"/"+getName(count)+"."+format);
		InputStream is = null;
		DataInputStream fis = null;
		DataOutputStream dos = null;
		try {
			URL url = new URL(address);
			HttpURLConnection uc = (HttpURLConnection) url.openConnection();
			System.setProperty("sun.net.client.defaultConnectTimeout", "10000");  
			System.setProperty("sun.net.client.defaultReadTimeout", "10000");  
			uc.setConnectTimeout(10000);
			uc.setReadTimeout(10000);
			
			for(String s:map.keySet()) {
				uc.setRequestProperty(s,map.get(s));
			}
			code = uc.getResponseCode();
			int contentLength = uc.getContentLength();
			//System.out.println(contentLength);
			is = uc.getInputStream();
			fis = new DataInputStream(is);
			dos = new DataOutputStream(new FileOutputStream(f));
			byte[] b = new byte[4096];
			int lg;
			while((lg=fis.read(b))!=-1){
				dos.write(b,0,lg);
			}
			dos.flush();
		} catch (Exception e) {
			isagin = true;
			e.printStackTrace();
		}finally {
			try {
				if(dos!=null)dos.close();
				if(fis!=null)fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(isagin&&code!=400) {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				readAndWrite( address, count, fileflag, format, FOLDER, map);
			}
		}
		return code;
	}
	
	public static String readbyUnit2(String address) {  
        WebClient wc = new WebClient(BrowserVersion.CHROME,host,Integer.parseInt(port));  
        wc.getOptions().setJavaScriptEnabled(true);  
        wc.getOptions().setCssEnabled(false); 
        wc.getOptions().setThrowExceptionOnScriptError(false); 
        wc.getOptions().setTimeout(10000); 
        TextPage page = null;
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
			pageXml = page.getContent();
		}
		wc.close();
        if(pageXml.equals("")){
        	//pageXml = readbyUnit2(address);
        }
        return pageXml;
    } 
	
	public static String readbyUnitWithoutProxy(String address) {  
        WebClient wc = new WebClient(BrowserVersion.CHROME);  
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
	
	public static String readbyUnit(String address) {  
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
        	try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        	pageXml = readbyUnit(address);
        }
        return pageXml;
    } 
	
	public static String readbyUnit(String address,boolean isjsrun) {  
        WebClient wc = new WebClient(BrowserVersion.CHROME,host,Integer.parseInt(port));  
        wc.getOptions().setJavaScriptEnabled(isjsrun);  
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
        	try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        	pageXml = readbyUnit(address,isjsrun);
        }
        return pageXml;
    } 
	
	public static String readbyUnit(String address,Map<String,String> map) {  
        WebClient wc = new WebClient(BrowserVersion.CHROME,host,Integer.parseInt(port));  
        wc.getOptions().setJavaScriptEnabled(true);  
        wc.getOptions().setCssEnabled(false); 
        wc.getOptions().setThrowExceptionOnScriptError(false); 
        wc.getOptions().setTimeout(10000); 
       
        URL link = null;
		try {
			link = new URL(address);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} 
        WebRequest request=new WebRequest(link); 
        for(String s:map.keySet()) {
        	request.setAdditionalHeader(s,map.get(s));
        }
        HtmlPage page = null;
		try {
			page = wc.getPage(request);
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
        	pageXml = readbyUnit(address,map);
        }
        return pageXml;
    } 
	
	public static String readbyUnitReturnTextPage(String address,Map<String,String> map) {  
        WebClient wc = new WebClient(BrowserVersion.CHROME,host,Integer.parseInt(port));  
        wc.getOptions().setJavaScriptEnabled(true);  
        wc.getOptions().setCssEnabled(false); 
        wc.getOptions().setThrowExceptionOnScriptError(false); 
        wc.getOptions().setTimeout(10000); 
       
        URL link = null;
		try {
			link = new URL(address);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} 
        WebRequest request=new WebRequest(link); 
        for(String s:map.keySet()) {
        	request.setAdditionalHeader(s,map.get(s));
        }
        TextPage  page = null;
		try {
			page = wc.getPage(request);
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  
		String pageXml = "";
		if(page!=null){
			pageXml = page.getContent(); 
		}
		wc.close();
        if(pageXml.equals("")){
        	pageXml = readbyUnitReturnTextPage(address,map);
        }
        return pageXml;
    } 
	
	public static String readbyUnit(String address,Map<String,String> map,boolean isjsrun) {  
        WebClient wc = new WebClient(BrowserVersion.CHROME,host,Integer.parseInt(port));  
        wc.getOptions().setJavaScriptEnabled(isjsrun);  
        wc.getOptions().setCssEnabled(false); 
        wc.getOptions().setActiveXNative(false);
        wc.getOptions().setThrowExceptionOnFailingStatusCode(false);
        wc.getOptions().setThrowExceptionOnScriptError(false); 
        wc.getOptions().setTimeout(10000); 
       
        URL link = null;
		try {
			link = new URL(address);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} 
        WebRequest request=new WebRequest(link); 
        for(String s:map.keySet()) {
        	request.setAdditionalHeader(s,map.get(s));
        }
        HtmlPage page = null;
		try {
			page = wc.getPage(request);
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
        	pageXml = readbyUnit(address,map,isjsrun);
        }
        return pageXml;
    } 
	
	public static StringBuffer read(String address){
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
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				sb = read(address);
			}
		}
		return sb;
	}
	
	public static StringBuffer read(String address,Map<String,String> map){
		StringBuffer sb = new StringBuffer();
		try {
			URL url = new URL(address);
			URLConnection uc = url.openConnection();
			System.setProperty("sun.net.client.defaultConnectTimeout", "10000");  
			System.setProperty("sun.net.client.defaultReadTimeout", "10000");  
			uc.setConnectTimeout(10000);
			uc.setReadTimeout(10000);
			
			for(String s:map.keySet()) {
				uc.setRequestProperty(s,map.get(s));
			}
			
			
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
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				sb = read(address,map);
			}
		}
		return sb;
	}
	
	
	public static StringBuffer read(String address,Map<String,String> map,String charset){
		StringBuffer sb = new StringBuffer();
		try {
			URL url = new URL(address);
			URLConnection uc = url.openConnection();
			System.setProperty("sun.net.client.defaultConnectTimeout", "10000");  
			System.setProperty("sun.net.client.defaultReadTimeout", "10000");  
			uc.setConnectTimeout(10000);
			uc.setReadTimeout(10000);
			
			for(String s:map.keySet()) {
				uc.setRequestProperty(s,map.get(s));
			}
			
			BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream(),charset));
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
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				sb = read(address,map);
			}
		}
		return sb;
	}
	
	public static StringBuffer readOnlyOnce(String address,Map<String,String> map){
		StringBuffer sb = new StringBuffer();
		try {
			URL url = new URL(address);
			URLConnection uc = url.openConnection();
			System.setProperty("sun.net.client.defaultConnectTimeout", "10000");  
			System.setProperty("sun.net.client.defaultReadTimeout", "10000");  
			uc.setConnectTimeout(10000);
			uc.setReadTimeout(10000);
			
			for(String s:map.keySet()) {
				uc.setRequestProperty(s,map.get(s));
			}
			
			
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
			
		}
		return sb;
	}
	
 public static String getTargetDV(String html,String regex) {
		
		String[] ss = html.split("\r\n");
		
		boolean b = false;
		
		StringBuilder sb = new StringBuilder();
		
		for(String s:ss) {
			if(s.contains(regex)) {
				b = true;
				continue;
			}
			if(b&&s.contains("</div")) {
				break;
			}
			if(b) {
				sb.append(s);
			}
			
		}
		return sb.toString();
	}
}
