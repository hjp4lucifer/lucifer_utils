package cn.lucifer.http;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HttpHelperTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testHttps() throws IOException, HttpClientException {
		String url = "https://test.pay.api.oxzj.com/test/test.do";
		url = "https://ouj.yy.com/test/test.do";
		byte[] rsp = HttpHelper.http(url, HttpMethod.GET, null, null);
		System.out.println(new String(rsp));
	}

	@Test
	public void testHttp() throws IOException, HttpClientException {
		String url = "http://www.ouj.com/";
		byte[] rsp = HttpHelper.http(url, HttpMethod.GET, null, null);
		System.out.println(new String(rsp));
	}

	@Test
	public void testHttp2() throws IOException, HttpClientException {
		String strFormat = "https://share.dmhy.org/json/%dQ%d.json";
		String path = "F:/lcf/cartoon/";
		for (int year = 2015; year > 2009; year--) {
			for (int quarter = 1; quarter <= 4; quarter++) {
				String url = String.format(strFormat, year, quarter);
				System.out.println(url);
				byte[] rsp = HttpHelper.http(url, HttpMethod.GET, null, null);
				String fn = url.substring(url.lastIndexOf("/") + 1);
				FileUtils.writeByteArrayToFile(new File(path + fn), rsp);
			}
		}
	}

	@Test
	public void testHttp3() throws IOException, HttpClientException {
		String[] urlArray = { "https://share.dmhy.org/json/2016Q2.json",
				"https://share.dmhy.org/json/2016Q1.json",
				"https://share.dmhy.org/json/2009Q2.json",
				"https://share.dmhy.org/json/2009Q4.json",
				"https://share.dmhy.org/json/2008Q2.json",
				"https://share.dmhy.org/json/2008Q4.json",
				"https://share.dmhy.org/json/2006Q2.json",
				"https://share.dmhy.org/json/2006Q4.json",
				"https://share.dmhy.org/json/2004Q4.json" };
		String path = "F:/lcf/cartoon/";
		for (String url : urlArray) {
			System.out.println(url);
			byte[] rsp = HttpHelper.http(url, HttpMethod.GET, null, null);
			String fn = url.substring(url.lastIndexOf("/") + 1);
			FileUtils.writeByteArrayToFile(new File(path + fn), rsp);
		}
	}
}
