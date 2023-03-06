package idea.verlif.spring.file.domain;

import idea.verlif.easy.file.util.File64Util;

import java.io.File;
import java.io.IOException;

/**
 * 文件上传信息
 *
 * @author Verlif
 * @version 1.0
 */
public class FileUpload {

    /**
     * 原文件名
     */
    private String filename;

    /**
     * Base64内容
     */
    private String file;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public static FileUpload toUpload(String path) throws IOException {
        return toUpload(new File(path));
    }

    public static FileUpload toUpload(File file) throws IOException {
        if (file.exists() && file.isFile()) {
            FileUpload upload = new FileUpload();
            upload.setFile(File64Util.toBase64(file));
            upload.setFilename(file.getName());
            return upload;
        } else {
            return null;
        }
    }
}
