package net.dreamlu.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;

/**
 * 该类参考于#hutool#
 * 
 * 地址：http://git.oschina.net/loolly/hutool
 * 
 * 类工具类
 * 1、扫描指定包下的所有类<br>
 * 参考 http://www.oschina.net/code/snippet_234657_22722
 * @author seaside_hi, xiaoleilu
 *
 */
public final class ClassUtil {
	private static Log log = Log.getLog(ClassUtil.class);

	// 静态类不可实例化
	private ClassUtil() {}

	/** Class文件扩展名 */
	private static final String CLASS_EXT = ".class";
	/** Jar文件扩展名 */
	private static final String JAR_FILE_EXT = ".jar";
	/** 在Jar中的路径jar的扩展名形式 */
	private static final String JAR_PATH_EXT = ".jar!";
	/** 当Path为文件形式时, path会加入一个表示文件的前缀 */
	private static final String PATH_FILE_PRE = "file:";

	/**
	 * 扫描指定包路径下所有包含指定注解的类
	 * @param packageName 包路径
	 * @param inJar 在jar包中查找
	 * @param annotationClass 注解类
	 * @return 类集合
	 */
	public static Set<Class<?>> scanPackageByAnnotation(String packageName, boolean inJar, final Class<? extends Annotation> annotationClass) {
		final Set<Class<?>> classes = new HashSet<Class<?>>();
		
		scanPackage(packageName, inJar, new ClassFilter() {
			@Override
			public boolean accept(Class<?> clazz) {
				return clazz.isAnnotationPresent(annotationClass);
			}
			
			@Override
			public void addClass(Class<?> clazz) {
				classes.add(clazz);
			}
		});
		
		return classes;
	}
	
	/**
	 * 扫描指定包路径下所有指定类的子类
	 * @param packageName 包路径
	 * @param inJar 在jar包中查找
	 * @param superClass 父类 
	 * @return 类集合
	 */
	public static Set<Class<?>> scanPackageBySuper(String packageName, boolean inJar, final Class<?> superClass) {
		final Set<Class<?>> classes = new HashSet<Class<?>>();
		
		scanPackage(packageName, inJar, new ClassFilter() {
			@Override
			public boolean accept(Class<?> clazz) {
				return superClass.isAssignableFrom(clazz) && !superClass.equals(clazz);
			}
			
			@Override
			public void addClass(Class<?> clazz) {
				classes.add(clazz);
			}
		});
		
		return classes;
	}

	/**
	 * 扫面包路径下满足class过滤器条件的所有class文件
	 * 如果包路径为 com.abs + A.class 但是输入 abs会产生classNotFoundException
	 * 因为className 应该为 com.abs.A 现在却成为abs.A,此工具类对该异常进行忽略处理,有可能是一个不完善的地方，以后需要进行修改
	 * 
	 * @param packageName 包路径 com | com. | com.abs | com.abs.
	 * @param inJar 在jar包中查找
	 * @param classFilter class过滤器，过滤掉不需要的class
	 */
	public static void scanPackage(String packageName, boolean inJar, ClassFilter classFilter) {
		if(packageName == null) {
			packageName = "";
		}
		// 支持;分割多个包
		String[] pkgs = packageName.split(";");
		for (String pkg : pkgs) {
			String pkgName = getWellFormedPackageName(pkg);
			for (String classPath : getClassPaths(pkgName)) {
				// 填充 classes, 并对classpath解码
				classPath = decodeUrl(classPath);
				fillClasses(classPath, pkgName, classFilter);
			}
		}
		//如果在项目的ClassPath中未找到，去系统定义的ClassPath里找
		if(inJar) {
			for (String classPath : getJavaClassPaths()) {
				for (String pkg : pkgs) {
					String pkgName = getWellFormedPackageName(pkg);
					// 填充classes, 并对classpath解码
					classPath = decodeUrl(classPath);
					fillClasses(classPath, new File(classPath), pkgName, classFilter);
				}
			}
		}
	}

	/**
	 * 获得ClassPath
	 * @param packageName 包名称
	 * @return ClassPath路径字符串集合
	 */
	public static Set<String> getClassPaths(String packageName){
		String packagePath = packageName.replace('.', '/');
		Enumeration<URL> resources;
		try {
			resources = getClassLoader().getResources(packagePath);
		} catch (IOException e) {
			throw new RuntimeException(String.format("Loading classPath [%s] error!", packageName), e);
		}
		Set<String> paths = new HashSet<String>();
		while(resources.hasMoreElements()) {
			paths.add(resources.nextElement().getPath());
		}
		return paths;
	}

	/**
	 * @return 获得Java ClassPath路径，不包括 jre
	 */
	public static String[] getJavaClassPaths() {
		return System.getProperty("java.class.path").split(System.getProperty("path.separator"));
	}

	/**
	 * 获取当前线程的ClassLoader
	 * @return 当前线程的class loader
	 */
	public static ClassLoader getContextClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	/**
	 * 获得class loader
	 * 若当前线程class loader不存在，取当前类的class loader
	 * @return 类加载器
	 */
	public static ClassLoader getClassLoader() {
		ClassLoader classLoader = getContextClassLoader();
		if(classLoader == null) {
			classLoader = ClassUtil.class.getClassLoader();
		}
		return classLoader;
	}

	/**
	 * 根据指定的类名称加载类
	 * @param className 完整类名
	 * @return {Class}
	 * @throws ClassNotFoundException 找不到异常
	 */
	public static Class<?> loadClass(String className) throws ClassNotFoundException {
		try {
			return ClassUtil.getContextClassLoader().loadClass(className);
		} catch (ClassNotFoundException e) {
			try {
				return Class.forName(className, false, ClassUtil.getClassLoader());
			} catch (ClassNotFoundException ex) {
				try {
					return ClassLoader.class.getClassLoader().loadClass(className);
				} catch (ClassNotFoundException exc) {
					throw exc;
				}
			}
		}
	}

	//--------------------------------------------------------------------------------------------------- Private method start
	/** 
	 * 文件过滤器，过滤掉不需要的文件
	 * 只保留Class文件、目录和Jar
	 */
	private static FileFilter fileFilter = new FileFilter(){
		@Override
		public boolean accept(File pathname) {
			return isClass(pathname.getName()) || pathname.isDirectory() || isJarFile(pathname);
		}
	};

	/**
	 * 改变 com -> com. 避免在比较的时候把比如 completeTestSuite.class类扫描进去，如果没有"."
	 * 那class里面com开头的class类也会被扫描进去,其实名称后面或前面需要一个 ".",来添加包的特征
	 * 
	 * @param packageName 包名
	 * @return 格式化后的包名
	 */
	private static String getWellFormedPackageName(String packageName) {
		return packageName.lastIndexOf('.') != packageName.length() - 1 ? packageName + '.' : packageName;
	}

	/**
	 * 去掉指定前缀
	 * 
	 * @param str 字符串
	 * @param prefix 前缀
	 * @return 切掉后的字符串，若前缀不是 preffix， 返回原字符串
	 */
	private static String removePrefix(String str, String prefix) {
		if (str != null && str.startsWith(prefix)) {
			return str.substring(prefix.length());
		}
		return str;
	}

	/**
	 * 填充满足条件的class 填充到 classes
	 * 同时会判断给定的路径是否为Jar包内的路径，如果是，则扫描此Jar包
	 * 
	 * @param path Class文件路径或者所在目录Jar包路径
	 * @param packageName 需要扫面的包名
	 * @param classFilter class过滤器
	 * @param classes List 集合
	 */
	private static void fillClasses(String path, String packageName, ClassFilter classFilter) {
		//判定给定的路径是否为Jar
		int index = path.lastIndexOf(JAR_PATH_EXT);
		if(index != -1) {
			//Jar文件
			path = path.substring(0, index + JAR_FILE_EXT.length()); //截取jar路径
			path = removePrefix(path, PATH_FILE_PRE); //去掉文件前缀

			processJarFile(new File(path), packageName, classFilter);
		}else {
			fillClasses(path, new File(path), packageName, classFilter);
		}
	}
	
	/**
	 * 填充满足条件的class 填充到 classes
	 * 
	 * @param classPath 类文件所在目录，当包名为空时使用此参数，用于截掉类名前面的文件路径
	 * @param file Class文件或者所在目录Jar包文件
	 * @param packageName 需要扫面的包名
	 * @param classFilter class过滤器
	 * @param classes List 集合
	 */
	private static void fillClasses(String classPath, File file, String packageName, ClassFilter classFilter) {
		if (file.isDirectory()) {
			processDirectory(classPath, file, packageName, classFilter);
		} else if (isClassFile(file)) {
			processClassFile(classPath, file, packageName, classFilter);
		} else if (isJarFile(file)) {
			processJarFile(file, packageName, classFilter);
		}
	}

	/**
	 * 处理如果为目录的情况,需要递归调用 fillClasses方法
	 * 
	 * @param directory 目录
	 * @param packageName 包名
	 * @param classFilter 类过滤器
	 * @param classes 类集合
	 */
	private static void processDirectory(String classPath, File directory, String packageName, ClassFilter classFilter) {
		for (File file : directory.listFiles(fileFilter)) {
			fillClasses(classPath, file, packageName, classFilter);
		}
	}

	/**
	 * 处理为class文件的情况,填充满足条件的class 到 classes
	 * 
	 * @param classPath 类文件所在目录，当包名为空时使用此参数，用于截掉类名前面的文件路径
	 * @param file class文件
	 * @param packageName 包名
	 * @param classFilter 类过滤器
	 * @param classes 类集合
	 */
	private static void processClassFile(String classPath, File file, String packageName, ClassFilter classFilter) {
		if(false == classPath.endsWith(File.separator)) {
			classPath += File.separatorChar;
		}
		String path = file.getAbsolutePath();
		if(StrKit.isBlank(packageName)) {
			path = removePrefix(path, classPath);
		}
		final String filePathWithDot = path.replace(File.separatorChar, '.');

		int subIndex = -1;
		if ((subIndex = filePathWithDot.indexOf(packageName)) != -1) {
			final int endIndex = filePathWithDot.lastIndexOf(CLASS_EXT);

			final String className = filePathWithDot.substring(subIndex, endIndex);
			fillClass(className, packageName, classFilter);
		}
	}

	/**
	 * 处理为jar文件的情况，填充满足条件的class 到 classes
	 * 
	 * @param file jar文件
	 * @param packageName 包名
	 * @param classFilter 类过滤器
	 * @param classes 类集合
	 */
	private static void processJarFile(File file, String packageName, ClassFilter classFilter) {
		try {
			for (JarEntry entry : Collections.list(new JarFile(file).entries())) {
				if (isClass(entry.getName())) {
					final String className = entry.getName().replace('/', '.').replace(CLASS_EXT, "");
					fillClass(className, packageName, classFilter);
				}
			}
		} catch (Throwable ex) {
			log.error(ex.getMessage(), ex);
		}
	}

	/**
	 * 填充class 到 classes
	 * 
	 * @param className 类名
	 * @param packageName 包名
	 * @param classes 类集合
	 * @param classFilter 类过滤器
	 */
	private static void fillClass(String className, String packageName, ClassFilter classFilter) {
		if (className.startsWith(packageName)) {
			try {
				final Class<?> clazz = ClassUtil.loadClass(className);
				if (classFilter == null || classFilter.accept(clazz)) {
					classFilter.addClass(clazz);
				}
			} catch (Throwable ex) {
//				log.error(ex.getMessage(), ex);
//				Pass Load Error.
			}
		}
	}

	/**
	 * @param file 文件
	 * @return 是否为类文件
	 */
	private static boolean isClassFile(File file) {
		return isClass(file.getName());
	}

	/**
	 * @param fileName 文件名
	 * @return 是否为类文件
	 */
	private static boolean isClass(String fileName) {
		return fileName.endsWith(CLASS_EXT);
	}

	/**
	 * @param file 文件
	 * @return是否为Jar文件
	 */
	private static boolean isJarFile(File file) {
		return file.getName().endsWith(JAR_FILE_EXT);
	}
	//--------------------------------------------------------------------------------------------------- Private method end

	/**
	 * 类过滤器，用于过滤不需要加载的类
	 */
	public interface ClassFilter {
		boolean accept(Class<?> clazz);
		void addClass(Class<?> clazz);
	}

	/**
	 * 对路径解码
	 * @param url 路径
	 * @return String 解码后的路径
	 */
	private static String decodeUrl(String url) {
		try {
			return URLDecoder.decode(url, "UTF-8");
		} catch (java.io.UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

}