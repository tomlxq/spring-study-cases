#RabbitMQ教程（一） ——win7下安装RabbitMQ
1. RabbitMQ依赖erlang,所以先安装erlang，然后再安装RabbitMQ;

下载RabbitMQ,下载地址： rabbitmq-server-3.5.6.exe和erlang，下载地址：otp_win64_18.1.exe

先安装erlang,双击erlang的安装文件即可，然后配置环境变量：
  ERLANG_HOME=D:\Program Files\erl7.1
   追加到path=%ERLANG_HOME%\bin;

验证erlang是否安装成功， 打开cmd命令窗口，进入erlang的bin路径，输入erl命令，如果出现如下提示，则说明erlang安装成功：

D:\Program Files\erl7.1\bin>erl

Eshell V7.1  (abort with ^G)

2. 再安装RabbitMQ，双击安装文件即可，安装完毕后， 设置环境变量：
   RABBITMQ_SERVER=D:\Program Files\RabbitMQ Server\rabbitmq_server-3.5.6
   追加到path=%RABBITMQ_SERVER%\sbin;

验证RabbitMQ是否安装成功，在CMD命令窗口输入：

C:\Windows\system32>rabbitmq-service
然后安装服务，打开cmd窗体，进入D:\Program Files\RabbitMQ Server\rabbitmq_server-3.5.6\sbin路径，然后执行 rabbitmq-service install，提示安装成功

rabbitmq-service sta
提示启动成功；
然后安装web管理插件，执行命令如下：
rabbitmq-plugins enable rabbitmq_management
出现以上提示，说明安装成功，可以通过访问http://localhost:15672进行测试，默认的登陆账号为：guest，密码为：guest。
注：win下安装的时候会有个坑，就是打开cmd命令窗口时，需要打开命令提示符（管理员）不然会报错

#发起两个订阅者接收消息
cd F:\data\wwwtest\spring-study-cases\rabbitmq\target\classes
set CP=.;F:\server\repository\org\springframework\amqp\spring-amqp\2.0.2.RELEASE\spring-amqp-2.0.2.RELEASE.jar;F:\server\repository\com\rabbitmq\amqp-client\5.1.2\amqp-client-5.1.2.jar;F:\server\repository\org\slf4j\slf4j-api\1.7.25\slf4j-api-1.7.25.jar
启动服务器端
java -cp %CP% com.example.rabbitmq.EmitLog  test5 message
启动客户端(两个消费者)
java -cp %CP% com.example.rabbitmq.ReceiveLogs >F:\log.txt
java -cp %CP% com.example.rabbitmq.ReceiveLogs

#参考:
https://www.rabbitmq.com/getstarted.html