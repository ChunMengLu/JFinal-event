package net.dreamlu.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * 自己实现的ConcurrentMultiMap 重复key的map，使用监听的type，取出所有的监听器
 *
 * @author L.cm
 * email: 596392912@qq.com
 * site:http://www.dreamlu.net
 * date 2015年6月25日下午8:36:17
 */
public class ConcurrentMultiMap<K, V> {
	private transient final ConcurrentMap<K, List<V>> map;

	public ConcurrentMultiMap() {
		map = new ConcurrentHashMap<>();
	}

	private List<V> createlist() {
		return new ArrayList<>();
	}

	/**
	 * put to ConcurrentMultiMap
	 *
	 * @param key   键
	 * @param value 值
	 * @return boolean
	 */
	public boolean put(K key, V value) {
		List<V> list = map.get(key);
		if (list == null) {
			list = createlist();
			if (list.add(value)) {
				map.put(key, list);
				return true;
			} else {
				throw new AssertionError("New list violated the list spec");
			}
		} else if (list.add(value)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * put list to ConcurrentMultiMap
	 *
	 * @param key  键
	 * @param list 值列表
	 * @return boolean
	 */
	public boolean putAll(K key, List<V> list) {
		if (list == null) {
			return false;
		} else {
			map.put(key, list);
			return true;
		}
	}

	/**
	 * get List by key
	 *
	 * @param key 键
	 * @return List
	 */
	public List<V> get(K key) {
		return map.get(key);
	}

	/**
	 * get List by key
	 *
	 * @param key          key
	 * @param defaultValue 默认值
	 * @return List
	 */
	public List<V> getOrDefault(K key, List<V> defaultValue) {
		List<V> list;
		return (((list = map.get(key)) != null) || map.containsKey(key))
			? list
			: defaultValue;
	}

	/**
	 * return keySet
	 *
	 * @return Set
	 */
	public Set<K> keySet() {
		return map.keySet();
	}

	/**
	 * clear ConcurrentMultiMap
	 */
	public void clear() {
		map.clear();
	}

	/**
	 * return isEmpty
	 *
	 * @return boolean
	 */
	public boolean isEmpty() {
		return map.isEmpty();
	}

	/**
	 * computeIfAbsent
	 *
	 * @param key             key
	 * @param mappingFunction fun
	 * @return List
	 */
	public List<V> computeIfAbsent(K key, Function<? super K, ? extends List<V>> mappingFunction) {
		return map.computeIfAbsent(key, mappingFunction);
	}

}
