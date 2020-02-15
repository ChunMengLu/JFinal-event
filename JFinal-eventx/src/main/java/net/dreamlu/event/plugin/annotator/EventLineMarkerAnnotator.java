package net.dreamlu.event.plugin.annotator;

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import net.dreamlu.event.plugin.constant.EventConstant;
import net.dreamlu.event.plugin.icons.EventIcons;
import net.dreamlu.event.plugin.utils.EventUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.uast.*;

import java.util.Collection;

/**
 * JFinal event LineMarkerProvider
 *
 * @author L.cm
 */
public class EventLineMarkerAnnotator extends RelatedItemLineMarkerProvider {

	@Override
	protected void collectNavigationMarkers(@NotNull PsiElement psiElement, Collection<? super RelatedItemLineMarkerInfo> result) {
		UElement uElement = UastContextKt.toUElementOfExpectedTypes(psiElement, UMethod.class, UCallExpression.class, UQualifiedReferenceExpression.class);
		if (uElement == null) {
			return;
		}
		Module module = ModuleUtilCore.findModuleForPsiElement(psiElement);
		if (module == null) {
			return;
		}
		// 1. 查找监听器注解
		// 2. 查找发布的方法
		if (uElement instanceof UMethod) {
			UMethod uMethod = (UMethod) uElement;
			PsiMethod psiMethod = UElementKt.getAsJavaPsiElement(uMethod, PsiMethod.class);
			if (psiMethod == null) {
				return;
			}
			// 非 public 和 static、构造器，跳过
			if (!psiMethod.hasModifierProperty("public") ||
				psiMethod.hasModifierProperty("static") ||
				psiMethod.isConstructor()) {
				return;
			}
			PsiAnnotation psiAnnotation = psiMethod.getAnnotation(EventConstant.ANNOTATION_LISTENER);
			if (psiAnnotation != null) {
				PsiElement identifier = UAnnotationKt.getNamePsiElement(UastContextKt.toUElement(psiAnnotation, UAnnotation.class));
				if (identifier == null) {
					return;
				}
				NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder.create(EventIcons.LISTENER.getIcon())
					.setTargets(EventUtil.getPublishPoints(module, psiMethod, psiAnnotation))
					.setTooltipText("Go to JFinal event post.");
				result.add(builder.createLineMarkerInfo(identifier));
			}
		} else {
			UCallExpression callExpression = UastContextKt.toUElement(psiElement, UCallExpression.class);
			if (callExpression != null && callExpression.getSourcePsi() == psiElement) {
				String methodName = callExpression.getMethodName();
				if (methodName == null) {
					return;
				}
				PsiMethod element = callExpression.resolve();
				if (element == null) {
					return;
				}
				// EventKit.post(event)，only one args
				if (callExpression.getValueArgumentCount() != 1) {
					return;
				}
				PsiClass containingClass = element.getContainingClass();
				if (containingClass == null) {
					return;
				}
				// EventKit.post
				if (EventConstant.POST_METHOD.equals(methodName) && EventConstant.POST_CLASS.equals(containingClass.getQualifiedName())) {
					NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder.create(EventIcons.PUBLISHER.getIcon())
						.setTargets(EventUtil.getListenerPoints(module, callExpression))
						.setTooltipText("Go to JFinal event listener.");
					result.add(builder.createLineMarkerInfo(psiElement));
				}
			}
		}
	}

}
