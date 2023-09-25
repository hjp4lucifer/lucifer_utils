package cn.lucifer.util;

import cn.lucifer.http.HttpClientException;
import cn.lucifer.http.NameValuePair;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;
import org.apache.hc.core5.util.TimeValue;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public final class HttpClient5Helper {

	private static final int CONNECTION_TIMEOUT = 20000;
	private static final String content_type = "Content-Type";
	private static final String application_x_www_form_urlencoded = "application/x-www-form-urlencoded";
	private static final String http_socket_timeout = "http.socket.timeout";
	private static final String encoding_utf8 = "UTF-8";

	public static int reTryCount = 0;

	public static byte[] httpGet(final String oriUrl, NameValuePair[] parametersBody, Map<String, String> header) throws IOException {
		if (null == header) {
			header = new HashMap<>();
		}
		// init header value
		initHeader(header, "User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.107 Safari/537.36");
		initHeader(header, "Connection", "Keep-Alive");
		initHeader(header, "Accept-Language", "zh-cn");

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

		RequestConfig config = RequestConfig.custom()
				.setConnectionRequestTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
				.setConnectTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
				.setResponseTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
				.build();
		CloseableHttpClient httpClient = HttpClients.custom()
				.setDefaultRequestConfig(config)
				// 删除空闲连接时间
				.evictIdleConnections(TimeValue.of(40, TimeUnit.SECONDS))
				// 关闭自动重试
				.disableAutomaticRetries()
				.build();
		ClassicHttpRequest httpGet = ClassicRequestBuilder.get(url).build();

		for (Map.Entry<String, String> entry : header.entrySet()) {
			httpGet.addHeader(entry.getKey(), entry.getValue());
		}

		for (int retryCount = 0; retryCount < 5; retryCount++) {
			try {
				return httpClient.execute(httpGet, response -> {
					int statusCode = response.getCode();
					if (statusCode != HttpStatus.SC_OK) {
						System.out.println("HttpGet Method failed! url=" + url + ", statusCode=" + statusCode + ", statusLine="
								+ response.getReasonPhrase());
						throw new HttpClientException(statusCode, "statusCode=" + statusCode);
					}
					return EntityUtils.toByteArray(response.getEntity());
				});
			} catch (SocketTimeoutException e) {
				System.out.println(String.format("[SocketTimeoutException] HttpGet Method failed! url=%s, retryCount=%d", url, retryCount));
			} catch (HttpClientException e) {
				System.out.println("[HttpClientException] HttpGet Method failed! url=" + url);
				throw e;
			} catch (IOException e) {
				System.out.println("[IOException] HttpGet Method failed! url=" + url);
				throw e;
			}
		}
		return null;
	}

	private static void initHeader(Map<String, String> header, String key, String defaultValue) {
		if (header.containsKey(key)) {
			return;
		}
		header.put(key, defaultValue);
	}
}
