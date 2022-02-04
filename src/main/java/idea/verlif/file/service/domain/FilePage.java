package idea.verlif.file.service.domain;

import java.util.List;

/**
 * @author Verlif
 * @version 1.0
 * @date 2022/1/5 15:57
 */
public class FilePage {

    protected List<FileInfo> infos;
    protected long total;
    protected long size;
    protected long current;
    protected long pages;

    public List<FileInfo> getInfos() {
        return infos;
    }

    public void setInfos(List<FileInfo> infos) {
        this.infos = infos;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getCurrent() {
        return current;
    }

    public void setCurrent(long current) {
        this.current = current;
    }

    public long getPages() {
        return pages;
    }

    public void setPages(long pages) {
        this.pages = pages;
    }

}
