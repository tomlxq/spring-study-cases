# 二、性能优化之Mysql

## 锁

### row-level

InnoDB-行锁

```mysql
CREATE USER 'demo'@'%' IDENTIFIED BY 'demo';
GRANT ALL ON *.* TO 'demo'@'%';
```

会话1：关闭自动提交

```mysql
set autocommit=0;
update user set User='jack' where user='demo';
```

会话2：关闭自动提交

```mysql
-- 可以看到用户名没有变
select * from user; 
-- 试着更新，程序挂起
update user set User='jack1' where user='demo';
```

会话1：提交事务

```mysql
commit;
```

#### pros

精度小

#### cons

获取，释放所做的工作更多

容易发生死锁

#### 实现Innodb

* 共享锁

  ```mysql
  lock table user read;
  unlock tables;
  ```

* 排他锁

  ```mysql
  lock table user write;
  unlock tables;
  ```

* 间隙锁

  通过指向数据记录的第一个索引键之前和最后一个索引键之后的空域空间上标记锁定信息来实现的

* 锁优化

  * 尽可能让所有的数据检索都通过索引来完成

  * 合理设计索引

  * 减少基于范围的数据是检索过滤条件

  * 尽量控制事务的大小

  * 业务允许的情况下，尽量使用较低级别的事务隔离

  

### table-level

#### pros

* 实现逻辑简单
* 获取，释放快
* 避免死锁

#### cons

粒度太大，并发不够高

#### 实现MyISAM

### page-level

介于row和table之间

实现BerkeleyDB



|                  | 共享锁（S） | 排他锁（X） | 意向共享锁（IS） | 意向排他锁（IX） |
| ---------------- | ----------- | ----------- | ---------------- | ---------------- |
| 共享锁（S）      | 兼容        | 冲突        | 兼容             | 冲突             |
| 排他锁（X）      | 冲突        | 冲突        | 冲突             | 冲突             |
| 意向共享锁（IS） | 兼容        | 冲突        | 兼容             | 兼容             |
| 意向排他锁（IX） | 冲突        | 冲突        | 兼容             | 兼容             |

## 优化

### 原则

query 语句的优化思路和原则主要提现在以下几个方面：

1. 优化更需要优化的Query
2. 定位优化对象的性能瓶颈
3. 明确的优化目标
4. 从 Explain 入手
5. 多使用profile
6. 永远用小结果集驱动大的结果集
7. 尽可能在索引中完成排序
8. 只取出自己需要的Columns
9. 仅仅使用最有效的过滤条件
10. 尽可能避免复杂的Join和子查询

### QEP Query Execution Plan

#### 关于explain

用法：`explain select * from tables1 where 1 ...`

先看一下在 MySQL Explain功能中给我们展示的各种信息的解释：
◆ ID： Query Optimizer 所选定的执行计划中查询的序列号；
◆ Select_type：所使用的查询类型，主要有以下这几种查询类型
　　◇ DEPENDENT SUBQUERY：子查询中内层的第一个SELECT，依赖于外部查询的结果集；
　　◇ DEPENDENT UNION：子查询中的UNION，且为UNION中从第二个SELECT开始的后面所有SELECT，同样依赖于外部查询的结果集；
　　◇ PRIMARY：子查询中的最外层查询，注意并不是主键查询；
　　◇ SIMPLE：除子查询或者UNION之外的其他查询；
　　◇ SUBQUERY：子查询内层查询的第一个SELECT，结果不依赖于外部查询结果集；
　　◇ UNCACHEABLE SUBQUERY：结果集无法缓存的子查询；
　　◇ UNION： UNION语句中第二个SELECT开始的后面所有SELECT，第一个SELECT为PRIMARY
　　◇ UNION RESULT： UNION 中的合并结果；
◆ Table：显示这一步所访问的数据库中的表的名称；
◆ Type：告诉我们对表所使用的访问方式，主要包含如下集中类型；
　　◇ all：全表扫描
　　◇ const： 读常量，且最多只会有一条记录匹配，由于是常量，所以实际上只需要读一次；
　　◇ eq_ref： 最多只会有一条匹配结果，一般是通过主键或者唯一键索引来访问；
　　◇ fulltext：
　　◇ index：全索引扫描；
　　◇ index_merge：查询中同时使用两个（或更多）索引，然后对索引结果进行merge之后再读取表数据；
　　◇ index_subquery：子查询中的返回结果字段组合是一个索引（或索引组合），但不是一个主键或者唯一索引；
　　◇ rang：索引范围扫描；
　　◇ ref： Join语句中被驱动表索引引用查询；
　　◇ ref_or_null：与ref的唯一区别就是在使用索引引用查询之外再增加一个空值的查询；
　　◇ system：系统表，表中只有一行数据；
　　◇ unique_subquery：子查询中的返回结果字段组合是主键或者唯一约束；
◆ Possible_keys： 该查询可以利用的索引. 如果没有任何索引可以使用，就会显示成null，这一项内容对于优化时候索引的调整非常重要；
◆ Key： MySQL Query Optimizer 从 possible_keys 中所选择使用的索引；
◆ Key_len：被选中使用索引的索引键长度；
◆ Ref： 列出是通过常量（ const），还是某个表的某个字段（如果是join）来过滤（通过key）
的；
◆ Rows： MySQL Query Optimizer 通过系统收集到的统计信息估算出来的结果集记录条数；
◆ Extra：查询中每一步实现的额外细节信息，主要可能会是以下内容：
　　◇ Distinct：查找distinct 值，所以当mysql找到了第一条匹配的结果后，将停止该值的查询而转为后面其他值的查询；
　　◇ Full scan on NULL key：子查询中的一种优化方式，主要在遇到无法通过索引访问null值的使用使用；
　　◇ Impossible WHERE noticed after reading const tables： MySQL Query Optimizer 通过收集到的统计信息判断出不可能存在结果；
　　◇ No tables： Query 语句中使用 FROM DUAL 或者不包含任何 FROM子句；
　　◇ Not exists：在某些左连接中 MySQL Query Optimizer 所通过改变原有 Query 的组成而使用的优化方法，可以部分减少数据访问次数；
　　◇ Range checked for each record (index map: N)：通过 MySQL 官方手册的描述，当MySQL Query Optimizer 没有发现好的可以使用的索引的时候，如果发现如果来自前面的表的列值已知，可能部分索引可以使用。对前面的表的每个行组合， MySQL检查是否可以使用range或index_merge访问方法来索取行。
　　◇ Select tables optimized away：当我们使用某些聚合函数来访问存在索引的某个字段的时候， MySQL Query Optimizer 会通过索引而直接一次定位到所需的数据行完成整个查
询。当然，前提是在 Query 中不能有 GROUP BY 操作。如使用MIN()或者MAX（）的时候；
　　◇ Using filesort：当我们的 Query 中包含 ORDER BY 操作，而且无法利用索引完成排序操作的时候， MySQL Query Optimizer 不得不选择相应的排序算法来实现。
　　◇ Using index：所需要的数据只需要在 Index 即可全部获得而不需要再到表中取数据；
　　◇ Using index for group-by：数据访问和 Using index 一样，所需数据只需要读取索引即可，而当 Query 中使用了 GROUP BY 或者 DISTINCT 子句的时候，如果分组字段也在索引中， Extra中的信息就会是 Using index for group-by；
　　◇ Using temporary：当 MySQL 在某些操作中必须使用临时表的时候，在 Extra 信息中就会出现Using temporary 。主要常见于 GROUP BY 和 ORDER BY 等操作中。
　　◇ Using where：如果我们不是读取表的所有数据，或者不是仅仅通过索引就可以获取所有需要的数据，则会出现 Using where 信息；
　　◇ Using where with pushed condition：这是一个仅仅在 NDBCluster存储引擎中才会出现的信息，而且还需要通过打开 Condition Pushdown 优化功能才可能会被使用。控制参数
为 engine_condition_pushdown 

#### 关于 profiling

用法：

```mysql
-- 开启：
set profiling=1;
-- 查询
select * from tables1 where 1;
-- 打印查询语句的概要信息
show profiles;
-- 显示以上show profiles内容的Query_ID的具体信息
show profile cpu,block io for query ${Query_ID};
```

#### Join

Nested Loop Join

##### 优化

* 永远小结果集驱动大的结果集

* 保证被驱动表上的Join条件字段已经被索引

* Join Buffer   

  查看 join_buffer_size 大小`show variables like 'join_buffer_size'`

#### Order by

`show variables like '%sort_buffer_size'`

* 实现

  * 有序

  * 无序

    排序字段和指针在Sort buffer排序，然后用指针去取数据

    排序字段和所有数据全部取出，排序字段+指针 Sort Buffer排序（其它数据放到内存中），指针到内存去取数据然后返回。（节省IO，耗内存，空间换时间）

* 优化

  * 索引顺序一致，不需要排序
  * 加大max_length_for_sort_data从而使用第二种方法（排序只需要针对需要排序的字段）
  * 内存不足时，去掉不必要的返回字段
  * 增大sort_buffer_size，减少在排序过程中对需要排序的数据进行分段

#### group by

#### distinct

#### limit 

