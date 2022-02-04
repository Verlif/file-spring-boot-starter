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
     * 文件子路径
     */
    private String type;

    /**
     * Base64文件集合
     */
    private Base64File[] uploads;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Base64File[] getUploads() {
        return uploads;
    }

    public void setUploads(Base64File[] uploads) {
        this.uploads = uploads;
    }

    public static final class Base64File {

        /**
         * 文件名
         */
        private String fileName;

        /**
         * Base64内容
         */
        private String file;

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }
    }
}
