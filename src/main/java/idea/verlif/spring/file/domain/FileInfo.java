package idea.verlif.spring.file.domain;

import java.io.File;
import java.util.Date;

/**
 * 文件信息
 *
 * @author Verlif
 * @version 1.0
 */
public class FileInfo {

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件上次更新时间
     */
    private Date updateTime;

    /**
     * 文件大小
     */
    private Long size = 0L;

    /**
     * 后缀
     */
    private String suffix;

    /**
     * 是否是文件
     */
    private boolean isFile;

    /**
     * 相对路径
     */
    private String path;

    public FileInfo(File file, String path) {
        String name = file.getName();
        this.fileName = name;
        this.updateTime = new Date(file.lastModified());
        this.size = file.length();

        int suf = name.lastIndexOf(".");
        if (suf != -1) {
            this.suffix = name.substring(suf + 1);
        }

        this.isFile = file.isFile();
        this.path = path;
    }

    public FileInfo(File file) {
        this(file, file.getPath());
    }

    /**
     * 填充文件信息
     *
     * @param info 文件信息
     */
    public void fill(FileInfo info) {
        this.fileName = info.fileName;
        this.updateTime = info.updateTime;
        this.size = info.size;
        this.suffix = info.suffix;
        this.isFile = info.isFile;
        this.path = info.path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        if (size != null && size > 0) {
            this.size = size;
        }
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public boolean isFile() {
        return isFile;
    }

    public void setFile(boolean file) {
        isFile = file;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
