package com.example.demo.dao;

import com.example.demo.domain.Hotel;
import com.example.demo.jdbc.framework.MongoBaseDaoSupport;
import com.example.demo.jdbc.framework.QueryRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
@Slf4j
public class HotelMongoDao extends MongoBaseDaoSupport<Hotel, String> {
    @Resource(name="mongoTemplate")
    protected void setMongoTemplate(MongoTemplate mongoTemplate) {
        super.setMongoTemplate(mongoTemplate);
    }

    public List<Hotel> findByName(String name) {
        //构建一个QureyRule 查询规则
        QueryRule queryRule = QueryRule.getInstance();
        //查询一个name= 赋值 结果，List
        queryRule.andEqual("name", name);
        LOGGER.info("==================={}",name);
        return super.find(queryRule);
    }

    public Hotel findById(String id) {
        return super.get(id);
    }
}
