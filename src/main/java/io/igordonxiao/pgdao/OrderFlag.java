package io.igordonxiao.pgdao;

/**
 * 数据库查询排序方式
 */
public enum OrderFlag {

    ASC("ASC"), DESC("DESC");
    private String flag;
    private OrderFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return this.flag;
    }
}
