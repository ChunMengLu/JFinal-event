package net.dreamlu.processor;

import com.jfinal.kit.StrKit;
import net.dreamlu.event.core.EventListener;
import net.dreamlu.mica.auto.annotation.AutoService;

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
public class EventListenerProcessor extends AbstractProcessor {
	private static final Charset UTF_8 = StandardCharsets.UTF_8;
	/**
	 * The location to look for dream.events file
	 * <p>Can be present in multiple JAR files.
	 */
	private static final String DREAM_EVENTS_RESOURCE_LOCATION = DreamEventsLoader.DREAM_EVENTS_RESOURCE_LOCATION;
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
			processAnnotations(roundEnv);
		}
		return false;
	}

	private void processAnnotations(RoundEnvironment roundEnv) {
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
			// 1. read dream.events
			try {
				FileObject existingFile = filer.getResource(StandardLocation.CLASS_OUTPUT, "", DREAM_EVENTS_RESOURCE_LOCATION);
				// 查找是否已经存在 spring.factories
				printMessage(Kind.NOTE, "Looking for existing dream.events file at " + existingFile.toUri());
				Set<String> existingSet = readDreamEventsFile(existingFile);
				printMessage(Kind.NOTE, "Existing dream.events entries: " + existingSet);
				dreamEventsClazzSet.addAll(existingSet);
			} catch (IOException e) {
				printMessage(Kind.WARNING, "dream.events resource file did not already exist.");
			}
			// 2. writer dream.events
			FileObject eventFile = filer.createResource(StandardLocation.CLASS_OUTPUT, "", DREAM_EVENTS_RESOURCE_LOCATION);
			OutputStream outputStream = eventFile.openOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, UTF_8));
			writer.write("# Generated by JFinal-event, dreamlu.net " + new Date());
			writer.write('\n');
			StringJoiner joiner = new StringJoiner("\n");
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

	private static Set<String> readDreamEventsFile(FileObject fileObject) throws IOException {
		Set<String> clazzSet = new LinkedHashSet<>();
		try (
			InputStream input = fileObject.openInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(input);
			BufferedReader reader = new BufferedReader(inputStreamReader);
		) {
			reader.lines().forEach(line -> {
				if (StrKit.notBlank(line) && !line.trim().startsWith("#")) {
					clazzSet.add(line.trim());
				}
			});
		}
		return clazzSet;
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
