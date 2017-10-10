模仿的Spring中的消息事件：[详解Spring事件驱动模型](http://jinnianshilongnian.iteye.com/blog/1902886)

专为JFinal设计，无任何第三方依赖，小巧玲珑。

:laughing: `JFinal`event 插件，使用请查看[文档 wiki](http://git.oschina.net/596392912/JFinal-event/wikis/home)

## 初始化插件
```
// 初始化插件
EventPlugin plugin = new EventPlugin();
// 设置为异步，默认同步，或者使用`threadPool(ExecutorService executorService)`自定义线程池。
plugin.async();

// 设置扫描jar包，默认不扫描
plugin.scanJar();
// 设置监听器默认包，默认全扫描
plugin.scanPackage("net.dreamlu");

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

## 编写兼听
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

使用的场景，已经优势什么的可以参考[详解Spring事件驱动模型](http://jinnianshilongnian.iteye.com/blog/1902886)

jar包下载
http://maven.aliyun.com/nexus/#nexus-search;quick~jfinal-event

以上版本均已上传到maven仓库~

```
<dependency>
    <groupId>net.dreamlu</groupId>
    <artifactId>JFinal-event</artifactId>
    <version>2.0.0</version>
</dependency>
```

欢迎拍砖~~~

## 更新说明
>## 2017-10-10 v2.0.0
>基于注解和方法的兼听，简化使用，不兼容1.x

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