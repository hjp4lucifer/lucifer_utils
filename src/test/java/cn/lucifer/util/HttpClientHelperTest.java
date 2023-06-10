package cn.lucifer.util;

import static org.junit.Assert.*;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	public void testGetCoupon() throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		long targetTime = format.parse("2022-03-12 11:00:00").getTime();
		// 2分钟前开始强循环计时
		long beginCheckTime = targetTime - 2*60000;

		while (System.currentTimeMillis() < beginCheckTime) {
			System.out.println("current = " + format.format(new Date()));
			Thread.sleep(60000);
		}

		// 添加提前量
		targetTime -= 30;
		while (true) {
			if (System.currentTimeMillis() >= targetTime) {
				System.out.println("getCoupon= " + System.currentTimeMillis());
				String payload = "{\"templateId\":520,\"appVersion\":\"2.0.90\",\"env\":\"production\",\"wxTemplateId\":520,\"storeId\":1619042,\"appid\":\"wx83231ee9993066b7\",\"pid\":\"42\",\"refer\":\"\",\"openid\":\"ouj0M0a-br_akJhtDamaRQM4J6L8\",\"source\":1,\"sdpSource\":\"ec\",\"deliveryStoreList\":[{\"storeId\":1619042,\"deliveryType\":2},{\"storeId\":1615042,\"deliveryType\":6}],\"timeStamp\":1647052686323,\"sign\":\"b47ec8f90ccdcbdd72f5f5fd198c91cb1b5adfeb53b18557db23892d386bafa3d5a037ebda5078c6429882c106ea42532f383ac0f3d9e12127b04b5a867325f5\",\"userIdentitySign\":\"NHpgx0o7lWjgkaztDJtHTqaGu1NVJddoY2sx0PZ9Zae0BHt4l4AXQjaD8aZot1Y5KLPj8J9Ppnnlb8jKvUz1VQ4wJdSqek8eQlkC-GfNmNe4-yrJlv603mZLPr6L70AhuLgZXlJYKOde9pmkq5_QAoODDIx0fNXwitkOxoSX2f-K_Ogus24cwLsWDa8ZHpbGqtcDRLCLY8D_UONJyVB6dd6nouHwdUSW9uaAcxbrjjma_zPzfb6UFKGj8JhEpdqcv3OuoVL02K7odf6JP4R62uGPrOVg2U77-pQ_LOQXNI6OS0dTj2wpKY65Hn27DrU1pWM7ZbMxxXHjthPqBz39rDyOBFzBKj037oNbivRlYqrxKs5SaZCN1BcAslyn737obM0FH_rTFIG0cmkAt1UmKReimD7xZnDH4OBzThHnEgl4Jbhr4aWK2KocLYsdKtHv3NmkEhxm9V1dpfdrJjY06_xbOK6JjT6NW-wbsGh7HXSBM2spWUowgZYkXDGP3_WU2ge3QhctsBt9dBQz6PszPJUEeXXgx-m26R08grPJsUoKk9CdXV8SqcnYdoNH4p_gOxXl2Is1yAacnJDqgFlRxe2oi53cvP7PpBsvDCAL-Wz6sd47WSrDxQ9v2NwlP0r7Iw7MiN1SiVCXJrF10MKEetz0sgj-BVVEhC0ImKKVojJaBpYLHPA_Me2bZjIdtZaVo0qg6bf7NkrOwlX2yhjHrOUHxTJbFBJx4AeFwMI_IeCZVKRTCr0boRoGEhO3fYdqpkZj9ZQlEXFw87DOKT58YxDiQkotVzsOPWjXYJBGwomU7ctB6wKoY6eL3FiFtFOO\",\"nonce\":\"99a1146a-5dd7-4e99-ab96-ae61f96fe097\",\"activityId\":148904,\"couponActivityId\":40153,\"token\":\"jsc2skp.865e6980-c778-4d90-af66-537fe5dc043a\",\"sourceType\":1,\"bodySign\":\"25EC054BA52127F3AEB016CF6CB6658D\"}";
				getCoupon(payload);

				break;
			}
		}
	}


	public void getCoupon(String payload) throws IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost("https://mapi.ghsmpwalmart.com/api3/ec/activity/limitedcoupon/acquireCoupon");

		httpPost.setEntity(new StringEntity(payload));
		httpPost.setHeader("Limitcoupon", "0");
		httpPost.setHeader("Acceptrecommend", "1");
		httpPost.setHeader("Content-Type", "application/json");
		httpPost.setHeader("X-Wx-Token", "jsc2skp.865e6980-c778-4d90-af66-537fe5dc043a");
		httpPost.setHeader("Version", "");
		httpPost.setHeader("Accept-Encoding", "gzip, deflate");
		httpPost.setHeader("User-Agent", "Mozilla/5.0 (iPad; CPU OS 14_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/8.0.17(0x1800112e) NetType/WIFI Language/zh_CN");
		httpPost.setHeader("Referer", "https://servicewechat.com/wx83231ee9993066b7/256/page-frame.html");

		CloseableHttpResponse resp = httpclient.execute(httpPost);

		try {
			System.out.println(resp.getStatusLine());
			HttpEntity entity2 = resp.getEntity();
			// do something useful with the response body
			// and ensure it is fully consumed
			String respStr = EntityUtils.toString(entity2, "utf-8");
			System.out.println(respStr);
			EntityUtils.consume(entity2);
		} finally {
			resp.close();
		}
	}

	@Test
	public void testHttpPostWithFile() {
		fail("Not yet implemented");
	}

}
