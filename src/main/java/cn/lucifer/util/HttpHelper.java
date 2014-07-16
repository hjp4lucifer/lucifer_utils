package cn.lucifer.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 
 * @author Lucifer
 * 
 */
public class HttpHelper {

	/**
	 * 下载图片bitmap
	 * 
	 * @param url
	 * @return
	 */
	public static byte[] httpGet(URL url) {
		if (url == null) {
			return null;
		}
		byte[] imgData = null;

		try {
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();

			int length = conn.getContentLength();
			if (length != -1) {
				imgData = new byte[length];
				byte[] temp = new byte[512];
				int readLen = 0;
				int destPos = 0;
				while ((readLen = is.read(temp)) > 0) {
					System.arraycopy(temp, 0, imgData, destPos, readLen);
					destPos += readLen;
				}
			}
			is.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return imgData;
	}
}
