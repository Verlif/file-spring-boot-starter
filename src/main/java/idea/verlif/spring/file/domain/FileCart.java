package idea.verlif.spring.file.domain;

import idea.verlif.spring.file.FileConfig;
import idea.verlif.spring.file.util.FilePathUtil;

/**
 * 文件域 <br/>
 * 文件域的目的是方便维护，并且可以通过简单的修改来达到对文件的权限管控 <br/>
 * 建议文件相关的路径定义在文件域中
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/11/9 10:44
 */
public class FileCart {

    /**
     * 文件夹名
     */
    private final String Area;

    public FileCart(String type) {
        StringBuilder sb = new StringBuilder();
        String split = type.contains("/") ? "/" : "\\\\";
        for (String s : type.split(split)) {
            if (s.length() > 0) {
                sb.append(s).append(FileConfig.DIR_SPLIT);
            }
        }
        this.Area = FilePathUtil.beautyPath(sb.toString());
    }

    public String getArea() {
        return Area;
    }
}
