## 安装
```
wget http://nginx.org/download/nginx-1.16.0.tar.gz
tar zxvf nginx-1.16.0.tar.gz 
./configure --prefix=/opt/nginx
make & make install
```
## nginx启停
```
 ./nginx
./nginx -c /opt/nginx/conf/nginx.conf
ps -ef | grep nginx
kill -TERM 12554
kill -QUIT 12554
./nginx -s stop
./nginx -s reload
./nginx -s quit
```
## 安装过程可能缺少的包
```
yum -y install pcre-devel
yum install zlib-devel -y
yum install openssl-devel -y
```
## 基于域名的配置

C:\Windows\System32\drivers\etc\hosts
```
192.168.238.150 www.tom.com
```
cat ../conf/nginx.conf
```
server {
	listen 80;
	server_name www.tom.com;
	location /{
			root html/domain;
			index index.html index.htm;
	}
}
```


## 基于port的配置
```
cat ../conf/nginx.conf
server {
	listen 8080;
	server_name localhost;
	location /{
			root html/port;
			index index.html index.htm;
	}
}
```

## 日志格式定义

log_format 标识 格式
```
log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
#                  '$status $body_bytes_sent "$http_referer" '
#                  '"$http_user_agent" "$http_x_forwarded_for"';
```
访问日志 路径 标识
```
#access_log  logs/access.log  main;
```
## 关掉日志
```
`#access_log  off;`


#log_format myformat '$time_local | $remote_addr | $remote_user | $request | $status'
access_log  logs/host.access.8080.log  myformat;
```
## URL重写
```
location /{
                rewrite ^/ http://www.baidu.com;
}

location / {
		rewrite '^/images/([a-z]{3})/(.*)\.(png)$' /tom?file=$2.$3;
		set $image_file $2;
		set $image_type $3;
}
location /tom {
		root html;
		try_files /$arg_file /image404.html;
}
location =/image404.html {
		return 404 "image not find exception";
}
```
## 配置静态资源
```
location ~ \.(png|jpg|js|css|gif)$ {
		root html/static;
		expires 5m;
}
```
## 开启压缩功能

```
gzip on;
gzip_buffers 4 16k; 每次申请4倍16K空间大小
gzip_comp_level 4; 压缩级别为1~9
gzip_min_length 500; 500字节长度以下就不压缩
gzip_types text/css text/xml application/javascript; 压缩的类型
```