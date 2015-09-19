package cn.lucifer.util.http;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.lucifer.BaseTest;

public class HttpSocketTest extends BaseTest {

	HttpSocket httpSocket;

	@Before
	public void setUp() throws Exception {
		// httpSocket = new HttpSocket();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetHostIp() throws UnknownHostException {
		String host = "ojiaauthorvideos.26700.bs2.yy.com";
		host = "221.228.83.154";
		host = "172.19.94.25";
		HttpSocket httpSocket = new HttpSocket(host);
		String ip = httpSocket.inetAddress.getHostAddress();
		System.out.println(ip);
	}

	@Test
	public void testRequest() throws Exception {
		HttpSocket httpSocket = new HttpSocket("127.0.0.1");
		String uri = "/home/test.do";
		Map<String, String> httpHeads = null;
		int partNumber = 14;
		String jsonStr = "{\"partcount\":" + partNumber + "}";
		InputStream body = IOUtils.toInputStream(jsonStr);
		byte[] responeBytes = httpSocket.request(HttpMethod.POST, uri,
				httpHeads, body);
		System.out.println(responeBytes);
	}

	@Test
	public void testRequestIOS() throws Exception {
		String ip = "172.19.94.25";
		ip = "221.228.83.154";
		ip = "push.ouj.com";
		ip = "test.push.ouj.com";
		HttpSocket httpSocket = new HttpSocket(ip, 80, 30000);
		final String custom = "{postId:121964, barId:2, type:1}";
		final String uriBase = "/push?appId=ouj&osType=adr&content=1&badge=3&custom="
				+ URLEncoder.encode(custom, "utf-8") + "&deviceToken=";
		final String token = "f2baf93b-9c95-4c0b-89d7-a4b7cf16a26c";
		final String devicesToken = "f03b096bf4c691540170340b7f96cf568d38439f593df3ab618cabce0fa08df1";
		final String uriBaseIOS = "/push?appId=ouj&osType=ios&content=1&badge=3&custom="
				+ URLEncoder.encode(custom, "utf-8") + "&deviceToken=";

		Map<String, String> httpHeads = null;
		String postBody = "appId=ouj&deviceToken=8dd755a05f1e9bc25ec3a26b87ac9898928620beccc342a7eae5663e37a487aa&osType=ios&content=1&badge=3&custom={%22content%22%3A%22MCBBQkMgMCAwIDAgdHJ1ZSBJTSAxMDM0OCA2MDU1MTEyMDgwMDc0MjI2OTI0IDAg6YCa6K6v5b2V%22}";
		InputStream body = IOUtils.toInputStream(postBody);
		body = null;
		StrBuilder tokenBuilder = new StrBuilder();
		for (int i = 0; i < 1; i++) {
			tokenBuilder.append(devicesToken);
			if (i % 55 == 0) {
				byte[] responeBytes = httpSocket.request(
						HttpMethod.GET,
						uriBaseIOS
								+ URLEncoder.encode(tokenBuilder.toString(),
										"utf-8"), httpHeads, body);
				System.out.println(new String(responeBytes));
				tokenBuilder = new StrBuilder();
			} else {
				tokenBuilder.append('|');
			}
		}

		httpSocket.close();
	}

	@Test
	public void testRequestAndroid() throws Exception {
		String ip = "172.19.94.25";
		ip = "221.228.83.154";
		HttpSocket httpSocket = new HttpSocket(ip, 8191, 30000);
		final String custom = "{postId:121964, barId:2, type:1}";
		final String uriBase = "/push?appId=ouj&osType=adr&content=1&badge=3&custom="
				+ URLEncoder.encode(custom, "utf-8") + "&deviceToken=";
		final String token = "f2baf93b-9c95-4c0b-89d7-a4b7cf16a26c";

		Map<String, String> httpHeads = null;
		String postBody = "appId=ouj&deviceToken=8dd755a05f1e9bc25ec3a26b87ac9898928620beccc342a7eae5663e37a487aa&osType=ios&content=1&badge=3&custom={%22content%22%3A%22MCBBQkMgMCAwIDAgdHJ1ZSBJTSAxMDM0OCA2MDU1MTEyMDgwMDc0MjI2OTI0IDAg6YCa6K6v5b2V%22}";
		InputStream body = IOUtils.toInputStream(postBody);
		body = null;
		StrBuilder tokenBuilder = new StrBuilder();
		for (int i = 0; i < 1; i++) {
			tokenBuilder.append(token);
			if (i % 55 == 0) {
				byte[] responeBytes = httpSocket.request(
						HttpMethod.GET,
						uriBase
								+ URLEncoder.encode(tokenBuilder.toString(),
										"utf-8"), httpHeads, body);
				System.out.println(new String(responeBytes));
				tokenBuilder = new StrBuilder();
			} else {
				tokenBuilder.append('|');
			}
		}

		httpSocket.close();
	}
}
