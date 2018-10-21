package utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageInputStream;

import net.sf.javavp8decoder.tools.WebPViewer;

public class ImageUtil {

	
	public static void Webp2Jpg(File oldf,File newf){
		
		//System.out.println(System.getProperty("java.library.path"));  
		
		try {  
		          
		    BufferedImage im = ImageIO.read(oldf);   
		    ImageIO.write(im, "jpg", newf);  
		              
		              
		} catch (IOException e) {  
		    e.printStackTrace();  
		}  
		
		
	}
}
