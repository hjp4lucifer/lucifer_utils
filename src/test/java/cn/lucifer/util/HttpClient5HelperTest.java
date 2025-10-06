package cn.lucifer.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.Enumeration;

import static org.junit.Assert.*;

public class HttpClient5HelperTest {

	@Test
	public void httpGet() throws Exception{
		byte[] resp = HttpClient5Helper.httpGet(
				"https://javbot3.top/search?wd=MIKR-033",
				null, null);
		System.out.println(new String(resp));



	}

	@Test
	public void httpPost() {
	}

    /**
     * 列出系统默认证书信任库中的证书数量和别名
     */
    @Test
    public void listSystemCertificates() {
        try {
            String cacertsPath = System.getProperty("java.home") + "/lib/security/cacerts";
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());

            FileInputStream fis = new FileInputStream(cacertsPath);
            keyStore.load(fis, "changeit".toCharArray());
            fis.close();

            System.out.println("系统证书库路径: " + cacertsPath);
            System.out.println("证书总数: " + keyStore.size());

            Enumeration<String> aliases = keyStore.aliases();
            int count = 0;
            while (aliases.hasMoreElements() && count < 10) { // 只显示前10个
                String alias = aliases.nextElement();
                System.out.println("证书别名[" + (++count) + "]: " + alias);
            }
        } catch (Exception e) {
            System.err.println("读取系统证书失败: " + e.getMessage());
        }
    }

}