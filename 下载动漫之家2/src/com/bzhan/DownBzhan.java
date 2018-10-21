package com.bzhan;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;

import utils.NetUtil;

public class DownBzhan {

	private static String host = "10.203.74.189";  
	private static String port = "8080"; 
	
	private static final Map<String,String> map = new HashMap<String,String>();
	
	public static void main(String[] args) {
		
		NetUtil.initAuth();
		
		initMap("https://www.bilibili.com/video/av8889531/?spm_id_from=333.788.videocard.0","upos-hz-mirrorks3u.acgvideo.com");
		
		String url = "https://upos-hz-mirrorks3u.acgvideo.com/upgcxcode/45/04/14670445/14670445-1-32.flv?e=ig8euxZM2rNcNbNMhWKVhoMMnwN3hwdEto8g5X10ugNcXBlqNxHxNEVE5XREto8KqJZHUa6m5J0SqE85tZvEuENvNC8xNEVE9EKE9IMvXBvE2ENvNCImNEVEK9GVqJIwqa80WXIekXRE9IB5QK==&deadline=1534494436&gen=playurl&oi=3664410028&os=ks3u&platform=pc&trid=ffd5711a7d7246e4b4b268a7d7ea7b8d&uipk=5&uipv=5&upsig=b46e98078cccf40c2c2d27ac73daf615";
		
		String fname = "e:/古礼.flv";
		
		NetUtil.readAndWritebyUnit(url,
				fname,
				map);
		
		/*readAndWritebyUnit("14890351",
				fname);*/
	}

	private static void readAndWritebyUnit(String string, String fname) {
		    WebClient wc = new WebClient(BrowserVersion.CHROME,host,Integer.parseInt(port));  
			
			/*try {

			    for(int i=1;i<=2;i++) {
			        URL link = null;
			        String address = "";//"https://upos-hz-mirrorcos.acgvideo.com/upgcxcode/51/03/14890351/"+string+"-"+i+"-15.flv?um_deadline=1533265144&platform=pc&rate=122400&oi=3664410028&um_sign=ade06a7cbc9262438ca53357bd40e99b&gen=playurl&os=cos&trid=d51f99d14ba241b7a84f8c3c89b3fa0f";
					if(i==1) {
						address = "https://upos-hz-mirrorcos.acgvideo.com/upgcxcode/51/03/14890351/14890351-1-15.flv?um_deadline=1533265144&platform=pc&rate=122400&oi=3664410028&um_sign=ade06a7cbc9262438ca53357bd40e99b&gen=playurl&os=cos&trid=d51f99d14ba241b7a84f8c3c89b3fa0f";
						fname = "e:/机动警察25-1.flv";
					}
					if(i==2) {
						address = "https://upos-hz-mirrorcos.acgvideo.com/upgcxcode/51/03/14890351/14890351-2-15.flv?um_deadline=1533267376&platform=pc&rate=124100&oi=3664410028&um_sign=a25d45d0c752233f25fa66db5efaf380&gen=playurl&os=cos&trid=fc6fe42708614f4eba88410feb2c3302";
						fname = "e:/机动警察25-2.flv";
					}
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
				
				
					Page page = wc.getPage(request);
					InputStream is = page.getWebResponse().getContentAsStream();
					DataInputStream fis = new DataInputStream(is);
					File f = new File(fname);
					DataOutputStream dos = new DataOutputStream(new FileOutputStream(f));
					byte[] b = new byte[16384];
					int lg;
					while((lg=fis.read(b))!=-1){
						//System.out.println("lg="+lg);
						dos.write(b,0,lg);
					}
					dos.flush();
					dos.close();
				} 
				wc.close();
		    }catch (FailingHttpStatusCodeException e) {
				
				e.printStackTrace();
			} catch (MalformedURLException e) {
				
				e.printStackTrace();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			*/
		/*	try {
				DataOutputStream dos = new DataOutputStream(new FileOutputStream("e:/机动警察25.flv"));
				for(int i=1;i<=2;i++) {
					FileInputStream fins = new FileInputStream(new File("e:/机动警察25-"+i+".flv"));
					byte[] b = new byte[16384];
					int lg = -1;
					if(i>1) {
						fins.skip(4);
					}
					while((lg=fins.read(b))>0) {
						dos.write(b,0,lg);
					}
					fins.close();
				}
				dos.flush();
				dos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}*/
	}

	private static void initMap(String src,String host) {
		
		map.put("Accept", "*/*");
		map.put("Accept-Encoding", "gzip, deflate, br");
		map.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7");
		map.put("Connection", "keep-alive");
		map.put("DNT", "1");
		map.put("Host", host);
		map.put("Origin", "https://www.bilibili.com");
		map.put("Referer", src);
		map.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.162 Safari/537.36");
		
//		map.put("authority", "upos-hz-mirrorkodo.acgvideo.com");
//		map.put("method", "GET");
//		map.put("path", "/upgcxcode/39/64/9576439/9576439-1-48.mp4?e=ig8euxZM2rNcNbNa7zRVhoMB7WKghwdEto8g5X10ugNcXBlqNxHxNEVE5XREto8KqJZHUa6m5J0SqE85tZvEuENvNC8xNEVE9EKE9IMvXBvE2ENvNCImNEVEK9GVqJIwqa80WXIekXRE9IB5QK==&deadline=1532661104&dynamic=1&gen=playurl&oi=3664410028&os=kodo&platform=pc&rate=277100&trid=909c6c2c5ceb43b3bad02133023339ba&uipk=5&uipv=5&um_deadline=1532661104&um_sign=3563f5395ee98ac5565f22f32e0bd9e1&upsig=44a48e4b559bf0c27453986579234dc0");
//		map.put("scheme", "https");
//		map.put("accept", "*/*");
//		map.put("accept-encoding", "identity;q=1, *;q=0");
//		map.put("accept-language", "zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7");
//		map.put("dnt", "1");
//		map.put("range", "bytes=0-");
//		map.put("referer", src);
//		map.put("user-agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.162 Safari/537.36");
		
	}

}
