查看进程 
ps -aux | grep mongod
查看端口
netstat -anp | grep mongod
killall mongod



两台主机一主一从

三台主机，去中心化，随机选举(伪分布式副本集的数据M-S-S)
分片服务1（shard001）:
```
* 192.168.238.150:27001
* 192.168.238.150:27002
* 192.168.238.150:27003

cat /opt/mongodb/replica1/mongodb.cfg
dbpath=/opt/mongodb/replica1/data
logpath=/opt/mongodb/replica1/logs/mongodb.log
logappend=true
fork=true
bind_ip=192.168.238.150
port=27001
replSet=shard001

cat /opt/mongodb/replica2/mongodb.cfg
dbpath=/opt/mongodb/replica2/data
logpath=/opt/mongodb/replica2/logs/mongodb.log
logappend=true
fork=true
bind_ip=192.168.238.150
port=27002
replSet=shard001

cat /opt/mongodb/replica3/mongodb.cfg
dbpath=/opt/mongodb/replica3/data
logpath=/opt/mongodb/replica3/logs/mongodb.log
logappend=true
fork=true
bind_ip=192.168.238.150
port=27003
replSet=shard001
```
分别启动三台服务器
```
mongod -f /opt/mongodb/replica1/mongodb.cfg 
mongod -f /opt/mongodb/replica2/mongodb.cfg 
mongod -f /opt/mongodb/replica3/mongodb.cfg 
ps -aux | grep mongod
netstat -anp | grep mongod
```
配置复制集shard001
```
cfg={_id:"shard001",members:[{_id:0,host:'192.168.238.150:27001'},{_id:1,host:'192.168.238.150:27002'},{_id:2,host:'192.168.238.150:27003'}]}
rs.initiate(cfg)
rs.status();
```
三台主机，一主一从，一哨兵(M-S-A)
分片服务2（shard002）:
* 192.168.238.150:27017
* 192.168.238.155:27017
* 192.168.238.160:27017
```
cat /opt/mongodb/master/mongodb.cfg 
dbpath=/opt/mongodb/master/data
logpath=/opt/mongodb/master/logs/mongodb.log
logappend=true
fork=true
bind_ip=192.168.238.150
port=27017
replSet=shard002

cat /opt/mongodb/slave/mongodb.cfg 
dbpath=/opt/mongodb/slave/data
logpath=/opt/mongodb/slave/logs/mongodb.log
logappend=true
fork=true
bind_ip=192.168.238.155
port=27017
replSet=shard002

cat /opt/mongodb/slave/mongodb.cfg 
dbpath=/opt/mongodb/slave/data
logpath=/opt/mongodb/slave/logs/mongodb.log
logappend=true
fork=true
bind_ip=192.168.238.160
port=27017
replSet=shard002
 ```
 
分别启动三台服务器
```
mongod -f /opt/mongodb/master/mongodb.cfg 
mongod -f /opt/mongodb/slave/mongodb.cfg 
mongod -f /opt/mongodb/slave/mongodb.cfg 
```
配置复制集shard002
```
cfg={_id:"shard002",members:[{_id:0,host:'192.168.238.150:27017',priority:9},{_id:1,host:'192.168.238.155:27017',priority:1},{_id:2,host:'192.168.238.160:27017',arbiterOnly:true}]}
rs.initiate(cfg)
rs.status();
rs.printReplicationInfo()
```

三台配置服务器
配置服务（config）:
* 192.168.238.150:28001
* 192.168.238.150:28002
* 192.168.238.150:28003
```
cat /opt/mongodb/configsvr1/mongodb.cfg           
dbpath=/opt/mongodb/configsvr1/data
logpath=/opt/mongodb/configsvr1/logs/mongodb.log
configsvr=true
logappend=true
fork=true
bind_ip=192.168.238.150
port=28001
replSet=configrs

cat /opt/mongodb/configsvr2/mongodb.cfg           
dbpath=/opt/mongodb/configsvr2/data
logpath=/opt/mongodb/configsvr2/logs/mongodb.log
configsvr=true
logappend=true
fork=true
bind_ip=192.168.238.150
port=28002
replSet=configrs

cat /opt/mongodb/configsvr3/mongodb.cfg           
dbpath=/opt/mongodb/configsvr3/data
logpath=/opt/mongodb/configsvr3/logs/mongodb.log
configsvr=true
logappend=true
fork=true
bind_ip=192.168.238.150
port=28003
replSet=configrs
```

配置复制集
(1) 配置复制集config
mongo 192.168.238.150:28001/admin
初始化
```
cfg={_id:"configrs",members:[{_id:0,host:'192.168.238.150:28001'},{_id:1,host:'192.168.238.150:28002'},{_id:2,host:'192.168.238.150:28003'}]}
rs.initiate(cfg)
```

sh.status();
show dbs


配置一台路由服务器
路由服务（mongos）：
* 192.168.238.150:30000
* 192.168.238.155:30000
* 192.168.238.160:30000
```
cat /opt/mongodb/routersvr/mongodb.cfg 
configdb=configrs/192.168.238.150:28001,192.168.238.150:28002,192.168.238.150:28003
logpath=/opt/mongodb/routersvr/logs/mongodb.log
logappend=true
fork=true
bind_ip=192.168.238.150
port=30000

mongos -f /opt/mongodb/routersvr1/mongodb.cfg

mongo 192.168.238.150:30000/admin
sh.addShard("shard001/192.168.238.150:27001");
sh.addShard("shard002/192.168.238.150:27017");
db.runCommand({listshards : 1}); 
```
(1) 创建用户
```
mongo 192.168.238.150:30000/admin
use admin;
db.createUser({
  user : 'root',
  pwd : '123456',
  roles : [
    'clusterAdmin',
    'dbAdminAnyDatabase',
    'userAdminAnyDatabase',
    'readWriteAnyDatabase'
  ]
});
db.auth("root", "123456");
```
注意：当连接到Mongos服务时，创建的用户将保存在config服务中。
(2) 插入数据
* 启用数据集分片
```
use admin;
db.runCommand( { shardcollection : "test.testlxq",key : {"_id": "hashed"} } )
use test;
for (var i = 1; i <= 10000; i++) db.testlxq.save({id:i, name: "tom"+i, age:"28"});
```
数据集将被分隔为块，存储在不同的分片上，shard key由文档的一个或者多个物理键值组成.







激活
```
use admin;
sh.shardCollection("testdb.testcom",{name:"hashed"});
use testdb
for(var i=0;i<100;i++) db.testcom.insert({name:"tom",age:i});
db.testcom.drop()
#db.testcom.createIndex( { name: "hashed" } )
```

查看有那些数据库
`show dbs`
切换数据库
`use testdb`
显示数据库下的集合
`show collections`
查看集合testcom的数据
`db.testcom.find();`
mongos指定config库启动
mongos --port port --configdb=ip:port 
连接mongos，指定分片addshard
`db.runCommand({"addshard":"ip:port",allowLocal:true}) `

开启分片功能：
`db.runCommand({"enablesharding":"test"}) `
指定集合中分片的片键：
`db.runCommand({"shardCollection":"test.ActivityResult","key":{"ActivityId":1}})`
查看分片状态：
`db.printShardingStatus()`

db.shards.find();
db.chunks.find();
db.mongos.find();