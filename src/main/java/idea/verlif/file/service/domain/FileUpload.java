package idea.verlif.file.service.domain;

/**
 * 文件上传信息
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/11/11 16:44
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

}
