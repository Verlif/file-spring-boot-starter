package idea.verlif.spring.file.util;

import java.io.File;

/**
 * @author Verlif
 */
public class FilePathUtil {

    /**
     * 文件路径分隔符
     */
    public static final String DIR_SPLIT = File.separator;

    public static String beautyPath(String path) {
        if ("/".equals(DIR_SPLIT)) {
            return path.replaceAll("\\\\", "/");
        } else {
            return path.replaceAll("/", "\\\\");
        }
    }

    public static String filterPath(String path) {
        return path.replace("..", "");
    }

}
