package idea.verlif.file.service.domain;

/**
 * 文件查询条件
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/9/13 10:25
 */
public class FileQuery {

    /**
     * 文件名查询
     */
    protected String name;

    /**
     * 每页大小
     */
    protected Integer pageSize = 15;

    /**
     * 页码，从1开始
     */
    protected Integer pageNum = 1;

    /**
     * 排序字段
     */
    protected String orderBy;

    /**
     * 是否升序
     */
    protected boolean asc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public boolean isAsc() {
        return asc;
    }

    public void setAsc(boolean asc) {
        this.asc = asc;
    }

    /**
     * 获取页头序号
     */
    public Integer getPageHead() {
        return (pageNum - 1) * pageSize;
    }

    public void setPageNum(Integer pageNum) {
        if (pageNum < 1) {
            this.pageNum = 1;
        } else {
            this.pageNum = pageNum;
        }
    }

}
