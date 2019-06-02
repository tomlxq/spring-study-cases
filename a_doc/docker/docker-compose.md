查看并清掉容器
docker ps
docker rm -f mysql todo
查看并清掉镜象
docker images
docker rmi -f 6201a17848ce


安装Docker Compose
安装指令参看官网：
https://github.com/docker/compose/releases/

curl -L https://github.com/docker/compose/releases/download/1.25.0-rc1/docker-compose-`uname -s`-`uname -m` -o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose

