
## ip addr 发现网卡名称
在/etc/sysconfig/network-scripts/目录下找网络配置
`cat /etc/sysconfig/network-scripts/`

## 查看本机是否安装了sshd

```
rpm -qa| grep ssh
yum install -y  openssh-server
```
## 启动SSH服务

```
 systemctl stop sshd
systemctl start sshd
systemctl status sshd
netstat -antp | grep ssh
systemctl enable sshd
```
## 禁掉防火墙
```
setenforce 0
sed -i.bak "s/SELINUX=enforcing/SELINUX=disabled/g" /etc/selinux/config
systemctl disable firewalld.service
systemctl stop firewalld.service
iptables --flush
```