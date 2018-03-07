package io.igordonxiao.pgdao;

import java.util.List;

/**
 * 分页对象
 */
public class PageResult<T> {
    /**
     * 记录总数
     */
    private Long count;
    /**
     * 页数
     */
    private Integer page;
    /**
     * 分页条数
     */
    private Integer size;
    /**
     * 记录集
     */
    private List<T> data;

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
