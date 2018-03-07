package io.igordonxiao.pgdao;

/**
 * 配置
 */
public interface Conf {
    // ---------------------------------------------------------------
    //  数据库连接配置
    /**
     * DB URL
     */
    String DB_URL = "jdbc:postgresql://[host]:[ip]/[databasename]";
    /**
     * DB DRIVER
     */
    String DB_DRIVER = "org.postgresql.Driver";
    /**
     * DB USER
     */
    String DB_USER = "[username]";
    /**
     * DB PASSWORD
     */
    String DB_PASSWORD = "[password]";


    /**
     * 分页条数
     */
    Integer DB_PAGE_SIZE = 15;

    /**
     * 默认从第几条读取
     */
    Integer DB_PAGE_DEFAULT = 1;

    // ---------------------------------------------------------------

    /**
     * 逗号
     */
    String SERPERATOR_COMMA = ", ";

    /**
     * ID字段
     */
    String ID_FIELD = "id";
    /**
     * 创建时间字段
     */
    String CREATED_DATE = "createdDate";
    /**
     * 更新时间字段
     */
    String UPDATED_DATE = "updatedDate";
    /**
     * 删除标识字段
     */
    String DEL_FLAG = "delFlag";


}
