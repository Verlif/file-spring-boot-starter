package idea.verlif.file.service.domain;

import java.util.Date;

/**
 * 文件信息
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/9/13 10:22
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
    private Long size;

    /**
     * 后缀
     */
    private String suffix;

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
        this.size = size;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
