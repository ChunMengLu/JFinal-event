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

// 设置扫描jar包，默认不扫描
plugin.scanJar();
// 设置监听器默认包，多个包名使用;分割，默认全扫描
plugin.scanPackage("net.dreamlu");
// bean工厂，默认为DefaultBeanFactory，可实现IBeanFactory自定义扩展
// 对于将@EventListener写在不含无参构造器的类需要使用`ObjenesisBeanFactory`
plugin.beanFactory(new DuangBeanFactory());

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
> `events`支持的事件类型数组，用于将事件方法定义为`ApplicationEvent`或者自定义父类。

```java
@EventListener(events = Test1Event.class)
public void applicationEvent(ApplicationEvent event) {
    String xx = (String) event.getSource();
    System.out.println(Thread.currentThread().getName() + "\tsource:" + xx);
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

```
<dependency>
    <groupId>net.dreamlu</groupId>
    <artifactId>JFinal-event</artifactId>
    <version>2.3.0</version>
</dependency>
```

欢迎拍砖~~~

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
