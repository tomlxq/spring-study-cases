# spring boot+spring cloud整体回顾（1）

## JPA(java persistence API)

java 持久化的标准

EntityManager来管理

### 增加Maven依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

### 增加实体类

```java
/**
 * Person实体
 *
 * @author TomLuo
 * @date 2019/7/9
 */
@Entity
@Table(name = "person")
@Data
public class Person {
    @Id
    @GeneratedValue
    long id;
    @Column
    String name;
}
```

### 服务类

```java
/**
 * {@link Person}服务
 *
 * @author TomLuo
 * @date 2019/7/9
 */
public class PersonService {
    /**
     * 通过标准的ＪＰＡ方式注入
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * {@link Person}的存储
     * 
     * @param person
     */
    public void save(Person person) {
        entityManager.persist(person);
    }
}
```

### 增加JPA数据源

> 查找配置项
>
> `org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration`
>
> 里面有
>
> ```java
> @Configuration
> @ConditionalOnClass({ DataSource.class, EmbeddedDatabaseType.class })
> @EnableConfigurationProperties(DataSourceProperties.class)
> @Import({ DataSourcePoolMetadataProvidersConfiguration.class, DataSourceInitializationConfiguration.class })
> public class DataSourceAutoConfiguration {
> }
> ```
>
> 查看DataSourceProperties配置属性

```properties
spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://192.168.238.165:3306/demo?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.schema=classpath*:schema.sql
spring.datasource.data=classpath*:data.sql
spring.datasource.initialization-mode=always
```

### 增加Mysql JDBC的依赖

```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <scope>runtime</scope>
</dependency>
```

### 通过JPA自动创建表

```properties
# JPA配置
spring.jpa.show-sql=true
spring.jpa.generate-ddl=true
```
### 创建仓储接口

```java
@Repository
public interface PersonRepository extends PagingAndSortingRepository<Person, Integer> {

}
```
### 实现`person`分页

```java
@GetMapping("/person/findPage")
public Page<Person> findAll(Pageable pageable) {
    return personRepository.findAll(pageable);
}
```
> mysql分页
>
> ```sql
> select * from person limit 2 offset 0
> ```
> 查看分页源码
> `org.springframework.data.web.PageableArgumentResolver`
> `org.springframework.data.web.PageableHandlerMethodArgumentResolver`
> 默认显示
> `static final Pageable DEFAULT_PAGE_REQUEST = PageRequest.of(0, 20);`
> localhost:10001/person/findPage?size=2&page=0
> size指定分页大小  page指定当前第几个，从0开始