package cn.lucifer.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;

/**
 * 
 * @author lucifer
 */
public class HttpHelper {

	static {
		try {
			trustAllHttpsCertificates();
		} catch (KeyManagementException | NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}

		HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		});
	}

	private static void trustAllHttpsCertificates() throws NoSuchAlgorithmException, KeyManagementException {
		javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
		javax.net.ssl.TrustManager tm = new MiTM();
		trustAllCerts[0] = tm;
		javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, null);
		javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	}

	static class MiTM implements javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager {
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public boolean isServerTrusted(java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public boolean isClientTrusted(java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
			return;
		}

		public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
			return;
		}
	}

	public final static int CONNECT_TIMEOUT = 30000;

	public static byte[] httpGet(String url, int connectTimeout) throws IOException, HttpClientException {
		return http(url, HttpMethod.GET, null, null, connectTimeout);
	}

	public static byte[] httpPost(String url, InputStream input, int connectTimeout)
			throws IOException, HttpClientException {
		return http(url, HttpMethod.POST, null, input, connectTimeout);
	}

	public static byte[] http(String url, HttpMethod method, Map<String, String> httpHeads, InputStream input)
			throws IOException, HttpClientException {
		return http(url, method, httpHeads, input, CONNECT_TIMEOUT);
	}

	public static byte[] http(String url, HttpMethod method, Map<String, String> httpHeads, InputStream input,
			int connectTimeout) throws IOException, HttpClientException {
		return http(url, method, httpHeads, input, connectTimeout, null);
	}

	public static byte[] http(String url, HttpMethod method, Map<String, String> httpHeads, InputStream input,
			int connectTimeout, Map<String, List<String>> responseHeads) throws IOException, HttpClientException {
		return http(url, method, httpHeads, input, connectTimeout, responseHeads, null, connectTimeout);
	}

	public static byte[] http(String url, HttpMethod method, Map<String, String> httpHeads, InputStream input,
			int connectTimeout, Map<String, List<String>> responseHeads, OutputStream outputStream, int readTimeout)
			throws IOException, HttpClientException {
		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
		if (method != HttpMethod.DELETE) {
			conn.setDoInput(true);
		}
		if (method != HttpMethod.GET) {
			conn.setDoOutput(true);
		}

		conn.setRequestMethod(method.toString());

		conn(conn, httpHeads, connectTimeout, readTimeout);
		if (null != input) {
			OutputStream output = conn.getOutputStream();
			IOUtils.copy(input, output);
			output.flush();
			IOUtils.closeQuietly(output);
		}
		if (null == responseHeads) {
			responseHeads = new HashMap<>();
		}
		responseHeads.putAll(conn.getHeaderFields());

		int httpStatus = conn.getResponseCode();

		// 支持重定向
		if (httpStatus == HttpStatus.SC_MOVED_PERMANENTLY || httpStatus == HttpStatus.SC_MOVED_TEMPORARILY) {
			String newUrl = process301(conn);
			if (null == newUrl) {
				conn.disconnect();
				String msg = String.format("【%s】get data from url=%s fail, http status=%d", method.toString(), url,
						httpStatus);
				throw new HttpClientException(httpStatus, msg);
			}
			return http(newUrl, HttpMethod.GET, httpHeads, null, connectTimeout);
		}

		if (httpStatus != HttpStatus.SC_OK) {
			String msg = String.format("【%s】get data from url=%s fail, http status=%d", method.toString(), url,
					httpStatus);
			throw new HttpClientException(httpStatus, msg);
		}
		if (method == HttpMethod.DELETE) {
			return new byte[0];
		}

		List<String> rspEncoding = responseHeads.get("Content-Encoding");
		if (null != rspEncoding) {
			for (String s : rspEncoding) {
				if ("gzip".equals(s)) {
					if (null == outputStream) {
						return getGizpResponse(conn);
					} else {
						getGizpResponse(conn, outputStream);
						return null;
					}
				}
			}
		}

		if (null == outputStream) {
			return getResponse(conn);
		} else {
			getResponse(conn, outputStream);
			return null;
		}
	}

	protected static String process301(HttpURLConnection conn) {
		Map<String, List<String>> responseHeads = new HashMap<>(conn.getHeaderFields());
		List<String> locations = responseHeads.get("Location");
		if (null != locations) {
			for (String s : locations) {
				if (s.startsWith("http")) {
					return s;
				}
			}
		}

		return null;
	}

	protected static byte[] getResponse(HttpURLConnection conn) throws IOException {
		InputStream input = conn.getInputStream();
		byte[] response = IOUtils.toByteArray(input);

		IOUtils.closeQuietly(input);
		conn.disconnect();
		return response;
	}

	protected static void getResponse(HttpURLConnection conn, OutputStream output) throws IOException {
		InputStream input = conn.getInputStream();
		copy(input, output);

		IOUtils.closeQuietly(input);
		conn.disconnect();
		IOUtils.closeQuietly(output);
	}

	protected static byte[] getGizpResponse(HttpURLConnection conn) throws IOException {
		InputStream input = new GZIPInputStream(conn.getInputStream());
		byte[] response = IOUtils.toByteArray(input);

		IOUtils.closeQuietly(input);
		conn.disconnect();
		return response;
	}

	protected static void getGizpResponse(HttpURLConnection conn, OutputStream output) throws IOException {
		InputStream input = new GZIPInputStream(conn.getInputStream());
		copy(input, output);

		IOUtils.closeQuietly(input);
		conn.disconnect();
		IOUtils.closeQuietly(output);
	}

	protected static void conn(HttpURLConnection conn, Map<String, String> httpHeads, int connectTimeout,
			int readTimeout) throws IOException {
		if (httpHeads != null) {
			for (Entry<String, String> head : httpHeads.entrySet()) {
				conn.setRequestProperty(head.getKey(), head.getValue());
			}
		}
		conn.setConnectTimeout(connectTimeout);
		conn.setReadTimeout(readTimeout);

		conn.connect();
	}

	/**
	 * The default buffer size to use for {@link #copyLarge(InputStream, OutputStream)} and
	 * {@link #copyLarge(Reader, Writer)}
	 */
	public static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

	public static long copy(InputStream input, OutputStream output) throws IOException {
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		long count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}
}
