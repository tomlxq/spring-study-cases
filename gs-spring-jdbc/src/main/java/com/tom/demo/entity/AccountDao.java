package com.tom.demo.entity;

import com.tom.demo.jdbc.framework.BaseDaoSupport;
import com.tom.demo.jdbc.framework.QueryRule;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.List;

@Repository
public class AccountDao extends BaseDaoSupport<Account, Long> {

    @Resource(name = "dataSource")
    public void setDataSource(DataSource dataSource) {
        this.setDataSourceReadOnly(dataSource);
        this.setDataSourceWrite(dataSource);
    }

    /**
     * @param name
     * @return
     */
    public List<Account> selectByName(String name) {
        //构建一个QureyRule 查询规则
        QueryRule queryRule = QueryRule.getInstance();
        //查询一个name= 赋值 结果，List
        queryRule.andEqual("name", name);
//		queryRule.
        //相当于自己再拼SQL语句
        return super.select(queryRule);
    }

    /**
     *
     */
    public int insert(Account entity) throws Exception {
        return super.insert(entity);
    }

    /**
     * @throws Exception
     */
    public int insertAll(List<Account> entityList) throws Exception {
        return super.insertAll(entityList);
    }


}
