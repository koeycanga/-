package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import net.coobird.thumbnailator.Thumbnails;

public class DounUtil {

	public static void coressImg(String string) {
		int count = 1;
		File file = new File(string);
		if(file.isDirectory()){
			File[] ff = file.listFiles();
			for(int i=0;i<ff.length;i++){
				try {
					String fname = string+NetUtil.getName(count);
					Thumbnails.of(ff[i].getAbsolutePath())  
					.scale(1f)  
					.outputQuality(0.6f) 
					.outputFormat("jpg")  
					.toFile(fname);
					count++;
				} catch (IOException e) {
					e.printStackTrace();
				} 
			}
		}
	}
	
	public static void coressImg(String string,float f) {
		int count = 1;
		File file = new File(string);
		if(file.isDirectory()){
			File[] ff = file.listFiles();
			for(int i=0;i<ff.length;i++){
				try {
					String fname = string+NetUtil.getName(count);
					Thumbnails.of(ff[i].getAbsolutePath())  
					.scale(1f)  
					.outputQuality(f) 
					.outputFormat("jpg")  
					.toFile(fname);
					count++;
				} catch (IOException e) {
					e.printStackTrace();
				} 
			}
		}
	}
	
	public static void coressImg_CM(String string) {
		int count = 1;
		File file = new File(string);
		if(file.isDirectory()){
			File[] ff = file.listFiles();
			for(int i=0;i<ff.length;i++){
				try {
					String fname = string+"map_"+NetUtil.getName(count);
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
	
	public static void coressImg_CM(String string,float f) {
		int count = 1;
		File file = new File(string);
		if(file.isDirectory()){
			File[] ff = file.listFiles();
			for(int i=0;i<ff.length;i++){
				try {
					String fname = string+"map_"+NetUtil.getName(count);
					Thumbnails.of(ff[i].getAbsolutePath())  
					.scale(1f)  
					.outputQuality(f) 
					.outputFormat("jpg")  
					.toFile(fname);
					count++;
				} catch (IOException e) {
					e.printStackTrace();
				} 
			}
		}
	}
	

	public static void write2Tomcat(String tofile,String Tomcat_Web_path,String format) {
		
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(Tomcat_Web_path)),format)); 
			writer.write(tofile);
			writer.flush();
			writer.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void write2Tomcat(String tofile,String Tomcat_Web_path) {
		
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(Tomcat_Web_path)), "utf-8")); 
			writer.write(tofile);
			writer.flush();
			writer.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void deleteTemp(String string) {
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

	public static void deleteMapImg(String string) {
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


	public static void delete(String path) {
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
}
