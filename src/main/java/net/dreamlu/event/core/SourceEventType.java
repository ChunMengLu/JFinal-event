package net.dreamlu.event.core;

import java.util.Objects;

/**
 * 泛型事件类型
 *
 * @author L.cm
 */
public class SourceEventType {
	private final Class<?> eventClass;
	private final Class<?> genericClass;

	public SourceEventType(Class<?> eventClass, Class<?> genericClass) {
		this.eventClass = eventClass;
		this.genericClass = genericClass;
	}

	public Class<?> getEventClass() {
		return eventClass;
	}

	public Class<?> getGenericClass() {
		return genericClass;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		SourceEventType that = (SourceEventType) o;
		return Objects.equals(eventClass, that.eventClass) &&
			Objects.equals(genericClass, that.genericClass);
	}

	@Override
	public int hashCode() {
		return Objects.hash(eventClass, genericClass);
	}
}
