package utils;
import java.awt.image.BufferedImage;  
import java.io.File;  
import java.util.ArrayList;  
import java.util.List;  

import javax.imageio.ImageIO;  
//import com.lyis.commons.dto.ImageDto;  
/** 
 * 图片锟叫割工锟斤拷锟斤拷 
 *  
 * @author Johnson 
 * @version Friday June 10th, 2011 
 */  

public class CutImage {  
	public static void main(String[] args) throws Exception{
		CutImage c = new CutImage();
		c.cut("D:/output/鍓戦浼犲/0021.jpg","D:/output/鍓戦浼犲/",432,679,"jpg");
	}
	
	public int cut_auto(File sourceFile, String targetDir, String formatname,int count)  
            throws Exception {    
		BufferedImage source = ImageIO.read(sourceFile);  
        if(source==null){
        	return count;
        }
		int sWidth = source.getWidth(); // 图片锟斤拷锟�  
        int sHeight = source.getHeight(); // 图片锟竭讹拷  
        int height = sHeight;
        if(sWidth>=sHeight){
        	int width = sWidth/2;
        	if (sWidth >=width && sHeight >=height) {  
                int cols = 0; // 锟斤拷锟斤拷锟斤拷片锟斤拷锟斤拷  
                int rows = 0; // 锟斤拷锟斤拷锟斤拷片锟斤拷锟斤拷  
                int eWidth = 0; // 末锟斤拷锟斤拷片锟斤拷锟�  
                int eHeight = 0; // 末锟斤拷锟斤拷片锟竭讹拷  
                if (sWidth % width == 0) {  
                    cols = sWidth / width;  
                } else {  
                    cols = sWidth / width + 1;  
                }  
                eWidth = sWidth - width;  
                if (sHeight % height == 0) {  
                    rows = sHeight / height;  
                } else {  
                    eHeight = sHeight % height;  
                    rows = sHeight / height + 1;  
                }  
                rows = 1;
                cols = 2;
                String fileName = null;  
                File file = new File(targetDir);  
                if (!file.exists()) { // 锟芥储目录锟斤拷锟斤拷锟节ｏ拷锟津创斤拷目录  
                    file.mkdirs();  
                }  
                BufferedImage image = null;  
                int cWidth = 0; // 锟斤拷前锟斤拷片锟斤拷锟�  
                int cHeight = 0; // 锟斤拷前锟斤拷片锟竭讹拷  
                for (int i = rows-1; i>=0; i--) {  
                    for (int j = cols-1; j >= 0; j--) {  
                        cWidth = getWidth(j, cols, eWidth, width);  
                        cHeight = getHeight(i, rows, eHeight, height);  
                        // x锟斤拷锟斤拷,y锟斤拷锟斤拷,锟斤拷锟�,锟竭讹拷  
                        image = source.getSubimage(j * width, i * height, cWidth,cHeight);  
                        fileName = targetDir + "/map_" + getName(count)+ "."+formatname;  
                        count++;
                        file = new File(fileName);  
                        ImageIO.write(image, formatname, file); 
                    }  
                }  
            }  
        }else{
        	BufferedImage image = source.getSubimage(0, 0, sWidth,sHeight);  
            String fileName = targetDir + "/map_" + getName(count)+ "."+formatname;  
            count++;
            File file = new File(fileName);  
            ImageIO.write(image, formatname, file);  
        }
        return count;
    }  
	
	public int cut(File sourceFile, String targetDir, String formatname,int count,int twidth)  
            throws Exception {    
		BufferedImage source = ImageIO.read(sourceFile);  
        if(source==null){
        	return count;
        }
		int sWidth = source.getWidth(); // 图片锟斤拷锟�  
        int sHeight = source.getHeight(); // 图片锟竭讹拷  
        int height = sHeight;
        if(sWidth>=twidth){
        	int width = sWidth/2;
        	if (sWidth >=width && sHeight >=height) {  
                int cols = 0; // 锟斤拷锟斤拷锟斤拷片锟斤拷锟斤拷  
                int rows = 0; // 锟斤拷锟斤拷锟斤拷片锟斤拷锟斤拷  
                int eWidth = 0; // 末锟斤拷锟斤拷片锟斤拷锟�  
                int eHeight = 0; // 末锟斤拷锟斤拷片锟竭讹拷  
                if (sWidth % width == 0) {  
                    cols = sWidth / width;  
                } else {  
                    cols = sWidth / width + 1;  
                }  
                eWidth = sWidth - width;  
                if (sHeight % height == 0) {  
                    rows = sHeight / height;  
                } else {  
                    eHeight = sHeight % height;  
                    rows = sHeight / height + 1;  
                }  
                rows = 1;
                cols = 2;
                String fileName = null;  
                File file = new File(targetDir);  
                if (!file.exists()) { // 锟芥储目录锟斤拷锟斤拷锟节ｏ拷锟津创斤拷目录  
                    file.mkdirs();  
                }  
                BufferedImage image = null;  
                int cWidth = 0; // 锟斤拷前锟斤拷片锟斤拷锟�  
                int cHeight = 0; // 锟斤拷前锟斤拷片锟竭讹拷  
                for (int i = rows-1; i>=0; i--) {  
                    for (int j = cols-1; j >= 0; j--) {  
                        cWidth = getWidth(j, cols, eWidth, width);  
                        cHeight = getHeight(i, rows, eHeight, height);  
                        // x锟斤拷锟斤拷,y锟斤拷锟斤拷,锟斤拷锟�,锟竭讹拷  
                        image = source.getSubimage(j * width, i * height, cWidth,cHeight);  
                        fileName = targetDir + "/map_" + getName(count)+ "."+formatname;  
                        count++;
                        file = new File(fileName);  
                        ImageIO.write(image, formatname, file); 
                    }  
                }  
            }  
        }else{
        	BufferedImage image = source.getSubimage(0, 0, sWidth,sHeight);  
            String fileName = targetDir + "/map_" + getName(count)+ "."+formatname;  
            count++;
            File file = new File(fileName);  
            ImageIO.write(image, formatname, file);  
        }
        return count;
    }  
	
	public int cut(File sourceFile, String targetDir, String formatname,int count)  
            throws Exception {    
		BufferedImage source = ImageIO.read(sourceFile);  
        if(source==null){
        	return count;
        }
		int sWidth = source.getWidth(); // 图片锟斤拷锟�  
        int sHeight = source.getHeight(); // 图片锟竭讹拷  
        int width = 0 ;
        int height = sHeight;
        if(sWidth>=width*2){
        	width = sWidth/2;
        	if (sWidth >=width && sHeight >=height) {  
                int cols = 0; // 锟斤拷锟斤拷锟斤拷片锟斤拷锟斤拷  
                int rows = 0; // 锟斤拷锟斤拷锟斤拷片锟斤拷锟斤拷  
                int eWidth = 0; // 末锟斤拷锟斤拷片锟斤拷锟�  
                int eHeight = 0; // 末锟斤拷锟斤拷片锟竭讹拷  
                if (sWidth % width == 0) {  
                    cols = sWidth / width;  
                } else {  
                    cols = sWidth / width + 1;  
                }  
                eWidth = sWidth - width;  
                if (sHeight % height == 0) {  
                    rows = sHeight / height;  
                } else {  
                    eHeight = sHeight % height;  
                    rows = sHeight / height + 1;  
                }  
                rows = 1;
                cols = 2;
                String fileName = null;  
                File file = new File(targetDir);  
                if (!file.exists()) { // 锟芥储目录锟斤拷锟斤拷锟节ｏ拷锟津创斤拷目录  
                    file.mkdirs();  
                }  
                BufferedImage image = null;  
                int cWidth = 0; // 锟斤拷前锟斤拷片锟斤拷锟�  
                int cHeight = 0; // 锟斤拷前锟斤拷片锟竭讹拷  
                for (int i = rows-1; i>=0; i--) {  
                    for (int j = cols-1; j >= 0; j--) {  
                        cWidth = getWidth(j, cols, eWidth, width);  
                        cHeight = getHeight(i, rows, eHeight, height);  
                        // x锟斤拷锟斤拷,y锟斤拷锟斤拷,锟斤拷锟�,锟竭讹拷  
                        image = source.getSubimage(j * width, i * height, cWidth,cHeight);  
                        fileName = targetDir + "/map_" + getName(count)+ "."+formatname;  
                        count++;
                        file = new File(fileName);  
                        ImageIO.write(image, formatname, file); 
                    }  
                }  
            }  
        }else{
        	BufferedImage image = source.getSubimage(0, 0, sWidth,sHeight);  
            String fileName = targetDir + "/map_" + getName(count)+ "."+formatname;  
            count++;
            File file = new File(fileName);  
            ImageIO.write(image, formatname, file);  
        }
        return count;
    }  
	
	public int cut(File sourceFile, String targetDir, int width, int height,String formatname,int count)  
            throws Exception {    
		BufferedImage source = ImageIO.read(sourceFile);  
        if(source==null){
        	return count;
        }
		int sWidth = source.getWidth(); // 图片锟斤拷锟�  
        int sHeight = source.getHeight(); // 图片锟竭讹拷  
        height = sHeight;
        if(sWidth>=width*2){
        	width = sWidth/2;
        	if (sWidth >=width && sHeight >=height) {  
                int cols = 0; // 锟斤拷锟斤拷锟斤拷片锟斤拷锟斤拷  
                int rows = 0; // 锟斤拷锟斤拷锟斤拷片锟斤拷锟斤拷  
                int eWidth = 0; // 末锟斤拷锟斤拷片锟斤拷锟�  
                int eHeight = 0; // 末锟斤拷锟斤拷片锟竭讹拷  
                if (sWidth % width == 0) {  
                    cols = sWidth / width;  
                } else {  
                    cols = sWidth / width + 1;  
                }  
                eWidth = sWidth - width;  
                if (sHeight % height == 0) {  
                    rows = sHeight / height;  
                } else {  
                    eHeight = sHeight % height;  
                    rows = sHeight / height + 1;  
                }  
                rows = 1;
                cols = 2;
                String fileName = null;  
                File file = new File(targetDir);  
                if (!file.exists()) { // 锟芥储目录锟斤拷锟斤拷锟节ｏ拷锟津创斤拷目录  
                    file.mkdirs();  
                }  
                BufferedImage image = null;  
                int cWidth = 0; // 锟斤拷前锟斤拷片锟斤拷锟�  
                int cHeight = 0; // 锟斤拷前锟斤拷片锟竭讹拷  
                for (int i = rows-1; i>=0; i--) {  
                    for (int j = cols-1; j >= 0; j--) {  
                        cWidth = getWidth(j, cols, eWidth, width);  
                        cHeight = getHeight(i, rows, eHeight, height);  
                        // x锟斤拷锟斤拷,y锟斤拷锟斤拷,锟斤拷锟�,锟竭讹拷  
                        image = source.getSubimage(j * width, i * height, cWidth,cHeight);  
                        fileName = targetDir + "/map_" + getName(count)+ "."+formatname;  
                        count++;
                        file = new File(fileName);  
                        ImageIO.write(image, formatname, file); 
                    }  
                }  
            }  
        }else{
        	BufferedImage image = source.getSubimage(0, 0, sWidth,sHeight);  
            String fileName = targetDir + "/map_" + getName(count)+ "."+formatname;  
            count++;
            File file = new File(fileName);  
            ImageIO.write(image, formatname, file);  
        }
        return count;
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
	
    /** 
     * 锟叫革拷图片 
     *  
     * @param sourceFile 
     *            源锟侥硷拷 
     * @param targetDir 
     *            锟芥储目录 
     * @param width 
     *            锟斤拷片锟斤拷锟� 
     * @param height 
     *            锟斤拷片锟竭讹拷 
     * @return 
     * @throws Exception 
     */  
    public void cut(File sourceFile, String targetDir, int width, int height,String formatname)  
            throws Exception {  
        List<File> list = new ArrayList<File>();  
        BufferedImage source = ImageIO.read(sourceFile);  
        int sWidth = source.getWidth(); // 图片锟斤拷锟�  
        int sHeight = source.getHeight(); // 图片锟竭讹拷  
        if (sWidth >=width && sHeight >=height) {  
            int cols = 0; // 锟斤拷锟斤拷锟斤拷片锟斤拷锟斤拷  
            int rows = 0; // 锟斤拷锟斤拷锟斤拷片锟斤拷锟斤拷  
            int eWidth = 0; // 末锟斤拷锟斤拷片锟斤拷锟�  
            int eHeight = 0; // 末锟斤拷锟斤拷片锟竭讹拷  
            if (sWidth % width == 0) {  
                cols = sWidth / width;  
            } else {  
                eWidth = sWidth % width;  
                cols = sWidth / width + 1;  
            }  
            if (sHeight % height == 0) {  
                rows = sHeight / height;  
            } else {  
                eHeight = sHeight % height;  
                rows = sHeight / height + 1;  
            }  
            String fileName = null;  
            File file = new File(targetDir);  
            if (!file.exists()) { // 锟芥储目录锟斤拷锟斤拷锟节ｏ拷锟津创斤拷目录  
                file.mkdirs();  
            }  
            BufferedImage image = null;  
            int cWidth = 0; // 锟斤拷前锟斤拷片锟斤拷锟�  
            int cHeight = 0; // 锟斤拷前锟斤拷片锟竭讹拷  
            for (int i = 0; i < rows; i++) {  
                for (int j = 0; j < cols; j++) {  
                    cWidth = getWidth(j, cols, eWidth, width);  
                    cHeight = getHeight(i, rows, eHeight, height);  
                    // x锟斤拷锟斤拷,y锟斤拷锟斤拷,锟斤拷锟�,锟竭讹拷  
                    image = source.getSubimage(j * width, i * height, cWidth,cHeight);  
                    fileName = targetDir + "/map_" + i + "_" + j + "."+formatname;  
                    file = new File(fileName);  
                    System.out.println(fileName);
                    ImageIO.write(image, "png", file);  
                    list.add(file);  
                }  
            }  
        }  
    }  
    /** 
     * 锟叫革拷图片 
     *  
     * @param source 
     *            源锟侥硷拷路锟斤拷 
     * @param targetDir 
     *            锟芥储目录 
     * @param width 
     *            锟斤拷片锟斤拷锟� 
     * @param height 
     *            锟斤拷片锟竭讹拷 
     * @return 
     * @throws Exception 
     */  
    public void cut(String source, String targetDir, int width, int height,String formatname)  
            throws Exception {  
         cut(new File(source), targetDir, width, height,formatname);  
    }  
    /** 
     * 锟斤拷取锟斤拷前锟斤拷片锟侥匡拷锟� 
     *  
     * @param index 
     *            锟斤拷锟斤拷锟斤拷锟斤拷 
     * @param cols 
     *            锟斤拷锟斤拷锟斤拷片锟斤拷锟斤拷 
     * @param endWidth 
     *            末锟斤拷锟斤拷片锟斤拷锟� 
     * @param width 
     *            锟斤拷片锟斤拷锟� 
     * @return 
     */  
    private int getWidth(int index, int cols, int endWidth, int width) {  
        if (index == cols - 1) {  
            if (endWidth != 0) {  
                return endWidth;  
            }  
        }  
        return width;  
    }  
    /** 
     * 锟斤拷取锟斤拷前锟斤拷片锟侥高讹拷 
     *  
     * @param index 
     *            锟斤拷锟斤拷锟斤拷锟斤拷 
     * @param rows 
     *            锟斤拷锟斤拷锟斤拷片锟斤拷锟斤拷 
     * @param endHeight 
     *            末锟斤拷锟斤拷片锟竭讹拷 
     * @param height 
     *            锟斤拷片锟竭讹拷 
     * @return 
     */  
    private int getHeight(int index, int rows, int endHeight, int height) {  
        if (index == rows - 1) {  
            if (endHeight != 0) {  
                return endHeight;  
            }  
        }  
        return height;  
    }  
}  