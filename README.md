# JFinal event 事件驱动

[![Mica Maven release](https://img.shields.io/nexus/r/https/oss.sonatype.org/net.dreamlu/JFinal-event.svg?style=flat-square)](https://mvnrepository.com/artifact/net.dreamlu/JFinal-event)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/917161e0c2174fa6aff8d92a7f4a47a3)](https://www.codacy.com/app/ChunMengLu/JFinal-event?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=ChunMengLu/JFinal-event&amp;utm_campaign=Badge_Grade)

模仿的Spring中的消息事件：[详解Spring事件驱动模型](http://jinnianshilongnian.iteye.com/blog/1902886)

专为JFinal设计，无任何第三方依赖，小巧玲珑。

[更新记录](CHANGELOG.md)

:laughing: `JFinal-event` 插件，`老版本`使用请查看[文档 wiki](http://git.oschina.net/596392912/JFinal-event/wikis/home)

## 初始化插件
```
// 初始化插件
EventPlugin plugin = new EventPlugin();
// 设置为异步，默认同步，或者使用`threadPool(ExecutorService executorService)`自定义线程池。
plugin.async();
// 开启类扫描，默认为 false，用于不支持 注解处理器的情况
plugin.enableClassScan();
// 手动启动插件，用于main方法启动，jfinal中不需要，添加插件即可。
plugin.start();

// 停止插件，用于main方法测试
plugin.stop();
```

## 新建事件类（任意模型即可 2.3.0支持）
```java
public class AccountEvent {

	private Integer id;
	private String name;
	private Integer age;

	// 省略 get set

}
```

## 编写监听
```java
@EventListener
public void listenTest1Event(AccountEvent event) {
    System.out.println("AccountEvent：" + event);
}
```

## 发送事件
```java
AccountEvent event = new AccountEvent();
event.setId(1);
event.setName("张三");
event.setAge(18);

EventKit.post(event);
```

## @EventListener注解说明

### 示例
```java
@EventListener(events = Test1Event.class, order = 1, async = true, condition = "event.isExec()")
```

### 说明
> `value` 或 `events`支持的事件类型数组，用于将事件方法定义为`ApplicationEvent`或者自定义父类。

```java
public class Test {

    @EventListener({Test1Event.class, Test2Event.class})
    public void applicationEvent(ApplicationEvent event) {
        String xx = (String) event.getSource();
        System.out.println(Thread.currentThread().getName() + "\tsource:" + xx);
    }
}
```

> `order`排序，数值越小越先执行，默认为`Integer.MAX_VALUE`

> `async`异步执行，需要插件开启`async()`或者自定义线程池。

> `condition`表达式条件，使用`event.xxxx，event.isExec() == true`判定event的属性或者方法。

## 使用的场景优势
请参考[详解Spring事件驱动模型](http://jinnianshilongnian.iteye.com/blog/1902886)

jar包下载
http://central.maven.org/maven2/net/dreamlu/JFinal-event/

以上版本均已上传到maven仓库~

### maven

```xml
<dependency>
    <groupId>net.dreamlu</groupId>
    <artifactId>JFinal-event</artifactId>
    <version>3.1.0</version>
</dependency>
```

### gradle >= 5.x

```groovy
api("net.dreamlu:JFinal-event:3.1.0")
annotationProcessor("net.dreamlu:JFinal-event:3.1.0")
```

### gradle < 5.x

```groovy
compile("net.dreamlu:JFinal-event:3.1.0")
```

### `注意`

* `3.0.0` 由于使用了 `Annotation Processor` 技术，Idea 需要开启注解处理器。
* 如果你的开发工具不支持 `Annotation Processor`，3.1.0 可开启类扫描。

## License

( The MIT License )

## 开源推荐

* `mica` Spring boot 微服务核心组件集：[https://gitee.com/596392912/mica](https://gitee.com/596392912/mica)
* `Avue` 一款基于vue可配置化的神奇框架：[https://gitee.com/smallweigit/avue](https://gitee.com/smallweigit/avue)
* `pig` 宇宙最强微服务（架构师必备）：[https://gitee.com/log4j/pig](https://gitee.com/log4j/pig)
* `SpringBlade` 完整的线上解决方案（企业开发必备）：[https://gitee.com/smallc/SpringBlade](https://gitee.com/smallc/SpringBlade)
* `IJPay` 支付SDK让支付触手可及：[https://gitee.com/javen205/IJPay](https://gitee.com/javen205/IJPay)

## 微信公众号

![如梦技术](docs/img/dreamlu-weixin.jpg)

精彩内容每日推荐！!
