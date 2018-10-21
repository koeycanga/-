package com;

import java.io.File;

public class Test {

	public static void main(String[] args) {
		
		String s = "http://comic2.kukudm.com/comiclist/892/comiclist/892/16870/1.htm";
		
		String[] ss = s.split("/");
		
		for(int i=0;i<ss.length;i++) {
			System.out.println("i="+i+" "+ss[i]);
		}
	}
}
