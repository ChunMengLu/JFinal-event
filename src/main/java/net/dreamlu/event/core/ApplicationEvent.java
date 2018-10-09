package net.dreamlu.event.core;

/**
 * Class to be extended by all application events. Abstract as it
 * doesn't make sense for generic events to be published directly.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 */
public abstract class ApplicationEvent<T> implements java.io.Serializable {
	private static final long serialVersionUID = 7099057708183571937L;
	protected final T source;
	private final long timestamp;

	/**
	 * Create a new ApplicationEvent.
	 * @param source the component that published the event (never {@code null})
	 */
	public ApplicationEvent(T source) {
		if (source == null) {
			throw new IllegalArgumentException("null source");
		}
		this.source = source;
		this.timestamp = System.currentTimeMillis();
	}

	/**
	 * The object on which the Event initially occurred.
	 * @return   The object on which the Event initially occurred.
	 */
	public T getSource() {
		return source;
	}

	/**
	 * Returns a String representation of this EventObject.
	 * @return  A a String representation of this EventObject.
	 */
	@Override
	public String toString() {
		return getClass().getName() + "[source=" + source + "]";
	}
	
	/**
	 * Return the system time in milliseconds when the event happened.
	 * @return Return the system time in milliseconds
	 */
	public final long getTimestamp() {
		return this.timestamp;
	}

}