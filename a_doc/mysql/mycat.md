Mycat源码篇 : 起步，Mycat源码阅读调试环境搭建
安装成功后，我们在本地某个文件路径下，鼠标右键打开Git Bash，执行以下命令将代码克隆到当前文件路径:
git clone https://github.com/MyCATApache/Mycat-Server.git

从github上面clone的源码在src/main/resources目录下存在mycat运行所需要配置的文件，有schema.xml、rule.xml和server.xml。

mysql数据库准备
登录到本地mysql，创建需要的database和table:

启动mycat
到了这一步，所有的准备已经到位，在Eclipse的mycat项目下找到MycatStartup这个类，Run As -> Java Application即可跑起一个mycat server。默认mycat server端口8066，管理端口9066。

3. (可选)编译mycat软件包
我们可以通过mycat源码来编译得到mycat软件包，只要我们安装了maven即可。方法如下:
cmd进入到mycat源码根目录，即pom.xml所在目录，执行以下命令进行编译打包:
mvn package -Dmaven.test.skip=true
-Dmaven.test.skip=true表示忽略mycat的单元测试的执行，这样可以节省编译打包时间。
等待执行成功后，可以在mycat源码根目录的target子目录里面，看到如下格式的压缩包:
Mycat-server-${version}-${buildtime}-${platform}.tar.gz
其中${version}表示mycat版本号，${buildtime}为我们执行编译打包的日期时间，${platform}表示使用的操作系统平台，取值有win、linux、mac、solaris和unix。这样我们就可以拿对应的软件包到对应的平台上去部署了。



CREATE TABLE `company1` (
	`id` INT (11) NOT NULL,
	`name` VARCHAR (100) DEFAULT NULL,
	PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8;

CREATE TABLE `company2` (
	`id` INT (11) NOT NULL,
	`name` VARCHAR (100) DEFAULT NULL,
	PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8;

CREATE TABLE `company3` (
	`id` INT (11) NOT NULL,
	`name` VARCHAR (100) DEFAULT NULL,
	PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8;


explain select * from companysub;

insert into companysub(id,name) values(1,'a');
insert into companysub(id,name) values(2,'b');
insert into companysub(id,name) values(3,'c');
insert into companysub(id,name) values(4,'d');
insert into companysub(id,name) values(5,'e');
insert into companysub(id,name) values(6,'f');