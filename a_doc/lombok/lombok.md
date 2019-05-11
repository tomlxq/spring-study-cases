lombok项目通过增加处理程序使我们的java语言更加刺激（简洁和快速）。

1. Install the lombok plugin from intellij

    IntelliJ IDEA
    定位到 File > Settings > Plugins
    点击 Browse repositories…
    搜索 Lombok Plugin
    点击 Install plugin
    重启 IDEA

2. Enabling annotation processing will make it work

    Preferences… > Build, Execution, Deployment > Compiler > Annotation Processors
    
3. maven项目添加依赖
    ```
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>1.18.6</version>
    </dependency>
    ```

## Lombok注解详解

### 全局配置文件

我们可以从项目根目录下新建一个lombok.config（当然目录不是固定的，lombok会搜索所有lombok.config文件）
在这个文件加入一行
config.stopBubbling = true 
表示该文件目录为根目录，lombok将从该目录下开始搜索。
每个子目录都可以配置lombok.config 作用范围只在该目录下，并且覆盖父目录的配置。


Lombok通常为所有生成的节点生成注释,添加@javax.annotation.Generated 。

可以用:

lombok.addJavaxGeneratedAnnotation = false 设置取消
