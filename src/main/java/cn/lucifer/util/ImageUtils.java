package cn.lucifer.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.FileImageOutputStream;

public class ImageUtils {

	/**
	 * Crop an image
	 * 
	 * @param originalImage
	 *            The image file
	 * @param to
	 *            The destination file
	 * @param x1
	 *            The new x origin
	 * @param y1
	 *            The new y origin
	 * @param x2
	 *            The new x end
	 * @param y2
	 *            The new y end
	 */
	public static void crop(File originalImage, File to, int x1, int y1,
			int x2, int y2) {
		try {
			BufferedImage source = ImageIO.read(originalImage);
			crop(source, to, x1, y1, x2, y2);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * Crop an image
	 * 
	 * @param originalImage
	 *            The image file
	 * @param to
	 *            The destination file
	 * @param x1
	 *            The new x origin
	 * @param y1
	 *            The new y origin
	 * @param x2
	 *            The new x end
	 * @param y2
	 *            The new y end
	 */
	public static void crop(BufferedImage source, File to, int x1, int y1,
			int x2, int y2) {
		try {
			String mimeType = "image/jpeg";
			if (to.getName().endsWith(".png")) {
				mimeType = "image/png";
			}
			if (to.getName().endsWith(".gif")) {
				mimeType = "image/gif";
			}
			int width = x2 - x1;
			int height = y2 - y1;

			// out
			BufferedImage dest = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			Image croppedImage = source.getSubimage(x1, y1, width, height);
			Graphics graphics = dest.getGraphics();
			graphics.setColor(Color.WHITE);
			graphics.fillRect(0, 0, width, height);
			graphics.drawImage(croppedImage, 0, 0, null);
			ImageWriter writer = ImageIO.getImageWritersByMIMEType(mimeType)
					.next();
			writer.setOutput(new FileImageOutputStream(to));
			IIOImage image = new IIOImage(dest, null, null);
			writer.write(null, image, createImageWriteParam(writer));

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public static void crop4Square(BufferedImage source, File to, int size) {
		try {
			String mimeType = "image/jpeg";
			if (to.getName().endsWith(".png")) {
				mimeType = "image/png";
			}
			if (to.getName().endsWith(".gif")) {
				mimeType = "image/gif";
			}
			int width = source.getWidth();
			int height = source.getHeight();
			Image croppedImage;
			if (width == height) {
				croppedImage = source.getScaledInstance(size, size,
						Image.SCALE_SMOOTH);
			} else {
				int left, top;
				if (width > height) {
					left = (width - height) / 2;
					top = 0;
					croppedImage = source
							.getSubimage(left, top, height, height);
				} else {
					left = 0;
					top = (height - width) / 2;
					croppedImage = source.getSubimage(left, top, width, width);
				}
				croppedImage = croppedImage.getScaledInstance(size, size,
						Image.SCALE_SMOOTH);
			}

			// out
			BufferedImage dest = new BufferedImage(size, size,
					BufferedImage.TYPE_INT_RGB);
			Graphics graphics = dest.getGraphics();
			graphics.setColor(Color.WHITE);
			graphics.fillRect(0, 0, size, size);
			graphics.drawImage(croppedImage, 0, 0, null);
			ImageWriter writer = ImageIO.getImageWritersByMIMEType(mimeType)
					.next();
			writer.setOutput(new FileImageOutputStream(to));
			IIOImage image = new IIOImage(dest, null, null);
			writer.write(null, image, createImageWriteParam(writer));

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * Resize an image
	 * 
	 * @param originalImage
	 *            The image file
	 * @param to
	 *            The destination file
	 * @param w
	 *            The new width (or -1 to proportionally resize)
	 * @param h
	 *            The new height (or -1 to proportionally resize)
	 */
	public static void resize(File originalImage, File to, int w, int h) {
		try {
			BufferedImage source = ImageIO.read(originalImage);
			resize(source, to, w, h);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * Resize an image
	 * 
	 * @param source
	 *            The image is read
	 * @param to
	 *            The destination file
	 * @param w
	 *            The new width (or -1 to proportionally resize)
	 * @param h
	 *            The new height (or -1 to proportionally resize)
	 */
	public static void resize(BufferedImage source, File to, int w, int h) {
		try {
			int owidth = source.getWidth();
			int oheight = source.getHeight();
			double ratio = (double) owidth / oheight;

			if (w < 0 && h < 0) {
				w = owidth;
				h = oheight;
			}
			if (w < 0 && h > 0) {
				w = (int) (h * ratio);
			}
			if (w > 0 && h < 0) {
				h = (int) (w / ratio);
			}

			String mimeType = "image/jpeg";
			if (to.getName().endsWith(".png")) {
				mimeType = "image/png";
			}
			if (to.getName().endsWith(".gif")) {
				mimeType = "image/gif";
			}

			// out
			BufferedImage dest = new BufferedImage(w, h,
					BufferedImage.TYPE_INT_RGB);
			Image srcSized = source.getScaledInstance(w, h, Image.SCALE_SMOOTH);
			Graphics graphics = dest.getGraphics();
			graphics.setColor(Color.WHITE);
			graphics.fillRect(0, 0, w, h);
			graphics.drawImage(srcSized, 0, 0, null);
			ImageWriter writer = ImageIO.getImageWritersByMIMEType(mimeType)
					.next();
			FileImageOutputStream toFs = new FileImageOutputStream(to);
			writer.setOutput(toFs);
			IIOImage image = new IIOImage(dest, null, null);
			writer.write(null, image, createImageWriteParam(writer));
			toFs.flush();
			toFs.close();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * Resize an image
	 * 
	 * @param originalImage
	 *            The image file
	 * @param to
	 *            The destination file
	 * @param w
	 *            The new width (or -1 to proportionally resize)
	 */
	public static void resize4Square(File originalImage, File to, int w) {
		try {
			BufferedImage source = ImageIO.read(originalImage);
			resize4Square(source, to, w);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Resize an image
	 * 
	 * @param source
	 *            The image is read
	 * @param to
	 *            The destination file
	 * @param w
	 *            The new width (or -1 to proportionally resize)
	 */
	public static void resize4Square(BufferedImage source, File to, int w) {
		try {
			int owidth = source.getWidth();
			int oheight = source.getHeight();
			double ratio = (double) owidth / oheight;

			int rwidth, rheight;
			int x = 0, y = 0;
			if (owidth > oheight) {
				if (owidth >= w) {
					rwidth = w;
					rheight = (int) (w / ratio);
					x = 0;
					y = (w - rheight) >> 1;
				} else {
					rwidth = owidth;
					rheight = oheight;
					x = (w - rwidth) >> 1;
					y = (w - rheight) >> 1;
				}
			} else {
				if (oheight >= w) {
					rwidth = (int) (w * ratio);
					rheight = w;
					x = (w - rwidth) >> 1;
					y = 0;
				} else {
					rwidth = owidth;
					rheight = oheight;
					x = (w - rwidth) >> 1;
					y = (w - rheight) >> 1;
				}
			}

			String mimeType = "image/jpeg";
			if (to.getName().endsWith(".png")) {
				mimeType = "image/png";
			}
			if (to.getName().endsWith(".gif")) {
				mimeType = "image/gif";
			}

			// out
			BufferedImage dest = new BufferedImage(w, w,
					BufferedImage.TYPE_INT_RGB);
			Image srcSized = source.getScaledInstance(rwidth, rheight,
					Image.SCALE_SMOOTH);
			Graphics graphics = dest.getGraphics();
			graphics.setColor(new Color(225, 225, 225));
			graphics.fillRect(0, 0, w, w);
			graphics.drawImage(srcSized, x, y, null);
			ImageWriter writer = ImageIO.getImageWritersByMIMEType(mimeType)
					.next();
			FileImageOutputStream toFs = new FileImageOutputStream(to);
			writer.setOutput(toFs);
			IIOImage image = new IIOImage(dest, null, null);
			writer.write(null, image, createImageWriteParam(writer));
			toFs.flush();
			toFs.close();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取最大限定值, 但不超过给予的限制
	 * 
	 * @param width
	 * @param height
	 * @param limitSize
	 *            最大限定尺寸
	 * @return
	 */
	public static int getMaxInLimit(int width, int height, int limitSize) {
		int max_size;
		if (height > width) {
			max_size = height;
		} else {
			max_size = width;
		}
		if (max_size > limitSize) {
			max_size = limitSize;
		}
		return max_size;
	}

	/**
	 * 
	 * @param source
	 *            原图片
	 * @param to
	 *            生成的图片 路径
	 * @param cover
	 *            封面图片
	 * @param width
	 *            目标图片生成的宽
	 * @param height
	 *            目标图片生成的高
	 * @param sourceTop
	 *            原图片在新生成图片中的top值
	 * @param sourceLeft
	 *            原图片在新生成图片中的left值
	 * @param sourceWidth
	 *            原图片在新生成图片中的width值
	 * @param sourceHeight
	 *            原图片在新生成图片中的height值
	 */
	public static void cover(BufferedImage source, File to, Image cover,
			int width, int height, int sourceTop, int sourceLeft,
			int sourceWidth, int sourceHeight) {
		BufferedImage dest = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Image imageSource = source.getScaledInstance(sourceWidth, sourceHeight,
				Image.SCALE_SMOOTH);
		Graphics graphics = dest.getGraphics();
		graphics.setColor(new Color(225, 225, 225));
		graphics.fillRect(0, 0, width, height);
		graphics.drawImage(imageSource, sourceTop, sourceLeft, null);
		graphics.drawImage(cover, 0, 0, null);

		String mimeType = "image/jpeg";
		ImageWriter writer = ImageIO.getImageWritersByMIMEType(mimeType).next();
		try {
			FileImageOutputStream toFs = new FileImageOutputStream(to);
			writer.setOutput(toFs);
			IIOImage iioImage = new IIOImage(dest, null, null);
			writer.write(null, iioImage, createImageWriteParam(writer));
			toFs.flush();
			toFs.close();
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public static void crop4SquareWithCover(BufferedImage source, File to,
			Image cover, int size) {
		try {
			String mimeType = "image/jpeg";
			if (to.getName().endsWith(".png")) {
				mimeType = "image/png";
			}
			if (to.getName().endsWith(".gif")) {
				mimeType = "image/gif";
			}
			int width = source.getWidth();
			int height = source.getHeight();
			Image croppedImage;
			if (width == height) {
				croppedImage = source.getScaledInstance(size, size,
						Image.SCALE_SMOOTH);
			} else {
				int left, top;
				if (width > height) {
					left = (width - height) / 2;
					top = 0;
					croppedImage = source
							.getSubimage(left, top, height, height);
				} else {
					left = 0;
					top = (height - width) / 2;
					croppedImage = source.getSubimage(left, top, width, width);
				}
				croppedImage = croppedImage.getScaledInstance(size, size,
						Image.SCALE_SMOOTH);
			}

			// out
			BufferedImage dest = new BufferedImage(size, size,
					BufferedImage.TYPE_INT_RGB);
			Graphics graphics = dest.getGraphics();
			graphics.setColor(Color.WHITE);
			graphics.fillRect(0, 0, size, size);
			graphics.drawImage(croppedImage, 0, 0, null);
			graphics.drawImage(cover, 0, 0, null);
			ImageWriter writer = ImageIO.getImageWritersByMIMEType(mimeType)
					.next();
			writer.setOutput(new FileImageOutputStream(to));
			IIOImage image = new IIOImage(dest, null, null);
			writer.write(null, image, createImageWriteParam(writer));

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public static ImageWriteParam createImageWriteParam(ImageWriter writer) {
		ImageWriteParam params = writer.getDefaultWriteParam();
		// JPEGImageWriteParam jpegImageWriteParam = new
		// JPEGImageWriteParam(writer.getLocale());
		// jpegImageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		// jpegImageWriteParam.setCompressionQuality(0.8F);
		params.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		params.setCompressionQuality(0.95F);
		return params;
	}
}
