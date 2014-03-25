package com.niubi;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.imageio.plugins.gif.GIFImageReader;
import com.sun.imageio.plugins.gif.GIFImageReaderSpi;


public class RemoveNoise {
	
	//RGB = Count
	public static HashMap<Integer, Integer> rgbCountMap = new HashMap<Integer, Integer>();
	
	public static TreeMap<Integer, Integer> countRGBMap = new TreeMap<Integer, Integer>();
	
	public static TreeSet<Integer> rgbSet = new TreeSet<Integer>();
	
	public static TreeSet<Integer> countSet = new TreeSet<Integer>();
	
	public static void main(String[] args) throws Exception{

		//removeNoiseTest();
		
//		List<BufferedImage> frames = getFrames(new File("D:\\Tools\\Tesseract\\xiaomi\\1395497042499.gif"));
//		
//		int i = 0;
//		for (BufferedImage bufferedImage : frames) {
//			ImageIO.write(bufferedImage, "png", new File("D:\\Tools\\Tesseract\\xiaomi\\1395497042499_" + i + ".png"));
//			
//			i++;
//		}
		
		String baseDir = "D:\\Tools\\Tesseract\\xiaomi";
		File[] gifs = new File(baseDir).listFiles(new GIFfileFilter());
		
		for (int i = 0; i < gifs.length; i++) {
			
			File f = new File(baseDir + "\\" + gifs[i].getName().substring(0, gifs[i].getName().length() -4));
			if (!f.exists()) {
				f.mkdir();
			}
			
			generateAllFrames(gifs[i], f);
		}
	}
	
	public static void removeNoiseTest() throws Exception{
		
		File testFile = new File("D:\\data\\Jerry\\XiaoMi_OCR\\1395497131866.gif.png");
		
		BufferedImage image = ImageIO.read(testFile);
		int height = image.getHeight();
		int width = image.getWidth();
		
		
		
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				
				Integer rgb = image.getRGB(i, j);
				Integer count = rgbCountMap.get(rgb) == null ? 0 : rgbCountMap.get(rgb);
				count ++;
				
				Color color = new Color(rgb);
				rgbCountMap.put(rgb, count);
				rgbSet.add(rgb);
				
			}
		}
		
		countSet.addAll(rgbCountMap.values());
		
		for (Map.Entry<Integer, Integer> entry : rgbCountMap.entrySet()) {
			countRGBMap.put(entry.getValue(), entry.getKey());
		}
		
		System.out.println(countRGBMap.toString());
		System.out.println(countRGBMap.size());
		
		for (Iterator<Map.Entry<Integer, Integer>> iterator = rgbCountMap.entrySet().iterator(); iterator.hasNext();) {
			
			Entry<Integer, Integer> entry = iterator.next();
			BufferedImage newImage = new BufferedImage(width, height, image.getType());
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					newImage.setRGB(i, j, entry.getKey());
				}
			}
			
			ImageIO.write(newImage, "png", new File("" + entry.getValue() + ".png"));
		}
	}
	
	/**
	 * not work
	 * @param gif
	 * @return
	 * @throws IOException
	 */
	public static ArrayList<BufferedImage> getFrames(File gif) throws IOException{
	    ArrayList<BufferedImage> frames = new ArrayList<BufferedImage>();
	    ImageReader ir = new GIFImageReader(new GIFImageReaderSpi());
	    ir.setInput(ImageIO.createImageInputStream(gif));
	    for(int i = 0; i < ir.getNumImages(true); i++)
	        frames.add(ir.getRawImageType(i).createBufferedImage(ir.getWidth(i), ir.getHeight(i)));
	    return frames;
	}

	/**
	 * 
	 * @param gif
	 * @param png
	 */
	public static void generateAllFrames(File gif, File png) {

		try {

			String[] imageatt = new String[] {"imageLeftPosition", "imageTopPosition", "imageWidth", "imageHeight"};

			ImageReader reader = (ImageReader)ImageIO.getImageReadersByFormatName("gif").next();
			ImageInputStream ciis = ImageIO.createImageInputStream(gif);
			reader.setInput(ciis, false);

			int noi = reader.getNumImages(true);

			BufferedImage master = null;

			for (int i = 0; i < noi; i++) {

				BufferedImage image = reader.read(i);
				IIOMetadata metadata = reader.getImageMetadata(i);

				Node tree = metadata.getAsTree("javax_imageio_gif_image_1.0");

				NodeList children = tree.getChildNodes();

				for (int j = 0; j < children.getLength(); j++) {

					Node nodeItem = children.item(j);

					if (nodeItem.getNodeName().equals("ImageDescriptor")) {

						Map<String, Integer> imageAttr = new HashMap<String, Integer>();

						for (int k = 0; k < imageatt.length; k++) {

							NamedNodeMap attr = nodeItem.getAttributes();

							Node attnode = attr.getNamedItem(imageatt[k]);

							imageAttr.put(imageatt[k], Integer.valueOf(attnode.getNodeValue()));

						}

						if (i == 0) {
							master = new BufferedImage(imageAttr.get("imageWidth"), imageAttr.get("imageHeight"), BufferedImage.TYPE_INT_ARGB);

						}
						master.getGraphics().drawImage(image, imageAttr.get("imageLeftPosition"), imageAttr.get("imageTopPosition"), null);

					}
				}

				ImageIO.write(master, "GIF", new File(png.getAbsolutePath() + "\\" + png.getName() + "_" + i + ".png"));

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
