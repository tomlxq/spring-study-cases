package com.tom.demo.jdbc.framework;

import com.tom.demo.utils.GenericsUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import javax.sql.DataSource;
import java.io.Serializable;
import java.util.List;

public abstract class BaseDaoSupport<T extends Serializable, PK extends Serializable> {
    private DataSource dataSourceReadOnly;
    private DataSource dataSourceWrite;

    private SimpleJdbcTemplate jdbcTemplateWrite;
    private SimpleJdbcTemplate jdbcTemplateReadOnly;
    private String tableName;
    private EntityOperation<T> eo;

    public BaseDaoSupport() {
        Class<T> entityClass = GenericsUtils.getSuperClassGenricType(getClass(), 0);
        eo = new EntityOperation<T>(entityClass);
        this.tableName = eo.tableName;
    }

    protected void setDataSourceWrite(DataSource dataSourceWrite) {
        this.dataSourceWrite = dataSourceWrite;
        jdbcTemplateWrite = new SimpleJdbcTemplate(dataSourceWrite);
    }

    protected void setDataSourceReadOnly(DataSource dataSourceReadOnly) {
        this.dataSourceReadOnly = dataSourceReadOnly;
        jdbcTemplateReadOnly = new SimpleJdbcTemplate(dataSourceReadOnly);
    }

    /**
     * Getter for property 'jdbcTemplateWrite'.
     *
     * @return Value for property 'jdbcTemplateWrite'.
     */
    public SimpleJdbcTemplate getJdbcTemplateWrite() {
        return jdbcTemplateWrite;
    }

    /**
     * Getter for property 'jdbcTemplateReadOnly'.
     *
     * @return Value for property 'jdbcTemplateReadOnly'.
     */
    public SimpleJdbcTemplate getJdbcTemplateReadOnly() {
        return jdbcTemplateReadOnly;
    }

    protected List<T> find(QueryRule queryRule) {
        QueryRuleSqlBuilder queryRuleSqlBuilder = new QueryRuleSqlBuilder(queryRule);
        return null;
    }

    protected List<T> select(QueryRule queryRule) {
        QueryRuleSqlBuilder builder = new QueryRuleSqlBuilder(queryRule);
        String whereSql = builder.getWhereSql();
        String orderSql = builder.getOrderSql();
        StringBuffer sql = new StringBuffer("select " + this.eo.allColumn + " from " + this.tableName);
        if (!(whereSql == null || whereSql.trim().length() == 0)) {
            sql.append(" where " + whereSql);
        }
        if (!(orderSql == null || orderSql.trim().length() == 0)) {
            sql.append(" order by " + orderSql);
        }
        return this.jdbcTemplateReadOnly.query(sql.toString(), this.eo.rowMapper, builder.getValues());
    }

    protected int insert(T entity) throws Exception {
        return 0;
    }

    protected int insertAll(List<T> entityList) throws Exception {
        return 0;
    }
}
