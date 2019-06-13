

## 查看并清掉容器

docker ps
docker rm -f mysql todo
查看并清掉镜象
docker images
docker rmi -f 6201a17848ce

## 安装Docker Compose

安装指令参看官网：
https://github.com/docker/compose/releases/

curl -L https://github.com/docker/compose/releases/download/1.25.0-rc1/docker-compose-`uname -s`-`uname -m` -o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose

## 查看运行的容器

```bash
docker-compose ps
docker ps 
```

## 构建容器

```bash
docker-compose up -d
docker-compose up --build -d
```

## 查看容器

```bash
docker inspect todo
...
"Aliases": [
                        "c83a890abf51",
                        "todo"
            ]
...
```

可以的看到其中有别名，执行ping命令，可以查看到它们的通信信息

```bash
docker exec -ti todo ping mysql
PING mysql (172.18.0.2): 56 data bytes
64 bytes from 172.18.0.2: icmp_seq=0 ttl=64 time=0.059 ms
64 bytes from 172.18.0.2: icmp_seq=1 ttl=64 time=0.153 ms
64 bytes from 172.18.0.2: icmp_seq=2 ttl=64 time=0.045 ms
64 bytes from 172.18.0.2: icmp_seq=3 ttl=64 time=0.135 ms
```

docker-compose logs todo

进到容器查看进程

```bash
docker exec -ti todo bash
ps -ewwf
UID        PID  PPID  C STIME TTY          TIME CMD
todo         1     0 36 21:43 ?        00:00:11 java -Djava.security.egd=file:/dev/./urandom -jar TodoDemo.jar
todo        17     0  0 21:43 ?        00:00:00 bash
todo        44    17  0 21:44 ?        00:00:00 ps -ewwf
```

docker容器，在docker-compose up，如果镜象存在，则不会创建。

docker-compose stop
docker-compose rm
docker-compose images
docker-compose up -d

运行busybox

```bash
docker run --rm -d busybox sleep 100
afc9de695611180cb72811612bd64e4acc112ddad76a2d09c5320c4ad405ba7f
brctl show
bridge name     bridge id               STP enabled     interfaces
br-db461d83d702         8000.0242dad11638       no
docker0         8000.024285c63268       no              vethbd82559
```

查看网桥

```bash
brctl show
bridge name     bridge id               STP enabled     interfaces
br-db461d83d702         8000.0242dad11638       no
docker0         8000.024285c63268       no              vethbd82559
ifconfig 
bvethbd82559: flags=4163<UP,BROADCAST,RUNNING,MULTICAST>  mtu 1500
        inet6 fe80::38fa:8ff:fe3b:d933  prefixlen 64  scopeid 0x20<link>
        ether 3a:fa:08:3b:d9:33  txqueuelen 0  (Ethernet)
        RX packets 7  bytes 578 (578.0 B)
        RX errors 0  dropped 0  overruns 0  frame 0
        TX packets 7  bytes 578 (578.0 B)
        TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0
```

查看IP信息

```bash
docker ps

docker exec -ti b6564d1b7f60 ifconfig
eth0      Link encap:Ethernet  HWaddr 02:42:AC:11:00:02  
          inet addr:172.17.0.2  Bcast:0.0.0.0  Mask:255.255.0.0
          inet6 addr: fe80::42:acff:fe11:2/64 Scope:Link
          UP BROADCAST RUNNING MULTICAST  MTU:1500  Metric:1
          RX packets:8 errors:0 dropped:0 overruns:0 frame:0
          TX packets:8 errors:0 dropped:0 overruns:0 carrier:0
          collisions:0 txqueuelen:0 
          RX bytes:648 (648.0 B)  TX bytes:648 (648.0 B)

lo        Link encap:Local Loopback  
          inet addr:127.0.0.1  Mask:255.0.0.0
          inet6 addr: ::1/128 Scope:Host
          UP LOOPBACK RUNNING  MTU:65536  Metric:1
          RX packets:0 errors:0 dropped:0 overruns:0 frame:0
          TX packets:0 errors:0 dropped:0 overruns:0 carrier:0
          collisions:0 txqueuelen:1 
          RX bytes:0 (0.0 B)  TX bytes:0 (0.0 B)
```





brctl show
-bash: brctl: command not found
yum -y install bridge-utils







## Docker的网络模型

1. ### None

```bash
docker run --rm -d --net none busybox sleep 100
a2d04815c2f87808b0b063c77773a84a07111c62bb74581d801b129fb2ae2a69

docker ps
CONTAINER ID        IMAGE               COMMAND             CREATED             STATUS              PORTS               NAMES
a2d04815c2f8        busybox             "sleep 100"         45 seconds ago      Up 45 seconds                           hopeful_shannon
docker exec -ti a2d04815c2f8 ping 192.168.238.165
PING 192.168.238.165 (192.168.238.165): 56 data bytes
ping: sendto: Network is unreachable

docker exec -ti f7642f047b38 ifconfig
lo        Link encap:Local Loopback  
          inet addr:127.0.0.1  Mask:255.0.0.0
          inet6 addr: ::1/128 Scope:Host
          UP LOOPBACK RUNNING  MTU:65536  Metric:1
          RX packets:0 errors:0 dropped:0 overruns:0 frame:0
          TX packets:0 errors:0 dropped:0 overruns:0 carrier:0
          collisions:0 txqueuelen:1 
          RX bytes:0 (0.0 B)  TX bytes:0 (0.0 B)
```

2. ### Bridge

   ```bash
   [root@localhost ~]# docker network ls
   NETWORK ID          NAME                    DRIVER              SCOPE
   4fa3d6fe8bef        bridge                  bridge              local
   db461d83d702        docker-compose_mynet1   bridge              local
   de56d5f70b55        host                    host                local
   32306c37b78d        none                    null                local
   [root@localhost ~]# docker network help
   
   Usage:  docker network COMMAND
   
   Manage networks
   
   Options:
         --help   Print usage
   
   Commands:
     connect     Connect a container to a network
     create      Create a network
     disconnect  Disconnect a container from a network
     inspect     Display detailed information on one or more networks
     ls          List networks
     prune       Remove all unused networks
     rm          Remove one or more networks
   
   Run 'docker network COMMAND --help' for more information on a command.
   [root@localhost ~]# docker network inspect bridge
   [
       {
           "Name": "bridge",
           "Id": "4fa3d6fe8bef111a4438e49aede59e9ad528fa03ae500ddeb9d990b12147e89b",
           "Created": "2019-06-01T16:00:56.555951916-04:00",
           "Scope": "local",
           "Driver": "bridge",
           "EnableIPv6": false,
           "IPAM": {
               "Driver": "default",
               "Options": null,
               "Config": [
                   {
                       "Subnet": "172.17.0.0/16",
                       "Gateway": "172.17.0.1"
                   }
               ]
           },
           "Internal": false,
           "Attachable": false,
           "Containers": {},
           "Options": {
               "com.docker.network.bridge.default_bridge": "true",
               "com.docker.network.bridge.enable_icc": "true",
               "com.docker.network.bridge.enable_ip_masquerade": "true",
               "com.docker.network.bridge.host_binding_ipv4": "0.0.0.0",
               "com.docker.network.bridge.name": "docker0",
               "com.docker.network.driver.mtu": "1500"
           },
           "Labels": {}
       }
   ]
   ```

   创建两个容器

   ```bash
   [root@localhost ~]# docker run -d --rm --name c1 busybox sleep 500
   59becb635501807bd836c06b7daf7757cb4477e7aaba3a7235bce9f5f02fe872
   [root@localhost ~]# docker run -d --rm --name c2 busybox sleep 500 
   164ef141e0473a69cff9ee4dbb1f9e68c2e7e7190e636b1399059ddb61fd395e
   
   [root@localhost ~]# docker network inspect bridge                 
   [
       {
           "Name": "bridge",
           "Id": "4fa3d6fe8bef111a4438e49aede59e9ad528fa03ae500ddeb9d990b12147e89b",
           "Created": "2019-06-01T16:00:56.555951916-04:00",
           "Scope": "local",
           "Driver": "bridge",
           "EnableIPv6": false,
           "IPAM": {
               "Driver": "default",
               "Options": null,
               "Config": [
                   {
                       "Subnet": "172.17.0.0/16",
                       "Gateway": "172.17.0.1"
                   }
               ]
           },
           "Internal": false,
           "Attachable": false,
           "Containers": {
               "164ef141e0473a69cff9ee4dbb1f9e68c2e7e7190e636b1399059ddb61fd395e": {
                   "Name": "c2",
                   "EndpointID": "2960f9a3170aa9d88d7d13bed6612b058653450cc585d02e85217787a63755e5",
                   "MacAddress": "02:42:ac:11:00:03",
                   "IPv4Address": "172.17.0.3/16",
                   "IPv6Address": ""
               },
               "59becb635501807bd836c06b7daf7757cb4477e7aaba3a7235bce9f5f02fe872": {
                   "Name": "c1",
                   "EndpointID": "a2a595fccd3e996da213eef6534181e6f310e6562849c9cc35183e0ccf6e1992",
                   "MacAddress": "02:42:ac:11:00:02",
                   "IPv4Address": "172.17.0.2/16",
                   "IPv6Address": ""
               }
           },
           "Options": {
               "com.docker.network.bridge.default_bridge": "true",
               "com.docker.network.bridge.enable_icc": "true",
               "com.docker.network.bridge.enable_ip_masquerade": "true",
               "com.docker.network.bridge.host_binding_ipv4": "0.0.0.0",
               "com.docker.network.bridge.name": "docker0",
               "com.docker.network.driver.mtu": "1500"
           },
           "Labels": {}
       }
   ]
   [root@localhost ~]# docker exec -ti c1 ping 172.17.0.3
   PING 172.17.0.3 (172.17.0.3): 56 data bytes
   64 bytes from 172.17.0.3: seq=0 ttl=64 time=0.314 ms
   64 bytes from 172.17.0.3: seq=1 ttl=64 time=0.142 ms
   [root@localhost ~]# docker exec -ti c2 ping 172.17.0.2
   PING 172.17.0.2 (172.17.0.2): 56 data bytes
   64 bytes from 172.17.0.2: seq=0 ttl=64 time=0.115 ms
   64 bytes from 172.17.0.2: seq=1 ttl=64 time=0.158 ms
   64 bytes from 172.17.0.2: seq=2 ttl=64 time=0.149 ms
   
   ```

   创建一个网桥

   ```shell
   [root@localhost ~]# docker network create --driver bridge mynet
   945eaafc715d5e4fc88b7d31d3f309765cac81cda8215898bd1515e3f017e204
   [root@localhost ~]# docker network ls
   NETWORK ID          NAME                    DRIVER              SCOPE
   4fa3d6fe8bef        bridge                  bridge              local
   db461d83d702        docker-compose_mynet1   bridge              local
   de56d5f70b55        host                    host                local
   945eaafc715d        mynet                   bridge              local
   32306c37b78d        none                    null                local
   [root@localhost ~]# docker network inspect mynet
   [
       {
           "Name": "mynet",
           "Id": "945eaafc715d5e4fc88b7d31d3f309765cac81cda8215898bd1515e3f017e204",
           "Created": "2019-06-10T08:21:11.101423635-04:00",
           "Scope": "local",
           "Driver": "bridge",
           "EnableIPv6": false,
           "IPAM": {
               "Driver": "default",
               "Options": {},
               "Config": [
                   {
                       "Subnet": "172.19.0.0/16",
                       "Gateway": "172.19.0.1"
                   }
               ]
           },
           "Internal": false,
           "Attachable": false,
           "Containers": {},
           "Options": {},
           "Labels": {}
       }
   ]
   ```

   不同网桥相互是ping不通的

   ```bash
   [root@localhost ~]# docker run -d --rm --name c2 busybox sleep 500
   e116b06108b11a409888f954381d9d17a3a8fdb3b1e4133332480a23fb8d9454
   [root@localhost ~]# docker run -d --rm --name c1 busybox sleep 500 
   e162186addac3e1922f6d0314c793be65a60f67eee2d2a282dace38ede985f4a
   [root@localhost ~]# docker run -d --rm --name c3 --net mynet busybox sleep 500   
   9aadca8afb2c17248306da662c3a32639100d9b51f0aa379eea69afa26cd7634
   [root@localhost ~]# docker network ls
   NETWORK ID          NAME                    DRIVER              SCOPE
   4fa3d6fe8bef        bridge                  bridge              local
   db461d83d702        docker-compose_mynet1   bridge              local
   de56d5f70b55        host                    host                local
   945eaafc715d        mynet                   bridge              local
   32306c37b78d        none                    null                local
   [root@localhost ~]# docker ps
   CONTAINER ID        IMAGE               COMMAND             CREATED             STATUS              PORTS               NAMES
   9aadca8afb2c        busybox             "sleep 500"         21 seconds ago      Up 20 seconds                           c3
   e162186addac        busybox             "sleep 500"         35 seconds ago      Up 34 seconds                           c1
   e116b06108b1        busybox             "sleep 500"         39 seconds ago      Up 38 seconds                           c2
   [root@localhost ~]# docker exec -ti c3 ifconfig
   eth0      Link encap:Ethernet  HWaddr 02:42:AC:13:00:02  
             inet addr:172.19.0.2  Bcast:0.0.0.0  Mask:255.255.0.0
             inet6 addr: fe80::42:acff:fe13:2/64 Scope:Link
             UP BROADCAST RUNNING MULTICAST  MTU:1500  Metric:1
             RX packets:16 errors:0 dropped:0 overruns:0 frame:0
             TX packets:8 errors:0 dropped:0 overruns:0 carrier:0
             collisions:0 txqueuelen:0 
             RX bytes:1296 (1.2 KiB)  TX bytes:648 (648.0 B)
   
   lo        Link encap:Local Loopback  
             inet addr:127.0.0.1  Mask:255.0.0.0
             inet6 addr: ::1/128 Scope:Host
             UP LOOPBACK RUNNING  MTU:65536  Metric:1
             RX packets:0 errors:0 dropped:0 overruns:0 frame:0
             TX packets:0 errors:0 dropped:0 overruns:0 carrier:0
             collisions:0 txqueuelen:1 
             RX bytes:0 (0.0 B)  TX bytes:0 (0.0 B)
   
   [root@localhost ~]# docker exec -ti c1 ping 172.19.0.2
   PING 172.19.0.2 (172.19.0.2): 56 data bytes
   ^C
   --- 172.19.0.2 ping statistics ---
   5 packets transmitted, 0 packets received, 100% packet loss
   -- 将c1绑定到mynet
   [root@localhost ~]# docker network connect mynet c1  
   [root@localhost ~]# docker exec -ti c1 ping 172.19.0.2
   PING 172.19.0.2 (172.19.0.2): 56 data bytes
   64 bytes from 172.19.0.2: seq=0 ttl=64 time=0.253 ms
   64 bytes from 172.19.0.2: seq=1 ttl=64 time=0.147 ms
   64 bytes from 172.19.0.2: seq=2 ttl=64 time=0.155 ms
   64 bytes from 172.19.0.2: seq=3 ttl=64 time=0.169 ms
   ```

   

3. 