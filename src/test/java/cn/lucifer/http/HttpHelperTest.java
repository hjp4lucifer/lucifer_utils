package cn.lucifer.http;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
		String url = "https://itunes.apple.com/WebObjects/MZStore.woa/wa/search?submit=edit&mt=8&term=%E8%BF%90%E5%8A%A8";
		url = "https://itunes.apple.com/WebObjects/MZStore.woa/wa/search?submit=edit&mt=8&term=%E5%BE%AE%E5%8D%9A";
		Map<String, String> httpHeads = new HashMap<String, String>();
		httpHeads.put("Host", "itunes.apple.com");
		httpHeads
				.put("User-Agent",
						"iTunes/12.3.1 (Windows; Microsoft Windows 7 x64 Ultimate Edition Service Pack 1 (Build 7601); x64) AppleWebKit/7601.2007.2002.2");
		httpHeads
				.put("Referer",
						"https://itunes.apple.com/WebObjects/MZStore.woa/wa/viewGrouping?cc=jp&id=27753&mt=10");
		httpHeads.put("X-Apple-Tz", "28800");
		httpHeads.put("Accept-Language",
				"zh-cn, zh;q=0.75, en-us;q=0.50, en;q=0.25");
		httpHeads
				.put("Cookie",
						"amp=haoQi2tCgjvush1CJuNzPw+Va2TV2HGIu+uBf4L7Be8b8btpNamyZBmWiJ+sNhCI69LWFm09k4PvGgtAwsMRY/GUlQe2e9IxIpLFsdnp6Oz4OjGaIlGgMAO/kkb2dazXWoJYkGBypFUKRXYGhmc83TYstp7ktaTALe38zHN1BLQ=; ampt-1406689228=MeMAFamuxDEQllMibUSm1TsSJ0V/mH4ytxksBUl/umURuWED0lLzCYl6FiRGnjDUZUr8HFi22ZICFnmb/Otq4ePo126K376wSlarGFeoa2wS0oAWHK8Ykni2CBR091iBnG8yQ4cKUl4qDL2qJErH3g==; amia-1406689228=RlGARR4lGmYY+Av/1qp+39xYfh0tz2AGJTEWwvL2vGBnveh5Zq6DYXZXYPfj3RK86VJb6Us4KuMLPXc0rUrsQw==; X-JS-SP-TOKEN=G0jXwdVhK8GBdl4KtdYUnw==; X-JS-TIMESTAMP=1468831997; groupingPillToken=1_iphone; mz_mt0-1406689228=Aqgr6JnnIElXUKVRpmxjQjaEPZZIF2c9mYs7RyLEHRwcf93beh+oultv8wgMjEbHXn4XSVF05daVP9tzbOu1Xbq5Bo5u1RpV6q4xJlOVU9xrQPa3YmKrdq5R8RwBRX1qxIkHCLcLcNUpZAJOjyGJMQNDWs51MIiGg0ZwBWquBIV5ZFZAL57TMKY3B2gsOUEnkAU1fiQ=; hsaccnt=1; mzf_in=702418; session-store-id=1BB9F345BB2E50E106C71B697EEA4FE6; itspod=70; mz_at0-1406689228=AwQAAAECAAHV7QAAAABXjJj1wtVybfX2uFLnr7BNKw/ho4KMvhc=; mz_at_ssl-1406689228=AwUAAAECAAHV7QAAAABXjJj1a1rmqPTvsRuiXm9yL3MifmhXwgQ=; ns-mzf-inst=40-251-443-153-2-8106-702418-70-nk11; wosid-lite=KYRltFh9J4lE4m2b506qAw; X-Dsid=1406689228; xp_ci=3zTPqqoz6EHz5DyzD9Kz13JvABC4Z");
		httpHeads.put("X-Apple-Store-Front", "143465-19,32 ab:xCqrVhn1");
		byte[] rsp = HttpHelper.http(url, HttpMethod.GET, httpHeads, null);
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
