package io.igordonxiao.pgdao;

import java.sql.Timestamp;
import java.util.Date;

abstract public class AbstractModel {

    /**
     * ID
     */
    private Long id;

    /**
     * 创建时间
     * PG-JDBC driver不支持Date
     */
    private Timestamp createdDate;
    /**
     * 更新时间
     */
    private Timestamp updatedDate;

    /**
     * 删除标识
     */
    private Integer delFlag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }
}
