package cn.lucifer.util;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class HttpClientHelperTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testHttpGet() throws IOException {
		final String url = "http://test.account.api.5253.com/user/checkLogin.do?g_cid=normal&yyuid=50013623&g_appid=10001&sdk_ver=65&g_ver=28&token=142077910980155856&d_uuid=359250050539707";
		byte[] respone = HttpClientHelper.httpGet(url, null);
		System.out.println(new String(respone));
	}

	@Test
	public void testHttpPost() {
		fail("Not yet implemented");
	}

	@Test
	public void testHttpPostWithFile() {
		fail("Not yet implemented");
	}

}
