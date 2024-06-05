package cn.lucifer.util;

import cn.lucifer.http.HttpClientException;
import cn.lucifer.http.NameValuePair;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
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

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public final class HttpClient5Helper {

	private static final int CONNECTION_TIMEOUT = 60000;
	private static final String content_type = "Content-Type";
	private static final String application_x_www_form_urlencoded = "application/x-www-form-urlencoded";
	private static final String http_socket_timeout = "http.socket.timeout";
	private static final String encoding_utf8 = "UTF-8";

	public static int reTryCount = 0;

	public static byte[] httpGet(final String oriUrl, NameValuePair[] parametersBody, Map<String, String> header) throws IOException {
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

		return execute(header, url, httpGet);
	}

	private static byte[] execute(Map<String, String> header, String url, ClassicHttpRequest httpReq)
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
		try {
			SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
			SSLConnectionSocketFactory sslFactory = new SSLConnectionSocketFactory(sslContext,
					NoopHostnameVerifier.INSTANCE);
			connManager = PoolingHttpClientConnectionManagerBuilder.create().setSSLSocketFactory(sslFactory).build();
		} catch (Exception e) {
			e.printStackTrace();
		}

		CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(config)
				.setConnectionManager(connManager)
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
						System.out.printf("【%s】 Method failed! url=%s, statusCode=%s, statusLine=%s%n",
								httpReq.getMethod(), url, statusCode, response.getReasonPhrase());
						throw new HttpClientException(statusCode, "statusCode=" + statusCode);
					}
					return EntityUtils.toByteArray(response.getEntity());
				});
			} catch (SocketTimeoutException e) {
				System.out.printf("[SocketTimeoutException] 【%s】 Method failed! url=%s, retryCount=%d%n",
						httpReq.getMethod(), url, retryCount);
			} catch (HttpClientException e) {
				System.out.printf("[HttpClientException] 【%s】 Method failed! url=%s", httpReq.getMethod(), url);
				throw e;
			} catch (IOException e) {
				System.out.printf("[IOException] 【%s】 Method failed! url=%s", httpReq.getMethod(), url);
				throw e;
			}
		}
		return null;
	}

	public static  byte[] httpPost(final String url, NameValuePair[] parametersBody, Map<String, String> header) throws IOException {
		ClassicHttpRequest httpPost = ClassicRequestBuilder.post(url).build();
		if (null != parametersBody && parametersBody.length > 0) {
			List<org.apache.hc.core5.http.NameValuePair> nvpList = new ArrayList<>();
			for (NameValuePair nvp : parametersBody) {
				nvpList.add(new BasicNameValuePair(nvp.getName(), String.valueOf(nvp.getValue())));
			}
			httpPost.setEntity(new UrlEncodedFormEntity(nvpList));
		}

		return execute(header, url, httpPost);
	}

	public static byte[] httpPost(final String url, InputStream body, Map<String, String> header)
			throws IOException {
		ClassicHttpRequest httpPost = ClassicRequestBuilder.post(url).build();
		if (null != body) {
			httpPost.setEntity(new InputStreamEntity(body, null));
		}

		return execute(header, url, httpPost);
	}

	private static void initHeader(Map<String, String> header, String key, String defaultValue) {
		if (header.containsKey(key)) {
			return;
		}
		header.put(key, defaultValue);
	}
}
