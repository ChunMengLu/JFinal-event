package net.dreamlu.event.core;

import java.util.Objects;

/**
 * 泛型事件类型
 *
 * @author L.cm
 */
public class EventType {
	private final Class<?> eventClass;
	private final Class<?> sourceClass;

	public EventType(Class<?> eventClass, Class<?> sourceClass) {
		this.eventClass = eventClass;
		this.sourceClass = sourceClass;
	}

	public Class<?> getEventClass() {
		return eventClass;
	}

	public Class<?> getSourceClass() {
		return sourceClass;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		EventType eventType = (EventType) o;
		return Objects.equals(eventClass, eventType.eventClass) &&
			Objects.equals(sourceClass, eventType.sourceClass);
	}

	@Override
	public int hashCode() {
		return Objects.hash(eventClass, sourceClass);
	}
}
