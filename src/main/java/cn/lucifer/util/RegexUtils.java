package cn.lucifer.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式工具类
 * 
 * @author Lucifer
 *
 */
public class RegexUtils {

	/**
	 * 获取匹配的子项<br>
	 * 
	 * <pre>
	 * 举例：
	 * regex = "([\\w|$]+):L([\\w|/]+);";
	 * source = "a:Ljava/nio/charset/CharsetDecoder;";
	 * 那么返回的结果是["a","java/nio/charset/CharsetDecoder"]
	 * </pre>
	 * 
	 * @param source
	 * @param regex
	 * @return
	 */
	public static List<String> getMatchChildren(String source, String regex) {
		Pattern expression = Pattern.compile(regex);
		Matcher matcher = expression.matcher(source);
		int groupCount;
		List<String> children = new ArrayList<String>();
		while (matcher.find()) {
			groupCount = matcher.groupCount();
			for (int i = 1; i <= groupCount; i++) {
				children.add(matcher.group(i));
			}
		}
		return children;
	}
}
