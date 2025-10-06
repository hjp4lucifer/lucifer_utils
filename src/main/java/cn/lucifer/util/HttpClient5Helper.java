package cn.lucifer.util;

import cn.lucifer.http.HttpClientException;
import cn.lucifer.http.NameValuePair;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.cookie.BasicClientCookie;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.TrustSelfSignedStrategy;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.InputStreamEntity;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.util.TimeValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public final class HttpClient5Helper {

	private final static Logger logger = LoggerFactory.getLogger(HttpClient5Helper.class);

	public static final String cookie = "cookie";

	private static final int CONNECTION_TIMEOUT = 30000;
	private static final String content_type = "Content-Type";
	private static final String application_x_www_form_urlencoded = "application/x-www-form-urlencoded";
	private static final String http_socket_timeout = "http.socket.timeout";
	private static final String encoding_utf8 = "UTF-8";

	public static int reTryCount = 0;

	static final TrustManager[] trustAllCerts = new TrustManager[]{
			new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			}
	};

	public static byte[] httpGet(final String oriUrl, NameValuePair[] parametersBody, Map<String, String> header) throws IOException {
		return httpGet(oriUrl, parametersBody, header, null);
	}

	public static byte[] httpGet(final String oriUrl, NameValuePair[] parametersBody, Map<String, String> header, BasicCookieStore cookieStore) throws IOException {
		final String url;
		if (parametersBody != null && parametersBody.length != 0) {
			StrBuilder urlBuilder = new StrBuilder(oriUrl);
			urlBuilder.append("?");
			for (int i = 0; i < parametersBody.length; i++) {
				if (i > 0) {
					urlBuilder.append("&");
				}
				NameValuePair nvp = parametersBody[i];
				urlBuilder.append(nvp.getName()).append("=").append(nvp.getValue());
			}
			url = urlBuilder.toString();
		} else {
			url = oriUrl;
		}

		ClassicHttpRequest httpGet = ClassicRequestBuilder.get(url).build();

		return execute(header, url, httpGet, cookieStore);
	}

	private static byte[] execute(Map<String, String> header, String url, ClassicHttpRequest httpReq, BasicCookieStore cookieStore)
			throws IOException {
		if (null == header) {
			header = new HashMap<>();
		}
		// init header value
		initHeader(header, "User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.107 Safari/537.36");
		initHeader(header, "Connection", "Keep-Alive");
		initHeader(header, "Accept-Language", "zh-cn");

		RequestConfig config = RequestConfig.custom()
				.setConnectionRequestTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
				.setConnectTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
				.setResponseTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS).build();

		HttpClientConnectionManager connManager = null;
		boolean test = true;
		try {
			SSLContext sslContext;
			if (test) {
				sslContext = SSLContext.getInstance("TLS");
				sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
			} else {
				sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
			}
			SSLConnectionSocketFactory sslFactory = new SSLConnectionSocketFactory(sslContext,
					NoopHostnameVerifier.INSTANCE);
			connManager = PoolingHttpClientConnectionManagerBuilder.create().setSSLSocketFactory(sslFactory).build();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (null == cookieStore) {
			cookieStore = new BasicCookieStore();
		}
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(config)
				.setConnectionManager(connManager)
				// 设置cookie
				.setDefaultCookieStore(cookieStore)
				// 删除空闲连接时间
				.evictIdleConnections(TimeValue.of(40, TimeUnit.SECONDS))
				// 关闭自动重试
				.disableAutomaticRetries().build();

		for (Map.Entry<String, String> entry : header.entrySet()) {
			httpReq.addHeader(entry.getKey(), entry.getValue());
		}

		for (int retryCount = 0; retryCount < 5; retryCount++) {
			try {
				return httpClient.execute(httpReq, response -> {
					int statusCode = response.getCode();
					if (statusCode != HttpStatus.SC_OK) {
						logger.error("【{}】 Method failed! url={}, statusCode={}, statusLine={}{}",
								new Object[]{httpReq.getMethod(), url, statusCode, response.getReasonPhrase()});
						throw new HttpClientException(statusCode, "statusCode=" + statusCode);
					}
					return EntityUtils.toByteArray(response.getEntity());
				});
			} catch (SocketTimeoutException e) {
				logger.error("[SocketTimeoutException] 【{}】 Method failed! url=%s, retryCount={}{}",
						new Object[]{httpReq.getMethod(), url, retryCount});
			} catch (HttpClientException e) {
				logger.error("[HttpClientException] 【{}】 Method failed! url={}", new Object[]{httpReq.getMethod(), url});
				throw e;
			} catch (IOException e) {
				logger.error("[IOException] 【{}】 Method failed! url={}", new Object[]{httpReq.getMethod(), url});
				throw e;
			}
		}
		return null;
	}

	public static byte[] httpPost(final String url, NameValuePair[] parametersBody, Map<String, String> header) throws IOException {
		ClassicHttpRequest httpPost = ClassicRequestBuilder.post(url).build();
		if (null != parametersBody && parametersBody.length > 0) {
			List<org.apache.hc.core5.http.NameValuePair> nvpList = new ArrayList<>();
			for (NameValuePair nvp : parametersBody) {
				nvpList.add(new BasicNameValuePair(nvp.getName(), String.valueOf(nvp.getValue())));
			}
			httpPost.setEntity(new UrlEncodedFormEntity(nvpList));
		}

		return execute(header, url, httpPost, null);
	}

	public static byte[] httpPost(final String url, InputStream body, Map<String, String> header)
			throws IOException {
		ClassicHttpRequest httpPost = ClassicRequestBuilder.post(url).build();
		if (null != body) {
			httpPost.setEntity(new InputStreamEntity(body, null));
		}

		return execute(header, url, httpPost, null);
	}

	private static void initHeader(Map<String, String> header, String key, String defaultValue) {
		if (header.containsKey(key)) {
			return;
		}
		header.put(key, defaultValue);
	}
}
