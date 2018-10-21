package com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import utils.NetUtil;
/**
 * 本类用来下载句子迷网的句子
 * @author k
 *
 */
public class DownJuZiMI {

	private static final String BASEURL = "https://www.juzimi.com/";
	
	private static final Map<String,String> map = new HashMap<String,String>();
	
	public static void main(String[] args) throws IOException {
		
		NetUtil.initAuth();
		
		System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");
		
		URL reqURL = new URL("https://blog.csdn.net/qq_23152947/article/details/72676550" ); //创建URL对象
		HttpsURLConnection httpsConn = (HttpsURLConnection)reqURL.openConnection();

		/*下面这段代码实现向Web页面发送数据，实现与网页的交互访问
		httpsConn.setDoOutput(true); 
		OutputStreamWriter out = new OutputStreamWriter(huc.getOutputStream(), "8859_1"); 
		out.write( "……" ); 
		out.flush(); 
		out.close();
		*/

		//取得该连接的输入流，以读取响应内容 
		InputStreamReader insr = new InputStreamReader(httpsConn.getInputStream());

		//读取服务器的响应内容并显示
		int respInt = insr.read();
		while( respInt != -1){
	      System.out.print((char)respInt);
		  respInt = insr.read();
		}
		
		/*
		String src = "https://www.juzimi.com/ju/390705";
		initMap("https://www.juzimi.com/ju/390704","/ju/390705");
		
		String html = NetUtil.read(src).toString();
		
		System.out.println(html);
		
		Pattern p = Pattern.compile("<h1 class=\"with-tabs\" id=\"xqtitle\">([^<]+)</h1>");
		
		Matcher m = p.matcher(html);
		
		if(m.find()) {
			System.out.println(m.group(1));
		}*/
	}

	private static void initMap(String src,String path) {
		map.put("Cookie", "SESSc60faee9ca2381b86f19bef9617d499b=4li92tvh30vvjb42qfqsdhfgo5; __cfduid=de99492f3b5d9c65094d8baa4652952631516674357; xqrclbr=25325; bdshare_firstime=1516674357792; UM_distinctid=16127462071133-066f6d736edc7a-4049042b-1aeaa0-16127462073362; _ga=GA1.2.388830834.1516781772; CNZZDATA1256504232=292248224-1516782069-%7C1519370356; Hm_lvt_0684e5255bde597704c827d5819167ba=1531294557; visited=1; Hm_cv_0684e5255bde597704c827d5819167ba=1*login*PC-0!1*version*PC; has_js=1; xqrcli=MTUzMTQ0MjEyMywxNjgwKjEwNTAsV2luMzIsTmV0c2NhcGUsMjUzMjU%3D; homere=1");
		map.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.162 Safari/537.36");
		map.put("Referer", src);
		map.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		map.put("Accept-Encoding", "gzip, deflate");
		map.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7");
		map.put("Host", "www.juzimi.com");
		map.put("authority", "www.juzimi.com");
		map.put("method", "GET");
		map.put("path", path);
		map.put("scheme", "https");
		map.put("if-modified-since", "Fri, 13 Jul 2018 00:36:03 GMT");
	}

}
