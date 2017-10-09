package net.dreamlu.event.core;

import java.lang.reflect.AnnotatedElement;
import java.util.Objects;

/**
 * Represent an {@link AnnotatedElement} on a particular {@link Class}
 * and is suitable as a key.
 *
 * @author Costin Leau
 * @author Stephane Nicoll
 * @since 4.2.0
 * @see CachedExpressionEvaluator
 */
public final class AnnotatedElementKey {

	private final AnnotatedElement element;

	private final Class<?> targetClass;

	/**
	 * Create a new instance with the specified {@link AnnotatedElement} and
	 * optional target {@link Class}.
	 */
	public AnnotatedElementKey(AnnotatedElement element, Class<?> targetClass) {
		Objects.nonNull(element);
		this.element = element;
		this.targetClass = targetClass;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof AnnotatedElementKey)) {
			return false;
		}
		AnnotatedElementKey otherKey = (AnnotatedElementKey) other;
		return (this.element.equals(otherKey.element) &&
				Objects.deepEquals(this.targetClass, otherKey.targetClass));
	}

	@Override
	public int hashCode() {
		return this.element.hashCode() + (this.targetClass != null ? this.targetClass.hashCode() * 29 : 0);
	}

}