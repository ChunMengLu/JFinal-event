模仿的Spring中的消息事件：[详解Spring事件驱动模型](http://jinnianshilongnian.iteye.com/blog/1902886)

专为JFinal设计，无任何第三方依赖，小巧玲珑。

:laughing: `JFinal-event` 插件，`老版本`使用请查看[文档 wiki](http://git.oschina.net/596392912/JFinal-event/wikis/home)

:laughing: `如梦技术VIP` 限时￥99，[欢迎加入](https://www.dreamlu.net/spring-boot/spring-security/thymeleaf/maven/gradle/2018/04/14/dream-security.html)

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

## 新建事件类
```java
// 继承 ApplicationEvent
public class Test1Event extends ApplicationEvent {

    private static final long serialVersionUID = 6994987952247306131L;
    
    public Test1Event(Object source) {
        super(source);
    }

}
```

## 编写监听
```java
@EventListener
public void listenTest1Event(Test1Event event) {
    System.out.println("Test1Event：" + event.getSource());
}
```

## 发送事件
```java
EventKit.post(new Test1Event("hello1"));
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
    <version>2.2.0</version>
</dependency>
```

欢迎拍砖~~~

## 更新说明
>## 2018-04-15 v2.2.0
> 插件添加手动注册监听类 `plugin.register(Class<?> clazz)`, 多个类，多次调用`register`方法即可。

>## 2018-03-02 v2.1.0
>添加`CtrlHolderEvent`处理同步、异步中request、session、attr、header参数传递。
>使用：
>需先在Config中添加`me.add(new CtrlHolderInterceptor());`拦截器。
>然后继承`CtrlHolderEvent`编写自己的事件类
```java
CtrlHolder holder = event.getCtrlHolder();
holder.getPara("p");
holder.getAttr("x");
holder.getHeader("x");
holder.getSessionAttr("x");
```

>## 2017-11-29 v2.0.4
>添加`ObjenesisBeanFactory`处理不含有默认构造器的Bean
>依赖`objenesis`jar包，下载地址：http://mvnrepository.com/artifact/org.objenesis/objenesis/2.6

>## 2017-10-11 v2.0.3
>用户反馈的问题 #IFX3Z
>支持多包名，用`;`分割，如：`net.dreamlu.a;net.dreamlu.b`。
>插件初始化时，没有扫描到监听时依然初始化成功。

>## 2017-10-11 v2.0.2
>2.x bug修复版

>## 2017-10-11 v2.0.1
>插件添加Bean工厂，方便IOC容器和自定义扩展。
>默认为`DefaultBeanFactory`，可实现IBeanFactory自定义扩展。
>`plugin.beanFactory(new DuangBeanFactory());`

>## 2017-10-10 v2.0.0
>基于注解和方法的兼听，简化使用，不兼容1.x
>支持JFinal 3.1和3.1以上版本

>## 2017-04-20 v1.5.1
>基于rmi的远程Event

>## 2017-03-22 v1.4.2
>1. 更改默认线程池为SingleThreadExecutor，使异步执行有序化。
>2. 添加EventThreadFactory，处理异步时的异常避免影响服务请求。
> 建议：如果event需要发送大量的异步事件，建议使用自定义线程池。
> `eventPlugin.threadPool(Executors.newCachedThreadPool(new EventThreadFactory()));`

>## 2017-02-15 v1.4.1
>1. 添加自定义线程池EventPlugin.threadPool(ExecutorService executorService)方法

>## 2016-08-19 v1.4.0
>1. 升级到JFinal2.2，JFinal低版本用户请使用`v1.2.0`。
>2. `EventKit.postEvent(event)`更改为`EventKit.post(event)`，`postEvent`不再建议使用。
>3. 添加`EventKit.post(tag, event)`方法，`@Listener(order = 2, tag = "save")`添加`tag`。

>## 2015-12-30 v1.3.0
>1. 升级到JFinal2.1，JFinal低版本用户请使用`v1.2.0`

>## 2015-08-20 v1.2.0
>1. 解决部署时中文路径或者是空格找不到监听器

>## 2015-07-05 v1.0
>1. 调优，减少不必要的实例

>## 2015-07-04 v0.4.2
>1. 编译改为JDK1.6
>2. 新增监听器执行顺序@Listener(order = 1) 越小越优先执行，Default is Integer.MAX_VALUE
>3. 新增单个监听器的，同步或者异步开关@Listener(enableAsync = true)

## 交流群
如梦技术：[`237587118`](http://shang.qq.com/wpa/qunwpa?idkey=f78fcb750b4f72c92ff4d375d2884dd69b552301a1f2681af956bd32700eb2c0)

## 捐助共勉
<img src="http://soft.dreamlu.net/weixin-9.jpg" width = "200" alt="微信捐助" align=center />
<img src="http://soft.dreamlu.net/alipay.png" width = "200" alt="支付宝捐助" align=center />
<img src="http://soft.dreamlu.net/qq-9.jpg" width = "200" alt="QQ捐助" align=center />

## License

( The MIT License )