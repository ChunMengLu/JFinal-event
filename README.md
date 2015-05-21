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
依赖`guava.java`，`v0.2、v0.1`已上传到maven仓库~`v0.2`审核中~
```
<groupId>net.dreamlu</groupId>
<artifactId>JFinal-event</artifactId>
<version>0.1</version>
```

欢迎拍砖~~~

## 交流群

JFinal-bbs: `206034609`

## License

( The MIT License )