Vagrant是一个虚拟机管理软件，需要结合虚拟机软件来使用。使用Vagrant能迅速创建批量虚拟机环境。对于做实验的同学来说，可以说是利器。
官网地址：www.vagrantup.com

https://www.vagrantup.com/downloads.html

Vagrant的基本工作原理大致如下：

首先，通过读取配置文件，获知用户需要的环境的操作系统、网络配置、基础软件等信息；
然后，调用虚拟化管理软件的API（VMWare Fusion，Oracle VirtualBox, AWS, OpenStack等）为用户创建好基础环境；
最后，调用用户定义的安装脚本（shell，puppet，chef）安装好相应的服务和软件包；

Vagrant的主要应用场景
开发环境部署
作为开发人员可能会涉及到不同的开发语言和不同的包依赖，搭建开发环境总是一件很麻烦的事情，有些语言有强有力的项目构建工具支持，比如Java的Maven，而有些语言则没有这么方便的工具，比如Python。特别是随着时间的推移，开发环境也会变得很混乱。
Vagrant通过脚本文件的描述创建一个虚拟机实例，并通过shell脚本或puppet配置好开发环境，解决了开发环境的自动化搭建。同时，vagrant创建的开发环境也能被轻松的清理和共享，特别是对于一个团队，构建标准的开发环境将变得很轻松。
测试环境部署
对于测试环节中的集成测试，特别是分布式系统的集成测试，测试环境的搭建也是一个费时费力的工作。Vagrant支持多个实例的部署，可以在单机上创建多个虚拟机实例进行自动化的集成测试。如果单机的测试环境还不够大，也可以将这个工作交给AWS和OpenStack这样的云去完成。




安装
1. 安装Provider
我们使用VirtualBox作为虚拟化的Provider，下载并安装VirtualBox即可。
https://www.virtualbox.org/wiki/Downloads

2.安装Vagrant
安装Vagrant
Vagrant提供了windows，mac，deb和rpm的安装包
https://www.vagrantup.com/downloads.html

3.下载package.box

去vagrant官网下载一个package.box 文件，box文件就是一个系统的镜像文件 :
http://www.vagrantbox.es/

4. 创建项目

* 创建一个文件夹
D:\ocm\box

* 把虚拟机加载到box容器中
下载好之后，在该目录下执行命令加载镜像文件到Vagrant中去:
vagrant box add centos7 .\centos-7.0-x86_64.box
centos7是给虚拟机起的名字 ,随意写。然后可以通过以下命令查看，当前vagrant下有那些可用
vagrant box list

* 初始化虚拟机
在你想要创建虚拟机的目录下，执行以下命令进行初始化
vagrant init box centos/7 D:/ocm/centos_new/CentOS-7-x86_64-Vagrant-1902_01.VirtualBox.box
会生成一个Vagrantfile文件,该文件就是Vagrant的配置文件。
vagrantfile配置信息：
```
Vagrant.configure("2") do |config|
  config.vm.box = "centos/7"
  config.vm.hostname = "docker-training"
  config.vm.box_url = "centos/7"
  config.vm.network "forwarded_port",guest:8080,host:7000
  # config.vm.synced_folder "../data", "/vagrant_data"
  config.vm.provider "virtualbox" do |vb|
    vb.memory = "4096"
	vb.name = "centos7_new"
  end 
  config.vm.provision "shell", inline: <<-SHELL
	yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
	yum install -y docker
	systemctl enable docker
	systemctl start docker
  SHELL
end
```
其中config.vm.box指定了所使用的box，如果该box不存在于本地，vagrant将会自动从config.vm.box_url处下载并添加到本地。
端口转发 将本机具体端口转发到虚拟机端口　
# host=本机端口，guest=虚拟机端口
config.vm.network "forwarded_port", host:8888, guest:81
共享目录    将本机具体目录和虚拟机共享
三种可选共享方式：
　　1. Basic Usage # 基础共享方式【默认】
　　2. NFS # Mos系统 共享目录方式
　　3. SMB # Windows系统 共享目录方式
主要是为了使本机的文件更改之后能够更快的同步到虚拟机中，更快的生效　
# 本机目录同步到虚拟机目录 :nfc[可选项]
config.vm.synced_folder "/Users/vincent/code/", "/home/www", :nfc => true
注意：
#   1. 需要配置私有网络IP
#   2. Nginx 的sendfile on 需要改为 off；不然同步目录速度较慢
网络配置　　三种网络配置方式

公有网络
# 要和本机网段一致
config.vm.network "public_network", ip: "192.168.33.10"
私有网络
config.vm.network "private_network", ip: "192.168.33.10"

* 配置provisioning脚本
我们通常在安装完操作系统后希望能装一些软件或做一些配置，provisioning脚本正好能完成这个工作。比如完成操作系统安装后自动安装vim和git。

编辑Vagrantfile，添加一行
  config.vm.box_url = "http://files.vagrantup.com/precise64.box"
  # 添加下面的这行
  config.vm.provision "shell", path: "provision.sh"
这一行指定了provision使用shell脚本，shell脚本位于与Vagrantfile同目录下的provision.sh

创建provision.sh

sudo apt-get install vim git -y

5.启动实例
在linux-dev目录下运行vagrant up，vagran就会启动由该目录下Vagrantfile指定的虚拟机实例。

首先，vagrant会去本地查找box，如果没有就从远程下载（从s3上下载很慢，可以先用迅雷离线下载到本地，然后再通过vagrant box add命令来添加）；

然后，vagrant就会启动虚拟机，做一些网络配置，并将当前目录挂载到虚拟机的/vagrant下，使其能在虚拟机和物理机直接共享。

最后，vagrant会开始provisioning的过程，为虚拟机配置基础的软件(只在第一次启动时进行，以后可通过vagrant provision命令触发)。

vagrant up

自动安装Guest Additions
在命令行：
$ vagrant plugin install vagrant-vbguest


6. SSH登陆
使用vagrant ssh命令可以登陆到虚拟机上，进行相应的操作
记住对应的ssh连接是http://127.0.0.1:2222
密码：vagrant
通过vagrant ssh启动系统
su - 
vagrant的centos镜像
初始密码账户 可能是：
账户 密码
vagrant vagrant
root vagrant

7. 关闭实例
关闭实例可以使用三种方式vagrant suspending, vagrant halt, vagrant destroy。
suspending，暂停虚拟机，保存虚拟机当前的状态（内存和磁盘均不释放），可以使用vagrant up命令恢复运行；
halt，关机，虚拟机停止运行，但是虚拟机实例保留，不销毁，可以理解为是正常的关机；
destroy，销毁虚拟机，虚拟机的实例被销毁;




vagrant常用命令：

# 查看已有的box
$ vagrant box list

# 新建加一个box
$ vagrant box add [此次镜像名称] [源镜像]

# 删除指定box
$ vagrant box remove [名称]

# 初始化配置vagrantfile 初始化box的操作，会生成vagrant的配置文件Vagrantfile 
$ vagrant init

# 启动虚拟机
$ vagrant up

# ssh登陆虚拟机 通过 ssh 登录本地环境所在虚拟机 (Windows不支持使用此指定登录)
$ vagrant ssh

$ 挂起虚拟机 
# vagrant suspend

# 重启虚拟机 改了 Vagrantfile 后，使之生效（相当于先 halt，再 up） 
$ vagrant reload

# 关闭虚拟机
$ vagrant halt

# 查看虚拟机状态
$ vagrant status

# 删除虚拟机
$ vagrant destroy

# 打包当前环境下为 box镜像  打包命令，可以把当前的运行的虚拟机环境进行打包 
$ vagrant paskage --output xxx.box

vagrant box remove 删除相应的box
vagrant plugin 用于安装卸载插件
vagrant status 获取当前虚拟机的状态 
vagrant global-status 显示当前用户Vagrant的所有环境状态
vagrant --help


1. ssh failed Permission denied (publickey,gssapi-keyex,gssapi-with-mic).
/etc/ssh/sshd_config
PasswordAuthentication yes

service sshd restart

相关资源: 
官网：http://www.vagrantup.com
文档: http://docs.vagrantup.com/v2/
Box：http://www.vagrantbox.es/
docker国内仓库（网易云仓库）
网易云仓库地址： 
https://c.163.com/hub#/m/home/
国内docker hub的加速镜像服务
阿里云加速器
https://cr.console.aliyun.com/#/imageSearch
daocloud
https://www.daocloud.io/mirror#accelerator-doc
网易云镜像仓库
https://c.163.com/#/m/library/
时速云镜像服务
https://hub.tenxcloud.com/
https://github.com/yangzhares/mysql-spring-boot-todo.git
https://github.com/holms/vagrant-centos7-box/releases/download/7.1.1503.001/CentOS-7.1.1503-x86_64-netboot.box




