package cn.lucifer.util;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;

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
	public void testHttpGet() throws MalformedURLException {
		final String urlStr = "http://test.account.api.5253.com/user/checkLogin.do?g_cid=normal&yyuid=50013623&g_appid=10001&sdk_ver=65&g_ver=28&token=142077910980155856&d_uuid=359250050539707";
		URL url = new URL(urlStr);
		byte[] respone = HttpHelper.httpGet(url);
		System.out.println(new String(respone));
	}

}
