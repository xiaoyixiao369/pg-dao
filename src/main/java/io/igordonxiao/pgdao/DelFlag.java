package io.igordonxiao.pgdao;

/**
 * É¾³ý±êÊ¶Àà
 */
public enum DelFlag {
    NORMAL(0), REMOVED(1);
    private Integer flag;
    private DelFlag(int flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return String.valueOf(this.flag);
    }
}
