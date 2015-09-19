package cn.lucifer.util.http;

import java.io.IOException;

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
	public void testHttps() throws IOException, HttpException {
		String url = "https://test.pay.api.oxzj.com/test/test.do";
		url = "https://ouj.yy.com/test/test.do";
		byte[] rsp = HttpHelper.http(url, HttpMethod.GET, null, null);
		System.out.println(new String(rsp));
	}

	@Test
	public void testHttp() throws IOException, HttpException {
		String url = "http://www.ouj.com/";
		byte[] rsp = HttpHelper.http(url, HttpMethod.GET, null, null);
		System.out.println(new String(rsp));
	}
}
