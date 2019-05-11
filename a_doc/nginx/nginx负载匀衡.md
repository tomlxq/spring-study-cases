
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
`#access_log  off;`


#log_format myformat '$time_local | $remote_addr | $remote_user | $request | $status'
access_log  logs/host.access.8080.log  myformat;

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
gzip_buffers 4 16k;
gzip_comp_level 4;
gzip_min_length 500;
gzip_types text/css text/xml application/javascript;
```


综合演练
at upstream.conf 
```
upstream tomserver{
 server 192.168.238.150:8080;
 server 192.168.238.155:8080;
 server 192.168.238.160:8080;
}
```
cat www.tom.com.conf
```
server{
 listen 80;
 server_name www.tom.com;
 location / {
  proxy_pass http://tomserver;
  proxy_set_header X-Real_IP $remote_addr;
  proxy_send_timeout 60;
 }
 location ~ .*\.(js|css|png|jpg|gif){
   root /opt/tomcat/enabled/webapps/ROOT/;
 }
}
```
cat nginx.conf
```
#user  nobody;
worker_processes  2;
worker_cpu_affinity 01 10;
error_log /var/log/nginx/error.log warn;
worker_rlimit_nofile 10240; #too many files;

#error_log  logs/error.log;
#error_log  logs/error.log  notice;
#error_log  logs/error.log  info;

#pid        logs/nginx.pid;


events {
    use epoll; #select pool epoll kqueue
    worker_connections  1024;
    accept_mutex off;
}

http {
    include       mime.types;
    default_type  application/octet-stream;
    charset utf-8;
    access_log off;
    sendfile on;
    keepalive_timeout  65;
    gzip  on;
    gzip_min_length 5k;
    gzip_buffers 4 16k;
    gzip_comp_level 8;
    gzip_types text/css application/xml text/javascript image/jpeg image/png image/gif;
    gzip_vary on;
    proxy_temp_path /opt/tmp;
    proxy_cache_path /opt/tmp/cache levels=1:2 keys_zone=tom:200m max_size=1g;
    include /etc/nginx/conf.d/*.conf;
}
```