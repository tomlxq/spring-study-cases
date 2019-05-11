sudo useradd -r -m -U -d /opt/tomcat -s /bin/false tomcat
wget http://mirror.bit.edu.cn/apache/tomcat/tomcat-9/v9.0.19/bin/apache-tomcat-9.0.19.tar.gz
sudo tar xf apache-tomcat-9*.tar.gz -C /opt/tomcat
sudo ln -s /opt/tomcat/apache-tomcat-9.0.19 /opt/tomcat/enabled   
sudo chown -RH tomcat: /opt/tomcat/enabled
sudo chmod o+x /opt/tomcat/enabled/bin/

/etc/systemd/system/tomcat.service

[Unit]
Description=Tomcat 9 servlet container
After=network.target

[Service]
Type=forking

User=tomcat
Group=tomcat

Environment="JAVA_HOME=/opt/jdk1.8.0_191"
Environment="JAVA_OPTS=-Djava.security.egd=file:///dev/urandom -Djava.awt.headless=true"

Environment="CATALINA_BASE=/opt/tomcat/enabled"
Environment="CATALINA_HOME=/opt/tomcat/enabled"
Environment="CATALINA_PID=/opt/tomcat/enabled/temp/tomcat.pid"
Environment="CATALINA_OPTS=-Xms512M -Xmx1024M -server -XX:+UseParallelGC"

ExecStart=/opt/tomcat/enabled/bin/startup.sh
ExecStop=/opt/tomcat/enabled/bin/shutdown.sh

[Install]
WantedBy=multi-user.target

sudo systemctl daemon-reload
sudo systemctl start tomcat
sudo systemctl status tomcat
sudo systemctl enable tomcat
sudo firewall-cmd --zone=public --permanent --add-port=8080/tcp
sudo firewall-cmd --reload

vi /opt/tomcat/enabled/conf/tomcat-users.xml
<role rolename="admin-gui"/>
<role rolename="manager-gui"/>
<user username="admin" password="admin" roles="admin-gui,manager-gui"/>



scp root@192.168.238.150:/opt/apache-tomcat-9.0.19.tar.gz  .


https://linuxize.com/post/how-to-install-tomcat-9-on-centos-7/