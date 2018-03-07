package io.igordonxiao.pgdao;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

/**
 * 抽象DAO
 */
public class Dao {

    /**
     * 插入对象
     *
     * @param model
     */
    public <T extends AbstractModel> T save(AbstractModel model, Class<T> modelClass) {
        Map<String, Object> pojo = ObjectKit.getKeyAndValue(model);
        if (pojo.containsKey(Conf.ID_FIELD)) pojo.remove(Conf.ID_FIELD);
        Object[] fields = pojo.keySet().toArray();
        if (fields.length <= 0) throw new Ggexcption("不能插入空对象");
        String fieldNames = DbKit.joinArray(fields, Conf.SERPERATOR_COMMA);
        ArrayList<Object> values = new ArrayList<Object>();
        for (Object field : fields) values.add(pojo.get(field));
        // 处理创建时间，更新时间，删除标识
        if (pojo.containsKey(Conf.ID_FIELD)) pojo.remove(Conf.ID_FIELD);
        if (pojo.containsKey(Conf.CREATED_DATE)) pojo.remove(Conf.CREATED_DATE);
        if (pojo.containsKey(Conf.UPDATED_DATE)) pojo.remove(Conf.CREATED_DATE);
        QueryRunner qr = new QueryRunner();
        Connection conn = DbKit.getConnection();
        ScalarHandler<Integer> scalarHandler = new ScalarHandler<Integer>();
        // 处理创建时间，更新时间，删除标识
        Date curDate = new Date();
        Timestamp timestamp = new Timestamp(curDate.getTime());
        values.add(timestamp);
        values.add(timestamp);
        values.add(DelFlag.NORMAL.ordinal());
        String sql = "insert into public." + model.getClass().getSimpleName() + "(" + fieldNames + ", " + Conf.CREATED_DATE + ", " + Conf.UPDATED_DATE + ", " + Conf.DEL_FLAG + ") values(" + DbKit.genQuestionMarks(fields.length) + ",?,?,?);";
        try {
            Integer incretedVal = qr.insert(conn, sql, scalarHandler, values.toArray());
            DbKit.closeConnection(conn);
            return this.findById(Long.valueOf(incretedVal), modelClass);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 分页获取对象
     * @param modelClass
     * @param size
     * @param page
     * @param orderField
     * @param orderFlag
     * @param <T>
     * @return
     */
    public <T extends AbstractModel> PageResult<T> findPage(Class<T> modelClass, Integer size, Integer page, String orderField, OrderFlag orderFlag, String whereClause) {
        String tableName = modelClass.getSimpleName();
        QueryRunner qr = new QueryRunner();
        Connection conn = DbKit.getConnection();
        BeanListHandler<T> modelHandler = new BeanListHandler<T>(modelClass);
        ScalarHandler<Long> countHandler = new ScalarHandler<Long>();
        if (size == null) size = Conf.DB_PAGE_SIZE;
        if (page == null) page = Conf.DB_PAGE_DEFAULT;
        if (orderField == null) orderField = Conf.ID_FIELD;
        if (orderFlag == null) orderFlag = OrderFlag.DESC;
        try {
            String projection = " from public." + tableName + " where "+ Conf.DEL_FLAG+" = ? "+(whereClause == null ? "": " and " + whereClause);
            String sql = "select * " + projection +" order by "+orderField+" "+orderFlag.toString()+"  limit ? offset ?;";
            List<T> modelList = qr.query(conn, sql, modelHandler, DelFlag.NORMAL.ordinal() , size, (page - 1) * size);
            String countSql = "select count("+ Conf.ID_FIELD+") " + projection;
            Long count = qr.query(conn, countSql, countHandler, DelFlag.NORMAL.ordinal());
            PageResult<T> pageResult = new PageResult<T>();
            pageResult.setCount(count != null ? count : 0);
            pageResult.setPage(page);
            pageResult.setSize(size);
            pageResult.setData(modelList);
            DbUtils.close(conn);
            return pageResult;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 分页获取对象
     * @param modelClass
     * @param <T>
     * @return
     */
    public <T extends AbstractModel> PageResult<T> findPage(Class<T> modelClass, Integer size, Integer page,  String orderField, OrderFlag orderFlag) {
        return this.findPage(modelClass, size, page, orderField, orderFlag, null);
    }

    /**
     * 分页获取对象
     * @param modelClass
     * @param <T>
     * @return
     */
    public <T extends AbstractModel> PageResult<T> findPage(Class<T> modelClass, Integer size, Integer page) {
        return this.findPage(modelClass, size, page, null, null, null);
    }

    /**
     * 分页获取对象
     * @param modelClass
     * @param <T>
     * @return
     */
    public <T extends AbstractModel> PageResult<T> findPage(Class<T> modelClass, Integer size, Integer page, String whereClause) {
        return this.findPage(modelClass, size, page, null, null, whereClause);
    }

    /**
     * 分页获取对象
     * @param modelClass
     * @param <T>
     * @return
     */
    public <T extends AbstractModel> PageResult<T> findPage(Class<T> modelClass, String whereClause) {
        return this.findPage(modelClass, null, null, null, null, whereClause);
    }

    /**
     * 分页获取对象
     * @param modelClass
     * @param <T>
     * @return
     */
    public <T extends AbstractModel> PageResult<T> findPage(Class<T> modelClass) {
        return this.findPage(modelClass, null, null, null, null, null);
    }



    /**
     * 根据ID获取对象
     *
     * @param id
     * @param modelClass
     * @param <T>
     * @return
     */
    public <T extends AbstractModel> T findById(Long id, Class<T> modelClass) {
        String tableName = modelClass.getSimpleName();
        QueryRunner qr = new QueryRunner();
        Connection conn = DbKit.getConnection();
        String sql = "select * from public." + tableName + " where "+ Conf.ID_FIELD+" = ? and "+ Conf.DEL_FLAG+" = ?;";
        BeanListHandler<T> handler = new BeanListHandler<T>(modelClass);
        try {
            List<T> users = qr.query(conn, sql, handler, id, DelFlag.NORMAL.ordinal());
            DbUtils.close(conn);
            if (users.size() > 0) return users.get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 更新对象
     *
     * @param model
     * @param <T>
     * @return
     */
    public <T extends AbstractModel> T update(AbstractModel model, Class<T> modelClass) {
        Long id = model.getId();
        if (id == null || id <= 0) throw new Ggexcption("更新对象的ID不正确");
        T modelDb = this.findById(id, modelClass);
        if (modelDb == null) throw new Ggexcption("未查询到ID为" + id + "的" + modelClass.getSimpleName() + "对象");
        QueryRunner qr = new QueryRunner();
        Connection conn = DbKit.getConnection();
        Map<String, Object> pojo = ObjectKit.getKeyAndValue(model);
        StringBuilder sb = new StringBuilder();
        Object[] fields = pojo.keySet().toArray();
        int fieldLen = fields.length;
        for (int i = 0; i < fieldLen; i++) sb.append(fields[i] + ((i < fieldLen - 1) ? "=?, " : "=?"));
        ArrayList<Object> values = new ArrayList<Object>();
        for (Object field : fields) values.add(pojo.get(field));
        // 处理创建时间，更新时间，删除标识
        if (pojo.containsKey(Conf.UPDATED_DATE)) pojo.remove(Conf.CREATED_DATE);
        Date curDate = new Date();
        Timestamp timestamp = new Timestamp(curDate.getTime());
        values.add(timestamp);
        String sql = "update public." + model.getClass().getSimpleName() + " set " + sb.toString() + ", "+ Conf.UPDATED_DATE+"=? where "+ Conf.ID_FIELD+"=?";
        values.add(id);
        try {
            qr.update(conn, sql, values.toArray());
            DbKit.closeConnection(conn);
            return this.findById(id, modelClass);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 根据ID删除对象
     * @param id
     * @param modelClass
     * @param <T>
     */
    public <T extends AbstractModel> void deleteById(Long id, Class<T> modelClass) {
        if (id == null || id <= 0) throw new Ggexcption("删除对象的ID不正确");
        T modelDb = this.findById(id, modelClass);
        if (modelDb == null) throw new Ggexcption("未查询到ID为" + id + "的" + modelClass.getSimpleName() + "对象");
        QueryRunner qr = new QueryRunner();
        Connection conn = DbKit.getConnection();
        String sql = "update public." + modelClass.getSimpleName() + " set "+ Conf.UPDATED_DATE+"=?, "+Conf.DEL_FLAG+"=? where "+ Conf.ID_FIELD+"=?;";
        // 更新时间
        Date curDate = new Date();
        Timestamp timestamp = new Timestamp(curDate.getTime());
        try {
            qr.update(conn, sql, timestamp, DelFlag.REMOVED.ordinal(), id);
            DbKit.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}


