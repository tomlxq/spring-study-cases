## gitconfig配置文件
Git有一个工具被称为git config，它允许你获得和设置配置变量；这些变量可以控制Git的外观和操作的各个方面。

1. 设置你的用户名称和e-mail地址
git config --global user.name "tomlxq"
git config --global user.email 727190460@qq.com
git config --global push.default simple

2. 编辑器(Your Editor)
git config --global core.editor emacs

3. 检查你的设置(Checking Your Settings)
git config --list
git config user.name

三、常见安装
1. 如果输入$ ssh -T git@github.com
出现错误提示：Permission denied (publickey).因为新生成的key不能加入ssh就会导致连接不上github。
要客户端生成密钥
ssh-keygen -t rsa -C 727190460@qq.com
将密码添加到远程github
cat id_rsa.pub

2. 如果输入$ git push origin master
提示出错信息：error:failed to push som refs to .......
解决办法如下：
git pull origin master 
git push origin master
git remote add origin git@github.com:tomlxq/mysql-spring-boot-todo

3. 使用git在本地创建一个项目的过程
makdir ~/hello-world   
cd ~/hello-world      
git init            
touch README
git add README       
git commit -m 'first commit'    
git remote add origin git@github.com:tomlxq/hello-world.git     
git push -u origin master    

4. 解决git下载出现：Failed to connect to 127.0.0.1 port 1080: Connection refused拒绝连接错误（20190226）
查看Linux当前有没有使用代理
git config --global http.proxy
通过查询系统环境有没有使用代理（成功）
env|grep -I proxy
通过git取消代理设置
git config --global --unset http.proxy
git config --global --unset https.proxy



查看远程仓库的地址
git remote -v 
将本地代码更新到服务器
git push -u origin master
git push
增加所有文件
git add -a

