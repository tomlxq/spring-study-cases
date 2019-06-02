linux解决端口号被占用

1. 查看端口有没有被占用
lsof -i:8080

2. 查看指定端口的使用情况
netstat -tln | grep 1080
显示系统端口使用情况
netstat -anp

3. 查看进程号对用的可执行程序
ps -f -p 进程号

4. 终止进程号（被占用的端口）
kill -9 进程号