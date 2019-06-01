Postman一款非常流行的API调试工具。其实，开发人员用的更多。因为测试人员做接口测试会有更多选择，例如Jmeter、soapUI等。不过，对于开发过程中去调试接口，Postman确实足够的简单方便，而且功能强大。
官方网站：https://www.getpostman.com/

1、简单的Get请求：

参考：http://www.python-requests.org/en/master/user/quickstart/
GET请求：点击Params，输入参数及value，可输入多个，即时显示在URL链接上，
所以，GET请求的请求头与请求参数如在接口文档中无特别声明时，可以不填。

2、简单的POST请求

参考：http://www.python-requests.org/en/master/user/quickstart/
POST：HTTP的常用请求方法。

Body：设置POST请求的参数。
* form-data： HTTP请求中的multipart/form-data，它会将表单的数据处理为一条消息，以标签为单元，用分隔符分开。
* x-wwww-form-urlencode：HTTP请求中的application/x-www-from-urlencoded，会将表单内的数据转换为键值对。
* raw：可以发送任意格式的接口数据，可以text、json、xml、html等。
* binary：HTTP请求中的相Content-Type:application/octet-stream，只可以发送二进制数据。通常用于文件的上传。
json提交

选择JSON(application/json) 是会自动帮我们设置 headers 为 application/json


3、认证接口

创建一个接口调用：
Authorization：用于需要认证的接口。
Basic Auth：最基本的一种认证类型，还有OAuth 1.0/2.0、Digest Auth等认证类型。
Username/Password：这是针对Basic Auth类型的认证的用户名/密码，并非我们认为的系统登录的用户名密码。
1、Basic Auth
是基础的验证，所以会比较简单
会直接把用户名、密码的信息放在请求的 Header 中
2、Digest Auth
要比Basic Auth复杂的多。使用当前填写的值生成authorization header。所以在生成header之前要确保设置的正确性。如果当前的header已经存在，postman会移除之前的header。
3、OAuth 1.0
postman的OAuth helper让你签署支持OAuth
1.0基于身份验证的请求。OAuth不用获取access token,你需要去API提供者获取的。OAuth 1.0可以在header或者查询参数中设置value。
4、OAuth 2.0
postman支持获得OAuth 2.0 token并添加到requests中。


六、管理用例—Collections

Collections集合：也就是将多个接口请求可以放在一起，并管理起来。什么样的接口请求可以放在同一个collection里？

在这里告诉大家可以这样：一个工程一个Collection，这样方便查找及统一处理数据。
第一步, 创建Collections
点击上图中的带+号的图标，输入Name:”abc”，Description:”示例demo”，点击Create按钮即创建成功一个Collections.
第二步，在Collections里添加请求
在右侧准备好接口请求的所有数据，并验证后，点击save按钮。
