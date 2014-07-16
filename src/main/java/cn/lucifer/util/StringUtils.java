package cn.lucifer.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;



public class StringUtils {
	public static final String null_string = "";

	private static final String SUFFIX_ELLIPSIS_POINTS = "...";
	private static final String SUFFIX_DETAILS = "...[详细]";

	private static final String regexp_err_char = "(\\?|\\[|\\]|\\(|\\)|\\~|\\*)";
	private static final Pattern pattern_regexp_err_char = Pattern
			.compile(regexp_err_char);

	/**
	 * 处理正则表达式拼接里的非法字符
	 * 
	 * @param source
	 * @return
	 */
	public static String process4Regexp(String source) {
		String back = null;
		if (source != null) {
			back = pattern_regexp_err_char.matcher(source).replaceAll(
					null_string);
		}
		return back;
	}

	public static String process4Url(String target) {
		String result = null;
		if (target != null) {
			try {
				result = URLEncoder.encode(target, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * 处理json的非法字符 并把对应的非法字符进行替换
	 * 
	 * @param source
	 * @return
	 */
	public static String processMark(String source) {
		String back = null;
		if (source != null) {
			// back = source.replaceAll("\'", "’");
			back = source.replaceAll("\"", "”");
			back = back.replaceAll("(\\\t|\\\n|\\\r)", null_string);
		}
		return back;
	}

	/**
	 * 根据所给的长度lenght截取字符串
	 * 
	 * @param source
	 * @param lenght
	 * @return
	 */
	public static String cutString(String source, int lenght) {
		String back = null;
		if (source != null) {
			back = source;
			if (back.length() > lenght) {
				back = back.substring(0, lenght) + SUFFIX_ELLIPSIS_POINTS;
			}
		}
		return back;
	}

	public static String cutStringEndWithDetails(String source, int lenght) {
		String back = null;
		if (source != null) {
			back = source;
			if (back.length() > lenght) {
				back = back.substring(0, lenght) + SUFFIX_DETAILS;
			}
		}
		return back;
	}

	/**
	 * 截取字符长度
	 * 
	 * @param source
	 * @param lenght
	 * @return
	 */
	public static String cutStringByLeadin(String source, int lenght) {
		String back = null;
		if (source != null) {
			back = source.trim();
			if (back.length() > lenght) {
				back = back.substring(0, lenght);
			}
		}
		return back;
	}

	/**
	 * 处理括号问题
	 * 
	 * @param source
	 * @return
	 */
	public static String changBracket(String source) {
		String back = null;
		if (source != null) {
			back = source.replaceAll("(\\(|\\)|（|）)", " ");
		}
		return back;
	}

	/**
	 * 处理回车
	 * 
	 * @param target
	 * @return
	 */
	public static String processEnter(String target) {
		String replace = null;
		if (target != null) {
			replace = target.replaceAll("\\n", "<br />");
		}
		return replace;
	}


	/**
	 * 处理商家电话供手机显示
	 * 
	 * @param tel
	 *            数据库里的电话字符串
	 * @return 包含数个电话的list集合
	 */
	public static List<String> changeTel(String tel) {
		List<String> list = new ArrayList<String>();
		if (tel != null) {
			tel = tel.replaceAll("[(\\u4e00-\\u9fa5)]", null_string);
			tel = tel.replaceAll("&nbsp;", null_string);
			String[] arr = tel.split("(\\s{1,}|,|，|;|；|/)");
			for (int i = 0; i < arr.length; i++) {
				if (!null_string.equals(arr[i])) {
					list.add(arr[i].trim());
				}
			}
		}
		return list;
	}
}
