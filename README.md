模仿的Spring中的消息事件：[详解Spring事件驱动模型](http://jinnianshilongnian.iteye.com/blog/1902886)

专为JFinal设计，无任何第三方依赖，小巧玲珑。

:laughing: `JFinal`event 插件，使用请查看[文档 wiki](http://git.oschina.net/596392912/JFinal-event/wikis/home)

```
// 初始化插件
EventPlugin plugin = new EventPlugin();
// 设置为异步，默认同步
plugin.async();

// 设置扫描jar包，默认不扫描
plugin.scanJar();
// 设置监听器默认包，默认全扫描
plugin.scanPackage("net.dreamlu");

// 启动插件
plugin.start();

// 发送第一个消息
EventKit.post(new Test1Event("hello1"));
// 发送带tag的消息
EventKit.post("save", new Test2Event(123123));

Thread.sleep(1000);

// 停止插件
plugin.stop();
```

使用的场景，已经优势什么的可以参考[详解Spring事件驱动模型](http://jinnianshilongnian.iteye.com/blog/1902886)

## 依赖说明
`v0.3`以上版本除了JFinal，不依赖任何jar包

`v0.2、v0.1`依赖`guava.java`，建议直接升级到最新版本。

jar包下载
http://maven.aliyun.com/nexus/#nexus-search;quick~jfinal-event

以上版本均已上传到maven仓库~

```
<dependency>
    <groupId>net.dreamlu</groupId>
    <artifactId>JFinal-event</artifactId>
    <version>1.4.0</version>
</dependency>
```

欢迎拍砖~~~

## 更新说明
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

>## 2015-06-25 v0.3
>1. 去除`guava.java`依赖
>2. 更改EventPlugin `asyn`方法为`async`

>## 2015-05-21 v0.2
>1. 添加@Listener注解，方便使用

## 交流群
如梦技术：[`237587118`](http://shang.qq.com/wpa/qunwpa?idkey=f78fcb750b4f72c92ff4d375d2884dd69b552301a1f2681af956bd32700eb2c0)

## 捐助共勉
<img src="http://soft.dreamlu.net/weixin-9.jpg" width = "200" alt="微信捐助" align=center />
<img src="http://soft.dreamlu.net/alipay.png" width = "200" alt="支付宝捐助" align=center />
<img src="http://soft.dreamlu.net/qq-9.jpg" width = "200" alt="QQ捐助" align=center />

## License

( The MIT License )