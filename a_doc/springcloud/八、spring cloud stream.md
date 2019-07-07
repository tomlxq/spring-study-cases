# spring cloud stream

## kafka

https://github.com/reactive-streams/reactive-streams-jvm

1. Publisher
2. Subscriber
3. Subscription
4. Processor

### 主要用途

1. 消息中间件

2. 流式计算处理

3. 日志

### 同类产品比较

* ActiveMQ：实现JMS（java message service)规范
* RabbitMQ：AMQP（Advanced message  Queue Protocal)规范实现
* Kafka: 并非某种规范实现，灵活和性能是优势

### 下载安装kafka

[kafka的官方网页](http://kafka.apache.org/)

```shell
tar -xzf kafka_2.12-2.2.0.tgz
cd kafka_2.12-2.2.0
```

### 下载zookeeper并启解压

[http://zookeeper.apache.org](http://zookeeper.apache.org)

第一次需求复制`config/zoo_samples.cfg`为`zoo.cfg`

### 启动并运行`kafka`

1. 运行`zookeeper``

   ``bin/zookeeper-server-start.sh config/zookeeper.properties`

2. 运行`kafka`服务器

    `bin/kafka-server-start.sh config/server.properties`

### 创建主题

```bash
bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic test
```

创建后检查

```bash
bin/kafka-topics.sh --list --bootstrap-server localhost:9092
```

### 生产者发送消息

`bin/kafka-console-producer.sh --broker-list localhost:9092 --topic test`

### 消费者接收消息

`bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test --from-beginning`

### 原生API发送消息

```java
Properties properties = new Properties();
properties.put("bootstrap.servers", "192.168.238.165:9092");
properties.put("key.serializer", StringSerializer.class.getName());
properties.put("value.serializer", StringSerializer.class.getName());
// 创建一个producer
KafkaProducer<String, String> producer = new KafkaProducer(properties);
// 创建消息
String topic = "testMyTopic";
Integer partition = 0;
Long timestamp = System.currentTimeMillis();
String key = "message";
String value = "this is first message";
ProducerRecord<String, String> record = new ProducerRecord<>(topic, partition, timestamp, key, value);
// 发送消息
Future<RecordMetadata> send = producer.send(record);
// 强制执行
send.get();
```



## Spring kafka

设计模式

spring社区对数据（`spring data`)有一个基本的设计模式。Template模式

* Redis: RedisTemplate

* Kafka: KafkaTemplate

* JDBC: JdbcTemplate

* JMS: JmsTemplate

* Rest: RestTemplate

  > 一般XXXTemplate都会实现XXXOperations
  >
  > 如：`public class KafkaTemplate<K, V> implements KafkaOperations<K, V> `

## Spring boot kafka

### 增加依赖

> 找到某个Jar，快捷键`f4`，copy maven信息，如：`Maven: org.springframework.kafka:spring-kafka:2.2.7.RELEASE`

```xml
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>
```

### 配置文件`application.properties`

自动装配`KafkaAutoConfiguration`

查看配置项

`KafkaProperties.java`

```properties
# 定义应用名称
spring.application.name=spring-cloud-stream-kafka
# 定义Web服务端口
server.port=2000
# 失效管理安全
spring.management.security.enabled=false
management.endpoints.web.exposure.include=*
management.endpoints.web.base-path=/
management.endpoint.health.show-details=always
# Spring kafka的配置信息
spring.kafka.bootstrap-servers=192.168.238.160:9092
# Spring kafka生产者配置
#spring.kafka.producer.bootstrapServers=192.168.238.165:9092
spring.kafka.producer.keySerializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.valueSerializer=org.apache.kafka.common.serialization.StringSerializer
# Spring kafka消费者配置
spring.kafka.consumer.groupId=tomTopic-1
spring.kafka.consumer.keyDeserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.valueDeserializer=org.apache.kafka.common.serialization.StringDeserializer
# 定义kafka的主题
kafka.topic=tomTopic
```



### 创建生产者`KafkaProducerController`

```java

/**
 * {@link KafkaProducerController} 消息生产者
 *
 * @author TomLuo
 * @date 2019/7/6
 */
@RestController
public class KafkaProducerController {
    public final KafkaTemplate kafkaTemplate;
    public final String topic;

    @Autowired
    public KafkaProducerController(KafkaTemplate kafkaTemplate, @Value("${kafka.topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @PostMapping("/message/send")
    public Boolean sendMsg(@RequestParam String message) {
        ListenableFuture send = kafkaTemplate.send(this.topic, message);
        return true;
    }
}
```

### 创建消费者`KafkaConsumerListener`

```java
@Component
public class KafkaConsumerListener {
    @KafkaListener(topics = "${kafka.topic}")
    public void onMessage(String message) {
        System.out.println("消费者监听消息：" + message);
    }
}
```

### 激活kafka

```java
@SpringBootApplication
@EnableKafkaStreams
public class GsSpringCloudStreamKafkaApplication {
    public static void main(String[] args) {
        SpringApplication.run(GsSpringCloudStreamKafkaApplication.class, args);
    }
}
```



## Spring cloud stream

rabbitMQ官网：

[https://www.rabbitmq.com](https://www.rabbitmq.com)

参考文档

https://cloud.spring.io/spring-cloud-static/spring-cloud-stream/2.1.3.RELEASE/single/spring-cloud-stream.html

rabbitMQ:  AMQP,JMS规范

Kafka: 相对松散的消息队列协议

企业整合模式

### 基本概念

* source 来源，类似 producer,provider,publisher

* sink 接收器，类似 consumer, susbscriber

* processor 对上流而言是sink,对下流而言是source

  > reactive streams
  >
  > 1. publisher
  > 2. subscriber
  > 3. processor

## spring cloud stream  binder: kafka

> 消息分两问部分
>
> 1. 消息头 （headers)
> 2. 消息体 (body/payload)

### 定义标准的发送源

1. 增加配置

   ```properties
   # 定义kafka的主题
   kafka.topic=tomTopic
   # 定义Spring Cloud Stream Source消息的去向
   spring.cloud.stream.bindings.output.destination=${kafka.topic}
   ```

2. 定义发送源

    ```java
    /**
     * 消息发送者
     *
     * @author TomLuo
     * @date 2019/7/6
     */
    @EnableBinding({Source.class})
    public class ProducerMessageBean {
        @Autowired
        @Qualifier(Source.OUTPUT)
        MessageChannel messageChannel;
        @Autowired
        Source source;

        /**
         * 发送消息
         * 
         * @param message
         */
        public void send(String message) {
            messageChannel.send(MessageBuilder.withPayload("[messageChannel] send :" + message).build());
            source.output().send(MessageBuilder.withPayload("[source] send :" + message).build());
        }
    }
    ```

### 自定义发送源

1. 增加配置
```properties
# 定义kafka的主题
kafka.topic2=tomTopic2
# 自定义 spring.cloud.stream.bindings.${channel-name}.destination=${topic-name} spring.cloud.stream.bindings.tomOutput.destination=${kafka.topic2}
````

2. 自定义发送channel-name

    ```java
    public interface TomMessageSource {

        /**
         * Name of the output channel.
         */
        String OUTPUT = "tomOutput";

        /**
         * @return output channel
         */
        @Output(OUTPUT)
        MessageChannel tomOutput();

    }
    ```

3. 自定义发送源

  ```java
  @EnableBinding({TomMessageSource.class})
  public class MessageProducerBean {
      @Autowired
      TomMessageSource tomMessageSource;
  
      @Autowired
      @Qualifier(TomMessageSource.OUTPUT)
      private MessageChannel tomOutputMessageChannel;
  
      /**
       * 发送消息
       * 
       * @param message
       */
      public void send(String message) {
          tomOutputMessageChannel.send(MessageBuilder.withPayload("[tomOutputMessageChannel] send :" + message).build());
      }
  }
  ```

  

  

### 定义消费者

#### 通过`@KafkaListener`消费消息

```java
@KafkaListener(topics = "${kafka.topic2}")
public void onTomMessage(String message) {
    System.out.println("消费者监听消息 kafka.topic2 ：" + message);

}
```

#### 通过`SubscribableChannel`订阅消息

```java
@EnableBinding(Sink.class)
public class MessageConsumerBean {
    /**
     * Sink.INPUT 为Bean的名称
     */
    @Autowired
    @Qualifier(Sink.INPUT)
    private SubscribableChannel subscribableChannel;

    /**
     * 当字段注入完成后的回调
     */
    @PostConstruct
    public void init() {
        subscribableChannel.subscribe(new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                System.out.println("subscribe: " + message.getPayload());
            }
        });
    }
}
```



#### 通过`@ServiceActivator`订阅消息

```java
 /**
     * 通过@ServiceActivator订阅消息
     * 
     * @param message
     */
    @ServiceActivator(inputChannel = Sink.INPUT)
    public void onMessage(Object message) {
        System.out.println("onMessage: " + message);
    }
```

#### 通过`@StreamListener`订阅消息

```java
	/**
     * 通过@ServiceActivator订阅消息
     *
     * @param message
     */
    @StreamListener(Sink.INPUT)
    public void onMessage2(String message) {
        System.out.println("StreamListener: " + message);
    }
```



## spring cloud stream  binder: rabbitMQ

A typical binder implementation consists of the following:

- A class that implements the `Binder` interface;

- A Spring `@Configuration` class that creates a bean of type `Binder` along with the middleware connection infrastructure.

- A `META-INF/spring.binders` file found on the classpath containing one or more binder definitions, as shown in the following example:

  ```properties
  kafka:\
  org.springframework.cloud.stream.binder.kafka.config.KafkaBinderConfiguration
  rabbit:\
  org.springframework.cloud.stream.binder.rabbit.config.RabbitServiceAutoConfiguration
  ```



### 引入依赖

```xml
<dependency>
     <groupId>org.springframework.cloud</groupId>
     <artifactId>spring-cloud-stream-binder-rabbit</artifactId>
</dependency>
```

### 启动rabbitMQ

```
ImportSelector 实现类
ImportBeanDefinitionRegistrar 实现类
BeanPostProcessor 实现类
@Configuration 标注类 
```

### 重构kafka工程