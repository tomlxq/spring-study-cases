## 添加MongoDB源
cat /etc/yum.repos.d/mongodb-org.repo 
[mongodb-org-4.0]
name=MongoDB Repository
baseurl=https://repo.mongodb.org/yum/redhat/$releasever/mongodb-org/4.0/x86_64/
gpgcheck=1
enabled=1
gpgkey=https://www.mongodb.org/static/pgp/server-4.0.asc

## 安装MongoDB
yum -y install mongodb-org
此命令将安装mongodb-org，包含以下内容：
mongodb-org-server - 标准的MongoDB服务端程序（既守护程序），以及相应的init脚本和配置
mongodb-org-mongos - MongoDB Shard集群服务端程序（守护进程）
mongodb-org-shell - MongoDB shell，用于通过命令行与MongoDB交互
mongodb-org-tools - 包含一些用于恢复，导入和导出数据的基本工具，以及其他各种功能。


## 配置MongoDB
MongoDB的配置文件位于/etc/mongod.conf，并以YAML格式编写。大多数设置在文件中都有非常好（便于理解）的注释。我们概述了以下默认选项:

systemLog 指定各种日志记录选项，解释如下：
* destination 告诉MongoDB是将日志输出存储为文件或者是系统日志
* logAppend 指定守护程序重新启动时是否将新日志记录附加到现有日志的末尾（而不是创建备份并在重新启动时启动新日志）
* path告诉服务端程序（守护进程）发送日志信息到某个位置（/var/log/mongodb/mongod.log默认情况下）

storage 设置MongoDB如何存储数据，设置如下：
* dbPath指示数据库文件的存储位置（默认：/var/lib/mongo）
* journal.enabled 启用或禁用日志，以确保数据文件可以恢复

net 指定各种网络选项，具体如下：
* port 是MongoDB服务端（守护）程序监听的端口
* bindIP 指定MongoDB绑定的IP地址，因此它可以监听来自其他应用程序的连接


## 增加用户限制
vi /etc/security/limits.d/99-mongodb-nproc.conf

mongod soft nofile 64000
mongod hard nofile 64000
mongod soft nproc 64000
mongod hard nproc 64000

## 启动和停止MongoDB

systemctl start mongod
systemctl stop mongod
systemctl restart mongod
systemctl status mongod

设置开机时候MongoDB自动启动：
systemctl enable mongod

Check MongoDB version.
mongod --version

Use netstat command to check whether the MongoDB is listening on port “27017”
netstat -antup | grep -i 27017



Disable SELinux on CentOS or RHEL 5/5.1/5.2/5.3/5.4/5.5/5.6/5.7
vi /etc/selinux/config
Change SELinux=enforcing
to SELinux=disabled
This will disable SELinux on next reboot.
To disable SELinux without rebooting, use the following command.
setenforce 0
或
semanage port -a -t mongod_port_t -p tcp 27017

## 创建数据库用户

mongo
use admin
db.createUser({user: "root", pwd: "root", roles:[{role: "userAdminAnyDatabase", db: "admin"}]})
quit()



使用admin数据库进行身份验证：
mongo -u root -p --authenticationDatabase admin 
use user-data
demo使用user-data数据库的只读权限创建用户，并具有demoDB
db.createUser({user: "demo", pwd: "demo", roles:[{role: "read", db: "user-data"}, {role:"readWrite", db: "demoDB"}]})
quit()
作为demo用户，创建一个新数据库来存储常规用户数据以进行身份验证。
mongo -u demo -p --authenticationDatabase user-data

## 管理数据和集合
创建一个新数据库。
use demoDB
创建一个名为的新集合demoCollection
db.createCollection("demoCollection", {capped: false})
MongoDB接受输入以JSON对象的形式作为文档，如下所示。在a和b变量用于简化输入; 对象也可以通过函数直接插入。
var a = { name : "John Doe",  attributes: { age : 30, address : "123 Main St", phone : 8675309 }}
var b = { name : "Jane Doe",  attributes: { age : 29, address : "321 Main Rd", favorites : { food : "Spaghetti", animal : "Dog" } }}
db.demoCollection.insert(a) 
db.demoCollection.insert(b)
输出将列出包含当前工作数据库中数据的所有集合
show collections
使用find方法进行条件查询
db.demoCollection.find()
db.demoCollection.find({"name" : "John Doe"})
要查看可用选项或如何使用特定方法，请附加.help()到命令的末尾。
db.demoCollection.find().help()

可以远程联接
vi /etc/mongod.conf
net:
  port: 27017
  bindIp: 0.0.0.0 

查询数据库
show dbs
查看表
show collections
增删改查表中的数据
db.demoCollection.find()
db.demoCollection.find({name:"Jane Doe"})
db.demoCollection.update({name:"tom luo"},{$set:{name:"tom lxq"}},true)
db.demoCollection.remove({name:"tom lxq"})
db.demoCollection.insert({name:"tom"})
增加字段
db.demoCollection.update({name:"tom"},{$set:{age:23}},true);
删除字段
db.demoCollection.update({name:"tom"},{$unset:{age:23}});
删表
db.exampleCollection.drop()
 
db.demo.insert({name:"tom",age:23})
db.demo.update({name:"tom"},{$inc:{age:2}})