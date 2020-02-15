package net.dreamlu.event.plugin.model;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.uast.UCallExpression;
import org.jetbrains.uast.UExpression;
import org.jetbrains.uast.UastUtils;

import java.util.Objects;

/**
 * 发布方法解析
 *
 * @author L.cm
 */
public class PubMethodPointDescriptor {
	@NotNull
	private final UCallExpression callExpression;

	public PubMethodPointDescriptor(@NotNull UCallExpression callExpression) {
		this.callExpression = callExpression;
	}

	public PsiType getPostEventType() {
		UExpression parameter = callExpression.getArgumentForParameter(0);
		if (parameter == null) {
			return null;
		}
		return parameter.getExpressionType();
	}

	public PsiElement getNavAbleElement() {
		UCallExpression parentCallExpression = UastUtils.getUCallExpression(callExpression.getUastParent());
		if (parentCallExpression == null) {
			return callExpression.getSourcePsi();
		}
		return parentCallExpression.getSourcePsi();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		PubMethodPointDescriptor that = (PubMethodPointDescriptor) o;
		return callExpression.equals(that.callExpression);
	}

	@Override
	public int hashCode() {
		return Objects.hash(callExpression);
	}
}
