设置环境变量
git config --global user.name "tomlxq"
git config --global user.email 727190460@qq.com
git config --global push.default simple
git config --list
要客户端生成密钥
ssh-keygen -t rsa -C 727190460@qq.com
将密码添加到远程github
cat id_rsa.pub
切换本地仓库
git remote add origin git@github.com:tomlxq/mysql-spring-boot-todo
查看远程仓库的地址
git remote -v 
将本地代码更新到服务器
git push -u origin master
git push
增加所有文件
git add -a
