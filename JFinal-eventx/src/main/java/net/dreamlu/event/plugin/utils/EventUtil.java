package net.dreamlu.event.plugin.utils;

import com.intellij.codeInsight.AnnotationUtil;
import com.intellij.jam.JamService;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.PsiConstantEvaluationHelperImpl;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.search.searches.MethodReferencesSearch;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.InheritanceUtil;
import com.intellij.psi.util.PsiModificationTracker;
import net.dreamlu.event.plugin.constant.EventConstant;
import net.dreamlu.event.plugin.jam.JamEventListener;
import net.dreamlu.event.plugin.model.PubMethodPointDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.uast.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * mica idea 插件 工具
 *
 * @author L.cm
 */
public class EventUtil {
	private static final PsiConstantEvaluationHelper psiConstantEvaluationHelper = new PsiConstantEvaluationHelperImpl();

	@NotNull
	public static Set<PsiElement> getPublishPoints(@NotNull Module module,
												   @NotNull PsiMethod psiMethod,
												   @NotNull PsiAnnotation psiAnnotation) {
		// 1. 方法参数
		PsiParameter[] parameters = psiMethod.getParameterList().getParameters();
		// 2. 注解上的事件类型
		List<String> annotationAttrs = getAnnotationValue(psiAnnotation);
		return getPublishPoints(module).stream()
			.filter(descriptor -> filterPostPoint(descriptor, parameters, annotationAttrs))
			.map(PubMethodPointDescriptor::getNavAbleElement)
			.collect(Collectors.toSet());
	}

	private static boolean filterPostPoint(@NotNull PubMethodPointDescriptor descriptor,
										   @NotNull PsiParameter[] parameters,
										   @NotNull List<String> annotationAttrs) {
		// EventKit post publishType
		PsiType publishType = descriptor.getPostEventType();
		// 1. 先过滤注解
		boolean isAnnMatch = annotationAttrs.isEmpty() || annotationAttrs.stream().anyMatch(eventType -> {
			// 如果注解是万能 ApplicationEvent 直接为 true
			return EventConstant.EVENT_PARENT.equals(eventType) || InheritanceUtil.isInheritor(publishType, eventType);
		});
		// 注解就不匹配，直接跳出
		if (!isAnnMatch) {
			return false;
		}
		// 2. 再过滤方法参数
		if (parameters.length == 0) {
			return true;
		}
		// 多个参数不支持，直接过滤掉
		if (parameters.length > 1) {
			return false;
		}
		PsiType eventType = parameters[0].getType();
		return eventType.isAssignableFrom(publishType);
	}

	@NotNull
	public static Set<PubMethodPointDescriptor> getPublishPoints(@NotNull Module module) {
		return CachedValuesManager.getManager(module.getProject()).getCachedValue(module, () -> {
			Set<PubMethodPointDescriptor> descriptors = new LinkedHashSet<>();
			GlobalSearchScope scope = GlobalSearchScope.moduleWithDependenciesAndLibrariesScope(module);
			for (PsiMethod publishEventMethod : getPublishEventMethods(module)) {
				descriptors.addAll(searchPublishPoints(publishEventMethod, scope));
			}
			Set<PubMethodPointDescriptor> cacheValue = descriptors.isEmpty() ? Collections.emptySet() : new HashSet<>(descriptors);
			return CachedValueProvider.Result.createSingleDependency(cacheValue, PsiModificationTracker.MODIFICATION_COUNT);
		});
	}

	private static PsiMethod[] getPublishEventMethods(@NotNull Module module) {
		PsiClass eventPublisherClass = findLibraryClass(module, EventConstant.POST_CLASS);
		if (eventPublisherClass != null) {
			return eventPublisherClass.findMethodsByName(EventConstant.POST_METHOD, false);
		}
		return PsiMethod.EMPTY_ARRAY;
	}

	@NotNull
	private static Set<PubMethodPointDescriptor> searchPublishPoints(@NotNull PsiMethod publishMethod, @NotNull SearchScope searchScope) {
		Set<PubMethodPointDescriptor> set = new LinkedHashSet<>();
		MethodReferencesSearch.search(publishMethod, searchScope, true)
			.forEach(psiReference -> {
				UElement expression = UastContextKt.toUElement(psiReference.getElement());
				UCallExpression callExpression = UastUtils.getUCallExpression(expression);
				if (callExpression != null) {
					List<UExpression> arguments = callExpression.getValueArguments();
					if (1 == arguments.size()) {
						set.add(new PubMethodPointDescriptor(callExpression));
					}
				}
			});
		return set;
	}

	/**
	 * 根据类名查找类
	 *
	 * @param module    模块
	 * @param className 类名
	 * @return PsiClass
	 */
	private static PsiClass findLibraryClass(@Nullable Module module, @NotNull String className) {
		if (module == null || module.isDisposed()) {
			return null;
		}
		Project project = module.getProject();
		return CachedValuesManager.getManager(project).getCachedValue(module, () -> {
			JavaPsiFacade facade = JavaPsiFacade.getInstance(project);
			GlobalSearchScope scope = GlobalSearchScope.moduleWithDependenciesAndLibrariesScope(module);
			PsiClass cacheClass = facade.findClass(className, scope);
			return CachedValueProvider.Result.createSingleDependency(cacheClass, PsiModificationTracker.MODIFICATION_COUNT);
		});
	}

	public static Set<PsiMethod> getListenerPoints(@NotNull Module module, @NotNull UCallExpression callExpression) {
		UExpression expression = callExpression.getArgumentForParameter(0);
		if (expression == null) {
			return Collections.emptySet();
		}
		PsiType publishedType = expression.getExpressionType();
		if (publishedType == null) {
			return Collections.emptySet();
		}
		return getListenerPoints(module).stream()
			.map(JamEventListener::getPsiMethod)
			.filter(psiMethod -> filterListener(psiMethod, publishedType))
			.collect(Collectors.toSet());
	}

	private static boolean filterListener(@NotNull PsiMethod psiMethod, @NotNull PsiType publishType) {
		// 监听器注解
		PsiAnnotation annotation = psiMethod.getAnnotation(EventConstant.ANNOTATION_LISTENER);
		if (annotation == null) {
			return false;
		}
		List<String> annotationAttrs = getAnnotationValue(annotation);
		// 1. 先过滤注解
		boolean isAnnMatch = annotationAttrs.isEmpty() || annotationAttrs.stream().anyMatch(eventType -> InheritanceUtil.isInheritor(publishType, eventType));
		// 注解就不匹配，直接跳出
		if (!isAnnMatch) {
			return false;
		}
		PsiParameter[] parameters = psiMethod.getParameterList().getParameters();
		// 2. 再过滤方法参数
		if (parameters.length == 0) {
			return true;
		}
		// 多个参数不支持，直接过滤掉
		if (parameters.length > 1) {
			return false;
		}
		PsiType eventType = parameters[0].getType();
		return eventType.isAssignableFrom(publishType);
	}

	public static List<JamEventListener> getListenerPoints(@NotNull Module module) {
		return CachedValuesManager.getManager(module.getProject()).getCachedValue(module, () -> {
			JamService service = JamService.getJamService(module.getProject());
			GlobalSearchScope scope = GlobalSearchScope.moduleWithDependenciesAndLibrariesScope(module);
			List<JamEventListener> elements = service.getJamMethodElements(JamEventListener.LISTENER_JAM_KEY, EventConstant.ANNOTATION_LISTENER, scope);
			List<JamEventListener> cacheValue = elements.isEmpty() ? Collections.emptyList() : new ArrayList<>(elements);
			return CachedValueProvider.Result.createSingleDependency(cacheValue, PsiModificationTracker.MODIFICATION_COUNT);
		});
	}

	private static List<String> getAnnotationValue(@NotNull PsiAnnotation psiAnnotation) {
		// 注解 value
		PsiAnnotationMemberValue attrValue = psiAnnotation.findDeclaredAttributeValue("value");
		// 注解 events
		PsiAnnotationMemberValue attrEvents = psiAnnotation.findDeclaredAttributeValue("events");
		List<PsiAnnotationMemberValue> values = AnnotationUtil.arrayAttributeValues(attrValue);
		List<PsiAnnotationMemberValue> events = AnnotationUtil.arrayAttributeValues(attrEvents);
		// 合并 values 和 events
		List<PsiAnnotationMemberValue> allAttrValue = new ArrayList<>();
		allAttrValue.addAll(values);
		allAttrValue.addAll(events);
		return allAttrValue.stream()
			.map(psiConstantEvaluationHelper::computeConstantExpression)
			.map(attr -> ((PsiType) attr).getCanonicalText())
			.collect(Collectors.toList());
	}

}
