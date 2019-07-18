package net.dreamlu.processor;

import com.google.auto.service.AutoService;
import net.dreamlu.event.EventPlugin;
import net.dreamlu.event.core.EventListener;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic.Kind;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * EventListener 注解处理器
 *
 * @author L.cm
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes("*")
@SupportedOptions("debug")
public class EventListenerProcessor extends AbstractProcessor {
	private static final Charset UTF_8 = StandardCharsets.UTF_8;
	/**
	 * The location to look for dream.events file
	 * <p>Can be present in multiple JAR files.
	 */
	private static final String DREAM_EVENTS_RESOURCE_LOCATION = DreamEventsLoader.DREAM_EVENTS_RESOURCE_LOCATION;
	/**
	 * Event 自动配置类
	 */
	private static final String EVENT_PLUGIN_KEY = EventPlugin.class.getName();
	/**
	 * 数据承载，包含 @EventListener 方法注解的类
	 */
	private Set<String> dreamEventsClazzSet = new LinkedHashSet<>();
	/**
	 * 元素辅助类
	 */
	private Elements elementUtils;

	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
		super.init(processingEnv);
		elementUtils = processingEnv.getElementUtils();
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		if (roundEnv.processingOver()) {
			generateDreamEventFiles();
		} else {
			processAnnotations(annotations, roundEnv);
		}
		return false;
	}

	private void processAnnotations(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		Set<? extends Element> elementSet = roundEnv.getRootElements();

		// 过滤 TypeElement
		Set<TypeElement> typeElementSet = elementSet.stream()
			.filter(this::isClassOrInterface)
			.filter(e -> e instanceof TypeElement)
			.map(e -> (TypeElement) e)
			.collect(Collectors.toSet());

		for (TypeElement typeElement : typeElementSet) {
			String typeName = typeElement.getQualifiedName().toString();
			List<? extends Element> allMembers = elementUtils.getAllMembers(typeElement);
			Set<String> foundElement = new HashSet<>();
			for (Element memberElement : allMembers) {
				EventListener eventListener = memberElement.getAnnotation(EventListener.class);
				if (eventListener != null) {
					String methodName = memberElement.getSimpleName().toString();
					foundElement.add(methodName);
				}
			}
			// 如果扫描到类方法上面有 @EventListener 注解则存储
			if (!foundElement.isEmpty()) {
				printMessage(Kind.NOTE, "Found " + typeName + ", methods\t " + foundElement);
				dreamEventsClazzSet.add(typeName);
			}
		}
	}

	private boolean isClassOrInterface(Element e) {
		ElementKind kind = e.getKind();
		return kind == ElementKind.CLASS || kind == ElementKind.INTERFACE;
	}

	private void generateDreamEventFiles() {
		if (dreamEventsClazzSet.isEmpty()) {
			return;
		}
		Filer filer = processingEnv.getFiler();
		try {
			// 1. spring.factories
			FileObject eventFile = filer.createResource(StandardLocation.CLASS_OUTPUT, "", DREAM_EVENTS_RESOURCE_LOCATION);
			OutputStream outputStream = eventFile.openOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, UTF_8));
			writer.write("## Generated by JFinal event, " + new Date());
			writer.write('\n');
			writer.write(EVENT_PLUGIN_KEY);
			writer.write("=\\\n  ");
			StringJoiner joiner = new StringJoiner(",\\\n  ");
			for (String value : dreamEventsClazzSet) {
				joiner.add(value);
			}
			writer.write(joiner.toString());
			writer.newLine();
			writer.flush();
			outputStream.close();
		} catch (IOException e) {
			fatalError(e);
		}
	}

	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.latestSupported();
	}

	private void fatalError(Exception e) {
		StringWriter writer = new StringWriter();
		e.printStackTrace(new PrintWriter(writer));
		printMessage(Kind.ERROR, writer.toString());
	}

	private void printMessage(Kind kind, CharSequence msg) {
		processingEnv.getMessager().printMessage(kind, "FATAL ERROR: " + msg);
	}
}
