package idea.verlif.spring.file.domain;

import idea.verlif.easy.file.page.FilePage;

import java.io.File;

public class FileInfoPage {

    protected FileInfo[] files;
    protected long total;
    protected long size;
    protected long current;
    protected long pages;

    public FileInfoPage() {
    }

    public FileInfoPage(FilePage filePage, String prefix) {
        this.total = filePage.getTotal();
        this.size = filePage.getSize();
        this.current = filePage.getCurrent();
        this.pages = filePage.getPages();
        File[] files = filePage.getFiles();
        this.files = new FileInfo[files.length];
        for (int i = 0; i < files.length; i++) {
            FileInfo info = new FileInfo(files[i]);
            this.files[i] = info;
            if (!(info.getPath().charAt(0) == '/')) {
                info.setPath(info.getPath().substring(prefix.length()));
            }
        }
    }

    public FileInfo[] getFiles() {
        return files;
    }

    public void setFiles(FileInfo[] files) {
        this.files = files;
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
