# wget获取

`wget https://download.redis.io/releases/redis-7.0.7.tar.gz`

# 编译安装

```shell
tar -zxf redis-7.0.7.tar.gz -C /tools
cd redis-7.0.7
make
make install PREFIX=/tools/redis
```

# 配置环境变量

```shell
vi ~/.bash_profile
REDIS_HOME=/tools/redis
PATH=$PATH:$REDIS_HOME/bin
source ~/.bash_profile
```

# 启动和停止

```shell
#实际是去找/tools/redis/bin的这个启动语句,并使用redis配置文件
./redis-server /tools/redis-7.0.7/redis.conf
#/tools/redis/bin的这个预计进行停止
./redis-cli shutdown
```

# 测试

<BR>开启另外一个ssh窗口进行测试

```shell
redis-cli
set name potato
get name
```

# 修改配置文件

<BR>redis.conf文件说明

```shell
vi /tools/redis-7.0.7/redis.conf
#设置后台启动，如果不是后台启动，每次推出redis就关闭了
daemonize yes
#开启密码保护，注释则不需要密码
requirepass 密码
#设置端口号
port 端口号
#允许访问的ip，改为0.0.0.0就是所有ip均可
bind 127.0.0.1 -::1
bind 0.0.0.0
#修改安全模式，修改为no
protected-mode no
```

# 设开机置自启

```shell
cd /usr/lib/systemd/system
touch redis.service
vi redis.service
```

```text
[Unit]
Description=redis-server
After=network.target

[Service]
Type=forking

ExecStart=/tools/redis/bin/redis-server /tools/redis-7.0.7/redis.conf
PrivateTmp=true

[Install]
WantedBy=multi-user.target
```

```shell

#重载系统服务
systemctl daemon-reload
#设置开机自启
systemctl enable redis.service
#取消开机自启
systemctl disabled redis.service
#启动服务
systemctl start redis.service
#停止服务
systemctl stop redis.service
#查看服务状态
systemctl status redis.service
ps -ef |grep redis
```

# 检查端口

```shell
#检查6379端口是否开启，此处我已经开启
firewall-cmd --list-ports
#如果端口未开启，具体操作如下：
firewall-cmd --zone=public --add-port=6379/tcp --permanent
#重启防火墙
firewall-cmd --reload
#再次查看端口是否开启
firewall-cmd --list-ports
systemctl status firewalld
systemctl stop firewalld
```

> redis中主要有三个参数来进行安全控制的，也是我们最常用的三个。
> bind
> ①这个参数默认值是127.0.0.1，也就是只允许redis所在机器访问redis。
> ②如果我们的应用服务和redis服务不在一个机器我们就需要修改这个参数为0.0.0.0，这表示允许所有人都可以访问这个redis
> protected-mode
> ①这个参数的默认值是yes，也就是默认开启保护模式，当开启了此模式，限制为本地访问。
> ②如果设置为no，就关闭了保护模式，允许所有外部的网络直接访问redis服务。
> requirepass
> ①这个参数是用来设置redis密码的，默认情况下时被注释掉的，即没有密码。
> ②如果需要设置密码那就取消注释，注意删除#和requirepass之间的空格。

# 上线部署

<BR>一般情况上线部署的时候我们配置如下

```text
#本机ip或者改成应用服务所在的ip
bind 127.0.0.1
#保护模式保持默认开启即可
protected-mode yes
#redis密码设置
requirepass redispwd
线下调试
#我们要允许开发环境也可以连接到redis
bind 0.0.0.0
#保护模式保持默认开启即可
protected-mode yes
#redis密码设置
requirepass redispwd
```
