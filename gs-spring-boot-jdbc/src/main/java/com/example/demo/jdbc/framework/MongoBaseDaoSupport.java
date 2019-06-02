package com.example.demo.jdbc.framework;

import com.example.demo.utils.GenericsUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.io.Serializable;
import java.util.List;

@Slf4j
public abstract class MongoBaseDaoSupport<T extends Serializable, PK extends Serializable> {

    private MongoTemplate mongoTemplate;

    protected void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    private EntityOperation<T> eo;
    private String tableName;

    public MongoBaseDaoSupport() {
        Class<T> entityClass = GenericsUtils.getSuperClassGenricType(getClass(), 0);
        eo = new EntityOperation<T>(entityClass);
        this.tableName = eo.tableName;
    }

    /**
     * 根据主键值，获得一个对象
     *
     * @param pk
     * @return
     */
    protected T get(PK pk) {
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.andEqual(this.eo.pkColum, pk);
        MongoQueryRuleSqlBuilder builder = new MongoQueryRuleSqlBuilder(queryRule);
        Query query = builder.getQuery();
        return this.mongoTemplate.findOne(query, eo.entityClass);
    }

    /**
     * 1. 想办法把mongoTemplate注入进来
     * 2. 将queryRule操作转换成mongoTemplate的操作
     * 3. MongoDB要实现读写分离及动态数据源如何路由
     *
     * @param queryRule
     * @return
     */
    protected List<T> find(QueryRule queryRule) {

        MongoQueryRuleSqlBuilder builder = new MongoQueryRuleSqlBuilder(queryRule);
        /*String whereSql = builder.getWhereSql();
        String orderSql = builder.getOrderSql();
        StringBuffer sql = new StringBuffer("select " + this.eo.allColumn + " from " + this.tableName);
        if (!(whereSql == null || whereSql.trim().length() == 0)) {
            sql.append(" where " + whereSql);
        }
        if (!(orderSql == null || orderSql.trim().length() == 0)) {
            sql.append(" order by " + orderSql);
        }*/
        Query query = builder.getQuery();

        LOGGER.info("{}", eo.entityClass);
        return this.mongoTemplate.find(query, eo.entityClass);
    }


}
