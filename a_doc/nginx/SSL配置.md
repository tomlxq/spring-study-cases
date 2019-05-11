## 配置支持ssl
### 配置一个SSL证书
```
cd /opt/nginx-1.16.0
yum -y install openssl openssl-devel
./configure --prefix=/opt/nginx --with-http_ssl_module
make & make install
查看是否有安装ssl模块成功。
./sbin/nginx -V
```
### 产生证书和私钥
```
openssl genrsa -des3 -out server.key 1024
Generating RSA private key, 1024 bit long modulus
........++++++
...............................................................................................................................................++++++
e is 65537 (0x10001)
Enter pass phrase for server.key:
Verifying - Enter pass phrase for server.key:

openssl req -new -key server.key -out server.csr
Enter pass phrase for server.key:
You are about to be asked to enter information that will be incorporated
into your certificate request.
What you are about to enter is what is called a Distinguished Name or a DN.
There are quite a few fields but you can leave some blank
For some fields there will be a default value,
If you enter '.', the field will be left blank.
-----
Country Name (2 letter code) [XX]:CN
State or Province Name (full name) []:
Locality Name (eg, city) [Default City]:
Organization Name (eg, company) [Default Company Ltd]:tom.com
Organizational Unit Name (eg, section) []:
Common Name (eg, your name or your server's hostname) []:www.tom.com
Email Address []:

Please enter the following 'extra' attributes
to be sent with your certificate request
A challenge password []:123456
An optional company name []:

cp server.key server.key.org

openssl rsa -in server.key.org -out server.key
Enter pass phrase for server.key.org:
writing RSA key

openssl x509 -req -days 365 -in server.csr -signkey server.key -out server.crt
Signature ok
subject=/C=CN/L=Default City/O=tom.com/CN=www.tom.com
Getting Private key
```
### 配置ngnix
```
cat /etc/nginx/conf.d/www.tom.com.conf 增加
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
### 修改tomcat配置

/opt/tomcat/enabled/conf/server.xml 
```
   <Connector executor="tomcatThreadPool"
               port="8080" protocol="HTTP/1.1"
               connectionTimeout="20000"
             proxyPort="443" redirectPort="443" />

```