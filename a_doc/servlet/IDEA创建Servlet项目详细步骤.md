# IDEA创建Servlet项目详细步骤

1、Create New Project -> Java EE -> Web Application -> 项目命名 -> Finish

2、web/WEB-INF中添加classes、lib、src文件夹

File -> Project Structture -> Modules

在web目录下新建WEB-INF目录，并在WEB-INF目录下新建3个目录：classes、lib、src，其中src右键设为source

src用于存放源Servlet的java文件，classes用来存放编译后输出的class文件，lib用于存放第三方jar包

3、配置classes与lib文件夹路径

File -> Project Structure  -> Module -> Paths -> Use module compile output path" -> 将Output path和Test output path都选择刚刚创建的classes文件夹

4、配置tomcat容器

Run -> Edit Configuration -> 点击左上角“+”号 -> “Tomcat Server” -> “Local”

