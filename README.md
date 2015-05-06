模仿的Spring中的消息事件：[详解Spring事件驱动模型](http://jinnianshilongnian.iteye.com/blog/1902886)

:laughing: `JFinal`event 插件，使用请查看test~

```
// 初始化插件
EventPlugin plugin = new EventPlugin();
// 设置为异步
plugin.asyn();

// 设置扫描jar包，默认不扫描
plugin.scanJar();
// 设置默认扫描的包命，默认全扫描
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

欢迎拍砖~~~

## 交流群

JFinal-bbs: `206034609`

## License

( The MIT License )