package cn.lucifer.util;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class ConfigUtils {

    /**
     * 加载配置
     *
     * @param clazz
     * @param fileName
     * @return
     * @throws IOException
     */
    public static String loadStrResource(Class clazz, String fileName) throws IOException {
        InputStream inputStream = clazz.getResourceAsStream(fileName);
        return IOUtils.toString(inputStream, "UTF-8");
    }
}
