package cn.lucifer.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.webp.libwebp;

/**
 * 会调用jni, 请自行搭配环境
 * 
 * @author Lucifer
 *
 */
public class WebpUtil {
	public static final Log log = LogFactory.getLog("WebpUtil");

	static {
		System.out.println(System.getProperty("java.library.path"));
		System.loadLibrary("webp_jni");
	}

	protected final float jpeg_quality;
	protected final int webp_quality;

	public WebpUtil() {
		jpeg_quality = 0.85f;
		webp_quality = 85;
	}

	/**
	 * 
	 * @param jpeg_quality
	 *            85%质量传0.85f
	 * @param webp_quality
	 *            85%质量传85
	 */
	public WebpUtil(float jpeg_quality, int webp_quality) {
		this.jpeg_quality = jpeg_quality;
		this.webp_quality = webp_quality;
	}

	public ByteArrayOutputStream decodeWebpToJpeg(byte[] webpData)
			throws IOException {
		long start = System.currentTimeMillis();
		int[] width = new int[1];
		int[] height = new int[1];
		libwebp.WebPGetInfo(webpData, webpData.length, width, height);

		byte[] rgbData = libwebp.WebPDecodeRGB(webpData, webpData.length,
				width, height);
		BufferedImage img = new BufferedImage(width[0], height[0],
				BufferedImage.TYPE_INT_RGB);
		int[] rgbArray = getRGB(rgbData);
		img.setRGB(0, 0, width[0], height[0], rgbArray, 0, width[0]);

		ByteArrayOutputStream output = new ByteArrayOutputStream(65536);
		MemoryCacheImageOutputStream outputBuff = new MemoryCacheImageOutputStream(output); 
		
		ImageWriter writer = ImageIO.getImageWritersByMIMEType("image/jpeg")
				.next();

		writer.setOutput(outputBuff);
		IIOImage image = new IIOImage(img, null, null);
		writer.write(null, image, createImageWriteParam(writer));

		if (log.isDebugEnabled()) {
			log.debug(String.format(
					"decodeWebpToJpeg: width=%d, height=%d, costTime=%d",
					width[0], height[0], (System.currentTimeMillis() - start)));
		}
		return output;
	}

	public static int[] getRGB(byte[] rgbData) {
		int[] rgbArray = new int[rgbData.length / 3];
		for (int i = 0, j = 0, len = rgbArray.length; j < len; j++) {
			rgbArray[j] = ((int) rgbData[i++] << 16 & 0xff0000)
					+ ((int) rgbData[i++] << 8 & 0xff00)
					+ ((int) rgbData[i++] & 0xff);
		}
		return rgbArray;
	}

	public byte[] encodeToWebp(InputStream input) throws IOException {
		long start = System.currentTimeMillis();
		BufferedImage image = ImageIO.read(input);
		int width = image.getWidth();
		int height = image.getHeight();

		byte[] bgraData = getBGRA(image);
		byte[] outputData = libwebp.WebPEncodeBGRA(bgraData, width, height,
				width * 4, webp_quality);

		if (log.isDebugEnabled()) {
			log.debug(String.format(
					"encodeToWebp: width=%d, height=%d, costTime=%d", width,
					height, (System.currentTimeMillis() - start)));
		}
		return outputData;
	}

	public static byte[] getBGRA(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		byte[] bgraData = new byte[width * height * 4];
		int rgb = 0;
		int index = 0;

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				rgb = image.getRGB(x, y);
				bgraData[index++] = (byte) (rgb & 0xff);
				bgraData[index++] = (byte) (rgb >> 8 & 0xff);
				bgraData[index++] = (byte) (rgb >> 16 & 0xff);
				bgraData[index++] = (byte) (rgb >> 24 & 0xff);
			}
		}
		return bgraData;
	}

	public ImageWriteParam createImageWriteParam(ImageWriter writer) {
		ImageWriteParam params = writer.getDefaultWriteParam();
		params.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		params.setCompressionQuality(jpeg_quality);
		return params;
	}
}
