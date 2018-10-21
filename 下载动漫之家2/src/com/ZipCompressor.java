package com;

import java.io.BufferedInputStream;  
import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileOutputStream;  
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.CRC32;  
import java.util.zip.CheckedOutputStream;  
import java.util.zip.ZipEntry;  
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;  
  
public class ZipCompressor {     
    static final int BUFFER = 8192;     
   
    static  String genName ;
    
    private File zipFile;     
      
    
    public ZipCompressor(String pathName) {     
        zipFile = new File(pathName);     
    }     
    public void compress(String... pathName) {   
        ZipOutputStream out = null;     
        try {    
            FileOutputStream fileOutputStream = new FileOutputStream(zipFile);     
            CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream,     
                    new CRC32());     
            out = new ZipOutputStream(cos);     
            String basedir = "";   
            for (int i=0;i<pathName.length;i++){  
                compress(new File(pathName[i]), out, basedir);     
            }  
            out.close();    
        } catch (Exception e) {     
            throw new RuntimeException(e);     
        }   
    }     
    public void compress(String srcPathName) {     
        File file = new File(srcPathName);     
        if (!file.exists())     
            throw new RuntimeException(srcPathName + "not exists");     
        try {     
            FileOutputStream fileOutputStream = new FileOutputStream(zipFile);     
            CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream,     
                    new CRC32());     
            ZipOutputStream out = new ZipOutputStream(cos);     
            String basedir = "-1";     
            compress(file, out, basedir);     
            out.close();     
        } catch (Exception e) {     
            throw new RuntimeException(e);     
        }     
    }     
    
    private void compress(File file, ZipOutputStream out, String basedir) {     
        /* �ж���Ŀ¼�����ļ� */    
        if (file.isDirectory()) {     
            if(basedir.equals("-1")){
            	basedir = "";
            	genName = file.getName()+"/";
            }else{
            	basedir = basedir.replace(genName, "");
            }
            this.compressDirectory(file, out, basedir);     
        } else {       
            if(basedir.equals("-1")){
            	basedir = "";
            }else{
            	basedir = basedir.replace(genName, "");
            }
            this.compressFile(file, out, basedir);     
        }     
    }     
     
    private void compressDirectory(File dir, ZipOutputStream out, String basedir) {     
        if (!dir.exists())     
            return;     
    
        File[] files = dir.listFiles();     
        for (int i = 0; i < files.length; i++) {     
            /* �ݹ� */    
            compress(files[i], out, basedir+dir.getName() + "/");     
        }     
    }     
     
    private void compressFile(File file, ZipOutputStream out, String basedir) {     
        if (!file.exists()) {     
            return;     
        }     
        try {     
            BufferedInputStream bis = new BufferedInputStream(     
                    new FileInputStream(file));     
            ZipEntry entry = new ZipEntry(basedir + file.getName());     
            out.putNextEntry(entry);     
            int count;     
            byte data[] = new byte[BUFFER];     
            while ((count = bis.read(data, 0, BUFFER)) != -1) {     
                out.write(data, 0, count);     
            }     
            bis.close();     
        } catch (Exception e) {     
            throw new RuntimeException(e);     
        }     
    }     
    
    public void unZipFiles(File zipfile, String descDir) {
    	File file = new File(descDir);
    	if(!file.exists()){
    		file.mkdirs();
    	}
        try {
            ZipFile zf = new ZipFile(zipfile);
            for (Enumeration entries = zf.entries(); entries.hasMoreElements();) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                String zipEntryName = entry.getName(); 
                
                InputStream in = zf.getInputStream(entry);
                OutputStream out = new FileOutputStream(descDir + zipEntryName);
                byte[] buf1 = new byte[1024];
                int len;
                while ((len = in.read(buf1)) > 0) {
                    out.write(buf1, 0, len);
                }
                in.close();
                out.close();
                //System.out.println("��ѹ�����?.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    public void ZipMultiFile(String filepath ,String zippath) {
		try {
	        File file = new File(filepath);// Ҫ��ѹ�����ļ���
	        File zipFile = new File(zippath);
	        InputStream input = null;
	        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
	        if(file.isDirectory()){
	            File[] files = file.listFiles();
	            for(int i = 0; i < files.length; ++i){
	                input = new FileInputStream(files[i]);
	                zipOut.putNextEntry(new ZipEntry(file.getName() + File.separator + files[i].getName()));
	                int temp = 0;
	                while((temp = input.read()) != -1){
	                    zipOut.write(temp);
	                }
	                input.close();
	            }
	        }
	        zipOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
   
   
   public static void main(String[] args) {     
        ZipCompressor zc = new ZipCompressor("E:/resource.zip");     
        zc.compress("e:/");  
        File f = new File("E:/resource.zip");
      //  zc.unZipFiles(f, "e:/UNZIP/");
       // f.delete();
    }  
}   