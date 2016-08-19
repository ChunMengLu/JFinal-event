package net.dreamlu.event;

import java.lang.reflect.Type;

/**
 * 事件类型
 * @author L.cm
 * email: 596392912@qq.com
 * site:http://www.dreamlu.net
 * date 2016年8月19日下午9:58:53
 */
public class EventType {
	/**
	 * 默认的tag
	 */
	public static final String DEFAULT_TAG = "default_tag";
	
	/**
	 * 事件的tag
	 */
	private String tag = DEFAULT_TAG;
	
	/**
	 * 事件class type
	 */
	private final Type type;

	public EventType(String tag, Type type) {
		this.tag = tag;
		this.type = type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tag == null) ? 0 : tag.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EventType other = (EventType) obj;
		if (tag == null) {
			if (other.tag != null)
				return false;
		} else if (!tag.equals(other.tag))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
}
