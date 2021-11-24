package cn.lucifer.util;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;

/**
 * http client的帮助类
 *
 * @author Lucifer
 */
public class HttpClientHelper {

	private static final int CONNECTION_TIMEOUT = 20000;
	private static final String content_type = "Content-Type";
	private static final String application_x_www_form_urlencoded = "application/x-www-form-urlencoded";
	private static final String http_socket_timeout = "http.socket.timeout";
	private static final String encoding_utf8 = "UTF-8";

	public static int reTryCount = 0;

	/**
	 * get请求
	 *
	 * @param url
	 * @param parametersBody
	 * @return
	 */
	public static byte[] httpGet(String url, NameValuePair[] parametersBody) {
		return httpGet(url, parametersBody, null);
	}

	/**
	 * get请求
	 *
	 * @param url
	 * @param parametersBody
	 * @return
	 */
	public static byte[] httpGet(String url, NameValuePair[] parametersBody, Map<String, String> header) {
		if (null == header) {
			header = new HashMap<>();
		}
		// init header value
		initHeader(header, "User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.107 Safari/537.36");
		initHeader(header, "Connection", "Keep-Alive");
		initHeader(header, "Accept-Language", "zh-cn");

		byte[] responseData = null;
		if (parametersBody != null && parametersBody.length != 0) {
			StringBuffer urlBuffer = new StringBuffer(url);
			urlBuffer.append("?");
			NameValuePair nvp = null;
			for (int i = 0; i < parametersBody.length; i++) {
				if (i > 0) {
					urlBuffer.append("&");
				}
				nvp = parametersBody[i];
				urlBuffer.append(nvp.getName()).append("=")
						.append(nvp.getValue());
			}
			url = urlBuffer.toString();
		}
		HttpClient httpClient = new HttpClient();
		httpClient.getParams().setCookiePolicy(
				CookiePolicy.BROWSER_COMPATIBILITY);
		GetMethod httpGet = new GetMethod(url);

		httpGet.getParams().setParameter("http.socket.timeout",
				new Integer(CONNECTION_TIMEOUT));

		for (Map.Entry<String, String> entry : header.entrySet()) {
			httpGet.setRequestHeader(entry.getKey(), entry.getValue());
		}

		try {
			int statusCode = httpClient.executeMethod(httpGet);
			if (statusCode != HttpStatus.SC_OK) {
				System.out.println("HttpGet Method failed! url=" + url + ", statusCode=" + statusCode + ", statusLine="
						+ httpGet.getStatusLine());
			}
			// Read the response body.
			responseData = httpGet.getResponseBody();
		} catch (SocketTimeoutException e) {
			reTryCount++;
			if (reTryCount < 5) {
				System.out.println("retry count is : " + reTryCount);
				return httpGet(url, parametersBody);
			} else {
				e.printStackTrace();
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpGet.releaseConnection();
			httpClient = null;
		}
		reTryCount = 0;
		return responseData;
	}

	private static void initHeader(Map<String, String> header, String key, String defaultValue) {
		if (header.containsKey(key)) {
			return;
		}
		header.put(key, defaultValue);
	}

	/**
	 * post请求
	 *
	 * @param url
	 * @param parametersBody
	 * @return
	 */
	public static String httpPost(String url, NameValuePair[] parametersBody) {
		String responseData = null;
		HttpClient httpClient = new HttpClient();
		httpClient.getParams().setParameter(
				HttpMethodParams.HTTP_CONTENT_CHARSET, encoding_utf8);
		PostMethod httpPost = new PostMethod(url);
		httpPost.addParameter(content_type, application_x_www_form_urlencoded);
		httpPost.getParams().setParameter(http_socket_timeout,
				new Integer(CONNECTION_TIMEOUT));

		if (parametersBody != null) {
			httpPost.setRequestBody(parametersBody);
		}

		try {
			int statusCode = httpClient.executeMethod(httpPost);
			if (statusCode == HttpStatus.SC_OK) {
				responseData = httpPost.getResponseBodyAsString();
			} else {
				System.err.println("HttpPost Method failed: "
						+ httpPost.getStatusLine());
				System.err.println(httpPost.getResponseBodyAsString());
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpPost.releaseConnection();
			httpClient = null;
		}

		return responseData;
	}

	/**
	 * 带文件上传的post请求
	 *
	 * @param url
	 * @param parametersBody
	 * @param filePart
	 * @return
	 */
	public static String httpPostWithFile(String url,
										  NameValuePair[] parametersBody, FilePart filePart) {
		if (filePart == null) {
			return httpPost(url, parametersBody);
		}
		String responseData = null;
		HttpClient httpClient = new HttpClient();
		httpClient.getParams().setParameter(
				HttpMethodParams.HTTP_CONTENT_CHARSET, encoding_utf8);
		PostMethod httpPost = new PostMethod(url);

		try {
			int length = parametersBody == null ? 1 : parametersBody.length + 1;
			Part[] parts = new Part[length];
			int i = 0;
			for (NameValuePair pair : parametersBody) {
				parts[i++] = new StringPart(pair.getName(), pair.getValue(),
						encoding_utf8);
			}
			parts[i++] = filePart;

			httpPost.setRequestEntity(new MultipartRequestEntity(parts,
					httpPost.getParams()));
			int statusCode = httpClient.executeMethod(httpPost);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("HttpPost Method failed: "
						+ httpPost.getStatusLine());
			}
			responseData = httpPost.getResponseBodyAsString();
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpPost.releaseConnection();
			httpClient = null;
		}

		return responseData;
	}
}
