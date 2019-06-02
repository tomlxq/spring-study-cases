  
一、springMVC入口 
```
WebMvcConfigurer
EnableWebMvc
DelegatingWebMvcConfiguration
WebMvcConfigurationSupport
```
二、媒体类型
```
org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport
```
处理媒体类型的类
```
static {
	ClassLoader classLoader = WebMvcConfigurationSupport.class.getClassLoader();
	romePresent = ClassUtils.isPresent("com.rometools.rome.feed.WireFeed", classLoader);
	jaxb2Present = ClassUtils.isPresent("javax.xml.bind.Binder", classLoader);
	jackson2Present = ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", classLoader) && ClassUtils.isPresent("com.fasterxml.jackson.core.JsonGenerator", classLoader);
	jackson2XmlPresent = ClassUtils.isPresent("com.fasterxml.jackson.dataformat.xml.XmlMapper", classLoader);
	jackson2SmilePresent = ClassUtils.isPresent("com.fasterxml.jackson.dataformat.smile.SmileFactory", classLoader);
	jackson2CborPresent = ClassUtils.isPresent("com.fasterxml.jackson.dataformat.cbor.CBORFactory", classLoader);
	gsonPresent = ClassUtils.isPresent("com.google.gson.Gson", classLoader);
	jsonbPresent = ClassUtils.isPresent("javax.json.bind.Jsonb", classLoader);
}
```
默认类型
```
protected Map<String, MediaType> getDefaultMediaTypes() {
	Map<String, MediaType> map = new HashMap(4);
	if (romePresent) {
		map.put("atom", MediaType.APPLICATION_ATOM_XML);
		map.put("rss", MediaType.APPLICATION_RSS_XML);
	}

	if (jaxb2Present || jackson2XmlPresent) {
		map.put("xml", MediaType.APPLICATION_XML);
	}

	if (jackson2Present || gsonPresent || jsonbPresent) {
		map.put("json", MediaType.APPLICATION_JSON);
	}

	if (jackson2SmilePresent) {
		map.put("smile", MediaType.valueOf("application/x-jackson-smile"));
	}

	if (jackson2CborPresent) {
		map.put("cbor", MediaType.valueOf("application/cbor"));
	}

	return map;
}
```
三、修改POM文件，展示XML类型
由于XML的处理类型，默认是没有引进来的com.fasterxml.jackson.dataformat.xml.XmlMapper
通过查询类的引入包，引入相关的包
https://search.maven.org/classic/#advancedsearch
在POM文件中加入
```
<dependency>
	<groupId>com.fasterxml.jackson.dataformat</groupId>
	<artifactId>jackson-dataformat-xml</artifactId>
	<version>2.9.8</version>
</dependency>
```
重启应用后，可以看到http://localhost:8080/person/1?name=tom，展示的为XML形式
```
<Person>
	<id>1</id>
	<name>tom</name>
	<location/>
</Person>
```