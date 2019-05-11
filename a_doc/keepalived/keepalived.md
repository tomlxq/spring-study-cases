 ## keepalived安装
 ```
 wget https://www.keepalived.org/software/keepalived-2.0.15.tar.gz -c /opt/
 scp keepalived-2.0.15.tar.gz  root@192.168.238.155:/opt/
 tar zxvf keepalived-2.0.15.tar.gz 
 mkdir -p /opt/keepalived
  cd keepalived-2.0.15   
 ./configure --prefix=/opt/keepalived --sysconf=/etc
 make & make install
 
ln -s /opt/keepalived/sbin/keepalived /sbin
cp keepalived/etc/init.d/keepalived /etc/init.d/
chkconfig --add keepalived
chkconfig keepalived on

cd /etc/keepalived/
cp keepalived.conf keepalived.conf_bak
```
## 另一台机
```
mkdir -p /etc/nginx/conf.d/
scp root@192.168.238.150:/etc/nginx/conf.d/*.conf /etc/nginx/conf.d/
scp root@192.168.238.150:/opt/nginx-1.16.0.tar.gz .
tar -zxvf nginx-1.16.0.tar.gz 
 cd nginx-1.16.0
 ./configure --prefix=/opt/nginx --with-http_ssl_module
 mkdir -p /opt/nginx/logs/
 make & make install
cp conf/nginx.conf conf/nginx.conf_bak
scp root@192.168.238.150:/opt/nginx/conf/nginx.conf /opt/nginx/conf/nginx.conf
```
## 启动与停止

两台机子分别启动tomcat,nginx,keepalived
```
systemctl start tomcat
systemctl status tomcat

systemctl start keepalived
systemctl status keepalived
```
## 日志配置
打开keepalived日志
```
etc/sysconfig/keepalived
KEEPALIVED_OPTIONS="-D -d -S 0"
/etc/rsyslog.conf
local0.* /var/log/keepalived.log
```
重启日志
service rsyslog restart

http://192.168.238.150:8080/nginx/getIP
## 实战

### nginx 负载均衡
```
cat /etc/nginx/conf.d/upstream.conf 
upstream tomserver{
 server 192.168.238.150:8080;
 server 192.168.238.155:8080;
 server 192.168.238.160:8080;
}
```

```
cat /etc/nginx/conf.d/www.tom.com.conf 
server{
 listen 80;
 server_name localhost;
 location / {
  proxy_pass http://tomserver;
  proxy_set_header X-Real_IP $remote_addr;
  proxy_send_timeout 60;
 }
 location ~ .*\.(js|css|png|jpg|gif){
   root /opt/tomcat/enabled/webapps/ROOT/;
 }
}
server{
 listen 443 ssl;
 server_name www.tom.com;
 ssl_certificate /opt/nginx/conf/server.crt;
 ssl_certificate_key /opt/nginx/conf/server.key;
 location / {
  proxy_pass http://tomserver;
 }
}
```

### keepalived的failover

```
cat /etc/keepalived/keepalived.conf
! Configuration File for keepalived

global_defs {
   router_id LVS_DEVEL
   vrrp_skip_check_adv_addr
   vrrp_strict
   vrrp_garp_interval 0
   vrrp_gna_interval 0
}

vrrp_instance VI_1 {
    state MASTER
    interface ens33
    virtual_router_id 51
    priority 100
    advert_int 1
    authentication {
        auth_type PASS
        auth_pass 1111
    }
    virtual_ipaddress {
        192.168.238.200
    }
}

virtual_server 192.168.238.200 80 {
    delay_loop 6
    lb_algo rr
    lb_kind NAT
    persistence_timeout 50
    protocol TCP

    real_server 192.168.238.150 80 {
         weight 1
         TCP_CHECK{
          connect_timeout 3
          delay_before_retry 3
          connect_port 80
         }
        }
    }
}
```

## Q&A

1. 配置keepalived ping不通 解决办法
vi /etc/keepalived/keepalived.conf
把这个注释就可以了
vrrp_strict