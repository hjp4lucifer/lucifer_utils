package cn.lucifer.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.Cookie;
import org.apache.hc.client5.http.impl.cookie.BasicClientCookie;

public class CookiesUtils {

	public static BasicCookieStore getCookieStore(String domain, String cookieStr) {
		String[] cookieList = StringUtils.split(cookieStr, ";");

		BasicCookieStore cookieStore = new BasicCookieStore();
		for (String c : cookieList) {
			String[] pair = StringUtils.split(c.trim(), "=");
			BasicClientCookie cookie = new BasicClientCookie(pair[0], pair[1]);
			cookie.setDomain("javbot3.top");
			cookieStore.addCookie(cookie);
		}
		return cookieStore;
	}

	public static String getByName(BasicCookieStore cookieStore, String name){
		for (Cookie cookie : cookieStore.getCookies()) {
			if(name.equals(cookie.getName())){
				return cookie.getValue();
			}
		}
		return null;
	}

}
