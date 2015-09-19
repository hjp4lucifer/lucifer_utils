package cn.lucifer;

import org.apache.log4j.xml.DOMConfigurator;

public class BaseTest {
	static {
		final String fn = "src/test/resources/config/log4j.xml";
		DOMConfigurator.configure(fn);
	}
}
