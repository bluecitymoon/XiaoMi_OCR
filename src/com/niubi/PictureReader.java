package com.niubi;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class PictureReader {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{

		File testFile = new File("/Users/jerryjiang/Downloads/xiaomi/xiaomi/1395497042499/1395497042499_0.png");
		BufferedImage bufferedImage = ImageIO.read(testFile);
		
		System.out.println(bufferedImage.getRGB(1, 2));
		
		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();
		
		for (int i = 0; i < width; i ++) {
			for (int j =0; j < height; j ++) {
				int rgb = bufferedImage.getRGB(i, j);
				Color color = new Color(rgb);
				System.out.println("[" + i + "][" + j + "]=[r=" + color.getRed() + "][g="+ color.getRed() +"][b=" + color.getBlue() + "]");
			}
		}
		
	}

}
