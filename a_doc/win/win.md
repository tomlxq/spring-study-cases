1、安装JDK开发环境

下载网站：http://www.oracle.com/
2、配置环境变量：
(1)新建->变量名"JAVA_HOME"，变量值"E:\server\Java\jdk1.8.0_131"（即JDK的安装路径） 
(2)编辑->变量名"Path"，在原变量值的最后面加上“%JAVA_HOME%\bin;%JAVA_HOME%\jre\bin;” 
(3)新建->变量名“CLASSPATH”,变量值“.;%JAVA_HOME%\lib;%JAVA_HOME%\lib\tools.jar”

 3、确认环境配置是否真确：

在控制台分别输入java，javac，java -version 命令，出现如下所示的JDK的编译器信息，包括修改命令的语法和参数选项等信息。
4、在控制台下验证第一个java程序：

public class Test {
    public static void main(String[] args) {    
    System.out.println("Hello Java");
    }
}
用记事本编写好，点击“保存”，并存入C盘根目录后，输入javac Test.java和java Test命令，即可运行程序（打印出结果“Hello Java”）。注：这两个命令是在D:\java\jdk1.8.0_20\bin目录下。

Maven安装与配置

下载与安装

1. 前往https://maven.apache.org/download.cgi下载最新版的Maven程序：
3. 新建环境变量MAVEN_HOME，赋值D:\Program Files\Apache\maven
4. 编辑环境变量Path，追加%MAVEN_HOME%\bin\;
配置Maven本地仓库
apache-maven-3.6.1\conf\settings.xml
<localRepository>E:\server\repository</localRepository>
<mirror> 
	<id>alimaven</id> 
	<name>aliyun maven</name>
	<url>http://maven.aliyun.com/nexus/content/groups/public/</url>
	<mirrorOf>central</mirrorOf>  
</mirror>
<mirror>
	<id>nexus-163</id>
	<mirrorOf>*</mirrorOf>
	<name>Nexus 163</name>
	<url>http://mirrors.163.com/maven/repository/maven-public/</url>
</mirror>
通过DOS命令检查一下我们是否安装成功
mvn -v
mvn help:system