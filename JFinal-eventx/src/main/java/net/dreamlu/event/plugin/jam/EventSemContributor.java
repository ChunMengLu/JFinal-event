package net.dreamlu.event.plugin.jam;

import com.intellij.openapi.project.Project;
import com.intellij.patterns.PsiJavaPatterns;
import com.intellij.patterns.PsiMethodPattern;
import com.intellij.semantic.SemContributor;
import com.intellij.semantic.SemRegistrar;
import net.dreamlu.event.plugin.constant.EventConstant;
import org.jetbrains.annotations.NotNull;

/**
 * mica SemContributor
 *
 * @author L.cm
 */
public class EventSemContributor extends SemContributor {

	@Override
	public void registerSemProviders(@NotNull SemRegistrar registrar, @NotNull Project project) {
		PsiMethodPattern methodPattern = PsiJavaPatterns.psiMethod()
			.constructor(false)
			.withModifiers("public")
			.withAnnotation(EventConstant.ANNOTATION_LISTENER);
		// 注册监听器的方法
		JamEventListener.METHOD_META.register(registrar, methodPattern);
	}

}
