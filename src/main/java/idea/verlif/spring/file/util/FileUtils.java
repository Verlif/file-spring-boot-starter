package idea.verlif.spring.file.util;

import java.io.File;

/**
 * @author Verlif
 * @version 1.0
 * @date 2022/5/23 10:17
 */
public class FileUtils {

    public static int deleteFile(File file) {
        int count = 0;
        if (file.isFile()) {
            if (file.delete()) {
                count++;
            }
        } else {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    count += deleteFile(f);
                }
            }
            // 最后删除文件夹
            file.delete();
        }
        return count;
    }
}
