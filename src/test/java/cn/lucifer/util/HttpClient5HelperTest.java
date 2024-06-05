package cn.lucifer.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class HttpClient5HelperTest {

	@Test
	public void httpGet() throws Exception{
		byte[] resp = HttpClient5Helper.httpGet(
				"https://baike.baidu.com/item/%E8%BE%9B%E6%99%AE%E6%A3%AE%E6%9D%80%E5%A6%BB%E6%A1%88/8085815?fr=ge_ala",
				null, null);
		System.out.println(new String(resp));
	}

	@Test
	public void httpPost() {
	}
}