模仿的Spring中的消息事件：[详解Spring事件驱动模型](http://jinnianshilongnian.iteye.com/blog/1902886)

:laughing: `JFinal`event 插件，使用请查看[文档 wiki](http://git.oschina.net/596392912/JFinal-event/wikis/home)

```
// 初始化插件
EventPlugin plugin = new EventPlugin();
// 设置为异步，默认同步
plugin.asyn();

// 设置扫描jar包，默认不扫描
plugin.scanJar();
// 设置监听器默认包，默认全扫描
plugin.scanPackage("net.dreamlu");


// 启动插件
plugin.start();

// 发送第一个消息
EventKit.postEvent(new Test1Event("hello1"));
// 发送第二个消息
EventKit.postEvent(new Test2Event(123123));

Thread.sleep(1000);

// 停止插件
plugin.stop();
```

依赖: `guava.java` 多key的map，储存监听器，一个事件多个监听器监听！

使用的场景，已经优势什么的可以参考[详解Spring事件驱动模型](http://jinnianshilongnian.iteye.com/blog/1902886)

## Maven
`v0.3`除了JFinal，不依赖任何jar包

`v0.2、v0.1`依赖`guava.java`

以上版本均已上传到maven仓库~

```
<dependency>
    <groupId>net.dreamlu</groupId>
    <artifactId>JFinal-event</artifactId>
    <version>0.3</version>
</dependency>
```

欢迎拍砖~~~

## 更新说明
>## 2015-06-25 v0.3
>1. 去除`guava.java`依赖
>2. 更改EventPlugin `asyn`方法为`async`

>## 2015-05-21 v0.2
>1. 添加@Listener注解，方便使用

## 交流群
如梦技术：`237587118`

JFinal-bbs: `206034609`

## License

( The MIT License )