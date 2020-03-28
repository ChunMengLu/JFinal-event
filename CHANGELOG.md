## 更新说明
## 2020-01-10 v3.1.2
* 完成 gitee #I195BH PayloadApplicationEvent 支持万能事件监听。

## 2020-01-10 v3.1.1
* 支持老的 `plugin.scanJar();`
* 支持老的 `plugin.scanPackage("com.xxx.包名");`

## 2020-01-10 v3.1.0
* 升级到jfinal 4.8。
* google-auto 调整为自家的 mica-auto。
* 添加类扫描的开关，用于不是用 maven 等包管理的项目。
* 删除 `ObjenesisBeanFactory`，已经没有存在的意义。

## 2019-07-19 v3.0.0
* 升级到jfinal 4.3。
* 完成【3.0优化 有生之年闲下来了弄吧】#IR96V 之2 将类扫描，改到编译期。
* 删除 `ClassUtil`，但愿大伙没用这个类。

## 2019-04-08 v2.3.0
* 升级到jfinal 3.8。
* 支持 `@EventListener` 方法可以无参数。
* `ObjenesisBeanFactory` 和 jfinal Aop inject 冲突，去掉对 JFinal inject 支持，标记为弃用。
* 新增 `SourceClass` 作为 event 事件源（同 Spring PayloadApplicationEvent），event模型不再需要实现 `ApplicationEvent`。
* `@EventListener` 新增 value 变量，功能同 events。
* 修复 `@EventListener` events 参数类型判断bug。

## 2018-10-09 v2.2.2
* 升级到jfinal 3.5 (不兼容老版本)支持新版本inject，升级到java 8优化部分代码。
* DefaultBeanFactory改为jfinal 3.5 aop 创建，删除 DuangBeanFactory。
* 优化 ObjenesisBeanFactory 支持jfinal 3.5 inject

## 2018-08-14 v2.2.1
* ApplicationEvent 添加泛型，方便类型转换。

## 2018-04-15 v2.2.0
* 插件添加手动注册监听类 `plugin.register(Class<?* clazz)`, 多个类，多次调用`register`方法即可。

## 2018-03-02 v2.1.0
* 添加`CtrlHolderEvent`处理同步、异步中request、session、attr、header参数传递。
* 使用：
* 需先在Config中添加`me.add(new CtrlHolderInterceptor());`拦截器。
* 然后继承`CtrlHolderEvent`编写自己的事件类
```java
CtrlHolder holder = event.getCtrlHolder();
holder.getPara("p");
holder.getAttr("x");
holder.getHeader("x");
holder.getSessionAttr("x");
```

## 2017-11-29 v2.0.4
* 添加`ObjenesisBeanFactory`处理不含有默认构造器的Bean
* 依赖`objenesis`jar包，下载地址：http://mvnrepository.com/artifact/org.objenesis/objenesis/2.6

## 2017-10-11 v2.0.3
* 用户反馈的问题 #IFX3Z
* 支持多包名，用`;`分割，如：`net.dreamlu.a;net.dreamlu.b`。
* 插件初始化时，没有扫描到监听时依然初始化成功。

## 2017-10-11 v2.0.2
* 2.x bug修复版

## 2017-10-11 v2.0.1
* 插件添加Bean工厂，方便IOC容器和自定义扩展。
* 默认为`DefaultBeanFactory`，可实现IBeanFactory自定义扩展。
* `plugin.beanFactory(new DuangBeanFactory());`

## 2017-10-10 v2.0.0
* 基于注解和方法的兼听，简化使用，不兼容1.x
* 支持JFinal 3.1和3.1以上版本

## 2017-04-20 v1.5.1
* 基于rmi的远程Event

## 2017-03-22 v1.4.2
* 更改默认线程池为SingleThreadExecutor，使异步执行有序化。
* 添加EventThreadFactory，处理异步时的异常避免影响服务请求。
* 建议：如果event需要发送大量的异步事件，建议使用自定义线程池。
* `eventPlugin.threadPool(Executors.newCachedThreadPool(new EventThreadFactory()));`

## 2017-02-15 v1.4.1
* 添加自定义线程池EventPlugin.threadPool(ExecutorService executorService)方法

## 2016-08-19 v1.4.0
* 升级到JFinal2.2，JFinal低版本用户请使用`v1.2.0`。
* `EventKit.postEvent(event)`更改为`EventKit.post(event)`，`postEvent`不再建议使用。
* 添加`EventKit.post(tag, event)`方法，`@Listener(order = 2, tag = "save")`添加`tag`。

## 2015-12-30 v1.3.0
* 升级到JFinal2.1，JFinal低版本用户请使用`v1.2.0`

## 2015-08-20 v1.2.0
* 解决部署时中文路径或者是空格找不到监听器

## 2015-07-05 v1.0
* 调优，减少不必要的实例

## 2015-07-04 v0.4.2
* 编译改为JDK1.6
* 新增监听器执行顺序@Listener(order = 1) 越小越优先执行，Default is Integer.MAX_VALUE
* 新增单个监听器的，同步或者异步开关@Listener(enableAsync = true)