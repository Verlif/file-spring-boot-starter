package idea.verlif.spring.file.domain;

import java.util.Locale;

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
    protected Integer page = 15;

    /**
     * 页码，从1开始
     */
    protected Integer num = 1;

    /**
     * 排序字段
     */
    protected Order order;

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

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        if (page > 0) {
            this.page = page;
        }
    }

    public Integer getNum() {
        return num;
    }

    /**
     * 获取页头序号
     */
    public Integer getPageHead() {
        return (num - 1) * page;
    }

    public void setNum(Integer num) {
        if (num < 1) {
            this.num = 1;
        } else {
            this.num = num;
        }
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setOrder(String order) {
        try {
            this.order = Order.valueOf(order.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ignored) {}
    }

    public boolean isAsc() {
        return asc;
    }

    public void setAsc(boolean asc) {
        this.asc = asc;
    }

    public enum Order {
        /**
         * 通过文件名排序
         */
        NAME,

        /**
         * 文件更新时间
         */
        UPDATE_TIME,

        /**
         * 文件大小
         */
        SIZE,

        /**
         * 文件后缀
         */
        SUFFIX
    }
}