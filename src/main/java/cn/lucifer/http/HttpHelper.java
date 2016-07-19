package cn.lucifer.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;

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

	private static void trustAllHttpsCertificates()
			throws NoSuchAlgorithmException, KeyManagementException {
		javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
		javax.net.ssl.TrustManager tm = new MiTM();
		trustAllCerts[0] = tm;
		javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext
				.getInstance("SSL");
		sc.init(null, trustAllCerts, null);
		javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc
				.getSocketFactory());
	}

	static class MiTM implements javax.net.ssl.TrustManager,
			javax.net.ssl.X509TrustManager {
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public boolean isServerTrusted(
				java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public boolean isClientTrusted(
				java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public void checkServerTrusted(
				java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
			return;
		}

		public void checkClientTrusted(
				java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
			return;
		}
	}

	protected static int connect_timeout = 30000;

	public static byte[] http(String url, HttpMethod method,
			Map<String, String> httpHeads, InputStream input)
			throws IOException, HttpClientException {
		return http(url, method, httpHeads, input, connect_timeout);
	}

	public static byte[] http(String url, HttpMethod method,
			Map<String, String> httpHeads, InputStream input, int connectTimeout)
			throws IOException, HttpClientException {
		return http(url, method, httpHeads, input, connectTimeout, null);
	}

	public static byte[] http(String url, HttpMethod method,
			Map<String, String> httpHeads, InputStream input,
			int connectTimeout, Map<String, List<String>> responseHeads)
			throws IOException, HttpClientException {
		HttpURLConnection conn = (HttpURLConnection) new URL(url)
				.openConnection();
		if (method != HttpMethod.DELETE) {
			conn.setDoInput(true);
		}
		if (method != HttpMethod.GET) {
			conn.setDoOutput(true);
		}

		conn.setRequestMethod(method.toString());

		conn(conn, httpHeads, connectTimeout);
		if (null != responseHeads) {
			responseHeads.putAll(conn.getHeaderFields());
		}
		
		if (null != input) {
			OutputStream output = conn.getOutputStream();
			IOUtils.copy(input, output);
			output.flush();
			IOUtils.closeQuietly(output);
		}
		int httpStatus = conn.getResponseCode();
		if (httpStatus != HttpStatus.SC_OK) {
			String msg = String.format(
					"【%s】get data from url=%s fail, http status=%d",
					method.toString(), url, httpStatus);
			throw new HttpClientException(httpStatus, msg);
		}
		if (method == HttpMethod.DELETE) {
			return new byte[0];
		}
		return getResponse(conn);
	}

	protected static byte[] getResponse(HttpURLConnection conn)
			throws IOException {
		InputStream input = conn.getInputStream();
		byte[] response = IOUtils.toByteArray(input);

		IOUtils.closeQuietly(input);
		conn.disconnect();
		return response;
	}

	protected static void conn(HttpURLConnection conn,
			Map<String, String> httpHeads, int connectTimeout)
			throws IOException {
		if (httpHeads != null) {
			for (Entry<String, String> head : httpHeads.entrySet()) {
				conn.setRequestProperty(head.getKey(), head.getValue());
			}
		}
		conn.setConnectTimeout(connectTimeout);
		conn.setReadTimeout(connectTimeout);

		conn.connect();
	}
}
