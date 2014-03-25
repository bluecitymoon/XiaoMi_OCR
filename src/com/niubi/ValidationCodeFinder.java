package com.niubi;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

import net.sourceforge.tess4j.Tesseract;


public class ValidationCodeFinder {

	public static String BASE_DIR = "D:\\Tools\\Tesseract";
	
	public static Set<String> pngFiles = new HashSet<String>();

	public static void main(String[] args) throws IOException, Exception {

		GIF2PNG();
		
		for (String file : pngFiles) {
//			String absoluteFile = System.getProperty("user.dir") + File.separator + file;
//			String ocrResultFile =  System.getProperty("user.dir") + File.separator + file +".txt";
//			String cmd = "cmd /c tesseract " + absoluteFile + " " + ocrResultFile;
//			System.out.println(cmd);
//			Runtime.getRuntime().exec(cmd);
			
		//	String result = Tesseract.getInstance().doOCR(new File(absoluteFile));
			
		//	System.out.println(result);
		}
		

	}

	private static void GIF2PNG() throws IOException {

		String XIAOMI_PICTURES = BASE_DIR + "\\xiaomi";

		File[] images = new File(XIAOMI_PICTURES).listFiles(new GIFfileFilter());
		for (File image : images) {
			ImageIO.write(ImageIO.read(image), "png", new File(image.getName() + ".png"));
			
			pngFiles.add(image.getName() + ".png");
		}

	}
}
