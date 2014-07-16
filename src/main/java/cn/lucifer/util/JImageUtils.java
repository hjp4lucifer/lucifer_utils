package cn.lucifer.util;

import java.io.File;

import magick.ImageInfo;
import magick.MagickException;
import magick.MagickImage;

/**
 * jmagick工具类
 * 
 * @author Lucifer
 * 
 */
public class JImageUtils {

	public static String modulateImage(File file) throws MagickException {
		return modulateImage(file, "105");
	}

	/**
	 * 
	 * @param file
	 * @param modulate
	 *            100表示没有增加/减少
	 * @return
	 * @throws MagickException
	 */
	protected static String modulateImage(File file, String modulate)
			throws MagickException {
		String outFileName = file.getAbsolutePath() + "_up.jpg";
		File outFile = new File(outFileName);
		if (outFile.exists()) {
			return outFileName;
		}
		ImageInfo imageInfo = new ImageInfo(file.getAbsolutePath());
		// imageInfo.setQuality(100);
		MagickImage image = new MagickImage(imageInfo);
		image.setFileName(outFileName);
		image.modulateImage(modulate);
		image.writeImage(imageInfo);
		image.destroyImages();
		return outFileName;
	}
}
