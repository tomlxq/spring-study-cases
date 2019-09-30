# Mysql安装与配置

## Linux下安装MySQL数据库(压缩包方式安装)

1、这里我将Mysql安装在/usr/local/mysql目录里面，也可以安装在其他地方;

mkdir /usr/local/mysql
2、下载MySQL压缩包

wget http://dev.MySQL.com/get/Downloads/MySQL-5.7/mysql-5.7.11-Linux-glibc2.5-x86_64.tar.gz 

// 如果上边的命令不行的话 可以使用下边的命令
curl -O -L http://dev.MySQL.com/get/Downloads/MySQL-5.7/mysql-5.7.11-Linux-glibc2.5-x86_64.tar.gz 
3、解压并复制

tar -xvf mysql-8.0.15-linux-glibc2.12-x86_64.tar.xz 
mkdir -p /usr/local/mysql  
mv mysql-8.0.15-linux-glibc2.12-x86_64/* /usr/local/mysql/
4、创建data目录

mkdir /usr/local/mysql/data
5、创建mysql用户组及其用户

groupadd mysql
useradd -r -g mysql mysql
6、初始化数据

复制代码
[root@localhost mysql] ./bin/mysqld --user=mysql --basedir=/usr/local/mysql/ --datadir=/usr/local/mysql/data/
2016-01-20 02:47:35 [WARNING] mysql_install_db is deprecated. Please consider switching to mysqld --initialize
2016-01-20 02:47:45 [WARNING] The bootstrap log isn't empty:
2016-01-20 02:47:45 [WARNING] 2016-01-19T18:47:36.732678Z 0 [Warning] --bootstrap is deprecated. Please consider using --initialize instead
2016-01-19T18:47:36.750527Z 0 [Warning] Changed limits: max_open_files: 1024 (requested 5000)
2016-01-19T18:47:36.750560Z 0 [Warning] Changed limits: table_open_cache: 431 (requested 2000)
复制代码
7、复制配置文件到 /etc/my.cnf

cp -a ./support-files/my-default.cnf /etc/my.cnf (选择y) 
8、MySQL的服务脚本放到系统服务中

复制代码
cp -a ./support-files/mysql.server /etc/init.d/mysqld
修改my.cnf文件

These are commonly set, remove the # and set as required.

basedir = /usr/local/mysql
datadir = /usr/local/mysql/data
port = 3306

server_id = .....

socket = /tmp/mysql.sock
character-set-server = utf8

Remove leading # to set options mainly useful for reporting servers.

The server defaults are faster for transactions and fast SELECTs.

Adjust sizes as needed, experiment to find the optimal values.

join_buffer_size = 128M

sort_buffer_size = 2M

read_rnd_buffer_size = 2M 

chmod +x /etc/init.d/mysqld    
[root@localhost support-files]# chkconfig --add mysqld
chkconfig --list mysqld


9、创建In

ln -s /usr/local/mysql/  /usr/bin/
10、启动服务

service mysqld start 
11、初始化密码

mysql5.7会生成一个初始化密码，在root中.mysql_secret文件中。

[root@localhost ~]# cat /root/.mysql_secret

Password set for user 'root@localhost' at 2017-03-16 00:52:34 

ws;fmT7yh0CM
12、登录并修改密码

[root@localhost ~]# mysql -u root -p

alter user root@localhost identified by 'tiger';

flush privileges;
13、退出重新登录，完成

复制代码
mysql> show databases;
+--------------------+
| Database           |
+--------------------+
| information_schema |
| mysql              |
| performance_schema |
| sys                |
+--------------------+
4 rows in set (0.11 sec)
复制代码
OK

source /etc/profile
export PATH=$PATH:/usr/local/mysql/bin:/usr/local/mysql/lib
echo $PATH
/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/root/bin:/usr/local/mysql/bin:/usr/local/mysql/lib
mysql -uroot -p



## Issuers

### 1. The server time zone value 'EDT' is unrecognized or represents more than one time zone.

终端显示的时间和MySql中显示的时间不一致，这就是问题所在。

使用 `server mysql start`命令启动mysql

在mysql中执行`show variables like '%time_zone%';`

输入`select nows();`

在终端执行`date`命令

在mysql中执行 `set time_zone=SYSTEM;`

执行 `set global time_zone='+8:00';`

执行 `flush privileges;`

### 2. 显示创建表结构

`show create table t_user\G;`