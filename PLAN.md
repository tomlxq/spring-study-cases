mybatis.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl

# 多线程、线程池、锁

# TSF

# 单元测试、PowerMock

# guava、集合

# Java8

# Specifacation

抽象类命名使用Abstract或Base开头；异常类命名使用Exception结尾；测试类命名以它要测试的类名开始，以Test结尾。

包名统一使用小写，点分隔符之间有且仅有一个自然语义的英语单词。包名统一使用单数形式，但是类名如果有复数含义，类名可以使用复数形式

各层命名规约：
A) Service/DAO层方法命名规约
1） 获取单个对象的方法用get作前缀。
2） 获取多个对象的方法用list作前缀。
3） 获取统计值的方法用count作前缀。
4） 插入的方法用save/insert作前缀。
5） 删除的方法用remove/delete作前缀。
6） 修改的方法用update作前缀。
B) 领域模型命名规约
1） 数据对象：xxxDO，xxx即为数据表名。
2） 数据传输对象：xxxDTO，xxx为业务领域相关的名称。
3） 展示对象：xxxVO，xxx一般为网页名称。
4） POJO是DO/DTO/BO/VO的统称，禁止命名成xxxPOJO。

对于Service和DAO类，基于SOA的理念，暴露出来的服务一定是接口，内部的实现类用Impl的后缀与接口区别

应依赖使用日志框架SLF4J中的API，使用门面模式的日志框架

推荐阿里代码规范：
格式化模板文件下载地址：https://github.com/alibaba/p3c/tree/master/p3c-formatter
唯品会通用代码格式化模板 https://github.com/vipshop/vjtools/tree/master/standard/formatter

```


```